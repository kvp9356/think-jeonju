package com.kvp.thinkjeonju.dto;

import java.util.ArrayList;

import com.kvp.thinkjeonju.model.SpotImg;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SpotDTO {
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
	private ArrayList<String> imgUrl;
	
	public void setImgUrl(ArrayList<String> list) {
		imgUrl = list;
	}
}
