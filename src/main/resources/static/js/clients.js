var buttonText = `<a href="#editClient" rel="modal:open">
<img src="/img/pencil2.svg" class="edit-icon">
</a>`

var saveButton = document.getElementById("saveButton")

var editId = -1;

saveButton.addEventListener('click', saveButtonSumbit)

var list = function() {
    console.log("begin")
    let token = sessionStorage.getItem("token")

    let xhr = new XMLHttpRequest();
    let url = URL + "admin/clients?page=0&pageSize=1000";
    xhr.open("GET", url, true);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.setRequestHeader("Authorization", `Bearer_${token}`)
    console.log("sending request")
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            console.log("request successful")
            let json = JSON.parse(xhr.responseText);
            showClients(json)

            const editButton = document.getElementsByClassName("edit-icon")
            const pushButton = document.getElementsByClassName("push-icon")
            const deleteButton = document.getElementsByClassName("delete-icon")

            for (let svg of editButton) {
                svg.addEventListener('click', editEvent)
            }
            for (let svg of pushButton) {
                svg.addEventListener('click', pushEvent)
            }
            for (let svg of deleteButton) {
                svg.addEventListener('click', deleteEvent)
            }

            function editEvent() {
                editId = this.parentNode.parentNode.parentNode.childNodes[0].firstChild
                setAllFields()
                console.log(editId)
            }

            function pushEvent() {
                editId = this.parentNode.parentNode.parentNode.childNodes[0].firstChild
                console.log(editId)
            }

            function deleteEvent() {
                editId = this.parentNode.parentNode.parentNode.childNodes[0].firstChild
                console.log(editId)
                deleteUser(editId,"admin/Clients")
            }

            function setAllFields() {
                let fields = document.getElementsByClassName("modal-input")
                let counter = 1
                for (let field of fields) {
                    field.value = getNeededSibling(counter)
                    counter++;
                }
            }

            function getNeededSibling(i) {
                let res = editId.parentNode
                for (let j = 0; j < i; ++j) {
                    res = res.nextSibling
                }
                return res.textContent
            }
        }
    };

    function showClients(json) {
        console.log("In func")
        let table = document.getElementById("clientTable")
        json.clients.forEach(client => {
            let row = table.insertRow(-1);

            let id = row.insertCell(0);
            let name = row.insertCell(1);
            let surName = row.insertCell(2);
            let email = row.insertCell(3);
            let phone = row.insertCell(4);
            let cityCode = row.insertCell(5);
            let countryCode = row.insertCell(6);
            let rating = row.insertCell(7);
            let actions = row.insertCell(8);

            id.innerHTML = client.id
            name.innerHTML = client.firstName
            surName.innerHTML = client.lastName
            email.innerHTML = client.email
            phone.innerHTML = client.phoneNumber
            cityCode.innerHTML = client.cityCode
            countryCode.innerHTML = client.countryCode
            rating.innerHTML = client.averageRate
            actions.innerHTML = buttonPush + buttonText + buttonDeleteText
        });
    }
    xhr.send()
}

function collectAllFields() {
    let fields = document.getElementsByClassName("modal-input")

    let json = JSON.stringify({"id": parseInt(editId.textContent),"firstName": fields[0].value, "lastName": fields[1].value, "email": fields[2].value, "phoneNumber": fields[3].value, 
    "countryCode": [fields[4].value], "cityCode": [fields[5].value], "photo": null})
    console.log(json)
    return json
}

function saveButtonSumbit() {
    let token = sessionStorage.getItem("token")

    console.log(editId)

    let xhr = new XMLHttpRequest();
    let url = URL + "client/edit";
    xhr.open("POST", url, true);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.setRequestHeader("Authorization", `Bearer_${token}`);

    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4) {
            if(xhr.status === 200) {
                alert("Saved")
                getPage(URL + "admin/Clients")
            } else {
                alert("Error")
            }
        }
    }

    let json = collectAllFields();
    xhr.send(json)
}

list()