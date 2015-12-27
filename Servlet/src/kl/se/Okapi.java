package kl.se;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class Okapi {
	Connection conn = null;
	Statement stmt = null;
	Statement stmt1 = null;

	public void score() throws SQLException {

		conn = MySingleTon.getInstance();
		try {
			stmt = conn.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// "WITH AVGDL AS (select sum(term_frequency)/count(distinct(docid)) as avg from fea1)"
		String query1 = "WITH avg AS (select CAST(sum(term_frequency)/count(distinct(docid))as float) from fea1)update fea1 b set okapiscore= (((log(((select count(distinct(docid)) from fea1)-(select  count(docid) from fea1 a where a.term=b.term)+0.5)/((select  count(docid) from fea1 a where a.term=b.term)+0.5)))*term_frequency*3)/(term_frequency+(2*(0.25+0.75*(((select sum(term_frequency) from fea1 a where a.docid=b.docid group by docid ))/(select * from avg))))))";

		System.out.println(query1);
		stmt.executeUpdate(query1);
		System.out.println("okapiscore calculated");

		System.out.println("Normalized okapiscore calculated");
		String n_okapi = "WITH min_max AS "
				+ "(select max(okapiscore)as max,min(okapiscore)as min from fea1)"
				+ "update fea1 set n_okapiscore=(10*(okapiscore-m.min)/(m.max-m.min))"
				+ "from min_max m";

		PreparedStatement preparedStatement = conn.prepareStatement(n_okapi);
		preparedStatement.executeUpdate();
		
		String query3 = "CREATE VIEW features_BM25  AS Select docid,term,okapiscore from fea1";
		// System.out.println(query3);

		stmt.executeUpdate(query3);

		String view_f = "CREATE VIEW features_tfidf  AS Select docid,term,tf_idf from fea1";
		// System.out.println(view_f);

		stmt.executeUpdate(view_f);

		String view_combined = "CREATE VIEW features_combinedscore  AS Select docid,term,combinedscore from fea1";
		stmt.executeUpdate(view_combined);

		stmt.close();
		 conn.commit();

		// create_view cv=new create_view();

	}
}
