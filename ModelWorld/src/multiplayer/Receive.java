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
           		int country, action, id, x, y;
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
           			System.out.println("Rejected for "+rejected);
           			for(int i = 0; i != 6; i++){
           				if(!main.MultiplayerSetup.locked[i] && i != rejected){
           					main.MultiplayerSetup.mthis.chosen = i;
           					Client.send(0, ""+i);
           					break;
           				}
           			}
           			break;
           		case 10:
           			action = Integer.parseInt(strings[0]);
           			switch(action){
           			case 0:
               			x = Integer.parseInt(strings[1]);
               			y = Integer.parseInt(strings[2]);
               			id = Integer.parseInt(strings[3]);
               			country = Integer.parseInt(strings[4]);
           				System.out.println("Adding an AI unit");
           				Game.mthis.countries[country].AIarmyAdd(x, y, id);
           				break;
           			case 1:
           				id = Integer.parseInt(strings[1]);
           				System.out.println("Removing an unit");
           				Sol.sols.remove(id);
           				break;
           			case 2:
               			x = Integer.parseInt(strings[1]);
               			y = Integer.parseInt(strings[2]);
               			id = Integer.parseInt(strings[3]);
           				System.out.println("Setting aim of "+id);
           				Sol sol = Sol.sols.get(id);
           				sol.setAim(x, y);
           				break;
           			}
           			break;
           		case 11:
           			System.out.println("Received new build : "+string);
           			int building = Integer.parseInt(strings[0]);
           			country = Integer.parseInt(strings[4]);
           			x = Integer.parseInt(strings[2]);
           			y = Integer.parseInt(strings[3]);
           			if(Integer.parseInt(strings[1]) == 1){
           				switch(building){
           				case 0:
           					System.out.println("Building a mine");
           					Game.mthis.countries[country].AImineAdd(x, y);
           					break;
           				case 1:
           					System.out.println("Building an opium den");
           					Game.mthis.countries[country].AIopiumAdd(x, y);
           					break;
           				}
           			} else {
           				switch(building){
           				case 0:
           					break;
           				case 1:
           					break;
           				}
           			}
           			break;
           		case 12:
           			for(int i = 0; i != 6; i++){
           				Game.mthis.countries[i].update();
           			}
           			break;
           		case 13:
           			action = Integer.parseInt(strings[0]);
           			country = Integer.parseInt(strings[1]);
           			switch(action){
           			case 0:
           				Game.mthis.countries[country].die = true;
           				Game.mthis.countries[country].money = 0;
           				Game.mthis.countries[country].mines.clear();
           				Game.mthis.countries[country].dens.clear();
           				break;
           			case 1:
           				int money = Integer.parseInt(strings[2]);
           				Game.mthis.countries[country].money += money;
           				break;
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
