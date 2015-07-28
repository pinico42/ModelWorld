
import java.util.ArrayList;

public class Country {

    public Game owner;

	public static int USA = 0, SA = 1, EU = 2, AF = 3, AS = 4, OC = 5, scost = 20, mcost = 50, ocost = 30, sdist = 100, mdist = 200, odist = 300;
	public static String[] names = {"North America", "South America", "Europe", "Africa", "Asia", "Oceania"};

	//public boolean[][] owner.wars = new boolean[6][6];

	public ArrayList<int[]> mines = new ArrayList<int[]>(), dens = new ArrayList<int[]>(), ready = new ArrayList<int[]>(), add = new ArrayList<int[]>(), remove = new ArrayList<int[]>();
	public ArrayList<Sol> army = new ArrayList<Sol>();
	public int[] home = new int[2];
	public float income, money = 0, bincome, popularity, bpop;
	public int armyStrength = 0, armySize, reserves = 0;
	public int type;

	public boolean die = false;

	public Country(int type, Game mthis){
		this.type = type;
		switch(type){
		case 5:
			// Oceania
			money = 50;
			income = 6;
			home = new int[]{2169,924};
			armyStrength = 3;
			reserves = 2;
			popularity = 1;
			break;
		case 4:
			// Asia
			money = 300;
			income = 4;
			home = new int[]{1783,302};
			armyStrength = 3;
			reserves = 3;
			popularity = 5;
			break;
		case 3:
			// Africa
			money = 300;
			income = 4;
			home = new int[]{1237,578};
			armyStrength = 3;
			reserves = 5;
			popularity = 3;
			break;
		case 2:
			// Europe
			money = 100;
			income = 1;
			home = new int[]{1284,274};
			armyStrength = 5;
			reserves = 3;
			popularity = 2;
			break;
		case 1:
			// South America
			money = 100;
			income = 1;
			home = new int[]{612,862};
			armyStrength = 1;
			reserves = 10;
			popularity = 0;
			break;
		case 0:
			// USA
			money = 500;
			income = 1;
			home = new int[]{253,284};
			armyStrength = 10;
			reserves = 0;
			popularity = 6;
			break;
		default:
			money = 0;
			income = 0;
			home = new int[]{0,0};
			armyStrength = 0;
			break;
		}
		armySize = 0;
		bincome = income;
		bpop = popularity;
		owner = mthis;
		//for(int i = 0; i != owner.wars.length * owner.wars[0].length; i++){
		//	owner.wars[i / owner.wars.length][i % owner.wars[0].length] = true;
		//}
	}

	public void addAim(int x, int y, int index){

	}

	public void update(){
		money += income;
		income = bincome + mines.size() * 1;
		popularity = bpop + dens.size() * 2;
		reserves += popularity / 5;
		for(int[] sol: ready){
			armyAdd(sol);
		}
		ready.clear();
	}

	public void AI(){
		if(die){
			armyStrength = 0;
			armySize = 0;
			bincome = 0;
			bpop = 0;
			mines.clear();
			dens.clear();

			army.clear();
			return;
		}
		boolean[] warsw = new boolean[6];
		int warsn = 0;
		for(int i = 0; i != names.length; i++){
			if(owner.wars[i][i]){
				owner.wars[i][i] = false;
			}
			if(owner.wars[type][i] || owner.wars[i][type]){
				if(owner.countries[i].die){
					continue;
				}
				warsw[i] = true;
				warsn++;
			}
		}
		int[] warsa = new int[warsn];
		int e = 0;
		for(int i = 0; i != names.length; i++){
			if(owner.wars[type][i] || owner.wars[i][type]){
				if(owner.countries[i].die){
					continue;
				}
				warsa[e] = i;
				e++;
			}
		}
		int homeGuard = 0;
		switch(type){
		case 0:
			if(money > scost){
				AIarmyAdd();
				money -= scost;
			}
			homeGuard = 5;
			break;
		case 1:
			if(Game.rand.nextInt(10) == 1){
				if(money > scost){
					AIarmyAdd();
					money -= scost;
				}
			}
			if(Game.rand.nextInt(10) == 1){
				if(money > mcost){
					AImineAdd();
					money -= mcost;
				}
			}
			if(Game.rand.nextInt(10) == 1){
				if(money > ocost){
					AIopiumAdd();
					money -= ocost;
				}
			}
			homeGuard = 10;
			break;
		case 2:
			if(Game.rand.nextInt(5) == 1){
				if(money > scost){
					AIarmyAdd();
					money -= scost;
				}
			}
			if(Game.rand.nextInt(20) == 1){
				if(money > mcost){
					AImineAdd();
					money -= mcost;
				}
			}
			if(Game.rand.nextInt(5) == 1){
				if(money > ocost){
					AIopiumAdd();
					money -= ocost;
				}
			}
			homeGuard = 13;
			break;
		case 3:
			if(Game.rand.nextInt(10) == 1){
				if(money > scost){
					AIarmyAdd();
					money -= scost;
				}
			}
			if(Game.rand.nextInt(10) == 1){
				if(money > mcost){
					AImineAdd();
					money -= mcost;
				}
			}
			if(Game.rand.nextInt(10) == 1){
				if(money > ocost){
					AIopiumAdd();
					money -= ocost;
				}
			}
			homeGuard = 3;
			break;
		case 4:
			if(Game.rand.nextInt(20) == 1){
				if(money > scost){
					AIarmyAdd();
					money -= scost;
				}
			}
			if(Game.rand.nextInt(5) == 1){
				if(money > mcost){
					AImineAdd();
					money -= mcost;
				}
			}
			if(Game.rand.nextInt(5) == 1){
				if(money > ocost){
					AIopiumAdd();
					money -= ocost;
				}
			}
			homeGuard = 1;
			break;
		case 5:
			if(Game.rand.nextInt(12) == 1){
				if(money > scost){
					AIarmyAdd();
					money -= scost;
				}
			}
			if(Game.rand.nextInt(12) == 1){
				if(money > mcost){
					AImineAdd();
					money -= mcost;
				}
			}
			if(Game.rand.nextInt(4) == 1){
				if(money > ocost){
					AIopiumAdd();
					money -= ocost;
				}
			}
			homeGuard = 6;
			break;
		}

		if(warsn != 0 && army.size() > homeGuard && army.size() > 5){
			for(int i = homeGuard; i != army.size(); i++){
				if(!army.get(i).updated){
					army.get(i).setAim(owner.countries[warsa[(i-5) % warsn]].home[0] - 10 + Game.rand.nextInt(20), owner.countries[warsa[(i-5) % warsn]].home[1] - 10 + Game.rand.nextInt(20));
				}
			}
		}
		if(warsn != 0){
			while(reserves > 0){
				AIRarmyAdd();
			}
		}
		if(warsn < armyStrength / 3){
			if(Game.rand.nextInt(30000 - armyStrength * 360) == 1){
				System.out.println("WOW");
				int chosen = Game.rand.nextInt(5);
				chosen = chosen<type?chosen:chosen+1;
				owner.wars[type][chosen] = true;
				owner.wars[chosen][type] = true;
			}
		}
	}

    void AIRarmyAdd(){
        if(reserves<=0){return;}
        reserves--;
		armySize++;
		int x = home[0] + Game.rand.nextInt(sdist) - sdist / 2, y = home[1] + Game.rand.nextInt(sdist) - sdist / 2;
		owner.spin.sendAll(10, new int[]{0, x, y, owner.idCounter, type, 1}, null);
		armyAdd(x, y);
	}

	void AIarmyAdd(){
		armySize++;
		int x = home[0] + Game.rand.nextInt(sdist) - sdist / 2, y = home[1] + Game.rand.nextInt(sdist) - sdist / 2;
		owner.spin.sendAll(10, new int[]{0, x, y, owner.idCounter, type}, null);
		armyAdd(x, y);
	}

	void AIarmyAdd(int x, int y){
	    money -= scost;
		armySize++;
		owner.spin.sendAll(10, new int[]{0, x, y, owner.idCounter, type}, null);
		armyAdd(x, y);
	}

	void AIRarmyAdd(int x, int y){
        if(reserves<=0){return;}
        reserves--;
		armySize++;
		owner.spin.sendAll(10, new int[]{0, x, y, owner.idCounter, type, 1}, null);
		armyAdd(x, y);
	}

	private void AImineAdd(){
	    int[] location = new int[]{home[0] + Game.rand.nextInt(mdist) - mdist / 2, home[1] + Game.rand.nextInt(mdist) - mdist / 2};
	    owner.spin.sendAll(11, new int[]{0, 1, location[0], location[1], type}, null);
		mines.add(location);
	}

	public void AImineAdd(int x, int y){
	    int[] location = new int[]{x,y};
	    owner.spin.sendAll(11, new int[]{0, 1, location[0], location[1], type}, null);
		mines.add(location);
	}

	private void AIopiumAdd(){
	    //System.out.println("New opium house added.");
	    int[] location = new int[]{home[0] + Game.rand.nextInt(odist) - odist / 2, home[1] + Game.rand.nextInt(odist) - odist / 2};
	    owner.spin.sendAll(11, new int[]{1, 1, location[0], location[1], type}, null);
		dens.add(location);
	}

	public void AIopiumAdd(int x, int y){
	    //System.out.println("New opium house added.");
	    int[] location = new int[]{x,y};
	    owner.spin.sendAll(11, new int[]{1, 1, location[0], location[1], type}, null);
		dens.add(location);
	}

	public void move(float mX, float mY, int sol){
		army.get(sol).update();
	}

	public void armyAdd(int x, int y){
		army.add(new Sol(x, y, type, this));
	}

	public void armyAdd(int[] arr){
		army.add(new Sol(arr[0], arr[1], type, this));
	}

	public void clear() {
		money = 0;
		armySize = 0;
		armyStrength = 0;
		income = 0;
		bpop = 0;
		reserves = 0;
		dens.clear();
		army.clear();
		ready.clear();

	}

}
