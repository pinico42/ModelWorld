package multiplayer;

import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import main.Draw;
import main.MultiplayerSetup;

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
	
	public static Button[] buttons;
	public static String[] names, names2;
	public static int[][] points, points2;
	public static int dipBuf = 40, selected = -1;
	
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
		Game.FONTS[0] = MultiplayerSetup.FONT;
		Game.FONTS[1] = MultiplayerSetup.FONT2;
		
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
		
		Game.RWIDTH = MultiplayerSetup.RWIDTH;
		Game.RHEIGHT = MultiplayerSetup.RHEIGHT;
		
		Game.theight = Game.HEIGHT / 10;
		Game.twidth = Game.theight;
		Game.mthis.images = new Texture[6];
		
		Game.mthis.black = loadTex("black");
		Game.mthis.purple = loadTex("purple");
		Game.mthis.images[0] = loadTex("worldmap");
		Game.mthis.images[1] = loadTex("man");
		Game.mthis.images[2] = loadTex("mine");
		Game.mthis.images[3] = loadTex("opium");
		Game.mthis.images[4] = loadTex("house");
		Game.mthis.images[5] = loadTex("house2");

		Game.mthis.translate_y = -100;
		
		Game.mthis.texts = new String[]{"Menu", "Build", "", "Place reserves", "Diplomacy", "Automation"};
		Game.mthis.text = new int[][]{{Game.WIDTH - Game.FONTS[2].getWidth(Game.mthis.texts[0]),0} , {Game.WIDTH - Game.FONTS[2].getWidth(Game.mthis.texts[0] + "    ") - Game.FONTS[2].getWidth(Game.mthis.texts[1]), 0}, {Game.WIDTH / 2, Game.HEIGHT / 2}, {}, {},{}};//[Game.mthis.texts.length][];
		Game.mthis.text[3] = new int[]{Game.mthis.text[1][0] - Game.FONTS[2].getWidth(Game.mthis.texts[3] + "    "), 0};
		Game.mthis.text[4] = new int[]{Game.mthis.text[3][0], Game.mthis.text[3][1] + Game.FONTS[3].getHeight("I")};
		Game.mthis.text[5] = new int[]{Game.mthis.text[4][0] + Game.FONTS[2].getWidth(Game.mthis.texts[4] + "    "), Game.mthis.text[4][1]};
		Game.mthis.colours = new boolean[Game.mthis.texts.length];
		
        //for(int i = 0; i != Game.mthis.text.length; i++){
        	//Game.mthis.text[i] = new int[]{(int) (Game.WIDTH / 2.3 - MultiplayerSetup.FONT.getWidth(Game.mthis.texts[i]) / 2), Game.HEIGHT / (Game.mthis.text.length + 1) * (i + 1) - MultiplayerSetup.FONT.getHeight(Game.mthis.texts[i])};
        //}
        
        for(int i = 0; i != 6; i++){
        	Game.mthis.countries[i] = new Country(i);
        }
        
        System.out.println("Created countries");

		names = new String[5];
		Game.mthis.dcolours = new boolean[names.length];
		
		for(int i = 0; i != 6; i++){
			if(i < Game.player){
				names[i] = Country.names[i];
			} else if(i > Game.player){
				names[i - 1] = Country.names[i];
			}
		}
		
		points = new int[5][];
		for(int i = 0; i != 5; i++){
			points[i] = new int[]{(int) (Game.WIDTH / 2 - MultiplayerSetup.FONT.getWidth(names[i]) / 2), (Game.HEIGHT - dipBuf - 100) / (names.length + 1) * (i + 1) - MultiplayerSetup.FONT.getHeight(names[i]) + dipBuf + 100};
		}
		
		buttons = new Button[4];
		buttons[0] = new Button(Game.WIDTH / 2, points[0][1] * 2, "Toggle war");
		buttons[0].id = 0;
		
		names2 = new String[]{"Place reserves: ", "No. builds place: ", "No. units ordered: "};
		points2 = new int[][]{{dipBuf + 20, dipBuf + 120 + Game.HEIGHT / 8}, {}, {}};
		
		buttons[1] = new Button(points2[0][0] + Game.FONTS[0].getWidth(names2[0] + " "), points2[0][1], "YUP", false);
		buttons[1].id = 1;
		
		buttons[2] = new Button(buttons[1].x + Game.FONTS[0].getWidth("   "), Game.HEIGHT / 2 - Game.FONTS[1].getHeight("YUP") / 2, "YUP", false);
		buttons[2].id = 2;
		points2[1] = new int[]{points2[0][0], buttons[2].y};
		
		buttons[3] = new Button(buttons[1].x + Game.FONTS[0].getWidth("   "), Game.HEIGHT - dipBuf - Game.FONTS[1].getHeight("YUP") - 20 - Game.FONTS[0].getWidth(" ") - Game.HEIGHT / 8, "YUP", false);
		buttons[3].id = 3;
		points2[2] = new int[]{points2[0][0], buttons[3].y};
		
		Game.autos = new int[]{0, 1, 1};
		
		Country.wars = new boolean[6][6];
		
	}
	
	@SuppressWarnings("unchecked")
	public static void setFonts(int size){
		main.Main.mthis.initFont(size);
		main.MultiplayerSetup.FONT = main.Main.FONT;
		main.MultiplayerSetup.FONT2 = main.Main.FONT2;
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
		Draw.renderthiso(new Rectangle(x,y,mac, Game.FONTS[2].getHeight("I") * 3), 0.5f, 0.5f, 1f, 1f);
		if(Game.mthis.mousey >= y && Game.mthis.mousey <= y + Game.FONTS[2].getHeight("Unit")){
			Game.FONTS[3].drawString(x, y, "Unit");
			if(org.lwjgl.input.Mouse.isButtonDown(0)){
				Game.state = 1;
				Game.statei = 0;
			}
		} else {
			Game.FONTS[2].drawString(x, y, "Unit");
		}
		if(Game.mthis.mousey >= y + Game.FONTS[2].getHeight("I") && Game.mthis.mousey <= y + Game.FONTS[2].getHeight("Unit") + Game.FONTS[2].getHeight("I")){
			Game.FONTS[3].drawString(x, y + Game.FONTS[2].getHeight("I"), "Mine");
			if(org.lwjgl.input.Mouse.isButtonDown(0)){
				Game.state = 1;
				Game.statei = 1;
			}
		} else {
			Game.FONTS[2].drawString(x, y + Game.FONTS[2].getHeight("I"), "Mine");
		}
		if(Game.mthis.mousey >= y + Game.FONTS[2].getHeight("I") * 2 && Game.mthis.mousey <= y + Game.FONTS[2].getHeight("Unit") + Game.FONTS[2].getHeight("I") * 2){
			Game.FONTS[3].drawString(x, y + Game.FONTS[2].getHeight("I") * 2, "Opium Den");
			if(org.lwjgl.input.Mouse.isButtonDown(0)){
				Game.state = 1;
				Game.statei = 2;
			}
		} else {
			Game.FONTS[2].drawString(x, y + Game.FONTS[2].getHeight("I") * 2, "Opium Den");
		}
	}
	
	public static void renderDiplomacy(){
		Draw.renderthiso(new Rectangle(Game.mthis.translate_x + dipBuf, Game.mthis.translate_y + 100 + dipBuf, Game.WIDTH - (dipBuf * 2), Game.HEIGHT - (100 + dipBuf * 2)), 4.5f, 6.5f, 9f, 0.7f);
		if(selected == -1){
			for(int i = 0; i != names.length; i++){
				if(Game.mthis.dcolours[i]){
					Game.FONTS[1].drawString(points[i][0] + Game.mthis.translate_x, points[i][1] + Game.mthis.translate_y, names[i]);
				} else {
					Game.FONTS[0].drawString(points[i][0] + Game.mthis.translate_x, points[i][1] + Game.mthis.translate_y, names[i]);
				}
			}
		} else {
			Game.FONTS[0].drawString(points[selected][0] + Game.mthis.translate_x, points[0][1] + Game.mthis.translate_y, names[selected]);
			//for(int i = 0; i != names2.length; i++){
			//	Game.FONTS[0].drawString(points2[i][0] + Game.mthis.translate_x, points2[i][1] + Game.mthis.translate_y, names2[i]);
			//}
			int selected2 = selected<Game.player?selected:selected+1;
			String string = (Country.wars[Game.player][selected2]?"At war currently":"Not at war currently");
			Game.FONTS[0].drawString(Game.WIDTH / 2 - Game.FONTS[0].getWidth(string) / 2 + Game.mthis.translate_x, Game.mthis.translate_y + points[0][1] * 3, string);
		}
	}
	
	public static void renderAuto(){
		Draw.renderthiso(new Rectangle(Game.mthis.translate_x + dipBuf, Game.mthis.translate_y + 100 + dipBuf, Game.WIDTH - (dipBuf * 2), Game.HEIGHT - (100 + dipBuf * 2)), 4.5f, 6.5f, 9f, 1f);
		for(int i = 0; i != names2.length; i++){
			Game.FONTS[0].drawString(points2[i][0] + Game.mthis.translate_x, points2[i][1] + Game.mthis.translate_y, names2[i]);
		}
	}
	
	public static void logicDiplomacy(){
		
	}
}
