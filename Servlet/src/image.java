import java.awt.Image;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import kl.se.ContentExtraction;


public class image {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		//Image image = null;
		//try{
			//System.out.println("entering read url");
		ContentExtraction ce=new ContentExtraction();
		String targetString = null;
		URL project = new URL("http://blog.codehangover.com/read-html-with-java-then-7-fun-things-to-do-to-it/");
		//System.out.println(url);
		BufferedReader PageSource = new BufferedReader(new InputStreamReader(
				project.openStream()));
        String inputLine;
        StringBuilder a = new StringBuilder();
        while ((inputLine = PageSource.readLine()) != null)
            a.append(inputLine);
      // PageSource.close();
       targetString= a.toString();
		//String regexImage = "";
//		Pattern pImage = Pattern.compile(/(<img[^>]+src="?http:\/\/[^">]+"?[^>]+\/>)/g);
//		Matcher mImage = pImage.matcher(targetString);
      Pattern IMG_PATTERN = Pattern.compile(
               "<img(\\s+.*?)(?:src\\s*=\\s*(?:'|\")(.*?)(?:'|\"))(.*?)/>", 
               Pattern.DOTALL|Pattern.CASE_INSENSITIVE);
      Matcher mImage = IMG_PATTERN.matcher(targetString);
		while (mImage.find()) {
		   String imagePath = mImage.group();
		   System.out.println(imagePath);}
	//}
//		catch(Exception e){
//			e.getMessage();	
//			System.out.println("not working");
//			}

}
}
