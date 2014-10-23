/*
 * Till den här enkla funktionen såg vi ingen anledning till att använda angular
 * 
 * author elin
 */

var currentImage = 0;
var images;
var timer;
var interval = 3 * 1000;
var pageIndicators = [];

var goToImage = function(goTo) {
    var page = goTo % images;
    
    var sliderContents = document.getElementById("sliderContents");
    if (sliderContents === null) {
        return false;
    }
    sliderContents.style.left = "-" + goTo + "00%";
    setActivePageIndicator(page);
    currentImage = page;
    if (goTo !== page) {
        setTimeout(function() {
            var sliderContents = document.getElementById("sliderContents");
            sliderContents.className = "notransition";
            sliderContents.style.left = "-" + page + "00%";
            sliderContents.offsetHeight;
            sliderContents.className = "";
        }, 1000);
    }
    return true;
};

var setActivePageIndicator = function(n) {
    for (var i=0; i<pageIndicators.length; i++) {
        pageIndicators[i].className = "";
    }
    pageIndicators[n].className = "active";
};

var nextImage = function() {
    goToImage((currentImage + 1) % (images + 1));
};

var prevImage = function() {
    if(--currentImage < 0) {
        currentImage = images - 1;
    }   
    goToImage(currentImage);
};

var startTimer = function() {
    if(timer !== null) {
        clearTimeout(timer);
    }
    timer = setTimeout(function() {
        nextImage();
        startTimer();
    }, interval);
};

var generateLastImage = function() {
    var sliderContents = document.getElementById("sliderContents");
    var first = sliderContents.getElementsByTagName("li")[0].cloneNode(true);
    sliderContents.appendChild(first);
};

var generatePageIndicat = function() {
    for (var i=0; i<images; i++) {
        var indicator = document.createElement("span");
        indicator.setAttribute("n", i);
        indicator.onclick = function() {
            if (goToImage(this.getAttribute("n"))) {
                startTimer();
            }
        };
        pageIndicators[pageIndicators.length] = indicator;
        document.getElementById("pageIndicators").appendChild(indicator);
    }
};

var generateCSS = function() {
    document.getElementById("sliderContents").style.width = (images + 1) + "00%";
    var lis = document.getElementById("sliderContents").getElementsByTagName("li");
    for (var i=0; i<lis.length; i++) {
       lis[i].style.width = (100/(images+1))  + "%";
    }
};

var startSlide = function() {
    images = document.getElementById("sliderContents").children.length;
    generateLastImage();
    generatePageIndicat();
    setActivePageIndicator(0);
    generateCSS();
    startTimer();
};



