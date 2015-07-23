package multiplayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;

import main.MultiplayerSetup;

public class Receive extends Thread{
	
	boolean run = true;
	
	BufferedReader in;
	InputStream input;
	
	public Receive(InputStream input, BufferedReader in){
		this.in = in;
		this.input = input;
	}
	
	public void run(){
		boolean first = true;
		while(run){
           	try {
           		String string = in.readLine();
           		if(string==null){break;}
           		System.out.println(string);
           		String[] strings = string.split(":");
           		int protocol = Integer.parseInt(strings[0]);
           		strings = strings[1].split(";");
           		switch(protocol){
           		case 2:
           			main.MultiplayerSetup.warmupTime = Float.parseFloat(strings[0]);
           			main.MultiplayerSetup.playersInGame = Integer.parseInt(strings[1]);
           			if(first){
           				first = false;
           				main.MultiplayerSetup.mthis.chosen = main.MultiplayerSetup.playersInGame-1;
           				System.out.println("Set default country to country : "+(main.MultiplayerSetup.playersInGame-1));
						Client.send(0, ""+(main.MultiplayerSetup.playersInGame-1));
           			}
           			if(strings.length == 2){
           				break;
           			}
           			String[] countries = strings[2].split(",");
           			boolean[] countries2 = new boolean[main.MultiplayerSetup.locked.length];
           			for(int i = 0; i != countries.length; i++){
           				countries2[Integer.parseInt(countries[i])] = true;
           			}
           			main.MultiplayerSetup.locked = countries2;
           			MultiplayerSetup.mthis.setupStrings(MultiplayerSetup.mthis.list);
           			break;
           		}
           		/*if(string == null){break;}
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
            	}*/
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.exit(0); //SOMETHING SHOULD PROBABLY HAPPEN HERE- IT'S WHEN THE CONNECTION IS LOST
	}
	
}
