import React, { useEffect, useState } from 'react';
import {
  Button,
  Col,
  Container,
  Form,
  Row,
  Stack,
  Table,
} from 'react-bootstrap';
import { useParams, useNavigate } from 'react-router-dom'; //react-router-dom버전6 이후부터 props.history.push 사용 못한다
import BorrowItem from '../../components/BorrowItem';
import { callApi } from '../../js/ApiService';

const MyBorrow = () => {
  const params = useParams(); //그냥 useParams()로 받으면 오브젝트이다.
  const navigate = useNavigate();
  const [borrows, setBorrows] = useState([]); //DB에서 가져온 대출목록
  const [checkBorrows, setCheckBorrows] = useState(new Set()); //체크된항목담기
  const [isAllChecked, setIsAllChecked] = useState(false); //체크박스전체선택
  const [bChecked, setChecked] = useState(false);

  //stat변수에 추가 또는 삭제
  const checkedBorrowHandler = (borrowId, isChecked) => {
    console.log('checkedBorrowHandler: ' + borrowId + '-' + isChecked);

    if (isChecked) {
      checkBorrows.add(borrowId);
      setCheckBorrows(checkBorrows);
    } else if (!isChecked && checkBorrows.has(borrowId)) {
      checkBorrows.delete(borrowId);
      setCheckBorrows(checkBorrows);
    }

    console.log('checkBorrows: ' + JSON.stringify([...checkBorrows]));
  };

  //체크박스 선택시 호출- 이걸 통해서 해야 되네... ??
  const checkHandler = ({ target }) => {
    setChecked(!bChecked);
    allCheckedHandler(target.checked);
  };

  //전체선택
  const allCheckedHandler = (isChecked) => {
    if (isChecked) {
      setCheckBorrows(new Set(borrows.map(({ borrowId }) => borrowId)));
      setIsAllChecked(true);
    } else {
      checkBorrows.clear();
      setCheckBorrows(setCheckBorrows);
      setIsAllChecked(false);
    }
  };

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
  const returnBook = () => {
    callApi('/api/borrow/' + params.userId, 'DELETE', [...checkBorrows])
      .then((res) => {
        //응답이 정상이면 여기로 리턴받을수 있다
        alert('대출도서 반납이 완료되었습니다.');
        setBorrows(res);
      })
      .catch((err) => {
        console.log('err: ' + err);
      });
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
            <th>
              <Form.Check
                checked={bChecked}
                onChange={(e) => checkHandler(e)}
              />
            </th>
          </tr>
        </thead>
        <tbody>
          {borrows.length > 0 &&
            borrows.map((borrow, index) => (
              <BorrowItem
                key={borrow.borrowId}
                borrowbook={borrow}
                isAllChecked={isAllChecked}
                checkedBorrowHandler={checkedBorrowHandler}
                index={index + 1}
              />
            ))}
        </tbody>
      </Table>
    </>
  );
};

export default MyBorrow;
