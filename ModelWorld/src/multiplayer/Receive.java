package multiplayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;

import main.IOHandle;
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
           		//Sol sol;
           		String string = in.readLine();
           		if(string==null){break;}
           		//System.out.println(string);
           		String[] strings = string.split(":");
           		int protocol = Integer.parseInt(strings[0]);
           		strings = strings[1].split(";");
           		if(protocol > 9){
           			while(Game.mthis.countries == null || Game.mthis.countries[5] == null){
           				try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
           			}
           		}
           		switch(protocol){
           		case 2:
           			main.MultiplayerSetup.warmupTime = Float.parseFloat(strings[0]);
           			main.MultiplayerSetup.playersInGame = Integer.parseInt(strings[1]);
           			if(first){
           				first = false;
           				main.MultiplayerSetup.mthis.chosen = main.MultiplayerSetup.playersInGame-1;
           				//System.out.println("Set default country to country : "+(main.MultiplayerSetup.playersInGame-1));
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
           		case 4:
           			//System.out.println("Received a 4 for "+string.split(":", 2)[1]);
           			Game.mthis.countries = IOHandle.getMCSettings(string.split(":", 2)[1]);
           			break;
           		case 5:
           			main.MultiplayerSetup.serverSettings[0] = Integer.parseInt(strings[0]);
           			break;
           		case 10:
           			action = Integer.parseInt(strings[0]);
           			switch(action){
           			case 0:
               			x = Integer.parseInt(strings[1]);
               			y = Integer.parseInt(strings[2]);
               			id = Integer.parseInt(strings[3]);
               			country = Integer.parseInt(strings[4]);
               			int reserve = 0;
               			if(strings.length == 6){
               				reserve = 2;
               			}
               			synchronized(Game.mthis.countries[country].add){
               				Game.mthis.countries[country].add.add(new int[]{reserve, x, y, id});
               			}
           				break;
           			case 1:
           				id = Integer.parseInt(strings[1]);
           				//System.out.println("Removing unit "+id);
           				//sol = Sol.sols.get(id);
           				synchronized(Game.mthis.countries[0].remove){
           					Game.mthis.countries[0].remove.add(new int[]{0, id});
           				}
           				break;
           			case 2:
               			x = Integer.parseInt(strings[1]);
               			y = Integer.parseInt(strings[2]);
               			id = Integer.parseInt(strings[3]);
           				//System.out.println("Setting aim (for ID : "+id+") to "+x+", "+y);
           				//sol = Sol.sols.get(id);
               			synchronized(Game.mthis.countries[0].add){
               				Game.mthis.countries[0].add.add(new int[]{1, x, y, id});
               			}
           				//sol.setAim(x, y);
           				break;
           			}
           			break;
           		case 11:
           			//System.out.println("Received new build : "+string);
           			int building = Integer.parseInt(strings[0]);
           			country = Integer.parseInt(strings[4]);
           			x = Integer.parseInt(strings[2]);
           			y = Integer.parseInt(strings[3]);
           			if(Integer.parseInt(strings[1]) == 1){
           				switch(building){
           				case 0:
           					//System.out.println("Building a mine");
           					Game.mthis.countries[country].AImineAdd(x, y);
           					Game.mthis.countries[country].money += Country.mcost;
           					break;
           				case 1:
           					//System.out.println("Building an opium den");
           					Game.mthis.countries[country].AIopiumAdd(x, y);
           					Game.mthis.countries[country].money += Country.ocost;
           					break;
           				}
           			} else {
           				int[] toRemove;
           				switch(building){
           				case 0:
           					toRemove = null;
           					synchronized(Game.mthis.countries[country].mines){
           						for(int[] mine: Game.mthis.countries[country].mines){
           							if(mine[0] == x && mine[1] == y){
           								toRemove = mine;
           							}
           						}
               					if(toRemove == null){
               						System.out.println("No such thing?!?");
               					}
               					Game.mthis.countries[country].mines.remove(toRemove);
           					}
           					break;
           				case 1:
           					toRemove = null;
           					synchronized(Game.mthis.countries[country].mines){
           						for(int[] mine: Game.mthis.countries[country].dens){
           							if(mine[0] == x && mine[1] == y){
           								toRemove = mine;
           							}
           						}
               					if(toRemove == null){
               						System.out.println("No such thing?!?");
               					}
               					Game.mthis.countries[country].mines.remove(toRemove);
           					}
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
           				Game.mthis.countries[country].army.clear();
           				break;
           			case 1:
           				int money = Integer.parseInt(strings[2]);
           				Game.mthis.countries[country].money += money;
           				break;
           			}
           			break;
           		case 14:
           			System.out.println("Setting a war");
           			action = Integer.parseInt(strings[0]);
           			country = Integer.parseInt(strings[1]);
           			int country2 = Integer.parseInt(strings[2]);
           			Country.wars[country][country2] = action==1;
           			Country.wars[country2][country] = action==1;
           			break;
           		}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (IndexOutOfBoundsException e){
				e.printStackTrace();
			} catch(NumberFormatException e){
				Game.mthis.texts[2] = "The server has disconnected!";
			}
		}
		System.exit(0); //SOMETHING SHOULD PROBABLY HAPPEN HERE- IT'S WHEN THE CONNECTION IS LOST
	}
	
}
