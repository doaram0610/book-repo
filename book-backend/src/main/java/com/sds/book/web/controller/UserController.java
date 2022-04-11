package com.sds.book.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sds.book.config.auth.PrincipalDetails;
import com.sds.book.domain.model.User;
import com.sds.book.domain.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor //final 이라고 된 객체의 생성자를 자동으로 만들어준다.
@Controller
@RequestMapping(value = "/manager")
public class UserController {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private final UserService userService;
	
	//회원가입
	@GetMapping("/userForm")
	public String userForm(){
		return "userForm";	//왜 머스테치로 안갈까? 헐 RestController 로 하면 안되구 Controller로 해야 된다.
	}
	
	//회원 목록
	//자 보자 리턴값이 String 이면 화면으로 리턴해주고
	//리턴값이 @ResponseBody 이면 json 으로 리턴해준다.
	//그리고 @RestController 일 경우 기본 리턴값이 @ResponseBody 한거랑 똑같아 진다.
	//그래서 json 으로 리턴해줄때 리턴 형식을 동일하게 맞춰줄려고 ResponseEntity<?> 으로 리턴한다.
	@GetMapping("/user")
	public String findAll(Model model, @AuthenticationPrincipal PrincipalDetails principalDetails){		
		log.info("PrincipalDetails : "+principalDetails.getUser());	
		
		model.addAttribute("users", userService.ListAll());	//이 값은 머스테치에서 바로 받아 사용가능하다	
		return "users";
	}
	
	
	//회원저장
	//리턴타입을 ResponseEntity 로 사용하면 리턴코드와 리턴데이터를 함께 넘겨줄수 있으니 이것을 사용해라
	//security (라이브러리를 적용하면) - CORS정책을 갖고 있다.(시큐리티가 CORS를 해제해줘야 한다.)
//	@CrossOrigin  //외부에서오는 자바스크립트 요청을 허용해준다.
	@PostMapping("/user")
	public ResponseEntity<?> save(@RequestBody User user){  //@RequestBody는 json으로 받겠다는 뜻
		
		log.info("user: "+user);
		
		//입력한 암호를 시큐리티가 판단할수 있게 인코딩 해준다.
		String rawPassword = user.getUserPwd();
		String encPassword = bCryptPasswordEncoder.encode(rawPassword);
		user.setUserPwd(encPassword);
				
		if(user.getRole() == null) {
			user.setRole("ROLE_USER");
		}else {
			user.setRole("ROLE_"+user.getRole());	
		}		
		
		return new ResponseEntity<>(userService.Save(user), HttpStatus.OK);
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
	
	//메소드 단위로 인가처리 하고 싶을때 하는 2가지 방법 : 나는 글로벌 방식으로 했으니까 이건 나중에 쓰자
	@Secured("ROLE_MANAGER")	//간단하게 인가된 사용자만 접근 가능하도록 해주는 어노테이션
	@GetMapping("/info")
	public @ResponseBody String info() {
		return "개인정보";
	}
	
	//인가 대상을 하나만 걸땐 @Secured 를 사용하구 여러개 할때 아래 처럼 한다.
	@PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_MANAGER') ") //함수시작전
	@GetMapping("/data")
	public @ResponseBody String data() {
		return "데이터정보";
	}	
	
}
