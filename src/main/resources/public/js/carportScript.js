const frame = document.getElementById("frame");

const amountOfCars = document.getElementById("amountOfCars");

const widthSelect = document.getElementById("width");
const lengthSelect = document.getElementById("length");

const shedWidthSelect = document.getElementById("shedWidth");
const shedLengthSelect = document.getElementById("shedLength");

const oneCarWidths = [240, 270, 300, 330, 360, 390, 420, 450, 480, 510, 540, 570, 600];
const oneCarLengths = [240, 270, 300, 330, 360, 390, 420, 450, 480, 510, 540, 570, 600, 630, 660, 690, 720, 750, 780];

const twoCarWidths = [450, 480, 510, 540, 570, 600];
const twoCarLengths = [240, 270, 300, 330, 360, 390, 420, 450, 480, 510, 540, 570, 600, 630, 660, 690, 720, 750, 780]

const widthLabel = document.getElementById("widthLabel");
const heightLabel = document.getElementById("heightLabel");

function populateSelect(selectElement, values, placeholder) {
    selectElement.innerHTML = "";

    const defaultOption = document.createElement("option");
    defaultOption.value = "0";
    defaultOption.textContent = placeholder;
    selectElement.appendChild(defaultOption);

    values.forEach(value => {
        const option = document.createElement("option");
        option.value = value;
        option.textContent = value + " cm";
        selectElement.appendChild(option);
    });
}

function updateOptions(){
    const cars = amountOfCars.value;

    if (cars === "1") {
        populateSelect(widthSelect, oneCarWidths, "Vælg bredde");
        populateSelect(lengthSelect, oneCarLengths, "Vælg længde");


    } else if (cars === "2") {
        populateSelect(widthSelect, twoCarWidths, "Vælg bredde");
        populateSelect(lengthSelect, twoCarLengths, "Vælg længde");

    }
}

function updateCarport() {
    const selectedWidth = widthSelect.value;
    const selectedLength = lengthSelect.value;

    if (!selectedWidth || !selectedLength) return;

    updateShedOptions();

    const x = 20;
    const y = 20;

    const svgW = 400;
    const svgH = 500;

    const scaleX = selectedWidth / (svgW * 0.8);
    const scaleY = selectedLength / (svgH * 0.8);
    const scale = Math.max(scaleX, scaleY);

    const w = selectedWidth / scale;
    const h = selectedLength / scale;

    frame.setAttribute("x", x);
    frame.setAttribute("y", y);
    frame.setAttribute("width", w);
    frame.setAttribute("height", h);

    widthLabel.textContent = selectedWidth + " cm";
    heightLabel.textContent = selectedLength + " cm";

    widthLabel.setAttribute("x", x + w / 2);
    widthLabel.setAttribute("y", y - 5);

    heightLabel.setAttribute("x", x - 15);
    heightLabel.setAttribute("y", y + h / 2);

    const innerFrame = document.getElementById("innerFrame");

    const innerW = (selectedWidth - 30) / scale;
    const innerH = h;

    const innerX = x + (30 / (2 * scale));
    const innerY = y;

    innerFrame.setAttribute("x", innerX);
    innerFrame.setAttribute("y", innerY);
    innerFrame.setAttribute("width", innerW);
    innerFrame.setAttribute("height", innerH);

    const shed = document.getElementById("shed");

    const selectShedWidth = shedWidthSelect.value;
    const selectShedLength = shedLengthSelect.value;

    const sW = selectShedWidth / scale;
    const sH = selectShedLength / scale;

    shed.setAttribute("x", innerX);
    shed.setAttribute("y", innerY);
    shed.setAttribute("width", sW);
    shed.setAttribute("height", sH);

    const car = document.getElementById("car");

    const carWidthCm = 180;
    const carHeightCm = 430;

    car.setAttribute("width", carWidthCm / scale);
    car.setAttribute("height", carHeightCm / scale);
}

amountOfCars.addEventListener("change", updateOptions);

widthSelect.addEventListener("change", updateCarport);
lengthSelect.addEventListener("change", updateCarport);

shedWidthSelect.addEventListener("change", updateCarport);
shedLengthSelect.addEventListener("change", updateCarport);

const car = document.getElementById("car");
const svg = document.querySelector("svg");

let isDragging = false;
let offsetX = 0;
let offsetY = 0;

car.addEventListener("mousedown", startDrag);
svg.addEventListener("mousemove", drag);
svg.addEventListener("mouseup", stopDrag);
svg.addEventListener("mouseleave", stopDrag);

function startDrag(e) {
    isDragging = true;

    const pt = getMousePosition(e);

    offsetX = pt.x - parseFloat(car.getAttribute("x"));
    offsetY = pt.y - parseFloat(car.getAttribute("y"));
}

function drag(e) {
    if (!isDragging) return;

    const pt = getMousePosition(e);

    const newX = pt.x - offsetX;
    const newY = pt.y - offsetY;

    car.setAttribute("x", newX);
    car.setAttribute("y", newY);
}

function stopDrag() {
    isDragging = false;
}

function getMousePosition(evt) {
    const CTM = svg.getScreenCTM();
    return {
        x: (evt.clientX - CTM.e) / CTM.a,
        y: (evt.clientY - CTM.f) / CTM.d
    };
}

function updateShedOptions() {
    const selectedWidth = parseInt(widthSelect.value);
    const selectedLength = parseInt(lengthSelect.value);

    if (!selectedWidth || !selectedLength) return;

    const currentShedWidth = parseInt(shedWidthSelect.value);
    const currentShedLength = parseInt(shedLengthSelect.value);

    const innerWidthCm = selectedWidth - 30;
    const innerLengthCm = selectedLength - 15;

    const allShedWidths = [210, 240, 270, 300, 330, 360, 390, 420, 450, 480, 510, 540, 570, 600, 630, 660, 690, 720];
    const allShedLengths = [150, 180, 210, 240, 270, 300, 330, 360, 390, 420, 450, 480, 510, 540, 570, 600, 630, 660, 690];

    const validShedWidths = allShedWidths.filter(w =>
        w <= innerWidthCm + 0.0001
    );

    const validShedLengths = allShedLengths.filter(l =>
        l <= innerLengthCm + 0.0001
    );

    populateSelect(shedWidthSelect, validShedWidths, "Vælg skur bredde");
    populateSelect(shedLengthSelect, validShedLengths, "Vælg skur længde");

    if (validShedWidths.includes(currentShedWidth)) {
        shedWidthSelect.value = currentShedWidth;
    } else {
        shedWidthSelect.value = "0";
    }

    if (validShedLengths.includes(currentShedLength)) {
        shedLengthSelect.value = currentShedLength;
    } else {
        shedLengthSelect.value = "0";
    }
}
