package com.kvp.thinkjeonju.dto;

import java.util.ArrayList;

import com.kvp.thinkjeonju.exception.spot.CategoryNotFoundException;

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
	private String addr;
	private String addrDtl;
	private Double posX;
	private Double posY;
	private String url;
	private String tel;
	private long fileCnt;
	private ArrayList<String> imgUrl;
	private String category;
	private int likeCnt;
	private Boolean isLike;

	public void setImgUrl(ArrayList<String> imgUrl) {
		this.imgUrl = imgUrl;
	}
	
	public void setLikeCnt(int likeCnt) {
		this.likeCnt = likeCnt;
	}
	
	public void setIsLike(Boolean isLike) {
		this.isLike = isLike;
	}
	
	@Getter
	public enum Category {
		All(99, "통합검색"),
		CulturalSpace(0, "문화공간"),
		CulturalExperience(1, "문화체험"),
		OutdoorSpace(2, "야외여행지"),
		StreetTour(3, "거리투어"),
		CulturalProperty(4, "문화재");
		
		private int categoryNum;
		private String categoryName;
		
		Category(int categoryNum, String categoryName) {
			this.categoryNum = categoryNum;
			this.categoryName = categoryName;
		}
		
		public static Category getById(int id) {
			for(Category category : Category.values()) {
				if(category.getCategoryNum() == id) {
					return category;
				}
			}
			throw new CategoryNotFoundException();
		}

		public static Category getByName(String name) {
			for(Category category : Category.values()) {
				if(category.getCategoryName().equals(name)) {
					return category;
				} 
			}
			throw new CategoryNotFoundException();
		}
	}
}
