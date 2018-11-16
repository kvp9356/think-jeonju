package com.kvp.thinkjeonju.model;

import org.apache.ibatis.type.Alias;

import com.kvp.thinkjeonju.dto.SpotImgDTO;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Alias("spotImg")
@NoArgsConstructor
@AllArgsConstructor
public class SpotImg {

	private String id;
	private String spotId;
	private String url;
	
	public static SpotImg from(SpotImgDTO spotImgDTO) {
		return new SpotImg(spotImgDTO.getId(), spotImgDTO.getSpotId(), spotImgDTO.getUrl());
	}
	
	public SpotImgDTO toDTO() {
		return new SpotImgDTO(id, spotId, url);
	}
}
