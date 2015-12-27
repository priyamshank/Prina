package kl.se;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// URLS extraction using regular expression 

public class Regexmatch {

	public void crawl(String url, boolean leavhome, int q, Bfs bfs)
			throws Exception {
		System.out.println("Entering regex");
		URL url1;
		InputStream opens = null;
		BufferedReader br;
		String line;
		boolean home = leavhome;
		try {
			url1 = new URL(url);
			opens = url1.openStream();
			br = new BufferedReader(new InputStreamReader(opens));
			while ((line = br.readLine()) != null) {

				String patternString = ("<a\\s+href\\s*=\\s*\"?(.*?)[\"|>]");

				Pattern pattern = Pattern.compile(patternString,
						Pattern.CASE_INSENSITIVE);
				Matcher matcher = pattern.matcher(line);
				while (matcher.find()) {
					String toadd = (line.substring(matcher.start(0),
							matcher.end(0)).replaceAll("(<a href=)", ""));
					String toadd1 = (toadd.replaceAll("(<a  href=)", ""));
					String toadd2 = (toadd1.replaceAll("\"", ""));

					String toadd3 = null, toadd4 = null;
					Properurl pu = new Properurl();
					String val = pu.checkproperurl00(url, toadd2, bfs);
					if (val != null) {
						toadd3 = val;
					} else {
						continue;
					}
					// ProperUrl1 pu = new ProperUrl1();
					// Checking to crawl outside the domain or in the domain
					if (home == true) {

						// call to properurl class

						String val1 = pu.remove(toadd3);
						if (val1 != null) {
							toadd4 = val1;
						} else {
							continue;
						}

						if (q == 00) {
							bfs.enqueue1(url, toadd4);
						} else {
							bfs.enqueue0(url, toadd4);
						}

					}

					else if (home == false) {

						String val3 = pu.checkproperurl00(url, toadd2, bfs);
						if (val3 != null) {
							toadd3 = val3;
						} else {
							continue;
						}

						String val4 = pu.remove(toadd3);
						if (val4 != null) {
							toadd4 = val4;
						} else {
							continue;
						}
						boolean a;
						a = toadd4.startsWith(url);
						if (a == true) {

							if (q == 00) {
								bfs.enqueue1(url, toadd4);
							} else {
								bfs.enqueue0(url, toadd4);
							}
						}
					}
				}

			}
		}

		catch (Exception e) {

		}

	}
}


