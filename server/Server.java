import java.net.*;
import java.util.ArrayList;
import java.io.*;
import java.util.Scanner;

public class Server extends Thread {
	
	public static boolean run = true;
	
	public static void main(String[] args) throws IOException {
        	
		System.out.println("Starting server...");
		
		Server server = new Server();
		server.start();
		
		System.out.println("Setting up game classes...");
		
		Echo.main(null);
		Model.main(null);
		
		System.out.println("Game classes set up");
		
        	int portNumber = 26656;
        	
        	ArrayList<Handler> echos = new ArrayList<Handler>();
        	int i = 0;
        	
        	while(run){
        	    
        	    try (
        	        ServerSocket serverSocket =
        	            new ServerSocket(portNumber);
        	    ) {
			System.out.println("Server socket started.");
        	    	echos.add(new Handler(serverSocket.accept()));
        	    	echos.get(i).start();
        	    	i++;
        	    } catch (IOException e) {
        	        System.out.println("Exception caught when trying to listen on port "
        	            + portNumber + " or listening for a connection");
        	        System.out.println(e.getMessage());
        	    }
        	    
        	}
    	}
	
	public Server(){}
	
	public void run(){
		Scanner scan = new Scanner(System.in);
		scan.nextLine();
		Server.end();
	}
	
	public static void end(){
		Handler.write("Server stopping, bye.");
		run = false;
		System.out.println("Stopping server!");
		System.exit(0);
	}
	
}
