var adminList = 
document.querySelector('.admin-list');

var adminName = document.getElementsByClassName("admin-name")

var saveCountryButton = document.getElementById("saveCountry")

var saveCityButton = document.getElementById("saveCity")

var selectCountry = document.getElementById("country")
var selectCity = document.getElementById("city")
var saveAdminButton = document.getElementById("saveAdmin")

var deleteCityButton = document.getElementById("delCity")
var deleteCountryButton = document.getElementById("delCountry")

var cities;
var countries;

var curCountry;
var curCity;

selectCountry.addEventListener('change', onCountryChanged)
selectCity.addEventListener('change', onCityChanged)
saveCountryButton.addEventListener('click', function () { saveEvent(URL + "admin/country?", false, saveCountryButton) })
saveCityButton.addEventListener('click', function () { saveEvent(URL + "admin/city?", true, saveCityButton) })
saveAdminButton.addEventListener('click', function() { saveAdmin(saveAdminButton) })
deleteCityButton.addEventListener('click', function() { deleteCity() })
deleteCountryButton.addEventListener('click', function() { deleteCountry() })


function deleteCity() {
  let xhr = new XMLHttpRequest();
  let url = URL + "admin/deleteCity?cityName=" + curCity
  xhr.open("POST", url, true);
  xhr.setRequestHeader("Content-Type", "application/json");
  let token = sessionStorage.getItem("token")
  xhr.setRequestHeader("Authorization", `Bearer_${token}`)

  xhr.onreadystatechange = function () {
      if (xhr.readyState === 4) {
        if(xhr.status === 200) {
          alert("Deleted!")
          getPage(URL + "admin/PersonalCabinet")
        } else {
          alert("Exception: " + xhr.status)
        }
      }
  };
  xhr.send();
}

function deleteCountry() {
  let xhr = new XMLHttpRequest();
  let url = URL + "admin/deleteCountry?countryName=" + curCountry
  xhr.open("POST", url, true);
  xhr.setRequestHeader("Content-Type", "application/json");
  let token = sessionStorage.getItem("token")
  xhr.setRequestHeader("Authorization", `Bearer_${token}`)

  xhr.onreadystatechange = function () {
      if (xhr.readyState === 4) {
        if(xhr.status === 200) {
          alert("Deleted!")
          getPage(URL + "admin/PersonalCabinet")
        } else {
          alert("Exception: " + xhr.status)
        }
      }
  };
  xhr.send();
}

function addOptions() {
  getAllCountries()
  getAllCities()
}

function getAllCountries() {
  let xhr = new XMLHttpRequest();
  let url = URL + "admin/country"
  xhr.open("GET", url, true);
  xhr.setRequestHeader("Content-Type", "application/json");
  let token = sessionStorage.getItem("token")
  xhr.setRequestHeader("Authorization", `Bearer_${token}`)

  xhr.onreadystatechange = function () {
      if (xhr.readyState === 4 && xhr.status === 200) {
          countries = JSON.parse(xhr.responseText)
          let flag = true
          for(let country of countries) {
            let opt = document.createElement('option')
            opt.appendChild(document.createTextNode(country.title))
            opt.className = 'option'
            selectCountry.appendChild(opt)
            if (flag) {
              sessionStorage.setItem("locale", country.locale)
              flag = false
            }
          }
      }
  };
  xhr.send();
}

function getAllCities() {
  let xhr = new XMLHttpRequest();
  let url = URL + "admin/CaC"
  xhr.open("GET", url, true);
  xhr.setRequestHeader("Content-Type", "application/json");
  let token = sessionStorage.getItem("token")

  xhr.setRequestHeader("Authorization", `Bearer_${token}`)
  xhr.onreadystatechange = function () {
      if (xhr.readyState === 4 && xhr.status === 200) {
          cities = JSON.parse(xhr.responseText)
          onCountryChanged()
          onCityChanged()
      }
  };
  xhr.send();
}

function onCityChanged() {
  curCity = selectCity.options[selectCity.selectedIndex].value
}

function onCountryChanged() {
  curCountry = selectCountry.options[selectCountry.selectedIndex].value

  for (let country of countries) {
    if (country.title === curCountry) {
      sessionStorage.setItem("locale", country.locale)
    }
  }

  let cityList = cities[curCountry]

  var child = selectCity.lastElementChild;  
  while (child) { 
      selectCity.removeChild(child); 
      child = selectCity.lastElementChild; 
  }

  for(let city of cityList) {
    let opt = document.createElement('option');
    opt.appendChild(document.createTextNode(city));
    opt.className = 'option'
    selectCity.appendChild(opt)
  }
  onCityChanged()
}

function setAdminName() {
  let firstName = sessionStorage.getItem("first_name")
  let lastName = sessionStorage.getItem("last_name")
  adminName[0].innerText = firstName + " " + lastName
}

function showAdmins() {
    let xhr = new XMLHttpRequest();
    let url = URL + "admin/admins"
    xhr.open("GET", url, true);
    xhr.setRequestHeader("Content-Type", "application/json");
    
    let token = sessionStorage.getItem("token")

    xhr.setRequestHeader("Authorization", `Bearer_${token}`)
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            let json = JSON.parse(xhr.responseText)
            for(let admin of json) {
                generateAdminList(admin.firstName, admin.lastName, admin.email, admin.phoneNumber)
            }
            state = document.documentElement.innerHTML;
        }
    };
    xhr.send();
}

function generateAdminList(
    firstName, 
    lastName, 
    email, 
    phoneNumber 
) {
  const admin = `
  <li>
    <span class="admin-surname">${lastName}</span>
    <span class="admin-name-li">${firstName}</span>
    <span class="admin-email">${email}</span>
    <span class="admin-telephone">${phoneNumber}</span>
    <a><img src="/img/trash.svg" class="delete-icon" width="30px"></a>
  </li>
  `
  adminList.insertAdjacentHTML('afterbegin', admin);
  
}

function saveEvent(endpoint, isCity, button) {
  let xhr = new XMLHttpRequest();
  let url = endpoint
  if (isCity) {
    // here we pass 2n value cause of "\n" nodes
    console.log(returnPreviousSiblingNTimes(button, 2))
    url += "countryCode=" + returnPreviousSiblingNTimes(button, 2) + "&cityCode=" 
        + returnPreviousSiblingNTimes(button, 6)
        + "&cityTitleRu=" + returnPreviousSiblingNTimes(button, 14)
        + "&cityTitleEn=" + returnPreviousSiblingNTimes(button, 10)
    console.log(url)
  } else {
    url += "countryTitleRu=" + returnPreviousSiblingNTimes(button, 14) 
            + "&countryCode=" + returnPreviousSiblingNTimes(button, 6) 
            + "&locale=" + returnPreviousSiblingNTimes(button, 2)
            + "&countryTitleEn=" + returnPreviousSiblingNTimes(button, 10) 
    console.log(url)
  }
  xhr.open("POST", url, true);
  let token = sessionStorage.getItem("token")

  xhr.setRequestHeader("Authorization", `Bearer_${token}`)
  xhr.setRequestHeader("Content-Type", "application/json");
  xhr.onreadystatechange = function () {
      if (xhr.readyState === 4) {
          if(xhr.status === 200){
              alert("Saved")
              getPage(URL + "admin/PersonalCabinet")
          } else if (xhr.status === 409) {
              alert(JSON.parse(xhr.responseText).message)
          } else {
              alert("Error")
          }
      }
  };
  xhr.send();
}

function saveAdmin(button) {
    let xhr = new XMLHttpRequest();
    let url = URL + "admin/save"
    
    xhr.open("POST", url, true);

    let token = sessionStorage.getItem("token")

    xhr.setRequestHeader("Authorization", `Bearer_${token}`)
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                alert("Saved")
                getPage(URL + "admin/PersonalCabinet")
            } else if (xhr.status === 409) {
                alert(JSON.parse(xhr.responseText).message)
            } else {
                alert("Error")
            }
        }
    };

    let name = returnPreviousSiblingNTimes(button, 18)
    let lastName = returnPreviousSiblingNTimes(button, 14)
    let email = returnPreviousSiblingNTimes(button, 10)
    let tel = returnPreviousSiblingNTimes(button, 2)
    let password = returnPreviousSiblingNTimes(button, 6)

    let data = JSON.stringify({ "firstName": name, "lastName": lastName, "email": email, "phoneNumber": tel, "password": password })
    console.log(data)
    xhr.send(data);
}

// returns previous siblings
function returnPreviousSiblingNTimes(node , n) {
  let cur = node
  for(let i = 0; i < n; ++i) {
    cur = cur.previousSibling
  }
  return cur.value
}

setAdminName()
addOptions()
showAdmins()