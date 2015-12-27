package kl.se;

public class SE_Result {

	float score;
	String url;
	int rank;
	public SE_Result(float score, String url, int rank) {
		super();
		this.score = score;
		this.url = url;
		this.rank = rank;
	}
	public float getScore() {
		return score;
	}
	public void setScore(float score) {
		this.score = score;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	
	
}
