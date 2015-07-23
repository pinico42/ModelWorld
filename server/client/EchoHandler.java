import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class EchoHandler extends Thread{
	
	public static int counter = 0;
	public static ArrayList<PrintWriter> outs = new ArrayList<PrintWriter>();
	
	public int number;
	public PrintWriter out;
	public BufferedReader in;
	public Socket socket;
	
	public EchoHandler(Socket clientSocket){
		
		number = counter;
		counter++;
		
		this.socket = clientSocket;
        try {
        	out = new PrintWriter(clientSocket.getOutputStream(), true);
			outs.add(out);
		} catch (IOException e) {
			e.printStackTrace();
		}                   
        try {
			in = new BufferedReader(
			    new InputStreamReader(clientSocket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        out.println(number);
        
	}
	
	public void run() {
        System.out.println("Hello from a thread!");
        String inputLine;
        try {
			while ((inputLine = in.readLine()) != null) {
			    if(inputLine.equals("help")){
			    	out.println("The commands are: \n 'help' \n 'exit'");
			    } else if(inputLine.equals("exit")){
			    	out.println("Exiting");
					System.out.println(number + " thread has had a disconnection");
					outs.remove(out);
					out.close();
					break;
			    } else {
				    write(number + ": " + inputLine);
			    }
			    System.out.println(inputLine);
			}
		} catch (IOException e) {
			System.out.println(number + " thread has had a disconnection");
			outs.remove(out);
			out.close();
		}
    }
	
	public static void write(String string){
		for(PrintWriter out: outs){
			out.println(string);
		}
	}

    public static void main(String args[]) {
        (new EchoHandler(null)).start();
    }
	
}
