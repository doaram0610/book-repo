import React, { useEffect, useState } from 'react';
import { Button } from 'react-bootstrap';
import { useParams, useNavigate } from 'react-router-dom'; //react-router-dom버전6 이후부터 props.history.push 사용 못한다
import { API_BASE_URL } from '../../js/ApiConfig';
import { siginin } from '../../js/ApiService';

const UserDetail = () => {
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
  const host = API_BASE_URL + '/api';

  console.log('user detail', params);

  useEffect(() => {
    fetch(host + '/user/' + params.id)
      .then((res) => res.json())
      .then((res) => {
        setUser(res);
      });
  }, []);

  const deleteUser = () => {
    fetch(host + '/user/' + params.id, {
      method: 'DELETE',
    })
      .then((res) => res.text())
      .then((res) => {
        if (res === 'ok') {
          //props.history.push('/'); //요거 안먹는데 라우터 v6 이후부터 아래처럼 해야됨
          navigate('/userList');
        } else {
          alert('삭제실패');
        }
      });
  };

  const updateUser = () => {
    // props.history.push('/updateForm/' + id);
    navigate('/userUpdateForm/' + params.id);
  };

  const userList = () => {
    navigate('/userList');
  };

  return (
    <div>
      <h1>회원 상세보기</h1>
      <hr />
      <Button variant="warning" onClick={updateUser}>
        수정
      </Button>{' '}
      <Button variant="danger" onClick={deleteUser}>
        삭제
      </Button>{' '}
      <Button variant="secondary" onClick={userList}>
        목록
      </Button>
      <h3>{user.userId}</h3>
      <h1>{user.userName}</h1>
      <h1>{user.provider}</h1>
      <h1>{user.providerId}</h1>
      <h1>{user.role}</h1>
      <h1>{user.update}</h1>
    </div>
  );
};

export default UserDetail;
