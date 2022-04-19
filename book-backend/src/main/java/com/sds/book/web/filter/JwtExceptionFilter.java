package com.sds.book.web.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.exceptions.JWTDecodeException;

import lombok.extern.slf4j.Slf4j;


//컨트롤러단까지 들어오기전에 이 필터를 거쳐서 exception 처리를 한다.
// Filter 대신에 OncePerRequestFilter를 상속하면 필터가 두번 실행되는것을 막고 한번만 실행되게 할수 있다
@Slf4j
public class JwtExceptionFilter extends OncePerRequestFilter{
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		log.info("JwtExceptionFilter 진입 시작 ================================================");
		
		try {
			filterChain.doFilter(request, response);
		}catch(JwtException e) {
			response.setStatus(HttpStatus.FORBIDDEN.value());
		}catch(JWTDecodeException e) {
			response.setStatus(HttpStatus.FORBIDDEN.value());			
		}
	}

}
