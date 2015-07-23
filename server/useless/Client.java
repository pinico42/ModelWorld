import java.io.*;
import java.net.*;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Client {
	
	public static int number;
	
    public static void main(String[] args) throws IOException {
        
        if (args.length != 0) {
            System.err.println(
                "Usage: java EchoClient <host name> <port number>");
            System.exit(1);
        }
        
        Path dir = Paths.get(System.getProperty("user.dir") + "/plugins");;
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for (Path file: stream) {
                System.out.println(file.getFileName());
            }
        } catch (IOException | DirectoryIteratorException x) {
            // IOException can never be thrown by the iteration.
            // In this snippet, it can only be thrown by newDirectoryStream.

boolean success = (new File(dir.toString())).mkdirs();

//		boolean bob = (new File(dir)).mkdirs();
	}

        String hostName = "benhack.ddns.net";
        int portNumber = 26656;

        try (
            Socket echoSocket = new Socket(hostName, portNumber);
            PrintWriter out =
                new PrintWriter(echoSocket.getOutputStream(), true);
            BufferedReader in =
                new BufferedReader(
                    new InputStreamReader(echoSocket.getInputStream()));
            BufferedReader stdIn =
                new BufferedReader(
                    new InputStreamReader(System.in))
        ) {
        	number = Integer.parseInt(in.readLine());
        	Receive receive = new Receive(in);
        	receive.start();
            String userInput;
            while ((userInput = stdIn.readLine()) != null) {
                out.println(userInput);
            	if(userInput.equals("exit")){
            		receive.run = false;
            		break;
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
