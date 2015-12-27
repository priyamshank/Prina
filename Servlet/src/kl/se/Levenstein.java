package kl.se;

public class Levenstein {

	public int distance(String db_term,String user_term){
		if(db_term.length()==0){
			return user_term.length();
		}
		if(user_term.length()==0){return db_term.length();}
		int[][] difference = new int[db_term.length() + 1][user_term.length() + 1];
		int dist;
		for (int i = 0; i < difference.length; i++) {
			for (int j = 0; j < difference[i].length; j++) {
				difference[i][j] = i == 0 ? j : j == 0 ? i : 0;
				if (i > 0 && j > 0) {
					if (db_term.charAt(i - 1) == user_term.charAt(j - 1))
						dist=0;
					else{
						dist=1;}
						difference[i][j] = Math.min(difference[i][j - 1] + 1, Math.min(
								difference[i - 1][j - 1] + dist, difference[i - 1][j] + 1));
				}}}
		return difference[db_term.length()][user_term.length()];
				}
	
}