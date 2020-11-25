var URL  = "http://localhost:5000/api/v1/"

function common() {
    let userName = document.getElementsByClassName("user-name")
    let firstName = sessionStorage.getItem("first_name")
    let lastName = sessionStorage.getItem("last_name")
    userName[0].innerText = firstName + " " + lastName
}

function directions() {
    let xhr = new XMLHttpRequest();
    let url = URL + "admin/Directions"
  
    xhr.open("GET", url, true);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            window.location.href = URL + "admin/Directions"
        }
    };

    xhr.send();
}

function exit() {
    sessionStorage.clear();
    document.cookie = "jwt=; expires=Thu, 01-Jan-70 00:00:01 GMT;"
}

common()