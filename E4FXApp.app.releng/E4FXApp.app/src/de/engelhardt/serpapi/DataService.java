/**
 * 
 */
package de.engelhardt.serpapi;

import java.io.File;

import com.google.gson.JsonArray;

/**
 * 
 */
public interface DataService {

	
	public DataService setSymbol(String symbol);

	public DataService setInterval(String symbol);
	
	public JsonArray load();

	public void writeJSOANArray(JsonArray arr, File path);
}
