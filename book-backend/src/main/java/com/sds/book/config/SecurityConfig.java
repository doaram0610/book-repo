package com.sds.book.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.sds.book.config.oauth.PrincipalOauth2UserService;

import lombok.RequiredArgsConstructor;

@Configuration		//메모리에 떠야 하니까 이렇게 추가하고
@EnableWebSecurity	//스프링 시큐리티 필터가 스프링 필터체인에 등록된다.
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)	//secured 어노테이션 활성화, preAuthorize+postAuthorize 어노테이션 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter	{
	
	//구글로그인
	@Autowired
	private  PrincipalOauth2UserService principalOauth2UserService;
	
	//시큐리티코딩은 암호화가 안되면 로그인이 안되니까 이걸 추가해줘야 한다.
	@Bean		//해당 메서드의 리턴되는 오브젝트를 IoC로 등록해준다.
	public BCryptPasswordEncoder encodePwd() {
		return new BCryptPasswordEncoder();
	}
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		
		//아래 호출 주소는 모두 컨트롤러에 존재해야 한다. user, manager, admin
		http.csrf().disable();	//세션을 사용하지 않고 JWT 토큰을 활용하여 진행하고 REST API를 만드는 작업이기때문에 csrf 사용은 disable 처리합니다.
		http.cors().configurationSource(corsConfigurationSource());	//스프링시큐리티에서 CORS 허용(요거랑 밑에 corsConfigurationSource메소드 추가)			

		http.authorizeRequests()	//인증이 필요한 요청 목록을 적기 시작한다는 표시
				.antMatchers("/css/**", "/js/**", "/img/**").permitAll()	//이 요청은 무조건 접근허용한다.
				.antMatchers("/api/**").permitAll()		//이 요청은 인증절차 없이 접근이 허용된다.
				.antMatchers("/h2-console/**").permitAll()	
				.antMatchers("/loginForm/**").permitAll()	
				
				//인가처리에 대해서 글로벌로 걸고 싶으면 아래 처럼 하고 메소드 단위로 하고 싶을땐 @EnableGlobalMethodSecurity 를 이용한다.
				.antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")	// manager로 요청하면 admin+manager권한이 있어야 접근가능
				.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")	//admin로 요청하면 admin권한이 있어야 접근가능
//				.anyRequest().permitAll()		//위 요청 말고 모든 요청을 접근허용한다.(permitAll)
				.anyRequest().authenticated();	//위 명시된 요청 이외 모두 인증후에 접근할수 있다
				;
		http.formLogin()	//위에서 authenticated 로 선언한 요청이 호출되면 아래와 같이 로그인 처리시작한다. 
				.loginPage("/loginForm")	//내가 만든 로그인화면으로 이동 해준다.
				.loginProcessingUrl("/login")	//이 주소가 호출되면 시큐리티가 낚아채서 대신 로그인을 해준다.
				.defaultSuccessUrl("/manager")		//로그인 성공하면 돌아갈 페이지 주소
//				.failureUrl("/login?error") // default
//				.usernameParameter("userId")	//원래 시큐리티 사용하면 아이디의 파라미터명을 username 으로 해야 하는데 안할라면 이렇게 설정
//				.passwordParameter("userPwd")//시큐리티지정 파라미터 password의 변경할 파라미터명		
				.and()
					.oauth2Login()	//oauth2 기반 로그인을 할 경우 
					.loginPage("/loginForm") 	//로그인을 할 로그인화면 주소
					.userInfoEndpoint()	//구글로그인이 성공되면 구글에 등록된 리디렉션 주소로 사용자정보를 함께 리턴해준다. 					
					//참나 왜 여기서 컴파일 에러가 나거나 시작못하구 에러야!. 도대체 왜!!!! 에러가 났다가 안났다가 그러네 - 메이븐 버전 안맞아서 지정하니 해결!
					.userService(principalOauth2UserService)  //구글로그인이 완료되면 위 userInfoEndpoint 의 엑세스토큰+사용자정보를 받게 된다.
			;
		http.headers().frameOptions().disable();	//h2console 접근허용하기 위해 추가
	}
	
    // 스프링시큐리티에서 CORS 허용 적용 (스프링이랑 스프링시큐리티 두군데서 허용해줘야 함)
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
	    CorsConfiguration corsConfiguration = new CorsConfiguration();
	    corsConfiguration.addAllowedOrigin("http://localhost:3000");
	    corsConfiguration.addAllowedHeader("*");
	    corsConfiguration.addAllowedMethod("*");
	    corsConfiguration.setAllowCredentials(true);
	    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    source.registerCorsConfiguration("/**", corsConfiguration);
	    return source;
	}
	
}
