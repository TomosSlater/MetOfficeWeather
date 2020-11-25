const RAINOVERLAY = document.querySelector("#rain");
const CLOUDOVERLAY = document.querySelector("#cloud");
const CLOUDRAINOVERLAY = document.querySelector("#cloudrain");
const TEMPERATUREOVERLAY = document.querySelector("#temperature");
const PRESSUREOVERLAY = document.querySelector("#pressure");

document.querySelector("#overlaySelector").onchange = function(){
    RAINOVERLAY.style.display = (this.value == "rain") ? "block" : "none";
    CLOUDOVERLAY.style.display = (this.value == "cloud") ? "block" : "none";
    CLOUDRAINOVERLAY.style.display = (this.value == "cloudrain") ? "block" : "none";
    TEMPERATUREOVERLAY.style.display = (this.value == "temperature") ? "block" : "none";
    PRESSUREOVERLAY.style.display = (this.value == "pressure") ? "block" : "none";
}