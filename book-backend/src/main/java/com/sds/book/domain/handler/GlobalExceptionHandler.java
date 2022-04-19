package com.sds.book.domain.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice	//패키지 어디서는 호출할수 있따.
@RestController
public class GlobalExceptionHandler {

	//컨트롤러하위의 모든 Exception 발생 시 아래의 함수가 호출되어 값을 리턴시킨다.
	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<?> handleArgumentExeption(Exception e){
		log.info("GlobalExceptionHandler : "+e);
		return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
	}
}
