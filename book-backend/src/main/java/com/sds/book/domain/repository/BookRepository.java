package com.sds.book.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.sds.book.domain.model.Book;

//@Repository 를 적어야 스프링 빈 IoC에 빈으로 등록되는데
//JpaRepository 를 extends 하면 생략가능하다.
//JpaRepository 는 CRUD 함수를 들고 있다.
public interface BookRepository extends JpaRepository<Book, Long> {

	@Modifying
	@Query(nativeQuery = true, value="update book set quantity=quantity-1,  borrow=borrow+1 where id=?")
	int updateBorrow(Long BookId);
}
