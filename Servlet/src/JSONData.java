

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kl.se.Levenstein;
import kl.se.Processing;
import kl.se.QueryProcessing;
import kl.se.SearchQuery;
import kl.se.SE_Result;
import kl.se.StopWordRemoval;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class JSONData extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static JSONObject obj1 = new JSONObject();
	public static JSONArray abc = new JSONArray();
	public static JSONArray dfarr = new JSONArray();
	public static JSONObject objdf = new JSONObject();
	public static JSONObject mainObj = new JSONObject();
	Statement stmt;
	QueryProcessing qp=new QueryProcessing();
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		mainObj.clear();
		obj1.clear();
		abc.clear();
		dfarr.clear();
		objdf.clear();
	
		StopWordRemoval r = new StopWordRemoval();
		Levenstein l = new Levenstein();
		List<String> Didumean = null;
		List<String> Didumean1 = null;
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String title = "Prina Results";
		String docType = "<!doctype html public \"-//w3c//dtd html 4.0 "
				+ "transitional//en\">\n";
		out.println(docType + "<html>\n" + "<head><title>" + title
				+ "</title></head>\n" + "<body bgcolor=\"#f0f0f0\">\n"
				+ "<h1 align=\"center\">" + title + "</h1>\n");
		try {

			
			final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
			final String USER = "postgres";
			final String PASS = "postgres";
			// Register JDBC driver
			Class.forName("org.postgresql.Driver");
						// Open a connection
			Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
						
			stmt = conn.createStatement();
			int ressize = Integer.parseInt(request.getParameter("ressize"));
			String lang = request.getParameter("lang");
			String a = request.getParameter("query");
			int port=request.getLocalPort();
//			out.println(docType
//					+ "<h4>The query you entered in the search box: </h4>" + a
//					+ "<br>");
			String search = "site:";
			String b[] = a.split("\\s+");
			int size = b.length;
			Processing p = new Processing();

			obj1.put("query", a);
			obj1.put("k", ressize);
			mainObj.put("Query", obj1);
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
				
				//out.println("the query terms are " + s.toString() + "<br>");
				String con_dis_site[]=s.toString().split("\\s+");
				String scriptregex1 = "\"([^\"]*)\"";
				Pattern p1 = Pattern.compile(scriptregex1,
						Pattern.CASE_INSENSITIVE);
				Matcher m1 = p1.matcher(s);
				if (m1.find()) {
//					out.println(docType
//							+ "<h4>we found quotation marks in your query</h4><br>");
//					out.println(docType
//							+ "<h4>Running Conjunctive query for you!!</h4><br>");
					String siteop[] = m1.group(1).split("\\s+");
					if(lang.contains("english"))
					Didumean = p.NormProcess(siteop,lang);
					else{
						Didumean=p.NormProcess(siteop);
					}
//					if(Didumean.size()<siteop.length){
//						out.print("Did you mean: ");
//						StringBuilder rl=new StringBuilder();
//						rl.append("site:"+site);
//						Didumean1 = p.Process(siteop);
//						rl.append(" "+'"');
//						for (int i = 0; i < Didumean1.size(); i++){
//							rl.append(" ");
//							rl.append(Didumean1.get(i));
//							
//						}
//						rl.append('"');
//							out.println("<a href>"+rl.toString()+"</a>");
//					}
					String query=qp.siteconj(Didumean, lang, ressize, site);
					//out.println(query);
					List<SE_Result> result = p.getSearchResult(query);
					
					Result_cal(result);
					cw_cal(Didumean);
					stat_cal(Didumean);
					print(mainObj,out);
					

				} else {
					//out.println("<h4>We did not find quotes hence running Disjunctive query for you!!</h4> <br>");
					String siteopdisj[] = s.toString().split("\\s+");
					if(lang.contains("english"))
					Didumean = p.NormProcess(siteopdisj,lang);
					else{
						Didumean=p.NormProcess(siteopdisj);
					}
//					if(Didumean.size()<siteopdisj.length){
//						out.print("Did you mean: ");
//						StringBuilder rl=new StringBuilder();
//						rl.append("site:"+site);
//						Didumean1 = p.Process(siteopdisj);
//						rl.append(" "+'"');
//						for (int i = 0; i < Didumean1.size(); i++){
//							rl.append(" ");
//							rl.append(Didumean1.get(i));
//							
//						}
//						rl.append('"');
//							out.println("<a href>"+rl.toString()+"</a>");
//					}

					String query=qp.sitedisj(Didumean, lang, ressize,site);
					//out.println(query);
					List<SE_Result> result = p.getSearchResult(query);

					Result_cal(result);
					cw_cal(Didumean);
					stat_cal(Didumean);
					print(mainObj,out);
				}
			} else {

				String scriptregex = "\"([^\"]*)\"";
				Pattern p1 = Pattern.compile(scriptregex,
						Pattern.CASE_INSENSITIVE);
				Matcher m1 = p1.matcher(a);
				if (m1.find()) {
//					out.println(docType
//							+ "<h4>Found Quotes! Running conjunctive query for you!!</h4><br>");
					String conjnorm[] = m1.group(1).split("\\s+");
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

							out.println("<a href=http://localhost:1234/Servlet/JSONData?query="+rl.toString()+">" +corrected+"</a>");

					}

					String query=qp.conjquery(Didumean, lang, ressize);
					//out.println(query);
					List<SE_Result> result = p.getSearchResult(query);
					
					Result_cal(result);
					cw_cal(Didumean);
					stat_cal(Didumean);
					print(mainObj,out);
				}

				else {
					//out.println("<h4>Doing Disjunctive query for you!!</h4><br>");
					String disj[] = a.split("\\s+");
					if(lang.contains("english"))
					Didumean = p.NormProcess(disj,lang);
					else{
						Didumean=p.NormProcess(disj);
					}
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

							out.println("<a href=http://localhost:1234/Servlet/JSONData?query="+rl.toString()+">" +corrected+"</a>");

					}
					String sqlStr=qp.disjquery(Didumean, lang, ressize);
				//	out.println(sqlStr);
					List<SE_Result> result = p.getSearchResult(sqlStr);
					
					
					Result_cal(result);
					cw_cal(Didumean);
					stat_cal(Didumean);
					print(mainObj,out);
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

	@SuppressWarnings("unchecked")
	public void cw_cal(List<String> Didumean) throws SQLException {
		String cwquery=qp.cwquery(Didumean);
		ResultSet rs1 = stmt.executeQuery(cwquery);
		while (rs1.next()) {
			int cw = rs1.getInt("sum");

			mainObj.put("cw", cw);
		}

	}

	@SuppressWarnings("unchecked")
	public void stat_cal(List<String> Didumean) throws SQLException {
		String sqldf=qp.sqldf(Didumean);
		ResultSet rs2 = stmt.executeQuery(sqldf);
		while (rs2.next()) {
			String term = rs2.getString("term");
			int df = rs2.getInt("count");
			objdf = new JSONObject();
			objdf.put("term", term);
			objdf.put("df:", df);
			dfarr.add(objdf);
		}

		mainObj.put("stat", dfarr);
	}
	public void Result_cal(List<SE_Result> result){
		for(SE_Result item : result) {
			
			JSONObject obj = new JSONObject();
			obj = new JSONObject();
			obj.put("url:", item.getUrl());
			obj.put("rank:", item.getRank());
			obj.put("score:", item.getScore());
			abc.add(obj);
		}

		mainObj.put("Results", abc);
	}

	public void print(JSONObject mainObj,PrintWriter out) throws IOException {
		StringWriter output3 = new StringWriter();

		mainObj.writeJSONString(output3);
		String jsonText = output3.toString();
		out.println(jsonText);
		mainObj.clear();
	}
	
}
	


