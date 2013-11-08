import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Stack;
import java.util.Vector;

public class ERaAFN {

	private final String[] operadores = {"*", "+", "#", ","};
	private final String[] alfabeto = {"á","é","í","ó","ú","Á","É","Í","Ó","Ú","1","2","3","4","5","6","7","8","9","0"," ", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "ñ", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "Ñ", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
	private AFN_E automata;
	private int numEstados = 0;
	
	public ERaAFN(String ER){
		automata = new AFN_E();
		AFN_E tmp = new AFN_E();
		Stack<AFN_E> a = new Stack<AFN_E>();
		this.automata = crearAutomata(String.valueOf(ER.charAt(0)));
		a.add(automata);
		
		for(int i = 1; i < ER.length(); i++){
			
			if(ER.charAt(i) == '*'){
				a.peek().estrella();
				//this.automata.estrella();
				this.numEstados += 2;
			}
			else if(ER.charAt(i) == '+'){
				a.peek().positiva();
				//this.automata.positiva();
				this.numEstados++;
			}
			else if(ER.charAt(i) == '#'){//Concatena
				//tmp.imprimeAutomata();
				tmp = a.pop();
				a.peek().concatenacion(tmp);
				//this.automata.concatenacion(tmp);
				tmp = new AFN_E();
			}
			else if(ER.charAt(i) == ','){//Union
				tmp = a.pop();
				a.peek().union(tmp);
				//this.automata.union(tmp);
				tmp = new AFN_E();
				this.numEstados += 2;
			}
			else if(ER.charAt(i) == '.'){//Caso especial de caracteres de entreda (varios que pueden ser aleatorios)
				a.push(crearAutomataPunto());
				//tmp = crearAutomataPunto();
				this.numEstados++;
				if(ER.charAt(i+1) == '+'){
					a.peek().positiva();
					//tmp.positiva();
					i+=1;
					
				}
			}
			else if(ER.charAt(i) == 'ε'){
				a.push(crearAutomata(String.valueOf(ER.charAt(i))));
				//tmp = crearAutomataE();
			}
			else{
				a.push(crearAutomata(String.valueOf(ER.charAt(i))));
				//tmp = crearAutomata(String.valueOf(ER.charAt(i)));
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
	public static void sort (Vector <String> array){
		String pilot;
		boolean sorted;
		int length=array.size()-1;
		do{
			sorted=false;
			for (int i=0;i<length;i++){
				if (Integer.parseInt(array.get(i))>Integer.parseInt(array.get(i+1))){
					pilot=array.get(i);
					array.setElementAt(array.get(i+1), i);
					array.setElementAt(pilot, i+1);
					sorted=true;
					
				}
			}
			length--;
		}while(sorted);

		
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
		this.automata.imprimeAutomata();
		for(int i = 0; i <= this.numEstados; i++){
			clausura = new Vector<String>();
			this.getClausura(estados, clausura, String.valueOf(i), false);//El false es para la llamada recursiva, si se coloca un true entonces no se agrega a si mismo a la clausura
			clausuras.put(String.valueOf(i), clausura);
			System.out.println("Clausura de: " + String.valueOf(i) + " " +clausura);
		}
		
		Vector<String> inicial = clausuras.get(String.valueOf(estados.get(0).getEstado())); //Obtiene el estado inicial
		sort(inicial);
		String actual = this.automata.transicionAString(inicial);
		AFN_E afn = new AFN_E(); //Nuevo automata a regresar
		Hashtable<String,Vector<String>> transClausurasHash=null;
		this.automata.imprimeAutomata();
		Vector<String> estadosNuevoAutomata = new Vector<String>(); //Vector bidimensional que contiene los nuevos estados del nuevo automata
		estadosNuevoAutomata.add(actual);
		int contadorNuevosEstados=0;
		int contadorEstadoActual=0;
		while(contadorEstadoActual<=contadorNuevosEstados){
			boolean esFinal=false;
			String newState = ""; //Crear nombre de nuevo estado
			actual="";
			actual = estadosNuevoAutomata.get(contadorEstadoActual);
			
			String [] estadosActual = actual.split(",");
			transClausurasHash = new Hashtable<String,Vector<String>>(); //HashTable para el nuevo estado
			for(int a=0;a<estadosActual.length;a++){
				String estado=estadosActual[a];
				newState += estado + ","; //Concatena nombre del nuevo estado
				int index = automata.buscarEstado(estado);
				Hashtable<String,Vector<String>> trans = estados.get(index).getTransiciones();//Obtiene las transiciones del estado revisado
				esFinal=automata.buscarFinal(estado);
				//System.out.println(newState);
				Enumeration<String> key = trans.keys(); //Obtiene los caracteres a los que va el estado revisado
				//System.out.println("Estado "+estado + " Tiene transicion con los caracteres: "+ trans.keySet());
				while(key.hasMoreElements()){ //Recorre todos los caracteres a los que va el estado revisado
					String caracter = key.nextElement();
					//System.out.println("Estado: "+estado+" Caracter actual: "+caracter + " Va a estados: "+edos);
					Vector<String> newTransitions=new Vector<String>(); //Nuevas transiciones que tendra el nuevo estado
					if(caracter!="ε"){ //Si el caracter actual es diferente de epsilon, procede a obtener sus clausuras de epsilon
						Vector<String> transClausuras = trans.get(caracter); //Obtiene las transiciones de ese caracter
						
						for (int i=0;i<transClausuras.size();i++){
							Vector<String> sacaClausuras = clausuras.get(transClausuras.get(i)); //Obtiene la clausura del estado
							//String estadoClausuras ="";
							for(int j=0;j<sacaClausuras.size();j++){
								newTransitions.add(sacaClausuras.get(j)); //Agrega la clausura del estado al vector de las nuevas transiciones
								//estadoClausuras+=sacaClausuras.get(j)+","; //Agrega la clausura del estado al vector de las nuevas transiciones
							}
							//newTransitions.add(estadoClausuras.substring(0, estadoClausuras.length()-1));
						}
						this.eliminaRepetidos(newTransitions);
						
						sort(newTransitions);
						Vector<String> chequeoCaracterExistente = transClausurasHash.get(caracter);
						if(chequeoCaracterExistente!=null){
							//System.out.println("Hay un caracter que ya existe en la tablaHash "+caracter);
							for(String estadoAgregarCaracter : newTransitions){
								if(!chequeoCaracterExistente.contains(estadoAgregarCaracter)){
									chequeoCaracterExistente.add(estadoAgregarCaracter);
									//System.out.println(chequeoCaracterExistente+" No contiene "+estadoAgregarCaracter);
								}
							}
							
							newTransitions = transClausurasHash.get(caracter);
							sort(newTransitions);
							//System.out.println("Caracter : "+caracter+" Nueva transicion: "+newTransitions);
						}
						else {
							transClausurasHash.put(caracter, newTransitions);  //agrega las nuevas transiciones del caracter a la tabla hash que obtendra el nuevo estado
						}
						boolean agregable = true;
						
						for (String e : estadosNuevoAutomata){
							if(e.equals(this.automata.transicionAString(newTransitions))){
								agregable = false;
							}
						}
						if(agregable){
							sort(newTransitions);
							estadosNuevoAutomata.add(this.automata.transicionAString(newTransitions));
							//System.out.println(newTransitions + " Fue agregado");
							contadorNuevosEstados++;
						}
						newTransitions=new Vector<String>();
					}
				}
				
				
			}
			newState=newState.substring(0, newState.length()-1);
			afn.addState(newState, transClausurasHash, esFinal);
			contadorEstadoActual++;		
			/*System.out.println("Imprimir automata");
			afn.imprimeAutomata();
			System.out.println("Imprimir estados so far "+estadosNuevoAutomata);*/
			
		}
		afn.imprimeAutomata();
		System.out.println();
		System.out.println(afn.runAFN("010101010101011"));
		return afn;
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
	if(!isInVector(clausura, estado)){
		clausura.add(tmp.elementAt(j));
		this.getClausura(estados, clausura, tmp.elementAt(j), true);
	}
}
				//estados.get(i).getTransiciones().get(estado);
				break;
			}
			//estados.get(i).getTransiciones().get("ε");
		}
		
	}
	
boolean isInVector(Vector<String> clausura, String estado){
	for(String e : clausura){
		if(e.compareTo(estado) == 0){
			return true;
		}
	}
	return false;
}
	
	public static void main(String[] args) {
		//Epsilon de verdad: ε
		PostFix convertir = new PostFix("(01)*(ε,0)+(10)*(ε,1)");
		System.out.println(convertir.getResult());
		String exp = convertir.getResult();
		ERaAFN a = new ERaAFN(exp);
		//ERenAFN a = new ERenAFN("ab#.+#");
		//a.crearAutomataPorPalabra("pa#d#r#e#ε,.+#w#w#w#.+#c#o#m#");
		
		

	}

}
