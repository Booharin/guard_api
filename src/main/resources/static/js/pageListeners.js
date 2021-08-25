var lawyersHref = document.getElementById("hrefLawyers")
var clientsHref = document.getElementById("hrefClients")
var directionsHref = document.getElementById("hrefDirections")
var messagesHref = document.getElementById("hrefMessages")
var personalHref = document.getElementById("hrefPersonalCabinet")


lawyersHref.addEventListener('click', function() {getPage(URL + "admin/Lawyers")})
clientsHref.addEventListener('click', function() {getPage(URL + "admin/Clients")})
directionsHref.addEventListener('click', function() {getPage(URL + "admin/Directions")})
messagesHref.addEventListener('click', function() {getPage(URL + "admin/Messages")})
personalHref.addEventListener('click', function() {getPage(URL + "admin/PersonalCabinet")})


function getPage(url) {
    let xhr = new XMLHttpRequest();
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
                window.history.pushState({"html":xhr.responseText,"pageTitle":title},"", url);
            }
        }
        if (xhr.status === 401) {
                  alert("Token expired. Please relogin again")
                  logout()
              }
    };

    xhr.send();
}

function logout() {
    exit()
    window.location.href = URL + "admin/index"
}

