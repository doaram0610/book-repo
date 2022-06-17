package com.sds.book.web.controller.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sds.book.domain.model.Book;
import com.sds.book.domain.service.BookService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor //final 이라고 된 객체의 생성자를 자동으로 만들어준다.
@RestController
@RequestMapping(value = "/api")
public class BookControllerApi {

	private final BookService bookService;
	
	//리턴타입을 ResponseEntity 로 사용하면 리턴코드와 리턴데이터를 함께 넘겨줄수 있으니 이것을 사용해라
	//security (라이브러리를 적용하면) - CORS정책을 갖고 있다.(시큐리티가 CORS를 해제해줘야 한다.)
	//@CrossOrigin(origins = "*", allowedHeaders = "*") //스프링시큐리티 로그인 사용안할때 사용할수 있는 방법이다. 컨트롤러에서 설정 //외부에서오는 자바스크립트 요청을 허용해준다.	
	//스프링시큐리티 로그인을 사용한다면 별도의 설정파일을 만들어서 필터로 등록해줘야 한다.(예)addFilter(corsFilter))
	@PostMapping("/book")
	public ResponseEntity<?> save(@RequestBody Book book){  //@RequestBody는 json으로 받겠다는 뜻
		return new ResponseEntity<>(bookService.Save(book), HttpStatus.OK);
	}
	
	//도서전체
	@GetMapping("/book")
	public ResponseEntity<?> findAll(){
		return new ResponseEntity<>(bookService.ListAll(), HttpStatus.OK);
	}
	
	//도서검색
	@PostMapping("/bookSearch")
	public ResponseEntity<?> search(@RequestBody String searchVal){		
		log.info("searchVal = ["+searchVal+"]");
		searchVal = searchVal.replaceAll("\"", "");	//왜 더블코테이션이 들어가있지??? 이상하다
		return new ResponseEntity<>(bookService.search(searchVal, searchVal), HttpStatus.OK);	
	}	
	
	//도서상세
	@GetMapping("/book/{bookId}")
	public ResponseEntity<?> findById(@PathVariable Long bookId){
		return new ResponseEntity<>(bookService.Select(bookId), HttpStatus.OK);
	}	

	//도서수정
	@PutMapping("/book/{bookId}")
	public ResponseEntity<?> update(@PathVariable Long bookId, @RequestBody Book book){
		return new ResponseEntity<>(bookService.Update(bookId, book), HttpStatus.OK);
	}	
	
	//도서삭제
	//제네릭 ? 란 : JDK 1.5부터 도입된 제네릭을 사용하면 컴파일 시에 미리 타입이 정해지므로, 타입 검사나 타입 변환과 같은 번거로운 작업을 생략할 수 있게 됩니다.
	//<?> : 타입 변수에 모든 타입을 사용할 수 있음
	@DeleteMapping("/book/{bookId}")
	public ResponseEntity<?> deleteById(@PathVariable Long bookId){
		return new ResponseEntity<>(bookService.Delete(bookId), HttpStatus.OK);
	}	
}