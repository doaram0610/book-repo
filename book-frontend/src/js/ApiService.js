import { API_BASE_URL } from './ApiConfig';

//로그인체크
export function signup() {
  const token = localStorage.getItem('accessToken');
  console.log(0, token);

  if (token === null || token === 'null') {
    console.log(99, '토큰없어서 리다이렉트할거야');
    alert('토큰이 없거나 만료되었습니다 다시 로그인하세요.');
    window.location.href = '/loginForm';
  }
}

//로그아웃
export function signout() {
  localStorage.setItem('accessToken', null);
  alert('로그아웃 처리됩니다.');
  window.location.href = '/loginForm';
}

//공통-API 호출
export function callApi(api, method, request) {
  let options = {
    headers: new Headers({
      'Content-Type': 'application/json',
    }),
    url: API_BASE_URL + api,
    method: method,
  };

  //POST방식으로 보내는 파라미터가 있는 경우
  if (request) {
    options.body = JSON.stringify(request);
  }

  //fetch : 비동기함수(이 작업이 끝나길 기다리지 않구 다른 작업 하다가
  //이 작업이 끝나면 받아서 처리하는 방식)
  return fetch(options.url, options)
    .then((res) =>
      res.json().then((json) => {
        console.log(1, res);
        console.log(11, json);
        //정상응답이 아니면 응답내용을 catch 로 보내기 위해 아래와 같이 추가
        if (res.status !== 200) {
          return Promise.reject(res);
        }
        //정상이면 그대로 응답 리턴
        return json;
      }),
    )
    .catch((err) => {
      console.log(2, err);
      if (err.status === 403) {
        window.location.href = '/loginForm';
      }
      return Promise.reject(err);
    });
}
