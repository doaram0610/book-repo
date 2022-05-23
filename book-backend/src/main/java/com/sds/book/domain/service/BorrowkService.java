package com.sds.book.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sds.book.domain.model.Book;
import com.sds.book.domain.model.Borrow;
import com.sds.book.domain.model.IBorrowBook;
import com.sds.book.domain.repository.BookRepository;
import com.sds.book.domain.repository.BorrowRepository;

import lombok.RequiredArgsConstructor;



//기능을 정의하고 트랜잭션을 관리할 수 있다.

@RequiredArgsConstructor  //finall 이 붙어있는 함수의 생성자를 만들어 준다. 찾아바라
@Service
public class BorrowkService {

	private final BorrowRepository borrowRepository;
	
	public Borrow Save(Borrow borrow) {
		return borrowRepository.save(borrow);
	}
	
	@Transactional(readOnly = true) //readOnly=true 는 JPA의 변경감지 기능을 비활성화해서 내트랜잭션 안에서는 다른 트랜잭션이 UPDATE 를 해도 내 트랜젝션안의 데이터를 유지시켜준다.
	//단 INSERT 를 한 것에 대해서는 못 막아주긴 한다. 그래두 UPDATE 의 정합성을 유지해주니까 꼭 사용하자
	public List<IBorrowBook> Select(String userId) {
			return borrowRepository.selectBorrowBookList(userId);
	}
	
	@Transactional  //서비스 함수가 종료될때 commit 할지 rollback 할지 트랜잭션 관리하겠다.
	public Borrow Update(Long borrowId, Borrow borrow) {		
		Borrow borrowEntity = borrowRepository.findById(borrowId).orElseThrow(()->new IllegalArgumentException("대출번호를 확인하세요"));
		borrowEntity.setStartDate(borrow.getStartDate());
		borrowEntity.setEndDate(borrow.getEndDate());
		return borrowEntity;  //더티체킹  리턴시(함수종료)에 변경된데이터를 받으면서 영속화된데이터를 db 로 갱신(flush)하면서 commit 된다. 이것이 더티체킹이다.
	}
	
	@Transactional
	public int Delete(Long borrowId) {
		borrowRepository.deleteById(borrowId);
		return 200;
	}
}
