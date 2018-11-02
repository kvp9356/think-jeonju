package com.kvp.thinkjeonju.repository;

import org.apache.ibatis.annotations.Mapper;

import com.kvp.thinkjeonju.dto.LikeToDTO;
import com.kvp.thinkjeonju.model.Spot;
import com.kvp.thinkjeonju.model.SpotImg;

@Mapper
public interface SpotMapper {

	int checkSpotIdDuplicate(String id);

	int addSpot(Spot spot);

	int checkSpotImgDuplicate(String id);

	int addSpotImg(SpotImg spotImg);

	int getLikeCnt(String id);

	void setSpotLike(LikeToDTO like);

	void cancelSpotLike(LikeToDTO like);

	int getIsLike(LikeToDTO like);

}
