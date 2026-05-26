// Simple JSON Loader - Basic direct fetching

// Load personal points
function loadPersonalPoints() {
    console.log("Loading personal points...");
    fetch('file_operations_residents.json')
        .then(response => {
            console.log("Response status:", response.status);
            return response.json();
        })
        .then(data => {
            console.log("Residents data:", data);
            displayPersonalPoints(data);
        })
        .catch(error => {
            console.error("Error loading personal points:", error);
            document.getElementById('points-list').innerHTML = '<p>Error loading data</p>';
        });
}

function displayPersonalPoints(data) {
    const container = document.getElementById('points-list');
    
    if (!Array.isArray(data) || data.length === 0) {
        container.innerHTML = '<div class="points-header"><div>Resident ID</div><div>Personal Points</div></div><p style="text-align:center;padding:20px;color:#666;">No data available</p>';
        return;
    }
    
    let html = '<div class="points-header"><div>Resident ID</div><div>Personal Points</div></div>';
    
    for (const resident of data) {
        html += '<div class="points-row" onclick="showBoostModal(' + 
                (resident.id || 0) + ', ' + 
                (resident.hasBoost || false) + ')">';
        html += '<div>' + (resident.id || 'N/A') + '</div>';
        html += '<div>' + (resident.personalPoints || 0) + '</div>';
        html += '</div>';
    }
    
    container.innerHTML = html;
}

function showBoostModal(residentId, hasBoost) {
    const statusText = hasBoost ? '🚀 Active Boost' : 'No Boost';
    document.getElementById('modal-boost-status').textContent = statusText;
    document.getElementById('boost-modal').style.display = 'block';
}

// Load tasks
function loadTasks() {
    console.log("Loading tasks...");
    fetch('file_operations_tasks.json')
        .then(response => {
            console.log("Response status:", response.status);
            return response.json();
        })
        .then(data => {
            console.log("Tasks data:", data);
            displayTasks(data);
        })
        .catch(error => {
            console.error("Error loading tasks:", error);
            document.getElementById('tasks-list').innerHTML = '<p>Error loading data</p>';
        });
}

function displayTasks(data) {
    const container = document.getElementById('tasks-list');
    
    if (!Array.isArray(data) || data.length === 0) {
        container.innerHTML = '<div class="task-header"><div>Task Name</div><div>Type</div><div>Points</div></div><p style="text-align:center;padding:20px;color:#666;">No data available</p>';
        return;
    }
    
    let html = '<div class="task-header"><div>Task Name</div><div>Type</div><div>Points</div></div>';
    
    for (const task of data) {
        html += '<div class="task-row" onclick="showTaskModal(\'' + 
                escapeHtml(task.name || 'N/A') + '\', \'' + 
                escapeHtml(task.type || 'N/A') + '\', ' + 
                (task.points || 0) + ', \'' + 
                escapeHtml(task.description || 'No description available') + '\')">';
        html += '<div>' + (task.name || 'N/A') + '</div>';
        html += '<div>' + (task.type || 'N/A') + '</div>';
        html += '<div>' + (task.points || 0) + '</div>';
        html += '</div>';
    }
    
    container.innerHTML = html;
}

function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML.replace(/'/g, "\\'");
}

function showTaskModal(name, type, points, description) {
    document.getElementById('modal-task-name').textContent = name;
    document.getElementById('modal-task-type').textContent = type;
    document.getElementById('modal-task-points').textContent = points;
    document.getElementById('modal-task-description').textContent = description;
    document.getElementById('task-modal').style.display = 'block';
}

// Close modal when clicking X or outside
window.onclick = function(event) {
    const modal = document.getElementById('task-modal');
    if (event.target === modal || event.target.className === 'close') {
        modal.style.display = 'none';
    }
}

// Load trades
function loadTrades() {
    console.log("Loading trades...");
    fetch('file_operations_trades.json')
        .then(response => {
            console.log("Response status:", response.status);
            return response.json();
        })
        .then(data => {
            console.log("Trades data:", data);
            displayTrades(data);
        })
        .catch(error => {
            console.error("Error loading trades:", error);
            document.getElementById('trades-list').innerHTML = '<p>Error loading data</p>';
        });
}

function displayTrades(data) {
    const container = document.getElementById('trades-list');
    
    if (!Array.isArray(data) || data.length === 0) {
        container.innerHTML = '<div class="trade-header"><div>Trade Name</div><div>Trader</div><div>Points</div></div><p style="text-align:center;padding:20px;color:#666;">No data available</p>';
        return;
    }
    
    let html = '<div class="trade-header"><div>Trade Name</div><div>Trader</div><div>Points</div></div>';
    
    for (const trade of data) {
        html += '<div class="trade-row" onclick="showTradeModal(\'' + 
                escapeHtml(trade.name || 'N/A') + '\', \'' + 
                escapeHtml(trade.traderName || 'Unknown') + '\', ' + 
                (trade.pointCost || 0) + ', \'' + 
                escapeHtml(trade.description || 'No description available') + '\')">';
        html += '<div>' + (trade.name || 'N/A') + '</div>';
        html += '<div>' + (trade.traderName || 'Unknown') + '</div>';
        html += '<div>' + (trade.pointCost || 0) + '</div>';
        html += '</div>';
    }
    
    container.innerHTML = html;
}

function showTradeModal(name, trader, points, description) {
    document.getElementById('modal-trade-name').textContent = name;
    document.getElementById('modal-trade-trader').textContent = trader;
    document.getElementById('modal-trade-points').textContent = points;
    document.getElementById('modal-trade-description').textContent = description;
    document.getElementById('trade-modal').style.display = 'block';
}

// Update window.onclick to handle all modals
window.onclick = function(event) {
    const taskModal = document.getElementById('task-modal');
    const tradeModal = document.getElementById('trade-modal');
    const boostModal = document.getElementById('boost-modal');
    
    if (taskModal && (event.target === taskModal || event.target.className === 'close')) {
        taskModal.style.display = 'none';
    }
    if (tradeModal && (event.target === tradeModal || event.target.className === 'close')) {
        tradeModal.style.display = 'none';
    }
    if (boostModal && (event.target === boostModal || event.target.className === 'close')) {
        boostModal.style.display = 'none';
    }
}

// Load community points for homepage
function loadCommunityProgress() {
    console.log("Loading community progress...");
    fetch('file_operations_community.json')
        .then(response => response.json())
        .then(data => {
            const greenPoints = data.greenPoints || 0;
            const pointGoal = data.pointGoal || 100;
            const communityReward = data.communityReward || 'Unknown Goal';
            const percentage = Math.min((greenPoints / pointGoal) * 100, 100);
            
            document.getElementById('goal-title').textContent = 'Community Goal Progress to ' + communityReward;
            document.getElementById('progress-text').textContent = greenPoints + ' / ' + pointGoal + ' Green Points';
            document.getElementById('progress-fill').style.width = percentage + '%';
            document.getElementById('progress-status').textContent = 'Progress toward next community reward: ' + percentage.toFixed(1) + '%';
        })
        .catch(error => {
            console.error('Error loading community points:', error);
            document.getElementById('progress-text').textContent = 'Unable to load progress';
            document.getElementById('progress-status').textContent = 'Please check back later';
        });
}

// Auto-load on page load
window.addEventListener('load', function() {
    const path = window.location.pathname;
    if (path.includes('personalpoints.html')) {
        loadPersonalPoints();
    } else if (path.includes('tasks.html')) {
        loadTasks();
    } else if (path.includes('trades.html')) {
        loadTrades();
    } else if (path.includes('index.html') || path.endsWith('/docs/') || path.endsWith('/docs')) {
        loadCommunityProgress();
    }
});


// ============================================
// ===== JSON: STRUCTURE, USAGE, AND CONVERSION =====
// ============================================
// This section explains what JSON is, its structure, and how to convert
// between JSON strings and JavaScript objects.


// ============================================
// WHAT IS JSON?
// ============================================
// JSON = JavaScript Object Notation
// - A text-based format for representing structured data
// - Language-independent (most languages can read/write JSON)
// - Human-readable and easy to parse
// - Primarily used for data exchange between servers and web apps
//
// KEY DIFFERENCE from JavaScript objects:
// - JSON is a STRING representation of data
// - JavaScript objects are live data structures in memory
// - You need to convert between them!


// ============================================
// JSON SYNTAX RULES (STRICT!)
// ============================================
// JSON is MORE RESTRICTIVE than JavaScript object literals:
//
// 1) Property names MUST be in DOUBLE quotes (not single)
//    ✅ {"name": "John"}
//    ❌ {name: "John"}       // missing quotes
//    ❌ {'name': 'John'}     // single quotes
//
// 2) String values MUST use DOUBLE quotes
//    ✅ {"city": "Paris"}
//    ❌ {"city": 'Paris'}    // single quotes not allowed
//
// 3) NO trailing commas
//    ✅ {"a": 1, "b": 2}
//    ❌ {"a": 1, "b": 2,}    // trailing comma
//
// 4) NO comments allowed
//    ❌ {"name": "John"} // this is a comment  <-- not valid JSON
//
// 5) NO undefined, NO functions, NO symbols, NO dates (as Date objects)
//    ✅ {"value": null}       // null is OK
//    ❌ {"value": undefined}  // undefined not allowed
//    ❌ {"fn": function(){}}  // functions not allowed
//
// 6) Numbers can be integers or floats, but NOT NaN or Infinity
//    ✅ {"count": 42}
//    ✅ {"price": 19.99}
//    ❌ {"value": NaN}
//    ❌ {"value": Infinity}


// ============================================
// JSON DATA TYPES (Only 6 allowed!)
// ============================================
// 1) STRING:   "hello", "123", ""
// 2) NUMBER:   42, -10, 3.14, 1.5e10
// 3) BOOLEAN:  true, false
// 4) NULL:     null
// 5) OBJECT:   {"key": "value"}
// 6) ARRAY:    [1, 2, 3]
//
// That's it! No other types are valid JSON.


// ============================================
// JSON STRUCTURE EXAMPLES
// ============================================

// Simple JSON object (as a string):
const jsonString1 = '{"name": "Alice", "age": 30, "active": true}';

// Nested JSON object:
const jsonString2 = `{
  "user": {
    "id": 123,
    "name": "Bob",
    "email": "bob@example.com"
  },
  "settings": {
    "notifications": true,
    "theme": "dark"
  }
}`;

// JSON array:
const jsonString3 = '[1, 2, 3, 4, 5]';

// Array of objects:
const jsonString4 = `[
  {"id": 1, "name": "Product A", "price": 29.99},
  {"id": 2, "name": "Product B", "price": 49.99},
  {"id": 3, "name": "Product C", "price": 19.99}
]`;

// Complex nested structure:
const jsonString5 = `{
  "company": "TechCorp",
  "employees": [
    {
      "id": 1,
      "name": "Alice",
      "role": "Developer",
      "skills": ["JavaScript", "Python", "SQL"]
    },
    {
      "id": 2,
      "name": "Bob",
      "role": "Designer",
      "skills": ["Figma", "Photoshop", "Illustrator"]
    }
  ],
  "founded": 2020,
  "active": true
}`;


// ============================================
// DESERIALIZATION: JSON string → JavaScript object
// ============================================
// Use: JSON.parse(jsonString)
// - Converts a JSON STRING into a JavaScript OBJECT or ARRAY
// - Throws an error if the JSON is invalid
// - This is how you read JSON data from files, APIs, localStorage, etc.

// Example 1: Parse simple object
const obj1 = JSON.parse('{"name": "Alice", "age": 30}');
console.log(obj1.name);  // "Alice"
console.log(obj1.age);   // 30

// Example 2: Parse array
const arr1 = JSON.parse('[1, 2, 3, 4, 5]');
console.log(arr1[0]);    // 1
console.log(arr1.length); // 5

// Example 3: Parse nested structure
const data1 = JSON.parse('{"user": {"name": "Bob", "age": 25}}');
console.log(data1.user.name); // "Bob"

// Example 4: Handle errors with try-catch
const invalidJson = '{"name": "Alice", "age": 30,}';  // trailing comma!
try {
  const parsed = JSON.parse(invalidJson);
  console.log(parsed);
} catch (error) {
  console.error("Failed to parse JSON:", error.message);
  // Output: "Unexpected token } in JSON at position 27"
}

// Example 5: Parse with reviver function (transform during parsing)
// The reviver is called for each property, bottom-up
const jsonWithDate = '{"name": "Alice", "joined": "2023-01-15"}';
const obj2 = JSON.parse(jsonWithDate, (key, value) => {
  // Convert date strings to Date objects
  if (key === "joined") {
    return new Date(value);
  }
  return value;
});
console.log(obj2.joined);           // Date object
console.log(obj2.joined.getFullYear()); // 2023


// ============================================
// SERIALIZATION: JavaScript object → JSON string
// ============================================
// Use: JSON.stringify(value, replacer, space)
// - Converts a JavaScript OBJECT or ARRAY into a JSON STRING
// - Ignores functions, undefined, symbols
// - This is how you prepare data to send to APIs, save to files, etc.

// Example 1: Stringify simple object
const person = { name: "Alice", age: 30, active: true };
const json1 = JSON.stringify(person);
console.log(json1);  // '{"name":"Alice","age":30,"active":true}'

// Example 2: Stringify array
const numbers = [1, 2, 3, 4, 5];
const json2 = JSON.stringify(numbers);
console.log(json2);  // '[1,2,3,4,5]'

// Example 3: Stringify with pretty-printing (indentation)
const user = {
  name: "Bob",
  age: 25,
  skills: ["JS", "Python"]
};
const json3 = JSON.stringify(user, null, 2);  // 2 spaces indent
console.log(json3);
// Output (formatted):
// {
//   "name": "Bob",
//   "age": 25,
//   "skills": [
//     "JS",
//     "Python"
//   ]
// }

// Example 4: What gets ignored?
const mixed = {
  name: "Alice",           // ✅ included
  age: 30,                 // ✅ included
  greet: function() {},    // ❌ IGNORED (functions not allowed)
  status: undefined,       // ❌ IGNORED (undefined not allowed)
  sym: Symbol("id"),       // ❌ IGNORED (symbols not allowed)
  valid: null              // ✅ included (null is OK)
};
const json4 = JSON.stringify(mixed);
console.log(json4);  // '{"name":"Alice","age":30,"valid":null}'

// Example 5: Stringify with replacer (filter or transform)
// The replacer can be:
// - An array of property names to include
// - A function that transforms each value

// Using array replacer (whitelist):
const full = { name: "Alice", age: 30, password: "secret123" };
const json5 = JSON.stringify(full, ["name", "age"]);  // only these keys
console.log(json5);  // '{"name":"Alice","age":30}'

// Using function replacer (transform):
const data2 = { name: "Alice", age: 30, salary: 50000 };
const json6 = JSON.stringify(data2, (key, value) => {
  // Hide salary (replace with asterisks)
  if (key === "salary") {
    return "***";
  }
  return value;
});
console.log(json6);  // '{"name":"Alice","age":30,"salary":"***"}'

// Example 6: Handle circular references
// JSON.stringify will throw an error on circular references
const obj3 = { name: "Alice" };
obj3.self = obj3;  // circular reference!
try {
  JSON.stringify(obj3);
} catch (error) {
  console.error("Circular reference detected:", error.message);
  // Output: "Converting circular structure to JSON"
}


// ============================================
// COMMON USE CASES
// ============================================

// 1) Local Storage (browser storage)
// Store:
const settings = { theme: "dark", notifications: true };
localStorage.setItem("settings", JSON.stringify(settings));

// Retrieve:
const stored = localStorage.getItem("settings");
if (stored) {
  const parsed = JSON.parse(stored);
  console.log(parsed.theme);  // "dark"
}

// 2) Sending data to a server (API)
const formData = {
  username: "alice",
  email: "alice@example.com",
  age: 30
};

// fetch("/api/users", {
//   method: "POST",
//   headers: { "Content-Type": "application/json" },
//   body: JSON.stringify(formData)  // convert to JSON string
// });

// 3) Receiving data from a server (API)
// fetch("/api/users")
//   .then(response => response.json())  // automatically parses JSON
//   .then(data => {
//     console.log(data);  // JavaScript object/array
//   });

// 4) Deep cloning objects (simple method)
// Note: This only works for JSON-safe data (no functions, dates as objects, etc.)
const original = { name: "Alice", skills: ["JS", "CSS"] };
const clone = JSON.parse(JSON.stringify(original));
clone.skills.push("HTML");
console.log(original.skills);  // ["JS", "CSS"] - original unchanged
console.log(clone.skills);     // ["JS", "CSS", "HTML"] - clone modified

// 5) Configuration files
// Many tools use JSON for config: package.json, tsconfig.json, etc.
// Example: package.json
// {
//   "name": "my-app",
//   "version": "1.0.0",
//   "dependencies": {
//     "express": "^4.18.0"
//   }
// }


// ============================================
// COMMON PITFALLS & TIPS
// ============================================

// PITFALL 1: Forgetting to parse
const response = '{"count": 10}';  // This is a STRING
// console.log(response.count);     // undefined (it's a string!)
const parsed1 = JSON.parse(response);
console.log(parsed1.count);         // 10 ✅

// PITFALL 2: Double-stringifying
const obj4 = { name: "Alice" };
const json7 = JSON.stringify(obj4);
const double = JSON.stringify(json7);  // stringifying a string!
console.log(double);  // '"{\"name\":\"Alice\"}"' - escaped mess!

// PITFALL 3: Dates become strings
const event = { name: "Meeting", date: new Date() };
const json8 = JSON.stringify(event);
console.log(json8);  // date is now a string!
const parsed2 = JSON.parse(json8);
console.log(typeof parsed2.date);  // "string", not Date object

// Solution: Convert back manually or use a reviver
const parsed3 = JSON.parse(json8, (key, value) => {
  if (key === "date") return new Date(value);
  return value;
});
console.log(parsed3.date instanceof Date);  // true ✅

// PITFALL 4: Modifying during iteration
// Be careful when modifying objects while stringifying with a replacer

// TIP 1: Validate JSON before parsing
function isValidJson(str) {
  try {
    JSON.parse(str);
    return true;
  } catch {
    return false;
  }
}
console.log(isValidJson('{"name": "Alice"}'));  // true
console.log(isValidJson('invalid json'));       // false

// TIP 2: Use a schema validator for complex JSON
// Libraries like Ajv, Joi, or Zod can validate JSON structure

// TIP 3: Pretty-print for debugging
const debugData = { name: "Alice", age: 30, skills: ["JS", "Python"] };
console.log(JSON.stringify(debugData, null, 2));  // readable format


// ============================================
// QUICK REFERENCE
// ============================================
//
// Parse (string → object):
//   JSON.parse(jsonString)
//   JSON.parse(jsonString, reviverFunction)
//
// Stringify (object → string):
//   JSON.stringify(value)
//   JSON.stringify(value, replacerArray)
//   JSON.stringify(value, replacerFunction)
//   JSON.stringify(value, null, indentSpaces)
//   JSON.stringify(value, replacerFunction, indentSpaces)
//
// Valid JSON types: string, number, boolean, null, object, array
// Invalid: undefined, function, symbol, Date (as object), NaN, Infinity
//
// JSON rules: double quotes, no trailing commas, no comments, no undefined/functions


// ============================================
// ===== FETCH API: ERROR HANDLING =====
// ============================================
// This section explains how to properly handle errors when using fetch()
// to make HTTP requests.


// ============================================
// IMPORTANT: fetch() DOESN'T REJECT ON HTTP ERRORS!
// ============================================
// KEY CONCEPT: fetch() only rejects for NETWORK FAILURES, not HTTP errors.
//
// ✅ fetch() RESOLVES for:  200 OK, 201 Created, 204 No Content
// ✅ fetch() ALSO RESOLVES for: 404 Not Found, 500 Server Error, 403 Forbidden
// ❌ fetch() REJECTS only for: Network down, DNS lookup failed, CORS errors
//
// This is different from other HTTP libraries (like axios)!


// ============================================
// BASIC ERROR PATTERNS
// ============================================

// ❌ WRONG: This WON'T catch 404 or 500 errors!
function wrongErrorHandling() {
  fetch('https://api.example.com/data')
    .then(response => response.json())  // This runs even on 404!
    .then(data => console.log(data))
    .catch(error => {
      // This only catches NETWORK errors, not HTTP errors!
      console.error("Network error:", error);
    });
}


// ✅ CORRECT: Check response.ok before parsing
function correctErrorHandling() {
  fetch('https://api.example.com/data')
    .then(response => {
      // response.ok is true for status 200-299
      if (!response.ok) {
        // Throw an error for bad status codes
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      return response.json();
    })
    .then(data => {
      console.log("Success:", data);
    })
    .catch(error => {
      // Now this catches BOTH network errors AND HTTP errors
      console.error("Error:", error.message);
    });
}


// ============================================
// RESPONSE OBJECT PROPERTIES
// ============================================
// Understanding the response object helps with error handling

fetch('https://api.example.com/data')
  .then(response => {
    console.log(response.ok);         // true if status 200-299
    console.log(response.status);     // HTTP status code (200, 404, 500, etc.)
    console.log(response.statusText); // Status text ("OK", "Not Found", etc.)
    console.log(response.headers);    // Headers object
    console.log(response.url);        // Final URL (after redirects)
    console.log(response.redirected); // true if redirected
    
    // Check status
    if (!response.ok) {
      throw new Error(`${response.status} ${response.statusText}`);
    }
    return response.json();
  })
  .catch(error => {
    console.error("Error:", error);
  });


// ============================================
// DETAILED ERROR HANDLING
// ============================================

function detailedErrorHandling(url) {
  fetch(url)
    .then(response => {
      // Handle different status codes differently
      if (response.ok) {
        return response.json();  // Success: 200-299
      }
      
      // Client errors (4xx)
      if (response.status >= 400 && response.status < 500) {
        if (response.status === 404) {
          throw new Error("Resource not found (404)");
        } else if (response.status === 401) {
          throw new Error("Unauthorized - Please log in (401)");
        } else if (response.status === 403) {
          throw new Error("Forbidden - Access denied (403)");
        } else {
          throw new Error(`Client error: ${response.status}`);
        }
      }
      
      // Server errors (5xx)
      if (response.status >= 500) {
        throw new Error(`Server error: ${response.status} - Please try again later`);
      }
      
      // Other status codes
      throw new Error(`Unexpected status: ${response.status}`);
    })
    .then(data => {
      console.log("Success:", data);
    })
    .catch(error => {
      console.error("Request failed:", error.message);
      // Show user-friendly message to the user
      displayErrorMessage(error.message);
    });
}


// ============================================
// ASYNC/AWAIT ERROR HANDLING (Modern Approach)
// ============================================

// Using try-catch with async/await (cleaner syntax)
async function fetchWithAsyncAwait(url) {
  try {
    const response = await fetch(url);
    
    // Check if response is OK
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    
    const data = await response.json();
    console.log("Success:", data);
    return data;
    
  } catch (error) {
    // Catches BOTH network errors AND thrown errors
    console.error("Error:", error.message);
    throw error;  // Re-throw if caller needs to handle it
  }
}


// ============================================
// CHECKING JSON PARSING ERRORS
// ============================================

// Sometimes the response isn't valid JSON (e.g., HTML error page)
async function safeJsonParsing(url) {
  try {
    const response = await fetch(url);
    
    if (!response.ok) {
      throw new Error(`HTTP ${response.status}: ${response.statusText}`);
    }
    
    // Check content type before parsing as JSON
    const contentType = response.headers.get("content-type");
    if (!contentType || !contentType.includes("application/json")) {
      throw new Error("Response is not JSON");
    }
    
    const data = await response.json();
    return data;
    
  } catch (error) {
    if (error instanceof SyntaxError) {
      // JSON parsing failed
      console.error("Invalid JSON:", error);
      throw new Error("Server returned invalid JSON");
    } else {
      // Network or HTTP error
      console.error("Request error:", error);
      throw error;
    }
  }
}


// ============================================
// TIMEOUT HANDLING
// ============================================

// fetch() doesn't have a built-in timeout, so we need to implement it

function fetchWithTimeout(url, timeoutMs = 5000) {
  // Create a promise that rejects after timeout
  const timeoutPromise = new Promise((_, reject) => {
    setTimeout(() => {
      reject(new Error(`Request timeout after ${timeoutMs}ms`));
    }, timeoutMs);
  });
  
  // Race between fetch and timeout
  return Promise.race([
    fetch(url),
    timeoutPromise
  ])
    .then(response => {
      if (!response.ok) {
        throw new Error(`HTTP ${response.status}`);
      }
      return response.json();
    })
    .catch(error => {
      console.error("Fetch error:", error.message);
      throw error;
    });
}

// Usage:
// fetchWithTimeout('https://api.example.com/slow', 3000)
//   .then(data => console.log(data))
//   .catch(error => console.error(error.message));


// Using AbortController (modern approach, better cancellation)
async function fetchWithAbortTimeout(url, timeoutMs = 5000) {
  const controller = new AbortController();
  const signal = controller.signal;
  
  // Set timeout to abort the request
  const timeoutId = setTimeout(() => {
    controller.abort();
  }, timeoutMs);
  
  try {
    const response = await fetch(url, { signal });
    clearTimeout(timeoutId);  // Clear timeout if request completes
    
    if (!response.ok) {
      throw new Error(`HTTP ${response.status}`);
    }
    
    return await response.json();
    
  } catch (error) {
    clearTimeout(timeoutId);
    
    if (error.name === 'AbortError') {
      throw new Error(`Request timeout after ${timeoutMs}ms`);
    }
    throw error;
  }
}


// ============================================
// RETRY LOGIC
// ============================================

// Retry failed requests (useful for temporary network issues)
async function fetchWithRetry(url, maxRetries = 3, delayMs = 1000) {
  let lastError;
  
  for (let attempt = 1; attempt <= maxRetries; attempt++) {
    try {
      console.log(`Attempt ${attempt} of ${maxRetries}`);
      
      const response = await fetch(url);
      
      if (!response.ok) {
        throw new Error(`HTTP ${response.status}`);
      }
      
      return await response.json();  // Success!
      
    } catch (error) {
      lastError = error;
      console.warn(`Attempt ${attempt} failed:`, error.message);
      
      // Don't retry on client errors (4xx)
      if (error.message.includes('4')) {
        throw error;
      }
      
      // Wait before retrying (except on last attempt)
      if (attempt < maxRetries) {
        console.log(`Retrying in ${delayMs}ms...`);
        await new Promise(resolve => setTimeout(resolve, delayMs));
        delayMs *= 2;  // Exponential backoff
      }
    }
  }
  
  throw new Error(`Failed after ${maxRetries} attempts: ${lastError.message}`);
}


// ============================================
// COMPREHENSIVE ERROR HANDLER
// ============================================

// Production-ready fetch wrapper with all error handling
async function robustFetch(url, options = {}) {
  const {
    timeout = 10000,
    retries = 2,
    ...fetchOptions
  } = options;
  
  const controller = new AbortController();
  const timeoutId = setTimeout(() => controller.abort(), timeout);
  
  try {
    const response = await fetch(url, {
      ...fetchOptions,
      signal: controller.signal
    });
    
    clearTimeout(timeoutId);
    
    // Log for debugging
    console.log(`${fetchOptions.method || 'GET'} ${url} → ${response.status}`);
    
    // Handle different status codes
    if (response.ok) {
      // Check if there's content to parse
      const contentLength = response.headers.get('content-length');
      if (contentLength === '0') {
        return null;  // No content
      }
      
      // Parse based on content type
      const contentType = response.headers.get('content-type');
      if (contentType?.includes('application/json')) {
        return await response.json();
      } else if (contentType?.includes('text/')) {
        return await response.text();
      } else {
        return await response.blob();
      }
    }
    
    // HTTP errors
    let errorMessage = `HTTP ${response.status}: ${response.statusText}`;
    
    // Try to get error details from response body
    try {
      const errorBody = await response.json();
      if (errorBody.message) {
        errorMessage += ` - ${errorBody.message}`;
      }
    } catch {
      // Couldn't parse error body, use default message
    }
    
    throw new Error(errorMessage);
    
  } catch (error) {
    clearTimeout(timeoutId);
    
    // Categorize the error
    if (error.name === 'AbortError') {
      throw new Error(`Request timeout after ${timeout}ms`);
    } else if (error.message.includes('Failed to fetch')) {
      throw new Error('Network error - please check your connection');
    } else if (error.message.includes('CORS')) {
      throw new Error('CORS error - server does not allow this request');
    } else {
      throw error;  // Pass through other errors
    }
  }
}

// Usage examples:
// const data = await robustFetch('https://api.example.com/data');
// const result = await robustFetch('https://api.example.com/users', {
//   method: 'POST',
//   headers: { 'Content-Type': 'application/json' },
//   body: JSON.stringify({ name: 'Alice' }),
//   timeout: 5000
// });


// ============================================
// USER-FRIENDLY ERROR DISPLAY
// ============================================

// Function to show errors to users (not just console)
function displayErrorMessage(message) {
  // Example: Show in a dedicated error container
  const errorContainer = document.getElementById('error-message');
  if (errorContainer) {
    errorContainer.textContent = message;
    errorContainer.style.display = 'block';
    
    // Auto-hide after 5 seconds
    setTimeout(() => {
      errorContainer.style.display = 'none';
    }, 5000);
  }
}

// Example with loading state
async function fetchWithLoadingState(url) {
  const loadingEl = document.getElementById('loading');
  const errorEl = document.getElementById('error');
  const dataEl = document.getElementById('data');
  
  try {
    // Show loading
    if (loadingEl) loadingEl.style.display = 'block';
    if (errorEl) errorEl.style.display = 'none';
    if (dataEl) dataEl.style.display = 'none';
    
    const response = await fetch(url);
    
    if (!response.ok) {
      throw new Error(`Failed to load data (${response.status})`);
    }
    
    const data = await response.json();
    
    // Show data
    if (loadingEl) loadingEl.style.display = 'none';
    if (dataEl) {
      dataEl.textContent = JSON.stringify(data, null, 2);
      dataEl.style.display = 'block';
    }
    
  } catch (error) {
    // Show error
    if (loadingEl) loadingEl.style.display = 'none';
    if (errorEl) {
      errorEl.textContent = error.message;
      errorEl.style.display = 'block';
    }
    console.error('Fetch error:', error);
  }
}


// ============================================
// ERROR TYPES & SCENARIOS
// ============================================

/*
TYPE 1: Network Errors
- No internet connection
- DNS lookup failed
- Server unreachable
- CORS blocked
→ fetch() REJECTS (caught in .catch())
→ error.message: "Failed to fetch", "Network request failed"

TYPE 2: HTTP Errors
- 404 Not Found
- 500 Internal Server Error
- 401 Unauthorized
- 403 Forbidden
→ fetch() RESOLVES (response.ok is false)
→ Must manually check response.ok or response.status

TYPE 3: Timeout Errors
- Request takes too long
→ Need AbortController or Promise.race
→ error.name: "AbortError"

TYPE 4: JSON Parse Errors
- Server returns non-JSON (HTML error page)
- Malformed JSON
→ response.json() throws SyntaxError
→ error: SyntaxError

TYPE 5: CORS Errors
- Server doesn't allow cross-origin requests
→ fetch() REJECTS
→ error.message: "CORS policy" or "Failed to fetch"
*/


// ============================================
// BEST PRACTICES SUMMARY
// ============================================

/*
✅ DO:
1. Always check response.ok before parsing
2. Use try-catch with async/await for cleaner code
3. Provide user-friendly error messages
4. Implement timeouts for long requests
5. Check content-type before parsing as JSON
6. Log errors for debugging
7. Handle different status codes appropriately
8. Consider retry logic for transient errors
9. Show loading states to users
10. Cancel requests when component unmounts (in React/Vue)

❌ DON'T:
1. Assume .catch() catches HTTP errors (it doesn't!)
2. Parse response.json() without checking response.ok
3. Ignore content-type headers
4. Show technical error messages to users
5. Retry on client errors (4xx)
6. Forget to clear timeouts
7. Let fetch() run forever without timeout
8. Parse JSON without try-catch
*/


// ============================================
// QUICK REFERENCE
// ============================================

/*
// Basic pattern:
fetch(url)
  .then(response => {
    if (!response.ok) throw new Error(`HTTP ${response.status}`);
    return response.json();
  })
  .then(data => console.log(data))
  .catch(error => console.error(error));

// Async/await pattern:
try {
  const response = await fetch(url);
  if (!response.ok) throw new Error(`HTTP ${response.status}`);
  const data = await response.json();
} catch (error) {
  console.error(error);
}

// With timeout:
const controller = new AbortController();
setTimeout(() => controller.abort(), 5000);
const response = await fetch(url, { signal: controller.signal });

// Response properties:
response.ok         // true if 200-299
response.status     // 200, 404, 500, etc.
response.statusText // "OK", "Not Found", etc.
response.headers    // Headers object
response.json()     // Parse as JSON
response.text()     // Get as text
response.blob()     // Get as blob
*/


// ============================================
// FETCH API: COMPLETE GUIDE
// ============================================
// How to send and receive data from a web server

/*
┌─────────────────────────────────────────────────────────────┐
│ WHAT IS THE FETCH API?                                      │
└─────────────────────────────────────────────────────────────┘

The Fetch API is a modern JavaScript interface for making HTTP requests
to web servers. It replaces the older XMLHttpRequest approach.

KEY FEATURES:
✓ Promise-based (works with .then() and async/await)
✓ Cleaner, more intuitive syntax
✓ Better error handling
✓ Supports all HTTP methods (GET, POST, PUT, DELETE, etc.)
✓ Built-in JSON parsing
✓ Works with different content types
✓ Available in all modern browsers

BASIC CONCEPT:
1. You call fetch() with a URL
2. It returns a Promise
3. The Promise resolves to a Response object
4. You extract data from the Response (json(), text(), blob(), etc.)
5. Handle errors in .catch() or try/catch


┌─────────────────────────────────────────────────────────────┐
│ 1. BASIC GET REQUEST                                        │
└─────────────────────────────────────────────────────────────┘

GET is for RETRIEVING data from the server.
This is the default HTTP method.
*/

// Example 1A: Basic GET request (Promise syntax)
function getUserBasic(userId) {
    // fetch() returns a Promise
    fetch(`https://api.example.com/users/${userId}`)
        .then(response => {
            // response.ok is true if status is 200-299
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            // Parse JSON from response body
            return response.json();
        })
        .then(data => {
            // Now we have the actual data
            console.log('User data:', data);
            console.log('Name:', data.name);
            console.log('Email:', data.email);
        })
        .catch(error => {
            // Catches network errors AND errors we threw
            console.error('Error fetching user:', error);
        });
}

// Example 1B: Basic GET request (async/await syntax)
async function getUserAsync(userId) {
    try {
        // await pauses until Promise resolves
        const response = await fetch(`https://api.example.com/users/${userId}`);
        
        // Check if request was successful
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        
        // Parse JSON from response
        const data = await response.json();
        
        console.log('User data:', data);
        return data;
        
    } catch (error) {
        console.error('Error fetching user:', error);
        throw error; // Re-throw if you want caller to handle it
    }
}

// Example 1C: GET request with query parameters
async function searchUsers(searchTerm, limit = 10) {
    // Build URL with query parameters
    const params = new URLSearchParams({
        q: searchTerm,
        limit: limit,
        sort: 'name'
    });
    
    // URLSearchParams converts to: ?q=searchTerm&limit=10&sort=name
    const url = `https://api.example.com/users/search?${params}`;
    
    try {
        const response = await fetch(url);
        
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        
        const users = await response.json();
        console.log(`Found ${users.length} users matching "${searchTerm}"`);
        return users;
        
    } catch (error) {
        console.error('Search failed:', error);
        return [];
    }
}

// Usage examples:
// getUserBasic(123);
// const user = await getUserAsync(456);
// const results = await searchUsers('john', 20);


/*
┌─────────────────────────────────────────────────────────────┐
│ 2. POST REQUEST (Sending Data)                             │
└─────────────────────────────────────────────────────────────┘

POST is for CREATING new resources on the server.
You send data in the request body.

IMPORTANT OPTIONS:
- method: 'POST' (required, default is GET)
- headers: Tells server what format you're sending
- body: The actual data (must be a string)
*/

// Example 2A: POST request with JSON data
async function createUser(userData) {
    try {
        const response = await fetch('https://api.example.com/users', {
            method: 'POST',  // Specify that this is a POST request
            
            headers: {
                // Tell server we're sending JSON
                'Content-Type': 'application/json',
                // Often need authentication token
                'Authorization': 'Bearer YOUR_TOKEN_HERE'
            },
            
            body: JSON.stringify(userData)  // Convert object to JSON string
            // IMPORTANT: body must be a STRING, not an object!
            // That's why we use JSON.stringify()
        });
        
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        
        const newUser = await response.json();
        console.log('User created:', newUser);
        console.log('New user ID:', newUser.id);
        return newUser;
        
    } catch (error) {
        console.error('Failed to create user:', error);
        throw error;
    }
}

// Usage:
// const userData = {
//     name: 'John Doe',
//     email: 'john@example.com',
//     age: 30
// };
// const newUser = await createUser(userData);


// Example 2B: POST request with form data (like a form submission)
async function uploadUserProfile(formData) {
    // FormData is used when uploading files or sending form data
    // It automatically sets the correct Content-Type header
    
    try {
        const response = await fetch('https://api.example.com/profile', {
            method: 'POST',
            // NOTE: Don't set Content-Type header when using FormData!
            // Browser sets it automatically with correct boundary
            body: formData  // FormData object, not a string
        });
        
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        
        const result = await response.json();
        console.log('Profile uploaded:', result);
        return result;
        
    } catch (error) {
        console.error('Upload failed:', error);
        throw error;
    }
}

// Usage with file upload:
// const formData = new FormData();
// formData.append('name', 'John Doe');
// formData.append('avatar', fileInput.files[0]);  // File from <input type="file">
// await uploadUserProfile(formData);


// Example 2C: POST with URL-encoded data (like traditional HTML forms)
async function submitFormData(formData) {
    // URL-encoded: key1=value1&key2=value2
    // This is how traditional HTML forms send data
    
    const params = new URLSearchParams(formData);
    
    try {
        const response = await fetch('https://api.example.com/submit', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: params.toString()  // Converts to: name=John&email=john@example.com
        });
        
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        
        const result = await response.json();
        return result;
        
    } catch (error) {
        console.error('Form submission failed:', error);
        throw error;
    }
}


/*
┌─────────────────────────────────────────────────────────────┐
│ 3. PUT & PATCH REQUESTS (Updating Data)                    │
└─────────────────────────────────────────────────────────────┘

PUT: Replace entire resource (send ALL fields)
PATCH: Update specific fields (send ONLY changed fields)
*/

// Example 3A: PUT request (full update)
async function updateUserFull(userId, userData) {
    // PUT replaces the entire resource
    // You must send ALL fields, even unchanged ones
    
    try {
        const response = await fetch(`https://api.example.com/users/${userId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer YOUR_TOKEN_HERE'
            },
            body: JSON.stringify(userData)
        });
        
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        
        const updatedUser = await response.json();
        console.log('User fully updated:', updatedUser);
        return updatedUser;
        
    } catch (error) {
        console.error('Update failed:', error);
        throw error;
    }
}

// Usage - must send ALL fields:
// const fullUserData = {
//     name: 'John Doe',
//     email: 'newemail@example.com',
//     age: 31,
//     address: '123 Main St',
//     phone: '555-0123'
// };
// await updateUserFull(123, fullUserData);


// Example 3B: PATCH request (partial update)
async function updateUserPartial(userId, changes) {
    // PATCH updates only the fields you send
    // Other fields remain unchanged
    
    try {
        const response = await fetch(`https://api.example.com/users/${userId}`, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer YOUR_TOKEN_HERE'
            },
            body: JSON.stringify(changes)
        });
        
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        
        const updatedUser = await response.json();
        console.log('User partially updated:', updatedUser);
        return updatedUser;
        
    } catch (error) {
        console.error('Partial update failed:', error);
        throw error;
    }
}

// Usage - send ONLY changed fields:
// const changes = {
//     email: 'newemail@example.com'  // Only updating email
// };
// await updateUserPartial(123, changes);


/*
┌─────────────────────────────────────────────────────────────┐
│ 4. DELETE REQUEST                                           │
└─────────────────────────────────────────────────────────────┘

DELETE is for removing resources from the server.
*/

// Example 4: DELETE request
async function deleteUser(userId) {
    try {
        const response = await fetch(`https://api.example.com/users/${userId}`, {
            method: 'DELETE',
            headers: {
                'Authorization': 'Bearer YOUR_TOKEN_HERE'
            }
            // Usually no body needed for DELETE
        });
        
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        
        // Some APIs return deleted resource, some return empty response
        // Check if there's content before parsing
        const contentType = response.headers.get('content-type');
        let result;
        
        if (contentType && contentType.includes('application/json')) {
            result = await response.json();
        } else {
            result = await response.text();
        }
        
        console.log('User deleted:', result);
        return result;
        
    } catch (error) {
        console.error('Delete failed:', error);
        throw error;
    }
}

// Usage:
// await deleteUser(123);


/*
┌─────────────────────────────────────────────────────────────┐
│ 5. WORKING WITH HEADERS                                     │
└─────────────────────────────────────────────────────────────┘

Headers contain metadata about the request/response.
*/

// Example 5A: Reading response headers
async function getUserWithHeaders(userId) {
    try {
        const response = await fetch(`https://api.example.com/users/${userId}`);
        
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        
        // Reading response headers
        console.log('Content-Type:', response.headers.get('content-type'));
        console.log('Date:', response.headers.get('date'));
        console.log('Server:', response.headers.get('server'));
        
        // Check if response is JSON before parsing
        const contentType = response.headers.get('content-type');
        if (!contentType || !contentType.includes('application/json')) {
            throw new Error('Response is not JSON!');
        }
        
        // Iterate through all headers
        console.log('\nAll headers:');
        for (let [key, value] of response.headers) {
            console.log(`${key}: ${value}`);
        }
        
        const data = await response.json();
        return data;
        
    } catch (error) {
        console.error('Error:', error);
        throw error;
    }
}


// Example 5B: Sending custom headers
async function apiRequestWithHeaders(endpoint, data) {
    const response = await fetch(`https://api.example.com/${endpoint}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer YOUR_TOKEN_HERE',
            'X-API-Key': 'your-api-key',
            'X-Requested-With': 'XMLHttpRequest',
            'Accept': 'application/json',
            'Accept-Language': 'en-US',
            'Custom-Header': 'custom-value'
        },
        body: JSON.stringify(data)
    });
    
    return response.json();
}


/*
┌─────────────────────────────────────────────────────────────┐
│ 6. READING DIFFERENT RESPONSE TYPES                         │
└─────────────────────────────────────────────────────────────┘

The Response object has several methods for reading different formats.
*/

// Example 6A: Reading JSON
async function fetchJSON(url) {
    const response = await fetch(url);
    if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
    
    const data = await response.json();  // Parses JSON string to JavaScript object
    return data;
}

// Example 6B: Reading plain text
async function fetchText(url) {
    const response = await fetch(url);
    if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
    
    const text = await response.text();  // Gets response as plain text string
    return text;
}

// Example 6C: Reading as Blob (for files, images, etc.)
async function fetchImage(url) {
    const response = await fetch(url);
    if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
    
    const blob = await response.blob();  // Gets binary data as Blob
    
    // Create object URL for image
    const imageUrl = URL.createObjectURL(blob);
    
    // Use in <img> tag
    const img = document.createElement('img');
    img.src = imageUrl;
    document.body.appendChild(img);
    
    return blob;
}

// Example 6D: Reading as ArrayBuffer (for binary data)
async function fetchBinary(url) {
    const response = await fetch(url);
    if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
    
    const buffer = await response.arrayBuffer();  // Gets raw binary data
    return buffer;
}

// Example 6E: Reading as FormData
async function fetchFormData(url) {
    const response = await fetch(url);
    if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
    
    const formData = await response.formData();  // Parses multipart/form-data
    return formData;
}

/*
IMPORTANT: You can only call ONE of these methods per response!
Once you call .json(), .text(), .blob(), etc., the response body is consumed.
You CANNOT call .json() and then .text() on the same response.
*/


/*
┌─────────────────────────────────────────────────────────────┐
│ 7. COMPLETE REAL-WORLD EXAMPLES                            │
└─────────────────────────────────────────────────────────────┘
*/

// Example 7A: Complete CRUD operations for a blog post
class BlogAPI {
    constructor(baseURL, token) {
        this.baseURL = baseURL;
        this.token = token;
    }
    
    // Helper method for making requests
    async request(endpoint, options = {}) {
        const url = `${this.baseURL}${endpoint}`;
        
        const config = {
            ...options,
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${this.token}`,
                ...options.headers
            }
        };
        
        try {
            const response = await fetch(url, config);
            
            if (!response.ok) {
                const error = await response.json().catch(() => ({}));
                throw new Error(error.message || `HTTP error! status: ${response.status}`);
            }
            
            // Handle empty responses (like 204 No Content)
            if (response.status === 204) {
                return null;
            }
            
            return await response.json();
            
        } catch (error) {
            console.error(`Request failed: ${options.method || 'GET'} ${endpoint}`, error);
            throw error;
        }
    }
    
    // CREATE: Create a new blog post
    async createPost(postData) {
        return this.request('/posts', {
            method: 'POST',
            body: JSON.stringify(postData)
        });
    }
    
    // READ: Get all posts
    async getAllPosts(page = 1, limit = 10) {
        return this.request(`/posts?page=${page}&limit=${limit}`);
    }
    
    // READ: Get single post
    async getPost(postId) {
        return this.request(`/posts/${postId}`);
    }
    
    // UPDATE: Update entire post
    async updatePost(postId, postData) {
        return this.request(`/posts/${postId}`, {
            method: 'PUT',
            body: JSON.stringify(postData)
        });
    }
    
    // UPDATE: Partially update post
    async patchPost(postId, changes) {
        return this.request(`/posts/${postId}`, {
            method: 'PATCH',
            body: JSON.stringify(changes)
        });
    }
    
    // DELETE: Delete post
    async deletePost(postId) {
        return this.request(`/posts/${postId}`, {
            method: 'DELETE'
        });
    }
    
    // SEARCH: Search posts
    async searchPosts(query) {
        const params = new URLSearchParams({ q: query });
        return this.request(`/posts/search?${params}`);
    }
}

// Usage of BlogAPI class:
/*
const blog = new BlogAPI('https://api.example.com', 'your-token-here');

// Create
const newPost = await blog.createPost({
    title: 'My First Post',
    content: 'Hello, World!',
    author: 'John Doe'
});

// Read
const posts = await blog.getAllPosts(1, 20);
const post = await blog.getPost(123);

// Update
await blog.updatePost(123, {
    title: 'Updated Title',
    content: 'Updated content',
    author: 'John Doe'
});

// Partial update
await blog.patchPost(123, { title: 'New Title' });

// Delete
await blog.deletePost(123);

// Search
const results = await blog.searchPosts('javascript');
*/


// Example 7B: File upload with progress
async function uploadFileWithProgress(file, onProgress) {
    // Create FormData
    const formData = new FormData();
    formData.append('file', file);
    formData.append('description', 'User uploaded file');
    
    try {
        // Note: fetch() doesn't support upload progress tracking directly
        // For progress, you need to use XMLHttpRequest
        // But here's the basic fetch approach:
        
        const response = await fetch('https://api.example.com/upload', {
            method: 'POST',
            body: formData
            // Don't set Content-Type! Browser sets it with boundary
        });
        
        if (!response.ok) {
            throw new Error(`Upload failed! status: ${response.status}`);
        }
        
        const result = await response.json();
        console.log('File uploaded successfully:', result);
        return result;
        
    } catch (error) {
        console.error('Upload error:', error);
        throw error;
    }
}

// Usage:
// const fileInput = document.querySelector('input[type="file"]');
// const file = fileInput.files[0];
// await uploadFileWithProgress(file);


// Example 7C: Authenticated requests with token refresh
class AuthAPI {
    constructor(baseURL) {
        this.baseURL = baseURL;
        this.accessToken = null;
        this.refreshToken = null;
    }
    
    // Login and get tokens
    async login(username, password) {
        const response = await fetch(`${this.baseURL}/auth/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, password })
        });
        
        if (!response.ok) {
            throw new Error('Login failed');
        }
        
        const data = await response.json();
        this.accessToken = data.accessToken;
        this.refreshToken = data.refreshToken;
        
        return data;
    }
    
    // Refresh access token when it expires
    async refreshAccessToken() {
        const response = await fetch(`${this.baseURL}/auth/refresh`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ refreshToken: this.refreshToken })
        });
        
        if (!response.ok) {
            throw new Error('Token refresh failed');
        }
        
        const data = await response.json();
        this.accessToken = data.accessToken;
        
        return data;
    }
    
    // Make authenticated request
    async request(endpoint, options = {}) {
        const config = {
            ...options,
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${this.accessToken}`,
                ...options.headers
            }
        };
        
        let response = await fetch(`${this.baseURL}${endpoint}`, config);
        
        // If unauthorized, try refreshing token
        if (response.status === 401) {
            console.log('Token expired, refreshing...');
            await this.refreshAccessToken();
            
            // Retry request with new token
            config.headers.Authorization = `Bearer ${this.accessToken}`;
            response = await fetch(`${this.baseURL}${endpoint}`, config);
        }
        
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        
        return response.json();
    }
}


/*
┌─────────────────────────────────────────────────────────────┐
│ 8. BEST PRACTICES & TIPS                                   │
└─────────────────────────────────────────────────────────────┘

✅ DO:
1. Always check response.ok before parsing
2. Use try/catch for error handling
3. Set appropriate headers (Content-Type, Authorization)
4. Use async/await for cleaner code
5. Add timeouts for long requests (using AbortController)
6. Validate response content type before parsing
7. Use URLSearchParams for query strings
8. Use JSON.stringify() for request body
9. Handle both network errors and HTTP errors
10. Use appropriate HTTP methods (GET, POST, PUT, PATCH, DELETE)

❌ DON'T:
1. Don't forget to check response.ok
2. Don't forget JSON.stringify() for body
3. Don't set Content-Type for FormData (browser does it)
4. Don't call .json() twice on same response
5. Don't expose sensitive tokens in code
6. Don't use GET for sensitive data (use POST)
7. Don't ignore error handling
8. Don't forget await when using async functions
9. Don't trust response without validation
10. Don't make requests without timeout


┌─────────────────────────────────────────────────────────────┐
│ 9. HTTP METHODS SUMMARY                                     │
└─────────────────────────────────────────────────────────────┘

GET     - Retrieve data (no body)
POST    - Create new resource (with body)
PUT     - Replace entire resource (with body)
PATCH   - Update specific fields (with body)
DELETE  - Remove resource (usually no body)
HEAD    - Like GET but only headers (no body returned)
OPTIONS - Get allowed methods (used for CORS)


┌─────────────────────────────────────────────────────────────┐
│ 10. QUICK REFERENCE TEMPLATES                               │
└─────────────────────────────────────────────────────────────┘
*/

// GET request
async function getTemplate(url) {
    try {
        const response = await fetch(url);
        if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
        const data = await response.json();
        return data;
    } catch (error) {
        console.error('Error:', error);
        throw error;
    }
}

// POST request
async function postTemplate(url, data) {
    try {
        const response = await fetch(url, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });
        if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
        const result = await response.json();
        return result;
    } catch (error) {
        console.error('Error:', error);
        throw error;
    }
}

// PUT request
async function putTemplate(url, data) {
    try {
        const response = await fetch(url, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });
        if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
        const result = await response.json();
        return result;
    } catch (error) {
        console.error('Error:', error);
        throw error;
    }
}

// PATCH request
async function patchTemplate(url, changes) {
    try {
        const response = await fetch(url, {
            method: 'PATCH',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(changes)
        });
        if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
        const result = await response.json();
        return result;
    } catch (error) {
        console.error('Error:', error);
        throw error;
    }
}

// DELETE request
async function deleteTemplate(url) {
    try {
        const response = await fetch(url, { method: 'DELETE' });
        if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
        return true;
    } catch (error) {
        console.error('Error:', error);
        throw error;
    }
}

/*
┌─────────────────────────────────────────────────────────────┐
│ SUMMARY                                                      │
└─────────────────────────────────────────────────────────────┘

Fetch API is your tool for communicating with web servers.

Basic pattern:
  fetch(url, options) → Promise<Response> → data

1. Call fetch() with URL and options
2. Await the response
3. Check if response.ok
4. Parse the data (.json(), .text(), etc.)
5. Handle errors in catch block

Remember:
- Use appropriate HTTP methods
- Set correct headers
- Stringify request body
- Check response status
- Handle errors properly
- Add timeouts for reliability
*/
