import java.util.Stack;


public class PostFix {

	private Stack<Character> stack;
	private String result;
	
	
	public PostFix(String exp) {
		this.stack = new Stack<Character>();
		this.result=(this.convert(exp));
	}
	
	public Stack getstack(){
		return this.stack;
	}
	public String getResult(){
		return this.result;
	}
	
	private int opPriority(char operator){
		if(operator == ','){ 
			return 0;
		}
		else if(operator == '#'){
			return 1;
		}
		else if(operator == '+'){ 
			return 2;
		}
		if(operator == '*'){ 
			return 3;
		}
		else{
			return -1;
		}
	}
	
	
	private String convert(String exp) {
		
		System.out.println(exp);
		
		String expression = ""; 
		char act, act1;
		for(int i = 0; i < exp.length()-1; i++){
			act = exp.charAt(i);
			act1 = exp.charAt(i+1);
			if((act != ',' && act != '(') && (act1 != '+' && act1 != '*' && act1 != ',' && act1 != ')')){
				expression = expression + act + '#';
			}
			else{
				expression = expression + act;
			}
			
		}
		expression = expression + exp.charAt(exp.length() - 1);
		System.out.println(expression);
		
		
		String postFix = "";
		char symbol;
		
		for(int i = 0; i < expression.length();i++){
			symbol = expression.charAt(i);
			
			if(symbol == '(' || symbol == ')' || symbol == ',' || symbol == '#'){ 
				if(this.stack.isEmpty() || ( symbol == '(' || (this.opPriority(symbol) > this.opPriority(this.stack.peek()) )) ){ 
					this.stack.push(symbol);
				}
				else if(symbol == ')'){
					while(this.stack.peek() != '('){ 
						postFix = postFix + this.stack.pop();
					}
					this.stack.pop();
				}
				else{
					while(!this.stack.isEmpty() && (opPriority(symbol) <= opPriority(this.stack.peek()))){ 
						postFix = postFix + this.stack.pop();
					}
					this.stack.push(symbol);
				}	
			}
			else if(Character.isLetter(symbol) || symbol == '\n' || symbol == ' ' || Character.isDigit(symbol) || symbol == '.' || symbol == '+'|| symbol == '*'){ 
				postFix = postFix + symbol;
			}
		}
		while(!this.stack.isEmpty()){
			if(this.stack.peek() == '('){
				return "No se encontr�� el par��ntesis que cierra";
			}
			postFix = postFix + stack.pop();
		}
		
		return postFix;
	}
	
	public static void main(String[] args) {
		
		PostFix convertir = new PostFix("(padre,ε)(.)+www(.)+com");
		//PostFix convertir = new PostFix("(padre,ε)");
		System.out.println(convertir.getResult());
		ERenAFN a = new ERenAFN(convertir.getResult());

	}

}
