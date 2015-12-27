package kl.se;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import org.la4j.matrix.*;
import org.la4j.matrix.sparse.CRSMatrix;
import org.la4j.vector.SparseVector;

public class PageRank1 {
	Connection conn = null;
	static int size;
	int count_url;
	PreparedStatement preparedStatement = null;
	Datatransfer jd = new Datatransfer();
	// get data from links table
	HashSet<Integer> from_doc_id = new HashSet<Integer>();

	public void pr() throws SQLException {

		//System.out.println("Entering Pagerank");

		// connect to database
		conn = MySingleTon.getInstance();
		SparseMatrix transmatrix = null;
		// get size of matrix
		String getsize = "SELECT count(doc_id) FROM documents";

		// System.out.println("SIZE of MATRIX=" + getsize);
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = conn.prepareStatement(getsize);
		} catch (SQLException e) {

		}

		ResultSet rs = preparedStatement.executeQuery();
		while (rs.next()) {

			// Get the size of the matrix
			size = rs.getInt("count");
			// System.out.println("SIZE" + size);
		}
		transmatrix = CRSMatrix.zero(size, size);

		// putting entries into from hash set
		String get_from = "Select doc_id from documents";
		PreparedStatement preparedStatement1 = null;

		try {
			preparedStatement1 = conn.prepareStatement(get_from);
		} catch (SQLException e) {

		}

		ResultSet rs_get_from = preparedStatement1.executeQuery();
		while (rs_get_from.next()) {

			// Get the size of the matrix
			int doc_id = rs_get_from.getInt("doc_id");
			from_doc_id.add(doc_id);
		}
		//System.out.println("FROM HASH SET" + from_doc_id);
		preparedStatement1.close();

		int[] outDegree = new int[size];

		Iterator<Integer> it_from = from_doc_id.iterator();
		while (it_from.hasNext()) {
			int doc_id_from = (it_from.next());
			{

				Iterator<Integer> it_from1 = from_doc_id.iterator();
				while (it_from1.hasNext()) {
					int doc_id_from1 = (it_from1.next());

					String replace1 = "\'" + doc_id_from + "\'";
					String replace2 = "\'" + doc_id_from1 + "\'";

					String str = "SELECT count(FROM_URL_DOC_ID) FROM LINKS where from_url_doc_id="
							+ replace1 + "and to_url_doc_id=" + replace2;
					// System.out.println(str);

					PreparedStatement preparedStatement3 = null;
					preparedStatement3 = conn.prepareStatement(str);
					ResultSet rs1 = preparedStatement3.executeQuery();
					while (rs1.next()) {

						outDegree[doc_id_from] = outDegree[doc_id_from]
								+ rs1.getInt("count");
						int mat = (int) transmatrix.get(doc_id_from,
								doc_id_from1);
						transmatrix.set(doc_id_from, doc_id_from1,
								mat + rs1.getInt("count"));
						// a.set(doc_id_from, doc_id_to, increment);
						// int increment = rs1.getInt("count");

					}

				}

				preparedStatement.close();
				preparedStatement1.close();
				// preparedStatement2.close();

			}

			// System.out.println(outDegree);
			// for (int i = 0; i < outDegree.length; i++) {
			// System.out.println(outDegree[i]);
			//
			// }

		}

		// System.out.println(transmatrix.toCSV());

		for (int i = 0; i < size; i++) {

			// Print probability for column j.
			for (int j = 0; j < size; j++) {
				Double randjump = (.10 / size);
				Double p = (Double) ((.10 / size) + ((.90 * transmatrix.get(i,
						j)) / outDegree[i]));
				
				transmatrix.set(i, j, randjump);
				
				
			
			}

		}
		// System.out.println("SIZE OF THE MATRIX" + transmatrix.cardinality());
	//	System.out.println(transmatrix.toCSV());
		// System.out.println(rankmatrix.toCSV());
		// double[] rankmatrixold = new double[size];

		SparseVector rankmatrixold = SparseVector.zero(size);
		SparseVector rankmatrixsubtract = SparseVector.zero(size);

		rankmatrixold.set(0, 1.0);

		boolean check = false;
		for (int t = 0; t < 100; t++) {

			SparseVector rankmatrixnew = SparseVector.zero(size);

			rankmatrixnew = (SparseVector)(rankmatrixold
					.multiply(transmatrix));

			rankmatrixsubtract = (SparseVector) rankmatrixold
					.subtract(rankmatrixnew);

			rankmatrixold = rankmatrixnew;

			// Comparing the absolute value of the Vector
			double var1 = 0;
			double var;
			for (int i = 0; i < size; i++) {
				var = rankmatrixsubtract.get(i);
				var1 = var1 + (var * var);
			}

			double comp = Math.sqrt(var1);

			boolean value = (comp < (0.0001));
			if (value == true) {
				check = true;
			} else {
				check = false;

			}

			/*
			 * comparing each value of the matrix for (int i = 0; i < size; i++)
			 * { double var = rankmatrixsubtract.get(i); if (var <= (0.1 /
			 * size)) { check = true; } else { check = false; break; } }
			 * 
			 * 
			 * System.out.println("SUBTRACT"); for (int i = 0; i < size; i++) {
			 * System.out.println(BigDecimal.valueOf(rankmatrixsubtract.get(i)).
			 * toPlainString()); // System.out // .printf("%2d  %5.50f\n", i,
			 * rankmatrixsubtract.get(i)); }
			 */

			// System.out.println("SUBTRACT");
			// for (int i = 0; i < size; i++) {
			// System.out.println(BigDecimal.valueOf(rankmatrixsubtract.get(i)).toPlainString());
			// // System.out
			// // .printf("%2d  %5.50f\n", i, rankmatrixsubtract.get(i));
			// }

			if (check == true) {
				break;
			}

		}

//		System.out.println("RANK");
//		for (int i = 0; i < size; i++) {
//
//			System.out.println(BigDecimal.valueOf(rankmatrixold.get(i))
//					.toPlainString());
//			// System.out.printf("%2d  %5.50f\n", i, rankmatrixold.get(i));
//		}

		System.out.println("Calculating PR");
		for (int i = 0; i < size; i++) {

			String update_fea1 = "UPDATE  fea1 set prscore="
					+ rankmatrixold.get(i) + " where docid=" + i;
			String update_docids = "UPDATE  document_ids  set prscore="
					+ rankmatrixold.get(i) + " where docid2=" + i;
			preparedStatement = conn.prepareStatement(update_fea1);
			preparedStatement.executeUpdate();
			preparedStatement = conn.prepareStatement(update_docids);
			preparedStatement.executeUpdate();

		}

		System.out.println("Calculating Normalized PR");

		String n_prscore = "WITH min_max AS (select max(prscore)as max,min(prscore)as min from document_ids) "
				+ "update fea1 f1 set n_prscore=(10*(f.prscore-m.min))/(m.max-m.min) "
				+ "from fea1 f,min_max m where f1.docid=f.docid";
		preparedStatement = conn.prepareStatement(n_prscore);
		preparedStatement.executeUpdate();

		System.out.println("Calculating Combined score");

		String combinedscore = "UPDATE FEA1 SET COMBINEDSCORE=(SELECT 0.7*N_OKAPISCORE+0.3*N_PRSCORE)";
		preparedStatement = conn.prepareStatement(combinedscore);
		preparedStatement.executeUpdate();

		conn.commit();

	}

}
//if (Double.isNaN(p)) {
//	
//} else {
//
//	transmatrix.set(i, j, p);
//}
