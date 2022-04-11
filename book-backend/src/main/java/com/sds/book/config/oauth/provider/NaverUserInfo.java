package com.sds.book.config.oauth.provider;

import java.util.Map;

public class NaverUserInfo implements OAuth2UserInfo{

	private Map<String, Object> attributes;	//PrincipalOauth2UserService 에서 받은 oAuth2User.getAttributes()
	
	//생성자 Oauth2로 로그인할때 받은 사용자정보를 받는다.
	public NaverUserInfo(Map<String, Object> attributes) {
		this.attributes = attributes;
	}
	
	@Override
	public String getProviderId() {		
		return (String) attributes.get("id");	//네이버에서 id로 아이디값 리턴
	}

	@Override
	public String getProvider() {
		return "naver";
	}

	@Override
	public String getEmail() {
		return (String) attributes.get("email");
	}

	@Override
	public String getName() {
		return (String) attributes.get("name");
	}

}
