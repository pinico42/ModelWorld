package multiplayer;

public class Button {
	
	public int id;
	
	public String text;
	public int x,y,width,height, type = 0, ox, oy;
	public boolean hover = false;
	
	public Button(int x, int y, String text){
		this.x = x - Game.FONTS[0].getWidth(text) / 2;
		this.y = y - Game.FONTS[0].getHeight(text) / 2;
		ox = this.x;
		oy = this.y;
		width = Game.FONTS[0].getWidth(text);
		height = Game.FONTS[0].getHeight(text);
		this.text = text;
	}
	
	public Button(int x, int y, int type, String text){
		this.x = x - Game.FONTS[type].getWidth(text) / 2;
		this.y = y - Game.FONTS[type].getHeight(text) / 2;
		ox = this.x;
		oy = this.y;
		width = Game.FONTS[0].getWidth(text);
		height = Game.FONTS[0].getHeight(text);
		this.type = type;
		this.text = text;
	}
	
	public Button(int x, int y, String text, boolean bool){
		this.x = x;
		this.y = y;
		ox = this.x;
		oy = this.y;
		width = Game.FONTS[0].getWidth(text);
		height = Game.FONTS[0].getHeight(text);
		this.text = text;
	}
	
	public void update(){
		x = ox + Game.mthis.translate_x;
		y = oy + Game.mthis.translate_y;
		if(Game.mthis.mousex > x && Game.mthis.mousex < x + width && Game.mthis.mousey > y && Game.mthis.mousey < y + height){
			hover = true;
		} else {
			hover = false;
		}
	}
	
	public void render(){
		int one;
		if(type == 0){
			one = 0;
		} else {
			one = 2;
		}
		if(hover){
			one++;
		}
		if(id == 1){
			Game.FONTS[one].drawString(x, y, (Game.autos[0]==1?"On":"Off"));
			return;
		}
		if(id == 2){
			Game.FONTS[one].drawString(x, y, "" + Game.autos[1]);
			return;
		}
		if(id == 3){
			Game.FONTS[one].drawString(x, y, "" + (Game.autos[2]==11?"All":Game.autos[2]));
			return;
		}
		Game.FONTS[one].drawString(x, y, text);
	}

}
