import java.util.Hashtable;
import java.util.Vector;

public class ERenAFN {

	//Las comillas vacias representa concatenación (No es necesario ponerlo).
	private final String[] operadores = {"*", "+", "#", ","};
	private final String[] alfabeto = {" ", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "ñ", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "Ñ", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
	private AFN_E automata;
	private int numEstados = 0;
	
	//"pa#d#r#e#ε,.+#w#w#w#.+#c#o#m#"
	public ERenAFN(String ER){
		automata = new AFN_E();
		AFN_E tmp = new AFN_E();
		this.automata = crearAutomata(String.valueOf(ER.charAt(0)));
		for(int i = 1; i < 4; i++){
			
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
				if(ER.charAt(i+1) == '+'){
					tmp.positiva();
					i++;
				}
			}
			else if(ER.charAt(i) == 'ε'){
				tmp = crearAutomataE();
			}
			else{
				tmp = crearAutomata(String.valueOf(ER.charAt(i)));
			}
		}
	}
	
	public AFN_E crearAutomataPunto(){
		AFN_E af = new AFN_E();
		Hashtable<String, Vector<String>> htmp = new Hashtable<String, Vector<String>>();
		Vector<String> estados = new Vector<String>();
		estados.add(String.valueOf(this.numEstados+1));
		for(int i = 0; i < this.alfabeto.length; i++){
			htmp.put(this.alfabeto[i], estados);
		}
		
		af.addState(String.valueOf(this.numEstados++), htmp, true);
		
		af.addState(String.valueOf(this.numEstados++), new Hashtable<String, Vector<String>>(), true);
		af.imprimeAutomata();
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
		
		af.imprimeAutomata();
		System.out.println();
		
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
		af.imprimeAutomata();
		System.out.println();
		return af;
	}
	
	public static void main(String[] args) {
		
		//ERenAFN a = new ERenAFN("pa#d#r#e#ε,.+#w#w#w#.+#c#o#m#");
		ERenAFN a = new ERenAFN("p.+,");
		//a.crearAutomataPorPalabra("pa#d#r#e#ε,.+#w#w#w#.+#c#o#m#");
		
		

	}

}
