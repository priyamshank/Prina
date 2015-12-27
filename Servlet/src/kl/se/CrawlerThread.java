package kl.se;

import java.sql.SQLException;

class CrawlerThread implements Runnable {
	public Thread t;
	private String threadName;
	boolean suspended = false;
	int a = 0;
	String url;
	DBPass db = new DBPass();
	TFIDF tf = new TFIDF();
	Bfs bfs = new Bfs();
	Datatransfer jd;

	CrawlerThread(String name, String url1, Datatransfer jd1)
			throws ClassNotFoundException, SQLException {
		threadName = name;
		System.out.println("Creating " + threadName);
		url = url1;
		jd = jd1;
		bfs.initial_enqueue(url, jd);

	}

	// Method of the crawler which crawls according to the users input

	public void run() {
		System.out.println("Running " + threadName);
		try {

			while ((bfs.que00 != null || bfs.que11 != null)
					&& ReadUrl.count <= ReadUrl.limit && a <= ReadUrl.depth) {
				{
					System.out.println("Thread number" + threadName + "count"
							+ ReadUrl.count);

					Regexmatch rm = new Regexmatch();

					try {
						Thread.sleep(300);
						synchronized (this) {
							while (suspended) {
								wait();
							}
						}
						if (bfs.que00.size() != 0) {
							while (bfs.que00.size() != 0) {

								synchronized (this) {
									while (suspended) {
										wait();
									}
								}
								System.out.println("Seed URL from que00: "
										+ bfs.que00.peek());
								rm.crawl(bfs.que00.peek(), ReadUrl.leavhome,
										00, bfs);

								bfs.dequeue0(bfs.que00.peek(), jd);
								ReadUrl.count++;
								if (ReadUrl.count > ReadUrl.limit)
									break;
							}
							a++;
							System.out.println("Depth=" + a);
						} else if (bfs.que11.size() != 0) {
							while (bfs.que11.size() != 0) {

								System.out.println("Seed URL from que11: "
										+ bfs.que11.peek());
								rm.crawl(bfs.que11.peek(), ReadUrl.leavhome,
										11, bfs);
								bfs.dequeue1(bfs.que11.peek(), jd);
								ReadUrl.count++;

								synchronized (this) {
									while (suspended) {
										wait();
									}
								}
								if (ReadUrl.count > ReadUrl.limit)
									break;

							}
							a++;
							System.out.println("Depth=" + a);
						}

					} catch (Exception e1) {

					}

				}

			}

			try {
				System.out.println("TFIDF_score calculating");
				ReadUrl.db.gettingurl(ReadUrl.que_index);
				ReadUrl.tf.tfidf();

				System.out.println("links_hash");
				// jd.getlink_from();
				// jd.getlink_to();

				jd.update_to_link();
				//okapi score calculation
				System.out.println("Okapi_score calculating");
				Okapi Okapi_score = new Okapi();
				Okapi_score.score();
				//page rank calculation
				PageRank1 pagerank1 = new PageRank1();
				pagerank1.pr();

				System.out.println("In cralwerthread end of program");

			} catch (SQLException e) {
				e.printStackTrace();
			}

			Thread.sleep(300);

		} catch (InterruptedException e) {
			System.out.println("Thread " + threadName + " interrupted.");
		}

	}

	public void start() {
		System.out.println("Starting " + threadName);
		if (t == null) {
			t = new Thread(this, threadName);
			t.start();
		}
	}

	void suspend() {
		suspended = true;
	}

	synchronized void resume() {
		suspended = false;
		System.out.println("Resuming the thread");
		notify();
	}
}
