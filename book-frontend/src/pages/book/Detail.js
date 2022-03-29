import React, { useEffect, useState } from 'react';
import { Button } from 'react-bootstrap';
import { useParams, useNavigate } from 'react-router-dom'; //react-router-dom버전6 이후부터 props.history.push 사용 못한다
import * as global_variables from '../../js/global_variables';

const Detail = () => {
  const params = useParams(); //그냥 useParams()로 받으면 오브젝트이다.
  const navigate = useNavigate();
  const [book, setBook] = useState({
    id: '',
    title: '',
    author: '',
  });
  const host = global_variables.BACK_BASE_URL;

  console.log('detail', params);

  useEffect(() => {
    fetch(host + '/book/' + params.id)
      .then((res) => res.json())
      .then((res) => {
        setBook(res);
      });
  }, []);

  const deleteBook = () => {
    fetch(host + '/book/' + params.id, {
      method: 'DELETE',
    })
      .then((res) => res.text())
      .then((res) => {
        if (res === 'ok') {
          //props.history.push('/'); //요거 안먹는데 라우터 v6 이후부터 아래처럼 해야됨
          navigate('/');
        } else {
          alert('삭제실패');
        }
      });
  };

  const updateBook = () => {
    // props.history.push('/updateForm/' + id);
    navigate('/updateForm/' + params.id);
  };

  return (
    <div>
      <h1>책 상세보기</h1>
      <Button variant="warning" onClick={updateBook}>
        수정
      </Button>{' '}
      <Button variant="danger" onClick={deleteBook}>
        삭제
      </Button>
      <hr />
      <h3>{book.author}</h3>
      <h1>{book.title}</h1>
    </div>
  );
};

export default Detail;
