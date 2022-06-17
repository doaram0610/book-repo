package com.sds.book.domain.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sds.book.domain.model.Book;
import com.sds.book.domain.model.Borrow;
import com.sds.book.domain.model.IBorrowBook;
import com.sds.book.domain.repository.BookRepository;
import com.sds.book.domain.repository.BorrowRepository;

import lombok.RequiredArgsConstructor;



//기능을 정의하고 트랜잭션을 관리할 수 있다.(비즈니스로직)
@RequiredArgsConstructor  //finall 이 붙어있는 함수의 생성자를 만들어 준다. 찾아바라
@Service
public class BorrowService {

	private final BorrowRepository borrowRepository;
	private final BookRepository bookRepository;

	//대출도서 목록
	@Transactional(readOnly = true) //readOnly=true 는 JPA의 변경감지 기능을 비활성화해서 내트랜잭션 안에서는 다른 트랜잭션이 UPDATE 를 해도 내 트랜젝션안의 데이터를 유지시켜준다.
	//단 INSERT 를 한 것에 대해서는 못 막아주긴 한다. 그래두 UPDATE 의 정합성을 유지해주니까 꼭 사용하자
	public List<IBorrowBook> Select(String user_Id) {
			return borrowRepository.selectBorrowBookList(user_Id);
	}
	
	//대출신청
	@Transactional 
	public Borrow Save(Borrow borrow) {
		
		//0. 아이디생성
		String newBorrowId = UUID.randomUUID().toString().replace("-", "");
		borrow.setBorrowId(newBorrowId);
		
		//1. 도서의 현재대출권수 증가시키고 (더티체킹:실제로 업데이트하지 않고 추출하여 변경하고 함수가 종료되면 변경이 커밋된다. @Transactional를 꼭써야한다.)
		Book bookEntity = bookRepository.findById(borrow.getBook().getBookId()).orElseThrow(()->new IllegalArgumentException("존재하지 않는 도서이다"));
		bookEntity.setBorrow(bookEntity.getBorrow()+1);
		
		//2. 대출등록
		return borrowRepository.save(borrow);
	}
	
	//대출도서반납
	@Transactional
	public List<IBorrowBook> Delete(String user_Id, List<String> borrowIds) {

		//1. 반납도서목록 아이디를 리스트에 담아
		List<Borrow> borrows = borrowRepository.findByBorrowIdIn(borrowIds);
		List<Long> bookIds = new ArrayList<>();
		for(Borrow borrow : borrows) {
			bookIds.add( borrow.getBook().getBookId()) ;
		}
		
		//2. 대출목록에서 선택한 대출 삭제
//		borrowRepository.deleteAllByUserIdAndBorrowIdInQuery(userId, borrowIds);
		borrowRepository.deleteAllById(borrowIds);

		//3. 도서의 대출권수를 감소시킨다. 
		List<Book> books = bookRepository.findByBookIdIn(bookIds);
		for(Book book : books) {
			book.setBorrow(book.getBorrow()-1);
		}
//		List<Book> books = bookRepository.updateBookBorrow(bookIds);

		return borrowRepository.selectBorrowBookList(user_Id);
	}
	
	//대출기간 연장
	@Transactional  //서비스 함수가 종료될때 commit 할지 rollback 할지 트랜잭션 관리하겠다.
	public Borrow Update(String borrowId, Borrow borrow) {		
		Borrow borrowEntity = borrowRepository.findById(borrowId).orElseThrow(()->new IllegalArgumentException("대출번호를 확인하세요"));
		borrowEntity.setStartDate(borrow.getStartDate());
		borrowEntity.setEndDate(borrow.getEndDate());
		return borrowEntity;  //더티체킹  리턴시(함수종료)에 변경된데이터를 받으면서 영속화된데이터를 db 로 갱신(flush)하면서 commit 된다. 이것이 더티체킹이다.
	}	
}
