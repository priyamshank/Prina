package kl.se;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;


public class DBPass {
	
	public void gettingurl(LinkedHashMap<Integer, String> que_index) throws SQLException {
		//Get url from crawler
	//	System.out.println("Insiede gettingurl");
		ReadUrlIndexer re = new ReadUrlIndexer();
		JBDCConnection j = new JBDCConnection();
		try {
			//connect to Database to create the features table
			j.connect();
		} catch (ClassNotFoundException e) {
			
		}
		

		try{
		Iterator<Entry<Integer,String>> iterator=que_index.entrySet().iterator();
	        while (iterator.hasNext()) 
	        {
	        	
	        Map.Entry<Integer,String> Entry = (Map.Entry<Integer,String>) iterator.next();
	        Integer keyValue = Entry.getKey();
	        String value =  Entry.getValue();
	    //    System.out.println("Key "+keyValue+"Value"+value);
	        
	        re.getUrlSource( keyValue , value);			//call indexer program
	     
	        }
		}
		catch(Exception e){
			
		}

//		TFIDF t = new TFIDF();
//		t.tfidf();

	}
	//insert values into features table
	public void database(int docid, Map<String, Integer> map)
			throws SQLException, ClassNotFoundException {
		//System.out.println("entering jdbc");
		PreparedStatement pstmt = null;
		Connection c = null;

		try {

			String query = "insert into fea1(docid,term,term_frequency) values(?,?, ?)";

			c = MySingleTon.getInstance();
			c.setAutoCommit(false);
			pstmt = c.prepareStatement(query);
			for (Map.Entry<String, Integer> entry : map.entrySet()) {
				String Key = entry.getKey();
				int Value = entry.getValue();
				pstmt.setInt(1, docid);
				pstmt.setString(2, Key);
				pstmt.setInt(3, Value);
				pstmt.executeUpdate();
				
				
				
			}
			
		c.commit();
		} catch (SQLException e) {

		

		} finally {
			if (pstmt != null) {
				pstmt.close();
			}
		}
	
	}

	
}
