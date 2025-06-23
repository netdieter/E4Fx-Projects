/**
 * 
 */
package de.engelhardt.data;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import de.engelhardt.serpapi.GoogleFinanceSearch;
import de.engelhardt.serpapi.SerpApiSearchException;

/**
 * 
 */
public class SerpAPIGoogleFinance {

	Map<String, String> parameter = new HashMap<>();
	GoogleFinanceSearch search;

	public SerpAPIGoogleFinance() {
		parameter.put("api_key", "1c8b9c26fc7e063ac7bc79e57fc3141ffa7c9bdc86f227d1e4df5c031ee08544");
		parameter.put("engine", "google_finance");
		parameter.put("q", "DAX:INDEXDB");
//		parameter.put("hl", "de");
//		parameter.put("window", "1D");
		
		search = new GoogleFinanceSearch(parameter);
	}

	public JsonArray load() {
		try {
			JsonObject results = search.getJson();
//			Gson gson = new GsonBuilder().setPrettyPrinting().create();
//		    JsonElement jsonElement = JsonParser.parseString(results.toString());
//		    String prettyJsonString = gson.toJson(jsonElement);
//			System.out.println(prettyJsonString);
			
			JsonArray values = results.getAsJsonArray("graph");
			for(JsonElement el : values) {
				System.out.println(el.toString());
			}
			return values;
		} catch (SerpApiSearchException ex) {
			System.out.println("Exception:");
			System.out.println(ex.toString());
			return null;
		}
	}
}
