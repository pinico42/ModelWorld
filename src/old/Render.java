package old;

import org.lwjgl.util.Rectangle;
import org.newdawn.slick.opengl.Texture;

import main.Draw;
import main.Setup;

public class Render{
	
	static Texture[] images;
	static int WIDTH, HEIGHT;
	static int[][] text;
	static String[] texts;
	
	public static void run(){
		text = Game.mthis.text;
		texts = Game.mthis.texts;
		for(int i = 0; i != text.length; i++){
			Setup.FONT.drawString(text[i][0], text[i][1], texts[i]);
		}
		
		Draw.renderthistex(new Rectangle(0,0, WIDTH / 2, HEIGHT / 2), images[0]);
	}
	
}
