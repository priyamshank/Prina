import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kl.se.Levenstein;
import kl.se.MySingleTon;
import kl.se.Processing;
import kl.se.QueryProcessing;
import kl.se.SearchQuery;
import kl.se.SE_Result;
import kl.se.StopWordRemoval;

import org.json.simple.JSONArray;
//import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class DatabaseAccess extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Statement stmt;
	QueryProcessing qp=new QueryProcessing();
	static int conjsize;
	static int size;
	static List<String> Disjnew = null;
	static StringBuilder abc=new StringBuilder();
		@SuppressWarnings("unchecked")
		public void doGet(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
		
			List<String> Didumean = null;
			List<String> Didumean1 = null;
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			String title = "Prina Results";
			String docType = "<!doctype html public \"-//w3c//dtd html 4.0 "
					+ "transitional//en\">\n";
			out.println(docType + "<html>\n" + "<head><title>" + title
					+ "</title></head>\n" 
					+ "<h1 align=\"center\">" + title + "</h1>\n");
			try {
		
				//get form data in servlet
				
				int ressize = Integer.parseInt(request.getParameter("ressize"));
				String lang = request.getParameter("lang");
				String a = request.getParameter("query");
				int port=request.getLocalPort();
				
				
				
//				out.println(docType
//						+ "<h4>The query you entered in the search box: </h4>" + a
//						+ "<br>");
				final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
				final String USER = "postgres";
				final String PASS = "postgres";
				// Register JDBC driver
				Class.forName("org.postgresql.Driver");
							// Open a connection
				Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
							
				stmt = conn.createStatement();
				String search = "site:";
				String b[] = a.split("\\s+");
				Processing p = new Processing();
				SearchQuery sq=new SearchQuery();
				boolean retval=sq.QuerySearch(a,out,lang,ressize);
				if(retval){
				if (b[0].toLowerCase().indexOf(search.toLowerCase()) != -1) {
		
					//out.println(docType + "<h4>We found the keyword site </h4><br>");
					String site = b[0].substring(b[0].lastIndexOf(":") + 1);
					//out.println("website is " + site);
					StringBuilder s = new StringBuilder();
					for (int i = 1; i < b.length; i++) {
						s.append(b[i]);
						s.append(' ');
					}
					
				//	out.println("the query terms are " + s.toString() + "<br>");
				//	String con_dis_site[]=s.toString().split("\\s+");
					String scriptregex1 = "\"([^\"]*)\"";
					Pattern p1 = Pattern.compile(scriptregex1,
							Pattern.CASE_INSENSITIVE);
					Matcher m1 = p1.matcher(s);
					if (m1.find()) {
//						out.println(docType
//								+ "<h4>we found quotation marks in your query</h4><br>");
//						out.println(docType
//								+ "<h4>Running Conjunctive query for you!!</h4><br>");
						String siteop[] = m1.group(1).split("\\s+");
						if(lang.contains("english"))
						Didumean = p.NormProcess(siteop,lang);
						else
						Didumean=p.NormProcess(siteop);
						if(Didumean.isEmpty()){
							out.println("Your search query did not match any documents."
									+"<br><br>"

							+"Suggestions:"
								+"<br>"
							    +"Make sure that all words are spelled correctly. <br>"
							    +"Try different keywords.<br>"
							    +"Try more general keywords.<br>"
							    +"Try fewer keywords.<br>");
							    
						}
						String query=qp.siteconj(Didumean, lang, ressize, site);
					//	out.println(query);
						List<SE_Result> result = p.getSearchResult(query);
						p.dumpData(result,out);
						
		
					} else {
					//	out.println("<h4>We did not find quotes hence running Disjunctive query for you!!</h4> <br>");
						String siteopdisj[] = s.toString().split("\\s+");
						if(lang.contains("english")){
						Didumean = p.NormProcess(siteopdisj,lang);}
						else{
							Didumean=p.NormProcess(siteopdisj);
						}

						if(Didumean.isEmpty()){
							out.println("Your search query did not match any documents."
									+"<br><br>"

							+"Suggestions:"
								+"<br>"
							    +"Make sure that all words are spelled correctly. <br>"
							    +"Try different keywords.<br>"
							    +"Try more general keywords.<br>"
							    +"Try fewer keywords.<br>");
							    
						}
						String query=qp.sitedisj(Didumean, lang, ressize,site);
					//	out.println(query);
						List<SE_Result> result = p.getSearchResult(query);
						p.dumpData(result,out);
		
						
						
					}
				} else {
		
					String scriptregex = "\"([^\"]*)\"";
					Pattern p1 = Pattern.compile(scriptregex,
							Pattern.CASE_INSENSITIVE);
					Matcher m1 = p1.matcher(a); 
					if (m1.find()) {
//						out.println(docType
//								+ "<h4>Found Quotes! Running conjunctive query for you!!</h4><br>");
						String conjnorm[] = m1.group(1).split("\\s+");
						//out.println(conjnorm.length);
						//out.println(b.length);
						if(lang.contains("english"))
						Didumean = p.NormProcess(conjnorm,lang);
						else{
							Didumean=p.NormProcess(conjnorm);
						}
						if(Didumean.size()<conjnorm.length){
							out.print("Did you mean: ");
							StringBuilder rl=new StringBuilder();
							StringBuilder corrected=new StringBuilder();
							Didumean1 = p.Process(conjnorm,lang);
							//rl.append(" "+'"');
							for (int i = 0; i < Didumean1.size(); i++){
								if(i==0)
								rl.append("%22");
								rl.append(Didumean1.get(i));
								if(i<Didumean1.size()-1)
								rl.append("+");
								corrected.append(" ");
								corrected.append(Didumean1.get(i));
								
								
							}
								rl.append("%22");
								rl.append("&ressize="+ressize);
								rl.append("&lang="+lang);
		
								out.println("<a href=http://localhost:"+port+"/Servlet/DatabaseAccess?query="+rl.toString()+">" +corrected+"</a>");
		
						}
		
						String query=qp.conjquery(Didumean, lang, ressize);
						//out.println(query);
						List<SE_Result> result = p.getSearchResult(query);
						p.dumpData(result,out);
		
						
					}
		
					else {
						//out.println("<h4>Doing Disjunctive query for you!!</h4><br>");
						String disj[] = a.split("\\s+");
						//out.println(lang);
						if(lang.contains("english")){
							//out.println("english called");
							Didumean = p.NormProcess(disj,lang);}
							else{
						//	out.println("german called");
							Didumean=p.NormProcess(disj);}
						
						if(Didumean.size()<disj.length){
							out.print("Did you mean: ");
							StringBuilder rl=new StringBuilder();
							StringBuilder corrected=new StringBuilder();
							Didumean1 = p.Process(disj,lang);
							for (int i = 0; i < Didumean1.size(); i++){
								
								
								
								rl.append(Didumean1.get(i));
								if(i<Didumean1.size()-1)
								rl.append("+");
								corrected.append(" ");
								corrected.append(Didumean1.get(i));
							}
								rl.append("&ressize="+ressize);
								rl.append("&lang="+lang);
		
								out.println("<a href=http://localhost:"+port+"/Servlet/DatabaseAccess?query="+rl.toString()+">" +corrected+"</a>");
		
						}
						String sqlStr=qp.disjquery(Didumean, lang, ressize);
						//out.println(sqlStr);
						List<SE_Result> result = p.getSearchResult(sqlStr);
						p.dumpData(result,out);
					}
					out.println("</center>");
					out.println("</body>");
					out.println("</html>");
					out.close();
					stmt.close();
					conn.close();
				}
				}
			} catch (SQLException se) {
				// Handle errors for JDBC
				se.printStackTrace();
			} catch (Exception e) {
				// Handle errors for Class.forName
				e.printStackTrace();
			}
		
		}

}