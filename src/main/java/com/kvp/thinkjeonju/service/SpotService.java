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

	final static int MAX = 999999;
	final static int IMGMAX = 5;
	
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
	
	// 검색키워드를 통해 해당 값을 포함한 spot list 반환(이미지 포함)
	public ArrayList<SpotDTO> getSpotData(String dataValue) {
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
    		
    		ArrayList<String> img = new ArrayList<>();
    		
    		if(totalCount > 0) {
    			// 뽑아낸 list의 길이가 1일 때
    			if(totalCount <= 1) {
	    			obj = (JSONObject)obj.get("list");
	    			
	    			if(obj.getInt("fileCnt") > 0) {
	    				img = getSpotImgData(obj.getString("dataSid"));
	    			}
	    			
	    			spots.add(new SpotDTO(obj.getString("dataSid"), obj.getString("dataTitle"), 
	    					obj.getString("dataContent"), obj.getString("zipCode"), obj.getString("addr"), 
	    					""+obj.get("addrDtl"), obj.getDouble("posx"), obj.getDouble("posy"), 
	    					obj.getString("userHomepage"), obj.getString("tel"), obj.getInt("fileCnt"), img, SpotDTO.Category.CulturalSpace.getCategoryName(), 0, false));
    			} else { // 길이가 2이상일 때
    				JSONArray spot = (JSONArray)obj.get("list");
    				
    				// JSONArray의 각 값을 spotDTO로 바꿔서 ArrayList<SpotDTO>에 저장
            		for(int i=0; i<totalCount; i++) {
            			JSONObject tmp = (JSONObject)spot.get(i);
            			
            			if(tmp.getInt("fileCnt") > 0) {
    	    				img = getSpotImgData(tmp.getString("dataSid"));
    	    			}
            			
            			SpotDTO spotDTO = new SpotDTO(tmp.getString("dataSid"), tmp.getString("dataTitle"), 
            					tmp.getString("dataContent"), tmp.getString("zipCode"), tmp.getString("addr"), 
            					""+tmp.get("addrDtl"), tmp.getDouble("posx"), tmp.getDouble("posy"), 
            					tmp.getString("userHomepage"), tmp.getString("tel"), tmp.getInt("fileCnt"), img, SpotDTO.Category.CulturalSpace.getCategoryName(), 0, false);
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
		return spots;
	}

	// spot id 값을 통해 해당 spot의 이미지 리스트 반환
	public ArrayList<String> getSpotImgData(String id) {
		
		// DB저장용 ArrayList (모든 값 포함)
		ArrayList<SpotImgDTO> spotImgs = new ArrayList<>();
		
		// 반환용 ArrayList (이미지  url만 포함)
		ArrayList<String> url = new ArrayList<>();
		
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
        			url.add(obj.getString("thumbUrl"));
    			} else {
    				JSONArray spotImg = (JSONArray)obj.get("list");
    	    		
    				if(totalCount > IMGMAX) 
    	    			totalCount = IMGMAX;
    				
    	    		for(int i=0; i<totalCount; i++) {
    	    			JSONObject tmp = (JSONObject)spotImg.get(i);
    	    			SpotImgDTO spotImgDTO = new SpotImgDTO(tmp.getString("fileSid"), tmp.getString("dataSid"), tmp.getString("thumbUrl"));
    	    			spotImgs.add(spotImgDTO);
    	    			url.add(tmp.getString("thumbUrl"));
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
}