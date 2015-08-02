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
import java.util.HashMap;
import java.util.Scanner;

public class IOHandle{

    public static final String COUNTRY_SETTINGS = "res/countries.settings";
    public static final String MULTI_SETTINGS = "res/multiplayer.settings";

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

    public static HashMap<String, String> getMultiSettings(){
    	HashMap<String, String> answers = new HashMap<String, String>();
    	String settings = null;
    	try {
			settings = slurp(new FileInputStream(MULTI_SETTINGS));
		} catch (FileNotFoundException e) {
			System.out.println("Failure!");
		}
    	if(settings.equals("")){
    		return answers;
    	}
    	String[] keys = new String[]{"port", "update_interval", "warmup_interval", "warmup_start", "min_players"};
    	String[] pairs = settings.split(";");
    	for(int i = 0; i != pairs.length; i++){
    		String[] pair = pairs[i].toLowerCase().trim().split(":");
    		boolean trip = false;
    		for(String key: keys){
    			if(pair[0].equals(key)){trip=true;}
    		}
    		if(!trip){System.out.println("Unrecognized key - "+pair[0]+":"+pair[1]+"!");continue;}
    		answers.put(pair[0], pair[1]);
    	}
    	return answers;
    }

    public static Country[] getMCSettings(Game game){
    	String settings = null;
    	try {
			settings = slurp(new FileInputStream(COUNTRY_SETTINGS));
		} catch (FileNotFoundException e) {
			System.out.println("Failure!");
		}
    	return getMCSettings(settings, game);
    }

    public static Country[] getMCSettings(String settings, Game game){
    	Country[] countries = new Country[6];
    	for(int i = 0; i != countries.length; i++){
    		countries[i] = new Country(i, game);
    	}
    	if(settings.equals("")){
    		return countries;
    	}
    	String[] keys = new String[]{"north america", "south america", "europe", "africa", "asia", "oceania"};
		String[] keys2 = new String[]{"money", "income", "popularity", "reserves", "strength", "soldiers"};
    	String[] pairs = settings.split(";");
    	for(int i = 0; i != pairs.length; i++){
    		String[] pair = pairs[i].toLowerCase().trim().split(":", 2);
    		int number = -1;
    		for(int i2 = 0; i2 != keys.length; i2++){
    			String key = keys[i2];
    			if(key.equals(pair[0])){
    				number = i2;
    				break;
    			}
    		}
    		if(number == -1){
    			System.out.println("Uh oh MCSettings error : "+pair[0]);
    			return null;
    		}
    		Country country = countries[number];
    		String[] pairs2 = pair[1].split(",");
    		for(int a = 0; a != pairs2.length; a++){
    			String[] pair2 = pairs2[a].split(":");
    			if(pair2.equals(keys2[0])){
    				country.money = Integer.parseInt(pair2[1]);
    				continue;
    			}
    			if(pair2.equals(keys2[1])){
    				country.income = Integer.parseInt(pair2[1]);
    				country.bincome = country.income;
    				continue;
    			}
    			if(pair2.equals(keys2[2])){
    				country.popularity = Integer.parseInt(pair2[1]);
    				country.bpop = country.popularity;
    				continue;
    			}
    			if(pair2.equals(keys2[3])){
    				country.reserves = Integer.parseInt(pairs[1]);
    				continue;
    			}
    			if(pair2.equals(keys2[4])){
    				country.armyStrength = Integer.parseInt(pairs[1]);
    				continue;
    			}
    			if(pair2.equals(keys2[5])){
    				int size = Integer.parseInt(pairs[1]);
    				for(int count = 0; count != size; count++){
    					country.armyAdd(country.home[0], country.home[1]);
    				}
    				continue;
    			}
/*    			if(pair2.equals(keys2[])){

    			}*/
    		}
    	}
    	return countries;
    }
}
