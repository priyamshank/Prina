
package kl.se;
import java.io.BufferedReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
//import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.io.IOException;
import java.io.InputStreamReader;

public class CLI {
	public static void main(String[] args) throws SQLException, IOException {
		Connection c = null;
		Statement stmt = null;
		ArrayList<String> Stemmed_array = new ArrayList<String>();

		try {
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection(
					"jdbc:postgresql://localhost:5432/postgres", "postgres",
					"postgres");
			System.out.println("Opened database successfully");
			stmt = c.createStatement();

		} catch (SQLException | ClassNotFoundException e) {

			System.out.println(e.getMessage());

		}
		CLI input = new CLI();
		input.grab_input(Stemmed_array,c);

	}

	public void grab_input(List<String> Stemmed_array,Connection c) throws SQLException, IOException {

		// Keywords, resultsize, boolean indicator of conjunctive or disjunctive
	
		List<String> inter = new ArrayList<String>();
		int resultsize = 0, dis_con = 0, n = 0;
		String pscore = null;
		List<SE_Result> result2=new ArrayList<SE_Result>();
		List<SE_Result> result2d=new ArrayList<SE_Result>();
		QueryProcessing q=new QueryProcessing();
		Connection d = c;

		InputStreamReader key_input = new InputStreamReader(System.in);

		BufferedReader number_terms = new BufferedReader(key_input);
		System.out.println("Enter the number of query terms");
		try {
			n = Integer.parseInt(number_terms.readLine());
		} catch (NumberFormatException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
	//	String[] keywords = new String[n];
		List<String> keywords = new ArrayList<String>();
		System.out.println("Enter the query terms");
		BufferedReader key = new BufferedReader(key_input);
		String[] s = key.readLine().split(" ");
		Processing p=new Processing();
		Stemmed_array=p.NormProcess(s,"english");
		for (int i = 0; i < n; i++) {

			keywords.add(s[i]) ;
		}

		
		BufferedReader result = new BufferedReader(key_input);
		System.out.println("Enter the result size");
		try {
			resultsize = Integer.parseInt(result.readLine());
		} catch (NumberFormatException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		int pref;
		System.out.println("Enter the preferred score");
		System.out.println("1.tf_idf 2.okapiscore 3.combinedscore");
		pref = Integer.parseInt(result.readLine());
		switch(pref)
		{
		case 1: pscore="tf_idf";
				break;
		case 2 :pscore="n_okapiscore";
				break;
		case 3: pscore="combinedscore";
				break;
		}
		
		
		BufferedReader dis = new BufferedReader(key_input);
		System.out.print("Please enter preference 1.Conjunctive 2.Disjunctive");
		try {
			dis_con = Integer.parseInt(dis.readLine());
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		CLI djquery = new CLI();

		switch (dis_con) {
		case 1:
			System.out.print("Conjunctive Mode");
			
			String cquery_stem=q.conjquerycli(Stemmed_array, pscore, resultsize);
			String cquery_unstem=q.conjquerycli(keywords, pscore, resultsize);
			System.out.println(cquery_stem);
			System.out.println(cquery_unstem);
			List<SE_Result> result1_stem = p.getSearchResult(cquery_stem);
			List<SE_Result> result1_unstem = p.getSearchResult(cquery_unstem);
			
			for(SE_Result item : result1_stem){
				result2.add(item);
			}
			for(SE_Result item : result1_unstem){
				result2.add(item);
			}

			System.out.println("got result");
			djquery.displayresult(result2,resultsize);
			//djquery.conjunctive(Stemmed_array, resultsize, d, n);
			break;

		case 2:
			System.out.print("Disjunctive Mode \n");
			//djquery.disjunctive(Stemmed_array, resultsize, d);
			
			String dquery_stem=q.disjquerycli(Stemmed_array, pscore, resultsize);
			String dquery_unstem=q.disjquerycli(keywords, pscore, resultsize);
			System.out.println(dquery_stem);
			System.out.println(dquery_unstem);
			List<SE_Result> resultd1_stem = p.getSearchResult(dquery_stem);
			List<SE_Result> resultd2_unstem = p.getSearchResult(dquery_unstem);
			
			for(SE_Result item : resultd1_stem){
				result2d.add(item);
			}
			for(SE_Result item : resultd2_unstem){
				result2d.add(item);
			}

			System.out.println("got result");
			djquery.displayresult(result2d,resultsize);
			break;

		}

	}
	public  void displayresult(List<SE_Result> result,int resultsize){
		int rank=1;
		System.out.println("res size:"+resultsize);
		TreeMap<String,Float> sorted=new TreeMap<String,Float>(Collections.reverseOrder());
		for(SE_Result item : result){
			
			
	
			sorted.put(item.getUrl(),item.getScore());
			
			
			
		}

		for (Entry<String,Float> entry : Descsort(sorted)) {
		    String key = entry.getKey();
		    Float value = entry.getValue();
		  
		    System.out.print(rank+ " ");
		    System.out.print(key + " ");
		    System.out.println(value);
		    if(rank<resultsize)
		    rank++;
		    else{
		    	break;
		    }
		}
		
	}
	

	
	
	
	static <K,V extends Comparable<? super V>> 
    List<Entry<K, V>> Descsort(Map<K,V> map) {

List<Entry<K,V>> a= new ArrayList<Entry<K,V>>(map.entrySet());

Collections.sort(a, 
    new Comparator<Entry<K,V>>() {
        @Override
        public int compare(Entry<K,V> value1, Entry<K,V> value2) {
            return value2.getValue().compareTo(value1.getValue());
        }
    }
);

return a;
}
	
	

}