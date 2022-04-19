package com.sds.book.domain.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

//요건 나중에 사용하자 지금은 다른 방식으로 사용할란다.
@Component		//@Bean 은 외부라이브러리를 IoC할때, 거의 메소드단위, @Component 는 개발자가 직접 컨트롤가능한 클래스 단위 IoC
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint{

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
			
		log.info("CustomAuthenticationEntryPoint Start =========================");
	}

}
