package com.example.maplink.service;

import com.example.maplink.dto.*;
import com.example.maplink.utils.GeocodeUtils;
import com.example.maplink.utils.GothamBoundaries;
import com.example.maplink.utils.HaversineUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		final List<TargetDTO> targets = new ArrayList<>();
		final VillainDTO villain = new VillainDTO();
		final ResponseDTO response = new ResponseDTO();
		
		lat = lat.replace(",", ".");
		lng = lng.replace(",", ".");
		
		boolean isValid = this.checkGothamBoundaries(Double.parseDouble(lat), Double.parseDouble(lng));
		
		if(isValid) {
			villain.setName("Joker");
			villain.setLocation(new LocationDTO(Double.parseDouble(lat), Double.parseDouble(lng)));
			final Map<String,LocationDTO> places = this.getGothamPlaces();
			
			for(final Map.Entry<String,LocationDTO> map : places.entrySet()) {
				final LocationDTO dto = map.getValue();
				double probability = HaversineUtils.haversine(dto.getLat(), dto.getLng(), Double.parseDouble(lat), Double.parseDouble(lng));
				probability = ((probability * 95)/2);
				
				final TargetDTO targetDTO = new TargetDTO();
				targetDTO.setLocation(dto);
				targetDTO.setProbability(probability);
				targetDTO.setPlace(map.getKey());
				targets.add(targetDTO);
			}
			response.setVillain(villain);
			response.setTargets(targets);
			
			return ResponseEntity.ok(response);
		}
		else {
			return ResponseEntity.ok(new RestDTO("Boundaries out of range!!!"));
		}
	}
	
	@GET
	@RequestMapping("/location/{location}")
	@Consumes("service/json")
	public ResponseEntity getByLocation(@PathVariable("location") final String location){
		final List<TargetDTO> targets = new ArrayList<>();
		final VillainDTO villain = new VillainDTO();
		final ResponseDTO response = new ResponseDTO();
		
		final String lat = this.getLatLgnFromAddress(location, "lat");
		final String lng = this.getLatLgnFromAddress(location, "lng");
		
		boolean isValid = this.checkGothamBoundaries(Double.parseDouble(lat), Double.parseDouble(lng));
		
		if(isValid) {
			villain.setName("Joker");
			villain.setLocation(new LocationDTO(Double.parseDouble(lat), Double.parseDouble(lng)));
			final Map<String,LocationDTO> places = this.getGothamPlaces();
			
			for(final Map.Entry<String,LocationDTO> map : places.entrySet()) {
				final LocationDTO dto = map.getValue();
				double probability = HaversineUtils.haversine(dto.getLat(), dto.getLng(), Double.parseDouble(lat), Double.parseDouble(lng));
				probability = ((probability * 95)/2);
				
				final TargetDTO targetDTO = new TargetDTO();
				targetDTO.setLocation(dto);
				targetDTO.setProbability(probability);
				targetDTO.setPlace(map.getKey());
				targets.add(targetDTO);
			}
			response.setVillain(villain);
			response.setTargets(targets);
			
			return ResponseEntity.ok(response);
		}
		else {
			return ResponseEntity.ok(new RestDTO("Boundaries out of range!!!"));
		}
	}
	
	@GET
	@RequestMapping("/address/{address}")
	@Consumes("service/json")
	public ResponseEntity getByAddress(@PathVariable("address") final String address){
		final List<TargetDTO> targets = new ArrayList<>();
		final VillainDTO villain = new VillainDTO();
		final ResponseDTO response = new ResponseDTO();
		
		final String lat = this.getLatLgnFromAddress(address, "lat");
		final String lng = this.getLatLgnFromAddress(address, "lng");
		
		boolean isValid = this.checkGothamBoundaries(Double.parseDouble(lat), Double.parseDouble(lng));
		
		if(isValid) {
			villain.setName("Joker");
			villain.setLocation(new LocationDTO(Double.parseDouble(lat), Double.parseDouble(lng)));
			final Map<String,LocationDTO> places = this.getGothamPlaces();
			
			for(final Map.Entry<String,LocationDTO> map : places.entrySet()) {
				final LocationDTO dto = map.getValue();
				double probability = HaversineUtils.haversine(dto.getLat(), dto.getLng(), Double.parseDouble(lat), Double.parseDouble(lng));
				probability = ((probability * 95)/2);
				
				final TargetDTO targetDTO = new TargetDTO();
				targetDTO.setLocation(dto);
				targetDTO.setProbability(probability);
				targetDTO.setPlace(map.getKey());
				targets.add(targetDTO);
			}
			response.setVillain(villain);
			response.setTargets(targets);
			
			return ResponseEntity.ok(response);
		}
		else {
			return ResponseEntity.ok(new RestDTO("Boundaries out of range!!!"));
		}
	}
	
	/**
	 * Read a file from directory.
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
	
	/**
	 * Get coordinates from an specific address.
	 * @param address the address
	 * @param coordinate the coordinate
	 * @return lat or lng
	 */
	private String getLatLgnFromAddress(final String address, final String coordinate){
		final String json;
		String coord = "";
		try{
			json = GeocodeUtils.getJSONByGoogle(address);
			final JSONObject jsonObject = new JSONObject(json);
			final JSONArray jsonArray = jsonObject.getJSONArray("results");
			final JSONObject results = jsonArray.getJSONObject(0);
			final JSONObject geometry = results.getJSONObject("geometry");
			final JSONObject locations = geometry.getJSONObject("location");
			coord = locations.get(coordinate).toString();
		}
		catch(final IOException | JSONException e){
			log.error(e.getMessage());
		}
		return coord;
	}
	
	/**
	 * Get all places in Gotham.
	 * @return Map places
	 */
	private Map getGothamPlaces(){
		final Map<String,LocationDTO> places = new HashMap();
		final String json = this.readFile("Gotham.json");
		final JSONObject jsonObject;
		try{
			jsonObject = new JSONObject(json);
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
		}
		catch(final JSONException e){
			log.error(e.getMessage());
		}
		
		return places;
	}
	
	/**
	 * Check Gotham Boundaries.
	 * @param lat the latitude
	 * @param lng the longitude
	 * @return true or false
	 */
	private boolean checkGothamBoundaries(double lat, double lng){
		boolean isValid = true;
		if(!(lat >= GothamBoundaries.BOTTOM_LEFT_LAT && lat <= GothamBoundaries.TOP_RIGHT_LAT)){
			isValid = false;
		}
		if(!(lng >= GothamBoundaries.BOTTOM_LEFT_LNG && lng <= GothamBoundaries.TOP_RIGHT_LNG)){
			isValid = false;
		}
		return isValid;
	}
}