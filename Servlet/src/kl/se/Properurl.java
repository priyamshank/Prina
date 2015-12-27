package kl.se;

import java.net.MalformedURLException;
import java.net.URL;

// Checking for various types of URL and converting them into absolute URLS
public class Properurl {

	public String checkproperurl00(String seed, String str, Bfs bfs)
			throws MalformedURLException {

		if (str.startsWith("http") | str.startsWith("https")) {
			return str;
		} else if (str.startsWith("//")) {
			String str2=":".concat(str);
			URL url = new URL(seed);
			String protocol = url.getProtocol();
			String Str1 = (protocol).concat(str2);
			return Str1;
		}
		return null;
	}

	// Checking for URLS ending with the jsp,png,gif,js formats
	public String remove(String str) {
		if (str.endsWith(".jsp") | str.endsWith(".png") | str.endsWith(".gif")
				| str.endsWith(".js") | str.endsWith(".pdf")) {
			return null;
		} else {
			return str;
		}
	}
}
