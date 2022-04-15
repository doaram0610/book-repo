package com.sds.book.web.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sds.book.config.auth.PrincipalDetails;
import com.sds.book.domain.service.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api")
public class LoginControllerApi {

		private final BCryptPasswordEncoder bCryptPasswordEncoder;
		
		@Autowired
		private final UserService userService;

//		시큐리티를 이용하기 때문에 이게 필요 없다. 이게 바로 PrincipalDetailsService 가 하는 일이다.
//		@CrossOrigin
//		@PostMapping("/login")
//		public ResponseEntity<?> login(@RequestBody String userId, String userPwd){
//			return new ResponseEntity<>(userService.login(userId, userPwd), HttpStatus.OK);
//		}
//		
		// Tip : JWT를 사용하면 UserDetailsService를 호출하지 않기 때문에 @AuthenticationPrincipal 사용 불가능.
		// 왜냐하면 @AuthenticationPrincipal은 UserDetailsService에서 리턴될 때 만들어지기 때문이다.		
		//토큰정보와 함께 아래 주소 호출시 사용자 정보를 리턴한다.
		@PostMapping("/login")
		public ResponseEntity<?> login(Authentication authentication){
			
			//아래 Authentication 값이 널이면 세션이 생성안됐다는 거다 
			PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
			return new ResponseEntity<>(principalDetails.getUser(), HttpStatus.OK);
		}
		
		@GetMapping("/manager")
		public ResponseEntity<?> manager(){
			return new ResponseEntity<>("Manager 성공", HttpStatus.OK);
		}
		
		@GetMapping("/admin")
		public ResponseEntity<?> admin(){
			return new ResponseEntity<>("Admin 성공", HttpStatus.OK);
		}
}
