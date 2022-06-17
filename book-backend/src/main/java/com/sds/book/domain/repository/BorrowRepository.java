package com.sds.book.domain.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.sds.book.domain.model.Borrow;
import com.sds.book.domain.model.IBorrowBook;

//@Repository 를 적어야 스프링 빈 IoC에 빈으로 등록되는데
//JpaRepository 를 extends 하면 @Repository가 생략가능하다.
//JpaRepository 는 CRUD 함수를 들고 있다.
public interface BorrowRepository extends JpaRepository<Borrow, String> {

	//JPA규칙대로 생성한 메소드
	public List<Borrow> findByUser_Id(String user_Id);
	
	//컬럼명을 꼭 입력해야 한다. 리턴 오프벡트 변수와 일치
	//리턴객체는 인터페이스로 해야 에러가 안난다.
	@Query(value="SELECT a.borrowId, a.user_Id, b.bookId, b.title, b.author, a.startdate, a. enddate "+
	"FROM borrow a, book b WHERE a.book_bookId = b.bookId and a.user_Id=?", nativeQuery = true)
	List<IBorrowBook> selectBorrowBookList(String user_Id);
	
	//이거 왜 에러나지??
	@Modifying(clearAutomatically = true)
	@Query(nativeQuery = true,  value="delete from borrow where user_Id = :userId and borrowId in :borrowIds")
	void deleteAllByUserIdAndBorrowIdInQuery(String userId, List<UUID> borrowIds);
	  
	//JPA규칙대로 생성한 메소드
	List<Borrow> findByBorrowIdIn(List<String> borrowIds);
	
// insert+update+detete 할때 @Modifying와 @Query를 함께 사용한다.
//	@Modifying(clearAutomatically=true, flushAutomatically=true)
//	@Query(value="DELETE FROM borrow WHERE userId=:userId and borrowId in :borrowIds", nativeQuery = true)
//	void deleteBorrowByIds(String userId, String borrowIds);
}
