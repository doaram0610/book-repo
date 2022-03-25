package com.sds.book.web.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sds.book.domain.model.Book;
import com.sds.book.domain.service.BookService;

import lombok.extern.slf4j.Slf4j;




//전체 실행 단축키 : 컨트롤 + F11 

//단위테스트(컨트롤러만 테스트, 컨트롤러 관련 로직만 띄우기, Controller, Filter, ControllerAdvice )
//JUNIT4 와 JUNIT5가 있는데
//JUNIT4 는 @RunWith(SpringRunner.class)를 붙여야 하고  스프링으로 확장하는 어노테이션
//JUNIT5 는  @WebMvcTest 만 붙이면 된다 왜냐면 @ExtendWith(SpringExtension.class) 가 들어가 있다

@Slf4j  //log
@WebMvcTest
public class BookControllerUnitTest {

	@Autowired  //이렇게 쓸수 있는 이유는 @WebMvcTest에 Mock 가 걸려 있기 때문이다.
	private MockMvc mockMvc;
	
	@MockBean  //IoC환경에 빈이 등록됨, 가짜 BookService 를 띄우는거나 에러만 안나게 (통합테스트는 필요없다 단위테스트만 필요)
	private BookService bookService;
	
	//BDDMocito 패턴
	@Test
	public void save_test() throws Exception {
		log.info("save test start ===================================");
		
		//given (테스트를 하기 위한 준비)
		Book book = new Book(null, "스프링 따라하기", "코스");
		String content = new ObjectMapper().writeValueAsString(book);  //json 데이터로 리턴
		
		//stub - 동작지정
		when(bookService.Save(book)).thenReturn(new Book(1L, "스프링 따라하기", "코스"));  //지금은 컨트롤러만 테스트 해야하니까 서비스랑레퍼지토리의 값을 미리 예상해서 넣어준다.
		
		//when (테스트 실행)
		ResultActions resultActions = mockMvc.perform(post("/book")
				.contentType(MediaType.APPLICATION_JSON_UTF8)  //내가 던져주는 데이터의 타입
				.content(content) //내가 주는 데이터
				.accept(MediaType.APPLICATION_JSON_UTF8));  //내가 기대하는 데이터의 타입
		
		//then (검증)
		resultActions
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.title").value("스프링 따라하기")) //jsonPath 찾아보면 검색할 키워드를 입력하는 문법을 알수 있따
			.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void findAll_test() throws Exception {
		log.info("save test start ===================================");
		
		//given (테스트를 하기 위한 준비)
		List<Book> books = new ArrayList<>();
		books.add(new Book(1L, "부트 따라하기", "코스"));
		books.add(new Book(2L, "리엑트 따라하기", "코스"));

		//stub - 동작지정
		when(bookService.ListAll()).thenReturn(books);  //지금은 컨트롤러만 테스트 해야하니까 서비스랑레퍼지토리의 값을 미리 예상해서 넣어준다.
		
		//when (테스트 실행)
		ResultActions resultActions = mockMvc.perform(get("/book")
				.accept(MediaType.APPLICATION_JSON_UTF8));
		
		//then (검증) : 기대하는 결과값을 아래에 나열해준다.
		resultActions
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", Matchers.hasSize(4))) //jsonPath 찾아보면 검색할 키워드를 입력하는 문법을 알수 있따
			.andExpect(jsonPath("$.[0].title").value("부트 따라하기"))
			.andDo(MockMvcResultHandlers.print());
	}	
	
	
	@Test
	public void findById_test() throws Exception {
		log.info("save test start ===================================");
		
		//given (테스트를 하기 위한 준비)
		Long id = 1L;
		Book book = new Book(1L, "자바공부하기", "쌀");
		
		//stub - 동작지정
		when(bookService.Select(id)).thenReturn(book);  //지금은 컨트롤러만 테스트 해야하니까 서비스랑레퍼지토리의 값을 미리 예상해서 넣어준다.
		
		//when (테스트 실행)
		ResultActions resultActions = mockMvc.perform(get("/book/{id}", id)
				.accept(MediaType.APPLICATION_JSON_UTF8));
		
		//then (검증) : 기대하는 결과값을 아래에 나열해준다.
		resultActions
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", Matchers.hasSize(2))) //jsonPath 찾아보면 검색할 키워드를 입력하는 문법을 알수 있따
			.andExpect(jsonPath("$.title").value("자바공부하기"))
			.andDo(MockMvcResultHandlers.print());
	}	
	

	@Test
	public void update_test() throws Exception {
		log.info("save test start ===================================");
		
		
		//given (테스트를 하기 위한 준비)
		Long id = 1L;
		Book book = new Book(null, "C++ 따라하기", "코스");
		String content = new ObjectMapper().writeValueAsString(book);  //테스트할 데이터를 json 데이터로 리턴
		
		//stub - 동작지정
		when(bookService.Update(id, book)).thenReturn(new Book(1L, "C++ 따라하기", "코스"));  //지금은 컨트롤러만 테스트 해야하니까 서비스랑레퍼지토리의 값을 미리 예상해서 넣어준다.
		
		//when (테스트 실행)
		ResultActions resultActions = mockMvc.perform(put("/book/{id}", id)
				.contentType(MediaType.APPLICATION_JSON_UTF8)  //내가 던져주는 데이터의 타입
				.content(content) //내가 주는 데이터
				.accept(MediaType.APPLICATION_JSON_UTF8));  //내가 기대하는 데이터의 타입
		
		//then (검증)
		resultActions
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.title").value("C++ 따라하기")) //jsonPath 찾아보면 검색할 키워드를 입력하는 문법을 알수 있따
			.andDo(MockMvcResultHandlers.print());
	}	
	
	
	@Test
	public void delete_test() throws Exception {
		log.info("save test start ===================================");
		
		
		//given (테스트를 하기 위한 준비)
		Long id = 1L;
		
		//stub - 동작지정
		when(bookService.Delete(id)).thenReturn("ok");  //지금은 컨트롤러만 테스트 해야하니까 서비스랑레퍼지토리의 값을 미리 예상해서 넣어준다.
		
		//when (테스트 실행)
		ResultActions resultActions = mockMvc.perform(delete("/book/{id}", id)
				.accept(MediaType.TEXT_PLAIN));  //내가 기대하는 데이터의 타입
		
		//then (검증)
		resultActions
			.andExpect(status().isOk())
			.andDo(MockMvcResultHandlers.print());
		
		MvcResult requestResult = resultActions.andReturn();
		String result = requestResult.getResponse().getContentAsString();
		
		assertEquals("ok", result);
	}	
	
}
