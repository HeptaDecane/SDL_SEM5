import java.io.*; 
  
public class ConsolePin{ 
    public static void main(String[] args) {
        Console console = System.console();
  
		String password = null;
		try{
        	char[] ch = console.readPassword("Enter password: ");
  			password = new String(ch);
  		}catch(Exception e){
  			System.out.println(e);
  		}
        System.out.println("Password: "+password);
    } 
} 
