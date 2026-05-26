// ========== GEOLOCATION API ==========
document.getElementById('getLocation').addEventListener('click', () => {
    const resultDiv = document.getElementById('locationResult');
    
    if (!navigator.geolocation) {
        showResult(resultDiv, 'Geolocation is not supported by your browser', 'error');
        return;
    }
    
    showResult(resultDiv, 'Getting your location...', 'info');
    
    navigator.geolocation.getCurrentPosition(
        (position) => {
            const { latitude, longitude } = position.coords;
            showResult(resultDiv, `
                <strong>Location found!</strong><br>
                Latitude: ${latitude.toFixed(6)}<br>
                Longitude: ${longitude.toFixed(6)}<br>
                <a href="https://www.google.com/maps?q=${latitude},${longitude}" target="_blank">View on Google Maps</a>
            `, 'success');
        },
        (error) => {
            showResult(resultDiv, `Error: ${error.message}`, 'error');
        }
    );
});

// ========== FETCH API ==========
document.getElementById('fetchUser').addEventListener('click', async () => {
    const resultDiv = document.getElementById('userResult');
    showResult(resultDiv, 'Fetching random user...', 'info');
    
    try {
        const response = await fetch('https://randomuser.me/api/');
        const data = await response.json();
        const user = data.results[0];
        
        showResult(resultDiv, `
            <div class="user-card">
                <img src="${user.picture.large}" alt="${user.name.first}">
                <div class="user-info">
                    <h3>${user.name.first} ${user.name.last}</h3>
                    <p><strong>Email:</strong> ${user.email}</p>
                    <p><strong>Location:</strong> ${user.location.city}, ${user.location.country}</p>
                    <p><strong>Phone:</strong> ${user.phone}</p>
                </div>
            </div>
        `, 'success');
    } catch (error) {
        showResult(resultDiv, `Error fetching data: ${error.message}`, 'error');
    }
});

// ========== LOCAL STORAGE API ==========
document.getElementById('saveData').addEventListener('click', () => {
    const input = document.getElementById('storageInput');
    const resultDiv = document.getElementById('storageResult');
    
    if (!input.value.trim()) {
        showResult(resultDiv, 'Please enter some text first!', 'error');
        return;
    }
    
    localStorage.setItem('savedData', input.value);
    showResult(resultDiv, `Data saved: "${input.value}"`, 'success');
});

document.getElementById('loadData').addEventListener('click', () => {
    const resultDiv = document.getElementById('storageResult');
    const savedData = localStorage.getItem('savedData');
    
    if (savedData) {
        document.getElementById('storageInput').value = savedData;
        showResult(resultDiv, `Data loaded: "${savedData}"`, 'success');
    } else {
        showResult(resultDiv, 'No data found in storage', 'info');
    }
});

document.getElementById('clearData').addEventListener('click', () => {
    const resultDiv = document.getElementById('storageResult');
    localStorage.removeItem('savedData');
    document.getElementById('storageInput').value = '';
    showResult(resultDiv, 'Storage cleared!', 'info');
});

// ========== DATE API ==========
document.getElementById('calculateDate').addEventListener('click', () => {
    const selectedDate = document.getElementById('selectedDate').value;
    const resultDiv = document.getElementById('dateResult');
    
    if (!selectedDate) {
        showResult(resultDiv, 'Please select a date first!', 'error');
        return;
    }
    
    const selected = new Date(selectedDate);
    const today = new Date();
    const diffTime = selected - today;
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
    
    let message = '';
    if (diffDays === 0) {
        message = "That's today!";
    } else if (diffDays > 0) {
        message = `That's ${diffDays} days in the future`;
    } else {
        message = `That was ${Math.abs(diffDays)} days ago`;
    }
    
    showResult(resultDiv, `
        <strong>Selected Date:</strong> ${selected.toLocaleDateString()}<br>
        <strong>Today:</strong> ${today.toLocaleDateString()}<br>
        <strong>Difference:</strong> ${message}
    `, 'success');
});

// ========== CANVAS API ==========
const canvas = document.getElementById('myCanvas');
const ctx = canvas.getContext('2d');

document.getElementById('drawCircle').addEventListener('click', () => {
    const x = Math.random() * (canvas.width - 100) + 50;
    const y = Math.random() * (canvas.height - 100) + 50;
    const radius = Math.random() * 30 + 20;
    const color = `hsl(${Math.random() * 360}, 70%, 60%)`;
    
    ctx.beginPath();
    ctx.arc(x, y, radius, 0, 2 * Math.PI);
    ctx.fillStyle = color;
    ctx.fill();
    ctx.strokeStyle = '#333';
    ctx.lineWidth = 2;
    ctx.stroke();
});

document.getElementById('drawRectangle').addEventListener('click', () => {
    const x = Math.random() * (canvas.width - 120);
    const y = Math.random() * (canvas.height - 80);
    const width = Math.random() * 80 + 40;
    const height = Math.random() * 60 + 30;
    const color = `hsl(${Math.random() * 360}, 70%, 60%)`;
    
    ctx.fillStyle = color;
    ctx.fillRect(x, y, width, height);
    ctx.strokeStyle = '#333';
    ctx.lineWidth = 2;
    ctx.strokeRect(x, y, width, height);
});

document.getElementById('clearCanvas').addEventListener('click', () => {
    ctx.clearRect(0, 0, canvas.width, canvas.height);
});

// ========== NOTIFICATIONS API ==========
document.getElementById('requestNotification').addEventListener('click', async () => {
    const resultDiv = document.getElementById('notificationResult');
    
    if (!('Notification' in window)) {
        showResult(resultDiv, 'This browser does not support notifications', 'error');
        return;
    }
    
    try {
        const permission = await Notification.requestPermission();
        
        if (permission === 'granted') {
            showResult(resultDiv, 'Notification permission granted!', 'success');
            document.getElementById('sendNotification').disabled = false;
        } else {
            showResult(resultDiv, 'Notification permission denied', 'error');
        }
    } catch (error) {
        showResult(resultDiv, `Error: ${error.message}`, 'error');
    }
});

document.getElementById('sendNotification').addEventListener('click', () => {
    const resultDiv = document.getElementById('notificationResult');
    
    if (Notification.permission === 'granted') {
        new Notification('Hello! 👋', {
            body: 'This is a test notification from the Browser API Examples page!',
            icon: 'https://via.placeholder.com/128',
            tag: 'demo-notification'
        });
        showResult(resultDiv, 'Notification sent!', 'success');
    } else {
        showResult(resultDiv, 'Please grant notification permission first', 'error');
    }
});

// ========== CLIPBOARD API ==========
document.getElementById('copyText').addEventListener('click', async () => {
    const text = document.getElementById('clipboardInput').value;
    const resultDiv = document.getElementById('clipboardResult');
    
    try {
        await navigator.clipboard.writeText(text);
        showResult(resultDiv, `Copied to clipboard: "${text}"`, 'success');
    } catch (error) {
        showResult(resultDiv, `Failed to copy: ${error.message}`, 'error');
    }
});

document.getElementById('pasteText').addEventListener('click', async () => {
    const resultDiv = document.getElementById('clipboardResult');
    
    try {
        const text = await navigator.clipboard.readText();
        document.getElementById('clipboardInput').value = text;
        showResult(resultDiv, `Pasted from clipboard: "${text}"`, 'success');
    } catch (error) {
        showResult(resultDiv, `Failed to paste: ${error.message}`, 'error');
    }
});

// ========== BATTERY STATUS API ==========
document.getElementById('getBattery').addEventListener('click', async () => {
    const resultDiv = document.getElementById('batteryResult');
    
    if (!('getBattery' in navigator)) {
        showResult(resultDiv, 'Battery Status API is not supported on this device', 'error');
        return;
    }
    
    try {
        const battery = await navigator.getBattery();
        const level = (battery.level * 100).toFixed(0);
        const charging = battery.charging ? 'Yes' : 'No';
        const chargingTime = battery.chargingTime === Infinity ? 'N/A' : `${battery.chargingTime} seconds`;
        const dischargingTime = battery.dischargingTime === Infinity ? 'N/A' : `${battery.dischargingTime} seconds`;
        
        showResult(resultDiv, `
            <strong>Battery Level:</strong> ${level}%<br>
            <strong>Charging:</strong> ${charging}<br>
            <strong>Charging Time:</strong> ${chargingTime}<br>
            <strong>Discharging Time:</strong> ${dischargingTime}
        `, 'success');
    } catch (error) {
        showResult(resultDiv, `Error: ${error.message}`, 'error');
    }
});

// ========== WEB SPEECH API ==========
document.getElementById('speakText').addEventListener('click', () => {
    const text = document.getElementById('speechText').value;
    const resultDiv = document.getElementById('speechResult');
    
    if (!('speechSynthesis' in window)) {
        showResult(resultDiv, 'Text-to-Speech is not supported in this browser', 'error');
        return;
    }
    
    if (!text.trim()) {
        showResult(resultDiv, 'Please enter some text first!', 'error');
        return;
    }
    
    const utterance = new SpeechSynthesisUtterance(text);
    utterance.rate = 1;
    utterance.pitch = 1;
    utterance.volume = 1;
    
    utterance.onstart = () => {
        showResult(resultDiv, 'Speaking...', 'info');
    };
    
    utterance.onend = () => {
        showResult(resultDiv, 'Finished speaking!', 'success');
    };
    
    utterance.onerror = (event) => {
        showResult(resultDiv, `Speech error: ${event.error}`, 'error');
    };
    
    speechSynthesis.speak(utterance);
});

// ========== INTERSECTION OBSERVER API ==========
const observerBox = document.getElementById('observerBox');
const observerResult = document.getElementById('observerResult');

const observer = new IntersectionObserver((entries) => {
    entries.forEach(entry => {
        if (entry.isIntersecting) {
            entry.target.classList.add('visible');
            showResult(observerResult, 'Box is now visible in viewport! 🎉', 'success');
        } else {
            entry.target.classList.remove('visible');
            showResult(observerResult, 'Box is outside viewport', 'info');
        }
    });
}, {
    threshold: 0.5
});

observer.observe(observerBox);

// ========== HELPER FUNCTION ==========
function showResult(element, message, type = 'info') {
    element.innerHTML = message;
    element.className = `result show ${type}`;
}

// Initialize on page load
window.addEventListener('load', () => {
    // Check if notification permission is already granted
    if ('Notification' in window && Notification.permission === 'granted') {
        document.getElementById('sendNotification').disabled = false;
    }
});
