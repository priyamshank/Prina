

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
 
public class New extends HttpServlet{
    
  public void doGet(HttpServletRequest request,
                    HttpServletResponse response)
            throws ServletException, IOException
  {
      
      final String DB_URL="jdbc:postgresql://localhost:5432/postgres";
      final String USER = "postgres";
      final String PASS = "postgres";

      // Set response content type
//  response.setContentType("application/json");
//  response.setCharacterEncoding("utf-8");
     response.setContentType("text/html");
      PrintWriter out = response.getWriter();
      String title = "Database Result";
      String docType =
        "<!doctype html public \"-//w3c//dtd html 4.0 " +
         "transitional//en\">\n";
         out.println(docType +
         "<html>\n" +
         "<head><title>" + title + "</title></head>\n" +
         "<body bgcolor=\"#f0f0f0\">\n" +
         "<h1 align=\"center\">" + title + "</h1>\n");
      try{
         // Register JDBC driver
         Class.forName("org.postgresql.Driver");

         // Open a connection
         Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
         //String query=request.getParameter("query");
         // Execute SQL query
        Statement stmt = conn.createStatement();
        int ressize = Integer.parseInt(request.getParameter("ressize"));
         String a=request.getParameter("query");
         out.println(a);
        String b[]= a.split("\\s+");
        
         
        	String scriptregex = "\"([^\"]*)\"";
     	    Pattern p1 = Pattern.compile(scriptregex,Pattern.CASE_INSENSITIVE);
     	    Matcher m1 = p1.matcher(a);
     	   if(m1.find()) {
     		  
     		  out.println("<h4>"+m1.group(1)+"</h4>");
     		  String conj[]=m1.group(1).split("\\s+");
     		  String query = "select subresult.docid, sum(tfidf)as score,url from (select * from fea1 where term in(";
     				 query += "'" + conj[0] + "'";  
              for (int i = 1; i < conj.length;++i) {
             	// System.out.print(i);
             	 
             	 query+= ", '" + conj[i] + "'";
             }
     				
     				query+= "))AS subresult,documents "
     				+ "where subresult.docid= documents.doc_id group by subresult.docid,url having count(subresult.docid)="
     				+ conj.length + "" + "order by score DESC limit(" + ressize + ");";
     		 out.println(query);
             ResultSet rs = stmt.executeQuery(query);
             out.println(docType+"<h4>connected to db</h4>");
            

             
             int rank=1;
             // Extract data from result set
             while(rs.next()){
                //Retrieve by column name
                //int docid  = rs.getInt("docid");
                //String url = rs.getString("url");
            	
                String url = rs.getString("url");
                float score = rs.getFloat("score");
                
                out.println("Rank: "+rank+"<br>");
               
                //Display values
               // out.println("docid: " + docid + "<br>");
                //out.println(" url: " + url + "<br>");
                
                out.println("url : " + url + "<br>");
                out.println("score: " + score + "<br>");
                rank++;
                out.println("<br>");
                
     		  
     		}
             rs.close();
     	   }
        	 
     	   else{
         String sqlStr = "SELECT b.url as url,a.tfidf as score FROM fea1 a,documents b WHERE a.docid=b.doc_id and a.term in(";
         sqlStr += "'" + b[0] + "'";  
         for (int i = 1; i < b.length;++i) {
        	// System.out.print(i);
        	 
        	 sqlStr+= ", '" + b[i] + "'";
        }
         sqlStr += ") ORDER BY score DESC limit(" + ressize + ")";
         out.println(sqlStr);
         ResultSet rs = stmt.executeQuery(sqlStr);
         out.println(docType+"<h4>connected to db</h4>");
        

         
         int rank=1;
         // Extract data from result set
         while(rs.next()){
            //Retrieve by column name
            //int docid  = rs.getInt("docid");
            //String url = rs.getString("url");
        	
            String url = rs.getString("url");
            float score = rs.getFloat("score");
            
            out.println("Rank: "+rank+"<br>");
           
            //Display values
           // out.println("docid: " + docid + "<br>");
            //out.println(" url: " + url + "<br>");
            
            out.println("url : " + url + "<br>");
            out.println("score: " + score + "<br>");
            rank++;
            out.println("<br>");
            
         }
         
         rs.close();
     	   }
         
         
         out.println("</body></html>");
         out.println("got data from db");
         // Clean-up environment
         out.close();
         stmt.close();
         conn.close();
      }catch(SQLException se){
         //Handle errors for JDBC
         se.printStackTrace();
      }catch(Exception e){
         //Handle errors for Class.forName
         e.printStackTrace();
      }
   }
} 