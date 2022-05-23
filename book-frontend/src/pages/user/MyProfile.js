import React, { useEffect, useState } from 'react';
import { Button, Col, Container, Row, Table } from 'react-bootstrap';
import { useParams, useNavigate } from 'react-router-dom'; //react-router-dom버전6 이후부터 props.history.push 사용 못한다
import { callApi } from '../../js/ApiService';

const MyProfile = () => {
  const params = useParams(); //그냥 useParams()로 받으면 오브젝트이다.
  const navigate = useNavigate();
  const [user, setUser] = useState({
    id: '',
    userId: '',
    userName: '',
    userPwd: '',
    provider: '',
    prividerId: '',
    role: '',
    update: '',
  });
  console.log('user detail', params);

  useEffect(() => {
    callApi('/api/user/' + params.id, 'GET')
      .then((res) => {
        //응답이 정상이면 여기로 리턴받을수 있다
        setUser(res); //users 객체에 결과값을 담는다
      })
      .catch((err) => {
        console.log('err: ' + err);
      });
  }, []);

  const updateUser = () => {
    // props.history.push('/updateForm/' + id);
    navigate('/updProfile/' + params.id);
  };

  return (
    <div>
      <h1>나의 정보</h1>
      <hr />
      <div className="d-flex justify-content-end">
        <Button variant="primary" onClick={updateUser}>
          수정
        </Button>
      </div>
      <br></br>
      <Container fluid="sm">
        <Row className="bg-light">
          <Col sm={3} md={2} className="p-2">
            아이디
          </Col>
          <Col sm={9} md={10} className="p-2">
            {user.userId}
          </Col>
        </Row>
        <Row>
          <Col sm={3} md={2} className="p-2">
            성명
          </Col>
          <Col sm={9} md={10} className="p-2">
            {user.userName}
          </Col>
        </Row>
        <Row className="bg-light">
          <Col sm={3} md={2} className="p-2">
            OAuth2
          </Col>
          <Col sm={9} md={10} className="p-2">
            {user.provider}
          </Col>
        </Row>
        <Row>
          <Col sm={3} md={2} className="p-2">
            OAuth2 ID
          </Col>
          <Col sm={9} md={10} className="p-2">
            {user.providerId}
          </Col>
        </Row>
        <Row className="bg-light">
          <Col sm={3} md={2} className="p-2">
            권한
          </Col>
          <Col sm={9} md={10} className="p-2">
            {user.role}
          </Col>
        </Row>
      </Container>
    </div>
  );
};

export default MyProfile;
