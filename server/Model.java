
import java.util.ArrayList;
import java.io.PrintWriter;

public class Model extends Thread implements GameI{

	public static final int nInGame = 6;

	public static int games = 0, clients = 0, id;
	public int playern;
	public static ArrayList<Spin> spins = new ArrayList<Spin>();
	public Spin spin;
	public PrintWriter out;
	public boolean runB = true, hi = false;
	public ArrayList<String> send = new ArrayList<String>();

	public Model(PrintWriter out, int id){
		this.out = out;
		this.id = id;
		this.playern = clients % nInGame;
		clients++;
	}

	public void run(){
		System.out.println("Model started");
		if(spins.size() * nInGame < clients){
			System.out.println("new Spin started " + spins.size() +" "+ nInGame + " " + clients);
			spins.add(new Spin(this));
			spins.get(spins.size()-1).start();
		} else {
			spins.get(spins.size()-1).addPlayer(this);
		}
		spin = spins.get(spins.size()-1);
		System.out.println("RUN STARTED ");
		while(runB){
			//System.out.println("STILL RUNNNNIG " + hi);
			/*if(!send.isEmpty()){
				String string = send.remove(0);
				out.println(string);
				//System.out.println("SENT STRING "+string);
			}*/
			try{
				Thread.sleep(4000);
			}catch(InterruptedException e){}
		}
		spin.removePlayer(playern);
		clients--;
		if(spin.nPlayers == 0){
			spins.remove(spins.indexOf(spin));
			spin.end();
			spin = null;
			System.out.println("RUN ENDED, Removing Spin");
		} else {
            System.out.println("RUN ENDED : " + spin.nPlayers);
        }
	}

	public void updateWarmup(double time, int players, String countries){
		String string;
		string = "2:"+time+";"+players+";"+countries;
		System.out.println("updateWarmup  to "+id+ " - " + string);
		out.println(string);
	}

	public void sendReject(int country){
        String string;
        string = "3:"+country;
        System.out.println(id + "rejecting country choice!");
        out.println(string);
        spin.sendAll(2, null, null);
	}

	public void receive(String string){
		send.add(string);
		hi = true;
		String[] strings = string.split(":");
		int protocol = Integer.parseInt(strings[0]);
		switch(protocol){
		case 0:
			System.out.println(playern +" choosing country");
			spin.chooseCountry(playern, Integer.parseInt(strings[1]));
			break;
		case 1:
			this.end();
			break;
		}
		System.out.println("received string "+string);
		System.out.println("send "+send.isEmpty());
	}

	public void end(){
		runB = false;
		//spin.removePlayer(playern); THIS IS ALREADY DONE AT THE END OF THE LOOP
	}

	public static void main(String[] args){
		System.out.println("Model game started");
	}

}
