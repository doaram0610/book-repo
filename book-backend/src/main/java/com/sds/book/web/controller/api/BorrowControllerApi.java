package com.sds.book.web.controller.api;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sds.book.domain.model.Book;
import com.sds.book.domain.model.Borrow;
import com.sds.book.domain.service.BookService;
import com.sds.book.domain.service.BorrowkService;

import jdk.internal.org.jline.utils.Log;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


//도서대출 컨트롤러
@Slf4j
@RequiredArgsConstructor //final 이라고 된 객체의 생성자를 자동으로 만들어준다.
@RestController
@RequestMapping(value = "/api")
public class BorrowControllerApi {

	private final BorrowkService borrowkService;
	private final BookService bookService;
	
	//대출하기
	@PostMapping("/borrow")
	public ResponseEntity<?> save(@RequestBody Borrow borrow){  //@RequestBody는 json으로 받겠다는 뜻
		
		log.info("borrow="+borrow);
		
		LocalDateTime startDatetime = LocalDateTime.now();
		LocalDateTime endDatetime = LocalDateTime.now().plusDays(7);
		  
		log.info("startDatetime="+startDatetime);
		log.info("endDatetime= "+endDatetime);
		
		//대출기간 오늘 ~ +7일
		borrow.setStartDate(startDatetime);
		borrow.setEndDate(endDatetime);

		//대출권수 업데이트
		int result = bookService.UpdateBorrow(borrow.getBookId());
		
		return new ResponseEntity<>(borrowkService.Save(borrow), HttpStatus.OK);
	}
	
	//나의 대출목록
	@GetMapping("/borrow/{userId}")
	public ResponseEntity<?> findById(@PathVariable String userId){
		return new ResponseEntity<>(borrowkService.Select(userId), HttpStatus.OK);
	}	

	//기간연장
	@PutMapping("/borrow/{userId}")
	public ResponseEntity<?> update(@PathVariable Long borrowId, @RequestBody Borrow borrow){
		return new ResponseEntity<>(borrowkService.Update(borrowId, borrow), HttpStatus.OK);
	}	
	
	//반납하기
	@DeleteMapping("/borrow/{borrowId}")
	public ResponseEntity<?> deleteById(@PathVariable Long borrowId){
		return new ResponseEntity<>(borrowkService.Delete(borrowId), HttpStatus.OK);
	}	
}