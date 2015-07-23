import java.io.BufferedReader;
import java.io.IOException;

public class Receive extends Thread{
	
	boolean run = true;
	
	BufferedReader in;
	
	public Receive(BufferedReader in){
		this.in = in;
	}
	
	public void run(){
		while(run){
            try {
            	String string = in.readLine();
            	try{
                	if(Integer.parseInt(string.substring(0, 1)) != Client.number){
                		System.out.println(string);
                	}
            	} catch(NumberFormatException e){
            		System.out.println(string);
            	}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}

