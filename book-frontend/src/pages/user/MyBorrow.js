import React, { useEffect, useState } from 'react';
import { Button, Col, Container, Row, Stack, Table } from 'react-bootstrap';
import { useParams, useNavigate } from 'react-router-dom'; //react-router-dom버전6 이후부터 props.history.push 사용 못한다
import BorrowItem from '../../components/BorrowItem';
import { callApi } from '../../js/ApiService';

const MyBorrow = () => {
  const params = useParams(); //그냥 useParams()로 받으면 오브젝트이다.
  const navigate = useNavigate();
  const [borrows, setBorrows] = useState([]);
  const [checkBorrows, setCheckBorrows] = useState([]);

  useEffect(() => {
    callApi('/api/borrow/' + params.userId, 'GET')
      .then((res) => {
        //응답이 정상이면 여기로 리턴받을수 있다
        setBorrows(res); //users 객체에 결과값을 담는다
      })
      .catch((err) => {
        console.log('err: ' + err);
      });
  }, []);

  //반납하기 : 체크박스 확인해서 넘기기 안됨
  const returnBook = (e) => {
    const { name, value } = e.target;
    setCheckBorrows({ ...checkBorrows, [name]: value });

    console.log(checkBorrows);

    // navigate('/bookList');
  };

  return (
    <>
      <h1>대출현황</h1>
      <hr />
      <Stack gap={2}>
        <Button variant="primary" className="ms-auto" onClick={returnBook}>
          반납하기
        </Button>
        <div className="d-grid gap-2"></div>
      </Stack>
      <Table striped bordered hover>
        <thead>
          <tr>
            <th>No</th>
            <th>도서명</th>
            <th>저자</th>
            <th>대출시작일</th>
            <th>대출종료일</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          {borrows.length > 0 &&
            borrows.map((borrow) => (
              <BorrowItem key={borrow.borrowId} borrowbook={borrow} />
            ))}
        </tbody>
      </Table>
    </>
  );
};

export default MyBorrow;
