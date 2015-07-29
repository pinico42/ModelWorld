package multiplayer;

import java.io.*;
import java.net.*;
import java.util.HashMap;

public class Client {
	
	public static boolean alive = false;
	public static int number;
	public static int game = -1;
	public static PrintWriter out;
	
    public static void main(HashMap<String, String> msettings) throws IOException, Exception, UnknownHostException {

        String hostName = msettings.get("host");
        int portNumber = Integer.parseInt(msettings.get("port"));

        try {
            Socket echoSocket = new Socket(hostName, portNumber);
            out = new PrintWriter(echoSocket.getOutputStream(), true);
    	    InputStream input = echoSocket.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
        	number = Integer.parseInt(in.readLine());
        	Receive receive = new Receive(input, in);
        	receive.start();
        	out.println("game 1");
        	System.out.println("Game selected");
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            //System.exit(1);
        }
    }
    
    public static void closeConnection(){
    	out.println("exit");
    }
    
    public static void send(int protocol, String string){
    	out.println(protocol+":"+string);
    }
    
}
