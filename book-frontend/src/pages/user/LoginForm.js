import React, { useState } from 'react';
import { Button, Form } from 'react-bootstrap';
import { useDispatch } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import { API_BASE_URL } from '../../js/ApiConfig';
import { userLogin } from '../../js/ApiService';

const LoginForm = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();

  //화면에 입력한 값
  const [login, setLogin] = useState({
    userId: '',
    userPwd: '',
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

    //백앤드호출해서 로그인시키자, 주소는 고정이다.
    fetch(API_BASE_URL + '/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json; charset=utf-8',
      },
      body: JSON.stringify(login), //화면에서 입력받은 아이디,암호 값
    })
      .then((res) => {
        console.log(1, res);

        if (res.status === 200) {
          alert('로그인 성공!');
          localStorage.setItem(
            'Authorization',
            res.headers.get('Authorization'),
          );
          // localStorage.setItem('accessToken', res.data.data.accessToken)
          // localStorage.setItem('refreshToken', res.data.data.refreshToken)
          // localStorage.setItem('expiredTime', res.data.data.cur_time)
          // axios.defaults.headers.common['x-access-token'] = res.data.data.accessToken
        } else {
          alert('로그인에 실패하였습니다.');
          // return null;
        }
        return res.json();
      })
      .then((json) => {
        console.log('로그인 성공', json);
        dispatch(userLogin(json.data));
        navigate('/'); //루트로 이동
      })
      .catch((err) => {
        console.log('LoginForm Error:', err);
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
          name="userId"
          onChange={changeLogin}
        />
      </Form.Group>

      <Form.Group className="mb-3">
        <Form.Label>암호</Form.Label>
        <Form.Control
          type="password"
          placeholder="Password"
          name="userPwd"
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
