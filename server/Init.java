
//import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

//import main.Draw;
//import main.MultiplayerSetup;
/*
import org.lwjgl.opengl.Display;
import org.lwjgl.util.Rectangle;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;*/

public class Init {

	//public static Button[] buttons;
	//public static String[] names, names2;
	//public static int[][] points, points2;
	public static int dipBuf = 40, selected = -1;

/*	public static Texture loadTex(String name){
		try {
			return TextureLoader.getTexture("PNG", new FileInputStream(new File("res/"+name+".png")));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}*/


	@SuppressWarnings("unchecked")
	public static void init_game(){

        for(int i = 0; i != 6; i++){
        	Game.mthis.countries[i] = new Country(i);
        }

		/*for(int i = 0; i != 6; i++){
			if(i < Game.player){
				names[i] = Country.names[i];
			} else if(i > Game.player){
				names[i - 1] = Country.names[i];
			}
		}*/

		//Game.autos = new int[]{0, 1, 1};

		Country.wars = new boolean[6][6];

	}

	/*@SuppressWarnings("unchecked")
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
	}*/

	/*public static void renderBox(int x, int y, float a, float b, int c, int d){
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
	}*/

	public static void logicDiplomacy(){

	}
}
