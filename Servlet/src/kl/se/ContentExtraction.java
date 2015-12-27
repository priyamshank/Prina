package kl.se;
import java.util.regex.Pattern;
import java.util.regex.Matcher;



public class ContentExtraction {
	//gets only the actual content from the pagesource
	public void content(int docid,String content) throws Exception {

	    // Regex to replace anything between script or style tags
	    
	    String scriptregex = "<(script|style)[^>]*>[^<]*</(script|style)>";
	    Pattern p1 = Pattern.compile(scriptregex,Pattern.CASE_INSENSITIVE);
	    Matcher m1 = p1.matcher(content);
	    content = m1.replaceAll("");

	    // A Regex to match anything in between <>
	    
	    String tagregex = "<[^>]*>";
	    Pattern p2 = Pattern.compile(tagregex);
	    Matcher m2 = p2.matcher(content);
	    content = m2.replaceAll("");
	    StopWordRemoval se=new StopWordRemoval();
	    se.removeStopWords(docid,content);
	  
		
	    
	  }
}
