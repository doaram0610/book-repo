let auth = document.loginForm.auth.value;

//로컬저장소에 헤더값 저장
localStorage.setItem('Authorization', auth);

alert(localStorage.getItem('Authorization'));

fetch("https://localhost:5000/manager/books", {
  method: "GET",
  headers: {
    "Content-Type": "application/json",
     "Authorization": localStorage.getItem('Authorization')
  },
  /*
  body: JSON.stringify({
    title: "Test",
    body: "I am testing!",
    userId: 1,
  }),*/
})
  .then((response) => response.json())
  .then((data) => {
		console.log(data);
});