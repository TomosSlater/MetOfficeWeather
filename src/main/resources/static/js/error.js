const QUERYSTRING = window.location.search;
const URLPARAMETERS = new URLSearchParams(QUERYSTRING)
const LOCATION = URLPARAMETERS.get("location")

var h2Element = document.querySelector("#errorMessage");
h2Element.innerHTML = `Error: Could not find ${LOCATION} using the Met Office API`