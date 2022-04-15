import React from 'react';
import { Container } from 'react-bootstrap';
import { Route, Routes } from 'react-router-dom';
import Header from './components/Header';
import Home from './pages/book/Home';
import SaveForm from './pages/book/SaveForm';
import Detail from './pages/book/Detail';
import UpdateForm from './pages/book/UpdateForm';
import JoinForm from './pages/user/JoinForm';
import LoginForm from './pages/user/LoginForm';
import UserList from './pages/user/UserList';
import BookList from './pages/book/BookList';
import UserDetail from './pages/user/UserDetail';
import UserUpdateForm from './pages/user/UserUpdateForm';

function App() {
  return (
    <div>
      <Header />
      <Container>
        <Routes>
          <Route path="/" element={<Home />}></Route>
          <Route path="/saveForm" element={<SaveForm />} />
          <Route path="/book/:id" element={<Detail />} />
          <Route path="/joinForm" element={<JoinForm />} />
          <Route path="/loginForm" element={<LoginForm />} />
          <Route path="/updateForm/:id" element={<UpdateForm />} />
          <Route path="/userList" element={<UserList />} />
          <Route path="/bookList" element={<BookList />} />
          <Route path="/user/:id" element={<UserDetail />} />
          <Route path="/userUpdateForm/:id" element={<UserUpdateForm />} />
        </Routes>
      </Container>
    </div>
  );
}

export default App;
