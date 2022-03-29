import React, { useEffect, useState } from 'react';
import { Button, Form } from 'react-bootstrap';
import { useNavigate, useParams } from 'react-router-dom';
import * as global_variables from '../../js/global_variables';

const UpdateForm = () => {
  const navigate = useNavigate();
  const params = useParams();
  const host = global_variables.BACK_BASE_URL;

  //아래 input 값에 입력된 값을 보관하는 상수
  const [book, setBook] = useState({
    title: '',
    author: '',
  });

  //백앤드 api 호출
  useEffect(() => {
    fetch(host + '/book/' + params.id)
      .then((res) => res.json())
      .then((res) => {
        setBook(res); //받은 결과를 저장
      });
  }, []);

  const changeValue = (e) => {
    setBook({
      ...book,
      [e.target.name]: e.target.value,
    });
  };

  const submitBook = (e) => {
    e.preventDefault(); //submit 이 action을 안타고 자기 할일을 그만함
    fetch(host + '/book/' + params.id, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json; charset=utf-8',
      },
      body: JSON.stringify(book), //json 객체로 만든다
    })
      .then((res) => {
        console.log(1, res);
        if (res.status === 200) {
          return res.json();
        } else {
          return null;
        }
      })
      .then((res) => {
        console.log(2, res);
        if (res !== null) {
          navigate('/book/' + params.id); //v6 이하버전에서는 props.history 를 썼다
        } else {
          alert('책 등록에 실패하였습니다.');
        }
      }); //catch 는 여기서 오류나야 실행된다
  };

  return (
    <Form onSubmit={submitBook}>
      <Form.Group className="mb-3" controlId="formBasicEmail">
        <Form.Label>Title</Form.Label>
        <Form.Control
          type="text"
          placeholder="Enter Title"
          onChange={changeValue}
          name="title"
          value={book.title}
        />
      </Form.Group>

      <Form.Group className="mb-3" controlId="formBasicEmail">
        <Form.Label>Author</Form.Label>
        <Form.Control
          type="text"
          placeholder="Enter Author"
          onChange={changeValue}
          name="author"
          value={book.author}
        />
      </Form.Group>

      <Button variant="primary" type="submit">
        수정
      </Button>
    </Form>
  );
};

export default UpdateForm;
