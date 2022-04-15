import React from 'react';
import { Container, Nav, Navbar } from 'react-bootstrap';
import { Link } from 'react-router-dom';

const Header = () => {
  return (
    <>
      <Navbar bg="dark" variant="dark">
        <Container>
          <Link to="/" className="navbar-brand">
            홈
          </Link>
          <Nav className="me-auto">
            <Link to="/loginForm" className="nav-link">
              로그인
            </Link>
            <Link to="/userList" className="nav-link">
              회원관리
            </Link>
            <Link to="/bookList" className="nav-link">
              도서관리
            </Link>
          </Nav>
        </Container>
      </Navbar>
      <br />
    </>
  );
};

export default Header;
