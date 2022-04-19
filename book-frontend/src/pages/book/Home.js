import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import BookItem from '../../components/BookItem';
import { API_BASE_URL } from '../../js/ApiConfig';
import { signup } from '../../js/ApiService';

const Home = () => {
  const [books, setBooks] = useState([]);
  const navigate = useNavigate();
  const host = API_BASE_URL + '/api';
  const token = localStorage.getItem('accessToken');

  //Home 함수실행시 최초 한번 실행되지만 books가 상태값이 변경되도 실행된다.
  //그러니까 마지막에 빈배열을 함께 넘겨줘야 한다.
  useEffect(() => {
    console.log(1, 'token = ' + token);
    signup();

    fetch(host + '/book', {
      headers: {
        Authorization: token, //토큰값이 없으면 에러나니까 이거 처리해 보자
      },
    })
      .then((res) => {
        res.json().then((json) => {
          console.log(2, res); //response 객체 전체
          console.log(3, json); //response객체중 body(데이터영역)
          if (res.status === 200) {
            setBooks(json); //books 객체에 결과값을 담는다
          } else if (res.status === 400 || res.status === 403) {
            alert('토큰이 없거나 만료되었습니다! 다시 로그인하세요.');
            localStorage.removeItem('accessToken'); //만료된 토큰 삭제
            navigate('/loginForm');
          }
        });
      })
      .catch((error) => {
        console.log('error:' + error);
      }); //비동기함수(이 작업이 끝나길 기다리지 않구 다른 작업 하다가 이 작업이 끝나면 받아서 처리하는 방식)
  }, []); //빈배열을 넣어 한번만 수행되도록 한다.*중요*

  return (
    <div>
      <h1>홈</h1>
      <hr />
      {books.map((book) => (
        <BookItem key={book.id} book={book} />
      ))}
    </div>
  );
};

export default Home;
