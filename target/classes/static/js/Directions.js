const saveIssueButton = document.getElementById("saveIssue")
const saveSubIssuebutton = document.getElementById("saveSubIssue")
const lawsDiv = document.getElementsByClassName("laws")[0]
const subIssuesBlock = document.getElementById("subIssuesBlock")
const descriptionIssue = document.getElementById("issueDescription")
const descriptionSubIssue = document.getElementById("subIssueDescription")

saveIssueButton.addEventListener("click", function() {saveIssue(saveIssueButton)})
saveSubIssuebutton.addEventListener('click', function() {saveSubIssue(saveSubIssuebutton)})

let issueMap = new Map();

function saveIssue(button) {
  let xhr = new XMLHttpRequest();
  let url = "http://localhost:8080/api/v1/issue/issue"
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
  let url = "http://localhost:8080/api/v1/issue/subIssue" // fixme
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

  let issueNameRu = returnPreviousSiblingNTimes(button, 10)
  let descriptionRu = returnPreviousSiblingNTimes(button, 6)
  let subIssueCode = returnPreviousSiblingNTimes(button, 2)
  let issueCode = findActiveIssue().issueCode

  let data = JSON.stringify({ "title": issueNameRu, "subtitle": descriptionRu, "issueCode": issueCode, "subIssueCode": subIssueCode }) // fixme
  console.log(data)
  xhr.send(data);
}

function findActiveIssue() {
  let block = document.getElementsByClassName("law is-active")[0]
  return issueMap.get(parseInt(block.getAttribute("data-tab-name")))
}


function showIssues() {
  console.log("In issues")
  let xhr = new XMLHttpRequest();
  let url = "http://localhost:8080/api/v1/issue/all?locale=" + sessionStorage.getItem("locale")
  let token = sessionStorage.getItem("token")
  console.log("url - " + url)

  xhr.open("GET", url, true);
  xhr.setRequestHeader("Authorization", `Bearer_${token}`)
  xhr.setRequestHeader("Content-Type", "application/json");
  xhr.onreadystatechange = function () {
      if (xhr.readyState === 4 && xhr.status === 200) {
          console.log("In readyState")
          let issues = JSON.parse(xhr.responseText).reverse()
          let counter = issues.length - 1
          console.log(issues)
          for (let issue of issues) {
            // add issue logic
            let divNode = document.createElement('div')
            divNode.classList.add("law")
            if(counter === 0) {
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
            if (counter === 0) {
              ul.classList.add("is-active")
            }

            if (issue.subIssueTypeList.length === 0) {
              ul.insertAdjacentHTML("beforeend", plus)
            }
            for (let subIssue of issue.subIssueTypeList) {
              ul.insertAdjacentHTML("beforeend", getSubIssueTemplate(subIssue.title))

              // check if last then add plus to last
              if (subIssue === issue.subIssueTypeList[issue.subIssueTypeList.length - 1]) {
                ul.insertAdjacentHTML("beforeend", plus)
              }
            }
            subIssuesBlock.insertBefore(ul, subIssuesBlock.firstChild)
            if(counter === 0) {
              lawsDiv.insertAdjacentHTML('beforeend',plusDiv)
            }
            counter--;
          }
          tab()
      }
  };
  xhr.send();
}

const plus = `<li><a href="#newSubDir" rel="modal:open" class="law-plus"><span class="plus">+</span></a></li>`

const plusDiv = `<a href="#newDir" rel="modal:open" class="law-plus"><span class="plus">+</span></a>`

function getSubIssueTemplate(name) {
  return `
  <li>
  ${name} 
  <a href="#editSubDir" rel="modal:open">
    <svg class="edit-icon">
      <use xlink:href="img/pencil2.svg#pencil-svgrepo-com"></use>
    </svg>
  </a>
</li>`
}

function getIssueTemplateCode(issueName) {
  return `
    <h1> <br>${issueName}</h1>
    <a href="#editDir" rel="modal:open">
      <svg class="edit-icon">
        <use xlink:href="img/pencil2.svg#pencil-svgrepo-com"></use>
      </svg>
    </a>`
}

function returnPreviousSiblingNTimes(node , n) {
  let cur = node
  for(let i = 0; i < n; ++i) {
    cur = cur.previousSibling
  }
  return cur.value
}

let tab = function() {
  let law = document.querySelectorAll('.law');
  let lawList = document.querySelectorAll('.law-list');
  let lawName;

  law.forEach(item => {
    item.addEventListener('click', selectLaw)
  });

  function selectLaw() {
    law.forEach(item => {
      item.classList.remove('is-active');
    });
    this.classList.add('is-active');
    console.log(findActiveIssue())
    descriptionIssue.innerText = findActiveIssue().subtitle
    lawName = this.getAttribute('data-tab-name');
    selectLawList(lawName);
  }

  function selectLawList(lawName) {
    lawList.forEach(item => {
      item.classList.contains(lawName) ? item.classList.add('is-active') : item.classList.remove('is-active'); 
    })
  }

};
showIssues();