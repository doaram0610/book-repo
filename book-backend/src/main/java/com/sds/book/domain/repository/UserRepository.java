package com.sds.book.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sds.book.domain.model.User;

//@Repository 를 적어야 스프링 빈 IoC에 빈으로 등록되는데
//JpaRepository 를 extends 하면 생략가능하다.
//JpaRepository 는 CRUD 함수를 들고 있다.
public interface UserRepository extends JpaRepository<User, String> {
	
	@Query(value="SELECT * FROM user WHERE userId=? AND userPwd = ? ", nativeQuery = true)
	User login(String userId, String userPwd);
	
	//findBy규칙 -> UserId 문법  : 요거궁금하면 JPA Query methods 함수 찾아보자
	//select * from user where userId = 1?
	public User findByUserId(String userId);
}
