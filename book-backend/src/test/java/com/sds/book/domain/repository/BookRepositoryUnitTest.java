package com.sds.book.domain.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sds.book.domain.model.Book;

//단위테스트(DB관련된 빈이 Ioc에 등록되면 됨)

@Transactional
@AutoConfigureTestDatabase(replace = Replace.ANY) //가짜 디비로 테스트, Replace.NONE는 진짜
@DataJpaTest  //Repository 들을 다 IoC에 등록해두니까 Mock로 Repository 를 선언할 필요 없다.
public class BookRepositoryUnitTest {

	@Autowired
	private BookRepository bookRepository;
	
	@Test  //여기선 레퍼지토리가 가짜객체이고 서비스에선 로직이 있어야 테스트할게 있으니 알고만 있자
	public void save_test() throws Exception {
		
		//given (테스트를 하기 위한 준비)
		Book book = new Book(null, "스프링 따라하기", "코스", 5, 0);

		//when (테스트 실행)
		Book bookEntity = bookRepository.save(book);
		
		//then (검증)
		assertEquals("스프링 따라하기", bookEntity.getTitle());
	}		
}
