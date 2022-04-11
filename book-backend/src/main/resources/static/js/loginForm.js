//로그인 버튼 누르면
// document.querySelector("#btn-submit").addEventListener("click", (e) => {
//   let user = {
//     username: document.querySelector("#userId").value,
//     password: document.querySelector("#userPwd").value,
//   };

//   getLogin(user);
// });

//로그인 가져오기
//async function 함수명(){ await fetch(배동기메소드명);} : 비동기메소드 호출해서 받은 값을 함수에 할당한다.
//이 순서를 지켜주기 위해 사용한다.
//와 돌겠네...   application/json 를 application/x-www-form-urlencoded 로 해야지 시큐리티로그인에서 파라미터 받아진다.
let getLogin = async (user) => {
  console.log(1, user);

  let response = await fetch("http://localhost:8080/login", {
    method: "POST",
    headers: {
      "Content-Type": "application/x-www-form-urlencoded; charset=utf-8",
    },
    body: JSON.stringify(user),
  });
  let responsePasing = await response.json();
  console.log(2, responsePasing);
};

//구글로그인 버튼 누르면
// document.querySelector("#btn-google-login").addEventListener("click", (e) => {
//   fetch("http://localhost:8080/login/oauth2/code/google", {
//       method: "POST",
//       headers: {
//         "Content-Type": "application/x-www-form-urlencoded; charset=utf-8",
//       },
//       body: JSON.stringify(user),
//     });
//     let responsePasing = await response.json();
//     console.log(2, responsePasing);
// });
