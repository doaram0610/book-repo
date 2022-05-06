import React, { useEffect, useState } from 'react';
import { Button, Stack, Table } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import UserItem from '../../components/UserItem';
import { callApi } from '../../js/ApiService';

const UserList = () => {
  const [users, setUsers] = useState([]);
  const navigate = useNavigate();

  const joinUser = () => {
    navigate('/joinForm');
  };

  //Home 함수실행시 최초 한번 실행되지만 books가 상태값이 변경되도 실행된다.
  //그러니까 마지막에 빈배열을 함께 넘겨줘야 한다.
  useEffect(() => {
    callApi('/api/user', 'GET')
      .then((res) => {
        //응답이 정상이면 여기로 리턴받을수 있다
        setUsers(res); //users 객체에 결과값을 담는다
      })
      .catch((err) => {
        console.log('err: ' + err);
      });
  }, []); //빈배열을 넣어 한번만 수행되도록 한다.*중요*

  return (
    <div>
      <h1>회원관리</h1>
      <hr />
      <Stack gap={2}>
        <Button variant="primary" className="ms-auto" onClick={joinUser}>
          회원등록
        </Button>
        <div className="d-grid gap-2"></div>
      </Stack>
      <Table striped bordered hover>
        <thead>
          <tr>
            <th>No</th>
            <th>아이디</th>
            <th>성명</th>
            <th>provider</th>
            <th>provider id</th>
            <th>권한</th>
            <th>등록일</th>
          </tr>
        </thead>
        <tbody>
          {users.length > 0 &&
            users.map((user) => <UserItem key={user.id} user={user} />)}
        </tbody>
      </Table>
    </div>
  );
};

export default UserList;
