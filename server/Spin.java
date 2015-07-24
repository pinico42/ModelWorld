
public class Spin extends Thread{

	public Model[] players = new Model[6];
	public int nPlayers = 0;
	public float warmup = 10; //Seconds
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
	    System.out.println("Using default remove for "+nPlayers);
		players[nPlayers--] = null;
		System.out.println("Player removed, there are now "+nPlayers+" players.");
	}

	public void removePlayer(int num){
		if(num == nPlayers - 1){removePlayer();return;}
		System.out.println("removing : "+num+" out of "+nPlayers);
		for(int i = num; i != nPlayers; i++){
            System.out.println("i : "+i+" + num : "+num+" + nPlayers : "+nPlayers);
			players[i] = i==5?null:players[i+1];
		}
		nPlayers--;
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
		case 2:
			for(int i = 0; i != players.length; i++){
				if(players[i] == null || playersc[i] == -1){continue;}
				string += playersc[i] + ",";
			}
			if(string != ""){
                string = string.substring(0,string.length()-1);
			}
			break;
		}
		for(int i = 0; i != nPlayers; i++){
			switch(code){
			case 2:
				players[i].updateWarmup(warmup, nPlayers, string);
				break;
			}
		}
	}

	public void run(){
		System.out.println("Spin started");
		float interval = 1000;
		while(warmup > 0){
			warmup -= interval / 1000;
			if(warmup<0){
                warmup = (float)0.0;
			}
			sendAll(2, null, null);
			try{
				Thread.sleep((long)interval);
			} catch(InterruptedException e){}
		}
		while(runb){}
	}

}
