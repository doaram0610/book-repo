import React, { useState } from 'react';
import { Button, Form } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import * as global_variables from '../../js/global_variables';

const LoginForm = () => {
  const navigate = useNavigate();
  const host = global_variables.BACK_BASE_URL;

  //화면에 입력한 값
  const [login, setLogin] = useState({
    username: '',
    password: '',
  });

  //아이디, 암호값 변경시
  const changeLogin = (e) => {
    setLogin({
      ...login,
      [e.target.name]: e.target.value,
    });
  };

  //로그인버튼 클릭시
  const submitLogin = (e) => {
    e.preventDefault(); //submit 이 action을 안타고 자기 할일을 그만함

    //백앤드호출해서 로그인시키자
    fetch(host + '/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json; charset=utf-8',
      },
      body: JSON.stringify(login), //화면에서 입력받은 아이디,암호 값
    })
      .then((res) => {
        console.log(1, res);
        if (res.status === '200') {
          alert('로그인 성공!');
          return res.json();
        } else {
          return null;
        }
      })
      .then((res) => {
        console.log(2, res);
        if (res !== null) {
          navigate('/'); //루트로 이동
        } else {
          alert('로그인에 실패하였습니다.');
        }
      });
  };

  //화면에 그려줄 내용
  return (
    <Form onSubmit={submitLogin}>
      <Form.Group className="mb-3">
        <Form.Label>아이디</Form.Label>
        <Form.Control
          type="text"
          placeholder="아이디를 입력하세요"
          name="username"
          onChange={changeLogin}
        />
      </Form.Group>

      <Form.Group className="mb-3">
        <Form.Label>암호</Form.Label>
        <Form.Control
          type="password"
          placeholder="Password"
          name="password"
          onChange={changeLogin}
        />
      </Form.Group>
      <Button variant="primary" type="submit">
        로그인
      </Button>
    </Form>
  );
};

export default LoginForm;
