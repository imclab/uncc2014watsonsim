package edu.uncc.cs.watsonsim.scorers;

import java.util.List;

import edu.uncc.cs.watsonsim.Answer;
import edu.uncc.cs.watsonsim.Environment;
import edu.uncc.cs.watsonsim.Question;
import edu.uncc.cs.watsonsim.nlp.Synonyms;
import edu.uncc.cs.watsonsim.nlp.Weighted;
import edu.uncc.cs.watsonsim.scorers.AnswerScorer;

/**
 * Check if the question LAT matches one of the answer LATs
 * @author Sean
 *
 */
public class LATCheck extends AnswerScorer {
	private final Synonyms syn;
	
	/**
	 * Create a new LATCheck using a shared environment
	 */
	public LATCheck(Environment env) {
		syn = new Synonyms(env);
	}
	
	@Override
	public double scoreAnswer(Question q, Answer a) {
		/*
		 * There are several options here of how to determine synonyms.
		 * 
		 * Synonym generation approaches:
		 * 1) Given a label, find the article titles.
		 * 2)*Given an article title, find the labels.
		 * 3) Given a label, find the other labels sharing an article title.
		 * 4) Given a label, find the main article, and all the links to that main article.
		 * 5) Given two labels, combine the weights of common article titles.
		 * 
		 * Synonym checking approaches:
		 * 1)*Synonymize Q's, check against A's
		 * 2) Synonymize A's, check against Q's
		 * 3) Synonymize both, combine common results
		 * 
		 * Right now, we are using (G2, C1).
		 */
		if (!q.simple_lat.isEmpty()) {
			List<Weighted<String>> question_synonyms = syn.viaWikiLinks(new String[]{q.simple_lat});
			for (Weighted<String> synonym : question_synonyms) {
				if (a.lexical_types.contains(synonym.item)) {
					System.out.println("synonym " + synonym.item
							+ " of " + q.simple_lat
							+ " matches with weight " + synonym.weight);
					return Math.log(synonym.weight);
				}
			}
		}
		return -1.0;
	}
}