
// Sol stands for soldier, obviously...

public class Sol {

    // It needs some sort of owner to account for the need to be non static in all this.
    public Country cOwner;
	public float health;
	public int owner, strength = -1, id;
	public boolean updated = false, die;
	public double[] vel;
	public int[] pos;
	private int[] aim;

	public Sol(int x, int y, int by, Country Owner){
		id = Owner.owner.idCounter;
		Owner.owner.idCounter++;
		pos = new int[]{x,y};
		vel = new double[]{0,0};
		owner = by;
		aim = new int[]{0,0};
		strength = Owner.armyStrength;
		health = strength;
		cOwner = Owner;
		cOwner.owner.Sols.add(this);
	}

	public void setAim(int x, int y){
		System.out.println("Setting aim of thing");
		cOwner.owner.spin.sendAll(10, new int[]{2, x, y, id}, null);
		aim = new int[]{x /*- 2 + Game.rand.nextInt(4)*/, y /*- 2 + Game.rand.nextInt(4)*/};
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

			if(Math.abs(aim[0] - pos[0]) < 7 && Math.abs(aim[1] - pos[1]) < 7){
				vel[0] = 0;
				vel[1] = 0;
				updated = false;
			}
		}

		health += 0.1 + ((float)strength) / 70;

        if(owner == 0 && health < 50){
            //System.out.println("Health : "+health);
        }

		if(health > 100){
			health = 100;
		}

		if(health <= 0){
            if(owner == 0){
                System.out.println("Killing...");
            }
			die = true;
			cOwner.owner.spin.sendAll(10, new int[]{1, id}, null);
		}

		pos[0] += vel[0];
		pos[1] += vel[1];
	}

}
