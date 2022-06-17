package com.sds.book.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.sds.book.domain.model.Book;

import lombok.extern.slf4j.Slf4j;

//단위테스트(DB관련된 빈이 Ioc에 등록되면 됨)

@Slf4j
//@AutoConfigureTestDatabase(replace = Replace.ANY) //가짜 디비로 테스트, Replace.NONE 는 진짜 / 요거 @DataJpaTest에 다 들어있다
@DataJpaTest  //@Transactional을 기본적으로 내장하고 있으므로, 매 테스트 코드가 종료되면 자동으로 DB가 롤백된다
public class BookRepositoryUnitTest {

	@Autowired
	private BookRepository bookRepository;

	@DisplayName("도서검색 테스트")
	@Test  //여기선 레퍼지토리가 가짜객체이고 서비스에선 로직이 있어야 테스트할게 있으니 알고만 있자
	public void test_search() throws Exception {
		
		log.info("BookRepositoryUnitTest test_search Start !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		
		//given (테스트를 하기 위한 준비)
		bookRepository.save(new Book(null, "스프링 따라하기", "코스", 5, 0));
		
		//when (테스트 실행)
		List<Book> books = bookRepository.findByTitleContainingOrAuthorContaining("스프링", "스프링");
		
		//then (검증)
		assertEquals(1, books.size());	//기대하는 값, 실제결과값        
	}	
	
	@DisplayName("도서가 DB에 저장이 잘 되는지 테스트")
	@Test  //여기선 레퍼지토리가 가짜객체이고 서비스에선 로직이 있어야 테스트할게 있으니 알고만 있자
	public void test_save() throws Exception {
		
		log.info("BookRepositoryUnitTest test_save Start !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		
		//given (테스트를 하기 위한 준비)
		Book book = new Book(null, "스프링 따라하기", "코스", 5, 0);

		//when (테스트 실행)
		Book bookEntity = bookRepository.save(book);
		
		//then (검증)
		assertThat(book).isSameAs(bookEntity);
		assertThat(book.getBookId()).isNotNull();
		assertEquals(book, bookEntity);
		assertEquals("스프링 따라하기", bookEntity.getTitle()); //두개의 파라미터값을 비교해서 같으면 성공!
		assertEquals(1, bookRepository.count());	//기대하는 값, 실제결과값        
	}	
	
	@DisplayName("도서가 삭제가 잘 되는지 테스트")
	@Test  //여기선 레퍼지토리가 가짜객체이고 서비스에선 로직이 있어야 테스트할게 있으니 알고만 있자
	public void test_delete() throws Exception {
		
		log.info("BookRepositoryUnitTest test_delete Start !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		
		//given (테스트를 하기 위한 준비)
		Book book = new Book(2L, "삭제테스트", "홍길동", 4, 0);
		Long bookId = bookRepository.save(book).getBookId();
		
		//when (테스트 실행)
		bookRepository.deleteById(bookId);

		//then (검증)
		assertThat(bookRepository.findById(bookId)).isEmpty();
	}		
}
