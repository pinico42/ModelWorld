import java.net.*;
import java.util.ArrayList;
import java.io.*;

public class EchoServer {
    public static void main(String[] args) throws IOException {
        
        if (args.length != 0) {
            System.err.println("Usage: java EchoServer <port number>");
            System.exit(1);
        }
        
        int portNumber = 26656;
        
        ArrayList<EchoHandler> echos = new ArrayList<EchoHandler>();
        int i = 0;
        
        while(true){
            
            try (
                ServerSocket serverSocket =
                    new ServerSocket(portNumber);
            ) {
            	echos.add(new EchoHandler(serverSocket.accept()));
            	echos.get(i).start();
            	i++;
            } catch (IOException e) {
                System.out.println("Exception caught when trying to listen on port "
                    + portNumber + " or listening for a connection");
                System.out.println(e.getMessage());
            }
            
        }
    }
}