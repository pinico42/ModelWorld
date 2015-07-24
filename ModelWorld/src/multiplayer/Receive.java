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
           			if(main.MultiplayerSetup.locked != countries2){
           				main.MultiplayerSetup.locked = countries2;
           				MultiplayerSetup.mthis.setupStrings(MultiplayerSetup.mthis.list);
           			}
           			break;
           		case 3:
           			int rejected = Integer.parseInt(strings[0]);
           			for(int i = 0; i != 6; i++){
           				if(!main.MultiplayerSetup.locked[i] && i != rejected){
           					main.MultiplayerSetup.mthis.chosen = i;
           					Client.send(0, ""+i);
           				}
           			}
           			break;
           		}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.exit(0); //SOMETHING SHOULD PROBABLY HAPPEN HERE- IT'S WHEN THE CONNECTION IS LOST
	}
	
}
