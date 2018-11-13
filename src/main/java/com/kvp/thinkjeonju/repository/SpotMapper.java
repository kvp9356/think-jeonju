package com.kvp.thinkjeonju.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kvp.thinkjeonju.dto.LikeToDTO;
import com.kvp.thinkjeonju.dto.SpotDTO;
import com.kvp.thinkjeonju.dto.SpotImgDTO;
import com.kvp.thinkjeonju.model.Spot;
import com.kvp.thinkjeonju.model.SpotImg;

@Mapper
public interface SpotMapper {

	int checkSpotIdDuplicate(String id);

	int addSpot(Spot spot);

	int checkSpotImgDuplicate(String id);

	int addSpotImg(SpotImg spotImg);

	ArrayList<String> findSpotImgUrlById(String id);

	int getLikeCnt(String id);

	int getIsLike(LikeToDTO like);

	int getSpotDataSize(HashMap<String, String> map);

	Spot getSpotDetail(String id);

	List<Spot> findBestSpots();

	ArrayList<Spot> getSpotData(HashMap<String, String> map);

	ArrayList<SpotImg> getSpotImg(String id);

}