package com.sds.book.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sds.book.web.filter.MyFilter1;
import com.sds.book.web.filter.MyFilter2;

@Configuration	 //IoC로 등록한다는 말, 그럼 아래 @Bean 설정된 함수가 메모리에 띄워진다.
public class FilterConfig {
	
	//이렇게 만들어서 필터에 등록하면 시큐리티필터가 모두 동작한 다음에 수행된다.
	//아래처럼 bean 으로 선언해준 함수는 IoC에 등록되어 바로 사용할 수 있기 때문에
	//필터에 등록되어 내가 따로 수행하도록 호출하지 않아도 실행된다.
	//즉, 필터 만들고(MyFilter1, 2...) 지금 이 클래스에 빈으로 선언하면서 등록하면 필터로 수행된다.
//	@Bean
//	public FilterRegistrationBean<MyFilter1> filter1(){
//		FilterRegistrationBean<MyFilter1> bean = new FilterRegistrationBean<>(new MyFilter1());
//		bean.addUrlPatterns("/*");	//모든 요청에 대해서 필터를 걸어보자
//		bean.setOrder(0);		//낮은 번호가 필터중에서 가장 먼저 실행됨
//		return bean;
//	}
//	
//	@Bean
//	public FilterRegistrationBean<MyFilter2> filter2(){
//		FilterRegistrationBean<MyFilter2> bean = new FilterRegistrationBean<>(new MyFilter2());
//		bean.addUrlPatterns("/*");		//특정요청에 대해서만 필터를 걸고 싶으면 이거 변경하면 된다.
//		bean.setOrder(1);		//낮은 번호가 필터중에서 가장 먼저 실행됨
//		return bean;
//	}	
}
