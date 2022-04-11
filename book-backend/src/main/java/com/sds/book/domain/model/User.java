package com.sds.book.domain.model;


import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity	//테이블이 만들어진다.
@Data	//getter setter 가 만들어진다.
public class User {

		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private long Id;
		
		private String userId;
		private String userPwd;
		private String userName;
		private String role;
		
		//oauth 로그인으로 가입된 회원구분
		private String provider;		//google, naver
		private String providerId;	//
		
		@CreationTimestamp
		private Timestamp upDate;

		//builder 패턴을 이용해서 생성할수 있게 해준다.
		@Builder
		public User(String userId, String userPwd, String userName, String role, String provider, String providerId) {
			this.userId = userId;
			this.userPwd = userPwd;
			this.userName = userName;
			this.role = role;
			this.provider = provider;
			this.providerId = providerId;
		}
		
		
}
