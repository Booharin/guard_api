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
                if (findActiveIssue().image != null)
                  child.setAttribute('src', 'data:image/png;base64,'+findActiveIssue().image);
                else
                  child.setAttribute('src', 'data:image/png;base64,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');
            }
        }
    }
    $("#editDir").modal({
          escapeClose: false,
          clickClose: false
        });
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
              if (findActiveSubIssue().image != null)
                child.setAttribute('src', 'data:image/png;base64,'+findActiveSubIssue().image);
              else
                child.setAttribute('src', 'data:image/png;base64,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');
            }
        }
    }
    $("#editSubDir").modal({
      escapeClose: false,
      clickClose: false
    });
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

var plus = `<li><a href="#newSubDir" onclick='$("#newSubDir").modal({escapeClose: false,clickClose: false})' class="law-plus"><span class="plus">+</span></a></li>`

var plusDiv = `<a href="#newDir" onclick='$("#newDir").modal({escapeClose: false,clickClose: false})' class="law-plus"><span class="plus">+</span></a>`

function getSubIssueTemplate(name, isActive, id) {
    return `
  <li ${isActive} data-tab-name="${id}">
  ${name} 
  <a class="aToSubDir" href="#editSubDir">
  <img src="/img/pencil2.svg" class="edit-icon">
  </a>
</li>`
}

function getIssueTemplateCode(issueName) {
    return `
    <h1> <br>${issueName}</h1>
    <a class="aToDir" href="#editDir">
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