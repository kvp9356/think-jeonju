package com.kvp.thinkjeonju.repository;

import org.apache.ibatis.annotations.Mapper;

import com.kvp.thinkjeonju.dto.SpotDTO;
import com.kvp.thinkjeonju.dto.SpotImgDTO;

@Mapper
public interface SpotMapper {

	int checkSpotIdDuplicate(String id);

	int addSpot(SpotDTO spotDTO);

	int checkSpotImgDuplicate(String id);

	int addSpotImg(SpotImgDTO spotImgDTO);

}
