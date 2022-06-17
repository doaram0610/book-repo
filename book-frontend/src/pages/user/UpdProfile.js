import React, { useEffect, useState } from 'react';
import { Button, Col, Container, Form, Row } from 'react-bootstrap';
import { useNavigate, useParams } from 'react-router-dom';
import { callApi } from '../../js/ApiService';

const UpdProfile = () => {
  const navigate = useNavigate();
  const params = useParams();

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

  //로딩 최초 수행
  useEffect(() => {
    //보기
    callApi('/api/user/' + params.id, 'GET')
      .then((res) => {
        //응답이 정상이면 여기로 리턴받을수 있다

        res.userPwd = '';

        console.log(999, res.userPwd);
        setUser(res); //users 객체에 결과값을 담는다
      })
      .catch((err) => {
        console.log('err: ' + err);
      });
  }, []);

  //내용입력변경시 변수값 수정
  const changeValue = (e) => {
    setUser({
      ...user,
      [e.target.name]: e.target.value,
    });
  };

  //수정
  const submitUser = (e) => {
    e.preventDefault(); //submit 이 action을 안타고 자기 할일을 그만함

    alert(user.userName);

    callApi('/api/user/' + params.id, 'PUT', user)
      .then((res) => {
        navigate('/myProfile/' + params.id); //v6 이하버전에서는 props.history 를 썼다
      })
      .catch((err) => {
        console.log('err: ' + err);
        alert('회원 등록에 실패하였습니다.');
      });
  };

  //나의정보 이동
  const myProfile = () => {
    navigate('/myProfile/' + user.id);
  };

  return (
    <>
      <h1>나의 정보</h1>
      <hr />
      <div class="text-right">
        <Button variant="primary" onClick={submitUser}>
          수정
        </Button>{' '}
        <Button variant="secondary" onClick={myProfile}>
          취소
        </Button>
      </div>
      {/* <Container fluid>
        <Row>
          <Col className={'text-right'}>
            <Button variant="primary" onClick={submitUser}>
              수정
            </Button>{' '}
            <Button variant="secondary" onClick={myProfile}>
              취소
            </Button>
          </Col>
        </Row>
      </Container> */}
      <Form>
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
            value={user.provider !== null ? user.provider : ''}
          />
        </Form.Group>
        <Form.Group className="mb-3" controlId="providerId">
          <Form.Label>Provider Id</Form.Label>
          <Form.Control
            type="text"
            placeholder="Enter Provider Id"
            onChange={changeValue}
            name="providerId"
            value={user.providerId !== null ? user.providerId : ''}
          />
        </Form.Group>
        <Form.Group className="mb-3" controlId="role">
          <Form.Label>권한</Form.Label>
          <Form.Select
            name="role"
            onChange={changeValue}
            key={user.role}
            defaultValue={user.role}
          >
            <option value="">- 선택 -</option>
            <option value="ROLE_ADMIN">ADMIN</option>
            <option value="ROLE_MANAGER">MANAGER</option>
            <option value="ROLE_USER">USER</option>
          </Form.Select>
        </Form.Group>
      </Form>
    </>
  );
};

export default UpdProfile;
