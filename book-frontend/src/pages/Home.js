import { useSelector } from 'react-redux';

const Home = () => {
  const { isLogin, user } = useSelector((store) => store);

  console.log('isLogin', isLogin);
  console.log('user', user);

  return (
    <div>
      <h1>홈</h1>
      <hr />
      {isLogin && user.userName + '님 환영합니다.'}
    </div>
  );
};

export default Home;
