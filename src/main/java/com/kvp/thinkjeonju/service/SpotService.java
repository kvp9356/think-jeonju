package com.kvp.thinkjeonju.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kvp.thinkjeonju.dto.LikeToDTO;
import com.kvp.thinkjeonju.dto.MemberDTO;
import com.kvp.thinkjeonju.dto.SpotDTO;
import com.kvp.thinkjeonju.dto.SpotImgDTO;
import com.kvp.thinkjeonju.exception.common.DataBaseException;
import com.kvp.thinkjeonju.model.Spot;
import com.kvp.thinkjeonju.model.SpotImg;
import com.kvp.thinkjeonju.repository.SpotMapper;


@Service
public class SpotService {

	final static int PAGESIZE = 8;
	final static int IMGMAX = 5;
	final static int MAX = 99999;
	
	private final String authApiKey = "DCSBMCAPSGRWVRX";
	
	@Autowired
	private SpotMapper spotMapper;
	
	public String webConnection(String apiUrl) {
		
		String json = "";
		
		try {
			URL url = new URL(apiUrl);

			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod("GET");
			int responseCode = conn.getResponseCode();
			
			BufferedReader br;
			if(responseCode==200) { // 정상 호출
				br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			} else {  // 에러 발생
				br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
			}
			
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = br.readLine()) != null) {
				response.append(inputLine.toString());
			}
			
			// api 결과 값을 string 형식으로 받음.
			json = response.toString();
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return json;
	}
	
	public void getApiCulturalSpace() {
		ArrayList<SpotDTO> spots = new ArrayList<>();
		String imgAddr = "http://openapi.jeonju.go.kr/rest/culture/getCultureFile?authApiKey="+authApiKey+"&dataSid=";
		
		try {
			String addr = "http://openapi.jeonju.go.kr/rest/culture/getCultureList?authApiKey=";
			String parameter = "&pageSize=" + MAX;
			
			addr += authApiKey + parameter;
			
			String json = webConnection(addr);
            
            // XML 파일을 json으로 바꿈.
    		JSONObject obj = XML.toJSONObject(json);
    		
    		obj = (JSONObject)obj.get("rfcOpenApi");
    		obj = (JSONObject)obj.get("body");
    		long totalCount = (long)obj.get("totalCount");
            			
    		if(totalCount > 0) {
    			obj = (JSONObject)obj.get("data");
    			ArrayList<String> img = new ArrayList<>();
    			// 뽑아낸 list의 길이가 1일 때
    			if(totalCount <= 1) {
	    			obj = (JSONObject)obj.get("list");
	    			
	    			if((long)obj.get("fileCnt") > 0) {
	    				img = getApiSpotImgData(imgAddr, (String)obj.get("dataSid"));
	    			}
	    			
	    			spots.add(new SpotDTO((String)obj.get("dataSid"), (String)obj.get("dataTitle"), 
	    					(String)obj.get("dataContent"), (String)obj.get("addr"), 
	    					(String)(""+obj.get("addrDtl")), (double)obj.get("posx"), (double)obj.get("posy"), 
	    					(String)obj.get("userHomepage"), (String)obj.get("tel"), (long)obj.get("fileCnt"), img, SpotDTO.Category.CulturalSpace.getCategoryName(), 0, false));
    			} else { // 길이가 2이상일 때
    				JSONArray spot = (JSONArray)obj.get("list");
    				
    				// JSONArray의 각 값을 spotDTO로 바꿔서 ArrayList<SpotDTO>에 저장
            		for(int i=0; i<spot.length(); i++) {
            			JSONObject tmp = (JSONObject)spot.get(i);
            			
            			if((long)tmp.get("fileCnt") > 0) {
    	    				img = getApiSpotImgData(imgAddr, (String)tmp.get("dataSid"));
    	    			}
            			
            			SpotDTO spotDTO = new SpotDTO((String)tmp.get("dataSid"), (String)tmp.get("dataTitle"), 
            					(String)tmp.get("dataContent"), (String)tmp.get("addr"), 
            					(String)(""+tmp.get("addrDtl")), (double)tmp.get("posx"), (double)tmp.get("posy"), 
            					(String)tmp.get("userHomepage"), (String)tmp.get("tel"), (long)tmp.get("fileCnt"), img, SpotDTO.Category.CulturalSpace.getCategoryName(), 0, false);
            			spots.add(spotDTO);
            		}
    			}
    			
    			// DB에 존재하지 않는 장소만 저장
        		for(int i=0; i<spots.size(); i++) {
        			if(checkSpotIdDuplicate(spots.get(i).getId()) == 0) {	
        				addSpot(Spot.from(spots.get(i)));
        			} else {
        				spots.get(i).setLikeCnt(getLikeCnt(spots.get(i).getId()));
        			}
        		}
    		}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ArrayList<String> getApiSpotImgData(String addr, String id) {
		
		// DB저장용 ArrayList (모든 값 포함)
		ArrayList<SpotImgDTO> spotImgs = new ArrayList<>();
		
		// 반환용 ArrayList (이미지  url만 포함)
		ArrayList<String> url = new ArrayList<>();
		
        try {
        	String apiUrl = addr + URLEncoder.encode(id, "UTF-8");
			String json = webConnection(apiUrl);
			
			// XML 파일을 json으로 바꿈.
    		JSONObject obj = XML.toJSONObject(json);
    		
    		obj = (JSONObject)obj.get("rfcOpenApi");
    		obj = (JSONObject)obj.get("body");
    		long totalCount = (long)obj.get("totalCount");
    		
    		if(totalCount > 0) {
    			obj = (JSONObject)obj.get("data");
    			if (totalCount <= 1) {
        			obj = (JSONObject)obj.get("list");
        			SpotImgDTO spotImgDTO = null;
        			if(obj.get("fileSid").getClass().equals(String.class)) {
        				spotImgDTO = new SpotImgDTO((String)obj.get("fileSid"), (String)obj.get("dataSid"), (String)obj.get("fileUrl"));
        			} else {
        				spotImgDTO = new SpotImgDTO(Long.toString((long)obj.get("fileSid")), Long.toString((long)obj.get("dataSid")), (String)obj.get("fileUrl"));
        			}
        			
        			spotImgs.add(spotImgDTO);
        			url.add((String)obj.get("fileUrl"));
    			} else {
    				JSONArray spotImg = (JSONArray)obj.get("list");
    	    		
    				if(totalCount > IMGMAX) 
    	    			totalCount = IMGMAX;
    				
    	    		for(int i=0; i<totalCount; i++) {
    	    			JSONObject tmp = (JSONObject)spotImg.get(i);
    	    			SpotImgDTO spotImgDTO = new SpotImgDTO((String)(""+tmp.get("fileSid")), (String)(""+tmp.get("dataSid")), (String)tmp.get("fileUrl"));
    	    			spotImgs.add(spotImgDTO);
    	    			url.add((String)tmp.get("fileUrl"));
    	    		}
    			}
    			
    			// DB에 존재하지 않는 이미지만 저장
        		for(int i=0; i<spotImgs.size(); i++) {
        			if(checkSpotImgDuplicate(spotImgs.get(i).getId()) == 0) {	
        				addSpotImg(SpotImg.from(spotImgs.get(i)));
        			}
        		}
    		}	
		} catch (Exception e) {
			e.printStackTrace();
		}
        return url;
	}

	public void addSpotImg(SpotImg spotImg) {
		int result = spotMapper.addSpotImg(spotImg);
		if(result <= 0) 
			throw new DataBaseException("장소 이미지 등록에 실패했습니다.");
	}


	public void addSpot(Spot spot) {
		int result = spotMapper.addSpot(spot);
		if(result <= 0) 
			throw new DataBaseException("장소 등록에 실패했습니다.");
	}

	public int checkSpotIdDuplicate(String id) {
		return spotMapper.checkSpotIdDuplicate(id);
	}
	
	public int checkSpotImgDuplicate(String id) {
		return spotMapper.checkSpotImgDuplicate(id);
	}
	
	public int getLikeCnt(String id) {
		return spotMapper.getLikeCnt(id);
	}

	public void setSpotLike(LikeToDTO like) {
		spotMapper.setSpotLike(like);
	}

	public void cancelSpotLike(LikeToDTO like) {
		spotMapper.cancelSpotLike(like);
	}

	public int getIsLike(LikeToDTO like) {
		return spotMapper.getIsLike(like);
	}
	
	public ArrayList<SpotDTO> setLikeInSpotDTOs(MemberDTO m, ArrayList<SpotDTO> spots) {
		for(int i=0; i<spots.size(); i++) {
			LikeToDTO like = new LikeToDTO(m.getId(), spots.get(i).getId(), 's');
			int result = getIsLike(like);

			if(result == 1) {
				spots.get(i).setIsLike(true);
			} else {
				spots.get(i).setIsLike(false);
			}
		}
		
		return spots;
	}

	public int getSpotDataSize(HashMap<String, String> map) {
		return spotMapper.getSpotDataSize(map);
	}

	public Spot getSpotDetail(String name) {
		return spotMapper.getSpotDetail(name);
	}

	public List<SpotDTO> getBestSpots() {
		return spotMapper.findBestSpots().stream()
		.map(spot -> spot.toDTO())
		.map(spotDTO -> {
			spotDTO.setLikeCnt(spotMapper.getLikeCnt(spotDTO.getId()));
			spotDTO.setImgUrl(spotMapper.findSpotImgUrlById(spotDTO.getId()));
			return spotDTO;
		})
		.collect(Collectors.toList());
	}
	
	public void getApiCulturalExperience() {
		ArrayList<SpotDTO> spots = new ArrayList<>();
		String imgAddr = "http://openapi.jeonju.go.kr/rest/experience/getExperienceFile?authApiKey="+authApiKey+"&dataSid=";

		try {
			String addr = "http://openapi.jeonju.go.kr/rest/experience/getExperienceList?authApiKey=";
			String parameter = "&pageSize=" + MAX;
			
			addr += authApiKey + parameter;
			
			String json = webConnection(addr);
            
            // XML 파일을 json으로 바꿈.
    		JSONObject obj = XML.toJSONObject(json);
    		
    		obj = (JSONObject)obj.get("rfcOpenApi");
    		obj = (JSONObject)obj.get("body");
    		long totalCount = (long)obj.get("totalCount");
    		
    		if(totalCount > 0) {
    			obj = (JSONObject)obj.get("data");
    			ArrayList<String> img = new ArrayList<>();
    			// 뽑아낸 list의 길이가 1일 때
    			if(totalCount <= 1) {
	    			obj = (JSONObject)obj.get("list");
	    			
	    			if((long)obj.get("fileCnt") > 0) {
	    				img = getApiSpotImgData(imgAddr, (String)obj.get("dataSid"));
	    			}
	    			
	    			spots.add(new SpotDTO((String)obj.get("dataSid"), (String)obj.get("dataTitle"), 
	    					(String)obj.get("dataContent"), (String)obj.get("addr"), 
	    					(String)(""+obj.get("addrDtl")), (double)obj.get("posx"), (double)obj.get("posy"), 
	    					(String)obj.get("homepage"), (String)obj.get("tel"), (long)obj.get("fileCnt"), img, SpotDTO.Category.CulturalExperience.getCategoryName(), 0, false));
    			} else { // 길이가 2이상일 때
    				JSONArray spot = (JSONArray)obj.get("list");
    				
    				// JSONArray의 각 값을 spotDTO로 바꿔서 ArrayList<SpotDTO>에 저장
            		for(int i=0; i<spot.length(); i++) {
            			JSONObject tmp = (JSONObject)spot.get(i);
            			
            			if((long)tmp.get("fileCnt") > 0) {
    	    				img = getApiSpotImgData(imgAddr, (String)tmp.get("dataSid"));
    	    			}
            			
            			SpotDTO spotDTO = new SpotDTO();
            			
            			if(tmp.get("posx").getClass().equals(Double.class) && tmp.get("posy").getClass().equals(Double.class)) {
            				spotDTO = new SpotDTO((String)tmp.get("dataSid"), (String)tmp.get("dataTitle"), 
                					(String)tmp.get("dataContent"), (String)tmp.get("addr"), 
                					(String)(""+tmp.get("addrDtl")), (double)tmp.get("posx"), (double)tmp.get("posy"), 
                					(String)tmp.get("homepage"), (String)tmp.get("tel"), (long)tmp.get("fileCnt"), img, SpotDTO.Category.CulturalExperience.getCategoryName(), 0, false);
            			} else {
            				spotDTO = new SpotDTO((String)tmp.get("dataSid"), (String)tmp.get("dataTitle"), 
                					(String)tmp.get("dataContent"), (String)tmp.get("addr"), 
                					(String)(""+tmp.get("addrDtl")), (double)tmp.getLong("posx"), (double)tmp.getLong("posy"), 
                					(String)tmp.get("homepage"), (String)tmp.get("tel"), (long)tmp.get("fileCnt"), img, SpotDTO.Category.CulturalExperience.getCategoryName(), 0, false);
            			}            			
            			spots.add(spotDTO);
            		}
    			}
    			
    			// DB에 존재하지 않는 장소만 저장
        		for(int i=0; i<spots.size(); i++) {
        			if(checkSpotIdDuplicate(spots.get(i).getId()) == 0) {	
        				addSpot(Spot.from(spots.get(i)));
        			} else {
        				spots.get(i).setLikeCnt(getLikeCnt(spots.get(i).getId()));
        			}
        		}
    		}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getApiOutdoorSpace() {
		ArrayList<SpotDTO> spots = new ArrayList<>();
		String imgAddr = "http://openapi.jeonju.go.kr/rest/greentour/getGreentourFile?authApiKey="+authApiKey+"&dataSid=";
		
		try {
			String addr = "http://openapi.jeonju.go.kr/rest/greentour/getGreentourList?authApiKey=";
			String parameter = "&pageSize=" + MAX;
			
			addr += authApiKey + parameter;
			
			String json = webConnection(addr);
            // XML 파일을 json으로 바꿈.
    		JSONObject obj = XML.toJSONObject(json);
    		    		
    		obj = (JSONObject)obj.get("rfcOpenApi");
    		obj = (JSONObject)obj.get("body");
    		long totalCount = (long)obj.get("totalCount");
    		
    		if(totalCount > 0) {
    			obj = (JSONObject)obj.get("data");
    			ArrayList<String> img = new ArrayList<>();
    			// 뽑아낸 list의 길이가 1일 때
    			if(totalCount <= 1) {
	    			obj = (JSONObject)obj.get("list");
	    			
	    			if((long)obj.get("fileCnt") > 0) {
	    				img = getApiSpotImgData(imgAddr, Long.toString((long)obj.get("dataSid")));
	    			}
	    			
	    			SpotDTO spotDTO = null;
	    			if(obj.get("posx").getClass().equals(Double.class) && obj.get("posy").getClass().equals(Double.class)) {
        				spotDTO = new SpotDTO((String)(""+obj.get("dataSid")), (String)obj.get("dataTitle"), 
    	    					(String)obj.get("dataContent"), (String)obj.get("addr"),
    	    					"", (Double)obj.get("posx"),
    	    					(Double)obj.get("posy"), (String)obj.get("userHomepage"),
    	    					(String)obj.get("tel"), (long)obj.get("fileCnt"), img, SpotDTO.Category.OutdoorSpace.getCategoryName(), 0, false);
        			} else if(obj.get("posx").getClass().equals(String.class) && obj.get("posy").getClass().equals(Double.class)) {
        				spotDTO = new SpotDTO((String)(""+obj.get("dataSid")), (String)obj.get("dataTitle"), 
    	    					(String)obj.get("dataContent"), (String)obj.get("addr"),
    	    					"", Double.parseDouble((String)obj.get("posx")),
    	    					(Double)obj.get("posy"), (String)obj.get("userHomepage"),
    	    					(String)obj.get("tel"), (long)obj.get("fileCnt"), img, SpotDTO.Category.OutdoorSpace.getCategoryName(), 0, false);
        			} else if(obj.get("posx").getClass().equals(Double.class) && obj.get("posy").getClass().equals(String.class)) {
        				spotDTO = new SpotDTO((String)(""+obj.get("dataSid")), (String)obj.get("dataTitle"), 
    	    					(String)obj.get("dataContent"), (String)obj.get("addr"),
    	    					"", (Double)obj.get("posx"),
    	    					Double.parseDouble((String)obj.get("posy")), (String)obj.get("userHomepage"),
    	    					(String)obj.get("tel"), (long)obj.get("fileCnt"), img, SpotDTO.Category.OutdoorSpace.getCategoryName(), 0, false);
        			} else {
        				spotDTO = new SpotDTO((String)(""+obj.get("dataSid")), (String)obj.get("dataTitle"), 
    	    					(String)obj.get("dataContent"), (String)obj.get("addr"),
    	    					"", Double.parseDouble((String)obj.get("posx")),
    	    					Double.parseDouble((String)obj.get("posy")), (String)obj.get("userHomepage"),
    	    					(String)obj.get("tel"), (long)obj.get("fileCnt"), img, SpotDTO.Category.OutdoorSpace.getCategoryName(), 0, false);
        			}
	    			
	    			spots.add(spotDTO);
    			} else { // 길이가 2이상일 때
    				JSONArray spot = (JSONArray)obj.get("list");
    				
    				// JSONArray의 각 값을 spotDTO로 바꿔서 ArrayList<SpotDTO>에 저장
            		for(int i=0; i<spot.length(); i++) {
            			JSONObject tmp = (JSONObject)spot.get(i);
            			
            			if((long)tmp.get("fileCnt") > 0) {
    	    				img = getApiSpotImgData(imgAddr, Long.toString((long)tmp.get("dataSid")));
    	    			}
            			
            			SpotDTO spotDTO = null;
            			
            			if(tmp.get("posx").getClass().equals(Double.class) && tmp.get("posy").getClass().equals(Double.class)) {
            				spotDTO = new SpotDTO((String)(""+tmp.get("dataSid")), (String)tmp.get("dataTitle"), 
        	    					(String)tmp.get("dataContent"), (String)tmp.get("addr"),
        	    					"", (Double)tmp.get("posx"),
        	    					(Double)tmp.get("posy"), (String)tmp.get("userHomepage"),
        	    					(String)tmp.get("tel"), (long)tmp.get("fileCnt"), img, SpotDTO.Category.OutdoorSpace.getCategoryName(), 0, false);
            			} else if(tmp.get("posx").getClass().equals(String.class) && tmp.get("posy").getClass().equals(Double.class)) {
            				spotDTO = new SpotDTO((String)(""+tmp.get("dataSid")), (String)tmp.get("dataTitle"), 
        	    					(String)tmp.get("dataContent"), (String)tmp.get("addr"),
        	    					"", Double.parseDouble((String)tmp.get("posx")),
        	    					(Double)tmp.get("posy"), (String)tmp.get("userHomepage"),
        	    					(String)tmp.get("tel"), (long)tmp.get("fileCnt"), img, SpotDTO.Category.OutdoorSpace.getCategoryName(), 0, false);
            			} else if(tmp.get("posx").getClass().equals(Double.class) && tmp.get("posy").getClass().equals(String.class)) {
            				spotDTO = new SpotDTO((String)(""+tmp.get("dataSid")), (String)tmp.get("dataTitle"), 
        	    					(String)tmp.get("dataContent"), (String)tmp.get("addr"),
        	    					"", (Double)tmp.get("posx"),
        	    					Double.parseDouble((String)tmp.get("posy")), (String)tmp.get("userHomepage"),
        	    					(String)tmp.get("tel"), (long)tmp.get("fileCnt"), img, SpotDTO.Category.OutdoorSpace.getCategoryName(), 0, false);
            			} else {
            				spotDTO = new SpotDTO((String)(""+tmp.get("dataSid")), (String)tmp.get("dataTitle"), 
        	    					(String)tmp.get("dataContent"), (String)tmp.get("addr"),
        	    					"", Double.parseDouble((String)tmp.get("posx")),
        	    					Double.parseDouble((String)tmp.get("posy")), (String)tmp.get("userHomepage"),
        	    					(String)tmp.get("tel"), (long)tmp.get("fileCnt"), img, SpotDTO.Category.OutdoorSpace.getCategoryName(), 0, false);
            			}
            			spots.add(spotDTO);
            		}
    			}
    			
    			// DB에 존재하지 않는 장소만 저장
        		for(int i=0; i<spots.size(); i++) {
        			if(checkSpotIdDuplicate(spots.get(i).getId()) == 0) {	
        				addSpot(Spot.from(spots.get(i)));
        			} else {
        				spots.get(i).setLikeCnt(getLikeCnt(spots.get(i).getId()));
        			}
        		}
    		}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getApiStreetTour() {
		ArrayList<SpotDTO> spots = new ArrayList<>();
		String imgAddr = "http://openapi.jeonju.go.kr/rest/tourstreet/getTourstreetFile?authApiKey="+authApiKey+"&dataSid=";
		
		try {
			String addr = "http://openapi.jeonju.go.kr/rest/tourstreet/getTourstreetList?authApiKey=";
			String parameter = "&pageSize=" + MAX;
			
			addr += authApiKey + parameter;
			
			String json = webConnection(addr);
            
            // XML 파일을 json으로 바꿈.
    		JSONObject obj = XML.toJSONObject(json);

    		obj = (JSONObject)obj.get("rfcOpenApi");
    		obj = (JSONObject)obj.get("body");
    		long totalCount = (long)obj.get("totalCount");
    		
    		if(totalCount > 0) {
    			obj = (JSONObject)obj.get("data");
    			ArrayList<String> img = new ArrayList<>();
    			// 뽑아낸 list의 길이가 1일 때
    			if(totalCount <= 1) {
	    			obj = (JSONObject)obj.get("list");
	    			
	    			if((long)obj.get("fileCnt") > 0) {
	    				img = getApiSpotImgData(imgAddr, Long.toString((long)obj.get("dataSid")));
	    			}
	    			
	    			spots.add(new SpotDTO(Long.toString((long)obj.get("dataSid")), (String)obj.get("dataTitle"), 
	    					(String)obj.get("topDataContent"), "", "", 
	    					(double)obj.get("posx"), (double)obj.get("posy"), 
	    					"", "", (long)obj.get("fileCnt"), img, SpotDTO.Category.StreetTour.getCategoryName(), 0, false));
    			} else { // 길이가 2이상일 때
    				JSONArray spot = (JSONArray)obj.get("list");
    				
    				// JSONArray의 각 값을 spotDTO로 바꿔서 ArrayList<SpotDTO>에 저장
            		for(int i=0; i<spot.length(); i++) {
            			JSONObject tmp = (JSONObject)spot.get(i);
            			
            			if((long)tmp.get("fileCnt") > 0) {
    	    				img = getApiSpotImgData(imgAddr, Long.toString((long)tmp.get("dataSid")));
    	    			}
            			
            			SpotDTO spotDTO = new SpotDTO(Long.toString((long)tmp.get("dataSid")), (String)tmp.get("dataTitle"), 
            					(String)tmp.get("topDataContent"), "", "", 
            					(double)tmp.get("posx"), (double)tmp.get("posy"), 
            					"", "", (long)tmp.get("fileCnt"), img, SpotDTO.Category.StreetTour.getCategoryName(), 0, false);
            			spots.add(spotDTO);
            		}
    			}
    			
    			// DB에 존재하지 않는 장소만 저장
        		for(int i=0; i<spots.size(); i++) {
        			if(checkSpotIdDuplicate(spots.get(i).getId()) == 0) {	
        				addSpot(Spot.from(spots.get(i)));
        			} else {
        				spots.get(i).setLikeCnt(getLikeCnt(spots.get(i).getId()));
        			}
        		}
    		}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ArrayList<SpotDTO> getSpotData(HashMap<String, String> map) {
		ArrayList<Spot> spots = spotMapper.getSpotData(map);
		return (ArrayList)spots.stream().map(spot -> spot.toDTO()).collect(Collectors.toList());
	}

	public ArrayList<String> getSpotImg(String id) {
		ArrayList<SpotImg> img = spotMapper.getSpotImg(id);
		ArrayList<String> url = new ArrayList<>();
		
		for(int i=0; i<img.size(); i++) {
			url.add(img.get(i).toDTO().getUrl());
		}
		return url;
	}

	/*public ArrayList<SpotDTO> getSpotDataByCategory(HashMap<String, String> map) {
		ArrayList<Spot> spots = spotMapper.getSpotDataByCategory(map);
		return (ArrayList)spots.stream().map(spot -> spot.toDTO()).collect(Collectors.toList());
	}*/
}