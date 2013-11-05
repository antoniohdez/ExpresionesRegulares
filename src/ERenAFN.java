import java.util.Hashtable;
import java.util.Vector;

public class ERenAFN {

	private final String[] operadores = {"*", "+", "#", ","};
	private final String[] alfabeto = {" ", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "ñ", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "Ñ", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
	private AFN_E automata;
	private int numEstados = 0;
	
	public ERenAFN(String ER){
		automata = new AFN_E();
		AFN_E tmp = new AFN_E();
		this.automata = crearAutomata(String.valueOf(ER.charAt(0)));
		for(int i = 1; i < ER.length(); i++){
			
			if(ER.charAt(i) == '*'){
				this.automata.estrella();
				this.numEstados += 2;
			}
			else if(ER.charAt(i) == '+'){
				this.automata.positiva();
				this.numEstados++;
			}
			else if(ER.charAt(i) == '#'){//Concatena
				//tmp.imprimeAutomata();
				this.automata.concatenacion(tmp);
				tmp = new AFN_E();
			}
			else if(ER.charAt(i) == ','){//Union
				this.automata.union(tmp);
				tmp = new AFN_E();
				this.numEstados += 2;
			}
			else if(ER.charAt(i) == '.'){//Caso especial de caracteres de entreda (varios que pueden ser aleatorios)
				tmp = crearAutomataPunto();
				this.numEstados++;
				if(ER.charAt(i+1) == '+'){
					tmp.positiva();
					i+=1;
					
				}
			}
			else if(ER.charAt(i) == 'ε'){
				tmp = crearAutomataE();
			}
			else{
				tmp = crearAutomata(String.valueOf(ER.charAt(i)));
			}
		}
		this.automata.imprimeAutomata();
		this.AFNEaAFN();
	}
	
	public AFN_E crearAutomataPunto(){
		AFN_E af = new AFN_E();
		Hashtable<String, Vector<String>> htmp = new Hashtable<String, Vector<String>>();
		Vector<String> estados = new Vector<String>();
		estados.add(String.valueOf(this.numEstados+1));
		for(int i = 0; i < this.alfabeto.length; i++){
			htmp.put(this.alfabeto[i], estados);
		}
		
		af.addState(String.valueOf(this.numEstados++), htmp, false);
		
		af.addState(String.valueOf(this.numEstados++), new Hashtable<String, Vector<String>>(), true);
		return af;
	}
	
	public AFN_E crearAutomataE(){
		AFN_E af = new AFN_E();
		Hashtable<String, Vector<String>> htmp;
		Vector<String> estados;
		
		//Si es el ultimo estado a agregar se agrega el estado y se agrega un estado extra que a donde lleva la transicion del ultimo agregado
		estados = new Vector<String>();
		estados.add(String.valueOf(this.numEstados));
		
		af.addState(String.valueOf(this.numEstados++), new Hashtable<String, Vector<String>>(), true);
		
		return af;
	}
	
	public AFN_E crearAutomata(String cadena){
		AFN_E af = new AFN_E();
		Hashtable<String, Vector<String>> htmp;
		Vector<String> estados;
		htmp  = new Hashtable<String, Vector<String>>();
		//Si es el ultimo estado a agregar se agrega el estado y se agrega un estado extra que a donde lleva la transicion del ultimo agregado
		estados = new Vector<String>();
		estados.add(String.valueOf(this.numEstados+1));
		htmp.put(cadena, estados);
		af.addState(String.valueOf(this.numEstados++), htmp, false);
		af.addState(String.valueOf(this.numEstados++), new Hashtable<String, Vector<String>>(), true);

		return af;
	}
	
	public AFN_E AFNEaAFN(){
		Hashtable<String, Vector<String>> clausuras = new Hashtable<String, Vector<String>>();
		Vector<State> estados = this.automata.getAutomata();
		Vector<String> clausura;
		
		for(int i = 0; i <= this.numEstados; i++){
			clausura = new Vector<String>();
			this.getClausura(estados, clausura, String.valueOf(i), false);//El false es para la llamada recursiva, si se coloca un true entonces no se agrega a si mismo a la clausura
			clausuras.put(String.valueOf(i), clausura);
			System.out.println("Calusura de: " + String.valueOf(i) + " " +clausura);
		}
		
		return null;
	}
	
	public boolean existeEstado(Vector<Vector<String>> estados, Vector<String> simbolo){
		boolean bandera = true;
		boolean retorno = false;
		//Elimina repetidos
		eliminaRepetidos(simbolo);
		Vector<Vector<String>> tmpEstados = new Vector<Vector<String>>();
		Vector<String> tmpSimbolo = new Vector<String>();
		for(int i = 0; i < estados.size(); i++){
			tmpEstados.add(new Vector<String>());
			for(int j = 0; j < estados.elementAt(i).size(); j++){
				tmpEstados.elementAt(i).add(estados.elementAt(i).elementAt(j));
			}
		}
		return bandera;
	}
	
	public void eliminaRepetidos(Vector<String> simbolo){
		for(int i = 0; i < simbolo.size(); i++){
			for(int j = i+1; j < simbolo.size(); j++){
				if(simbolo.elementAt(i).compareTo(simbolo.elementAt(j)) == 0){
					simbolo.remove(j);
					j--;
				}
			}
		}
	}
	
	public void getClausura(Vector<State> estados, Vector<String> clausura, String estado, boolean r){
		
		Vector<String> tmp = new Vector<String>();
		for(int i = 0; i < estados.size(); i++){
			if(estados.get(i).getEstado().compareTo(estado) == 0){
				if(!r)
					clausura.add(estados.get(i).getEstado());
				tmp = new Vector<String>();
				tmp = estados.get(i).getTransiciones().get("ε");
				if(tmp == null) break;
				//Sacar el estado a donde manda la transicion e
				
				for(int j = 0; j < tmp.size(); j++){
					clausura.add(tmp.elementAt(j));
					this.getClausura(estados, clausura, tmp.elementAt(j), true);
				}
				//estados.get(i).getTransiciones().get(estado);
				break;
			}
			//estados.get(i).getTransiciones().get("ε");
		}
		
	}
	
	
	public static void main(String[] args) {
		
		ERenAFN a = new ERenAFN("pa#d#r#e#ε,.+#w#w#w#.+#c#o#m#");
		//ERenAFN a = new ERenAFN("ab#.+#");
		//a.crearAutomataPorPalabra("pa#d#r#e#ε,.+#w#w#w#.+#c#o#m#");
		
		

	}

}
