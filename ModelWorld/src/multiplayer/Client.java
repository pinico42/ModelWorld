package multiplayer;

import java.io.*;
import java.net.*;

public class Client {
	
	public static boolean alive = false;
	public static int number;
	public static int game = -1;
	public static PrintWriter out;
	
    public static void main(String[] args) throws IOException, Exception, UnknownHostException {

        String hostName = "benhack.ddns.net";
        int portNumber = 26656;

        try {
            Socket echoSocket = new Socket(hostName, portNumber);
            out = new PrintWriter(echoSocket.getOutputStream(), true);
    	    InputStream input = echoSocket.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            //BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        	number = Integer.parseInt(in.readLine());
        	Receive receive = new Receive(input, in);
        	receive.start();
        	out.println("game 1");
        	System.out.println("Game selected");
            /*String userInput;
            while ((userInput = stdIn.readLine()) != null) {
            	if(game == -1){
            		out.println(userInput);
            	} else {	
            		if(userInput.equals("game stop")){
            			game = -1;
            			System.out.println("Stopping game...");	
            			out.println("STOP");
            			continue;
            		}
            		switch(game){	
            		case 0:
            			out.println(userInput);
            			break;
            		case 1:
            			out.println(userInput);
            			break;
            		default:
            			System.out.println("ERR: NO SUCH GAME!");
            			break;
            		}
            	}
            	if(userInput.equals("exit")){
            		receive.run = false;
            		break;
            	} else if(userInput.startsWith("game")){
            		game = Integer.parseInt(userInput.substring(5));
            		switch(game){
            		case 0:
            			System.out.println("picked up game 0");
            			break;
            		case 1:
            			System.out.println("picked up game 1");
            			break;
            		default:
            			System.out.println("Unidentified game: " + game);
            			break;
            		}
            	}
            }*/
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            //System.exit(1);
        }
    }
    
    public static void send(String string){
    	out.println(number+":"+string);
    }
    
}
