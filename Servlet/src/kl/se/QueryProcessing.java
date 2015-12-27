package kl.se;

import java.util.List;

public class QueryProcessing {

	public String disjquery(List Didumean,String lang,int ressize)
	
	{	
		String sqlStr = "SELECT distinct(a.docid),b.url as url,sum(a.combinedscore) as score FROM fea1 a,documents b WHERE a.docid=b.doc_id and a.term in ( ";
		sqlStr += "'" + Didumean.get(0) + "'";
		for (int i = 1; i < Didumean.size(); ++i) {

			sqlStr += ",'" + Didumean.get(i) + "'";
		}
		sqlStr += ") and b.lang =";
		sqlStr += "'" + lang + "'";
		sqlStr += " group by docid,url ORDER BY score DESC limit(" + ressize + ")";
		return sqlStr;
	}
	public String conjquery(List Didumean,String lang,int ressize){
		String query = "select subresult.docid, sum(subresult.combinedscore)as score,url from (select docid,combinedscore,term from fea1 where term in(";
		query += "'" + Didumean.get(0) + "'";
		for (int i = 1; i < Didumean.size(); ++i) {
			// System.out.print(i);

			query += ", '" + Didumean.get(i) + "'";
		}

		query += ") group by term,docid,combinedscore)AS subresult,documents "
				+ "where subresult.docid= documents.doc_id and documents.lang = ";
		query += "'" + lang + "'";
		query += " group by subresult.docid,url "
				+ "	having count(subresult.docid)="
				+ Didumean.size() + ""
				+ "order by score DESC limit(" + ressize + ");";
		return query;
	}
	
	/*FOR CLI*/
public String disjquerycli(List Didumean,String pscore,int ressize)
	
	{	
		String sqlStr = "SELECT distinct(a.docid),b.url as url,a."+pscore+" as score FROM fea1 a,documents b WHERE a.docid=b.doc_id and a.term in ( ";
		sqlStr += "'" + Didumean.get(0) + "'";
		for (int i = 1; i < Didumean.size(); ++i) {

			sqlStr += ",'" + Didumean.get(i) + "'";
		}
		sqlStr += ") ";
		
		sqlStr += " ORDER BY score DESC limit(" + ressize + ")";
		return sqlStr;
	}
	public String conjquerycli(List Didumean,String pscore,int ressize){
		String query = "select subresult.docid, sum(subresult."+pscore+")as score,url from (select docid,"+pscore+",term from fea1 where term in(";
		query += "'" + Didumean.get(0) + "'";
		for (int i = 1; i < Didumean.size(); ++i) {
			// System.out.print(i);

			query += ", '" + Didumean.get(i) + "'";
		}

		query += ") group by term,docid,"+pscore+")AS subresult,documents "
				+ "where subresult.docid= documents.doc_id";
		
		query += " group by subresult.docid,url "
				+ "	having count(subresult.docid)="
				+ Didumean.size() + ""
				+ "order by score DESC limit(" + ressize + ");";
		return query;
	}

	
	
	public String sitedisj(List Didumean,String lang,int ressize,String site){
		String sqlStr = "SELECT distinct(a.docid),b.url as url,a.combinedscore as score FROM fea1 a,documents b WHERE a.docid in (select doc_id from documents where SUBSTRING(url FROM '.*://([^/]*)' ) like ";
		sqlStr += "'%" + site + "%'";

		sqlStr += ") and a.term in(";
		sqlStr += "'" + Didumean.get(0) + "'";
		for (int i = 1; i < Didumean.size(); ++i) {

			sqlStr += ", '" + Didumean.get(i) + "'";
		}
		sqlStr += ") and a.docid=b.doc_id  and b.lang = ";
		sqlStr += "'" + lang + "'";
		sqlStr += " ORDER BY score DESC limit(" + ressize + ")";
		return sqlStr;
	}
	public String siteconj(List Didumean,String lang,int ressize,String site){
		String query = "select subresult.docid, sum(subresult.combinedscore)as score,url from (select docid,combinedscore,term from fea1 where term in(";
		query += "'" + Didumean.get(0) + "'";
		for (int i = 1; i < Didumean.size(); ++i) {
			// System.out.print(i);

			query += ", '" + Didumean.get(i) + "'";
		}

		query += ") and docid in (select doc_id from documents where SUBSTRING(url FROM '.*://([^/]*)' ) like ";
		query += "'%" + site + "%'";
		query += ") group by term,docid,combinedscore )AS subresult,documents dd "
				+ "where subresult.docid= dd.doc_id and dd.lang = ";
		query += "'" + lang + "'";
		query += " group by subresult.docid,url having count(subresult.docid)>="
				+ Didumean.size() + ""

				+ "  order by score DESC limit(" + ressize + ");";
		return query;
	}
//	public String conjdisjquery(List Didumean,List conjdisj,String lang,int ressize){
//		String query = "(select subresult.docid, sum(subresult.tf_idf)as score,url from (select docid,tf_idf,term from fea1 where term in(";
//		query += "'" + Didumean.get(0) + "'";
//		for (int i = 1; i < Didumean.size(); ++i) {
//			// System.out.print(i);
//
//			query += ", '" + Didumean.get(i) + "'";
//		}
//
//		query += ") group by term,docid,tf_idf)AS subresult,documents "
//				+ "where subresult.docid= documents.doc_id and documents.lang = ";
//		query += "'" + lang + "'";
//		query += " group by subresult.docid,url "
//				+ "	having count(subresult.docid)="
//				+ Didumean.size() + ")";
//			query+=" INTERSECT ";
//		query += " (SELECT distinct(a.docid),a.tf_idf as score,b.url as url FROM fea1 a,documents b WHERE a.docid=b.doc_id and a.term in ( ";
//				query += "'" + conjdisj.get(0) + "'";
//				for (int i = 1; i < conjdisj.size(); ++i) {
//
//					query += ",'" + conjdisj.get(i) + "'";
//				}
//				query += ") and b.lang =";
//				query += "'" + lang + "') order by score DESC limit(" + ressize + ");";
//		return query;
//	}
	public String conjdisjquery(List conj,List disj,String lang,int ressize){
		String query="select distinct(url),sum(combinedscore) as score "+
		"from fea1 f,documents d "+
		"where f.docid = d.doc_id and d.lang=" +
		"'"+lang+"'"+
		" and term in (" +
		"'" + disj.get(0) + "'";
		for (int i = 1; i < disj.size(); ++i) {
			// System.out.print(i);

			query += ", '" + disj.get(i) + "'";
		}

		query += ") "+
		"and f.docid in (select docid from fea1 f where term in ("+
		"'" + disj.get(0) + "'";
		for (int i = 1; i < disj.size(); ++i) {
			// System.out.print(i);

			query += ", '" + disj.get(i) + "'";
		}

		query += ") "+
		"intersect "+

		"select docid from fea1 f where term in (" +
		"'" + conj.get(0) + "'";
		for (int i = 1; i < conj.size(); ++i) {
			// System.out.print(i);

			query += ", '" + conj.get(i) + "'";
		}

		query += ") "+
		"group by docid having count(*)= " +
				conj.size()+
		") group by url order by score DESC limit(" + ressize + ");";
		return query;
	}
	

	public String cwquery(List Didumean){
		String cwquery = "select sum(term_frequency) from fea1 where term in (";
		cwquery += "'" + Didumean.get(0) + "'";
		for (int i = 1; i < Didumean.size(); ++i) {

			cwquery += ", '" + Didumean.get(i) + "'";
		}
		cwquery += ")";
		return cwquery;
	}
	public String sqldf(List Didumean){
		String sqldf = "SELECT term,count(distinct(docid)) FROM fea1 WHERE term in ( ";
		sqldf += "'" + Didumean.get(0) + "'";
		for (int i = 1; i < Didumean.size(); ++i) {

			sqldf += ", '" + Didumean.get(i) + "'";
		}
		sqldf += ") group by term ";
		return sqldf;
	}
}
