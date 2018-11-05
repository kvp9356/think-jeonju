package com.kvp.thinkjeonju.model;

import java.util.ArrayList;

import org.apache.ibatis.type.Alias;

import com.kvp.thinkjeonju.dto.SpotDTO;
import com.kvp.thinkjeonju.dto.SpotDTO.Category;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Alias("spot")
@NoArgsConstructor
@AllArgsConstructor
public class Spot {
	private String id;
	private String name;
	private String content;
	private String addr;
	private String addrDtl;
	private Double posX;
	private Double posY;
	private String url;
	private String tel;
	private int fileCnt;
	private int category;
	
	public static Spot from(SpotDTO spotDTO) {
		return new Spot(spotDTO.getId(), spotDTO.getName(), spotDTO.getContent(), 
				spotDTO.getAddr(), spotDTO.getAddrDtl(),
				spotDTO.getPosX(), spotDTO.getPosY(), spotDTO.getUrl(), spotDTO.getTel(), spotDTO.getFileCnt(), Category.getByName(spotDTO.getCategory()).getCategoryNum());
	}
	
	public SpotDTO toDTO() {
		return new SpotDTO(id, name, content, addr, addrDtl, posX, posY, url, tel, fileCnt, new ArrayList<String>(), Category.getById(category).getCategoryName(), 0, false);
	}
}
