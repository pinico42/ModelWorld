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
import java.util.ArrayList;
import java.util.Random;
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
	
	public static final Random rand = new Random();
	public static final int SWIDTH = 2840, SHEIGHT = 1310;
	public static int WIDTH = 1000, HEIGHT = 1000;
	public static boolean run = true;
	public static UnicodeFont[] FONTS;
	public static int RHEIGHT, RWIDTH, theight, twidth, player = 0, sel, selsol, solw = 40, solh = 100, minw = 100, minh = 100, opw = 100, oph = 100, hover = -1, state = 0, statei = 0, homew = 100, homeh = 100, temw = 100, temh = 100, lastClick = 0;
	public static long  ltime = System.currentTimeMillis(), time, last = ltime;
	public static Game mthis;
	public static int[] autos;
	
	public Texture black, purple;
	public Texture[] images;
	public Audio[] sounds;
	
	public ArrayList<Button> buttons = new ArrayList<Button>();
	public Country[] countries = new Country[6];
	
	public int[][] text;
	public String[] texts;
	public boolean[] colours, dcolours;
	public boolean retry = false, showbd = false, PAUSE = false, wintrip = false;

	public int mousex, mousey, translate_x, translate_y, deathCount = -1;
	private int[] doubleClick;
	
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
			mousex = (int) (Mouse.getX() * ((double)(WIDTH) / RWIDTH) - -translate_x);
			mousey = (int) (HEIGHT - Mouse.getY() * ((double)(HEIGHT) / RHEIGHT) - 1 - -translate_y);
			
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
			if(!PAUSE){
				update();
			} else {
				if(isKeyDown(KEY_P)){
					PAUSE = !PAUSE;
				}
			}
			
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
		
		for(Button button: buttons){
			button.update();
		}
		
		boolean trip = false;
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
					for(Button button: buttons){
						if(button.hover){
							switch(button.id){
							case 0:
								int selected = Init.selected<player?Init.selected:Init.selected+1;
								Country.wars[player][selected] = !Country.wars[player][selected];
								Country.wars[selected][player] = Country.wars[player][selected];
								if(Country.wars[selected][player]){
									countries[player].nwars++;
								} else {
									countries[player].nwars--;
								}
								break;
							case 1:
								autos[0] = autos[0]==0?1:0;
								break;
							case 2:
								autos[1] %= 10;
								autos[1]++;
								break;
							case 3:
								autos[2] %= 11;
								autos[2] += autos[2]%10<2?1:2;
								break;
							}
						}
					}
					switch(state){
					case 0:
						for(int i = 0; i != text.length; i++){
							if(mousex - translate_x >= text[i][0] && mousex - translate_x <= text[i][0] + FONTS[2].getWidth(texts[i]) && mousey - translate_y >= text[i][1] && mousey - translate_y <= text[i][1] + FONTS[2].getHeight(texts[i])){
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
									break;
								case 4:
									state = 4;
									break;
								case 5:
									state = 5;
									buttons.add(Init.buttons[1]);
									buttons.add(Init.buttons[2]);
									buttons.add(Init.buttons[3]);
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
							if(50 > Math.sqrt(Math.pow(countries[player].army.get(i).pos[0] - (mousex), 2) + Math.pow(countries[player].army.get(i).pos[1] - (mousey), 2))){
								selsol = i;
								state = 3;
							}
						}
						break;
					case 1:
						switch(statei){
						case 0:
							if(countries[player].money < Country.scost * autos[1]){break;}
							if(Country.sdist < Math.sqrt(Math.pow(countries[player].home[0] - mousex, 2) + Math.pow(countries[player].home[1] - mousey, 2))){
								texts[2] = "You cannot build a unit that far from base!";
								text[2][0] = WIDTH / 2 - FONTS[2].getWidth(texts[2]) / 2;
								state = 0;
								break event;
							}
							countries[player].armySize += autos[1];
							for(int i = 0; i != autos[1]; i++){
								countries[player].armyAdd(new int[]{mousex + solw / 2, mousey + solh / 2});
							}
							countries[player].money -= Country.scost * autos[1];
							break;
						case 1:
							if(countries[player].money < Country.mcost * autos[1]){break;}
							if(Country.mdist < Math.sqrt(Math.pow(countries[player].home[0] - mousex, 2) + Math.pow(countries[player].home[1] - mousey, 2))){
								texts[2] = "You cannot build a mine that far from base!";
								text[2][0] = WIDTH / 2 - FONTS[2].getWidth(texts[2]) / 2;
								state = 0;
								break event;
							}
							for(int i = 0; i != autos[1]; i++){
								countries[player].mines.add(new int[]{mousex + minw / 2, mousey + minh / 2});
							}
							countries[player].money -= Country.mcost * autos[1];
							break;
						case 2:
							if(countries[player].money < Country.ocost * autos[1]){break;}
							if(Country.odist < Math.sqrt(Math.pow(countries[player].home[0] - mousex, 2) + Math.pow(countries[player].home[1] - mousey, 2))){
								texts[2] = "You cannot build a den that far from base!";
								text[2][0] = WIDTH / 2 - FONTS[2].getWidth(texts[2]) / 2;
								state = 0;
								break event;
							}
							for(int i = 0; i != autos[1]; i++){
								countries[player].dens.add(new int[]{mousex + opw / 2, mousey + oph / 2});
							}
							countries[player].money -= Country.ocost * autos[1];
							break;
						case 3:
							if(countries[player].money < Country.tcost * autos[1]){break;}
							if(Country.tdist < Math.sqrt(Math.pow(countries[player].home[0] - mousex, 2) + Math.pow(countries[player].home[1] - mousey, 2))){
								texts[2] = "You cannot build a temple that far from base!";
								text[2][0] = WIDTH / 2 - FONTS[2].getWidth(texts[2]) / 2;
								state = 0;
								break event;
							}
							for(int i = 0; i != autos[1]; i++){
								countries[player].temples.add(new int[]{mousex + temw / 2, mousey + temh / 2});
							}
							countries[player].money -= Country.tcost * autos[1];
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
						if(autos[2] == 1){
							try{
								countries[player].army.get(selsol).setAim(mousex, mousey);
							} catch (IndexOutOfBoundsException e){
								texts[2] = "That soldier died!";
							}
						} else {
							if(autos[2] == 11){
								for(int i = 0; i != countries[player].armySize;i++){
									countries[player].army.get(i).setAim(mousex, mousey);
								}
							} else {
								for(int i = 0; i != autos[2];i++){
									try{
										countries[player].army.get(i).setAim(mousex, mousey);
									} catch (IndexOutOfBoundsException e){
										texts[2] = "Not enough soldiers";
									}
								}
							}
						}
						state = 0;
						break;
					case 4:
						if(Init.selected == -1){
							trip = false;
							for(int i = 0; i != Init.names.length; i++){
								if(mousex - translate_x >= Init.points[i][0] && mousex - translate_x <= Init.points[i][0] + FONTS[1].getWidth(Init.names[i]) && mousey - translate_y >= Init.points[i][1] && mousey - translate_y <= Init.points[i][1] + FONTS[1].getHeight(Init.names[i])){
									Init.selected = i;
									buttons.add(Init.buttons[0]);
									trip = true;
								}
							}
						}
						if(mousex - translate_x >= text[4][0] && mousex - translate_x <= text[4][0] + FONTS[2].getWidth(texts[4]) && mousey - translate_y >= text[4][1] && mousey - translate_y <= text[4][1] + FONTS[2].getHeight(texts[4])){
							if(Init.selected != -1){
								Init.selected = -1;
								if(buttons.contains(Init.buttons[0])){
									buttons.remove(Init.buttons[0]);
								}
								break;
							}
							state = 0;
							texts[2] = "";
						}
						/*if(!trip){
							Init.selected = -1;
							if(buttons.contains(Init.buttons[0])){
								buttons.remove(Init.buttons[0]);
							}
						}*/
					case 5:
						if(mousex - translate_x >= text[5][0] && mousex - translate_x <= text[5][0] + FONTS[2].getWidth(texts[5]) && mousey - translate_y >= text[5][1] && mousey - translate_y <= text[5][1] + FONTS[2].getHeight(texts[5])){
							if(buttons.contains(Init.buttons[1])){
								buttons.remove(Init.buttons[1]);
							}
							if(buttons.contains(Init.buttons[2])){
								buttons.remove(Init.buttons[2]);
							}
							if(buttons.contains(Init.buttons[3])){
								buttons.remove(Init.buttons[3]);
							}
							state = 0;
							texts[2] = "";
						}
						break;
					default:
						break;
					}
					break;
				case 1:
					if(state == 4 && Init.selected != -1){
						Init.selected = -1;
						if(buttons.contains(Init.buttons[0])){
							buttons.remove(Init.buttons[0]);
						}
						break;
					}
					if(state == 5){
						if(buttons.contains(Init.buttons[1])){
							buttons.remove(Init.buttons[1]);
						}
						if(buttons.contains(Init.buttons[2])){
							buttons.remove(Init.buttons[2]);
						}
						if(buttons.contains(Init.buttons[3])){
							buttons.remove(Init.buttons[3]);
						}
					}
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
		
		if(deathCount == -1){
			int tripa = 0;
			for(Country country: countries){
				if(country.type == player){continue;}
				if(country.die){
					tripa++;
				}
				//System.out.println(Country.names[country.type]+ " - " +country.die);
				for(Sol sol: country.army){
					if(Matha.hypo(sol.pos[0] - countries[player].home[0], sol.pos[1] - countries[player].home[1]) < solw){
						deathCount = 1800;
					}
				}
			}
			if(tripa == 5){
				texts[2] = "You won!";
				text[2][0] = WIDTH / 2 - FONTS[2].getWidth(texts[2]) / 2;
				if(!wintrip && player == 5-Main.unlocked){
					//System.out.println("TTT" + player + " - " + (5-Main.unlocked));
					if(Main.unlocked != 5){
						Main.unlocked++;
					}
					wintrip = true;
				}
			}
		} else {
			if(deathCount != 0){
				countries[player].reserves = 0;
				deathCount--;
				texts[2] = Integer.toString(deathCount / 60);
				for(Sol sol: countries[player].army){
					if(Matha.hypo(sol.pos[0] - countries[player].home[0], sol.pos[1] - countries[player].home[1]) < solw){
						deathCount = -1;
						texts[2] = "";
					}
				}
			} else {
				texts[2] = "You failed";
				countries[player].clear();
				countries[player].die = true;
			}
		}
		
	}
	
	public void render(){
		
		Draw.renderthistex(new Rectangle(0,0, SWIDTH, SHEIGHT), images[0]);
		
		for(Country country: countries){
			for(int[] mine: country.mines){
				Draw.renderthistex(new Rectangle(mine[0] - minw / 2, mine[1] - minh / 2, minw, minh), images[2]);
			}
			for(int[] den: country.dens){
				Draw.renderthistex(new Rectangle(den[0] - opw / 2, den[1] - oph / 2, opw, oph), images[3]);
			}
			Draw.renderthistex(new Rectangle(country.home[0] - homew / 2, country.home[1] - homeh / 2, homew, homeh), country.die?images[5]:images[4]);
			for(Sol sol: country.army){
				int img = 1;
				if(sol.christian){
					img = 7;
				}
				Draw.renderthistex(new Rectangle(sol.pos[0] - solw / 2, sol.pos[1] - solh / 2, solw, solh), images[img]);
			}
			for(int[] tem: country.temples){
				Draw.renderthistex(new Rectangle(tem[0] - temw / 2, tem[1] - temh / 2, temw, temh), images[6]);
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
			case 3:
				Draw.renderthistex(new Rectangle(mousex, mousey, temw, temh), images[6]);
			}
			break;
		case 2:
			Draw.renderthistex(new Rectangle(mousex, mousey, solw, solh), images[1]); 
			break;
		case 4:
			Init.renderDiplomacy();
			break;
		case 5:
			Init.renderAuto();
			break;
		}
		
		if(showbd){
			Init.renderBuilds();
			texts[5] = "Auto"; 
		} else {
			texts[5] = "Automation";
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
		
		for(Button button: buttons){
			button.render();
		}
		
		if(doubleClick != null){
			int width = Math.abs(doubleClick[0] - mousex);
			int height = Math.abs(doubleClick[1] - mousey);
			int x = mousex>doubleClick[0]?doubleClick[0]:mousex, y = mousey>doubleClick[1]?doubleClick[1]:mousey;
			Draw.renderthiso(new Rectangle(x, y, width, height), 1f, 1f, 1f, 0.4f);
			System.out.println("Epic drawzzz "+x+" "+y+" "+width+" "+height);
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
		
		switch(state){
		case 4:
			for(int i = 0; i != Init.names.length; i++){
				if(mousex - translate_x >= Init.points[i][0] && mousex - translate_x <= Init.points[i][0] + FONTS[1].getWidth(Init.names[i]) && mousey - translate_y >= Init.points[i][1] && mousey - translate_y <= Init.points[i][1] + FONTS[1].getHeight(Init.names[i])){
					dcolours[i] = true;
				} else {
					dcolours[i] = false;
				}
			}
			break;
		default:
			break;
		}
		
		// /Buttons
		
		// Mouse
		
		if(lastClick != 0){
			lastClick--;
		}
		if(Mouse.isButtonDown(0)){
			System.out.println("Click detected "+lastClick);
			if(lastClick == 0){
				lastClick = 10;
			} else if(doubleClick==null){
				System.out.println("Double cliicicikk");
				doubleClick = new int[]{mousex, mousey};
			}
		} else if(doubleClick != null){
			doubleClick = null;
		}
		
		// /Mouse
		
		// States
		
		if(state == 1){
			texts[2] = "Building a" + (statei == 0?" unit":(statei == 1?" mine":statei==2?"n opium den":" temple"));
			text[2][0] = WIDTH / 2 - FONTS[2].getWidth(texts[2]) / 2;
		}
		
		// /States
		
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
								//System.out.println("HEY");
								if(!(Country.wars[sol.owner][sold.owner] || Country.wars[sold.owner][sol.owner])){
									//System.out.println("GET");
									// I MANTAIN THIS IS USELESS <-- WATHCH OUT
									//if((sol.owner != player && sold.owner != player) || Matha.hypo((sol.owner==player?sol:sold).pos[0] - countries[player].home[0], (sol.owner==player?sol:sold).pos[1] - countries[player].home[1]) < Matha.hypo((sol.owner==player?sold:sol).pos[0] - countries[player].home[0], (sol.owner==player?sold:sol).pos[1] - countries[player].home[1])){
										Country.wars[sol.owner][sold.owner] = true;
										//System.out.println("Met");
										Country.wars[sold.owner][sol.owner] = true;
									//}
								}
							}
						}
						if(country.type == 1){
							//System.out.println("HEY2");
						}
						if(country.type != sold.owner && (Country.wars[country.type][sold.owner] || Country.wars[sold.owner][country.type])){
							if(country.type == 1){
								//System.out.println("HEY1");
							}
							if(solh > Matha.hypo(country.home[0] - sold.pos[0], country.home[1] - sold.pos[1])){
								if(country.type == 1){
									//System.out.println("HEY");
								}
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
		
		// /Soldiers
		
		if(time - last > 10000){
			for(Country country: countries){
				country.update();
			}
			last += 10000;
		}
		
		if(isKeyDown(KEY_LEFT) || mousex - translate_x < WIDTH / 10 && mousey - translate_y > 100){
			translate_x -= 3;
		} else if(isKeyDown(KEY_RIGHT) || mousex - translate_x > WIDTH - WIDTH / 10 && mousey - translate_y > 100){
			translate_x += 3;
		}
		if(isKeyDown(KEY_DOWN) || mousey - translate_y - 100 > (HEIGHT - 100) - (HEIGHT - 100) / 10){
			translate_y += 3;
		} else if(isKeyDown(KEY_UP) || mousey - translate_y - 100 < (HEIGHT - 100) / 10 && mousey - translate_y > 100 && (!showbd || mousey - translate_y < 200)){
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
		
		for(Country country: countries){
			for(Sol sol: country.army){
				sol.update();
			}
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
