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
	public int buscarEstado(String nombre){
		for(State estado:automata){
			if(estado.getEstado().equals(nombre)){
				return automata.indexOf(estado);
			}
		}
		return -1;
	}
	public void restartActual(){
		this.actual=this.automata.elementAt(0);
	}
	public boolean buscarFinal(String nombre){
		boolean esFinal = false;
		for(State estado:automata){
			if(estado.getEstado().equals(nombre)){
				esFinal =  estado.getFinal();
			}
		}
		return esFinal;
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
	public void addState(State estado){
		this.automata.add(estado);
	}

	public boolean runAFN(String cadena){
		return runAFN(cadena,0,0);
	}
	public boolean runAFN(String input, int index, int state ){
		if(index==input.length()){
			return this.automata.get(state).getFinal();
		}
		else {
			String estado = this.transicionAString(this.automata.get(state).getTransiciones().get(String.valueOf(input.charAt(index))));;
			if(estado==null) return false;
			//System.out.println(estado +" posicion "+this.buscarEstado(estado));
			return runAFN(input,index+1,this.buscarEstado(estado));	
			
		}
	}
	public String transicionAString(Vector<String> transicion){
		String transicionString="";
		if(transicion!=null){
			for(String estado : transicion){
				transicionString+=estado+",";
			}
			return transicionString.substring(0,transicionString.length()-1);
		}
		return null;

	}
	public void concatenacion(AFN_E af){
		this.automata.lastElement().setFinal(false);
		this.automata.lastElement().addTransiciones("ε", af.automata.firstElement().getEstado());
		while(af.automata.size() > 0){
			this.automata.add(af.automata.firstElement());
			af.automata.remove(0);
		}

		//this.imprimeAutomata();
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