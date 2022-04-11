package com.sds.book.config.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.sds.book.domain.model.User;

import lombok.Data;

//자 이제 시큐리티가 만들어주는 세션에 내가 만든 User를 넣어 보자.
//시큐리티가 /login 주소 요청되면 낚아채서 로그인을 진행해주는데
//로그인이 완료되면 시큐리티 session 을 만들어준다.(Security ContextHolder)
//이 세션의 오브젝트 타입은 Authentication 이고  이 Authentication 안에 User 정보가 있어야 됨
//그리고 User오브젝트 타입은 UserDetails 타입이다.
//즉, Security Session => Authentication => UserDetails 또는 OAuth2User
//Authentication 이 세션에 들어가는데 이때 Authentication이 두개의 타입을 갖도록 해주면 된다.(*중요)
@Data
public class PrincipalDetails implements UserDetails,  OAuth2User{

	private User user;
	private Map<String, Object> attributes;	//구글로그인 회원정보를 담기위해 추가
	
	
	//일반로그인
	public PrincipalDetails(User user) {
		this.user = user;
	}
	
	//OAuth2로그인할때
	public PrincipalDetails(User user, Map<String, Object> attributes) {
		this.user = user;
		this.attributes = attributes;
	}
	
	//해당 유저의 권한
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collect = new ArrayList<>();
		
		collect.add(new GrantedAuthority() {
			@Override
			public String getAuthority() {
				return user.getRole();
			}
		});
		return collect;
	}

	//유저의 인코딩된 암호
	@Override
	public String getPassword() {
		return user.getUserPwd();
	}

	//유저의 아이디
	@Override
	public String getUsername() {
		return user.getUserId();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	//OAuth2User 를 implements 하면서 자동추가된 메소드
	@Override
	public String getName() {
//		return attributes.get("sub");	//요건 안쓰니까 무시
		return null;
	}

	//OAuth2User 를 implements 하면서 자동추가된 메소드
	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}

}
