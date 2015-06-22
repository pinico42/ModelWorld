package old;

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
import org.newdawn.slick.openal.SoundStore;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

@SuppressWarnings("unused")
public class Game {
	
	public static final int WIDTH = 1000;
	public static final int HEIGHT = 1000;
	public static UnicodeFont FONT;
	public static int RWIDTH;
	public static int RHEIGHT;
	public static int twidth;
	public static int theight;
	
	public Texture black;
	public Texture purple;
	public Texture[] images;
	
	public int[][] text;
	public String[] texts;

	public boolean run = true;
	public int mousex, mousey, translate_x, translate_y;
	
	public Texture loadTex(String name){
		try {
			return TextureLoader.getTexture("PNG", new FileInputStream(new File("res/"+name+".png")));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void init_game() throws FileNotFoundException, IOException{
		
		theight = HEIGHT / 10;
		twidth = theight;
		images = new Texture[1];
		
		black = loadTex("black");
		purple = loadTex("purple");
		images[0] = loadTex("worldmap");
        
        texts = new String[]{"I have no settings"};
        text = new int[texts.length][];
		
        for(int i = 0; i != text.length; i++){
        	text[i] = new int[]{(int) (WIDTH / 2.3 - Main.FONT.getWidth(texts[i]) / 2), HEIGHT / (text.length + 1) * (i + 1) - Main.FONT.getHeight(texts[i])};
        }
		
	}
	
	public void run(boolean initi) {
		
		try {
			init_game();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		while (run) {
			
			glClear(GL_COLOR_BUFFER_BIT);
			
			glPushMatrix();
			
			glTranslatef(translate_x, translate_y, 0);
			
			if (Display.wasResized()) {
	            RWIDTH = Display.getWidth();
	            RHEIGHT = Display.getHeight();

	            GL11.glViewport(0, 0, RWIDTH, RHEIGHT);
	            GL11.glLoadIdentity();
			}
			
			// Retrieve the "true" coordinates of the mouse.
			mousex = (int) (Mouse.getX() * ((float)(WIDTH) / RWIDTH) - translate_x);
			mousey = (int) (HEIGHT - Mouse.getY() * ((float)(HEIGHT) / RHEIGHT) - 1 - translate_y);
			Draw.renderthistex(new Rectangle(0,0,WIDTH,HEIGHT), purple);
			
			logic();
			render();
			update();
			
			if(Display.isCloseRequested()){
				run = false;
				Main.mthis.end();
			}

			glPopMatrix();
			Display.update();
			Display.sync(60);
		}
		
		Main.RWIDTH = RWIDTH;
		Main.RHEIGHT = RHEIGHT;
		
	}
	
	public void logic(){
		
		if(isKeyDown(KEY_LEFT)){
			translate_x += 3;
		} else if(isKeyDown(KEY_RIGHT)){
			translate_x -= 3;
		}
		if(isKeyDown(KEY_DOWN)){
			translate_y += -3;
		} else if(isKeyDown(KEY_UP)){
			translate_y -= -3;
		}
		if(isKeyDown(KEY_SPACE)){
			translate_x = 0; translate_y = 0;
		}
		
		Mouse.poll();
		while(Mouse.next()){
			if(Mouse.getEventButtonState()){
				int eventKey = Mouse.getEventButton();
				switch(eventKey){
				case 0:
					for(int i = 0; i != text.length; i++){
						//System.out.println(mousex +" >= "+ text[i][0] +" && "+ mousex +" <= "+ (text[i][0] + Main.FONT.getWidth(texts[i])) +" && "+ mousey +" >= "+ text[i][1] +" && "+ mousey +" <= "+ (text[i][1] + Main.FONT.getHeight(texts[i])));
						
						System.out.println("HERE");
						if(mousex >= text[i][0] && mousex <= text[i][0] + Main.FONT.getWidth(texts[i]) && mousey >= text[i][1] && mousey <= text[i][1] + Main.FONT.getHeight(texts[i])){
							
							System.out.println("HERE1");
							switch(i){
							case 0:
								texts[0] = "This is no game!";
								break;
							case 1:
								break;
							}
						}
					}
					break;
				default:
					System.out.println(eventKey);
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
					run = false;
					break;
				default:
					break;
				}
			}
		}
		
	}
	
	public void render(){
		
		for(int i = 0; i != text.length; i++){
			Main.FONT.drawString(text[i][0], text[i][1], texts[i]);
		}
		
		Draw.renderthistex(new Rectangle(0,0, WIDTH / 2, HEIGHT / 2), images[0]);
		
	}
	
	public void update(){
		
	}
	
	public void end(){
		
		
		
	}
	
}
