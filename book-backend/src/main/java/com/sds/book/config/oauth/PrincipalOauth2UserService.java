package com.sds.book.config.oauth;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.sds.book.config.auth.PrincipalDetails;
import com.sds.book.config.oauth.provider.GoogleUserInfo;
import com.sds.book.config.oauth.provider.NaverUserInfo;
import com.sds.book.config.oauth.provider.OAuth2UserInfo;
import com.sds.book.domain.model.User;
import com.sds.book.domain.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;


@Slf4j
//시큐리티 설정에서 loginProcessingUrl("/login");
// login요청이 오면 자동으로 UserDetailsService 타입으로 IoC되어 있는 loadUserByUsername 함수가 실행된다.
@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService{
	
	@Autowired
	private UserRepository userRepository;
	
	//이 함수 종료시에 @AuthenticationPrincipa 어노테이션이 만들어진다.
	//구글로그인 후처리 함수(구글에서 받은 userRequest 에 들어있는 정보로 회원가입해준다.) 
	@Override
		public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		
			log.info("userRequest.getAccessToken: "+userRequest.getAccessToken());	
			log.info("userRequest.getClientRegistration: "+userRequest.getClientRegistration());		//어떤 oauth 로그인인지

			//구글에서 넘어온 정보(userRequest)로 회원가입 강제 진행
			//구글로그인버튼 클릭 -> 구글로그인창 -> 로그인완료 -> code리턴(OAuth-Client라이브러리) -> AccessToken 요청 : 여기까지 userRequest
			//이제 userRequest 정보를 이용해 loadUser 함수에서 구글로 부터 회원프로필을 받을 수 있다.
			
			OAuth2User oAuth2User = super.loadUser(userRequest);
			log.info("userRequest.getAuthorities: "+oAuth2User.getAttributes()); //와 여기에 실제구글회원정보있다
			
			
			//아래부분은 구글, 네이버, 페이스북 중 어디냐에 따라 파라미터 값이 달라질수 있으니 별도 클래스를 받아서 처리한다.
			String provider = userRequest.getClientRegistration().getRegistrationId();	//google
			/*
			String providerId = oAuth2User.getAttribute("sub");	//구글아이디키값
			String userId = provider+"_"+providerId;			
			String password = new BCryptPasswordEncoder().encode("123"); // bCryptPasswordEncoder.encode("짱");  //강제회원가입을 위해 인코딩된 암호값지정
			String name = oAuth2User.getAttribute("name");
			String role = "ROLE_MANAGER";		//관리자로 등록
			*/
			OAuth2UserInfo oAuth2UserInfo = null;
			if(provider.equals("google")) {
				log.info("구글 로그인 요청");
				oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
			}else if(provider.equals("naver")) {
				log.info("네이버 로그인 요청");
				oAuth2UserInfo = new NaverUserInfo((Map<String, Object>) oAuth2User.getAttributes().get("response"));
			}else {
				log.info("구글하구 네이버만 지원한다.");
			}
			String providerId = oAuth2UserInfo.getProviderId();
			String userId = oAuth2UserInfo.getProviderId();	
			String password = new BCryptPasswordEncoder().encode("123"); // bCryptPasswordEncoder.encode("짱");  //강제회원가입을 위해 인코딩된 암호값지정
			String name = oAuth2UserInfo.getName();
			String role = "ROLE_MANAGER";		//관리자로 등록
			
			
			//기존회원과 등록이 중복되는 아이디 인지 확인해서 없는 아이디이면 저장
			User user = userRepository.findByUserId(userId);
			if(user == null) {
				user = User.builder()
						.userId(userId)
						.userName(name)
						.userPwd(password)
						.provider(provider)
						.providerId(providerId)
						.role(role)
						.build();		
				
				userRepository.save(user);	//참내 Config에 등록한 암호화빈을 여기서 주입해서 사용하려니 순환참조 에러가 나네... 
			}
			
			//아래 값이 리턴될때 Authentication 객체에 UserDetails 또는 OAuth2User 객체가 들어가면서 세션이 생성된다.!!!
//			return super.loadUser(userRequest);  //요거 하라구 해서 했더니 에러 ! 참내  메이븐 버전이 엉켜서 버전 지정해주니 해결!
			return new PrincipalDetails(user, oAuth2User.getAttributes());	//리턴되는 순간 이 정보로 세션생성!!
		}
}
