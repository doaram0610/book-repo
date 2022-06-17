import React, { useEffect, useState } from 'react';
import {
  Accordion,
  Button,
  Col,
  Container,
  Form,
  FormControl,
  InputGroup,
  Row,
  Stack,
} from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import BookItem from '../../components/BookItem';
import { callApi } from '../../js/ApiService';

const BookList = () => {
  const [books, setBooks] = useState([]);
  const navigate = useNavigate();
  const [search, setSearch] = useState('');

  //Home 함수실행시 최초 한번 실행되지만 books가 상태값이 변경되도 실행된다.
  //그러니까 마지막에 빈배열을 함께 넘겨줘야 한다.
  useEffect(() => {
    callApi('/api/book', 'GET')
      .then((json) => {
        console.log('BookList Ok', json);
        setBooks(json);
      })
      .catch((error) => {
        console.log('BookList Error', error);
      });
  }, []); //빈배열을 넣어 한번만 수행되도록 한다.*중요*

  //도서검색
  const searchForm = (e) => {
    e.preventDefault(); //submit 이 action을 안타고 자기 할일을 그만함

    if (search === '') {
      callApi('/api/book', 'GET')
        .then((json) => {
          console.log('searchForm Ok', json);
          setBooks(json);
        })
        .catch((error) => {
          console.log('searchForm ERROR', error);
        });
    } else {
      callApi('/api/bookSearch', 'POST', search)
        .then((json) => {
          console.log('searchForm Ok', json);
          setBooks(json);
        })
        .catch((error) => {
          console.log('searchForm ERROR', error);
        });
    }
  };

  const searchHandler = (e) => {
    // console.log('searchHandler:', e.target.value);
    setSearch(e.target.value);
  };

  return (
    <>
      <h1>도서검색</h1>
      <hr />
      <Form onSubmit={searchForm}>
        <Container>
          <div class="row no-gutters">
            <Row className="align-items-center">
              <Col xs={10}>
                <Form.Label htmlFor="inlineFormInput" visuallyHidden>
                  Name
                </Form.Label>
                <Form.Control
                  className="mb-2"
                  id="inlineFormInput"
                  placeholder="도서명 또는 저자를 입력하세요"
                  onChange={searchHandler}
                />
              </Col>
              <Col xs="auto">
                <Button className="mb-2" type="submit">
                  조회
                </Button>
              </Col>
            </Row>
          </div>
        </Container>
      </Form>
      <Accordion flush>
        {books.length > 0 &&
          books.map((book) => <BookItem key={book.bookId} book={book} />)}
      </Accordion>
    </>
  );
};

export default BookList;
