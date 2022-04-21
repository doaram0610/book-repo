package com.sds.book.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

//이건 프론트앤드에서 보통 ajax 로 백앤드를 호출하는데
//그 호출에 대해서 응답을 허용할 건지를 설정해주어야 한다. 
@Configuration
public class CorsConfig {
	
	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);	//서버가 응답할때 json을 자바스크립트에서 처리할수 있게 할지 설정
		config.addAllowedOriginPattern("http://localhost:3000");  //config.addAllowedOrigin("*");		//모든 ip 응답을 허용, 버전업 되서 이젠 이거 사용 못한다.
		config.addAllowedHeader("*");	//모든 header에 응답을 허용
		config.addAllowedMethod("*");	//모든 post, get, put, delete, patch 요청을 허용
		config.addExposedHeader("*");	//프론트앤드가 리액트일때 이거 선언안하면 통과 못한다. 그런데 이것만 해서는 안되네.. 멀또 해야 되지? 아래 모든 주소를 포함하는걸로 수정해야됨
		source.registerCorsConfiguration("/**", config);	//로그인 url 이 루트/login 으로 고정되어 있어서 이렇게 변경
		return new CorsFilter(source);
	}
}
