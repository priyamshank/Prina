package kl.se;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JBDCConnection {

	public void connect() throws SQLException, ClassNotFoundException {
		Connection c = null;
		Statement stmt = null;
		Statement stmt8=null;
		Statement stmt7 = null;
		// Class.forName("org.postgresql.Driver");
		// c = DriverManager
		// .getConnection("jdbc:postgresql://localhost:5432/postgres",
		// "postgres", "postgres");
		// System.out.println("Opened database successfully");

		c = MySingleTon.getInstance();
		stmt = c.createStatement();
		DatabaseMetaData dbm = c.getMetaData();
		// check if "fea1" table is there
		ResultSet tables = dbm.getTables(null, null, "fea1", null);
		if (tables.next()) {
			// Table exists
			System.out.println("table exists");
		} else {
			System.out.println("table does not exist");

			//String sql = "CREATE TABLE fea1(docid int, term text,term_frequency int,tf float,idf float,tf_idf float,nqi float,IDF_OKAPI float,Doc_length int,avgdl float,OkapiScore float,n_okapiscore float,Prscore float,n_prscore float,combinedScore float)";
			String sql = "CREATE TABLE fea1(docid int, term text,term_frequency int,tf float,idf float,tf_idf float,OkapiScore float,n_okapiscore float,Prscore float,n_prscore float,combinedScore float)";
			stmt.executeUpdate(sql);
			stmt8 = c.createStatement();
			String sql8 = "CREATE INDEX TERM_INDEX ON fea1(term)";
			stmt8.execute(sql8);
			stmt8.close();
			
			stmt7 = c.createStatement();
			String sql7 = "CREATE INDEX DOC_ID_INDEX ON fea1(docid)";
			stmt7.execute(sql7);
			stmt7.close();
			
		}

	}

}
