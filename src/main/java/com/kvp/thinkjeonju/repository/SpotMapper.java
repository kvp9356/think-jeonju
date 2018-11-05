package com.kvp.thinkjeonju.repository;

import com.kvp.thinkjeonju.model.SpotImg;
import org.apache.ibatis.annotations.Mapper;

import com.kvp.thinkjeonju.dto.LikeToDTO;
import com.kvp.thinkjeonju.model.Spot;
import com.kvp.thinkjeonju.model.SpotImg;

import java.util.ArrayList;

@Mapper
public interface SpotMapper {

	int checkSpotIdDuplicate(String id);

	int addSpot(Spot spot);

	int checkSpotImgDuplicate(String id);

	int addSpotImg(SpotImg spotImg);

	ArrayList<String> findSpotImgUrlById(String id);

	int getLikeCnt(String id);

	void setSpotLike(LikeToDTO like);

	void cancelSpotLike(LikeToDTO like);

	int getIsLike(LikeToDTO like);

}