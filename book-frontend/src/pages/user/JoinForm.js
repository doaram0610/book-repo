import { useState } from 'react';
import { Button, Form } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import { API_BASE_URL } from '../../js/ApiConfig';

const JoinForm = () => {
  const host = API_BASE_URL + '/api';
  const navigate = useNavigate();

  const [user, setUser] = useState({
    userId: '',
    userPwd: '',
    userName: '',
    role: '',
  });

  const changeValue = (e) => {
    setUser({
      ...user,
      [e.target.name]: e.target.value,
    });
  };

  const userList = () => {
    navigate('/userList');
  };

  const submitUser = (e) => {
    e.preventDefault();

    fetch(host + '/user', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json; charset=utf-8',
      },
      body: JSON.stringify(user),
    })
      .then((res) => {
        console.log(res);
        if (res.status === 200) {
          return res.json();
        } else {
          return null;
        }
      })
      .then((res) => {
        if (res !== null) {
          navigate('/userList');
        } else {
          alert('사용자 등록에 실패하였습니다.');
        }
      });
  };

  return (
    <Form onSubmit={submitUser}>
      <h1>회원등록</h1>
      <hr />
      <Form.Group className="mb-3" controlId="formBasicId">
        <Form.Label>아이디</Form.Label>
        <Form.Control
          type="text"
          placeholder="아이디를 입력하세요"
          onChange={changeValue}
          name="userId"
        />
      </Form.Group>
      <Form.Group className="mb-3" controlId="formBasicPasssword">
        <Form.Label>암호</Form.Label>
        <Form.Control
          type="text"
          placeholder="암호를 입력하세요"
          onChange={changeValue}
          name="userPwd"
        />
      </Form.Group>
      <Form.Group className="mb-3" controlId="formBasicName">
        <Form.Label>성명</Form.Label>
        <Form.Control
          type="text"
          placeholder="성명를 입력하세요"
          onChange={changeValue}
          name="userName"
        />
      </Form.Group>
      <Form.Group className="mb-3" controlId="formBasicRole">
        <Form.Label>권한</Form.Label>
        <Form.Select name="role" onChange={changeValue}>
          <option value="USER">USER</option>
          <option value="MANAGER">MANAGER</option>
          <option value="ADMIN">ADMIN</option>
        </Form.Select>
      </Form.Group>
      <Button variant="secondary" onClick={userList}>
        목록
      </Button>{' '}
      <Button variant="primary" type="submit">
        저장
      </Button>
    </Form>
  );
};

export default JoinForm;
