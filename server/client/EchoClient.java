

import java.io.*;
import java.net.*;

public class EchoClient {
	
	public static int number;
	public static int game = -1;
	
    public static void main(String[] args) throws IOException {
        
        if (args.length != 0) {
            System.err.println(
                "Usage: java EchoClient <host name> <port number>");
            System.exit(1);
        }

        String hostName = "52.26.110.247";
        int portNumber = 26656;

        try (
            Socket echoSocket = new Socket(hostName, portNumber);
            PrintWriter out =
                new PrintWriter(echoSocket.getOutputStream(), true);
        	InputStream input = echoSocket.getInputStream();
            BufferedReader in =
                new BufferedReader(
                    new InputStreamReader(input));
            BufferedReader stdIn =
                new BufferedReader(
                    new InputStreamReader(System.in))
        ) {
        	number = Integer.parseInt(in.readLine());
		System.out.println(number);
        	Receive receive = new Receive(input, in);
        	receive.start();
            String userInput;
            while ((userInput = stdIn.readLine()) != null) {
                out.println(userInput);
            	if(userInput.equals("exit")){
            		receive.run = false;
            		break;
            	} else if(userInput.startsWith("game")){
			game = Integer.parseInt(userInput.substring(5));
			switch(game){
			case 0:
				System.out.println("picked up game 0");
				break;
			default:
				System.out.println("Unidentified game: " + game);
				break;
			}
		}
            }
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                hostName);
            System.exit(1);
        } 
    }
}
