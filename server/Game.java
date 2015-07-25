
/*import static org.lwjgl.input.Keyboard.*;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTranslatef;*/

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import java.awt.Font;

//import main.Draw;
//import main.Main;
//import main.Setup;

/*import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Rectangle;
import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.SoundStore;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;*/

@SuppressWarnings("unused")
public class Game {

	public static final Random rand = new Random();
	public static int WIDTH = 1000, HEIGHT = 1000;
	public static boolean run = true;
	public static int  theight, twidth, player = 0, sel, selsol, solw = 40, solh = 100, minw = 100, minh = 100, opw = 100, oph = 100, hover = -1, state = 0, statei = 0, homew = 100, homeh = 100;
	public static long  ltime = System.currentTimeMillis(), time, last = ltime;
	public static Game mthis;
	public static int[] autos;

	public Country[] countries = new Country[6];

	public boolean retry = false, showbd = false, PAUSE = false, wintrip = false;

	public int deathCount = -1;

	public Game(int chosen){
		player = chosen;
		sel = chosen;
	}

	public boolean run() {

		Game.mthis = this;
		Init.init_game();

		run = true;

		while (run) {

			time = System.currentTimeMillis();


			logic();
			if(!run){
				break;
			}
			//render();

			update();

            try{
                Thread.sleep((long)(1000/60));
            } catch(InterruptedException e){
                System.out.println("Since when has there ever been one of these?!?");
            }

		}

		return retry;

	}

	public void logic(){

	}

	public void update(){

		// Soldiers

		ArrayList<Sol> rsols = new ArrayList<Sol>();

		for(Country country: countries){
			if(country.type != player){
				country.AI();
			} else {
				if(autos[0]==1){
					while(countries[player].reserves > 0){
						countries[player].AIarmyAdd();
						countries[player].reserves--;
					}
				}
			}
			for(Sol sol: country.army){
				for(Country country2: countries){
					for(Sol sold: country2.army){
						if(sold.id != sol.id && sold.owner != sol.owner){
							if(Matha.hypo(sol.pos[0] - sold.pos[0], sol.pos[1] - sold.pos[1]) < solh){
								sol.health -= 1;
								sold.health -= 1;
								if(!(Country.wars[sol.owner][sold.owner] || Country.wars[sold.owner][sol.owner])){
									if((sol.owner != player && sold.owner != player) || Matha.hypo((sol.owner==player?sol:sold).pos[0] - countries[player].home[0], (sol.owner==player?sol:sold).pos[1] - countries[player].home[1]) < Matha.hypo((sol.owner==player?sold:sol).pos[0] - countries[player].home[0], (sol.owner==player?sold:sol).pos[1] - countries[player].home[1])){
										Country.wars[sol.owner][sold.owner] = true;
										Country.wars[sold.owner][sol.owner] = true;
									}
								}
							}
						}
						// I think some sort of problem was here, i logged when country.type == 1
						if(country.type != sold.owner && (Country.wars[country.type][sold.owner] || Country.wars[sold.owner][country.type])){
							if(solh > Matha.hypo(country.home[0] - sold.pos[0], country.home[1] - sold.pos[1])){
								if(country.income > 0){
									country.income -= 0.5;
									country2.income += 0.5;
								}
								if(country.income <= 0){
									country.die = true;
									country2.money += country.money;
									country.money = 0;
									for(int[] mine: country.mines){
										country2.mines.add(mine);
									}
									for(int[] mine: country.dens){
										country2.dens.add(mine);
									}
									country.mines.clear();
									country.dens.clear();
								}
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
			for(Country country: countries){
				country.update();
			}
			last += 10000;
		}

		for(Country country: countries){
			for(Sol sol: country.army){
				sol.update();
			}
		}

	}

	public void end(){

	}

}
