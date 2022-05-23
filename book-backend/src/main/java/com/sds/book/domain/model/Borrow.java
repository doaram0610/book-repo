package com.sds.book.domain.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//도서대출
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Borrow {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long borrowId;
	
	private String userId;
	private Long bookId;
	
	private LocalDateTime startDate;
	private LocalDateTime endDate;	
}
