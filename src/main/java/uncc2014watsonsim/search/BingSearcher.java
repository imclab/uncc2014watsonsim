package uncc2014watsonsim.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import privatedata.UserSpecificConstants;

import org.apache.http.client.fluent.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.sun.xml.internal.ws.developer.MemberSubmissionEndpointReference.Elements;

import uncc2014watsonsim.Passage;

public class BingSearcher extends Searcher {
	
	public List<Passage> query(String query) {
		//TODO: Should this be done in StringUtils?
	    query = query.replaceAll(" ", "%20");
	    String url = "https://api.datamarket.azure.com/Data.ashx/Bing/Search/v1/Web?Query=%27" + query + "%27&$top=50&$format=Atom";

	    List<Passage> results = new ArrayList<Passage>();
	    try {
	    	String resp = Executor
	    		.newInstance()
	    		.auth(UserSpecificConstants.bingAPIKey, UserSpecificConstants.bingAPIKey)
	    		.execute(Request.Get(url))
	    		.returnContent().asString();
	    	
	    	Document doc = Jsoup.parse(resp);
	    	List<Element> elements = doc.select("entry");
	    	// Perhaps limit to MAX_RESULTS?
		    for (int i=0; i < elements.size(); i++) {
		    	Element e = elements.get(i);
	    		results.add(new Passage(
        			"bing",         	// Engine
        			e.select("d|Title").text(),	        // Title
        			e.select("d|Description").text(), // Full Text
        			e.select("d|Url").text())          // Reference
    				.score("BING_RANK", (double) i) // Score
    			);
	    	}
		    System.out.print("B!");
	    } catch (IOException e) {
	    	System.out.println("Error while searching with Bing. Ignoring. Details follow.");
	        e.printStackTrace();
	    }
	    return results;
	}
}
