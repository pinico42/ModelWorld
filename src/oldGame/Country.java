package game;

import java.util.ArrayList;

public class Country {
	
	public static int USA = 0, SA = 1, EU = 2, AF = 3, AS = 4, OC = 5;
	
	public ArrayList<int[]> army = new ArrayList<int[]>(), mines = new ArrayList<int[]>(), dens = new ArrayList<int[]>(), aims = new ArrayList<int[]>(), vels = new ArrayList<int[]>(), ready = new ArrayList<int[]>();
	public int[] home = new int[2];
	public float income, money = 0, bincome, popularity, bpop;
	public int armyStrength = 0, armySize, reserves = 0;
	public int type;
	
	public boolean die = false;
	
	public Country(int type){
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
			army.add(home);
			break;
		case 4:
			// Asia
			money = 300;
			income = 4;
			home = new int[]{1783,302};
			armyStrength = 3;
			reserves = 3;
			popularity = 5;
			army.add(home);
			break;
		case 3:
			// Africa
			money = 300;
			income = 4;
			home = new int[]{1237,578};
			armyStrength = 3;
			reserves = 5;
			popularity = 3;
			army.add(home);
			break;
		case 2:
			// Europe
			money = 100;
			income = 1;
			home = new int[]{1284,274};
			armyStrength = 5;
			reserves = 3;
			popularity = 2;
			army.add(home);
			break;
		case 1:
			// South America
			money = 100;
			income = 1;
			home = new int[]{612,862};
			armyStrength = 1;
			reserves = 10;
			popularity = 0;
			army.add(home);
			break;
		case 0:
			// USA
			money = 500;
			income = 1;
			home = new int[]{253,284};
			armyStrength = 10;
			reserves = 0;
			popularity = 6;
			army.add(home);
			break;
		default:
			money = 0;
			income = 0;
			home = new int[]{0,0};
			armyStrength = 0;
			break;
		}
		armySize = 1;
		bincome = income;
		bpop = popularity;
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
	
	public void move(float mX, float mY, int sol){
		
		float angle = (float) Math.atan2(mY - army.get(sol)[1], mX - army.get(sol)[1]);
		float speed = 2;
		
		float scaleX = (float)Math.cos(angle);
		float scaleY = (float)Math.sin(angle);
		
		vels.get(sol)[0] = (int) (scaleX * speed);
		vels.get(sol)[1] = (int) (scaleY * speed);
	}
	
	public void armyAdd(int x, int y){
		
	}
	
	public void armyAdd(int[] arr){
		army.add(arr);
		vels.add(new int[]{0,0});
		aims.add(arr);
	}
		
}
