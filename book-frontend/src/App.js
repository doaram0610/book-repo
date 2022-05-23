import React from 'react';
import { Container } from 'react-bootstrap';
import { Route, Routes } from 'react-router-dom';
import Header from './components/Header';
import Home from './pages/Home';
import SaveForm from './pages/book/SaveForm';
import Detail from './pages/book/BookDetail';
import UpdateForm from './pages/book/UpdateForm';
import JoinForm from './pages/user/JoinForm';
import LoginForm from './pages/user/LoginForm';
import UserList from './pages/user/UserList';
import BookList from './pages/book/BookList';
import UserDetail from './pages/user/UserDetail';
import UserUpdateForm from './pages/user/UserUpdateForm';
import MyProfile from './pages/user/MyProfile';
import UpdProfile from './pages/user/UpdProfile';
import MyBorrow from './pages/user/MyBorrow';

function App() {
  return (
    <div>
      <Header />
      <Container>
        <Routes>
          <Route path="/" element={<Home />}></Route>

          <Route path="/saveForm" element={<SaveForm />} />
          <Route path="/book/:id" element={<Detail />} />
          <Route path="/bookList" element={<BookList />} />
          <Route path="/updateForm/:id" element={<UpdateForm />} />

          <Route path="/myProfile/:id" element={<MyProfile />} />
          <Route path="/updProfile/:id" element={<UpdProfile />} />
          <Route path="/myBorrow/:userId" element={<MyBorrow />} />
          <Route path="/loginForm" element={<LoginForm />} />

          <Route path="/joinForm" element={<JoinForm />} />
          <Route path="/userList" element={<UserList />} />
          <Route path="/userUpdateForm/:id" element={<UserUpdateForm />} />
          <Route path="/user/:id" element={<UserDetail />} />
          <Route path="/userUpdateForm/:id" element={<UserUpdateForm />} />
        </Routes>
      </Container>
    </div>
  );
}

export default App;
