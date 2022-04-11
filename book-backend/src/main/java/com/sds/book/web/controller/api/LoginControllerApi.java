package com.sds.book.web.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sds.book.domain.service.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class LoginControllerApi {

		@Autowired
		private BCryptPasswordEncoder bCryptPasswordEncoder;
		
		@Autowired
		private final UserService userService;

//		시큐리티를 이용하기 때문에 이게 필요 없다. 이게 바로 PrincipalDetailsService 가 하는 일이다.
//		@CrossOrigin
//		@PostMapping("/login")
//		public ResponseEntity<?> login(@RequestBody String userId, String userPwd){
//			return new ResponseEntity<>(userService.login(userId, userPwd), HttpStatus.OK);
//		}
//		

		@GetMapping("/api/manager")
		public ResponseEntity<?> manager(){
			return new ResponseEntity<>("성공", HttpStatus.OK);
		}
		
		@GetMapping("/api/admin")
		public ResponseEntity<?> admin(){
			return new ResponseEntity<>("성공", HttpStatus.OK);
		}
}
