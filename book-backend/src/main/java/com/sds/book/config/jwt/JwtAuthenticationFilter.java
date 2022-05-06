package com.sds.book.config.jwt;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.mapping.Map;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sds.book.config.auth.PrincipalDetails;
import com.sds.book.domain.model.User;
import com.sds.book.web.dto.CmResponseDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

//스프링시큐리티에 UsernamePasswordAuthenticationFilter가 있다.
//login요청해서 username, password 전송하면(post)  UsernamePasswordAuthenticationFilter가 동작한다.

@Slf4j
@RequiredArgsConstructor	//final 로 선언된 객체의 생성자를 만들어준다.
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
	private final AuthenticationManager authenticationManager;
	
	// /login 호출하면 로그인 시도를 위해 실행된다.
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		log.info("JwtAuthenticationFilter 시작(로그인 시도중...) =================================");
				
		//1. userid, password 받아서
		//2. authenticationManager 로 로그인시도를 하면 PrincipalDetailsService가 호출되어 loadUserByUsername가 실행됨
		//3. PrincipalDetails를 세션에 담고 : 권한관리때문에 하는거다
		//4. JWT토큰을 만들어서 응답해주면 됨
		
		//넘어온 파라미터를 받아서 로그인정보를 담는다.
		ObjectMapper om = new ObjectMapper();
		User user = null;
		try {				
			user =  om.readValue(request.getInputStream(), User.class);			
			log.info("user: "+user);
		} catch (Exception e) {
			log.info("아이디 암호 값이 없어서 인증이 불가하다.\n"+e.getMessage());
	//		e.printStackTrace();
		}		
			
		if(user == null) {
			return null;
		}
	
		//파라미터로 넘어온 아이디암호 값으로 토큰을 만들어서 로그인시도 할 거다
		UsernamePasswordAuthenticationToken authenticationToken = 
				new UsernamePasswordAuthenticationToken(user.getUserId(), user.getUserPwd());
		
		//위에서 만든 토큰으로 로그인 시도하자
		//아래처럼 authenticationManager로 로그인시도하면 PrincipalDetailsService.loadUserByUsername 가 실행되서
		//정상이면 authentication 가 리턴된다. - 정상이라는건 db의 아이디,암호와 일치한다는건
		Authentication authentication = authenticationManager.authenticate(authenticationToken);
		
		//위에서 로그인성공되면 아래의 PrincipalDetails객체에 값을 봐서 있다는건 로그인이 정상적으로 되었다는것
		PrincipalDetails principalDetails = (PrincipalDetails)authentication.getPrincipal();
		log.info("로그인완료 user: "+principalDetails.getUser().getUserName());
				
		
		//아래처럼 authentication이 리턴되면 세션이 생성된다.
		//굳이 JWT토큰을 사용하면서 세션을 생성하는건 권한관리를 시큐리티한테 맡기려고 한다.
		return authentication;
	}
	
	
	//위attemptAuthentication함수가 정상 수행되면 successfulAuthentication함수가 수행된다.
	//여기서 JWT토큰을 만들어서 response에 담아준다.
	//서버가 보내준 토큰이라는 증명을 하려고 토큰없이 요청시 서버만 알고 있는 값을 넣어서 토큰을 보내준다.
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
		Authentication authResult) throws IOException, ServletException {
		
		log.info("successfulAuthentication실행됨:인증이 완료되었다는 뜻");
		
		//아래 로그인정보를 이용해서 JWT토큰을 생성할거다.
		PrincipalDetails principalDetails = (PrincipalDetails)authResult.getPrincipal();
		
		//RSA방식(공개키+개인키)이 아니라 HASH암호화방식(서명값)
		String jwtToken = JWT.create()
				.withSubject("jjang token")		//토큰명 : 크게 의미 없다.
				.withExpiresAt(new Date(System.currentTimeMillis()+(60000*10)))		//토큰만료시간=현재시간+1초(=1000)/1분(=60000)
				.withClaim("id", principalDetails.getUser().getId())	//내가 넣고싶은값 넣으면됨
				.withClaim("userId", principalDetails.getUser().getUserId())
				.withClaim("userName", principalDetails.getUser().getUserName())
				.sign(Algorithm.HMAC512("jjang"));		// 서버에서 토큰을 검증할때 이 값으로 확인한다.
		
		//리턴하는 헤더랑 타입
		response.addHeader("Authorization", "Bearer "+jwtToken);		//프론트앤드에 응답해주는 값에 추가하는거다
		response.setContentType("application/json; charset=utf-8");
		
		log.info("response.addHeader : "+ response.getHeader("Authorization"));
		
		//리턴하는 body 값
		CmResponseDto<User> cmResponseDto = new CmResponseDto<>(200, "성공",  
				User.builder().userId(principalDetails.getUser().getUserId())
										.userName(principalDetails.getUser().getUserName())
										.role(principalDetails.getUser().getRole())
										.build()
				);
		ObjectMapper om = new ObjectMapper();
		String cmRespDtoJson = om.writeValueAsString(cmResponseDto);
		PrintWriter out = response.getWriter();
		out.print(cmRespDtoJson);
		out.flush();
		
		//아래 한줄 때문에  응답으로 보내고 멈춰야 하는데 자꾸 로컬호스트 /루트가 호출됐었다! 그래서 헤더값확인이 안됐었다. 
//		super.successfulAuthentication(request, response, chain, authResult);
	}
}
