package com.sds.book.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonSubTypes.Type;

//도서대출
public interface IBorrowBook {
	
	String getBorrowId();
	
	String getUserId();
	
	Long getBookId();
	
	String getTitle();
	String getAuthor();
	
	LocalDateTime getStartDate();
	LocalDateTime getEndDate();	

}
