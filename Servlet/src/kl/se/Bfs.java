package kl.se;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class Bfs {
	Queue<String> que1 = new LinkedList<String>();
	Queue<String> que00 = new LinkedList<String>();
	Queue<String> que11 = new LinkedList<String>();
	Set<String> new_unique = new HashSet<String>();
	int uniq;
	static volatile int doc_id;

	Connection conn = MySingleTon.getInstance();

	java.util.Date date = Calendar.getInstance().getTime();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd.hhmmss");

	Removeduplicates rm = new Removeduplicates();

	void initial_enqueue(String url, Datatransfer jd) throws ClassNotFoundException,
			SQLException {
		que00.clear();
		que11.clear();
		jd.putvalue_unvisited(url, 0);
		jd.put_document_ids(url);
		new_unique.add(url);
		que00.add(url);

	}

	void enqueue0(String seed, String url) throws ClassNotFoundException,
			SQLException {
		Removeduplicates rm = new Removeduplicates();
		jd.putlink(seed, url);
		uniq = rm.remove(url, new_unique);
		if (uniq == 0) {

			que00.add(url);
			jd.putvalue_unvisited(url, 0);
			jd.put_document_ids(url);
			// jd.linksid(seed,url);
			System.out.println(url);

		}
		new_unique.add(url);
	}

	void enqueue1(String seed, String url) throws ClassNotFoundException,
			SQLException {

		uniq = rm.remove(url, new_unique);
		jd.putlink(seed, url);
		if (uniq == 0) {

			que11.add(url);
			jd.putvalue_unvisited(url, 0);
			jd.put_document_ids(url);
			// jd.linksid(seed,url);
			System.out.println(url);

		}
		new_unique.add(url);
	}

	void dequeue0(String str, Datatransfer jd) throws Exception {
		String a = (sdf.format(date));
		System.out.println("Adding from dequeue 0" + que1.add(str));

		jd.putdoc(str, a);

		jd.delvalue(str, 1);

		(que00).remove();

	}

	void dequeue1(String str, Datatransfer jd) throws Exception {
		String a = (sdf.format(date));
		System.out.println("Adding from dequeue 1" + que1.add(str));

		// System.out.println("INdexer call doc_id  " + doc_id);

		jd.putdoc(str, a);

		jd.delvalue(str, 1);

		(que11).remove();

	}

	Datatransfer jd = new Datatransfer();

	void resume_crawl() throws SQLException {
		que00.clear();
		que11.clear();
		System.out.println("Queue size que00 = " + que00.size());
		System.out.println("Queue size que11 = " + que11.size());
		jd.ins_rec_que00(que00);
	}

}
