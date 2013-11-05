import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;


public class AFN_E {
	private State actual;
	private Vector<State> automata;
	
	public AFN_E(){
		this.actual = null;
		this.automata = new Vector<State>();
	}
	
	public void addState(String nombre, Hashtable<String, Vector<String>> transiciones, boolean esFinal){
		if(this.automata.size() == 0){
			//this.automata = new Vector<State>();
			this.automata.add(new State(nombre, transiciones, esFinal));
			this.actual = automata.firstElement();
		}
		else{
			this.automata.add(new State(nombre, transiciones, esFinal));
		}
	}
	
	public boolean runAFN(String cadena){
		Vector<String> estado = new Vector<String>();
		for(int i = 0; i < cadena.length(); i++){
			//Se obtiene el nombre del siguiente estado de acuerdo al caracter de entrada
			estado =  this.actual.getTransiciones().get(String.valueOf(cadena.charAt(i)));
			if(estado == null) return false;
			int tmp = 0;
			for(int j = 0; j < automata.size(); j++){
				if(tmp < estado.size() && estado.elementAt(tmp).compareTo(automata.elementAt(j).getEstado()) == 0){
					this.actual = this.automata.elementAt(j);
					if(runAFN(cadena.substring(i+1)))
						return true;
					else{
						tmp++;
						j = 0;
						i++;
						this.actual = this.automata.elementAt(j);
					}
				}
			}
		}
		return actual.getFinal();
	}
	
	public void concatenacion(AFN_E af){
		this.automata.lastElement().setFinal(false);
		this.automata.lastElement().addTransiciones("ε", af.automata.firstElement().getEstado());
		while(af.automata.size() > 0){
			this.automata.add(af.automata.firstElement());
			af.automata.remove(0);
		}
		
		this.imprimeAutomata();
	}
	
	public void union(AFN_E af){
		int n;
		//Se agrega un estado al inicio y sus transiciones
		n = Integer.parseInt(af.automata.lastElement().getEstado());
		Vector<String> estados = new Vector<String>();
		estados.add(this.automata.firstElement().getEstado());
		estados.add(af.automata.firstElement().getEstado());
		Hashtable<String, Vector<String>> htmp = new Hashtable<String, Vector<String>>();
		htmp.put("ε", estados);
		this.automata.add(0, new State(String.valueOf(n+1), htmp, false));
		
		//Se agrega una transicion de los antiguos finales al nuevo final
		this.automata.lastElement().setFinal(false);
		this.automata.lastElement().addTransiciones("ε", String.valueOf(n+2));
		af.automata.lastElement().setFinal(false);
		af.automata.lastElement().addTransiciones("ε", String.valueOf(n+2));
		
		//Copia los elementos de af a el automata de la clase
		while(af.automata.size() > 0){
			this.automata.add(af.automata.firstElement());
			af.automata.remove(0);
		}
		
		//Se agrega un nuevo estado final
		this.automata.add(new State(String.valueOf(n+2), new Hashtable<String, Vector<String>>(), true));
		
	}
	
	public void estrella(){
		int n;
		n = Integer.parseInt(this.automata.lastElement().getEstado());
		Vector<String> estados = new Vector<String>();
		//Se agrega transicion del final al inicial
		this.automata.lastElement().addTransiciones("ε", this.automata.firstElement().getEstado());
		//Se agrega el nuevo estado inicial
		estados.add(this.automata.firstElement().getEstado());
		Hashtable<String, Vector<String>> htmp = new Hashtable<String, Vector<String>>();
		htmp.put("ε", estados);
		this.automata.add(0, new State(String.valueOf(n+1), htmp, false));
		//Se agrega el nuevo estado final
		this.automata.lastElement().setFinal(false);
		this.automata.lastElement().addTransiciones("ε", String.valueOf(n+2));
		this.automata.add(new State(String.valueOf(n+2), new Hashtable<String, Vector<String>>(), true));
		//Se agrega la transicion del nuevo estao inicial al nueo final
		this.automata.firstElement().addTransiciones("ε", this.automata.lastElement().getEstado());
	}
	
	public void positiva(){
		int n;
		n = Integer.parseInt(this.automata.lastElement().getEstado());
		this.automata.lastElement().addTransiciones("ε", this.automata.firstElement().getEstado());
		this.automata.lastElement().addTransiciones("ε", String.valueOf(n+1));
		this.automata.lastElement().setFinal(false);
		
		this.automata.add(new State(String.valueOf(n+1), new Hashtable<String, Vector<String>>(), true));
		
	}
	
	public void imprimeAutomata(){
		for(int i = 0; i < this.automata.size(); i++){
			System.out.print(this.automata.elementAt(i).getEstado() + " ");
			Enumeration<Vector<String>> e = this.automata.elementAt(i).getTransiciones().elements();
			Enumeration<String> entradas = this.automata.elementAt(i).getTransiciones().keys();
			while(e.hasMoreElements()){
				System.out.print(entradas.nextElement() + " ");
				System.out.print(e.nextElement() + " ");
			}
			if(this.automata.elementAt(i).getFinal()) System.out.println("\tFinal");
			System.out.println();
			
		}
	}
	
	public Vector<State> getAutomata(){
		return this.automata;
	}

	public static void main(String[] args) {
		AFN_E a = new AFN_E();
		//System.out.println(a.runAFN("aabaab"));
		a.imprimeAutomata();
		
	}

}

class State{
	//private Vector<String> transiciones;
	private Hashtable<String,Vector<String>> transiciones;
	private String estado;
	private boolean esFinal;
	
	public State(String estado, Hashtable<String,Vector<String>> transiciones, boolean esFinal){
		this.estado = estado;
		this.transiciones = transiciones;
		this.esFinal = esFinal;
	}
	
	public Hashtable<String,Vector<String>> getTransiciones(){
		return transiciones;
	}
	
	public void addTransiciones(String t, String e){
		if(this.transiciones.get(t) == null){
			Vector<String> tmp = new Vector<String>();
			tmp.add(e);
			this.transiciones.put(t, tmp);
		}
		else{
			this.transiciones.get(t).add(e);
		}
	}
	
	public String getEstado(){
		return this.estado;
	}
	
	public void setEstado(String estado){
		this.estado = estado;
	}
	
	public boolean getFinal(){
		return this.esFinal;
	}
	
	public void setFinal(boolean esFinal){
		this.esFinal = esFinal;
	}
	
}