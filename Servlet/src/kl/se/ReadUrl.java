package kl.se;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;



public class ReadUrl {
	static volatile int limit;
	static int depth;
	static boolean leavhome;
	static volatile int count = 1;
	static volatile LinkedHashMap<Integer, String> que_index = new LinkedHashMap<Integer, String>();
	static volatile DBPass db = new DBPass();
	static volatile TFIDF tf = new TFIDF();

	public static void main(String[] args) throws Exception {

		// Read the number of documents to be crawled, Seed URLS,depth

		String url = null;

		int url_nos = 0;
		boolean isreq;
		Datatransfer jd = new Datatransfer();
		jd.connect();

		HashSet<String> Valid_URL = new HashSet<String>();
		HashSet<String> Invalid_URL = new HashSet<String>();

		try {

			@SuppressWarnings("unused")
			BufferedReader buffread = new BufferedReader(new InputStreamReader(
					System.in));
			@SuppressWarnings("resource")
			Scanner scanner = new Scanner(System.in);

			do {
				System.out
						.println("Enter the number of documents to be crawled :");
				if (scanner.hasNextInt()) {
					limit = scanner.nextInt();
					isreq = true;
				} else {
					System.out.println("Please enter a number ");
					isreq = false;
					scanner.next();
				}

			} while (!(isreq));

//			do {
//				System.out.println("Enter the number of URLS to be crawled :");
//				if (scanner.hasNextInt()) {
//					url_nos = scanner.nextInt();
//					isreq = true;
//				} else {
//					System.out.println("Please enter a number ");
//					isreq = false;
//					scanner.next();
//				}
//
//			} while (!(isreq));

	//		System.out.print("Please enter " + url_nos + " url : ");
			
			System.out.print("Please enter the seed url : ");
			url = scanner.next();
			Valid_URL.add(url);
//			for (int i = 0; i < url_nos; i++) {
//				url = scanner.next();
//				Valid_URL.add(url);
//
//			}
//
//			System.out.println("Crawling only valid and unique url \n ");
//			for (String s : Valid_URL) {
//				System.out.println(s);
//			}

			do {
				System.out
						.println("Enter preference TRUE: leave domain FALSE: stay in domain");
				if (scanner.hasNextBoolean()) {
					leavhome = scanner.nextBoolean();
					isreq = true;
				} else {
					System.out
							.println("Please enter a boolean value true or false ");
					isreq = false;
					scanner.next();
				}

			} while (!(isreq));

			do {
				System.out.println("Enter maximum depth");
				if (scanner.hasNextInt()) {
					depth = scanner.nextInt();
					isreq = true;
				} else {
					System.out.println("Please enter a number ");
					isreq = false;
					scanner.next();
				}

			} while (!(isreq));

		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}

		int i = 1;

		java.util.Iterator<String> iter = Valid_URL.iterator();

		CrawlerThread R1 = null;
		Map<String, CrawlerThread> map = new HashMap<>();
		while (iter.hasNext()) {

			map.put("Thread" + i, new CrawlerThread("Thread" + i, iter.next(),
					jd));
			R1 = map.get("Thread" + i);
			R1.start();
			i++;

		}

		// call to indexer and tf_idf calculation
//		db.gettingurl(que_index);
//		tf.tfidf();

		// To stop the threads press any character
		while (true) {
			BufferedReader buffreadStop = new BufferedReader(
					new InputStreamReader(System.in));
			Scanner scanner = new Scanner(System.in);
			String pattern = "start";

			Character c = 'c';
			c = (char) (buffreadStop.read());

			for (String key : map.keySet()) {

				R1 = map.get(key);
				R1.suspend();

			}

			System.out.println("Suspending the Thread");

			// To restart the stopped threads press start
			BufferedReader buffreadResume = new BufferedReader(
					new InputStreamReader(System.in));
			String b = null;
			do {
				System.out.println("Enter 'start' to restart");
				if (scanner.hasNext(pattern)) {
					b = scanner.next(pattern);
					isreq = true;
				} else {
					System.out.println("Please enter 'start' ");
					isreq = false;
					scanner.next();
				}

			} while (!(isreq));

			if ("start".equalsIgnoreCase(b)) {

//				db.gettingurl(que_index);
//
//				tf.tfidf();

				System.out.println("Restarted \n");
				System.out.println("Inside resume");
				Thread.sleep(1000);

				for (String key1 : map.keySet()) {

					R1 = map.get(key1);
					R1.resume();

				}

				System.out.println("Resuming First Thread");
				Thread.sleep(1000);
			}
//			System.out.println();
//			db.gettingurl(que_index);
//
//			tf.tfidf();

			//System.out.println("End of program");

		}

	}

}
