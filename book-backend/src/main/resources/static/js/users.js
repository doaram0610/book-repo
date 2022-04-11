//조회하기 버튼 누르면
document.querySelector("#btn-submit").addEventListener("click", (e) => {
  let search = document.querySelector("#search").value;
  let searchval = document.querySelector("#searchval").value;
  let author = "";
  let title = "";

  if (search === "author") {
    author = searchval;
  } else if (search === "title") {
    title = searchval;
  }
  getBooks(author, title);
});

//목록 가져오기
let getBooks = async (author, title) => {
  let response = await fetch(`http://localhost:8080/manager/user`);
  let responsePasing = await response.json();
  // console.log(responsePasing);
  setBooks(responsePasing);
};

//목록 뿌리기
setBooks = (responsePasing) => {
  let tbodyBooksDom = document.querySelector("#tbody-books");
  tbodyBooksDom.innerHTML = "";

  //반복하면서 한행씩 그려준다.
  responsePasing.forEach((e) => {
    let trEl = document.createElement("tr");
    let tdEL1 = document.createElement("td");
    let tdEL2 = document.createElement("td");
    let tdEL3 = document.createElement("td");

    tdEL1.innerHTML = e.userId;
    tdEL2.innerHTML = e.userPwd;
    tdEL3.innerHTML = e.role;

    trEl.append(tdEL1);
    trEl.append(tdEL2);
    trEl.append(tdEL3);

    tbodyHospitalDom.append(trEl);
  });
};
