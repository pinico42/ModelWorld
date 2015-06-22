package main;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;


public class IOHandle {
	
	public static Scanner getText(String path){
		Scanner file;
		try {
			file = new Scanner(new BufferedReader(new FileReader(path)));
			return file;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
        return null;
	}

    public static String slurp(final InputStream is){
        final char[] buffer = new char[3019];
        final StringBuilder out = new StringBuilder();
        try {
            final Reader in = new InputStreamReader(is, "UTF-8");
            try {
                for (;;) {
                    int rsz = in.read(buffer, 0, buffer.length);
                    if (rsz < 0)
                        break;
                    out.append(buffer, 0, rsz);
                }
            }
            finally {
                in.close();
            }
        }
        catch (UnsupportedEncodingException ex) {
            System.out.println(ex.getMessage() + " " + ex.getMessage());
        }
        catch (IOException ex) {
        	System.out.println(ex.getMessage() + " " +ex.getMessage());
        }
        return out.toString();
    }
    
    public static int[] getSettings(){
    	String settings = null;
    	try {
			settings = slurp(new FileInputStream("res/.settings"));
		} catch (FileNotFoundException e) {
			System.out.println("Failure!");
		}
    	Scanner scan = new Scanner(settings);
    	int sn = 4;
    	int[] answers = new int[sn];
    	
    	for(int i = 0; i != sn; i++){
    		try{
    			answers[i] = scan.nextInt();
    		} catch(java.util.NoSuchElementException e){
    			answers[i] = 0;
    		}
    	}
    	
    	scan.close();
    	return answers;
    	
    }
    
    public static void writeSettings(int[] settings){
    	PrintWriter writer = null;
		try {
			writer = new PrintWriter("res/.settings", "UTF-8");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		} catch (UnsupportedEncodingException e){
			e.printStackTrace();
			return;
		}
		for(int i: settings){
			writer.print(i);
			writer.print(" ");
		}
		writer.close();
    }
    
    /*
    public static ArrayList<String[]> getInfo(InputStream input){
        String slevel = slurp(input);
        Scanner scanner = new Scanner(slevel);

        String[] answer = slevel.split("\n");
        @SuppressWarnings("unused")
		List<String[]> meanIt = new ArrayList<String[]>();
        for(@SuppressWarnings("unused") String string: answer){

        }
        scanner.close();
        return null;

    }
	
	@SuppressWarnings("unused")
	public static Tile[][] getLevel(InputStream input){

        String slevel = slurp(input);
        Scanner scanner = new Scanner(slevel);

        int x = 0;
		int y = 0;
		String next;
        Tile[][] level;
        level = new Tile[150][10];
		while(scanner.hasNext()){
            next = scanner.next();
			if(next.equals("l")){
                //Log.d(Integer.toString(y),Integer.toString(y));
				y++;
				x = 0;
			} else {
				level[x][y] = ( new Tile(x, y, Integer.parseInt(next), Tiggles.twidth, Tiggles.theight));
				x++;
			}
		}

        loop: for(Tile[] tiles: level){

            lopp: for(Tile tile: tiles){

                if(tile.type == 1 || tile.type == 3){

                    tile.state = 1;
                    continue loop;

                }

            }

        }
		
		scanner.close();
		return level;
	}
	
	public static void writeLevel(Tile[][] world){
		
		PrintWriter writer = null;
		try {
			writer = new PrintWriter("res/level", "UTF-8");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		} catch (UnsupportedEncodingException e){
			e.printStackTrace();
			return;
		}
		for(int y = 0; y != 10; y++){
			for(int x = 0; x != 150; x++){
				
				writer.print(world[x][y].type + " ");
				
			}
			writer.write("l\n");
		}
		writer.close();
		
		
	}
	
	public static int[] getLeveli(String path){
		
		int[] leveli = new int[3];
		
		Scanner file = getText(path);
		int next;
		
		for(int i = 0; i != 3; i++){
			next = file.nextInt();
			leveli[i] = next;
		}
		
		return leveli;
		
	}
	
	public static String[] getLevels(){
		
		File folder = new File("res/levels");
		File[] listOfFiles = folder.listFiles();
		String[] bob = new String[listOfFiles.length];
		int i = 0;

		for (File file : listOfFiles) {
		    bob[i] = file.getAbsolutePath();
		    i++;
		}
		
		return bob;
		
	}
	
	public static int getScore(){
		
		Scanner info = getText("res/info");
		if(info == null){
			return -1;
		} else {
			int score;
			try{
				String string = info.next();
				String sting = "";
				for(int i = 0; i != string.length(); i++){
					sting = sting + string.charAt(i);
				}
				score = Integer.parseInt(sting);
			} catch(java.util.NoSuchElementException e){
				PrintWriter outputStream;
				try {
					outputStream = new PrintWriter(new FileWriter(new File("res/info").getAbsolutePath(), true));
					outputStream.print(0);
					outputStream.close();
					System.out.println(99999);
				} catch (IOException e1) {
					return -1;
				}
				score = 0;
			}
			return score;
		}
		
	}
	
	public static int[] getScores(){
		
		int[] scores = new int[3];
		Scanner info = getText("res/info");
		if(info == null){
			return scores;
		} else {
			try{
				int i = 0;
				while(info.hasNextInt()){
					scores[i] = info.nextInt();
					i++;
				}
				return scores;
			} catch(java.util.NoSuchElementException e){
				PrintWriter outputStream;
				try {
					outputStream = new PrintWriter(new FileWriter(new File("res/info").getAbsolutePath(), true));
					outputStream.print(0);
					outputStream.close();
					System.out.println(99999);
				} catch (IOException e1) {
					return scores;
				}
			}
			return scores;
		}
		
	}
	
	public static void writeScore(int score){
		
		PrintWriter writer = null;
		try {
			writer = new PrintWriter("res/info", "UTF-8");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e){
			e.printStackTrace();
		}
		writer.println(score);
		writer.close();
		
	}
	
	public static void writeScores(int[] scores){
		
		PrintWriter writer = null;
		try {
			writer = new PrintWriter("res/info", "UTF-8");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e){
			e.printStackTrace();
		}
		for(int i: scores){
			writer.println(i);
		}
		writer.close();
		
	}*/
	
	public static void main(String[] args){
		
		/*for(Tilee tile : getLevel(64,48, "res/Text").values()){
			System.out.println(tile.x);
		}
		System.out.println(getLevel(64,48, "res/Text").size());
		System.out.println(18 * 48);*/
		int[] bob = new int[3];
		bob[0] = 1;
		bob[1] = 0;
		bob[2] = 2;
		//writeScores(bob);
		//for(int i: getScores()){
		//	System.out.println(i);
		//}
		
	}
	
}
