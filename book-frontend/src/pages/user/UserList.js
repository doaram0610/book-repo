import React, { useEffect, useState } from 'react';
import { Button, Stack, Table } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import UserItem from '../../components/UserItem';
import * as global_variables from '../../js/global_variables';

const UserList = () => {
  const [users, setUsers] = useState([]);
  const host = global_variables.BACK_BASE_URL;
  const navigate = useNavigate();
  const token = localStorage.getItem('accessToken');

  const joinUser = () => {
    navigate('/joinForm');
  };

  //Home 함수실행시 최초 한번 실행되지만 books가 상태값이 변경되도 실행된다.
  //그러니까 마지막에 빈배열을 함께 넘겨줘야 한다.
  useEffect(() => {
    fetch(host + '/user', {
      headers: {
        Authorization: token,
      },
    })
      .then((res) => res.json())
      .then((res) => {
        // console.log(1, res);  클릭했을때 이동이 안되니까 지우자
        setUsers(res); //users 객체에 결과값을 담는다
      }); //비동기함수(이 작업이 끝나길 기다리지 않구 다른 작업 하다가 이 작업이 끝나면 받아서 처리하는 방식)
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
          {users.map((user) => (
            <UserItem key={user.id} user={user} />
          ))}
        </tbody>
      </Table>
    </div>
  );
};

export default UserList;
