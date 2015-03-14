package edu.uncc.cs.watsonsim.researchers;

import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;

import org.apache.log4j.Logger;

import edu.uncc.cs.watsonsim.Answer;
import edu.uncc.cs.watsonsim.Environment;
import edu.uncc.cs.watsonsim.Passage;
import edu.uncc.cs.watsonsim.Question;
import edu.uncc.cs.watsonsim.search.*;

public class PassageRetrieval extends Researcher {
	private final Searcher[] searchers;
	private final Logger log = Logger.getLogger(this.getClass());
	
	public PassageRetrieval(Environment env) {
		searchers = new Searcher[]{
			new LucenePassageSearcher(env),
			//new IndriSearcher(env),
			//new CachingSearcher(new BingSearcher(env), "bing"),
		};
	}
	
	
	@Override
	public void question(Question q) {
		int total_passages=0; // logging
		
		for (Answer a: q) {
	    	String sr = getPassageQuery(q, a);
	    	// Query every engine
	    	for (Searcher s : searchers) {
	    		List<Passage> passages = s.query(sr);
	    		total_passages += passages.size();
	    		a.passages.addAll(passages);
	    	}
		}
		
		log.info("Found " + total_passages + " supporting passages.");
	}
	
	
	private String getPassageQuery(Question q, Answer a) {
		return q.getRaw_text() + " " + Matcher.quoteReplacement(a.candidate_text);
	}

}