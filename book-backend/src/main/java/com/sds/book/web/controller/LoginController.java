package com.sds.book.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sds.book.config.auth.PrincipalDetails;
import com.sds.book.domain.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping(value = "/index")
public class LoginController {

		@Autowired
		private BCryptPasswordEncoder bCryptPasswordEncoder;
		
		@Autowired
		private UserService userService;

//		시큐리티를 이용하기 때문에 이게 필요 없다. 이게 바로 PrincipalDetailsService 가 하는 일이다.
//		@CrossOrigin
//		@PostMapping("/login")
//		public ResponseEntity<?> login(@RequestBody String userId, String userPwd){
//			return new ResponseEntity<>(userService.login(userId, userPwd), HttpStatus.OK);
//		}
//		
		//구글,네이버 로그인 완료되면 호출되는걸 지정할수가 없고 무조건 루트를 호출하게 되는거 같아서 내가 원하는 페이지로 리다이렉트했다.
		//네이버, 구글 모두 로그인성공 후 호출되는 주소는 도메인:포트 까지만 된다. 하나의 도메인에 하나의 api를 지원하기 위해서인것 같다.
		//뒤에 주소붙이는건 안되니 ... 하지만 아마도 삼성은 내부서비스 호출 주소가 같으니까. 그 뒤에 주소까지도 넣을수 있게 구현했을거다. 
		@PostMapping({"/", ""})
		public String index(){
			return "login";
		}		
		
		//@AuthenticationPrincipal   어노테이션은 시큐리티세션값을 가져오는 방법이다.
		//AuthenticationPrincipal 은 두개의 타입을 갖는데 그 두 타입을 PrincipalDetails 에서 implements 했다.
		@GetMapping("/loginForm")
		public String loginForm(){
			return "loginForm";	//왜 머스테치로 안갈까? 헐 RestController 로 하면 안되구 Controller로 해야 된다.
		}
		
		//일반로그인 시 세션 정보 확인 테스트
		@GetMapping("/test/login")
		public @ResponseBody String testLogin(Authentication authentication, 
				@AuthenticationPrincipal PrincipalDetails pincipalDetails){
			log.info("testLogin=============================");
			
			PrincipalDetails principalDetails = (PrincipalDetails)authentication.getPrincipal();			
			log.info("authentication : "+ principalDetails.getUser()); //요건 시큐리티에서 받은 회원정보
			log.info("userDetails : "+ pincipalDetails.getUser());  //세션정보 로그인아이디값
			
			return "세션 정보 확인하기";
		}
		
		//구글로그인 시 세션정보 확인
		@GetMapping("/test/oauth/login")
		public @ResponseBody String testOAuthLogin(Authentication authentication,
				@AuthenticationPrincipal OAuth2User oAuth){
			log.info("testOAuthLogin=============================");
			
			OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal();			
			log.info("authentication : "+ oAuth2User.getAttributes()); 	//구굴에서 받은 회원정보
			log.info("oAuth2User : "+ oAuth.getAttributes()); 	//세션정보 로그인값
			
			return "OAuth 세션 정보 확인하기";
		}
		
//		@GetMapping("/manager")
//		public ResponseEntity<?> manager(){
//			return new ResponseEntity<>("성공", HttpStatus.OK);
//		}
//		
//		@GetMapping("/admin")
//		public ResponseEntity<?> admin(){
//			return new ResponseEntity<>("성공", HttpStatus.OK);
//		}
}
