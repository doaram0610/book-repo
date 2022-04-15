package com.sds.book.web.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sds.book.domain.model.User;
import com.sds.book.domain.service.UserService;

import jdk.internal.org.jline.utils.Log;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor //final 이라고 된 객체의 생성자를 자동으로 만들어준다.
@RestController
@RequestMapping(value = "/api")
public class UserControllerApi {

	//@RequiredArgsConstructor 를 사용해서 final 로 선언하던지 아님 @Autowired 를 사용하던지 하면 IoC가 된다.
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private final UserService userService;
	
	//리턴타입을 ResponseEntity 로 사용하면 리턴코드와 리턴데이터를 함께 넘겨줄수 있으니 이것을 사용해라
	//security (라이브러리를 적용하면) - CORS정책을 갖고 있다.(시큐리티가 CORS를 해제해줘야 한다.)
//	@CrossOrigin
	@PostMapping("/user")
	public ResponseEntity<?> save(@RequestBody User user){  //@RequestBody는 json으로 받겠다는 뜻
		
		log.info("user: "+user);
		user.setRole("ROLE_"+user.getRole());
		
		//입력한 암호를 시큐리티가 판단할수 있게 인코딩 해준다.
		String rawPassword = user.getUserPwd();
		String encPassword = bCryptPasswordEncoder.encode(rawPassword);
		user.setUserPwd(encPassword);
		
		return new ResponseEntity<>(userService.Save(user), HttpStatus.OK);
	}
	
//	@CrossOrigin  //외부에서오는 자바스크립트 요청을 허용해준다.
	@GetMapping("/user")
	public ResponseEntity<?> findAll(){
		return new ResponseEntity<>(userService.ListAll(), HttpStatus.OK);
	}
	
//	@CrossOrigin(origins = "*", allowedHeaders = "*") // 컨트롤러에서 설정 //외부에서오는 자바스크립트 요청을 허용해준다.
	@GetMapping("/user/{id}")
	public ResponseEntity<?> findById(@PathVariable Long id){
		return new ResponseEntity<>(userService.Select(id), HttpStatus.OK);
	}	
	
	@PutMapping("/user/{id}")
	public ResponseEntity<?> update(@PathVariable Long id, @RequestBody User user){
		return new ResponseEntity<>(userService.Update(id, user), HttpStatus.OK);
	}	
	
	//제네릭 ? 란 : JDK 1.5부터 도입된 제네릭을 사용하면 컴파일 시에 미리 타입이 정해지므로, 타입 검사나 타입 변환과 같은 번거로운 작업을 생략할 수 있게 됩니다.
	//<?> : 타입 변수에 모든 타입을 사용할 수 있음
	@DeleteMapping("/user/{id}")
	public ResponseEntity<?> deleteById(@PathVariable Long id){
		return new ResponseEntity<>(userService.Delete(id), HttpStatus.OK);
	}	
}
