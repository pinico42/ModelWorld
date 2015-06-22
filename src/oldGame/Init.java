package game;

import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import main.Draw;
import main.Setup;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.Rectangle;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class Init {
	
	public static Texture loadTex(String name){
		try {
			return TextureLoader.getTexture("PNG", new FileInputStream(new File("res/"+name+".png")));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	
	@SuppressWarnings("unchecked")
	public static void init_game(){
		
		Game.mthis.sounds = new Audio[1];
		
		try {
			Game.mthis.sounds[0] = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("res/heart.wav"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		Game.FONTS = new UnicodeFont[4];
		Game.FONTS[0] = Setup.FONT;
		Game.FONTS[1] = Setup.FONT2;
		
		Font font = new Font("Comic Sans MS", Font.BOLD, 30);
	    UnicodeFont FONT = new UnicodeFont(font);
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
	    Game.FONTS[2] = FONT;
	    
	    FONT = new UnicodeFont(font);
	    FONT.addAsciiGlyphs();
	    FONT.addGlyphs(400, 600);
	    FONT.getEffects().add(new ColorEffect(java.awt.Color.blue));
	    try {
	        FONT.loadGlyphs();
	    } catch (SlickException e) {
		    System.out.println("something went wrong here!");
		    e.printStackTrace();
		    Display.destroy();
	    }
	    Game.FONTS[3] = FONT;
		
		Game.RWIDTH = Setup.RWIDTH;
		Game.RHEIGHT = Setup.RHEIGHT;
		
		Game.theight = Game.HEIGHT / 10;
		Game.twidth = Game.theight;
		Game.mthis.images = new Texture[4];
		
		Game.mthis.black = loadTex("black");
		Game.mthis.purple = loadTex("purple");
		Game.mthis.images[0] = loadTex("worldmap");
		Game.mthis.images[1] = loadTex("man");
		Game.mthis.images[2] = loadTex("mine");
		Game.mthis.images[3] = loadTex("opium");

		Game.mthis.translate_y = -100;
		
		Game.mthis.texts = new String[]{"Menu", "Build", "", "Place reserves"};
		Game.mthis.text = new int[][]{{Game.WIDTH - Game.FONTS[2].getWidth(Game.mthis.texts[0]),0} , {Game.WIDTH - Game.FONTS[2].getWidth(Game.mthis.texts[0] + "    ") - Game.FONTS[2].getWidth(Game.mthis.texts[1]), 0}, {Game.WIDTH / 2, Game.HEIGHT / 2}, {}};//[Game.mthis.texts.length][];
		Game.mthis.text[3] = new int[]{Game.mthis.text[1][0] - Game.FONTS[2].getWidth(Game.mthis.texts[3] + "    "), 0};
		Game.mthis.colours = new boolean[Game.mthis.texts.length];
		
        //for(int i = 0; i != Game.mthis.text.length; i++){
        	//Game.mthis.text[i] = new int[]{(int) (Game.WIDTH / 2.3 - Setup.FONT.getWidth(Game.mthis.texts[i]) / 2), Game.HEIGHT / (Game.mthis.text.length + 1) * (i + 1) - Setup.FONT.getHeight(Game.mthis.texts[i])};
        //}
        
        for(int i = 0; i != 6; i++){
        	Game.mthis.countries[i] = new Country(i);
        }
		
	}
	
	@SuppressWarnings("unchecked")
	public static void setFonts(int size){
		main.Main.mthis.initFont(size);
		main.Setup.FONT = main.Main.FONT;
		main.Setup.FONT2 = main.Main.FONT2;
		Game.FONTS[0] = main.Main.FONT;
		Game.FONTS[1] = main.Main.FONT2;
		
		Font font = new Font("Comic Sans MS", Font.BOLD, size / 2);
	    UnicodeFont FONT = new UnicodeFont(font);
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
	    Game.FONTS[2] = FONT;
	}
	
	public static void renderBox(int x, int y, float a, float b, int c, int d){
		int mac = Math.max(Math.max(Math.max(Game.FONTS[2].getWidth("Money: " + a), Game.FONTS[2].getWidth("Reserves: " + d)), Game.FONTS[2].getWidth("Income: " + b)), Game.FONTS[2].getWidth("Army :" + c));
		if(x + mac > Game.WIDTH + Game.mthis.translate_x){
			x = x - mac;
		}
		Draw.renderthiso(new Rectangle(x,y,mac, Game.FONTS[2].getHeight("I") * 4), 0.5f, 0.5f, 1f, 0.5f);
		Game.FONTS[2].drawString(x, y, "Money: " + a);
		Game.FONTS[2].drawString(x, y + Game.FONTS[2].getHeight("I"), "Income: " + b);
		Game.FONTS[2].drawString(x, y + Game.FONTS[2].getHeight("I") * 2, "Army: " + c);
		Game.FONTS[2].drawString(x, y + Game.FONTS[2].getHeight("I") * 3, "Reserves: " + d);
	}
	
	public static void renderBuilds(){
		int mac = Math.max(Math.max(Math.max(Game.FONTS[2].getWidth("Unit"), Game.FONTS[2].getWidth("Mine")), Game.FONTS[2].getWidth("Opium Den")), Game.FONTS[2].getWidth(""));
		int y = (int) (Game.mthis.translate_y + Game.FONTS[2].getHeight("Build") * 1.5);
		int x = Game.WIDTH - Game.FONTS[2].getWidth("Menu    ") + Game.mthis.translate_x - Game.FONTS[2].getWidth("Build");
		Draw.renderthiso(new Rectangle(x,y,mac, Game.FONTS[2].getHeight("I") * 3), 0.5f, 0.5f, 1f, 0.5f);
		if(Game.mthis.mousey - Game.mthis.translate_y >= y && Game.mthis.mousey - Game.mthis.translate_y <= y + Game.FONTS[2].getHeight("Unit")){
			Game.FONTS[3].drawString(x, y, "Unit");
			if(org.lwjgl.input.Mouse.isButtonDown(0)){
				Game.state = 1;
				Game.statei = 0;
			}
		} else {
			Game.FONTS[2].drawString(x, y, "Unit");
		}
		if(Game.mthis.mousey - Game.mthis.translate_y >= y +  + Game.FONTS[2].getHeight("I") && Game.mthis.mousey - Game.mthis.translate_y <= y + Game.FONTS[2].getHeight("Unit") +  + Game.FONTS[2].getHeight("I")){
			Game.FONTS[3].drawString(x, y + Game.FONTS[2].getHeight("I"), "Mine");
			if(org.lwjgl.input.Mouse.isButtonDown(0)){
				Game.state = 1;
				Game.statei = 1;
			}
		} else {
			Game.FONTS[2].drawString(x, y + Game.FONTS[2].getHeight("I"), "Mine");
		}
		if(Game.mthis.mousey - Game.mthis.translate_y >= y +  + Game.FONTS[2].getHeight("I") * 2 && Game.mthis.mousey - Game.mthis.translate_y <= y + Game.FONTS[2].getHeight("Unit") +  + Game.FONTS[2].getHeight("I") * 2){
			Game.FONTS[3].drawString(x, y + Game.FONTS[2].getHeight("I") * 2, "Opium Den");
			if(org.lwjgl.input.Mouse.isButtonDown(0)){
				Game.state = 1;
				Game.statei = 2;
			}
		} else {
			Game.FONTS[2].drawString(x, y + Game.FONTS[2].getHeight("I") * 2, "Opium Den");
		}
	}
}
