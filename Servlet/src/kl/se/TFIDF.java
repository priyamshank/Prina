package kl.se;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class TFIDF {
	Connection c1=null;
	Statement stmt1=null;
	public void tfidf() throws SQLException{
	 c1 = DriverManager
	         .getConnection("jdbc:postgresql://localhost:5432/postgres",
	         "postgres", "postgres");
 

		//5c1= MySingleTon.getInstance();
		 stmt1 = c1.createStatement(); 
		
		
String query3="update fea1 set tf=LOG(term_frequency)+1";
stmt1.executeUpdate(query3);

String query5="update fea1 b set idf=LOG((select cast (count(*) as float) from documents)/(select cast (count(*) as float) from fea1 a where a.term=b.term))";
stmt1.executeUpdate(query5);

String query7="update fea1 set tf_idf=tf*idf";
stmt1.executeUpdate(query7);
}
}
