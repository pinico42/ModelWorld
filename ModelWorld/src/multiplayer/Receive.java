package multiplayer;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileOutputStream;

public class Receive extends Thread{
	
	boolean run = true;
	
	BufferedReader in;
	InputStream input;
	
	public Receive(InputStream input, BufferedReader in){
		this.in = in;
		this.input = input;
	}
	
	public void run(){
		while(run){
           	try {
           		String string = in.readLine();
           		if(string == null){break;}
           		if(Client.game != -1){
           			switch(Client.game){
           			case 0:
           				System.out.println("GAME 0 : "+string);
           				break;
           			case 1:
           				System.out.println("GAME 1 : "+string);
           				break;
           			default:
           				System.out.println("UNKNOWN GAME!");
           			}
           			continue;
           		}
           		try{
            		if(string.substring(0, 1).equals("\f")){
            			System.out.println("Many d");
            			String filename = string.substring(1, string.length());
            			String file = "";
            			string = in.readLine();
            			while(!string.equals("\f")){
            				file += string;
            				string = in.readLine();
            				System.out.println("Read: " + string);
            			}
            			System.out.println("File - " + file);
            			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("./plugins/" + filename));
            			byte[] bytes = file.getBytes();
            			bos.write(bytes, 0, bytes.length);
            			bos.flush();
            			bos.close();
            		} else if(Integer.parseInt(string.substring(0, 1)) != Client.number){
                		System.out.println(string);
                	}
            	} catch(NumberFormatException e){
            		System.out.println(string);
            	} catch(NullPointerException e){
            		System.out.println(string);
            	}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.exit(0);
	}
	
}
