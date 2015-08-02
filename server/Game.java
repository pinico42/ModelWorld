
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

            try{Thread.sleep((long)(1000/60));}catch(InterruptedException e){System.out.println("Since when has there ever been one of these?!?");}

		}

		finish();

	}

	public void logic(){

	}

	public void update(){

		// Soldiers

		ArrayList<Sol> rsols = new ArrayList<Sol>();
		ArrayList<Integer> killOff = new ArrayList<Integer>();

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
								if(sol.owner == 0 || sold.owner == 0){
                                    //System.out.println("Hurt");
								}
								if(!atWar(sol.owner, sold.owner)){
                                    // I don't remember what this logic does so i just deleted it. <-- WATCH OUT
									//if((!players.contains(sol.owner) && !players.contains(sold.owner)) || Matha.hypo((players.contains(sol.owner)?sol:sold).pos[0] - countries[player].home[0], (players.contains(sol.owner)?sol:sold).pos[1] - countries[player].home[1]) < Matha.hypo((players.contains(sol.owner)?sold:sol).pos[0] - countries[player].home[0], (players.contains(sol.owner)?sold:sol).pos[1] - countries[player].home[1])){
										//wars[sol.owner][sold.owner] = true;
										//wars[sold.owner][sol.owner] = true;
										setWar(sol.owner, sold.owner, true);
									//}
								}
							}
						}
						// I think some sort of problem was here, i logged when country.type == 1
						if(!country.die && country.type != sold.owner && atWar(country.type, country2.type)){
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
								killOff.add(country.type);
								System.out.println("Killing "+country.type);
							}
						}
					}
				}
				if(sol.die){
					rsols.add(sol);
				}
			}
		}

		for(int country: killOff){
            countries[country].army.clear();
		}

		for(Sol sol: rsols){
            if(sol.owner == 0){
                System.out.println("Removing sol");
            }
            spin.sendAll(10, new int[]{1, sol.id}, null);
			countries[sol.owner].army.remove(sol);
			countries[sol.owner].armySize--;
		}

		// Soldiers

		if(time - last > Model.updateInterval){
            spin.sendAll(12, null, null);
			for(Country country: countries){
				country.update();
			}
			last += Model.updateInterval;
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

    public void setWar(int country, int country2, boolean atWar){
        spin.sendAll(14, new int[]{atWar?1:0, country, country2}, null);
        wars[country][country2] = atWar;
        wars[country2][country] = atWar;
    }

    public boolean atWar(int country, int country2){
        return wars[country2][country] || wars[country][country2];
    }

    private void finish(){

    }

	public void end(){
        run = false;
	}

}
