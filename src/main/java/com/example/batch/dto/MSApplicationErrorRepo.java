package com.example.batch.dto;

import java.util.HashMap;
import java.util.Map;

import com.example.batch.util.ErrorCodes;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MSApplicationErrorRepo {

	private Map<Integer, Enum<ErrorCodes>> errorMap = new HashMap<>();
}
