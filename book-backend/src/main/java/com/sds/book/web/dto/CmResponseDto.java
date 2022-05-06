package com.sds.book.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CmResponseDto<T> {
	private int code;
	private String msg;
	private T data;
}
