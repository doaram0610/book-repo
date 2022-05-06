import { useSelector } from 'react-redux';

const Home = () => {
  const { isLogin, user } = useSelector((store) => store);

  return (
    <div>
      <h1>홈</h1>
      <hr />
      {user.userName}님 환영합니다.
    </div>
  );
};

export default Home;
