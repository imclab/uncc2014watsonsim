package watson;

/** Holds one search result, associated with one document.
 * 
 * Note the meaning of score and max_score.
 * Lower scores are better (and can be negative).
 * worst_score is the worst possible score for this result (which may not have existed in the set)
 * Scores are later scaled according to ( 
 * 
 * */ 
public class Result {
	// Inherent
	public String docid;
	public String text;
	public String title;
	// Calculated
	private double score;
	// Dependent on engine
	// You can set this via inheritance if you prefer.
	public double best_score = 0; 
	public double worst_score = 1;
	
	/** Convenience constructor */
	public Result(String docid, String title, String text, double score) {
		this.docid = docid;
		this.title = title;
		this.text = text;
		setScore(score);
	}
	
	/** Copy constructor */
	public Result(Result result) {
		this.docid = result.docid;
		this.title = result.title;
		this.text = result.text;
		// Don't normalize the score twice.
		this.score = result.score;
		this.best_score = result.best_score;
		this.worst_score = result.worst_score;
	}
	
	/** Return normalized score.
	 * This may not be same number you put into setScore.
	 */
	public double getScore() {
		return score;
	}
	
	
	/** Autogenerated hashcode: includes only title */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	/** Results are equal if their titles are equal
	 * TODO: Use fuzzy matches
	 * Note that the transitive property will not remain
	 * */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Result other = (Result) obj;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

	/** Normalize scores to be from 0 to 1, less is better. */
	public Result setScore(double raw) {
		score = (raw - best_score) / (worst_score - best_score);
		return this;
	}
}