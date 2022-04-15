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

import lombok.extern.slf4j.Slf4j;


//요건 jwt 의 토큰이 요청되었을때 어떻게 처리해야 할지를 보기위해서 샘플로 만들어 본거다
@Slf4j
public class MyFilter3 implements Filter{

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {		
		log.info("Filter 3");
		
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse res = (HttpServletResponse)response;
		
		//토큰: jjang 를 만들어줘야 한다. id, pwd 가 정상적으로 들어와서 로그인 완료되면 토큰 생성해주고 응답해준다.
		//요청할때 마다 header에 Authorization 에 value 값으로 토큰을 가져오면
		//그 토큰이 내가 만든 토큰인지만 검증하면 된다. (RSA, HS256 이걸로 디코딩해서)
		log.info("req.getMethod(): "+req.getMethod());
		
		if("POST".equals(req.getMethod())) {
			String headerAuth = req.getHeader("Authorization");			
			log.info("headerAuth: "+headerAuth);
			
			if("jjang".equals(headerAuth)) {
				chain.doFilter(req, res);//필터가 여기서 끝나지 말고 다음 필터를 계속 타라!
			}else {
				log.info("인증안됨. 내가 허용한 클라이언트가 아니다.");
				PrintWriter out = res.getWriter();		//이거 쓰면 에러나는데...  중요하지 않아서 넘어간다.
				out.println("인증안됨. 내가 허용한 클라이언트가 아니다.");
			}
//			chain.doFilter(req, res);
		}else {
			chain.doFilter(req, res);
		}
	}

}
