package kl.se;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import kl.se.Levenstein;
import kl.se.MySingleTon;
import kl.se.SE_Result;
import kl.se.StopWordRemoval;

public class Processing {
	public static int npsize;
	Connection conn=MySingleTon.getInstance();
	public List<String> NormProcess(String[] b,String lang) throws SQLException{
		StopWordRemoval r = new StopWordRemoval();
		List<String> present = new ArrayList<String>();
		String wf=null;
		Statement stmt = conn.createStatement();
		for (int bi = 0; bi < b.length; bi++) {
			
				
			wf = r.stemWords(b[bi]);
			
			System.out.println(wf);
			String wordfind = "select (a.term)as term from fea1 a,documents b where a.docid=b.doc_id and b.lang='english' and term=";
			wordfind += "'" + wf + "'";
			// out.println(wordfind);
			ResultSet wf1 = stmt.executeQuery(wordfind);
			if (wf1.next()) {
				present.add(wf);
				
				//System.out.println(Didumean);
			} 
		}
		return present;
	}
	public List<String> NormProcess(String[] b) throws SQLException{
		List<String> present = new ArrayList<String>();
		String wf=null;
		Statement stmt = conn.createStatement();
		for (int bi = 0; bi < b.length; bi++) {
			
				
			wf = (b[bi]);
			
			System.out.println(wf);
			String wordfind = "select (a.term)as term from fea1 a,documents b where a.docid=b.doc_id and b.lang='german' and term=";
			wordfind += "'" + wf + "'";
			// out.println(wordfind);
			ResultSet wf1 = stmt.executeQuery(wordfind);
			if (wf1.next()) {
				present.add(wf);
				
				//System.out.println(Didumean);
			} 
		}
		return present;
	}
		
	
	public List<String> Process(String[] b,String lang) throws SQLException,
			ClassNotFoundException {
		StopWordRemoval r = new StopWordRemoval();
		Levenstein l = new Levenstein();
		List<String> present = new ArrayList<String>();
		List<String> notpresent = new ArrayList<String>();
		List<String> words = new ArrayList<String>();
		String wf=null;
		Statement stmt = conn.createStatement();
		// Map <IntegerString> Didumean=new ArrayList<String>();
		List<String> Didumean = new ArrayList<String>(Collections.nCopies(0,
				" null"));
		String news = null;
		for (int bi = 0; bi < b.length; bi++) {
			if(lang.contains("english")){
				wf = r.stemWords(b[bi]);
			}
				
			else{
				wf = (b[bi]);
			}
				
			System.out.println(wf);
			String wordfind = "select (a.term)as term,docid from fea1 a,documents b where a.docid=b.doc_id and  b.lang=";
					wordfind+= "'" + lang + "'";
			wordfind += "'" + wf + "'";
			// out.println(wordfind);
			ResultSet wf1 = stmt.executeQuery(wordfind);
			if (wf1.next()) {
				present.add(wf);

				Didumean.add(bi, wf);
				
				//System.out.println(Didumean);
			} else {
				notpresent.add(wf);
				npsize++;

			}

			for (int i = 0; i < notpresent.size(); i++) {
				Map<String, Integer> dist = new HashMap<String, Integer>();
				String termquery = "select (a.term)as term,docid from fea1 a,documents b where a.docid=b.doc_id and  b.lang=";
				termquery+= "'" + lang + "'";
				ResultSet rss = stmt.executeQuery(termquery);
				while (rss.next()) {
					String term = rss.getString("term");
					dist.put(term, l.distance(term, notpresent.get(i)));

				}
				Entry<String, Integer> min1 = null;
				for (Entry<String, Integer> entry : dist.entrySet()) {
					if (min1 == null || min1.getValue() > entry.getValue()) {
						min1 = entry;
					}
				}

				int m = min1.getValue();

				for (Map.Entry<String, Integer> entry : dist.entrySet()) {
					if (entry.getValue() == m) {
						words.add(entry.getKey());

					}
				}

				String maxquery = "select distinct(term) from fea1 where tf_idf=(select max(tf_idf) from fea1 where term in (";
				maxquery += "'" + words.get(0) + "'";
				for (int i1 = 1; i1 < words.size(); ++i1) {
					// System.out.print(i);

					maxquery += ", '" + words.get(i1) + "'";
				}

				maxquery += ")) and term in (";
				maxquery += "'" + words.get(0) + "'";
				for (int i1 = 1; i1 < words.size(); ++i1) {
					// System.out.print(i);

					maxquery += ", '" + words.get(i1) + "'";
				}

				maxquery += ")";
				ResultSet mq = stmt.executeQuery(maxquery);
				while (mq.next()) {

					news = mq.getString("term");
					Didumean.add(bi, news);
					present.add(news);

					words.clear();

					notpresent.clear();
					System.out.println(Didumean);
				}
			}
		}
		
		return Didumean;
	}
	public List<SE_Result> getSearchResult(String query){
		System.out.println("got called");
		List<SE_Result> result = new ArrayList<>();
		try {
			Connection con=MySingleTon.getInstance();
			Statement stmt=con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			int rank=1;
			while (rs.next()) {
				float score=rs.getFloat("score");
				String url=rs.getString("url");
				SE_Result item = new SE_Result(score, url, rank);
				result.add(item);
				rank++;
			}
		}
		catch(Exception e){
			
		}
		
		return result;
	}
	public void dumpData(List<SE_Result> result, java.io.PrintWriter out)
			throws Exception {

		out.println("<P ALIGN='center'><TABLE BORDER=2>");
		
		
		out.println("<TR>");
		out.println("<TH>" + "Rank" + "</TH>");
		out.println("<TH>" + "url" + "</TH>");
		out.println("<TH>" + "score" + "</TH>");
		out.println("</TR>");
		
    		
		for(SE_Result item : result){
			out.println("<TR>");

			out.println("<TD>" + item.getRank() + "</TD>");
			out.println("<TD>");
			out.println("<a href=" + item.getUrl() +">"+item.getUrl() +"</a>");
			out.println("</TD>");
			out.println("<TD>" + item.getScore() + "</TD>");

			out.println("</TR>");
			
		}
		out.println("</TABLE></P>");
		
	}
	
}