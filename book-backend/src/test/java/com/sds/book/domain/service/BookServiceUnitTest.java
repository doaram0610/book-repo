package com.sds.book.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sds.book.domain.model.Book;
import com.sds.book.domain.repository.BookRepository;

import lombok.extern.slf4j.Slf4j;

//단위테스트(Service와 관련된 애들만 메모리에 띄우면 됨)
//그러니까 repository 를 제외하고 테스트 해야한다.
//그래서 mock(가짜환경)을 이용해서 테스트한다.

@Slf4j
@ExtendWith(MockitoExtension.class)  
public class BookServiceUnitTest {
	
	@InjectMocks	//가짜객체, BookService 가 만들어 질때 BookServiceUnitTest 에 @Mock 로 등록된 모든 애들을 주입받는다.
	private BookService bookService; 
	
	@Mock  //가짜객체
	private BookRepository bookRepository;
	
	//BDDMocito 패턴
	@Test  //여기선 레퍼지토리가 가짜객체이고 서비스에선 로직이 있어야 테스트할게 있으니 알고만 있자
	public void search_test() throws Exception {
		
		//given (테스트를 하기 위한 준비)
		Book book = new Book(null, "스프링 따라하기", "코스", 5, 0);
		String content = new ObjectMapper().writeValueAsString(book);  //json 데이터로 리턴
		List<Book> books = new ArrayList<>();
		books.add(book);
		bookService.Save(book);
		
		//stub - 동작지정
		when(bookService.search("스프링","스프링")).thenReturn(books);  //지금은 컨트롤러만 테스트 해야하니까 서비스랑레퍼지토리의 값을 미리 예상해서 넣어준다.
		
		//when (테스트 실행)
		List<Book> booklist = bookService.search("스프링","스프링");
		
		//then (검증)
		assertEquals(booklist, books);
	}	
	
	//BDDMocito 패턴
	@Test  //여기선 레퍼지토리가 가짜객체이고 서비스에선 로직이 있어야 테스트할게 있으니 알고만 있자
	public void save_test() throws Exception {
		
		//given (테스트를 하기 위한 준비)
		Book book = new Book(null, "스프링 따라하기", "코스", 5, 0);
		String content = new ObjectMapper().writeValueAsString(book);  //json 데이터로 리턴
		
		//stub - 동작지정
		when(bookService.Save(book)).thenReturn(book);  //지금은 컨트롤러만 테스트 해야하니까 서비스랑레퍼지토리의 값을 미리 예상해서 넣어준다.
		
		//when (테스트 실행)
		Book bookEntity = bookService.Save(book);
		
		//then (검증)
		assertEquals(bookEntity, book);
	}	
}
