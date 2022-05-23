import React, { useEffect, useState } from 'react';
import { Button, Card, Stack } from 'react-bootstrap';
import { useSelector } from 'react-redux';
import { useParams, useNavigate } from 'react-router-dom'; //react-router-dom버전6 이후부터 props.history.push 사용 못한다
import { API_BASE_URL } from '../../js/ApiConfig';
import { callApi, siginin } from '../../js/ApiService';

const BookDetail = () => {
  const { isLogin, user } = useSelector((store) => store); //store에 등록된 변수값을 가져온다.
  const params = useParams(); //그냥 useParams()로 받으면 오브젝트이다.
  const navigate = useNavigate();
  const [book, setBook] = useState({
    id: '',
    title: '',
    author: '',
    quantity: 0,
    borrow: 0,
  });
  const [borrow, setBorrow] = useState({
    userId: user.userId,
    bookId: params.id,
  });

  useEffect(() => {
    callApi('/api/book/' + params.id, 'GET')
      .then((json) => {
        setBook(json);
        // setBorrow({ bookId: json.id, userId: user.userId });
      })
      .catch((error) => {
        console.log('BookDetail Error', error);
      });
  }, []);

  //대출신청
  const borrowBook = (e) => {
    e.target.variant = 'secondary'; //요건 안먹네..
    e.target.disabled = true;

    callApi('/api/borrow', 'POST', borrow)
      .then((json) => {
        setBook(json);
      })
      .catch((error) => {
        console.log('BorrowBook Error', error);
      });
  };

  //목록
  const bookList = () => {
    navigate('/bookList');
  };

  // const deleteBook = () => {
  //   fetch(host + '/book/' + params.id, {
  //     method: 'DELETE',
  //   })
  //     .then((res) => res.text())
  //     .then((res) => {
  //       if (res === 'ok') {
  //         //props.history.push('/'); //요거 안먹는데 라우터 v6 이후부터 아래처럼 해야됨
  //         navigate('/bookList');
  //       } else {
  //         alert('삭제실패');
  //       }
  //     });
  // };

  // const updateBook = () => {
  //   // props.history.push('/updateForm/' + id);
  //   navigate('/updateForm/' + params.id);
  // };

  return (
    <>
      <h1>도서관리</h1>
      {/* <Button variant="warning" onClick={updateBook}>
        수정
      </Button>{' '}
      <Button variant="danger" onClick={deleteBook}>
        삭제
      </Button>{' '}
      <Button variant="secondary" onClick={bookList}>
        목록
      </Button> */}{' '}
      <hr />
      <Stack gap={2}>
        <Button variant="primary" className="ms-auto" onClick={bookList}>
          목록
        </Button>
        <div className="d-grid gap-2"></div>
      </Stack>
      <Card>
        <Card.Body>
          <Card.Title>{book.title}</Card.Title>
          <Card.Subtitle className="mb-2 text-muted">
            {book.author}
          </Card.Subtitle>
          <Card.Text className="text-right">
            {book.quantity} / {book.borrow}
          </Card.Text>
          <Button
            variant={book.quantity > 0 ? 'primary' : 'secondary'}
            onClick={borrowBook}
          >
            대출신청
          </Button>
        </Card.Body>
      </Card>
    </>
  );
};

export default BookDetail;
