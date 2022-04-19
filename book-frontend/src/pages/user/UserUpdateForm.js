import React, { useEffect, useState } from 'react';
import { Button, Form } from 'react-bootstrap';
import { useNavigate, useParams } from 'react-router-dom';
import { API_BASE_URL } from '../../js/ApiConfig';
import { siginin } from '../../js/ApiService';

const UserUpdateForm = () => {
  const navigate = useNavigate();
  const params = useParams();
  const host = API_BASE_URL + '/api';

  //아래 input 값에 입력된 값을 보관하는 상수
  const [user, setUser] = useState({
    id: '',
    userId: '',
    userName: '',
    userPwd: '',
    provider: '',
    prividerId: '',
    role: '',
  });

  //백앤드 api 호출
  useEffect(() => {
    fetch(host + '/user/' + params.id)
      .then((res) => res.json())
      .then((res) => {
        setUser(res); //받은 결과를 저장
      });
  }, []);

  const changeValue = (e) => {
    setUser({
      ...user,
      [e.target.name]: e.target.value,
    });
  };

  const submitUser = (e) => {
    e.preventDefault(); //submit 이 action을 안타고 자기 할일을 그만함
    fetch(host + '/user/' + params.id, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json; charset=utf-8',
      },
      body: JSON.stringify(user), //json 객체로 만든다
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
          navigate('/user/' + params.id); //v6 이하버전에서는 props.history 를 썼다
        } else {
          alert('회원 등록에 실패하였습니다.');
        }
      }); //catch 는 여기서 오류나야 실행된다
  };

  const userList = () => {
    navigate('/user/' + user.userId);
  };

  return (
    <Form onSubmit={submitUser}>
      <Form.Group className="mb-3" controlId="userId">
        <Form.Label>아이디</Form.Label>
        <Form.Control value={user.userId} disabled />
      </Form.Group>
      <Form.Group className="mb-3" controlId="userPwd">
        <Form.Label>암호</Form.Label>
        <Form.Control
          type="password"
          placeholder="Enter Password"
          onChange={changeValue}
          name="userPwd"
          value={user.userPwd}
        />
      </Form.Group>
      <Form.Group className="mb-3" controlId="userName">
        <Form.Label>성명</Form.Label>
        <Form.Control
          type="text"
          placeholder="Enter User Name"
          onChange={changeValue}
          name="userName"
          value={user.userName}
        />
      </Form.Group>
      <Form.Group className="mb-3" controlId="provider">
        <Form.Label>Provider</Form.Label>
        <Form.Control
          type="text"
          placeholder="Enter Provider"
          onChange={changeValue}
          name="provider"
          value={user.provider !== null && user.provider}
        />
      </Form.Group>
      <Form.Group className="mb-3" controlId="providerId">
        <Form.Label>Provider Id</Form.Label>
        <Form.Control
          type="text"
          placeholder="Enter Provider Id"
          onChange={changeValue}
          name="providerId"
          value={user.providerId !== null && user.providerId}
        />
      </Form.Group>
      <Form.Group className="mb-3" controlId="role">
        <Form.Label>권한</Form.Label>
        <Form.Control
          type="text"
          placeholder="Enter Role"
          onChange={changeValue}
          name="role"
          value={user.role}
        />
      </Form.Group>
      <Button variant="primary" type="submit">
        수정
      </Button>{' '}
      <Button variant="secondary" onClick={userList}>
        취소
      </Button>
    </Form>
  );
};

export default UserUpdateForm;
