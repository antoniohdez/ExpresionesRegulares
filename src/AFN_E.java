import java.util.Hashtable;
import java.util.Vector;


public class AFN_E {
	private state actual;
	private Vector<state> automata;
	
	public AFN_E(){
		this.automata = new Vector<state>();
		/////
		Hashtable<String, Vector<String>> htmp = new Hashtable<String, Vector<String>>();
		Vector<String> estados = new Vector<String>();
		estados.add("q1");
		htmp.put("a", estados);
		state tmp = new state("q0", htmp, false);
		this.automata.add(tmp);
		
		htmp = new Hashtable<String, Vector<String>>();
		estados = new Vector<String>();
		estados.add("q1");
		htmp.put("a", estados);
		estados = new Vector<String>();
		estados.add("q2");
		estados.add("q0");
		htmp.put("b", estados);
		tmp = new state("q1", htmp, false);
		this.automata.add(tmp);
		
		tmp = new state("q2", new Hashtable<String, Vector<String>>(), true);
		this.automata.add(tmp);
		
		this.actual = automata.firstElement();
		
		
		
		
	}
	
	public boolean runAFN(String cadena){
		Vector<String> estado = new Vector<String>();
		for(int i = 0; i < cadena.length(); i++){
			//Se obtiene el nombre del siguiente estado de acuerdo al caracter de entrada
			estado =  this.actual.getTransiciones().get(String.valueOf(cadena.charAt(i)));
			if(estado == null) return false;
			System.out.println(this.actual.getEstado());
			System.out.println(cadena.charAt(0));
			int tmp = 0;
			for(int j = 0; j < automata.size(); j++){
				if(tmp < estado.size() && estado.elementAt(tmp).compareTo(automata.elementAt(j).getEstado()) == 0){
					this.actual = this.automata.elementAt(j);
					if(runAFN(cadena.substring(i+1))) return true;
					else{
						
						tmp++;
						j = 0;
						i++;
						this.actual = this.automata.elementAt(j);
					}
				}
				else{
					
				}
			}
		}
		return actual.getFinal();
	}

	public static void main(String[] args) {
		AFN_E a = new AFN_E();
		System.out.println(a.runAFN("aababab"));
		
	}

}

class state{
	//private Vector<String> transiciones;
	private Hashtable<String,Vector<String>> transiciones;
	private String estado;
	private boolean esFinal;
	
	public state(String estado, Hashtable<String,Vector<String>> transiciones, boolean esFinal){
		this.estado = estado;
		this.transiciones = transiciones;
		this.esFinal = esFinal;
	}
	
	public Hashtable<String,Vector<String>> getTransiciones(){
		return transiciones;
	}
	
	public String getEstado(){
		return this.estado;
	}
	
	public boolean getFinal(){
		return this.esFinal;
	}
	
}