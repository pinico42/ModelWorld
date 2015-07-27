
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import java.awt.Font;

@SuppressWarnings("unused")
public class Game {

	public static final Random rand = new Random();
	public static final int WIDTH = 1000, HEIGHT = 1000;
	public static int theight, twidth, solw = 40, solh = 100, minw = 100, minh = 100, opw = 100, oph = 100, state = 0, statei = 0, homew = 100, homeh = 100;

    public Spin spin;
    public ArrayList<Sol> Sols = new ArrayList<Sol>();

	public long  ltime = System.currentTimeMillis(), time, last = ltime;
	public int idCounter = 0;
	public boolean run = true;

	public boolean[][] wars;

	// This was a cheat I should never have used...
	//public static Game mthis;

	public Country[] countries = new Country[6];

	public boolean retry = false, showbd = false, PAUSE = false, wintrip = false;
	public ArrayList<Integer> players = new ArrayList<Integer>();

	public int deathCount = -1;

	public Game(int[] tplayers, Spin tspin){
        for(int i = 0; i != tplayers.length; i++){
            if(tplayers[i] != -1){
                players.add(tplayers[i]);
            }
        }
        spin = tspin;
	}

	public void init(){
        for(int i = 0; i != 6; i++){
            countries[i] = new Country(i, this);
        }
		wars = new boolean[6][6];
	}

	public void run() {

		init();

		run = true;

        System.out.println("Game starting");

		while (run) {

			time = System.currentTimeMillis();

			logic();
			if(!run){
				break;
			}

			update();

            try{
                Thread.sleep((long)(1000/60));
            } catch(InterruptedException e){
                System.out.println("Since when has there ever been one of these?!?");
            }

		}

		finish();

	}

	public void logic(){

	}

	public void update(){

        //System.out.println("Update called");

		// Soldiers

		ArrayList<Sol> rsols = new ArrayList<Sol>();

		for(Country country: countries){
			if(!players.contains(country.type)){
				country.AI();
			}
			for(Sol sol: country.army){
				for(Country country2: countries){
					for(Sol sold: country2.army){
						if(sold.id != sol.id && sold.owner != sol.owner){
							if(Matha.hypo(sol.pos[0] - sold.pos[0], sol.pos[1] - sold.pos[1]) < solh){
								sol.health -= 1;
								sold.health -= 1;
								if(!(wars[sol.owner][sold.owner] || wars[sold.owner][sol.owner])){
                                    // I don't remember what this logic does so i just deleted it. <-- WATCH OUT
									//if((!players.contains(sol.owner) && !players.contains(sold.owner)) || Matha.hypo((players.contains(sol.owner)?sol:sold).pos[0] - countries[player].home[0], (players.contains(sol.owner)?sol:sold).pos[1] - countries[player].home[1]) < Matha.hypo((players.contains(sol.owner)?sold:sol).pos[0] - countries[player].home[0], (players.contains(sol.owner)?sold:sol).pos[1] - countries[player].home[1])){
										wars[sol.owner][sold.owner] = true;
										wars[sold.owner][sol.owner] = true;
									//}
								}
							}
						}
						// I think some sort of problem was here, i logged when country.type == 1
						if(country.type != sold.owner && (wars[country.type][sold.owner] || wars[sold.owner][country.type])){
							if(solh > Matha.hypo(country.home[0] - sold.pos[0], country.home[1] - sold.pos[1])){
							    spin.sendAll(13, new int[]{0, country.type}, null);
                                country.die = true;
								country2.money += country.money;
								spin.sendAll(13, new int[]{1, country2.type, (int)country.money}, null);
								//country.money = 0; MOVED TO COUNTRY DIE CODE
								for(int[] mine: country.mines){
                                    spin.sendAll(11, new int[]{0, 1, mine[0], mine[1], country2.type}, null);
									country2.mines.add(mine);
								}
								for(int[] mine: country.dens){
                                    spin.sendAll(11, new int[]{1, 1, mine[0], mine[1], country2.type}, null);
									country2.dens.add(mine);
								}
								country.mines.clear();
								country.dens.clear();
							}
						}
					}
				}
				if(sol.die){
					rsols.add(sol);
				}
			}
		}

		for(Sol sol: rsols){
			countries[sol.owner].army.remove(sol);
			countries[sol.owner].armySize--;
		}

		// Soldiers

		if(time - last > 10000){
            spin.sendAll(12, null, null);
			for(Country country: countries){
				country.update();
			}
			last += 10000;
		}

		for(Country country: countries){
            synchronized(country.add){
                for(int[] ints: country.add){
                    switch(ints[0]){
                    case 0:
                        countries[ints[3]].AIarmyAdd(ints[1], ints[2]);
                        break;
                    case 1:
                        Sol sol = Sols.get(ints[3]);
                        sol.setAim(ints[1], ints[2]);
                        break;
                    case 2:
                        countries[ints[3]].AIRarmyAdd(ints[1], ints[2]);
                        break;
                    }
                }
            }
            country.add.clear();
            synchronized(country.remove){
                for(int[] ints: country.remove){

                }
            }
            country.remove.clear();
			for(Sol sol: country.army){
				sol.update();
			}
		}

	}

    private void finish(){

    }

	public void end(){
        run = false;
	}

}
