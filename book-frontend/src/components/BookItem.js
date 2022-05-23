import React, { useState } from 'react';
import { Accordion, Button, Card } from 'react-bootstrap';
import { useSelector } from 'react-redux';
import { Link, useNavigate } from 'react-router-dom';
import { callApi } from '../js/ApiService';

const BookItem = (props) => {
  const { id, title, author, quantity, borrow } = props.book;
  const { isLogin, user } = useSelector((store) => store); //store에 등록된 변수값을 가져온다.
  const navigate = useNavigate();

  //대출신청
  const borrowBook = (e, borrowBook) => {
    e.target.disabled = true;

    callApi('/api/borrow', 'POST', borrowBook)
      .then((json) => {
        alert('대출신청이 완료되었습니다.');
        navigate('/bookList');
      })
      .catch((error) => {
        console.log('BorrowBook Error', error);
      });
  };

  return (
    // <Card>
    //   <Card.Body>
    //     <Card.Title>{title}</Card.Title>
    //     <Card.Text className="text-right">
    //       [{borrow}/{quantity}]
    //     </Card.Text>
    //     {/* <Button variant="primary">Go somewhere</Button> */}
    //     <Link to={'/book/' + id} className="btn btn-primary">
    //       상세보기
    //     </Link>
    //   </Card.Body>
    // </Card>

    <Accordion.Item eventKey={id}>
      <Accordion.Header>{title}</Accordion.Header>
      <Accordion.Body>
        <Card>
          <Card.Body>
            <Card.Title>{title}</Card.Title>
            <Card.Subtitle className="mb-2 text-muted">{author}</Card.Subtitle>
            <Card.Text className="text-right">
              {quantity} / {borrow}
            </Card.Text>
            <Button
              variant={quantity > 0 ? 'primary' : 'secondary'}
              onClick={(e) => {
                borrowBook(e, { bookId: id, userId: user.userId });
              }}
              disabled={quantity > 0 ? false : true}
            >
              대출신청
            </Button>
          </Card.Body>
        </Card>
      </Accordion.Body>
    </Accordion.Item>
  );
};

export default BookItem;
