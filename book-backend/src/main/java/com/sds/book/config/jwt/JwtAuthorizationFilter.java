package com.sds.book.config.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.sds.book.config.auth.PrincipalDetails;
import com.sds.book.domain.handler.GlobalExceptionHandler;
import com.sds.book.domain.model.User;
import com.sds.book.domain.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

//시큐리티가 필터를 가지고 있는거 중에 BasicAuthenticationFilter 라는 것이 있는데
//권한이나 인증이 필요한 특정주소를 요청했을때 위 필터를 무조건 타는데
//권한인증이 필요하지 않으면 안탄다.
@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter{

	private final UserRepository userRepository;
	
	public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
		super(authenticationManager);
		this.userRepository = userRepository;
	}

	//인증 권한이 필요하면 해당 필터를 타게 된다.
	 @Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		 log.info("인증이나 권한이 필요한 주소가 요청됨 JwtAuthorizationFilter");
//		super.doFilterInternal(request, response, chain);  ///응답두번되서 에러남
		
		String jwtHeader = request.getHeader("Authorization");
		log.info("jwtHeader : "+jwtHeader);
		
		//로그인화면에서 호출한 경우에는 토큰이 없다 그러니 정상적으로 흘러가도록 한다.
		if(jwtHeader == null || !jwtHeader.startsWith("Bearer")) {
//			response.sendError(HttpStatus.BAD_REQUEST.value(), "토큰이 없다!");
			chain.doFilter(request, response);
			return;
		}
		log.info("헤더값이 있다.");
		
		//JWT토큰을 검증해서 정상적인 사용자인지 확인
		String jwtToken = request.getHeader("Authorization").replace("Bearer ", "");		
		log.info("jwtToken : "+jwtToken);
		
		String userId = null;
		try {
			userId = JWT.require(Algorithm.HMAC512("jjang")).build()
				.verify(jwtToken)
				.getClaim("userId").asString();
		}catch(TokenExpiredException e) {
			log.info("토큰 서명 중 에러 1: "+e.getMessage());
			response.sendError(HttpStatus.FORBIDDEN.value(), "만료된 토큰");
		}catch(JWTDecodeException e) {
			log.info("토큰 서명 중 에러 2: "+e.getMessage());
			response.sendError(HttpStatus.BAD_REQUEST.value(), "토큰이 없다");
		}
		
		log.info("jwtHeader.userId : "+userId);
		
		//서명이 정상적으로 됨
		if(userId != null) {
			User userEntity = userRepository.findByUserId(userId);
			log.info("DB에서 가져온 사용자정보 : "+userEntity);
			
			//JWT토큰 서명을 통해서 서명이 정상이면 Authentication객체를 만들어준다.
			PrincipalDetails principalDetails = new PrincipalDetails(userEntity);
			log.info("세션에 담을 사용자 정보 principalDetails : "+principalDetails);
			Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
		
			//강제로 시큐리티의 세션에 접근해서 Authentication객체를 저장한다.
			SecurityContextHolder.getContext().setAuthentication(authentication);
			
			chain.doFilter(request, response);
		}
		
	}
}
