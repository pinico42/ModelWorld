package game;

import static org.lwjgl.input.Keyboard.*;
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
import static org.lwjgl.opengl.GL11.glTranslatef;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.awt.Font;

import main.Draw;
import main.Main;
import main.Setup;

import org.lwjgl.LWJGLException;
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
import org.newdawn.slick.opengl.TextureLoader;

@SuppressWarnings("unused")
public class Game {
	
	public static final int SWIDTH = 2840;
	public static final int SHEIGHT = 1310;
	public static int WIDTH = 1000;
	public static int HEIGHT = 1000;
	public static boolean run = true;
	public static UnicodeFont[] FONTS;
	public static int RHEIGHT, RWIDTH, theight, twidth, player = 0, sel, selsol, solw = 40, solh = 100, minw = 100, minh = 100, opw = 100, oph = 100, hover = -1, state = 0, statei = 0;
	public static long  ltime = System.currentTimeMillis(), time, last = ltime;
	public static Game mthis;
	
	public Texture black, purple;
	public Texture[] images;
	public Audio[] sounds;
	
	public Country[] countries = new Country[6];
	
	public int[][] text;
	public String[] texts;
	public boolean[] colours;
	public boolean retry = false, showbd = false, PAUSE = false;

	public int mousex, mousey, translate_x, translate_y;
	
	public Game(int chosen){
		player = chosen;
		sel = chosen;
	}
	
	public boolean run() {

		Game.mthis = this;
		Init.init_game();
		
		sounds[0].playAsMusic(1.0f, 1.0f, true);
		
		run = true;
		
		while (run) {
			
			glClear(GL_COLOR_BUFFER_BIT);
			
			glPushMatrix();
			
			glTranslatef(-translate_x, -translate_y, 0);
			
			time = System.currentTimeMillis();
			
			if (Display.wasResized()) {
	            RWIDTH = Display.getWidth();
	            RHEIGHT = Display.getHeight();

	            GL11.glViewport(0, 0, RWIDTH, RHEIGHT);
	            GL11.glLoadIdentity();
			}
			
			// Retrieve the "true" coordinates of the mouse.
			mousex = (int) (Mouse.getX() * ((float)(WIDTH) / RWIDTH) - -translate_x);
			mousey = (int) (HEIGHT - Mouse.getY() * ((float)(HEIGHT) / RHEIGHT) - 1 - -translate_y);
			
			if(!PAUSE){
				logic();
			} else {
				texts[2] = "Paused";
				text[2][0] = WIDTH / 2 - FONTS[2].getWidth(texts[2]) / 2;
				state = 0;
			}
			if(!run){
				break;
			}
			render();
			update();
			
			if(Display.isCloseRequested()){
				run = false;
				main.Main.mthis.end();
			}

			glPopMatrix();
			Display.update();
			Display.sync(60);
		}
		
		Setup.RWIDTH = RWIDTH;
		Setup.RHEIGHT = RHEIGHT;
		
		return retry;
		
	}
	
	public void logic(){
		
		boolean trip = false;
		//System.out.println(countries[sel].home[0] - mousex);
		for(int i = 0; i != countries.length; i++){
			if(100 > Math.sqrt(Math.pow(countries[i].home[0] - (mousex), 2) + Math.pow(countries[i].home[1] - (mousey), 2))){
				hover = i;
				trip = true;
			}
		}
		if(!trip){
			hover = -1;
		}
		
		Mouse.poll();
		event: while(Mouse.next()){
			if(Mouse.getEventButtonState()){
				int eventKey = Mouse.getEventButton();
				switch(eventKey){
				case 0:
					switch(state){
					case 0:
						for(int i = 0; i != text.length; i++){
							if(mousex - translate_x >= text[i][0] && mousex - translate_x <= text[i][0] + Main.FONT.getWidth(texts[i]) && mousey - translate_y >= text[i][1] && mousey - translate_y <= text[i][1] + Main.FONT.getHeight(texts[i])){
								switch(i){
								case 0:
									Menu menu = new Menu();
									menu.translate_x = translate_x;
									menu.translate_y = translate_y;
									menu.run(false);
									menu = null;
									break;
								case 3:
									if(countries[player].reserves == 0){break;}
									state = 2;
									System.out.println("HI");
									break;
								}
							}
						}
						if(mousey - translate_y < 100 || showbd){
							break;
						}
						int closest = sel;
						double dist = Math.sqrt(Math.pow(countries[sel].home[0] - (mousex), 2) + Math.pow(countries[sel].home[1] - (mousey), 2));
						for(int i = 0; i != countries.length; i++){
							if(dist > Math.sqrt(Math.pow(countries[i].home[0] - (mousex), 2) + Math.pow(countries[i].home[1] - (mousey), 2))){
								sel = i;
								dist = Math.sqrt(Math.pow(countries[sel].home[0] - (mousex), 2) + Math.pow(countries[sel].home[1] - (mousey), 2));
							}
						}
						for(int i = 0; i != countries[player].army.size(); i++){
							if(50 > Math.sqrt(Math.pow(countries[player].army.get(i)[0] - (mousex), 2) + Math.pow(countries[player].army.get(i)[1] - (mousey), 2))){
								selsol = i;
								state = 3;
							}
						}
						break;
					case 1:
						switch(statei){
						case 0:
							if(countries[player].money < 20){break;}
							if(100 < Math.sqrt(Math.pow(countries[player].home[0] - mousex, 2) + Math.pow(countries[player].home[1] - mousey, 2))){
								texts[2] = "You cannot build a unit that far from base!";
								text[2][0] = WIDTH / 2 - FONTS[2].getWidth(texts[2]) / 2;
								state = 0;
								break event;
							}
							countries[player].armySize++;
							countries[player].armyAdd(new int[]{mousex + solw / 2, mousey + solh / 2});
							countries[player].money -= 20;
							break;
						case 1:
							if(countries[player].money < 50){break;}
							if(200 < Math.sqrt(Math.pow(countries[player].home[0] - mousex, 2) + Math.pow(countries[player].home[1] - mousey, 2))){
								texts[2] = "You cannot build a mine that far from base!";
								text[2][0] = WIDTH / 2 - FONTS[2].getWidth(texts[2]) / 2;
								state = 0;
								break event;
							}
							countries[player].mines.add(new int[]{mousex + minw / 2, mousey + minh / 2});
							countries[player].money -= 50;
							break;
						case 2:
							if(countries[player].money < 30){break;}
							if(300 < Math.sqrt(Math.pow(countries[player].home[0] - mousex, 2) + Math.pow(countries[player].home[1] - mousey, 2))){
								texts[2] = "You cannot build a den that far from base!";
								text[2][0] = WIDTH / 2 - FONTS[2].getWidth(texts[2]) / 2;
								state = 0;
								break event;
							}
							countries[player].dens.add(new int[]{mousex + opw / 2, mousey + oph / 2});
							countries[player].money -= 30;
							break;
						}
						state = 0;
						texts[2] = "";
						break;
					case 2:
						if(100 < Math.sqrt(Math.pow(countries[player].home[0] - mousex, 2) + Math.pow(countries[player].home[1] - mousey, 2))){
							texts[2] = "You cannot build a unit that far from base!";
							text[2][0] = WIDTH / 2 - FONTS[2].getWidth(texts[2]) / 2;
							state = 0;
							break event;
						}
						countries[player].armySize++;
						countries[player].reserves--;
						countries[player].ready.add(new int[]{mousex + solw / 2, mousey + solh / 2});
						if(countries[player].reserves == 0){
							state = 0;
						}
						break;
					case 3:
						countries[player].aims.add(selsol, new int[]{mousex, mousey});
						state = 0;
						break;
					}
					break;
				case 1:
					state = 0;
					texts[2] = "";
					break;
				default:
					//System.out.println(eventKey);
					break;
				}
			}
		}
		
		Keyboard.poll();
		while(Keyboard.next()){
			if(Keyboard.getEventKeyState()){
				int eventKey = Keyboard.getEventKey();
				switch(eventKey){
				case KEY_ESCAPE:
					Menu menu = new Menu();
					menu.translate_x = translate_x;
					menu.translate_y = translate_y;
					menu.run(false);
					menu = null;
					break;
				default:
					break;
				}
			}
		}
		
		/*for(int i = 0; i != countries[player].army.size(); i++){
			try{
			countries[player].move(countries[player].aims.get(i)[0], countries[player].aims.get(i)[1], i);
			} catch(IndexOutOfBoundsException e){
				countries[player].aims.remove(i);
				countries[player].aims.add(i, new int[]{countries[player].army.get(i)[0], countries[player].army.get(i)[1]});
				System.out.println(countries[player].aims.size());
			}
		}*/
		
	}
	
	public void render(){
		
		Draw.renderthistex(new Rectangle(0,0, SWIDTH, SHEIGHT), images[0]);
		
		for(Country country: countries){
			for(int[] sol: country.army){
				Draw.renderthistex(new Rectangle(sol[0] - solw / 2, sol[1] - solh / 2, solw, solh), images[1]);
			}
			for(int[] mine: country.mines){
				Draw.renderthistex(new Rectangle(mine[0] - minw / 2, mine[1] - minh / 2, minw, minh), images[2]);
			}
			for(int[] den: country.dens){
				Draw.renderthistex(new Rectangle(den[0] - opw / 2, den[1] - oph / 2, opw, oph), images[3]);
			}
		}
		
		Draw.renderthiso(new Rectangle(countries[sel].home[0] - 100, countries[sel].home[1] - 100, 200, 200), 1, 1, 1, 0.2f);
		if(hover != -1){
			Init.renderBox(mousex, mousey, countries[hover].money, countries[hover].income, countries[hover].armySize, countries[hover].reserves);
		}
		
		Draw.renderthistex(new Rectangle(translate_x, translate_y, WIDTH, 100), black, 0.6f);
		
		switch(state){
		case 0:
			break;
		case 1:
			switch(statei){
			case 0:
				Draw.renderthistex(new Rectangle(mousex, mousey, solw, solh), images[1]); 
				break;
			case 1:
				Draw.renderthistex(new Rectangle(mousex, mousey, minw, minh), images[2]); 
				break;
			case 2:
				Draw.renderthistex(new Rectangle(mousex, mousey, opw, oph), images[3]); 
				break;
			}
			break;
		case 2:
			Draw.renderthistex(new Rectangle(mousex, mousey, solw, solh), images[1]); 
			break;
		}
		
		if(showbd){
			Init.renderBuilds();
		}
		FONTS[2].drawString(translate_x, translate_y, "Money: " + countries[player].money);
		FONTS[2].drawString(translate_x, translate_y + FONTS[2].getHeight("Money"), "Income: " + countries[player].income);
		FONTS[2].drawString(translate_x + FONTS[2].getWidth("Money :     " + countries[player].money), translate_y, "Army: " + countries[player].armySize);
		FONTS[2].drawString(translate_x + FONTS[2].getWidth("Money :     " + countries[player].money), translate_y + FONTS[2].getHeight("Army"), "Reserves: " + countries[player].reserves);
		
		for(int i = 0; i != text.length; i++){
			int temp = colours[i]?3:2;
			if(i == 1 && showbd){temp = 3;}
			FONTS[temp].drawString(text[i][0] + translate_x, text[i][1] + translate_y, texts[i]);
		}
		
	}
	
	public void update(){
		
		if(isKeyDown(KEY_SPACE) || isKeyDown(KEY_P)){
			PAUSE = true;
		} else {
			PAUSE = false;
		}
		
		// Buttons
		
		for(int i = 0; i != text.length; i++){
			if(mousex - translate_x >= text[i][0] && mousex - translate_x <= text[i][0] + FONTS[2].getWidth(texts[i]) && mousey - translate_y >= text[i][1] && mousey - translate_y <= text[i][1] + FONTS[2].getHeight(texts[i])){
				colours[i] = true;
			} else {
				colours[i] = false;
			}
			if(i == 1 && (mousex - translate_x >= text[i][0] && mousex - translate_x <= text[i][0] + FONTS[2].getWidth(texts[i]) && mousey - translate_y >= text[i][1] && mousey - translate_y <= text[i][1] + FONTS[2].getHeight(texts[i]))){
				showbd = true;
			} else if(i == 1){
				if((mousex - translate_x >= text[i][0] && mousex - translate_x <= text[i][0] + FONTS[2].getWidth(texts[i]) && mousey - translate_y >= text[i][1] && mousey - translate_y <= text[i][1] + FONTS[2].getHeight(texts[i]) + 200)){
					//showbd = true;
				} else {
					showbd = false;
				}
			}
		}
		
		// Buttons
		
		// States
		
		if(state == 1){
			texts[2] = "Building a" + (statei == 0?" unit":(statei == 1?" mine":"n opium den"));
			text[2][0] = WIDTH / 2 - FONTS[2].getWidth(texts[2]) / 2;
		}
		
		// States
		
		if(time - last > 10000){
			for(Country country: countries){
				country.update();
			}
			last = time;
		}
		
		if(isKeyDown(KEY_LEFT) || mousex - translate_x < WIDTH / 10 && mousey - translate_y > 100){
			translate_x -= 3;
		} else if(isKeyDown(KEY_RIGHT) || mousex - translate_x > WIDTH - WIDTH / 10 && mousey - translate_y > 100){
			translate_x += 3;
		}
		if(isKeyDown(KEY_DOWN) || mousey - translate_y - 100 > (HEIGHT - 100) - (HEIGHT - 100) / 10){
			translate_y += 3;
		} else if(isKeyDown(KEY_UP) || mousey - translate_y - 100 < (HEIGHT - 100) / 10 && mousey - translate_y > 100){
			translate_y -= 3;
		}
		if(isKeyDown(KEY_SPACE)){
			translate_x = 0; translate_y = 0;
		}
		if(translate_x < 0){
			translate_x = 0;
		} else if(translate_x + WIDTH > SWIDTH){
			translate_x = SWIDTH - WIDTH;
		}
		if(translate_y < 0){
			translate_y = 0;
		} else if(translate_y + HEIGHT > SHEIGHT){
			translate_y = SHEIGHT - HEIGHT;
		}
		
	}
	
	public void end(){
		
		for(Audio sound: sounds){
			sound.stop();
			sound = null;
		}
		
		for(Texture tex:images){
			tex.release();
			tex = null;
		}
		
	}
	
}
