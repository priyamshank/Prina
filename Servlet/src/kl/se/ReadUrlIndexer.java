package kl.se;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;




public class ReadUrlIndexer {
		//takes url and docid and get the pagesource out of it
		public void getUrlSource(int docid,String url){
			
			try{
				//System.out.println("entering read url");
			ContentExtraction ce=new ContentExtraction();
			String targetString = null;
			URL project = new URL(url);
			//System.out.println(url);
			BufferedReader PageSource = new BufferedReader(new InputStreamReader(
					project.openStream()));
            String inputLine;
            StringBuilder a = new StringBuilder();
            while ((inputLine = PageSource.readLine()) != null)
                a.append(inputLine);
           PageSource.close();
           targetString= a.toString();
           ce.content(docid,targetString);        
           
		      }
		catch(Exception e){
			e.getMessage();	
			}
			
		}
	}

