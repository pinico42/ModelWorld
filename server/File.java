import java.io.PrintWriter;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class File{
	public static String path = "./plugins/";
	public static void writeFile(OutputStream os, String fpath){
		
		BufferedInputStream bis = null;
		
		try{
		try{
			java.io.File file = new java.io.File(path + fpath);
			byte[] bytes = new byte[(int)file.length()];
			bis = new BufferedInputStream(new FileInputStream(file));
			bis.read(bytes,0,bytes.length);
			System.out.println("Sending " + path + fpath + "(" + bytes.length + " bytes)");
			os.write(bytes,0,bytes.length);
			os.flush();
			System.out.println("Done.");
		} finally {
			if (bis != null) bis.close();
			//if (os != null) os.close();
		}
		} catch (IOException e){e.printStackTrace();}
	}
}
