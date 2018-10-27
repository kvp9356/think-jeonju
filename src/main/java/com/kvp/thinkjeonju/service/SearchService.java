package com.kvp.thinkjeonju.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kvp.thinkjeonju.dto.SpotDTO;
import com.kvp.thinkjeonju.dto.SpotImgDTO;
import com.kvp.thinkjeonju.repository.SearchMapper;


@Service
public class SearchService {

	final int MAX = 999999;
	final int IMGMAX = 5;
	
	private String authApiKey = "DCSBMCAPSGRWVRX";
	
	@Autowired
	private SearchMapper searchMapper;
	
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
	
	public void getSpotData(String dataValue) {
		ArrayList<SpotDTO> spots = new ArrayList<>();
		
		try {
			String addr = "http://openapi.jeonju.go.kr/rest/culture/getCultureList?authApiKey=";
			String parameter = "&dataValue=" + URLEncoder.encode(dataValue, "UTF-8");
			parameter += "&pageSize=" + MAX;
			
			addr += authApiKey + parameter;
			
			String json = webConnection(addr);
            
            // XML 파일을 json으로 바꿈.
    		JSONObject obj = XML.toJSONObject(json);
    		
    		obj = (JSONObject)obj.get("rfcOpenApi");
    		obj = (JSONObject)obj.get("body");
    		long totalCount = (long)obj.get("totalCount");
    		obj = (JSONObject)obj.get("data");
    		
    		if(totalCount > 0) {
    			// 뽑아낸 list의 길이가 1일 때
    			if(totalCount <= 1) {
    			obj = (JSONObject)obj.get("list");
    			spots.add(new SpotDTO(obj.getString("dataSid"), obj.getString("dataTitle"), 
    					obj.getString("dataContent"), obj.getString("zipCode"), obj.getString("addr"), 
    					obj.getString("addrDtl"), obj.getDouble("posx"), obj.getDouble("posy"), 
    					obj.getString("userHomepage"), obj.getString("tel"), obj.getInt("fileCnt"), null));
    			} else { // 길이가 2이상일 때
    				JSONArray spot = (JSONArray)obj.get("list");
    				
    				// JSONArray의 각 값을 spotDTO로 바꿔서 ArrayList<SpotDTO>에 저장
            		for(int i=0; i<totalCount; i++) {
            			JSONObject tmp = (JSONObject)spot.get(i);
            			SpotDTO spotDTO = new SpotDTO(tmp.getString("dataSid"), tmp.getString("dataTitle"), 
            					tmp.getString("dataContent"), tmp.getString("zipCode"), tmp.getString("addr"), 
            					tmp.getString("addrDtl"), tmp.getDouble("posx"), tmp.getDouble("posy"), 
            					tmp.getString("userHomepage"), tmp.getString("tel"), tmp.getInt("fileCnt"), null);
            			spots.add(spotDTO);
            		}
    			}
    			
    			// DB에 존재하지 않는 장소만 저장
        		for(int i=0; i<spots.size(); i++) {
        			if(checkSpotIdDuplicate(spots.get(i).getId()) == 0) {	
        				addSpot(spots.get(i));
        				if(spots.get(i).getFileCnt() != 0) {				  
            				getSpotImgUrl(spots.get(i).getId());
            			}
        			}
        		}
    		}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getSpotImgUrl(String id) {
		
		ArrayList<SpotImgDTO> spotImgs = new ArrayList<>();
		
        try {
        	String addr = "http://openapi.jeonju.go.kr/rest/culture/getCultureFile?authApiKey=";
            String parameter = "&dataSid=" + URLEncoder.encode(id, "UTF-8");
			
			addr += authApiKey + parameter;
			
			String json = webConnection(addr);
			
			// XML 파일을 json으로 바꿈.
    		JSONObject obj = XML.toJSONObject(json);
    		
    		obj = (JSONObject)obj.get("rfcOpenApi");
    		obj = (JSONObject)obj.get("body");
    		long totalCount = (long)obj.get("totalCount");
    		obj = (JSONObject)obj.get("data");
    		
    		if(totalCount > 0) {
    			if (totalCount <= 1) {
        			obj = (JSONObject)obj.get("list");
        			SpotImgDTO spotImgDTO = new SpotImgDTO(obj.getString("fileSid"), obj.getString("dataSid"), obj.getString("thumbUrl"));
        			spotImgs.add(spotImgDTO);
    			} else {
    				JSONArray spotImg = (JSONArray)obj.get("list");
    	    		
    				if(totalCount > IMGMAX) 
    	    			totalCount = IMGMAX;
    				
    	    		for(int i=0; i<totalCount; i++) {
    	    			JSONObject tmp = (JSONObject)spotImg.get(i);
    	    			SpotImgDTO spotImgDTO = new SpotImgDTO(tmp.getString("fileSid"), tmp.getString("dataSid"), tmp.getString("thumbUrl"));
    	    			spotImgs.add(spotImgDTO);
    	    		}
    			}
    			
    			// DB에 존재하지 않는 이미지만 저장
        		for(int i=0; i<spotImgs.size(); i++) {
        			if(checkSpotImgDuplicate(spotImgs.get(i).getId()) == 0) {	
        				addSpotImg(spotImgs.get(i));
        			}
        		}
    		}	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<SpotDTO> searchSpot(String dataValue) {
		ArrayList<SpotDTO> spot = searchMapper.searchSpot(dataValue);
		
		for(int i=0; i<spot.size(); i++) {
			if(spot.get(i).getFileCnt() != 0) {
				ArrayList<String> img = searchMapper.searchSpotImg(spot.get(i).getId());
				spot.get(i).setImgUrl(img);
				//System.out.println(spot.get(i).getImgUrl());
			}	
		}
		return spot;
	}

	public void addSpotImg(SpotImgDTO spotImgDTO) {
		int result = searchMapper.addSpotImg(spotImgDTO);
		/*if(result <= 0) 
			throw new DatabaseException("장소 이미지 등록에 실패했습니다.");*/
	}


	public void addSpot(SpotDTO spotDTO) {
		int result = searchMapper.addSpot(spotDTO);
		/*if(result <= 0) 
			throw new DatabaseException("장소 등록에 실패했습니다.");*/
	}

	public int checkSpotIdDuplicate(String id) {
		return searchMapper.checkSpotIdDuplicate(id);
	}
	
	public int checkSpotImgDuplicate(String id) {
		return searchMapper.checkSpotImgDuplicate(id);
	}

}
