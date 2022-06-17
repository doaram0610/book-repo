package com.sds.book.domain.repository;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.sds.book.domain.model.Book;
import com.sds.book.domain.model.Borrow;
import com.sds.book.domain.model.User;

import lombok.extern.slf4j.Slf4j;

//단위테스트(DB관련된 빈이 Ioc에 등록되면 됨)
//@Transactional  //아래 @DataJpaTest에 포함되어 있다
//@AutoConfigureTestDatabase(replace = Replace.ANY) //가짜 디비로 테스트, Replace.NONE는 진짜 / 아래 @DataJpaTest에 포함되어 있다

@Slf4j
@DataJpaTest  //Repository 들을 다 IoC에 등록해두니까 Mock로 Repository 를 선언할 필요 없다.
public class BorrowRepositoryUnitTest {
	
	@Autowired
	private BorrowRepository borrowRepository;
	
	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
	private UserRepository userRepository;	
	
	@Disabled("#1 이건 확인했으니까 비활성화")	
	@DisplayName("도서 대출하기 테스트")
	@Test  //여기선 레퍼지토리가 가짜객체이고 서비스에선 로직이 있어야 테스트할게 있으니 알고만 있자
	@Order(2)
	public void test_save() throws Exception {
		
		//given (테스트를 하기 위한 준비)
		List<Borrow> Borrows = new ArrayList<>();
		Borrow borrow1 = new Borrow(null, null, null, new Book(null, "책제목1", "", 5, 0), new User(null, "admin", "1", "1","1", "1", "1", null));
		Borrow borrow2 = new Borrow(null, null, null, new Book(null, "책제목2", "", 5, 0), new User(null, "admin", "1", "1","1", "1", "1", null));
		Borrows.add(borrow1);
		Borrows.add(borrow2);
		
		//when (테스트 실행)
		List<Borrow> borrowEntityList = borrowRepository.saveAll(Borrows);
		
		//then (검증)
		assertEquals(2, borrowEntityList.size()); //두개의 파라미터값을 비교해서 같으면 성공!
		assertEquals("책제목1", borrowEntityList.get(0).getBook().getTitle());
		assertEquals("책제목2", borrowEntityList.get(1).getBook().getTitle());
	}	
	
	@DisplayName("도서 반납하기 테스트")	
	@Test  //여기선 레퍼지토리가 가짜객체이고 서비스에선 로직이 있어야 테스트할게 있으니 알고만 있자
	@Order(1)
	public void test_delete() throws Exception {
		
		//given (준비 : 테스트를 하기 위한 준비)
		//Borrow 테이블은 User, Book 와 FK 로 묶여 있어서 두 테이블에 데이터가 없어서 테스트할때 에러가 났다.
		//그 에러를 해결하기 위해 (cascade = CascadeType.ALL) 를 추가했는데, 사실 추가하면 안된다.
		//Borrow에 데이터가 들어간다고 User, Book에 데이터가 같이 들어가면 안되기 때문이다.
		Book book1 = bookRepository.save(new Book(null, "책제;목1", "", 5, 0));
		Book book2 = bookRepository.save(new Book(null, "책제;목2", "", 5, 0));
		User user = userRepository.save(new User(null, "admin", "1", "1","1", "1", "1", null));
		
		List<Borrow> Borrows = new ArrayList<>();
		Borrow borrow1 = new Borrow(UUID.randomUUID().toString().replace("-", ""), null, null, book1, user);
		Borrow borrow2 = new Borrow(UUID.randomUUID().toString().replace("-", ""), null, null, book2, user);
		Borrows.add(borrow1);
		Borrows.add(borrow2);
		List<Borrow> borrowEntityList = borrowRepository.saveAll(Borrows);
		
		List<String> borrowIds = new ArrayList<>();		
		for(Borrow borrow : borrowEntityList) {
			log.info("borrow:"+borrow);
			borrowIds.add(borrow.getBorrowId());
		}
		
		//when (실행 : 테스트 실행)
		//JPA에서 지원하는 delete를 사용하면 하나씩 삭제되고 내가 만든 쿼리를 사용하면 한번에 수행
		borrowRepository.deleteAllById(borrowIds);		
//		borrowRepository.deleteAllByUserIdAndBorrowIdInQuery("admin", borrowIds);
//		borrowRepository.findByBorrowIdIn(borrowIds);
		
		//then (검증)
//		assertThatThrownBy(() -> borrowRepository.findByBorrowIdIn(borrowIds)).isInstanceOf(IllegalArgumentException.class);//요건 안되네..  		
		assertEquals(0, borrowRepository.findByBorrowIdIn(borrowIds).size()); //기대하는 값, 실제결과값
	}

}
