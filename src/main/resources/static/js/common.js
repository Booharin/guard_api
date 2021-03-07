//var URL  = "https://guardapi.co.uk/api/v1/"
var URL  = "http://localhost:5000/api/v1/"

function common() {
    let userName = document.getElementsByClassName("user-name")
    let firstName = sessionStorage.getItem("first_name")
    let lastName = sessionStorage.getItem("last_name")
    userName[0].innerText = firstName + " " + lastName
}

function personal() {
    let xhr = new XMLHttpRequest();
    let url = URL + "admin/PersonalCabinet"
    let token = sessionStorage.getItem("token")
  
    xhr.open("GET", url, true);
    xhr.setRequestHeader("Authorization", `Bearer_${token}`)
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                var newDoc = document.open("text/html", "replace");
                newDoc.write(xhr.responseText);
                newDoc.close();
                let title = (/<title>(.*?)<\/title>/m).exec(xhr.responseText)[1]

                document.title = title;
                window.history.pushState({"html":xhr.responseText,"pageTitle":title},"", URL + "admin/PersonalCabinet");
            }
        }
    };

    xhr.send();
}

function exit() {
    sessionStorage.clear();
    document.cookie = "jwt=; expires=Thu, 01-Jan-70 00:00:01 GMT;"
}

var buttonPush = `<a href="#pushToUser" rel="modal:open">
<img src="/img/envelope.svg" class="push-icon" width="30px">
</a>`
var pushButton = document.getElementById("pushButton")
pushButton.addEventListener('click', pushButtonSumbit)

function collectPushData() {
    let field = document.getElementById("pushMessage")

    let json = JSON.stringify({"userId": parseInt(editId.textContent),"message": field.value})
    console.log(json)
    return json
}

function pushButtonSumbit() {
    let token = sessionStorage.getItem("token")

    console.log(editId)

    let xhr = new XMLHttpRequest();
    let url = URL + "users/push";
    xhr.open("POST", url, true);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.setRequestHeader("Authorization", `Bearer_${token}`);

    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4) {
            if(xhr.status === 200) {
                alert("Sended")
                getPage(URL + "admin/Clients")
            } else {
                alert("Error")
            }
        }
    }

    let json = collectPushData();
    xhr.send(json)
}

var buttonDeleteText = `<a>
<img src="/img/trash.svg" class="delete-icon" width="30px">
</a>`

function deleteUser(userId, page) {
    if (!confirm("Удалить пользователя с ID="+userId.textContent+"?"))
        return;

    let token = sessionStorage.getItem("token")

    console.log(editId)

    let xhr = new XMLHttpRequest();
    let url = URL + "users/delete?id="+userId.textContent;
    xhr.open("GET", url, true);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.setRequestHeader("Authorization", `Bearer_${token}`);

    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4) {
            if(xhr.status === 200) {
                alert("Deleted")
                getPage(URL + page)
            } else {
                alert("Error")
            }
        }
    }
    let json = collectAllFields();
    xhr.send(json)
}

common()