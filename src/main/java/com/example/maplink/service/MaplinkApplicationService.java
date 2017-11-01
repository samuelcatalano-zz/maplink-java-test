package com.example.maplink.service;

import com.example.maplink.dto.LocationDTO;
import com.example.maplink.dto.PlaceDTO;
import com.example.maplink.dto.ResponseDTO;
import com.example.maplink.utils.GeocodeUtils;
import com.example.maplink.utils.HaversineUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.core.MediaType;

import static com.example.maplink.utils.GothamBoundaries.*;

/**
 * @author Samuel Catalano
 */

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/maplink")
@Transactional
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MaplinkApplicationService{
	
	@GET
	@RequestMapping("/coordinate/{lat}/{lng}")
	@Consumes("service/json")
	public ResponseEntity getByCoordinate(@PathVariable("lat") String lat, @PathVariable("lng") String lng){
		final PlaceDTO place = new PlaceDTO();
		final LocationDTO location = new LocationDTO();
		final ResponseDTO response = new ResponseDTO();
		
		try {
			lat = lat.replace(",", ".");
			lng = lng.replace(",", ".");
			
			final double probability = getProbability(lat, lng);
			
			location.setLat(Double.parseDouble(lat));
			location.setLng(Double.parseDouble(lng));
			
			place.setLocation(location);
			response.setLocation(place.getLocation());
			response.setProbability(probability);
		}
		catch(final Exception e){
			log.error(e.getMessage());
		}
		
		return ResponseEntity.ok(response);
	}
	
	@GET
	@RequestMapping("/location/{location}")
	@Consumes("service/json")
	public ResponseEntity getByLocation(@PathVariable("location") final String location){
		final Map<String,LocationDTO> places = new HashMap();
		final PlaceDTO place = new PlaceDTO();
		final ResponseDTO response = new ResponseDTO();
		
		try{
			final String json = this.readFile("Gotham.json");
			final JSONObject jsonObject = new JSONObject(json);
			final JSONArray jsonArray = jsonObject.getJSONArray("targets");
			for(int i = 0; i < jsonArray.length(); i++) {
				final JSONObject l = jsonArray.getJSONObject(i);
				final JSONObject coordinate = l.getJSONObject("location");
				final String placeName = l.get("place").toString();
				final String lat = coordinate.get("lat").toString();
				final String lng = coordinate.get("lng").toString();
				
				final LocationDTO local = new LocationDTO();
				local.setLat(Double.parseDouble(lat));
				local.setLng(Double.parseDouble(lng));
				
				places.put(placeName, local);
			}
			
			final LocationDTO local = places.get(location);
			final String lat = String.valueOf(local.getLat());
			final String lng = String.valueOf(local.getLng());
			
			final double probability = getProbability(lat, lng);
			local.setLat(Double.parseDouble(lat));
			local.setLng(Double.parseDouble(lng));
			
			place.setPlace(location);
			place.setLocation(local);
			
			response.setPlace(place.getPlace());
			response.setLocation(place.getLocation());
			response.setProbability(probability);
			
		}
		catch(final JSONException e){
			log.error(e.getMessage());
		}
		
		return ResponseEntity.ok(response);
	}
	
	@GET
	@RequestMapping("/address/{address}")
	@Consumes("service/json")
	public ResponseEntity getByAddress(@PathVariable("address") final String address){
		final PlaceDTO place = new PlaceDTO();
		final LocationDTO location = new LocationDTO();
		final ResponseDTO response = new ResponseDTO();
		
		try{
			final String json = GeocodeUtils.getJSONByGoogle(address);
			
			final JSONObject jsonObject = new JSONObject(json);
			final JSONArray jsonArray = jsonObject.getJSONArray("results");
			final JSONObject results = jsonArray.getJSONObject(0);
			final JSONObject geometry = results.getJSONObject("geometry");
			final JSONObject locations = geometry.getJSONObject("location");
			final String lat = locations.get("lat").toString();
			final String lng = locations.get("lng").toString();
			
			final double probability = getProbability(lat, lng);
			location.setLat(Double.parseDouble(lat));
			location.setLng(Double.parseDouble(lng));
			
			place.setPlace(address);
			place.setLocation(location);
			
			response.setPlace(place.getPlace());
			response.setLocation(place.getLocation());
			response.setProbability(probability);
			
		}
		catch(IOException | JSONException e){
			log.error(e.getMessage());
		}
		
		return ResponseEntity.ok(response);
	}
	
	/**
	 * Calculate the probability
	 * @param lat the latitude
	 * @param lng the longitude
	 * @return the result
	 */
	private double getProbability(@PathVariable("lat") final String lat, @PathVariable("lng") final String lng){
		final double topRightCalc = HaversineUtils.haversine(Double.parseDouble(lat), Double.parseDouble(lng), TOP_RIGHT_LAT, TOP_RIGHT_LNG);
		final double bottomLeftCalc = HaversineUtils.haversine(Double.parseDouble(lat), Double.parseDouble(lng), BOTTOM_LEFT_LAT, BOTTOM_LEFT_LNG);
		double result = topRightCalc - bottomLeftCalc;
		
		result = (result < 0 ? -result : result);
		result = (result * 95);
		result = result / 2;
		
		return ((result < 95) ? result : 95);
	}
	
	/**
	 * Read a file from directory
	 * @param fileName the file name
	 * @return return the file
	 */
	private String readFile(final String fileName){
		final StringBuilder sb = new StringBuilder();
		final ClassLoader classLoader = this.getClass().getClassLoader();
		final File configFile = new File(classLoader.getResource(fileName).getFile());
		
		try(FileInputStream inputStream = new FileInputStream(configFile)){
			final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			String line;
			
			while((line = reader.readLine()) != null){
				sb.append(line);
			}
			reader.close();
		}
		catch(final IOException e){
			log.error(e.getMessage());
		}
		
		return sb.toString();
	}
}