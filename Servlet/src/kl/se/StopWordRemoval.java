package kl.se;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;




public class StopWordRemoval {
          
	public static ArrayList<String> ar = new ArrayList<String>();	
	static String[] words=null;		
	String s1=null;
    Integer frequency;
      public void removeStopWords(int docid,String removedtags) {
    	
       String[] stopwords = {" { "," ","a", "about", "above", "above", "across", "after", "afterwards", "again", "against", "all", "almost",
           "alone", "along", "already", "also", "although", "always", "am", "among", "amongst", "amoungst", "amount", "an", "and",
           "another", "any", "anyhow", "anyone", "anything", "anyway", "anywhere", "are", "around", "as", "at", "back", "be", "became",
           "because", "become", "becomes", "becoming", "been", "before", "beforehand", "behind", "being", "below", "beside", "besides",
           "between", "beyond", "bill", "both", "bottom", "but", "by", "call", "can", "cannot", "cant", "co", "con", "could", "couldnt",
           "cry", "de", "describe", "detail", "do", "done", "down", "due", "during", "each", "eg", "eight", "either", "eleven", "else",
           "elsewhere", "empty", "enough", "etc", "even", "ever", "every", "everyone", "everything", "everywhere", "except", "few",
           "fifteen", "fify", "fill", "find", "fire", "first", "five", "for", "former", "formerly", "forty", "found", "four", "from",
           "front", "full", "further", "get", "give", "go", "had", "has", "hasnt",
           "have", "he", "hence", "her", "here", "hereafter", "hereby", "herein", "hereupon", "hers", "herself",
           "him", "himself", "his", "how", "however", "hundred", "ie", "if", "in", "inc", "indeed", "interest", "into",
           "is", "it", "its", "itself", "keep", "last", "latter", "latterly", "least", "less", "ltd", "made", "many",
           "may", "me", "meanwhile", "might", "mill", "mine", "more", "moreover", "most", "mostly", "move", "much", "must",
           "my", "myself", "name", "namely", "neither", "never", "nevertheless", "next", "nine", "no", "nobody", "none",
           "noone", "nor", "not", "nothing", "now", "nowhere", "of", "off", "often", "on", "once", "one", "only", "onto",
           "or", "other", "others", "otherwise", "our", "ours", "ourselves", "out", "over", "own", "part", "per", "perhaps",
           "please", "put", "rather", "re", "same", "see", "seem", "seemed", "seeming", "seems", "serious", "several", "she",
           "should", "show", "side", "since", "sincere", "six", "sixty", "so", "some", "somehow", "someone", "something",
           "sometime", "sometimes", "somewhere", "still", "such", "system", "take", "ten", "than", "that", "the", "their",
           "them", "themselves", "then", "thence", "there", "thereafter", "thereby", "therefore", "therein", "thereupon",
           "these", "they", "thickv", "thin", "third", "this", "those", "though", "three", "through", "throughout", "thru",
           "thus", "to", "together", "too", "top", "toward", "towards", "twelve", "twenty", "two", "un", "under", "until",
           "up", "upon", "us", "very", "via", "was", "we", "well", "were", "what", "whatever", "when", "whence", "whenever",
           "where", "whereafter", "whereas", "whereby", "wherein", "whereupon", "wherever", "whether", "which", "while",
           "whether", "who", "whoever", "whole", "whom", "whose", "why", "will", "with", "within", "without", "would", "yet",
           "you", "your", "yours", "yourself", "yourselves", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "1.", "2.", "3.", "4.", "5.", "6.", "11",
           "7.", "8.", "9.", "12", "13", "14","terms", "CONDITIONS", "conditions", "values", "interested.", "care", "sure", ".", "!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "{", "}", "[", "]", ":", ";", ",", "<", ".", ">", "/", "?", "_", "-", "+", "=",
           "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
           "contact", "grounds", "buyers", "tried", "said,", "plan", "value", "principle.", "forces", "sent", "is,", "was", "like",
           "discussion", "tmus", "diffrent.", "layout", "area", "thanks", "thankyou", "hello", "bye", "rise", "fell", "fall", "psqft.", "http://", "km", "miles"};
     String[] germanstopwords={"einer",
    	      "eine", "eines", "einem", "einen", "der", "die", "das",
    	      "dass", "daß", "du", "er", "sie", "es", "was", "wer",
    	      "wie", "wir", "und", "oder", "ohne", "mit", "am", "im",
    	      "in", "aus", "auf", "ist", "sein", "war", "wird", "ihr",
    	      "ihre", "ihres", "ihnen", "ihrer", "als", "für", "von",
    	      "mit", "dich", "dir", "mich", "mir", "mein", "sein",
    	      "kein", "durch", "wegen", "wird", "sich", "bei", "beim",
    	      "noch", "den", "dem", "zu", "zur", "zum", "auf", "ein",
    	      "auch", "werden", "an", "des", "sein", "sind", "vor",
    	      "nicht", "sehr", "um", "unsere", "ohne", "so", "da", "nur",
    	      "diese", "dieser", "diesem", "dieses", "nach", "über",
    	      "mehr", "hat", "bis", "uns", "unser", "unserer", "unserem",
    	      "unsers", "euch", "euers", "euer", "eurem", "ihr", "ihres",
    	      "ihrer", "ihrem", "alle", "vom", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "1.", "2.", "3.", "4.", "5.", "6.", "11",
              "7.", "8.", "9.", "12", "13", "14", ".", "!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "{", "}", "[", "]", ":", ";", ",", "<", ".", ">", "/", "?", "_", "-", "+", "=",};
       try
       { 
       
          	Scanner content = new Scanner(removedtags);
			Map<String, Integer> map = new TreeMap<String, Integer>();
			Integer a = new Integer(1);
			int counter_e=0;
			int counter_g=0;
			String lang=null;
			while(content.hasNext()){
				s1 = content.next();
				s1 = s1.toLowerCase();
				for(int i=0;i<stopwords.length;i++){
					if(s1.equalsIgnoreCase(stopwords[i])){
						//System.out.println("eng:"+s1);
						counter_e++;
						//System.out.println(counter_e);
					}
				}
				for(int i=0;i<germanstopwords.length;i++){
					if(s1.equalsIgnoreCase(germanstopwords[i])){
						//System.out.println("ger:"+s1);
						
						
						counter_g++;
						//System.out.println(counter_g);
					}
				}
			}
			
		  content = new Scanner(removedtags);
          while (content.hasNext()) {
           	int goodterm=1;
				s1 = content.next();
				s1 = s1.toLowerCase();
				for (int i = 0; i < stopwords.length; i++)
	            {
	                if (s1.equalsIgnoreCase(stopwords[i])) 
	                {
	                    goodterm=0;
	                    break;
	                    
	                }
	            }
				for (int i = 0; i < germanstopwords.length; i++)
	            {
	                if (s1.equalsIgnoreCase(germanstopwords[i])) 
	                {
	                    goodterm=0;
	                    break;
	                    
	                }
	            }
	            
	            
           if (goodterm != 0) 
          {
               if (s1.length() > 0 && counter_e>counter_g) 
               {
               lang="english";  
               ar.add(s1);
               //System.out.println("english");
               s1 = stemWords(s1);
                   frequency = (Integer) map.get(s1);
                   if (frequency == null) 
                	   
                   {
                       frequency = a;
                   } else 
                   {
                       int value = frequency.intValue();
                       frequency = new Integer(value + 1);
                   }
                   map.put(s1, frequency);
               }
               else{
            	   System.out.println("german");
            	   lang="german";
            	   s1 = s1.replaceAll("[^a-zA-Z]","");
            	   frequency = (Integer) map.get(s1);
                   if (frequency == null) 
                	   
                   {
                       frequency = a;
                   } else 
                   {
                       int value = frequency.intValue();
                       frequency = new Integer(value + 1);
                   }
                   map.put(s1, frequency);
               }
           }

           
           }
          
       
     //  DBPass d=new DBPass();
       System.out.println("call to database fea1");
       ReadUrl.db.database(docid,map);
       Datatransfer j=new Datatransfer();
//       for(int i=0;i<StopWordRemoval.ar.size();i++){
//    		System.out.println(StopWordRemoval.ar.get(i));
//    	}
       System.out.println("call to database documents");
       j.putlang(docid, lang);
       content.close();
			
       }
			 catch (Exception e) {			
			e.printStackTrace();
		}
  
   }
//stem every word present in the content
public String stemWords(String stringTerm) {
	// TODO Auto-generated method stub
	 char[] w = new char[501];
   	 Stemmer s = new Stemmer();
     InputStream in = new ByteArrayInputStream(stringTerm.getBytes());
	 BufferedReader br = new BufferedReader(new InputStreamReader(in));
	 String stemmed_term="";
	 try{
		while(true){
			/* Stemming the words given by user */
			int ch = br.read();					
			if (Character.isLetter((char) ch)){
				int j = 0;
				while(true)
				{  
                	ch = Character.toLowerCase((char) ch);
					w[j] = (char) ch;
					if (j < 500) j++;
					ch = br.read();
					if (!Character.isLetter((char) ch))
					{
					   /* to test add(char ch) */
					   for (int c = 0; c < j; c++) 
						   {
						   s.add(w[c]);
						   }

					   s.stem();
					   stemmed_term = s.toString();
					  // System.out.println(stemmed_term);
					   return stemmed_term;
                    }
                 }
            }	
			return stemmed_term;
		}
		}catch (IOException ioe){
		System.out.println("IO error taking ur choice!");
		}
	 
	return stemmed_term;
}

}
			 
       

    
   
