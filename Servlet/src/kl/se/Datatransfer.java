package kl.se;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Queue;

public class Datatransfer {
	static Connection c = null;
	static Statement stmt = null;
	PreparedStatement pstmt = null;
	static int count = 1;
	static int inc = 1;

	public void connect() throws SQLException, ClassNotFoundException {

		try {

			c = MySingleTon.getInstance();
			stmt = c.createStatement();

			//System.out.println("Dropping old table");
			String sql_drop = "DROP TABLE IF EXISTS documents;DROP TABLE IF EXISTS links;DROP TABLE IF EXISTS url_visited;"
					+ "DROP TABLE IF EXISTS url_unvisited;DROP TABLE IF EXISTS document_ids;DROP VIEW IF EXISTS features_BM25;"
					+ "DROP VIEW IF EXISTS features_combinedscore;DROP VIEW IF EXISTS features_tfidf;DROP SEQUENCE IF EXISTS doc_id_seq;"
					+ "DROP SEQUENCE IF EXISTS docid2_seq;DROP TABLE IF EXISTS fea1;";
			stmt.execute(sql_drop);

			// documents table creation
			//System.out.println("Creating new documents tables");
			String sql1 = "CREATE TABLE documents" + "(doc_id int ,"
					+ "url           text, " + "crawled_on_date text,"+"lang text )";

			stmt.execute(sql1);

			// index on document table
			String sql2 = "CREATE INDEX DOC_INDEX ON documents(doc_id)";
			stmt.execute(sql2);

			// sequence creation on documents table
			String sql3 = "CREATE SEQUENCE doc_id_seq MINVALUE 0 ";
			stmt.execute(sql3);
			// Links table creation
			//System.out.println("Creating new links tables");
			String sql4 = "CREATE TABLE links"
					+ "(from_url_doc_id int, from_url  TEXT    NOT NULL, "
					+ " to_url_doc_id int, to_url text     NOT NULL)";
			stmt.execute(sql4);

			// url_unvisited table creation
			//System.out.println("Creating new url_unvisited tables");
			String sql5 = "CREATE TABLE url_unvisited"
					+ "(docid1 int , url           TEXT   , "
					+ " visited           INT     )";
			stmt.execute(sql5);

			// url_visited table creation
			//System.out.println("Creating new url_visited tables");
			String sql6 = "CREATE TABLE url_visited"
					+ "(url           TEXT    , "
					+ " visited           INT    )";
			stmt.execute(sql6);

			//System.out.println("Creating new doc_id tables");
			String sql7 = "CREATE TABLE document_ids" + "(docid2 int,"
					+ "url           TEXT,prscore float )";
			stmt.execute(sql7);
			String sql10 = "CREATE SEQUENCE docid2_seq MINVALUE 0";
			stmt.execute(sql10);

			stmt.close();

		} catch (SQLException e) {

			System.out.println(e.getMessage());

		}

	}

	// populate data into url_unvisied table
	public void putvalue_unvisited(String str, int i) throws SQLException,
	ClassNotFoundException {
		try {

			String insertTableSQL = "insert into url_unvisited"
					+ "(url,visited) values (?,?)";
			PreparedStatement preparedStatement = c
					.prepareStatement(insertTableSQL);

			preparedStatement.setString(1, str);
			preparedStatement.setInt(2, i);

			// execute insert SQL statement
			preparedStatement.executeUpdate();
			preparedStatement.close();

		} catch (SQLException e) {

			System.out.println(e.getMessage());
		}
	}

	// delete url after crawled from unvisited to visited
	public void delvalue(String str, int i) throws SQLException,
	ClassNotFoundException {
		try {

			String ins_sql = "insert into url_visited" + "(url,visited) values"
					+ "(?,?)";

			PreparedStatement preparedStatement_ins = c
					.prepareStatement(ins_sql);
			preparedStatement_ins.setString(1, str);
			preparedStatement_ins.setInt(2, i);

			// execute insert SQL statement
			preparedStatement_ins.executeUpdate();
			String del_sql = "DELETE FROM url_unvisited WHERE url=?";

			PreparedStatement preparedStatement = c.prepareStatement(del_sql);
			preparedStatement.setString(1, str);
			// preparedStatement.setInt(2, i);
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {

			System.out.println(e.getMessage());
		}

	}

	// populate links table
	public void putlink(String from_url, String to_url) throws SQLException,
	ClassNotFoundException {
		try {

			String ins_link = "insert into links" + "(from_url,to_url) values"
					+ "(?,?)";

			PreparedStatement preparedStatement_ins_link = c
					.prepareStatement(ins_link);
			preparedStatement_ins_link.setString(1, from_url);
			preparedStatement_ins_link.setString(2, to_url);

			preparedStatement_ins_link.executeUpdate();

			// execute insert SQL statement

		} catch (SQLException e) {

			System.out.println(e.getMessage());
		}

	}

	// populate documents table
	public void putdoc(String url, String crawled_on_date) throws Exception {
		try {
			//
			int doc_id1 = 0;

			String ins_doc = "insert into documents(doc_id,url,crawled_on_date)values((select docid2 FROM document_ids WHERE url=?),?,?)";

			PreparedStatement preparedStatement_ins_doc = c
					.prepareStatement(ins_doc);
			preparedStatement_ins_doc.setString(1, url);
			preparedStatement_ins_doc.setString(2, url);
			preparedStatement_ins_doc.setString(3, crawled_on_date);

			preparedStatement_ins_doc.executeUpdate();

			String ins_doc_id = "select nextval('doc_id_seq')";
			PreparedStatement preparedStatement_ins_doc_id = c
					.prepareStatement(ins_doc_id);

			ResultSet rs = preparedStatement_ins_doc_id.executeQuery();
			while (rs.next()) {
				doc_id1 = rs.getInt("nextval");

			}
			// to calculate tfidf
			ReadUrl.que_index.put(doc_id1, url);

			preparedStatement_ins_doc.close();

		} catch (SQLException e) {

			System.out.println(e.getMessage());
		}

	}
	public void putlang(int docid,String flag) throws SQLException{
		String ins_lang = "update documents set lang=";
				ins_lang+="'"+ flag +"'";
				ins_lang+=" WHERE doc_id=";
				ins_lang+="'"+docid+"'";
	
		PreparedStatement preparedStatement_ins_lang = c
				.prepareStatement(ins_lang);

		preparedStatement_ins_lang.executeUpdate();
		preparedStatement_ins_lang.close();
		c.commit();
	}

	public void update_to_link() throws SQLException {
		String str6 = "select docid2,url  from document_ids";

		PreparedStatement preparedStatement = c.prepareStatement(str6);
		ResultSet rs6 = preparedStatement.executeQuery();
		while (rs6.next()) {

			String replace1 = "\'" + rs6.getInt("docid2") + "\'";
			String replace2 = "\'" + rs6.getString("url") + "\'";

			// updating from_url_doc_id using data from documents table

			String str_from = "update links set from_url_doc_id=" + replace1
					+ "where from_url=" + replace2;

			String str_to = "update links set to_url_doc_id=" + replace1
					+ "where to_url=" + replace2;

			PreparedStatement preparedStatement1 = c.prepareStatement(str_from);
			PreparedStatement preparedStatement2 = c.prepareStatement(str_to);

			preparedStatement1.executeUpdate();
			preparedStatement2.executeUpdate();

			preparedStatement1.close();
			preparedStatement2.close();

			// c.commit();
		}
	}

	public void ins_rec_que00(Queue que00) throws SQLException {
		String ins_rec_que00 = "select url from url_unvisited";

		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = c.prepareStatement(ins_rec_que00);
		} catch (SQLException e) {

		}

		// execute insert SQL statement
		ResultSet rs = preparedStatement.executeQuery();
		while (rs.next()) {
			que00.add(rs.getString("url"));

		}
	}

	public void put_document_ids(String url) throws SQLException {

		String str = "insert into document_ids" + "(docid2,url) values"
				+ "(nextval('docid2_seq'),?)";
		// System.out.println(str);
		PreparedStatement preparedStatement = c.prepareStatement(str);
		preparedStatement.setString(1, url);

		// execute insert SQL statement
		preparedStatement.executeUpdate();
		preparedStatement.close();

	}

}
