package de.engelhardt.serpapi;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/***
 * Encode parameter hash into a string
 *  to be sent over HTTP
 */
public class ParameterStringBuilder
 {
   /***
    * getParamsString 
    * @param params search parameters
    * @return concatenated parameters
    * @throws UnsupportedEncodingException when none UTF-8 character is part of the parameter
    */
    public static String getParamsString(Map<String, String> params)
      throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
 
        for (Map.Entry<String, String> entry : params.entrySet()) 
        {
          result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
          result.append("=");
          result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
          result.append("&");
        }
 
        String resultString = result.toString();
        return resultString.length() > 0
          ? resultString.substring(0, resultString.length() - 1)
          : resultString;
    }
}