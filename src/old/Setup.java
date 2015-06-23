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
public class Setup {
	
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
	public int[][] text2;
	public String[] texts2;

	public boolean run = true;
	public int mousex, mousey, translate_x, translate_y;
	
	@SuppressWarnings("unchecked")
	public void init_LWJGL(int[] settings){

		RWIDTH = Display.getDesktopDisplayMode().getWidth() - 10;
		RHEIGHT = Display.getDesktopDisplayMode().getHeight() - 50;
		
		DisplayMode[] modes = null;
		try {
			modes = Display.getAvailableDisplayModes();
		} catch (LWJGLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		 
		for (int i=0;i<modes.length;i++) {
		    DisplayMode current = modes[i];
		    System.out.println(current.getWidth() + "x" + current.getHeight() + "x" +
		                        current.getBitsPerPixel() + " " + current.getFrequency() + "Hz");
		}
		
		//System.out.println(Display.getDesktopDisplayMode().getWidth());
		
		try {
			Display.setInitialBackground(255, 255, 255);
			Display.setDisplayMode(new DisplayMode(RWIDTH, RHEIGHT));
			Display.setResizable(true);
			Display.setLocation(0, 0);
			Display.setFullscreen(true);
			Display.setTitle("Captain Wiggles");
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, WIDTH, HEIGHT, 0, -1, 10);
		glMatrixMode(GL_MODELVIEW);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        
		Font awtFont = new Font("Times New Roman", Font.BOLD, 60);
		FONT = new UnicodeFont(awtFont);
		FONT.addAsciiGlyphs();
		FONT.addGlyphs(400, 600);
		FONT.getEffects().add(new ColorEffect(java.awt.Color.WHITE));
		try {
		    FONT.loadGlyphs();
		} 
		catch (SlickException e){
		    System.out.println("something went wrong here!");
		    e.printStackTrace();
		    Display.destroy();
		}
        glEnable(GL_TEXTURE_2D);
        
        texts = new String[]{"Play", "Settings"};
        text = new int[texts.length][];
		
        for(int i = 0; i != text.length; i++){
        	text[i] = new int[]{(int) (WIDTH / 2.3 - FONT.getWidth(texts[i]) / 2), HEIGHT / (text.length + 1) * (i + 1) - FONT.getHeight(texts[i])};
        }
	}
	
	public void init_game(int[] settings){
		Main.FONT.drawString(0, HEIGHT - Main.FONT.getHeight("Back"), "Back");
		texts2 = new String[]{"Back", "Next"};
		text2 = new int[][]{{0, HEIGHT - Main.FONT.getHeight("Back")}};
		
		theight = HEIGHT / 10;
		twidth = theight;
		//images = new Texture[15];
		
		try {
			black = TextureLoader.getTexture("PNG", new FileInputStream(new File("res/black.png")));
			purple = TextureLoader.getTexture("PNG", new FileInputStream(new File("res/purple.png")));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
        
        setupStrings(new String[]{"Hey"});
		
	}
	
	public void setupStrings(String[] newa){
		
		texts = newa;
        text = new int[texts.length][];
		
		for(int i = 0; i != text.length; i++){
        	text[i] = new int[]{(int) (WIDTH / 2.3 - Main.FONT.getWidth(texts[i]) / 2), HEIGHT / (text.length + 1) * (i + 1) - Main.FONT.getHeight(texts[i])};
        }
	}
	
	public void init(boolean initi){
		int[] settings = IOHandle.getSettings();
		if(initi){
			init_LWJGL(settings);
		} else {
			RWIDTH = Main.RWIDTH;
			RHEIGHT = Main.RHEIGHT;
		}
		init_game(settings);
	}
	
	public void run(boolean initi) {
		
		FONT = Main.FONT;
		
		init(initi);
		
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
			mousey = (int) (HEIGHT - Mouse.getY() * ((float)(HEIGHT) / RHEIGHT) - 1);
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
		
		/*int pix, piy;
		pix = (int) mousex / twidth;
		piy = (int) mousey / theight;
		if(pix > 149 || piy > 9 || pix < 0 || piy < 0){
			return;
		}*/
		
		if(isKeyDown(KEY_LEFT)){
			translate_x += 3;
		} else if(isKeyDown(KEY_RIGHT)){
			translate_x -= 3;
		}
		if(isKeyDown(KEY_DOWN)){
			translate_y += 3;
		} else if(isKeyDown(KEY_UP)){
			translate_y -= 3;
		}
		
		Mouse.poll();
		while(Mouse.next()){
			if(Mouse.getEventButtonState()){
				int eventKey = Mouse.getEventButton();
				switch(eventKey){
				case 0:
					for(int i = 0; i != text.length; i++){
						if(mousex >= text[i][0] && mousex <= text[i][0] + Main.FONT.getWidth(texts[i]) && mousey >= text[i][1] && mousey <= text[i][1] + Main.FONT.getHeight(texts[i])){
							switch(i){
							case 0:
								texts[0] = "This is no game!";
								break;
							case 1:
								break;
							}
						}
					}
					for(int i = 0; i != text.length; i++){
						if(mousex >= text2[i][0] && mousex <= text2[i][0] + Main.FONT.getWidth(texts2[i]) && mousey >= text2[i][1] && mousey <= text2[i][1] + Main.FONT.getHeight(texts2[i])){
							switch(i){
							case 0:
								run = false;
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
		
		for(int i = 0; i != text2.length; i++){
			Main.FONT.drawString(text2[i][0], text2[i][1], texts2[i]);
		}
		
		//Main.FONT.drawString(0, HEIGHT - Main.FONT.getHeight("Back"), "Back");
		
	}
	
	public void update(){
		
	}
	
	public void end(){
		
		
		
	}
	
	//public static void main(String[] args) {
		//new Settings().run(true);
	//}
	
}
