package com.example.maplink.utils;

import org.apache.tomcat.util.http.fileupload.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * @author Samuel Catalano
 */
public class GeocodeUtils implements Serializable{
	
	public static final String GOOGLE_API = "AIzaSyAbdZ255EEBHmUsIoDclJmyx7FHqlyt2eQ";
	private static final String URL = "http://maps.googleapis.com/maps/api/geocode/json";
	
	/**
	 * Get a Json with the full infos about the address
	 * @param fullAddress the full address
	 * @return full informations about the address
	 * @throws IOException
	 */
	public static String getJSONByGoogle(final String fullAddress) throws IOException{
		final URL url = new URL(URL + "?address=" + URLEncoder.encode(fullAddress, "UTF-8") + "&sensor=false");
		final URLConnection conn = url.openConnection();
		final ByteArrayOutputStream output = new ByteArrayOutputStream(1024);
		
		IOUtils.copy(conn.getInputStream(), output);
		output.close();
		
		return output.toString();
	}
}