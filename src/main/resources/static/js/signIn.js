var button = document.querySelector('.sign-in-button');
var loginInput = document.querySelector('#login');

button.addEventListener('click', signIn);

function signIn() {
  let login = document.sendForm.login;
  let password = document.sendForm.password;

  let xhr = new XMLHttpRequest();
  let url = URL + "auth/login";
  xhr.open("POST", url, true);
  xhr.setRequestHeader("Content-Type", "application/json");
  xhr.onreadystatechange = function () {
      if (xhr.readyState === 4 && xhr.status === 200) {
        let json = JSON.parse(xhr.responseText);
        sessionStorage.setItem("token", json.token);
        sessionStorage.setItem("first_name", json.user.firstName);
        sessionStorage.setItem("last_name", json.user.lastName);
        personal()
      }
  };
  let data = JSON.stringify({"userEmail": login.value, "userPassword": password.value});
  xhr.send(data);
}

