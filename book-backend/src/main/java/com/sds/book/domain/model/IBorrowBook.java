package com.sds.book.domain.model;

import java.time.LocalDateTime;

//도서대출
public interface IBorrowBook {
	
	Long getBorrowId();
	
	String getUserId();
	Long getBookId();
	
	String getTitle();
	String getAuthor();
	
	LocalDateTime getStartDate();
	LocalDateTime getEndDate();	

}
