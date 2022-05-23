package com.sds.book.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sds.book.domain.model.Borrow;
import com.sds.book.domain.model.IBorrowBook;

//@Repository 를 적어야 스프링 빈 IoC에 빈으로 등록되는데
//JpaRepository 를 extends 하면 생략가능하다.
//JpaRepository 는 CRUD 함수를 들고 있다.
public interface BorrowRepository extends JpaRepository<Borrow, Long> {

	public List<Borrow> findByUserId(String userId);
	
	//컬럼명을 꼭 입력해야 한다. 리턴 오프벡트 변수와 일치
	//리턴객체는 인터페이스로 해야 에러가 안난다.
	@Query(value="SELECT a.borrowId, a.userId, a.bookId, b.title, b.author, a.startdate, a. enddate "+
	"FROM borrow a, book b WHERE a.bookId = b.id and userId=?", nativeQuery = true)
	List<IBorrowBook> selectBorrowBookList(String userId);
	
}
