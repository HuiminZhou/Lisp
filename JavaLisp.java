import java.io.*;
import java.util.*;
import java.beans.*;
import java.awt.*;

class JavaLisp {
	private static String str;
	private static Stack<String> stack = new Stack<String>();
	
	private static enum Primitive{
		NIL,
		T,
		SYMBOL,
		NUM,
		CONS,
		LIST
	};
	
	public static class LinkedList{
		public Primitive type;
		public Object car;
		public LinkedList cdr;
		LinkedList(Primitive type, Object car, LinkedList cdr){
			this.type = type;
			this.car = car;
			this.cdr = cdr;
		}
		public String val(){
			return "" + car;
		}
	}
	
	// global table
	// HashMap<String, LinkedList> map = new HashMap<String, LinkedList>();
	
	public static void main(String[] args) {
		System.out.print("> ");
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		char[] buf = new char[1024];
		try {
			while(in.read(buf, 0, 1024) != -1){
				str = (new String(buf)).toUpperCase();
				Object expression = SExpression();
				Object evaluate = Evaluate((LinkedList)expression);
				//System.out.print(evaluate);
				Print(evaluate);
				System.out.print("\n> ");
			}
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static Object SExpression(){
		return Parse();
	}
	
	public static Object Parse(){
		Object token = GetToken();
		if (token == null){
			return null;
		} else if (token.equals("(")){
			return NextParse();
		} else if(token.equals(")")){
			return null;
		} else if(token.equals("T")){
			return new LinkedList(Primitive.T, null, null);
		} else if(token.equals("NIL")){
			return new LinkedList(Primitive.NIL, null, null);
		} else{
			String res = (String)token;
			if(res.length() == 0){
				return null;
			}
			if (Character.isDigit(res.charAt(0))){
				return new LinkedList(Primitive.NUM, token, null);
			} else{
				return new LinkedList(Primitive.SYMBOL, token, null);
			}
		}
	}
	
	public static LinkedList NextParse(){
		Object token = GetToken();
		if (token == null){
			return null;
		} else if (token.toString().equals(")")){
			return null;
		} else if(token.toString().equals(".")){
			return (LinkedList)Parse();
		} else if(token.toString().equals("CONS")){
			return new LinkedList(Primitive.CONS, Parse(), NextParse());
		} else{
			stack.push(token.toString());
			return new LinkedList(Primitive.LIST, Parse(), NextParse());
		}
	}

	public static Object GetToken(){
		if (!stack.isEmpty()){
			return stack.pop();
		}
		if (str == null || str.length() < 1 || str.equals("\n")){
			return null;
		}
		int len = str.length(), i = 0;
		while (str.charAt(i) == ' '){
			i++;
		}
		StringBuffer sb = new StringBuffer("");
		if (str.charAt(i) == '(' || str.charAt(i) == ')' ){
			sb.append(str.charAt(i));
			str = str.substring(i + 1);
			return sb.toString();
		}
		for (; i < len; ++i){
			char c = str.charAt(i);
			if (c == ')' || c == ' ' || c == '\n') {
				break;
			}
			sb.append(str.charAt(i));
		}
		str = str.substring(i);
		return sb.toString();
	}
	
	public static LinkedList ReLink(LinkedList obj){
		if (obj == null){
			return null;
		} else{
			return new LinkedList(Primitive.LIST, Evaluate(obj.car), ReLink(obj.cdr));
		}
	}
	
	public static Object Evaluate(Object val){
		LinkedList obj = (LinkedList) val;
		if (obj == null) {
			return null;
		}

		switch (obj.type) {
			case NIL:
				return new LinkedList(Primitive.NIL, null, null);
			case T:
				return new LinkedList(Primitive.T, null, null);
			case NUM:
				return obj.car;
			case SYMBOL:
				return obj.car;
			case CONS:
				return new LinkedList(Primitive.CONS, ((LinkedList)(obj.car)).car, (LinkedList)(obj.cdr.car));
				// return new LinkedList(Primitive.CONS, ((LinkedList) obj.car).car, (LinkedList)((LinkedList)(obj.cdr.car)).car);
			case LIST:
				return Compute(Evaluate(obj.car), ReLink(obj.cdr));
		}
		return obj;
	}
	
	public static Object Compute(Object operation, LinkedList list){
		int car = Integer.parseInt(list.car.toString());
		int cdr = Integer.parseInt(list.cdr.car.toString());
		switch (operation.toString().charAt(0)) {
			case '+':
				//System.out.print(car + cdr);
				return (Object) (car + cdr);
			case '-':
				return (Object) (car - cdr);
			case '*':
				return (Object) (car * cdr);
			case '/':
				if (cdr != 0){
					return (Object) (car / cdr);
				} else{
					throw new IllegalArgumentException("Divisor is 0");
				}
			default:
				throw new IllegalArgumentException("Invalid Operation");
		}
	}
	
	public static void Print(Object obj){
		if (obj == null){
			return;
		}
		
		if(Character.isDigit(obj.toString().charAt(0)) || obj.toString().charAt(0) == '-'){
			System.out.print(obj);
			return;
		}
		
		if (obj.toString().indexOf("List") < 0) {
			System.out.print(obj + "\n");
			throw new IllegalArgumentException("Unbounded Value");
		}
		
		LinkedList list = (LinkedList)obj;
		//System.out.print(obj);
		switch (list.type) {
			case NUM:
				System.out.print(list.car);
				break;
			case T:
				System.out.print("T");
				break;
			case NIL:
				System.out.print("NIL");
				break;
			case CONS:
				System.out.print("(" + list.car + " . " + list.cdr.car + ")");
				break;
			default:
				break;
		}
	}
}