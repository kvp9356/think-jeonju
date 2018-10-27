package com.kvp.thinkjeonju.model;

import org.apache.ibatis.type.Alias;

import com.kvp.thinkjeonju.dto.SpotDTO;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Alias("spot")
@NoArgsConstructor
@AllArgsConstructor
public class Spot {
	private String id;
	private String name;
	private String content;
	private String zipCode;
	private String addr;
	private String addrDtl;
	private Double posX;
	private Double posY;
	private String url;
	private String tel;
	private int fileCnt;
	
	public static Spot from(SpotDTO spotDTO) {
		return new Spot(spotDTO.getId(), spotDTO.getName(), spotDTO.getContent(), 
				spotDTO.getZipCode(), spotDTO.getAddr(), spotDTO.getAddrDtl(),
				spotDTO.getPosX(), spotDTO.getPosY(), spotDTO.getUrl(), spotDTO.getTel(), spotDTO.getFileCnt());
	}
	
	public SpotDTO toDTO() {
		return new SpotDTO(id, name, content, zipCode, addr, addrDtl, posX, posY, url, tel, fileCnt, null);
	}
}
