const saveIssueButton = document.getElementById("saveIssue")
const saveSubIssuebutton = document.getElementById("saveSubIssue")
const lawsDiv = document.getElementsByClassName("laws")[0]
const subIssuesBlock = document.getElementById("subIssuesBlock")
const descriptionIssue = document.getElementById("issueDescription")
const descriptionSubIssue = document.getElementById("subIssueDescription")
const editIssueButton = document.getElementById("editIssue")
const editSubIssueButton = document.getElementById("editSubIssue")
const editIssueForm = document.getElementById("editIssueForm")
const editSubIssueForm = document.getElementById("editSubIssueForm")

saveIssueButton.addEventListener("click", function () { saveIssue(saveIssueButton) })
saveSubIssuebutton.addEventListener('click', function () { saveSubIssue(saveSubIssuebutton) })
editIssueButton.addEventListener('click', function () { editIssue(editIssueButton) })
editSubIssueButton.addEventListener('click', function () { editSubIssue(editSubIssueButton) })
let issueMap = new Map();
let subIssueMap = new Map();


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
            }
        }
    }
}


function editIssue(button) {
    // Request logic
    let xhr = new XMLHttpRequest();
    let url = URL + "issue/eissue" // fixme
    let token = sessionStorage.getItem("token")

    xhr.open("POST", url, true);
    xhr.setRequestHeader("Authorization", `Bearer_${token}`)
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                alert("Saved")
            } else {
                alert("Error")
            }
        }
    };

    let issueName = returnPreviousSiblingNTimes(button, 14)
    let locale = returnPreviousSiblingNTimes(button, 6)
    let subtitle = returnPreviousSiblingNTimes(button, 10)
    let issueCode = returnPreviousSiblingNTimes(button, 2)

    let data = JSON.stringify({ "title": issueName, "locale": locale, "subtitle": subtitle, "issueCode": issueCode, "id": findActiveIssue().id })
    console.log(data)
    xhr.send(data);
}


function editSubIssue(button) {
    let xhr = new XMLHttpRequest();
    let url = URL + "issue/esubissue" // fixme
    let token = sessionStorage.getItem("token")

    xhr.open("POST", url, true);
    xhr.setRequestHeader("Authorization", `Bearer_${token}`)
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                alert("Saved")
            } else {
                alert("Error")
            }
        }
    };

    let issueNameRu = returnPreviousSiblingNTimes(button, 10)
    let descriptionRu = returnPreviousSiblingNTimes(button, 6)
    let subIssueCode = returnPreviousSiblingNTimes(button, 2)
    let issueCode = findActiveIssue().issueCode

    let data = JSON.stringify({ "title": issueNameRu, "subtitle": descriptionRu, "issueCode": issueCode, "subIssueCode": subIssueCode, "id": findActiveSubIssue().id })
    console.log(data)
    xhr.send(data);
}

function saveIssue(button) {
  let xhr = new XMLHttpRequest();
  let url = URL + "issue/issue"
  let token = sessionStorage.getItem("token")

  xhr.open("POST", url, true);
  xhr.setRequestHeader("Authorization", `Bearer_${token}`)
  xhr.setRequestHeader("Content-Type", "application/json");
  xhr.onreadystatechange = function () {
      if (xhr.readyState === 4) {
          if (xhr.status === 200) {
              alert("Saved")
          } else if (xhr.status === 409) {
            alert(JSON.parse(xhr.responseText).message)
          } else {
            alert("Error")
          }
      }
  };

    let issueName = returnPreviousSiblingNTimes(button, 10)
    let locale = returnPreviousSiblingNTimes(button, 8)
    let subtitle = returnPreviousSiblingNTimes(button, 6)
    let issueCode = returnPreviousSiblingNTimes(button, 2)

    let data = JSON.stringify({ "title": issueName, "locale": locale, "subtitle": subtitle, "issueCode": issueCode })
    console.log(data)
    xhr.send(data);
}

function saveSubIssue(button) {
  let xhr = new XMLHttpRequest();
  let url = URL + "issue/subIssue"
  let token = sessionStorage.getItem("token")

  xhr.open("POST", url, true);
  xhr.setRequestHeader("Content-Type", "application/json");
  xhr.onreadystatechange = function () {
      if (xhr.readyState === 4) {
          if (xhr.status === 200) {
              alert("Saved")
          } else if (xhr.status === 409) {
            alert(JSON.parse(xhr.responseText).message)
          } else {
            alert("Error")
          }
      }
  };

    let issueNameRu = returnPreviousSiblingNTimes(button, 10)
    let descriptionRu = returnPreviousSiblingNTimes(button, 6)
    let subIssueCode = returnPreviousSiblingNTimes(button, 2)
    let issueCode = findActiveIssue().issueCode

    let data = JSON.stringify({ "title": issueNameRu, "subtitle": descriptionRu, "issueCode": issueCode, "subIssueCode": subIssueCode })
    console.log(data)
    xhr.send(data);
}


function showIssues() {
  console.log("In issues")
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

const plus = `<li><a href="#newSubDir" rel="modal:open" class="law-plus"><span class="plus">+</span></a></li>`

const plusDiv = `<a href="#newDir" rel="modal:open" class="law-plus"><span class="plus">+</span></a>`

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

let tabSubIssues = function () {
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
    }
}

let tab = function () {
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
        }
    }

};
showIssues();