import { API_BASE_URL } from './ApiConfig';

export async function callApi(api, method, request) {
  let options = {
    headers: new Headers({
      'Content-Type': 'application/json',
      Authorization: localStorage.getItem('Authorization'),
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
        console.log('공통함수(res) : ', res);
        console.log('공통함수(json) : ', json);
        //정상응답이 아니면 응답내용을 catch 로 보내기 위해 아래와 같이 추가
        if (res.status !== 200) {
          return Promise.reject(res);
        }
        //정상이면 그대로 응답 리턴
        return json;
      }),
    )
    .catch((err) => {
      console.log('공통함수(Error) : ', err);
      if (err.status === 403) {
        window.location.href = '/loginForm';
      }
      return Promise.reject(err);
    });

  // await fetch(url, {
  //   method: method,
  //   headers: {
  //     'Content-Type': 'application/json; charset=utf-8',
  //     Authorization: localStorage.getItem('Authorization'),
  //   },
  //   body: JSON.stringify(body),
  // })
  //   .then((res) => {
  //     console.log('공통함수(res) : ', res); //response 객체 전체
  //     console.log('공통함수(res) : ', res.status); //response 객체 전체
  //     console.log('공통함수(res) : ', res.ok); //response 객체 전체
  //     // if (res.ok) {
  //     //   res.json().then((json) => {
  //     //     console.log('공통함수(json) : ', json); //response객체중 body(데이터영역)
  //     //     return json;
  //     //   });
  //     // } else {
  //     //   throw Error(res.json());
  //     // }
  //     return res.json();
  //   })
  //   .then((json) => {
  //     console.log('공통함수(json) : ', json); //response객체중 body(데이터영역)
  //     return json;
  //   });
}
// -------------------------------

const LOGIN = 'LOGIN';
const LOGOUT = 'LOGOUT';

export const userLogin = (user) => {
  return {
    type: LOGIN,
    payload: user,
  };
};

export const userLogout = () => {
  return {
    type: LOGOUT,
  };
};

const initstate = {
  isLogin: false,
  user: {
    userId: '',
    userName: '',
    role: '',
  },
};

const reducer = (state = initstate, action) => {
  switch (action.type) {
    case LOGIN:
      return { isLogin: true, user: action.payload };
    case LOGOUT:
      return {
        isLogin: false,
        user: {
          userId: '',
          userName: '',
          role: '',
        },
      };
    default:
      return state;
  }
};

export default reducer;
