package com.sds.book.batch;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.sds.book.domain.model.Book;
import com.sds.book.domain.model.User;
import com.sds.book.domain.repository.BookRepository;
import com.sds.book.domain.repository.UserRepository;

//Configuration+Bean 을 선언해서 사용하면 스프링부트 최초 1회 실행된다. 
@Configuration
public class DBInitializer {

	@Bean
	public CommandLineRunner initDB(UserRepository userRepository, BookRepository bookRepository) {
		return(args) -> {
			List<Book> books = new ArrayList<>();
			books.add(new Book(1L, "노후 이렇게만 하면 잘먹고 잘살수 있다","김투자", 5, 0));
			books.add(new Book(2L, "제2의 직업! 어떻게 구할 것인가, 실전!","이말년", 5, 0));
			bookRepository.saveAll(books);
			
			List<User> users = new ArrayList<>();
			users.add(User.builder()
					.id(UUID.randomUUID().toString().replace("-", ""))
					.userId( "admin")
					.userPwd(new BCryptPasswordEncoder().encode("1234"))
					.userName("관리자")
					.role("ROLE_ADMIN")
					.build());
			
			users.add(User.builder()
					.id(UUID.randomUUID().toString().replace("-", ""))
					.userId( "1")
					.userPwd(new BCryptPasswordEncoder().encode("1"))
					.userName("홍길동")
					.role("ROLE_USER")
					.build());
			
			userRepository.saveAll(users);
		};
	}
}
