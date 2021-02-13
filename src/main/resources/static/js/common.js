// var URL  = "https://guardapi.co.uk/api/v1/"
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

common()