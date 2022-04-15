package com.sds.book.domain.model;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
		private String providerId;	
		
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
		
		//권한을 한개 이상 갖은 사용자인경우를 위해 배열로 리턴해준다.
//		public List<String> getRoleList(){
//			if(this.role.length() > 0) {
//				return Arrays.asList(this.role.split(","));	// 권한을 콤마로 구분지어 주면 배열로 리턴 USER,ADMIN
//			}
//			return new ArrayList<>();
//		}
}
