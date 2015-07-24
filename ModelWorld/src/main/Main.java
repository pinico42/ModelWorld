package main;

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
import game.Game;

import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

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
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.openal.SoundStore;
import org.newdawn.slick.opengl.ImageIOImageData;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

@SuppressWarnings("unused")
public class Main {
	
	public static final boolean DEBUG = true;
	public static int RWIDTH, RHEIGHT, twidth, theight, unlocked;
	public static UnicodeFont FONT, FONT2;
	public static boolean run = true, SOUND = true;
	public static int WIDTH, HEIGHT;
	public static Main mthis;

	public Audio tempt;
	public Texture black;
	public Texture[] images;
	
	public boolean[] colours;
	public int[][] text;
	public String[] texts;
	
	public int mousex, mousey, translate_x, translate_y;
	
	@SuppressWarnings("unchecked")
	public void initFont(int size) {
	    Font font = new Font("Comic Sans MS", Font.BOLD, size);
	    FONT = new UnicodeFont(font);
	    FONT.addAsciiGlyphs();
	    FONT.addGlyphs(400, 600);
	    FONT.getEffects().add(new ColorEffect(java.awt.Color.white));
	    try {
	        FONT.loadGlyphs();
	    } catch (SlickException e) {
		    System.out.println("something went wrong here!");
		    e.printStackTrace();
		    Display.destroy();
	    }
	    
	    FONT2 = new UnicodeFont(font);
	    FONT2.addAsciiGlyphs();
	    FONT2.addGlyphs(400, 600);
	    FONT2.getEffects().add(new ColorEffect(java.awt.Color.blue));
	    try {
	        FONT2.loadGlyphs();
	    } catch (SlickException e) {
		    System.out.println("something went wrong here!");
		    e.printStackTrace();
		    Display.destroy();
	    }
	}
	
	private void init_LWJGL(int[] settings){

		RWIDTH = 1280;//Display.getDesktopDisplayMode().getWidth() - 10;
		RHEIGHT = 720;//Display.getDesktopDisplayMode().getHeight() - 50;
		
		WIDTH = settings[0];
		HEIGHT = settings[1];
		
		DisplayMode displayMode = null;
        DisplayMode[] modes = null;
		try {
			modes = Display.getAvailableDisplayModes();
		} catch (LWJGLException e1) {
			e1.printStackTrace();
		}

         for (int i = 0; i < modes.length; i++){
             if (modes[i].getWidth() == RWIDTH && modes[i].getHeight() == RHEIGHT && modes[i].isFullscreenCapable()){
                    displayMode = modes[i];
               }
         }
         
         if(displayMode == null){
        	 displayMode = new DisplayMode(500, 500);
         }
         
         if(DEBUG){
        	 displayMode = new DisplayMode(400, 400);
        	 RWIDTH = 400;
        	 RHEIGHT = 400;
         }
    	 
		try {
			Display.setInitialBackground(255, 255, 255);
			Display.setDisplayMode(displayMode);
			Display.setResizable(true);
			Display.setLocation(0, 0);
			Display.setFullscreen(settings[2] == 0);
			Display.setTitle("Model World");
	        try {
				Display.setIcon(new ByteBuffer[] {
				         new ImageIOImageData().imageToByteBuffer(ImageIO.read(new File("res/black2.png")), false, false, null),
				         new ImageIOImageData().imageToByteBuffer(ImageIO.read(new File("res/black2.png")), false, false, null)
				         });
			} catch (IOException e1) {
				e1.printStackTrace();
			}
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
        
        initFont(WIDTH == 500?30:60);
		
        glEnable(GL_TEXTURE_2D);
        
        setupStrings(new String[]{"Campaign", "Multiplayer", "Settings", "Credits", "Instructions", "Quit"});
	}
	
	public void setupStrings(String[] newa){
		
		texts = newa;
        text = new int[texts.length][];
        colours = new boolean[text.length];
		
		for(int i = 0; i != text.length; i++){
        	text[i] = new int[]{(int) (WIDTH / 2.3 - Main.FONT.getWidth(texts[i]) / 2), HEIGHT / (text.length + 1) * (i + 1) - Main.FONT.getHeight(texts[i])};
        }
	}
	
	private void init_game(int[] settings){
		
		try {
			tempt = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("res/tempt.wav"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		theight = HEIGHT / 10;
		twidth = theight;
		//images = new Texture[15];
		
		try {
			black = TextureLoader.getTexture("PNG", new FileInputStream(new File("res/black.png")));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		unlocked = settings[3];
		
	}
	
	private void init(){
		mthis = this;
		int[] settings = IOHandle.getSettings();
		init_LWJGL(settings);
		init_game(settings);
	}
	
	public void run() {
		
		init();
		
		tempt.playAsMusic(1.0f, 1.0f, true);
		
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
			Draw.renderthistex(new Rectangle(0,0,WIDTH, HEIGHT), black);
			
			logic();
			render();
			update();
			
			if(Display.isCloseRequested()){
				run = false;
			}

			glPopMatrix();
			Display.update();
			Display.sync(60);
		}

		end();
	}
	
	public void logic(){
		
		/*if(isKeyDown(KEY_LEFT)){
			translate_x += 3;
		} else if(isKeyDown(KEY_RIGHT)){
			translate_x -= 3;
		}
		if(isKeyDown(KEY_DOWN)){
			translate_y += -3;
		} else if(isKeyDown(KEY_UP)){
			translate_y -= -3;
		}*/
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
						//System.out.println(mousex +" >= "+ text[i][0] +" && "+ mousex +" <= "+ (text[i][0] + FONT.getWidth(texts[i])) +" && "+ mousey +" >= "+ text[i][1] +" && "+ mousey +" <= "+ (text[i][1] + FONT.getHeight(texts[i])));
						if(mousex >= text[i][0] && mousex <= text[i][0] + FONT.getWidth(texts[i]) && mousey >= text[i][1] && mousey <= text[i][1] + FONT.getHeight(texts[i])){
							switch(i){
							case 0:
								float at = tempt.getPosition();
								tempt.stop();
								new Setup().run(false);
								tempt.playAsMusic(1.0f, 1.0f, true);
								tempt.setPosition(at);
								break;
							case 1:
								float at1 = tempt.getPosition();
								tempt.stop();
								new MultiplayerSetup().run(false);
								tempt.playAsMusic(1.0f, 1.0f, true);
								tempt.setPosition(at1);
								break;
							case 2:
								Settings.QUALITY = WIDTH==500?1:0;
								new Settings().run(false);
								break;
							case 3:
								new Credits().run(false);
								break;
							case 4:
								new Instructions().run(false);
								break;
							case 5:
								end();
								break;
							}
						}
					}
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
					end();
					break;
				default:
					break;
				}
			}
		}
		
		for(int i = 0; i != text.length; i++){
			if(mousex >= text[i][0] && mousex <= text[i][0] + FONT.getWidth(texts[i]) && mousey >= text[i][1] && mousey <= text[i][1] + FONT.getHeight(texts[i])){
				colours[i] = true;
			} else {
				colours[i] = false;
			}
		}
		
	}
	
	public void render(){
		
		for(int i = 0; i != text.length; i++){
			if(colours[i]){
				FONT2.drawString(text[i][0], text[i][1], texts[i]);
				continue;
			}
			FONT.drawString(text[i][0], text[i][1], texts[i]);
		}
		
	}
	
	public void update(){
		
	}
	
	public void end(){

		IOHandle.writeSettings(new int[]{WIDTH, HEIGHT, Display.isFullscreen()?0:1, unlocked});
		Display.destroy();
		AL.destroy();
		System.exit(0);
		
	}
	
	public void restart(){
		
		Display.destroy();
		AL.destroy();
		
	}
	
	public static void main(String[] args) {
		new Main().run();
	}
	
}
