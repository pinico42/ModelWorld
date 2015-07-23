import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Handler extends Thread{

	public static int counter = 0;
	public static OutputStream os;
	public static ArrayList<PrintWriter> outs = new ArrayList<PrintWriter>();
	public static ArrayList<String> nicks = new ArrayList<String>();
	public static ArrayList<Boolean> dead = new ArrayList<Boolean>();

	public int number;
	public PrintWriter out;
	public BufferedReader in;
	public Socket socket;

	public Handler(Socket clientSocket){

		number = counter;
		counter++;

		this.socket = clientSocket;
        try {
			os = clientSocket.getOutputStream();
            out = new PrintWriter(os, true);
			outs.add(out);
		} catch (IOException e) {
			e.printStackTrace();
		}
        try {
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}

        out.println(number);
        nicks.add("");
        dead.add(false);

	}

	public void run() {
    String inputLine;

	int i = 0;

	for(boolean bool: dead){
		if(!bool){i++;}
	}

	//out.println(number);

	//out.println("There are currently " + i + " users online.");
	//write("User " + number + " just logged on.");

	int game = -1;
	Game gameO = null;

        try {
		while ((inputLine = in.readLine()) != null) {
			if(inputLine.equals("exit")){
				out.println("Exiting");
				dead.set(number, true);
				write(number + (nicks.get(number).equals("")?"":" (" + nicks.get(number) + ")") + ": " + " thread has had a disconnection");
				out.close();
				gameO.end();
				gameO = null;
				break;
			}else if(game != -1){
				if(inputLine.equals("STOP")){
					game = -1;
					gameO.end();
					gameO = null;
					continue;
				}
				gameO.receive(inputLine);
				switch(game){
				case 0:

					break;
				}
			}else if(inputLine.startsWith("file")){
				if(inputLine.length() < 6){out.println("Wrong usage, try again");continue;}
				inputLine = inputLine.substring(5, inputLine.length());
				out.println("\f"+"shrek.png");
				File.writeFile(os, "shrek.png");
				out.println("\n");
				out.println("\f");
				System.out.println("hi, " + inputLine);
			} else if(inputLine.equals("help")){
				out.println("The commands are: \n 'help' \n 'nick' \n 'exit'");
			} else if(inputLine.startsWith("nick")){
				try{
					nicks.set(number, inputLine.substring(5));
			    	}catch(StringIndexOutOfBoundsException e){
			    		nicks.set(number, "");
			    	}
			} else if(inputLine.startsWith("game")){
				if(inputLine.length() < 6){out.println("Wrong usuage, try again");continue;}
				game = Integer.parseInt(inputLine.substring(5));
				System.out.println("User "+ number + " is switching to game "+game);
				switch(game){
				case 0:
					gameO = new Echo(out);
					gameO.start();
					break;
				case 1:
					gameO = new Model(out, number);
					gameO.start();
					break;
				}
			} else {
				write(number + (nicks.get(number).equals("")?"":" (" + nicks.get(number) + ")") + ": " + inputLine);
			}
			System.out.println(inputLine);

		}
	} catch (IOException e) {
		dead.set(number, true);
		write(number + (nicks.get(number).equals("")?"":" (" + nicks.get(number) + ")") + ": " + " thread has had a disconnection");
		out.close();
		if(gameO != null){gameO.end();gameO = null;}
	}
    }

	public static void write(String string){
		for(int i = 0; i != outs.size(); i++){
			if(!dead.get(i)){
				outs.get(i).println(string);
			}
		}
	}

    public static void main(String args[]) {
        (new Handler(null)).start();
    }

}

