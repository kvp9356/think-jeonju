package com.kvp.thinkjeonju.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LikeToDTO {

	private String memberId;
	private String relatedId;
	private char isSpot;

}
