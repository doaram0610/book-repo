import React, { useEffect, useState } from 'react';
import { Button, Stack } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import BookItem from '../../components/BookItem';
import { API_BASE_URL } from '../../js/ApiConfig';
import { signup } from '../../js/ApiService';

const BookList = () => {
  const [books, setBooks] = useState([]);
  const host = API_BASE_URL + '/api';
  const navigate = useNavigate();
  const token = localStorage.getItem('accessToken');

  //Home 함수실행시 최초 한번 실행되지만 books가 상태값이 변경되도 실행된다.
  //그러니까 마지막에 빈배열을 함께 넘겨줘야 한다.
  useEffect(() => {
    signup();

    fetch(host + '/book', {
      headers: {
        Authorization: token,
      },
    })
      .then((res) =>
        res.json().then((json) => {
          console.log(1, res); //response 객체
          console.log(2, json); //response의 body 부분

          if (res.status === 200) {
            setBooks(json); //books 객체에 결과값을 담는다
          } else if (res.status === 400 || res.status === 403) {
            alert('토큰이 없거나 만료되었습니다! 다시 로그인하세요.');
            localStorage.removeItem('accessToken'); //만료된 토큰 삭제
            navigate('/loginForm');
          }
        }),
      )
      .catch((error) => {
        console.log('error:' + error);
        navigate('/loginForm');
      }); //비동기함수(이 작업이 끝나길 기다리지 않구 다른 작업 하다가 이 작업이 끝나면 받아서 처리하는 방식)
  }, []); //빈배열을 넣어 한번만 수행되도록 한다.*중요*

  const saveForm = () => {
    navigate('/saveForm');
  };

  return (
    <div>
      <h1>도서관리</h1>
      <hr />
      <Stack gap={2}>
        <Button variant="primary" className="ms-auto" onClick={saveForm}>
          도서등록
        </Button>
        <div className="d-grid gap-2"></div>
      </Stack>
      {books.length > 0 &&
        books.map((book) => <BookItem key={book.id} book={book} />)}
    </div>
  );
};

export default BookList;
