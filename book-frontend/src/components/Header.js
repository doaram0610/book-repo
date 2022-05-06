import React from 'react';
import { Container, Nav, Navbar } from 'react-bootstrap';
import { useDispatch, useSelector } from 'react-redux';
import { Link, useNavigate } from 'react-router-dom';
import { API_BASE_URL } from '../js/ApiConfig';
import { userLogout } from '../js/ApiService';

const Header = () => {
  const navigate = useNavigate(); //등록된 라우터 값으로 이동하기
  const { isLogin, user } = useSelector((store) => store); //store에 등록된 변수값을 가져온다.
  const dispatcher = useDispatch(); //store에 등록된 변수값을 변경하겠다.
  const logout = () => {
    alert('로그아웃됩니다.');
    dispatcher(userLogout()); //store에 등록된 변수값을 변경하겠다.
    localStorage.removeItem('Authorization'); //로컬저장소에 등록된 토큰을 삭제한다.
    navigate('/loginForm');
  };
  const goManager = () => {
    let myForm = document.myForm;
    window.open('', 'newWin', '');
    myForm.auth.value = localStorage.getItem('Authorization');
    myForm.action = API_BASE_URL + '/index';
    myForm.target = 'newWin';
    myForm.submit();
  };

  return (
    <div>
      <form name="myForm" method="POST">
        <input type="hidden" name="auth"></input>
      </form>
      <Navbar collapseOnSelect expand="lg" bg="dark" variant="dark">
        <Container>
          <Link to="/" className="navbar-brand">
            홈
          </Link>
          <Navbar.Toggle aria-controls="responsive-navbar-nav" />
          {isLogin === true ? (
            <Navbar.Collapse id="responsive-navbar-nav">
              <Nav className="me-auto">
                <Link to="/userList" className="nav-link">
                  회원관리
                </Link>
                <Link to="/bookList" className="nav-link">
                  도서관리
                </Link>
                {user.role === 'ROLE_ADMIN' && (
                  <Link to="#" onClick={goManager} className="nav-link">
                    관리자메뉴
                  </Link>
                )}
              </Nav>
              <Nav>
                <Link to="#" onClick={logout} className="nav-link">
                  로그아웃
                </Link>
              </Nav>
            </Navbar.Collapse>
          ) : (
            <Navbar.Collapse id="responsive-navbar-nav">
              <Nav className="me-auto"></Nav>
              <Nav>
                <Link to="/loginForm" className="nav-link">
                  로그인
                </Link>
              </Nav>
            </Navbar.Collapse>
          )}
        </Container>
      </Navbar>
    </div>
  );
};

export default Header;
