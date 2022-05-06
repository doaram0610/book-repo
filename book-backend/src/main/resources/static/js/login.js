let auth = document.loginForm.auth.value;

fetch("https://localhost:5000/manager/books", {
  method: "GET",
  headers: {
    "Content-Type": "application/json",
     "Authorization": auth
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
		console.log(data)
});