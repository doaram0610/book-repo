package com.sds.book.config.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.sds.book.domain.model.User;
import com.sds.book.domain.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;


@Slf4j
//시큐리티 설정에서 loginProcessingUrl("/login");
// login요청이 오면 자동으로 UserDetailsService 타입으로 IoC되어 있는 loadUserByUsername 함수가 실행된다.
//그런데,  JWT 를 사용하려고 formLogin().disable() 해 놓으면 PrincipalDetailsService 를 호출하지 않으니까 
//UsernamePasswordAuthenticationFilter 를 이용해서 PrincipalDetailsService 를 호출한다.
@Service
public class PrincipalDetailsService implements UserDetailsService{

	@Autowired
	private UserRepository userRepository;
	
	//이 메소드가 호출되면 시큐리티세션(내부 Authentication(내부 UserDetails))이 생성된다.
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		//기가 막힌다 이거 파라미터 값이 안넘어와서 이틀 개고생했는데,
		//알고보니 기본 form submit 방식으로 넘겨야 시큐리티에서 파라미터를 받는다.
		//JSON방식으로 바꾸려면 필터를 추가해서 그 필터에서 얘를 호출해야 된다
		log.info("PrincipalDetailsService 시작 =============================");
		log.info("PrincipalDetailsService.loadUserByUsername.userId: "+username);
		
		User userEntity = userRepository.findByUserId(username);
		log.info("PrincipalDetailsService.loadUserByUsername.userEntity: "+userEntity);
		
		return new PrincipalDetails(userEntity);
	}

}
