package multiplayer;

// Sol stands for soldier, obviously...

public class Sol {
	public static int idCounter = 0;
	public float health;
	public int owner, strength = -1, id;
	public boolean updated = false, die;
	public double[] vel;
	public int[] pos;
	public int[] aim;
	
	public Sol(int x, int y, int by){
		id = idCounter;
		idCounter++;
		pos = new int[]{x,y};
		vel = new double[]{0,0};
		owner = by;
		aim = new int[]{0,0};
		Game game = Game.mthis;
		Country country = game.countries[owner];
		int str = country.armyStrength;
		strength = Game.mthis.countries[owner].armyStrength;
		health = strength;
	}
	
	public void setAim(int x, int y){
		//System.out.println(x + ", " + y + " - ???");
		aim = new int[]{x - 2 + Game.rand.nextInt(4), y - 2 + Game.rand.nextInt(4)};
		updated = true;
	}
	
	public void update(){
		if(updated){
			double angle = (float) Math.atan2(aim[1] - pos[1], aim[0] - pos[0]);
			double speed = 2;
			
			double scaleX = (float)Math.cos(angle);
			double scaleY = (float)Math.sin(angle);
			
			vel[0] = (int) (scaleX * speed);
			vel[1] = (int) (scaleY * speed);
		}
		
		if(Math.abs(aim[0] - pos[0]) < 7 && Math.abs(aim[1] - pos[1]) < 7){
			vel[0] = 0;
			vel[1] = 0;
			updated = false;
		}
		
		health += 0.1 + ((float)strength) / 70;
		
		if(health > 100){
			health = 100;
		}
		
		if(health <= 0){
			die = true;
		}

		pos[0] += vel[0];
		pos[1] += vel[1];
	}
	
}
