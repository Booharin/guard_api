const button = document.querySelector('.sign-in-button');
const loginInput = document.querySelector('#login');

button.addEventListener('click', signIn);

function signIn() {
  let login = document.sendForm.login;
  let password = document.sendForm.password;

  let xhr = new XMLHttpRequest();
  let url = URL + "auth/login";
  console.log(url)
  xhr.open("POST", url, true);
  xhr.setRequestHeader("Content-Type", "application/json");
  xhr.onreadystatechange = function () {
      if (xhr.readyState === 4 && xhr.status === 200) {
        let json = JSON.parse(xhr.responseText);
          console.log(json.token + ", " + json.user);
          document.cookie = "jwt=" + json.token
          sessionStorage.setItem("token", json.token);
          sessionStorage.setItem("first_name", json.user.firstName);
          sessionStorage.setItem("last_name", json.user.lastName);
          directions()
      }
  };
  let data = JSON.stringify({"userEmail": login.value, "userPassword": password.value});
  xhr.send(data);
}

