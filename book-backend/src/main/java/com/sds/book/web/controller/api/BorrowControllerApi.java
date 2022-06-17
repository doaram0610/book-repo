package com.sds.book.web.controller.api;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
import com.sds.book.domain.repository.BookRepository;
import com.sds.book.domain.service.BookService;
import com.sds.book.domain.service.BorrowService;
import com.sds.book.domain.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


//도서대출 컨트롤러
@Slf4j
@RequiredArgsConstructor //final 이라고 된 객체의 생성자를 자동으로 만들어준다.
@RestController
@RequestMapping(value = "/api")
public class BorrowControllerApi {

	private final BorrowService borrowService;
	private final BookService bookService;
	private final UserService userService;
	
	
	//나의 대출목록
	@GetMapping("/borrow/{userId}")
	public ResponseEntity<?> findById(@PathVariable String userId){
		return new ResponseEntity<>(borrowService.Select(userId), HttpStatus.OK);
	}	
	
	//대출신청
	@PostMapping("/borrow")
	public ResponseEntity<?> save(@RequestBody Map<String, Object> bodyParam){  //@RequestBody는 json으로 받겠다는 뜻
		
		log.info("bodyParam="+bodyParam);
		
		LocalDateTime startDatetime = LocalDateTime.now();
		LocalDateTime endDatetime = LocalDateTime.now().plusDays(7);
		  
		log.info("startDatetime="+startDatetime);
		log.info("endDatetime= "+endDatetime);
		
		Borrow borrow = new Borrow();
		borrow.setBook(bookService.Select(((Number) bodyParam.get("bookId")).longValue()));
		borrow.setUser(userService.SelectByUserId((String) bodyParam.get("userId")));
		
		//대출기간 오늘 ~ +7일
		borrow.setStartDate(startDatetime);
		borrow.setEndDate(endDatetime);

		return new ResponseEntity<>(borrowService.Save(borrow), HttpStatus.OK);
	}
	
	//반납하기
	@DeleteMapping("/borrow/{userId}")
	public ResponseEntity<?> deleteById(@PathVariable String userId, @RequestBody List<String> borrowIds){	
		log.info("userId: "+userId);
		log.info("borrowIds: "+borrowIds);
		return new ResponseEntity<>(borrowService.Delete(userId, borrowIds), HttpStatus.OK);
	}	
	
	//기간연장
	@PutMapping("/borrow/{userId}")
	public ResponseEntity<?> update(@PathVariable String borrowId, @RequestBody Borrow borrow){
		return new ResponseEntity<>(borrowService.Update(borrowId, borrow), HttpStatus.OK);
	}	

}