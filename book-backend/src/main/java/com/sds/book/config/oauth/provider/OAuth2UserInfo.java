package com.sds.book.config.oauth.provider;

//구글, 페이스북, 네이버 이렇게 여러개의 로그인을 연결하게 되면
//인터페이스를 만들어서 implements 하게 해서 형식을 맞추게 한다.
//구글, 페이스북, 네이버가 아래와 같은 형식으로 데이터를 받게끔 하기 위해서 만든거다.
public interface OAuth2UserInfo {
	
	String getProviderId();
	String getProvider();
	String getEmail();
	String getName();
}
