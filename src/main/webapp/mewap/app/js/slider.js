/*
 * Till den här enkla funktionen såg vi ingen anledning till att använda angular
 * 
 * author elin
 */

var currentImage = 0;
var images = ["img01.jpg", "img02.jpg", "img03.jpg", "img04.jpg"];
var timer;
var interval = 2 * 1000;

var goToImage = function(goTo) {
    document.getElementById("sliderContents").style.left = "-" + goTo + "00%";
    currentImage = goTo;
}

var nextImage = function() {
    goToImage((currentImage + 1) % images.length);
}

var prevImage = function() {
    if(--currentImage < 0) {
        currentImage = images.length - 1;
    }   
    goToImage(currentImage);
}

var animate = function() {
    if(timer != null) {
        clearTimeout(timer);
    }
    timer = setTimeout(function() {
        nextImage();
        animate();
    }, interval);
}

var generateImages = function() {
    images.forEach(function(image) {
        var li = document.createElement("li");
        li.className = "slideli";
        var img = document.createElement("img");
        img.src = "images/" + image;
        li.appendChild(img);
        document.getElementById("sliderContents").appendChild(li);
    });
}

var generateCSS = function() {
    document.getElementById("sliderContents").style.width = images.length + "00%";
    var lis = document.getElementsByClassName("slideli");
    for (var i=0; i<lis.length; i++) {
       lis[i].style.width = (100/images.length)  + "%";
    }
}

var startSlide = function() {
    generateImages();
    generateCSS();
    animate();
}



