/**
 * 
 */
package de.engelhardt.serpapi;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import javax.inject.Singleton;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * 
 */
@Singleton
@Creatable
public class DataServiceImpl implements DataService{
	 private static final Logger logger = LoggerFactory.getLogger(DataService.class);
	 
	SerpApiSearch search;
	
	public DataServiceImpl() {
		super();
		search = new SerpApiSearch("1c8b9c26fc7e063ac7bc79e57fc3141ffa7c9bdc86f227d1e4df5c031ee08544", "google_finance");
//		search.setStackTrace(null);
		search.parameter.put("hl", "de");
	}
	
//	parameter.put("q", "DAX:INDEXDB");
	@Override
	public DataServiceImpl setSymbol(String symbol){
		search.parameter.put("q", symbol);
		return this;
	}

//	parameter.put("window", "1D");
	@Override
	public DataServiceImpl setInterval(String symbol){
		search.parameter.put("q", symbol);
		return this;
	}
	@Override
	public JsonArray load() {
		try {
			JsonObject results = search.getJson();
			JsonArray values = results.getAsJsonArray("graph");
			for(JsonElement el : values) {
				System.out.println(el.toString());
			}
			return values;
		} catch (SerpApiSearchException ex) {
			System.out.println("Exception:");
			System.out.println(ex.toString());
			logger.error(ex.getMessage(), ex);
			return null;
		}
	}
	@Override
	public void writeJSOANArray(JsonArray arr, File path) {
		 Gson gson = new Gson();
		try(Writer writer = new FileWriter(path)){
			gson.toJson(arr, writer);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

}
