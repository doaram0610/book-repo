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

import javax.persistence.EntityManager;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sds.book.domain.model.Book;
import com.sds.book.domain.repository.BookRepository;

import lombok.extern.slf4j.Slf4j;

//통합테스트 (모든 빈들을 똑같이 IoC 올리고 테스트 하는 것)
//통합테스트할때는 @SpringBootTest 를 추가한다.  단위테스트는 @WebMvcTest 
//WebEnvironment.MOCK : 실제톰캣이 아니라 다른 톰캣으로 테스트
//WebEnvironment.RANDOM_PORT : 실제톰캣으로 테스트 

@Slf4j  //log
@Transactional  //@Test annotation과 함께 설정된 @Transactional은 항상 rollback, 즉 함수 종료되면 롤백된다.
//@Transactional의 디폴트 값은 롤백 true 이기 때문에 commit 하고 싶으면 @Rollback(value = false)  게 설정하면 된다.
@AutoConfigureMockMvc  // 단위테스트시에는  @WebMvcTest 에 들어있어서 안해줘두 된다.
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class BookControllerIntegreTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired	//통합테스트에서는 모든 빈이 IoC 되어 있으니 사용가능하다
	private BookRepository bookRepository;
	
	@Autowired
	private EntityManager entityManager;
	
	
	//아래 각각의 테스트 메소드를 한번에 수행하면 index 숫자값이 초기화가 되지 않고 수행되서
	//인덱스 숫자 값이 1부터 시작을 못하는 경우가 있다.
	//그래서 각 메소드 수행 전에 자동증가숫자값을 1로 초기화 해준다. 그러면 전체 메소드를 한번에 수행해도 인덱스 숫자오류가 없다. 	
	@BeforeEach //모든 메소드 실행 전에 수행된다.
	public void init() {
		
		//테스트데이터 생성
//		List<Book> books = new ArrayList<>();
//		books.add(new Book(1L, "부트 따라하기", "코스"));
//		books.add(new Book(2L, "리엑트 따라하기", "코스"));	
//		books.add(new Book(3L, "Junit 따라하기", "코스"));	
//		bookRepository.saveAll(books);		
		
		//자동증가번호 초기화
		entityManager.createNativeQuery("ALTER TABLE book ALTER COLUMN id RESTART WITH 1").executeUpdate();
	}
	
	@AfterEach //모든 메소드 실행 후에 수행된다.
	public void end() {
		
		//테스트 데이터 삭제
//		bookRepository.deleteAll();
	}	
	
	//junit 테스트는 기본적으로 각 메소드, 그리고 함수가 종료될때 마다 롤백이 된다.
	//그러니까 상단에 @Transactional 를 선언하면 메소드종료시에는 롤백이 안되고 함수종료시에 롤백되며
	//아예 @Transactional을 안쓰면 커밋된다.
	
	@Test
	public void save_test() throws Exception {
		log.info("save test start ===================================");
		
		//given (테스트를 하기 위한 준비)
		Book book = new Book(null, "스프링 따라하기", "코스");
		String content = new ObjectMapper().writeValueAsString(book);  //json 데이터로 리턴
		
		//stub - 동작지정
		//when(bookService.Save(book)).thenReturn(new Book(1L, "스프링 따라하기", "코스"));  //지금은 컨트롤러만 테스트 해야하니까 서비스랑레퍼지토리의 값을 미리 예상해서 넣어준다.
		
		//when (테스트 실행)
		ResultActions resultActions = mockMvc.perform(post("/book")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(content)
				.accept(MediaType.APPLICATION_JSON_UTF8));
		
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
		//통합테스트는 직접 서버에 붙어서 하기 때문에 when에 db에 있을 값을 입력해 줄 필요가 없다
		List<Book> books = new ArrayList<>();
		books.add(new Book(1L, "부트 따라하기", "코스"));
		books.add(new Book(2L, "리엑트 따라하기", "코스"));	
		books.add(new Book(3L, "Junit 따라하기", "코스"));	
		bookRepository.saveAll(books);		
		
		//stub - 동작지정
		//when(bookService.ListAll()).thenReturn(books);  //지금은 컨트롤러만 테스트 해야하니까 서비스랑레퍼지토리의 값을 미리 예상해서 넣어준다.
		
		//when (테스트 실행)
		ResultActions resultActions = mockMvc.perform(get("/book")
				.accept(MediaType.APPLICATION_JSON_UTF8));
		
		//then (검증) : 기대하는 결과값을 아래에 나열해준다.
		resultActions
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", Matchers.hasSize(3))) //jsonPath 찾아보면 검색할 키워드를 입력하는 문법을 알수 있따
			.andExpect(jsonPath("$.[0].title").value("부트 따라하기"))
			.andDo(MockMvcResultHandlers.print());
	}	
	
	
	@Test
	public void findById_test() throws Exception {
		log.info("save test start ===================================");
		
		//given (테스트를 하기 위한 준비)
		Long id = 2L;		
		List<Book> books = new ArrayList<>();		
		books.add(new Book(null, "부트 따라하기", "코스"));
		books.add(new Book(null, "리엑트 따라하기", "코스"));
		books.add(new Book(null, "Junit 따라하기", "코스"));
		bookRepository.saveAll(books);
		
		//stub - 동작지정
//		when(bookService.Select(id)).thenReturn(book);  //지금은 컨트롤러만 테스트 해야하니까 서비스랑레퍼지토리의 값을 미리 예상해서 넣어준다.  요건 단위테스트에서만 사용한다.
		
		//when (테스트 실행)
		ResultActions resultActions = mockMvc.perform(get("/book/{id}", id)
				.accept(MediaType.APPLICATION_JSON_UTF8));
		
		//then (검증) : 기대하는 결과값을 아래에 나열해준다.
		resultActions
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.title").value("리엑트 따라하기"))
			.andDo(MockMvcResultHandlers.print());
	}	
	
	
	@Test
	public void update_test() throws Exception {
		log.info("save test start ===================================");
		
		//given (테스트를 하기 위한 준비)
		List<Book> books = new ArrayList<>();		
		books.add(new Book(null, "부트 따라하기", "코스"));
		books.add(new Book(null, "리엑트 따라하기", "코스"));
		books.add(new Book(null, "Junit 따라하기", "코스"));
		bookRepository.saveAll(books);
		
		Long id = 3L;
		Book book = new Book(null, "C++ 따라하기", "코스");
		String content = new ObjectMapper().writeValueAsString(book);  //테스트할 데이터를 json 데이터로 리턴

		
		//when (테스트 실행)
		ResultActions resultActions = mockMvc.perform(put("/book/{id}", id)
				.contentType(MediaType.APPLICATION_JSON_UTF8)  //내가 던져주는 데이터의 타입
				.content(content) //내가 주는 데이터
				.accept(MediaType.APPLICATION_JSON_UTF8));  //내가 기대하는 데이터의 타입
		
		//then (검증)
		resultActions
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(3L)) 
			.andExpect(jsonPath("$.title").value("C++ 따라하기")) //jsonPath 찾아보면 검색할 키워드를 입력하는 문법을 알수 있따
			.andDo(MockMvcResultHandlers.print());
	}	
	
	
	@Test
	public void delete_test() throws Exception {
		log.info("delete test start ===================================");
		
		
		//given (테스트를 하기 위한 준비)
		List<Book> books = new ArrayList<>();		
		books.add(new Book(null, "부트 따라하기", "코스"));
		books.add(new Book(null, "리엑트 따라하기", "코스"));
		books.add(new Book(null, "Junit 따라하기", "코스"));
		bookRepository.saveAll(books);
		
		Long id = 1L;
		
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
