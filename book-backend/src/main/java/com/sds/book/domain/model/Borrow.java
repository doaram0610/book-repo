package com.sds.book.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//도서대출
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Borrow {
	
	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@GeneratedValue(generator = "uuid2")
//	@GenericGenerator(name = "uuid2", strategy = "uuid2")	
//	@Column(columnDefinition = "BINARY(16)")
	private String borrowId;
	
	private LocalDateTime startDate;
	private LocalDateTime endDate;
	
	//현재테이블과 아래 테이블의 조인시에 데이터 건수가 1개면 OneToOne
	//CascadeType : Entity의 상태 변화를 전파시키는 옵션
	@OneToOne
	@JoinColumn
	private Book book;
	
	//컬럼명이 중복되서 그런거니까 아래 insertable,updatable를 사용해서 참조테이블데이터가 수정되지 않도록 한다.
	@OneToOne
//	@JoinColumn(name = "테이블명_PK", insertable = false, updatable = false)
	@JoinColumn
	private User user;
	
	@Builder
	public Borrow(User user, Book book) {
		this.user = user;
		this.book = book; 
	}
}
