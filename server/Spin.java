
public class Spin extends Thread{
	
	public Model[] players = new Model[6];
	public int nPlayers = 0;
	public double warmup = 10; //Seconds
	public boolean runb = true;
	public int[] countries = new int[6];
	public int[] playersc = new int[6];
	
	public Spin(Model model){
		addPlayer(model);
		for(int i = 0; i != players.length; i++){
			countries[i] = -1;
			playersc [i] = -1;
		}
	}
	
	public void addPlayer(Model model){
		players[nPlayers++] = model;
		sendPlayer(nPlayers - 1, 0, null, null);
	}
	
	public void removePlayer(){
		players[nPlayers--] = null;
	}
	
	public void removePlayer(int num){
		if(num == nPlayers - 1){removePlayer();return;}
		System.out.println("removing : "+num);
		for(int i = num; num != nPlayers; i++){
			players[i] = i==5?null:players[i+1];
		}
	}
	
	public void chooseCountry(int player, int num){
		if(countries[num] != -1){return;}
		if(playersc[player] != -1){countries[num] = -1;}
		countries[num] = player;
		playersc[player] = num;
		System.out.println("Set country");
	}
	
	public void sendPlayer(int player, int code, int[] ints, String[] strings){
		String string = "";
		switch(code){
		case 0:
			for(int i = 0; i != players.length; i++){
				if(players[i] == null || playersc[i] == -1){continue;}
				string += playersc[i] + ",";
			}
			if(string != ""){
				string = string.substring(0,string.length()-1);
			}
			players[player].updateWarmup(warmup, nPlayers, string);
			break;
		}
	}
	
	public void sendAll(int code, int[] ints, String[] strings){
		//System.out.println("Sending with code "+code);
		String string = "";
		switch(code){
		case 0:
			for(int i = 0; i != players.length; i++){
				if(players[i] == null || playersc[i] == -1){continue;}
				string += playersc[i] + ",";
			}
			if(string == ""){break;}
			string = string.substring(0,string.length()-1);
			break;
		}
		for(int i = 0; i != nPlayers; i++){
			switch(code){
			case 0:
				players[i].updateWarmup(warmup, nPlayers, string);
				break;
			}
		}
	}
	
	public void run(){
		System.out.println("Spin started");
		int interval = 1000;
		while(warmup > 0){
			warmup -= interval / 1000;
			sendAll(0, null, null);
			try{
				Thread.sleep(interval);
			} catch(InterruptedException e){}
		}
		while(runb){}
	}
	
}
