package com.kvp.thinkjeonju.repository;

import com.kvp.thinkjeonju.model.SpotImg;
import org.apache.ibatis.annotations.Mapper;

import com.kvp.thinkjeonju.dto.SpotDTO;
import com.kvp.thinkjeonju.dto.SpotImgDTO;

import java.util.ArrayList;

@Mapper
public interface SpotMapper {

	int checkSpotIdDuplicate(String id);

	int addSpot(SpotDTO spotDTO);

	int checkSpotImgDuplicate(String id);

	int addSpotImg(SpotImgDTO spotImgDTO);

	ArrayList<String> findSpotImgUrlById(String id);
}
