export function signup() {
  const token = localStorage.getItem('accessToken');

  if (token === null) {
    console.log(99, '토큰없어서 리다이렉트할거야');
    alert('토큰이 없거나 만료되었습니다 다시 로그인하세요.');
    window.location.href = '/loginForm';
  }
}
