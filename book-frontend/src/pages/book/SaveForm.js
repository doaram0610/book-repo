import React, { useState } from 'react';
import { Button, Form } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import * as global_variables from '../../js/global_variables';

const SaveForm = () => {
  const navigate = useNavigate();
  const host = global_variables.BACK_BASE_URL;

  //아래 input 값에 입력된 값을 보관하는 상수
  const [book, setBook] = useState({
    title: '',
    author: '',
  });

  const changeValue = (e) => {
    setBook({
      ...book,
      [e.target.name]: e.target.value,
    });
  };

  const bookList = () => {
    navigate('/bookList');
  };
  const submitBook = (e) => {
    e.preventDefault(); //submit 이 action을 안타고 자기 할일을 그만함
    fetch(host + '/book', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json; charset=utf-8',
      },
      body: JSON.stringify(book), //json 객체로 만든다
    })
      .then((res) => {
        console.log(1, res);
        if (res.status === 200) {
          console.log(2, res);
          return res.json();
        } else {
          return null;
        }
      })
      .then((res) => {
        console.log(3, res);
        if (res !== null) {
          console.log(4, res);
          navigate('/'); //props.history.push('/'); //이전버전에서 사용하던 내용
        } else {
          alert('책 등록에 실패하였습니다.');
        }
      }); //catch 는 여기서 오류나야 실행된다
  };

  return (
    <Form onSubmit={submitBook}>
      <h1>도서등록</h1>
      <hr />
      <Form.Group className="mb-3" controlId="formBasicEmail">
        <Form.Label>책제목</Form.Label>
        <Form.Control
          type="text"
          placeholder="Enter Title"
          onChange={changeValue}
          name="title"
        />
      </Form.Group>
      <Form.Group className="mb-3" controlId="formBasicEmail">
        <Form.Label>저자</Form.Label>
        <Form.Control
          type="text"
          placeholder="Enter Author"
          onChange={changeValue}
          name="author"
        />
      </Form.Group>
      <Button variant="primary" type="submit">
        저장
      </Button>{' '}
      <Button variant="secondary" onClick={bookList}>
        목록
      </Button>
    </Form>
  );
};

export default SaveForm;
