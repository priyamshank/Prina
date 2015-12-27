package kl.se;

import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchQuery {

	public boolean QuerySearch(String s,PrintWriter out,String lang,int ressize){
		
		List<String> matchList = new ArrayList<String>();
		List<String> Listconj = new ArrayList<String>();
		List<String> Listdisj = new ArrayList<String>();
		List<String> matchListconj1 = new ArrayList<String>();
		List<String> matchListdisj1 = new ArrayList<String>();
		String conjarray[]=null;
		String query[] = s.split("\\s+");
		//String disjarray[]=new String[matchListdisj.size()];
		if (!s.contains("site:")){
		Pattern quoted_unquoted = Pattern.compile("[^\\s\"']+|\"([^\"]*)\"|'([^']*)'");
		Matcher m = quoted_unquoted.matcher(s);
		while (m.find()) {
		    if (m.group(1) != null) {
		        Listconj.add(m.group(1));
		    } else if (m.group(2) != null) {
		       
		    } else {
		        Listdisj.add(m.group());
		    }
		} }
		if(Listdisj.isEmpty() || Listconj.isEmpty()  ){
			return true;
		}
		else{
			
			
		
		//out.println("words in quotes");
		for(int i=0;i<Listconj.size();i++){
			
	    	//out.println(i + matchListconj.get(i));	
	    	conjarray=(Listconj.get(i).split("\\s+"));
	    	
	    	}
	
		String disjarray[] = new String[Listdisj.size()];              
		for(int j =0;j<Listdisj.size();j++){
		  disjarray[j] = Listdisj.get(j);}
		QueryProcessing qp=new QueryProcessing();
		Processing p=new Processing();
		if(lang.contentEquals("english")){
			try {
				matchListconj1=p.NormProcess(conjarray, lang);
				matchListdisj1=p.NormProcess(disjarray, lang);
				
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			}
		if(lang.contains("german")){
			
			try {
				matchListconj1=p.NormProcess(conjarray);
				matchListdisj1=p.NormProcess(disjarray);
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(matchListdisj1.isEmpty() || matchListconj1.isEmpty() )	{
			out.println("Your search query did not match any documents."
					+"<br><br>"

			+"Suggestions:"
				+"<br>"
			    +"Make sure that all words are spelled correctly. <br>"
			    +"Try different keywords.<br>"
			    +"Try more general keywords.<br>"
			    +"Try fewer keywords.<br>");
		}
		String query1=qp.conjdisjquery(matchListconj1,matchListdisj1, lang, ressize);
		//out.println(query1);
		List<SE_Result> result = p.getSearchResult(query1);
		try {
			if(result.isEmpty()){
				out.println("Your search query did not match any documents."
						+"<br><br>"

				+"Suggestions:"
					+"<br>"
				    +"Make sure that all words are spelled correctly. <br>"
				    +"Try different keywords.<br>"
				    +"Try more general keywords.<br>"
				    +"Try fewer keywords.<br>");
				    
			}else{
			p.dumpData(result,out);}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	
	}	
}
