var saveIssueButton = document.getElementById("saveIssue")
var saveSubIssuebutton = document.getElementById("saveSubIssue")
var lawsDiv = document.getElementsByClassName("laws")[0]
var subIssuesBlock = document.getElementById("subIssuesBlock")
var descriptionIssue = document.getElementById("issueDescription")
var descriptionSubIssue = document.getElementById("subIssueDescription")
var descriptionIssueEn = document.getElementById("issueDescriptionEn")
var descriptionSubIssueEn = document.getElementById("subIssueDescriptionEn")
var editIssueButton = document.getElementById("editIssue")
var editSubIssueButton = document.getElementById("editSubIssue")
var editIssueForm = document.getElementById("editIssueForm")
var editSubIssueForm = document.getElementById("editSubIssueForm")
var deleteIssueButton = document.getElementById("deleteIssue")
var deleteSubIssueButton = document.getElementById("deleteSubIssue")

saveIssueButton.addEventListener("click", function () { saveIssue(saveIssueButton) })
saveSubIssuebutton.addEventListener('click', function () { saveSubIssue(saveSubIssuebutton) })
editIssueButton.addEventListener('click', function () { editIssue(editIssueButton) })
editSubIssueButton.addEventListener('click', function () { editSubIssue(editSubIssueButton) })
deleteIssueButton.addEventListener("click", deleteIssue)
deleteSubIssueButton.addEventListener("click", deleteSubIssue)
var issueMap = new Map();
var subIssueMap = new Map();


function findActiveIssue() {
    let block = document.getElementsByClassName("law is-active")[0]
    return issueMap.get(parseInt(block.getAttribute("data-tab-name")))
}

function findActiveSubIssue() {
    let block = document.getElementsByClassName("sub-law is-active")[0]
    return subIssueMap.get(parseInt(block.getAttribute("data-tab-name")))
}

function fillDir() {
    // Set fields logic
    let children = editIssueForm.childNodes

    for (let child of children) {
        if (child.classList != null && child.classList.contains("modal-input")) {
            console.log(child)
            if (child.name === "name-ru") {
                child.value = findActiveIssue().title
            } else if (child.name === "locale") {
                child.value = findActiveIssue().locale
            } else if (child.name === "description-ru") {
                child.innerText = findActiveIssue().subtitle
            } else if (child.name === "issueCode") {
                child.value = findActiveIssue().issueCode
            } else if (child.name === "name-en") {
                child.value = findActiveIssue().titleEn
            } else if (child.name === "description-en") {
                child.value = findActiveIssue().subtitleEn
            } else if (child.name === "image-view")
            {
                child.setAttribute('src', 'data:image/png;base64,'+findActiveIssue().image);
            }
        }
    }
}

function fillSubDir() {
    let children = editSubIssueForm.childNodes
    for (let child of children) {
        if (child.classList != null && child.classList.contains("modal-input")) {
            if (child.name === "name-ru") {
                child.value = findActiveSubIssue().title
            } else if (child.name === "description-ru") {
                child.innerText = findActiveSubIssue().subtitle
            } else if (child.name === "subIssueCode") {
                child.value = findActiveSubIssue().subIssueCode
            } else if (child.name === "name-en") {
                child.value = findActiveSubIssue().titleEn
            } else if (child.name === "description-en") {
                child.value = findActiveSubIssue().subtitleEn
            } else if (child.name === "image-view")
            {
              child.setAttribute('src', 'data:image/png;base64,'+findActiveIssue().image);
            }
        }
    }
}


function editIssue(button) {
    // Request logic
    let xhr = new XMLHttpRequest();
    let url = URL + "issue/eissue"
    let token = sessionStorage.getItem("token")

    xhr.open("POST", url, true);
    xhr.setRequestHeader("Authorization", `Bearer_${token}`)
    //xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                alert("Saved")
                getPage(URL + "admin/Directions")
            } else {
                alert("Error")
            }
        }
    };

    let issueName = returnPreviousSiblingNTimes(button, 28)
    let issueNameEn = returnPreviousSiblingNTimes(button, 24)
    let locale = returnPreviousSiblingNTimes(button, 12)
    let subtitle = returnPreviousSiblingNTimes(button, 20)
    let subtitleEn = returnPreviousSiblingNTimes(button, 16)
    let issueCode = returnPreviousSiblingNTimes(button, 8)

    let data = JSON.stringify({ "title": issueName, "locale": locale, "subtitle": subtitle, "issueCode": issueCode, "id": findActiveIssue().id, "titleEn": issueNameEn, "subtitleEn": subtitleEn })

    var formData = new FormData();
    var image = $('#imageEditIssue').prop('files');
    formData.append("image", image[0]);
    formData.append("data", data);

    xhr.send(formData);
}


function editSubIssue(button) {
    let xhr = new XMLHttpRequest();
    let url = URL + "issue/esubissue" // fixme
    let token = sessionStorage.getItem("token")

    xhr.open("POST", url, true);
    xhr.setRequestHeader("Authorization", `Bearer_${token}`)
    //xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                alert("Saved")
                getPage(URL + "admin/Directions")
            } else {
                alert("Error")
            }
        }
    };

    let issueNameRu = returnPreviousSiblingNTimes(button, 24)
    let issueNameEn = returnPreviousSiblingNTimes(button, 20)
    let descriptionRu = returnPreviousSiblingNTimes(button, 16)
    let descriptionEn = returnPreviousSiblingNTimes(button, 12)
    let subIssueCode = returnPreviousSiblingNTimes(button, 8)
    let issueCode = findActiveIssue().issueCode

    let data = JSON.stringify({ "title": issueNameRu, "subtitle": descriptionRu, 
    "issueCode": issueCode, "subIssueCode": subIssueCode, "id": findActiveSubIssue().id, "titleEn": issueNameEn, "subtitleEn": descriptionEn })

    var formData = new FormData();
    var image = $('#imageEditSubIssue').prop('files');
    formData.append("image", image[0]);
    formData.append("data", data);

    xhr.send(formData);
}

function saveIssue(button) {
  let xhr = new XMLHttpRequest();
  let url = URL + "issue/issue"
  let token = sessionStorage.getItem("token")

  xhr.open("POST", url, true);
  xhr.setRequestHeader("Authorization", `Bearer_${token}`)
  //xhr.setRequestHeader("Content-Type", "multipart/form-data");
  xhr.onreadystatechange = function () {
      if (xhr.readyState === 4) {
          if (xhr.status === 200) {
              alert("Saved")
              getPage(URL + "admin/Directions")
          } else if (xhr.status === 409) {
            alert(JSON.parse(xhr.responseText).message)
          } else {
            alert("Error")
          }
      }
  };

    let issueNameRu = returnPreviousSiblingNTimes(button, 16)
    let issueNameEn = returnPreviousSiblingNTimes(button, 14)
    let subtitleRu = returnPreviousSiblingNTimes(button, 12)
    let subtitleEn = returnPreviousSiblingNTimes(button, 10)
    let issueCode = returnPreviousSiblingNTimes(button, 6)

    let data = JSON.stringify({"title": issueNameRu, "locale": sessionStorage.getItem("locale"), "subtitle": subtitleRu, "issueCode": issueCode, "titleEn": issueNameEn, "subtitleEn": subtitleEn })
    console.log(data)

    var formData = new FormData();
    var image = $('#imageAddIssue').prop('files');
    formData.append("image", image[0]);
    formData.append("data", data);

    xhr.send(formData);
}

function saveSubIssue(button) {
  let xhr = new XMLHttpRequest();
  let url = URL + "issue/subIssue"
  let token = sessionStorage.getItem("token")

  xhr.open("POST", url, true);
  //xhr.setRequestHeader("Content-Type", "application/json");
  xhr.setRequestHeader("Authorization", `Bearer_${token}`)
  xhr.onreadystatechange = function () {
      if (xhr.readyState === 4) {
          if (xhr.status === 200) {
              alert("Saved")
              getPage(URL + "admin/Directions")
          } else if (xhr.status === 409) {
            alert(JSON.parse(xhr.responseText).message)
          } else {
            alert("Error")
          }
      }
  };

    let issueNameRu = returnPreviousSiblingNTimes(button, 18)
    let issueNameEn = returnPreviousSiblingNTimes(button, 16)
    let descriptionRu = returnPreviousSiblingNTimes(button, 12)
    let descriptionEn = returnPreviousSiblingNTimes(button, 10)
    let subIssueCode = returnPreviousSiblingNTimes(button, 6)
    let issueCode = findActiveIssue().issueCode

    let data = JSON.stringify({ "title": issueNameRu, "subtitle": descriptionRu, "issueCode": issueCode, "subIssueCode": subIssueCode, "titleEn": issueNameEn, "subtitleEn": descriptionEn })

    var formData = new FormData();
    var image = $('#imageAddSubIssue').prop('files');
    formData.append("image", image[0]);
    formData.append("data", data);

    xhr.send(formData);
 }

function deleteIssue() {
    let xhr = new XMLHttpRequest();
    let url = URL + "issue/deleteIssue?id=" + findActiveIssue().id
    let token = sessionStorage.getItem("token")
  
    xhr.open("POST", url, true);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.setRequestHeader("Authorization", `Bearer_${token}`)
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                alert("Deleted")
                getPage(URL + "admin/Directions")
            } else if (xhr.status === 409) {
              alert(JSON.parse(xhr.responseText).message)
            } else {
              alert("Error")
            }
        }
    };

    xhr.send()
}

function deleteSubIssue() {
    let xhr = new XMLHttpRequest();
    let url = URL + "issue/deleteSubIssue?id=" + findActiveSubIssue().id
    let token = sessionStorage.getItem("token")
  
    xhr.open("POST", url, true);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.setRequestHeader("Authorization", `Bearer_${token}`)
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                alert("Deleted")
                getPage(URL + "admin/Directions")
            } else if (xhr.status === 409) {
              alert(JSON.parse(xhr.responseText).message)
            } else {
              alert("Error")
            }
        }
    };

    xhr.send()
}


function showIssues() {
  let xhr = new XMLHttpRequest();
  let url = URL + "common/issues?locale=" + sessionStorage.getItem("locale")
  let token = sessionStorage.getItem("token")
  console.log("url - " + url)

    xhr.open("GET", url, true);
    xhr.setRequestHeader("Authorization", `Bearer_${token}`)
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            let issues = JSON.parse(xhr.responseText).reverse()
            let counter = issues.length - 1

            if (issues.length === 0) {
              lawsDiv.insertAdjacentHTML('beforeend', plusDiv)
            }

            for (let issue of issues) {
                // add issue logic
                let divNode = document.createElement('div')
                divNode.classList.add("law")
                if (counter == 0) {
                    divNode.classList.add("is-active")
                    descriptionIssue.innerText = issue.subtitle
                    descriptionIssueEn.innerText = issue.subtitleEn
                }
                divNode.setAttribute("data-tab-name", counter)
                divNode.insertAdjacentHTML("beforeend", getIssueTemplateCode(issue.title))
                lawsDiv.insertBefore(divNode, lawsDiv.firstChild)
                issueMap.set(counter, issue)

                // add subIssue logic
                let ul = document.createElement('ul')
                ul.classList.add("law-list")
                ul.classList.add(counter)
                if (counter == 0) {
                    ul.classList.add("is-active")
                }

                if (issue.subIssueTypeList.length === 0) {
                    ul.insertAdjacentHTML("beforeend", plus)
                }
                for (let subIssue of issue.subIssueTypeList) {
                    subIssueMap.set(subIssue.id, subIssue)

                    // check if last then add plus to last
                    if (subIssue === issue.subIssueTypeList[issue.subIssueTypeList.length - 1]) {
                        ul.insertAdjacentHTML("beforeend", getSubIssueTemplate(subIssue.title, "class=\"sub-law is-active\"", subIssue.id))
                        ul.insertAdjacentHTML("beforeend", plus)
                        break;
                    } else if (subIssue === issue.subIssueTypeList[0]) {
                        descriptionSubIssue.innerText = subIssue.subtitle
                        descriptionSubIssueEn.innerText = subIssue.subtitleEn
                    }
                    ul.insertAdjacentHTML("beforeend", getSubIssueTemplate(subIssue.title, "class=\"sub-law\"", subIssue.id))
                }
                subIssuesBlock.insertBefore(ul, subIssuesBlock.firstChild)
                if (counter === 0) {
                    lawsDiv.insertAdjacentHTML('beforeend', plusDiv)
                }
                counter--;
            }
            const aToSubDir = document.getElementsByClassName("aToSubDir")
            for (let a of aToSubDir) {
                a.addEventListener('click', fillSubDir)
            }
            const aToDir = document.getElementsByClassName("aToDir")
            for (let a of aToDir) {
                a.addEventListener('click', fillDir)
            }
            tab()
            tabSubIssues()
        }
    };
    xhr.send();
}

var plus = `<li><a href="#newSubDir" rel="modal:open" class="law-plus"><span class="plus">+</span></a></li>`

var plusDiv = `<a href="#newDir" rel="modal:open" class="law-plus"><span class="plus">+</span></a>`

function getSubIssueTemplate(name, isActive, id) {
    return `
  <li ${isActive} data-tab-name="${id}">
  ${name} 
  <a class="aToSubDir" href="#editSubDir" rel="modal:open">
  <img src="/img/pencil2.svg" class="edit-icon">
  </a>
</li>`
}

function getIssueTemplateCode(issueName) {
    return `
    <h1> <br>${issueName}</h1>
    <a class="aToDir" href="#editDir" rel="modal:open">
      <img src="/img/pencil2.svg" class="edit-icon">
    </a>`
}

function returnPreviousSiblingNTimes(node, n) {
    let cur = node
    for (let i = 0; i < n; ++i) {
        cur = cur.previousSibling
    }
    return cur.value
}

var tabSubIssues = function () {
    let li = document.querySelectorAll('.sub-law');

    li.forEach(item => {
        item.addEventListener('mouseover', selectLaw)
    });

    function selectLaw() {
        li.forEach(item => {
            item.classList.remove('is-active');
        });
        this.classList.add('is-active');
        descriptionSubIssue.innerText = findActiveSubIssue().subtitle
        descriptionSubIssueEn.innerText = findActiveSubIssue().subtitleEn
    }
}

var tab = function () {
    let law = document.querySelectorAll('.law');
    let lawList = document.querySelectorAll('.law-list');
    let lawName;

    law.forEach(item => {
        item.addEventListener('mouseover', selectLaw)
    });

    function selectLaw() {
        law.forEach(item => {
            item.classList.remove('is-active');
        });
        this.classList.add('is-active');
        descriptionIssue.innerText = findActiveIssue().subtitle
        descriptionIssueEn.innerText = findActiveIssue().subtitleEn

        let activeSubIssue = document.getElementsByClassName("sub-law is-active")[0]
        if (activeSubIssue != null) {
            activeSubIssue.classList.remove('is-active')
        }

        lawName = this.getAttribute('data-tab-name');
        selectLawList(lawName);
    }

    function selectLawList(lawName) {
        lawList.forEach(item => {
            item.classList.contains(lawName) ? selectLawListFunc(item) : item.classList.remove('is-active');
        })
    }

    function selectLawListFunc(item) {
        item.classList.add('is-active')
        if (item.childNodes[1] != null) {
            item.childNodes[1].classList.add('is-active')
            descriptionSubIssue.innerText = findActiveSubIssue().subtitle
            descriptionSubIssueEn.innerText = findActiveSubIssue().subtitleEn
        }
    }

};
showIssues();