package com.kvp.thinkjeonju.repository;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;

import com.kvp.thinkjeonju.dto.SpotDTO;
import com.kvp.thinkjeonju.dto.SpotImgDTO;
import com.kvp.thinkjeonju.model.Spot;

@Mapper
public interface SearchMapper {

	int checkSpotIdDuplicate(String id);

	int addSpot(SpotDTO spotDTO);

	int checkSpotImgDuplicate(String id);

	int addSpotImg(SpotImgDTO spotImgDTO);

	ArrayList<SpotDTO> searchSpot(String dataValue);

	ArrayList<String> searchSpotImg(String dataValue);

}
