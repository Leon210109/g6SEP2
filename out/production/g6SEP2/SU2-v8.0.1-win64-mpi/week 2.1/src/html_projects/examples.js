// ===== CREATE ARRAYS =====
const fruits = ["apple", "banana", "cherry"];
console.log("Initial array:", fruits);

// You can also create empty arrays
const numbers = [];
console.log("Empty array:", numbers);


// ===== ACCESSING ELEMENTS =====
console.log("First fruit:", fruits[0]);     // apple
console.log("Last fruit:", fruits[fruits.length - 1]); // cherry


// ===== ADDING ELEMENTS =====

// Add to END
fruits.push("orange");
console.log("After push:", fruits);

// Add to START
fruits.unshift("mango");
console.log("After unshift:", fruits);

// Add at specific index (without removing anything)
fruits.splice(2, 0, "grape");
console.log("After splice insert:", fruits);


// ===== REMOVING ELEMENTS =====

// Remove from END
const last = fruits.pop();
console.log("Removed last:", last);
console.log("After pop:", fruits);

// Remove from START
const first = fruits.shift();
console.log("Removed first:", first);
console.log("After shift:", fruits);

// Remove from specific index
fruits.splice(1, 1); // remove 1 item at index 1
console.log("After splice remove:", fruits);


// ===== REPLACING ELEMENTS =====

// Replace item at index
fruits[1] = "kiwi";
console.log("After replace:", fruits);

// Replace using splice
fruits.splice(0, 1, "pineapple");
console.log("After splice replace:", fruits);


// ===== ITERATING ARRAYS =====

// Classic for loop
console.log("For loop:");
for (let i = 0; i < fruits.length; i++) {
  console.log(i, fruits[i]);
}

// for...of loop (simpler)
console.log("for...of loop:");
for (const fruit of fruits) {
  console.log(fruit);
}

// forEach method
console.log("forEach:");
fruits.forEach((fruit, index) => {
  console.log(index, fruit);
});


// ===== TRANSFORMING ARRAYS =====

// map — create new array from each item
const upper = fruits.map(f => f.toUpperCase());
console.log("Mapped to uppercase:", upper);

// filter — keep matching items
const longNames = fruits.filter(f => f.length > 5);
console.log("Filtered (length > 5):", longNames);

// find — first match
const found = fruits.find(f => f.startsWith("k"));
console.log("Found starting with k:", found);


// ===== CHECKING & SEARCHING =====

console.log("Includes 'kiwi'?", fruits.includes("kiwi"));
console.log("Index of 'kiwi':", fruits.indexOf("kiwi"));


// ===== COPYING ARRAYS =====

// Shallow copy
const copy = [...fruits];
console.log("Copy:", copy);


// ===== JOINING & SPLITTING =====

const joined = fruits.join(", ");
console.log("Joined string:", joined);

const fromString = joined.split(", ");
console.log("Split back to array:", fromString);


// ============================================
// ===== JAVASCRIPT OBJECTS vs JSON =====
// ============================================

/*
┌─────────────────────────────────────────────────────────────┐
│ WHAT ARE JAVASCRIPT OBJECTS?                                │
└─────────────────────────────────────────────────────────────┘

JavaScript objects are collections of key-value pairs.
They're THE fundamental data structure in JavaScript.

BASIC STRUCTURE:
{
  key: value,
  property: data,
  name: "value"
}

Keys (properties) are strings (or Symbols)
Values can be ANYTHING: strings, numbers, arrays, functions, other objects, etc.
*/


// ===== CREATING OBJECTS =====

// Method 1: Object literal (most common)
const person = {
    name: "John Doe",
    age: 30,
    email: "john@example.com",
    isActive: true
};
console.log("Person object:", person);

// Method 2: new Object() (less common)
const car = new Object();
car.brand = "Toyota";
car.model = "Camry";
car.year = 2024;
console.log("Car object:", car);

// Method 3: Object.create()
const prototype = { species: "human" };
const human = Object.create(prototype);
human.name = "Alice";
console.log("Human object:", human);


// ===== ACCESSING PROPERTIES =====

// Dot notation (most common)
console.log("Name:", person.name);           // "John Doe"
console.log("Age:", person.age);             // 30

// Bracket notation (when property name is dynamic or has spaces)
console.log("Email:", person["email"]);      // "john@example.com"

const propertyName = "age";
console.log("Dynamic access:", person[propertyName]); // 30

// Property with spaces (must use bracket notation)
const settings = {
    "background color": "blue",
    "font size": 16
};
console.log(settings["background color"]);   // "blue"


// ===== ADDING & MODIFYING PROPERTIES =====

// Add new property
person.phone = "123-456-7890";
person["country"] = "USA";
console.log("After adding:", person);

// Modify existing property
person.age = 31;
person["email"] = "newemail@example.com";
console.log("After modifying:", person);


// ===== DELETING PROPERTIES =====

delete person.phone;
console.log("After deleting phone:", person);


// ===== CHECKING IF PROPERTY EXISTS =====

// Method 1: in operator
console.log("Has name?", "name" in person);        // true
console.log("Has phone?", "phone" in person);      // false

// Method 2: hasOwnProperty (recommended)
console.log("Has age?", person.hasOwnProperty("age"));  // true

// Method 3: Check if undefined
console.log("Email exists?", person.email !== undefined); // true


// ===== NESTED OBJECTS =====

const user = {
    name: "Alice",
    age: 25,
    address: {
        street: "123 Main St",
        city: "New York",
        country: "USA",
        coordinates: {
            lat: 40.7128,
            lng: -74.0060
        }
    },
    hobbies: ["reading", "coding", "gaming"]
};

// Accessing nested properties
console.log("City:", user.address.city);                    // "New York"
console.log("Latitude:", user.address.coordinates.lat);     // 40.7128
console.log("First hobby:", user.hobbies[0]);               // "reading"

// Optional chaining (safe access - won't crash if property doesn't exist)
console.log("ZIP:", user.address?.zipCode);                 // undefined (no error)
console.log("Lat:", user.address?.coordinates?.lat);        // 40.7128


// ===== OBJECTS WITH METHODS (FUNCTIONS) =====

const calculator = {
    value: 0,
    
    // Method: function inside object
    add: function(num) {
        this.value += num;
        return this;  // Return this for chaining
    },
    
    // Shorthand method syntax (ES6+)
    subtract(num) {
        this.value -= num;
        return this;
    },
    
    multiply(num) {
        this.value *= num;
        return this;
    },
    
    getValue() {
        return this.value;
    },
    
    reset() {
        this.value = 0;
        return this;
    }
};

// Using methods
calculator.add(10).multiply(2).subtract(5);
console.log("Calculator result:", calculator.getValue());   // 15

// Arrow functions as methods (DON'T use for methods that need 'this')
const badExample = {
    value: 10,
    getValue: () => {
        // ❌ Arrow functions don't have their own 'this'
        // 'this' refers to outer scope, not the object!
        return this.value;  // Won't work as expected
    }
};


// ===== ITERATING OVER OBJECTS =====

const product = {
    name: "Laptop",
    price: 999,
    brand: "Dell",
    inStock: true
};

// Method 1: for...in loop
console.log("=== for...in loop ===");
for (let key in product) {
    console.log(`${key}: ${product[key]}`);
}

// Method 2: Object.keys() - Get array of keys
console.log("=== Object.keys() ===");
const keys = Object.keys(product);
console.log("Keys:", keys);                     // ["name", "price", "brand", "inStock"]

keys.forEach(key => {
    console.log(`${key}: ${product[key]}`);
});

// Method 3: Object.values() - Get array of values
console.log("=== Object.values() ===");
const values = Object.values(product);
console.log("Values:", values);                 // ["Laptop", 999, "Dell", true]

// Method 4: Object.entries() - Get array of [key, value] pairs
console.log("=== Object.entries() ===");
const entries = Object.entries(product);
console.log("Entries:", entries);
// [["name", "Laptop"], ["price", 999], ["brand", "Dell"], ["inStock", true]]

entries.forEach(([key, value]) => {
    console.log(`${key}: ${value}`);
});


// ===== COPYING OBJECTS =====

const original = {
    name: "Original",
    value: 100,
    nested: {
        data: "test"
    }
};

// ❌ WRONG: Assignment creates reference, not copy
const notACopy = original;
notACopy.name = "Changed";
console.log(original.name);  // "Changed" - original was modified too!

// ✅ Shallow copy - Method 1: Spread operator
const shallowCopy1 = { ...original };
shallowCopy1.name = "Copy";
console.log(original.name);  // "Original" - not affected
console.log(shallowCopy1.name); // "Copy"

// But nested objects are still references!
shallowCopy1.nested.data = "modified";
console.log(original.nested.data);  // "modified" - nested object was affected!

// ✅ Shallow copy - Method 2: Object.assign()
const shallowCopy2 = Object.assign({}, original);

// ✅ Deep copy - Method 1: JSON parse/stringify (simple objects only)
const deepCopy1 = JSON.parse(JSON.stringify(original));
deepCopy1.nested.data = "deep change";
console.log(original.nested.data);  // "modified" - not affected by deep copy

// ✅ Deep copy - Method 2: structuredClone() (modern, best)
const deepCopy2 = structuredClone(original);


// ===== MERGING OBJECTS =====

const defaults = {
    theme: "light",
    fontSize: 14,
    showSidebar: true
};

const userSettings = {
    fontSize: 16,
    language: "en"
};

// Merge using spread operator (later properties override earlier ones)
const settings2 = { ...defaults, ...userSettings };
console.log("Merged settings:", settings2);
// { theme: "light", fontSize: 16, showSidebar: true, language: "en" }

// Merge using Object.assign()
const settings3 = Object.assign({}, defaults, userSettings);


// ===== OBJECT DESTRUCTURING =====

const employee = {
    firstName: "John",
    lastName: "Doe",
    position: "Developer",
    salary: 75000,
    department: "Engineering"
};

// Extract properties into variables
const { firstName, lastName, position } = employee;
console.log(firstName);  // "John"
console.log(position);   // "Developer"

// Rename while destructuring
const { firstName: fName, lastName: lName } = employee;
console.log(fName);      // "John"

// Default values
const { firstName: fn, bonus = 5000 } = employee;
console.log(bonus);      // 5000 (default, since bonus doesn't exist)

// Nested destructuring
const company = {
    name: "TechCorp",
    location: {
        city: "San Francisco",
        state: "CA"
    }
};

const { location: { city, state } } = company;
console.log(city);       // "San Francisco"

// Rest operator (collect remaining properties)
const { firstName: f, lastName: l, ...rest } = employee;
console.log(rest);       // { position: "Developer", salary: 75000, department: "Engineering" }


/*
┌─────────────────────────────────────────────────────────────┐
│ WHAT IS JSON?                                               │
└─────────────────────────────────────────────────────────────┘

JSON = JavaScript Object Notation

A TEXT FORMAT for storing and exchanging data.
NOT the same as JavaScript objects!

KEY DIFFERENCES:

JAVASCRIPT OBJECT:
- Lives in memory (data structure)
- Can contain functions, undefined, Date objects, etc.
- Keys can be unquoted if valid identifiers
- Can have trailing commas
- Can have comments

JSON:
- Text/string format (for transmission/storage)
- Only data, no functions or methods
- Keys MUST be double-quoted strings
- No trailing commas
- No comments allowed
- Limited data types: string, number, boolean, null, array, object

ALLOWED IN JSON:
✅ Strings (double quotes only)
✅ Numbers
✅ Booleans (true/false)
✅ null
✅ Arrays
✅ Objects (nested)

NOT ALLOWED IN JSON:
❌ Functions
❌ undefined
❌ Date objects (must convert to string/number)
❌ Single quotes
❌ Trailing commas
❌ Comments
❌ NaN, Infinity
❌ Symbol, BigInt
*/


// ===== VALID JSON EXAMPLE =====

// This is a VALID JSON string:
const validJSONString = `{
  "name": "John Doe",
  "age": 30,
  "email": "john@example.com",
  "isActive": true,
  "balance": null,
  "hobbies": ["reading", "coding", "gaming"],
  "address": {
    "street": "123 Main St",
    "city": "New York"
  }
}`;

console.log("Valid JSON string:", validJSONString);


// ===== INVALID JSON EXAMPLES =====

// ❌ INVALID: Single quotes
// '{ 'name': 'John' }'

// ❌ INVALID: Unquoted keys
// { name: "John" }

// ❌ INVALID: Trailing comma
// { "name": "John", }

// ❌ INVALID: Function
// { "getName": function() { return this.name; } }

// ❌ INVALID: undefined
// { "name": "John", "age": undefined }

// ❌ INVALID: Date object
// { "date": new Date() }

// ❌ INVALID: Comments
// { "name": "John" /* comment */ }


// ===== CONVERTING: JavaScript Object ↔ JSON =====

// JavaScript Object
const jsObject = {
    name: "Alice",
    age: 25,
    hobbies: ["reading", "coding"],
    isActive: true,
    balance: null
};

// Convert JavaScript Object → JSON string
const jsonString = JSON.stringify(jsObject);
console.log("JSON string:", jsonString);
console.log("Type:", typeof jsonString);  // "string"
// Result: '{"name":"Alice","age":25,"hobbies":["reading","coding"],"isActive":true,"balance":null}'

// Convert JSON string → JavaScript Object
const parsedObject = JSON.parse(jsonString);
console.log("Parsed object:", parsedObject);
console.log("Type:", typeof parsedObject);  // "object"
console.log("Name:", parsedObject.name);    // "Alice"


// ===== JSON.stringify() OPTIONS =====

const data = {
    name: "John",
    age: 30,
    city: "New York",
    getValue: function() { return this.age; },  // Function (will be ignored)
    undefinedValue: undefined  // undefined (will be ignored)
};

// Basic stringify
console.log(JSON.stringify(data));
// {"name":"John","age":30,"city":"New York"}
// Note: function and undefined are omitted!

// Pretty print with indentation
console.log(JSON.stringify(data, null, 2));
/*
{
  "name": "John",
  "age": 30,
  "city": "New York"
}
*/

// Custom replacer function (filter/transform properties)
const filtered = JSON.stringify(data, (key, value) => {
    if (key === "age") return undefined;  // Exclude age
    return value;
}, 2);
console.log("Filtered:", filtered);

// Replacer array (only include specified keys)
const selected = JSON.stringify(data, ["name", "city"], 2);
console.log("Selected properties:", selected);


// ===== JSON.parse() WITH REVIVER =====

const jsonDate = '{"name":"Event","date":"2024-01-15T10:00:00.000Z"}';

// Basic parse
const parsed1 = JSON.parse(jsonDate);
console.log("Date type:", typeof parsed1.date);  // "string" (not Date object!)

// Parse with reviver (transform values)
const parsed2 = JSON.parse(jsonDate, (key, value) => {
    // Convert date strings back to Date objects
    if (key === "date") {
        return new Date(value);
    }
    return value;
});
console.log("Date type:", typeof parsed2.date);  // "object" (Date object!)
console.log("Date:", parsed2.date.toLocaleDateString());


// ===== HANDLING JSON ERRORS =====

// Always wrap JSON.parse() in try-catch
const invalidJSON = '{ "name": "John", }';  // Trailing comma - invalid!

try {
    const result = JSON.parse(invalidJSON);
    console.log(result);
} catch (error) {
    console.error("JSON Parse Error:", error.message);
    // "Unexpected token } in JSON at position 18"
}


// ===== REAL-WORLD EXAMPLES =====

// Example 1: Store user preferences in localStorage
function savePreferences(prefs) {
    // Convert object to JSON string for storage
    const jsonString = JSON.stringify(prefs);
    localStorage.setItem('userPreferences', jsonString);
    console.log("Preferences saved!");
}

function loadPreferences() {
    // Retrieve JSON string and parse back to object
    const jsonString = localStorage.getItem('userPreferences');
    
    if (jsonString) {
        try {
            const prefs = JSON.parse(jsonString);
            return prefs;
        } catch (error) {
            console.error("Failed to parse preferences:", error);
            return null;
        }
    }
    
    return null;
}

// Usage:
const preferences = {
    theme: "dark",
    language: "en",
    notifications: true
};
savePreferences(preferences);
const loaded = loadPreferences();
console.log("Loaded preferences:", loaded);


// Example 2: Send data to API
async function createUser(userData) {
    const response = await fetch('/api/users', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'  // Tell server we're sending JSON
        },
        body: JSON.stringify(userData)  // Convert object to JSON string
    });
    
    const result = await response.json();  // Parse JSON response to object
    return result;
}

// Usage:
const newUser = {
    name: "Alice",
    email: "alice@example.com",
    age: 25
};
// createUser(newUser);


// Example 3: Deep clone object with JSON
function deepClone(obj) {
    // Simple deep clone (works for plain objects)
    // Warning: Loses functions, undefined, Date objects, etc.
    return JSON.parse(JSON.stringify(obj));
}

const original2 = {
    name: "Original",
    nested: {
        value: 100
    }
};

const clone = deepClone(original2);
clone.nested.value = 200;
console.log("Original:", original2.nested.value);  // 100 (not affected)
console.log("Clone:", clone.nested.value);         // 200


/*
┌─────────────────────────────────────────────────────────────┐
│ COMPARISON TABLE: JavaScript Objects vs JSON                │
└─────────────────────────────────────────────────────────────┘

Feature              | JavaScript Object        | JSON
---------------------|--------------------------|------------------
Type                 | Data structure (object)  | Text format (string)
Keys                 | Can be unquoted         | Must be double-quoted
Strings              | Single or double quotes | Double quotes only
Functions            | ✅ Allowed              | ❌ Not allowed
undefined            | ✅ Allowed              | ❌ Not allowed
Date objects         | ✅ Allowed              | ❌ Must convert
Comments             | ✅ Allowed              | ❌ Not allowed
Trailing commas      | ✅ Allowed (ES5+)       | ❌ Not allowed
NaN, Infinity        | ✅ Allowed              | ❌ Not allowed
Methods              | ✅ Can have methods     | ❌ Data only
Use case             | In-memory data          | Data exchange/storage
Notation             | { key: "value" }        | { "key": "value" }


┌─────────────────────────────────────────────────────────────┐
│ WHEN TO USE WHICH?                                          │
└─────────────────────────────────────────────────────────────┘

USE JAVASCRIPT OBJECTS WHEN:
✅ Working with data in your application
✅ Need methods and functions
✅ Want to use any JavaScript value type
✅ Building in-memory data structures
✅ Creating complex application logic

USE JSON WHEN:
✅ Sending data over network (APIs)
✅ Storing data in files or databases
✅ Sharing data between different systems
✅ Saving to localStorage/sessionStorage
✅ Data needs to be serialized/deserialized
✅ Need human-readable data format


┌─────────────────────────────────────────────────────────────┐
│ COMMON PATTERNS & BEST PRACTICES                           │
└─────────────────────────────────────────────────────────────┘
*/

// Pattern 1: Configuration objects
const config = {
    apiUrl: "https://api.example.com",
    timeout: 5000,
    retries: 3,
    headers: {
        "Content-Type": "application/json",
        "Authorization": "Bearer token"
    }
};

// Pattern 2: Data transformation
function transformUser(apiUser) {
    // Convert API response to app format
    return {
        id: apiUser.user_id,
        name: apiUser.full_name,
        email: apiUser.email_address,
        isActive: apiUser.status === "active",
        registeredAt: new Date(apiUser.registered_timestamp)
    };
}

// Pattern 3: Object as dictionary/map
const userById = {
    "user1": { name: "Alice", age: 25 },
    "user2": { name: "Bob", age: 30 },
    "user3": { name: "Charlie", age: 35 }
};

// Quick lookup
const selectedUser = userById["user2"];

// Pattern 4: Options object
function createButton(options = {}) {
    const defaults = {
        text: "Click me",
        type: "primary",
        size: "medium",
        disabled: false
    };
    
    // Merge defaults with provided options
    const settings = { ...defaults, ...options };
    
    console.log("Creating button:", settings);
}

createButton({ text: "Submit", type: "success" });

// Pattern 5: Safe JSON parsing
function safeJSONParse(jsonString, defaultValue = null) {
    try {
        return JSON.parse(jsonString);
    } catch (error) {
        console.error("JSON parse failed:", error.message);
        return defaultValue;
    }
}

// Usage:
const data2 = safeJSONParse('invalid json', {});  // Returns {} instead of crashing


/*
┌─────────────────────────────────────────────────────────────┐
│ QUICK REFERENCE SUMMARY                                     │
└─────────────────────────────────────────────────────────────┘

// Create object:
const obj = { key: "value", num: 42 };

// Access properties:
obj.key              // Dot notation
obj["key"]           // Bracket notation

// Add/modify:
obj.newKey = "value";

// Delete:
delete obj.key;

// Check existence:
"key" in obj
obj.hasOwnProperty("key")

// Iterate:
Object.keys(obj)     // ["key", "num"]
Object.values(obj)   // ["value", 42]
Object.entries(obj)  // [["key", "value"], ["num", 42]]

// Copy:
{ ...obj }           // Shallow copy
structuredClone(obj) // Deep copy

// Merge:
{ ...obj1, ...obj2 }

// Destructure:
const { key, num } = obj;

// JSON conversion:
JSON.stringify(obj)  // Object → JSON string
JSON.parse(str)      // JSON string → Object

// Remember:
- JavaScript objects are in-memory data structures
- JSON is a text format for data exchange
- JSON is more restrictive than JS objects
- Always use try-catch with JSON.parse()
- Use JSON for APIs, storage, data exchange
- Use objects for application logic
*/


// ======================================
// ===== EVENT HANDLING IN JAVASCRIPT =====

// ======================================

// Events are actions or occurrences that happen in the browser.
// Examples: user clicks a button, types in a field, submits a form, page loads, etc.
// JavaScript can "listen" for these events and run code in response.

// =====================================
// 1) ADDING AN EVENT LISTENER
// =====================================
// Syntax: element.addEventListener(eventType, handlerFunction)
//
// - element: the DOM element you want to listen to (button, input, document, etc.)
// - eventType: a string like "click", "submit", "keydown", "mouseover", etc.
// - handlerFunction: the function that runs when the event occurs
//
// Example:
// const button = document.querySelector("#myButton");
// button.addEventListener("click", function() {
//   console.log("Button was clicked!");
// });
//
// NOTES:
// - The handler function does NOT run immediately when you call addEventListener.
// - It runs later, only when the event happens.
// - You can add multiple listeners to the same element for the same event.


// =====================================
// 2) THE EVENT OBJECT
// =====================================
// When an event happens, the browser creates an "event object" with details.
// This object is automatically passed to your handler function.
//
// Example:
// button.addEventListener("click", function(event) {
//   console.log(event.type);         // "click"
//   console.log(event.target);       // the element that was clicked
//   console.log(event.currentTarget); // the element the listener is attached to
//   console.log(event.timeStamp);    // when the event happened
// });
//
// NOTES:
// - event.target: the element that actually triggered the event (what was clicked)
// - event.currentTarget: the element the listener is on (can be different if nested)
// - The parameter name doesn't have to be "event" — common names: e, evt, event


// =====================================
// 3) DIFFERENT EVENT TYPES
// =====================================
// There are MANY event types. Here are the most common:
//
// MOUSE EVENTS:
// - "click": user clicks (mousedown + mouseup on same element)
// - "dblclick": double-click
// - "mousedown": mouse button is pressed
// - "mouseup": mouse button is released
// - "mousemove": mouse moves over the element
// - "mouseenter": mouse enters the element (doesn't bubble)
// - "mouseleave": mouse leaves the element (doesn't bubble)
//
// KEYBOARD EVENTS:
// - "keydown": key is pressed (fires continuously if held)
// - "keyup": key is released
// - "keypress": DEPRECATED (use keydown instead)
//
// FORM EVENTS:
// - "submit": form is submitted
// - "input": value of input/textarea changes (fires on every change)
// - "change": value changes AND element loses focus
// - "focus": element receives focus
// - "blur": element loses focus
//
// DOCUMENT/WINDOW EVENTS:
// - "DOMContentLoaded": HTML is parsed, DOM is ready (before images load)
// - "load": entire page including images/styles is loaded
// - "resize": window is resized
// - "scroll": page or element is scrolled
//
// Examples:
// input.addEventListener("input", (e) => {
//   console.log("Current value:", e.target.value);
// });
//
// document.addEventListener("DOMContentLoaded", () => {
//   console.log("DOM is ready!");
// });


// =====================================
// 4) ARROW FUNCTIONS AS HANDLERS
// =====================================
// You can use arrow functions for cleaner syntax:
//
// button.addEventListener("click", (event) => {
//   console.log("Clicked!");
// });
//
// NOTES:
// - Arrow functions are more concise
// - They have different "this" binding (not usually important for event handlers)
// - If you need to remove the listener later, DON'T use an anonymous arrow function


// =====================================
// 5) REMOVING EVENT LISTENERS
// =====================================
// To remove a listener, you need a reference to the SAME function.
//
// Example:
// function handleClick(event) {
//   console.log("Clicked!");
// }
//
// button.addEventListener("click", handleClick);   // add listener
// button.removeEventListener("click", handleClick); // remove listener
//
// NOTES:
// - This WON'T work:
//   button.addEventListener("click", () => console.log("hi"));
//   button.removeEventListener("click", () => console.log("hi"));
//   (Those are two DIFFERENT arrow function objects)
//
// - To remove, you must use the exact same function reference.


// =====================================
// 6) event.preventDefault()
// =====================================
// Prevents the browser's default action for that event.
//
// Examples:
// - Prevent a link from navigating:
//   link.addEventListener("click", (e) => {
//     e.preventDefault();
//     console.log("Link click prevented");
//   });
//
// - Prevent form from submitting/reloading:
//   form.addEventListener("submit", (e) => {
//     e.preventDefault();
//     console.log("Form submit prevented");
//   });
//
// NOTES:
// - Common use: validate form before allowing submit
// - Or: handle click without page navigation


// =====================================
// 7) event.stopPropagation()
// =====================================
// Events "bubble" up: they trigger on the target, then parent, then grandparent, etc.
// stopPropagation() prevents the event from bubbling further.
//
// Example:
// parent.addEventListener("click", () => console.log("parent clicked"));
// child.addEventListener("click", (e) => {
//   e.stopPropagation();
//   console.log("child clicked, but won't bubble to parent");
// });
//
// NOTES:
// - Without stopPropagation, clicking child logs BOTH "child clicked" and "parent clicked"
// - With stopPropagation, only "child clicked" is logged


// =====================================
// 8) EVENT DELEGATION
// =====================================
// Instead of adding listeners to many children, add ONE listener to the parent.
// Use event.target to figure out which child was clicked.
//
// Example:
// const list = document.querySelector("ul");
// list.addEventListener("click", (e) => {
//   if (e.target.tagName === "LI") {
//     console.log("Clicked on:", e.target.textContent);
//   }
// });
//
// NOTES:
// - More efficient when you have many children
// - Works even if children are added/removed dynamically
// - Common pattern: listen on <ul>, check if target is <li>


// =====================================
// 9) COMMON PATTERN: WAITING FOR DOM
// =====================================
// If your <script> is in the <head>, the DOM might not exist yet.
// Use DOMContentLoaded to wait until it's ready.
//
// document.addEventListener("DOMContentLoaded", () => {
//   const button = document.querySelector("#myButton");
//   button.addEventListener("click", () => {
//     console.log("Button clicked!");
//   });
// });
//
// NOTES:
// - If your <script> is at the END of <body>, you don't need this.
// - Alternative: put <script> tag at the bottom, or use defer/async attributes.


// =====================================
// 10) EXAMPLE: KEYBOARD EVENT DETAILS
// =====================================
// Keyboard events have special properties:
//
// input.addEventListener("keydown", (e) => {
//   console.log("Key:", e.key);       // "a", "Enter", "Shift", etc.
//   console.log("Code:", e.code);     // "KeyA", "Enter", "ShiftLeft", etc.
//   console.log("Ctrl?", e.ctrlKey);  // true if Ctrl is held
//   console.log("Alt?", e.altKey);    // true if Alt is held
//   console.log("Shift?", e.shiftKey); // true if Shift is held
// });
//
// NOTES:
// - e.key is the character or key name
// - e.code is the physical key (useful for games/shortcuts)
// - Check ctrlKey, altKey, shiftKey for modifier combos


// =====================================
// CHEATSHEET SUMMARY
// =====================================
//
// Add listener:       element.addEventListener("click", handler);
// Remove listener:    element.removeEventListener("click", handler);
// Event object:       function handler(event) { ... }
// Prevent default:    event.preventDefault();
// Stop bubbling:      event.stopPropagation();
// Target element:     event.target
// Current element:    event.currentTarget
// Keyboard key:       event.key
// Wait for DOM:       document.addEventListener("DOMContentLoaded", ...)
//
// Common events: click, input, submit, keydown, DOMContentLoaded, load, scroll


// ============================================
// TIMING FUNCTIONS: setTimeout & setInterval
// ============================================
// Execute code after a delay or repeatedly at intervals

/*
┌─────────────────────────────────────────────────────────────┐
│ WHAT ARE TIMING FUNCTIONS?                                  │
└─────────────────────────────────────────────────────────────┘

JavaScript provides built-in functions for executing code after a delay
or repeatedly at regular intervals:

1. setTimeout()  - Execute code ONCE after a delay
2. setInterval() - Execute code REPEATEDLY at regular intervals
3. clearTimeout() - Cancel a setTimeout
4. clearInterval() - Cancel a setInterval

IMPORTANT: These are ASYNCHRONOUS operations!
- Code execution doesn't pause while waiting
- Other code continues to run
- The callback function runs when the timer completes


┌─────────────────────────────────────────────────────────────┐
│ 1. setTimeout() - DELAYED EXECUTION                         │
└─────────────────────────────────────────────────────────────┘

Execute a function ONCE after a specified delay.

SYNTAX:
  const timerId = setTimeout(function, delay, arg1, arg2, ...);

PARAMETERS:
- function: The function to execute
- delay: Milliseconds to wait (1000ms = 1 second)
- arg1, arg2, ...: Optional arguments passed to the function

RETURNS:
- A timer ID (number) that can be used with clearTimeout()
*/

// Example 1A: Basic setTimeout
console.log("Start");

setTimeout(function() {
    console.log("This runs after 2 seconds");
}, 2000);

console.log("End");

// OUTPUT ORDER:
// "Start"
// "End"
// (2 seconds pass)
// "This runs after 2 seconds"
//
// Notice "End" logs BEFORE the timeout!
// This is because setTimeout is ASYNCHRONOUS


// Example 1B: setTimeout with arrow function (modern syntax)
setTimeout(() => {
    console.log("This also runs after 2 seconds");
}, 2000);


// Example 1C: setTimeout with named function
function greet() {
    console.log("Hello after delay!");
}

setTimeout(greet, 3000); // Call greet() after 3 seconds
// NOTE: Pass function NAME without (), not greet()


// Example 1D: setTimeout with parameters
function greetPerson(name, time) {
    console.log(`Hello ${name}! It's now ${time}`);
}

// Pass additional arguments after the delay
setTimeout(greetPerson, 1000, "John", "morning");
// After 1 second: "Hello John! It's now morning"


// Example 1E: Real-world use - Delayed notification
function showNotification(message) {
    console.log(`🔔 Notification: ${message}`);
}

// Show notification after 3 seconds
setTimeout(() => {
    showNotification("Your download is complete!");
}, 3000);


// Example 1F: Common pattern - Hide element after delay
// Simulate showing/hiding a message
setTimeout(() => {
    console.log("Hiding success message...");
    // In real code: element.style.display = 'none';
}, 5000);


/*
┌─────────────────────────────────────────────────────────────┐
│ 2. clearTimeout() - CANCEL A TIMEOUT                        │
└─────────────────────────────────────────────────────────────┘

Cancel a timeout before it executes.

SYNTAX:
  clearTimeout(timerId);

WHY USE IT:
- Cancel operations that are no longer needed
- Prevent memory leaks
- Stop scheduled tasks when component unmounts
*/

// Example 2A: Cancel a timeout
const timerId = setTimeout(() => {
    console.log("This will NEVER run");
}, 3000);

// Cancel it immediately
clearTimeout(timerId);
console.log("Timeout cancelled!");


// Example 2B: Conditional cancellation
let messageTimeout = setTimeout(() => {
    console.log("Too slow! Time's up.");
}, 5000);

// User submits form before timeout
function onFormSubmit() {
    clearTimeout(messageTimeout); // Cancel the timeout
    console.log("Form submitted in time!");
}

// Simulate form submission after 2 seconds
setTimeout(onFormSubmit, 2000);


// Example 2C: Debouncing pattern (real-world use case)
// Only execute after user stops typing for 500ms
let searchTimeout;

function onSearchInput(query) {
    // Clear previous timeout
    clearTimeout(searchTimeout);
    
    // Set new timeout
    searchTimeout = setTimeout(() => {
        console.log(`Searching for: ${query}`);
        // In real code: fetch API to search
    }, 500);
}

// Simulate typing
onSearchInput("j");
onSearchInput("ja");
onSearchInput("jav");
onSearchInput("java");
onSearchInput("javas");
onSearchInput("javasc");
onSearchInput("javascr");
onSearchInput("javascri");
onSearchInput("javascript");
// Only searches ONCE after typing stops for 500ms!


/*
┌─────────────────────────────────────────────────────────────┐
│ 3. setInterval() - REPEATED EXECUTION                       │
└─────────────────────────────────────────────────────────────┘

Execute a function REPEATEDLY at regular intervals.

SYNTAX:
  const intervalId = setInterval(function, delay, arg1, arg2, ...);

PARAMETERS:
- function: The function to execute
- delay: Milliseconds between executions
- arg1, arg2, ...: Optional arguments passed to the function

RETURNS:
- An interval ID (number) that can be used with clearInterval()

IMPORTANT: ALWAYS clear intervals when done!
- Intervals run forever until cleared
- Can cause memory leaks if not stopped
- Clear when component unmounts or condition is met
*/

// Example 3A: Basic setInterval
let count = 0;

const intervalId = setInterval(() => {
    count++;
    console.log(`Count: ${count}`);
    
    // Stop after 5 iterations
    if (count >= 5) {
        clearInterval(intervalId);
        console.log("Interval stopped!");
    }
}, 1000); // Runs every 1 second

// OUTPUT:
// Count: 1 (after 1 second)
// Count: 2 (after 2 seconds)
// Count: 3 (after 3 seconds)
// Count: 4 (after 4 seconds)
// Count: 5 (after 5 seconds)
// Interval stopped!


// Example 3B: Clock/Timer
let seconds = 0;

const clockInterval = setInterval(() => {
    seconds++;
    const minutes = Math.floor(seconds / 60);
    const remainingSeconds = seconds % 60;
    console.log(`Time: ${minutes}:${remainingSeconds.toString().padStart(2, '0')}`);
}, 1000);

// Stop after 10 seconds (for demo purposes)
setTimeout(() => {
    clearInterval(clockInterval);
    console.log("Clock stopped");
}, 10000);


// Example 3C: Real-world use - Auto-refresh data
let refreshCount = 0;

function fetchData() {
    refreshCount++;
    console.log(`Fetching data... (attempt ${refreshCount})`);
    // In real code: fetch('/api/data').then(...)
}

// Fetch data every 5 seconds
const refreshInterval = setInterval(fetchData, 5000);

// Stop after 3 refreshes
setTimeout(() => {
    clearInterval(refreshInterval);
    console.log("Auto-refresh stopped");
}, 16000);


// Example 3D: Progress indicator
let progress = 0;

function updateProgress() {
    progress += 10;
    console.log(`Progress: ${progress}%`);
    
    // In real code: progressBar.style.width = progress + '%';
    
    if (progress >= 100) {
        clearInterval(progressInterval);
        console.log("✅ Complete!");
    }
}

const progressInterval = setInterval(updateProgress, 500);


/*
┌─────────────────────────────────────────────────────────────┐
│ 4. clearInterval() - STOP A REPEATING INTERVAL              │
└─────────────────────────────────────────────────────────────┘

Stop an interval from continuing to execute.

SYNTAX:
  clearInterval(intervalId);

CRITICAL: ALWAYS clear intervals!
- Prevents memory leaks
- Stops unnecessary CPU usage
- Essential for cleanup
*/

// Example 4A: Stop interval on condition
let attempts = 0;

const attemptInterval = setInterval(() => {
    attempts++;
    console.log(`Attempt ${attempts}...`);
    
    // Simulate success on 3rd attempt
    if (attempts === 3) {
        console.log("✅ Success!");
        clearInterval(attemptInterval);
    }
}, 1000);


// Example 4B: Stop interval when user interacts
let autoPlayInterval = setInterval(() => {
    console.log("Auto-playing next slide...");
    // In real code: showNextSlide();
}, 3000);

// Stop auto-play when user clicks
function onUserClick() {
    clearInterval(autoPlayInterval);
    console.log("Auto-play stopped by user");
}

// Simulate user click after 8 seconds
setTimeout(onUserClick, 8000);


/*
┌─────────────────────────────────────────────────────────────┐
│ 5. COMMON PATTERNS & REAL-WORLD EXAMPLES                    │
└─────────────────────────────────────────────────────────────┘
*/

// Pattern 5A: Countdown Timer
function createCountdown(seconds) {
    let remaining = seconds;
    
    console.log(`⏱️ Countdown started: ${remaining}s`);
    
    const countdownInterval = setInterval(() => {
        remaining--;
        console.log(`⏱️ ${remaining}s remaining`);
        
        if (remaining <= 0) {
            clearInterval(countdownInterval);
            console.log("🎉 Time's up!");
        }
    }, 1000);
    
    // Return interval ID so it can be cancelled
    return countdownInterval;
}

// Usage:
const myCountdown = createCountdown(5);

// Can cancel early if needed:
// clearInterval(myCountdown);


// Pattern 5B: Delayed execution with cleanup
function delayedAction(action, delay) {
    console.log(`Scheduling action in ${delay}ms...`);
    
    const timerId = setTimeout(() => {
        console.log("Executing action...");
        action();
    }, delay);
    
    // Return cancel function
    return function cancel() {
        clearTimeout(timerId);
        console.log("Action cancelled");
    };
}

// Usage:
const cancel = delayedAction(() => {
    console.log("This is the delayed action!");
}, 3000);

// Can cancel before it runs:
// cancel();


// Pattern 5C: Polling (checking status repeatedly)
function pollUntilComplete(checkFunction, interval = 1000, maxAttempts = 10) {
    let attempts = 0;
    
    const pollInterval = setInterval(() => {
        attempts++;
        console.log(`Polling... attempt ${attempts}`);
        
        // Check if condition is met
        if (checkFunction()) {
            clearInterval(pollInterval);
            console.log("✅ Condition met!");
            return;
        }
        
        // Stop after max attempts
        if (attempts >= maxAttempts) {
            clearInterval(pollInterval);
            console.log("❌ Max attempts reached");
        }
    }, interval);
    
    return pollInterval;
}

// Usage:
let status = "pending";

pollUntilComplete(() => {
    // Check if status is complete
    return status === "complete";
}, 1000, 5);

// Simulate status change after 3 seconds
setTimeout(() => {
    status = "complete";
}, 3000);


// Pattern 5D: Animation loop
function animateElement(duration, onUpdate) {
    const startTime = Date.now();
    
    const animationInterval = setInterval(() => {
        const elapsed = Date.now() - startTime;
        const progress = Math.min(elapsed / duration, 1); // 0 to 1
        
        onUpdate(progress);
        
        if (progress >= 1) {
            clearInterval(animationInterval);
            console.log("Animation complete");
        }
    }, 16); // ~60 FPS (1000ms / 60 ≈ 16ms)
    
    return animationInterval;
}

// Usage:
animateElement(2000, (progress) => {
    console.log(`Animation progress: ${(progress * 100).toFixed(1)}%`);
    // In real code: element.style.left = (progress * 100) + 'px';
});


// Pattern 5E: Rate limiting with setTimeout
function rateLimitedFunction(fn, delay) {
    let timeoutId = null;
    
    return function(...args) {
        if (timeoutId) {
            console.log("Function is rate-limited, please wait");
            return;
        }
        
        // Execute function
        fn.apply(this, args);
        
        // Prevent execution for 'delay' milliseconds
        timeoutId = setTimeout(() => {
            timeoutId = null;
        }, delay);
    };
}

// Usage:
const limitedClick = rateLimitedFunction(() => {
    console.log("Button clicked!");
}, 2000);

// Can only execute once every 2 seconds
limitedClick(); // Works
limitedClick(); // Blocked
limitedClick(); // Blocked
setTimeout(() => limitedClick(), 2500); // Works (after 2 seconds)


/*
┌─────────────────────────────────────────────────────────────┐
│ 6. IMPORTANT CONCEPTS & GOTCHAS                             │
└─────────────────────────────────────────────────────────────┘
*/

// Gotcha 6A: setTimeout with loop (common mistake)
console.log("=== Loop Gotcha Demo ===");

// ❌ WRONG - All timeouts log "3"
for (var i = 0; i < 3; i++) {
    setTimeout(function() {
        console.log("Wrong way:", i); // Always logs 3!
    }, 1000);
}
// Why? var has function scope, not block scope
// All callbacks share the SAME 'i' variable
// When they execute, loop is done and i = 3


// ✅ CORRECT - Use let (block scope)
for (let i = 0; i < 3; i++) {
    setTimeout(function() {
        console.log("Correct way:", i); // Logs 0, 1, 2
    }, 1000);
}
// Each iteration has its OWN 'i' with let


// ✅ ALTERNATIVE - Pass parameter
for (var i = 0; i < 3; i++) {
    setTimeout(function(value) {
        console.log("Parameter way:", value); // Logs 0, 1, 2
    }, 1000, i); // Pass i as parameter
}


// Gotcha 6B: this context in setTimeout
const person = {
    name: "Alice",
    greetLater: function() {
        setTimeout(function() {
            // ❌ 'this' is NOT the person object!
            // It's window or undefined (strict mode)
            console.log("Hello " + this.name); // undefined
        }, 1000);
    },
    greetLaterCorrect: function() {
        // ✅ Use arrow function (inherits 'this')
        setTimeout(() => {
            console.log("Hello " + this.name); // "Hello Alice"
        }, 1000);
    },
    greetLaterBind: function() {
        // ✅ Or use .bind()
        setTimeout(function() {
            console.log("Hello " + this.name); // "Hello Alice"
        }.bind(this), 1000);
    }
};

person.greetLater();        // Wrong
person.greetLaterCorrect(); // Correct
person.greetLaterBind();    // Also correct


// Gotcha 6C: Timing is approximate, not exact
console.log("Starting timer...");
const startTime = Date.now();

setTimeout(() => {
    const actualDelay = Date.now() - startTime;
    console.log(`Expected: 1000ms, Actual: ${actualDelay}ms`);
    // Might be 1001, 1002, or even more!
}, 1000);

// Why? JavaScript is single-threaded
// If main thread is busy, callback waits
// Delay is MINIMUM time, not exact time


// Gotcha 6D: setInterval drift
// setInterval can drift over time if execution takes too long
let intervalStart = Date.now();
let intervalCount = 0;

const driftInterval = setInterval(() => {
    intervalCount++;
    const elapsed = Date.now() - intervalStart;
    const expectedTime = intervalCount * 1000;
    const drift = elapsed - expectedTime;
    
    console.log(`Expected: ${expectedTime}ms, Actual: ${elapsed}ms, Drift: ${drift}ms`);
    
    if (intervalCount >= 5) {
        clearInterval(driftInterval);
    }
}, 1000);


// Better alternative: Recursive setTimeout (no drift)
function recursiveTimeout(callback, interval) {
    callback();
    setTimeout(() => recursiveTimeout(callback, interval), interval);
}

// This adjusts for execution time on each iteration


/*
┌─────────────────────────────────────────────────────────────┐
│ 7. BEST PRACTICES                                           │
└─────────────────────────────────────────────────────────────┘

✅ DO:
1. ALWAYS clear intervals when done (clearInterval)
2. Store timer IDs if you might need to cancel
3. Use arrow functions to preserve 'this' context
4. Handle cleanup in component unmount (React, Vue, etc.)
5. Use let instead of var in loops with timeouts
6. Consider requestAnimationFrame for animations
7. Add error handling in timeout/interval callbacks
8. Use descriptive variable names for timer IDs
9. Document why delays are what they are
10. Test edge cases (rapid clicks, unmounting, etc.)

❌ DON'T:
1. Forget to clear intervals (memory leaks!)
2. Use very short intervals (<10ms) - CPU intensive
3. Set intervals/timeouts inside loops without control
4. Rely on exact timing - it's approximate
5. Use setInterval for animations (use requestAnimationFrame)
6. Chain many nested timeouts - hard to maintain
7. Forget that code is asynchronous
8. Use global variables without cleanup
9. Create intervals without a stop condition
10. Forget to handle errors in callbacks


┌─────────────────────────────────────────────────────────────┐
│ 8. USEFUL HELPER FUNCTIONS                                  │
└─────────────────────────────────────────────────────────────┘
*/

// Helper 8A: Sleep/Delay with Promises (async/await friendly)
function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}

// Usage with async/await:
async function doSomethingWithDelay() {
    console.log("Starting...");
    await sleep(2000); // Wait 2 seconds
    console.log("Done after 2 seconds!");
}


// Helper 8B: Debounce (execute after user stops action)
function debounce(func, delay) {
    let timeoutId;
    
    return function(...args) {
        clearTimeout(timeoutId);
        timeoutId = setTimeout(() => func.apply(this, args), delay);
    };
}

// Usage:
const debouncedSearch = debounce((query) => {
    console.log(`Searching for: ${query}`);
}, 500);

// Call multiple times, only last one executes
debouncedSearch("a");
debouncedSearch("ab");
debouncedSearch("abc"); // Only this one runs


// Helper 8C: Throttle (limit execution frequency)
function throttle(func, limit) {
    let inThrottle;
    
    return function(...args) {
        if (!inThrottle) {
            func.apply(this, args);
            inThrottle = true;
            setTimeout(() => inThrottle = false, limit);
        }
    };
}

// Usage:
const throttledScroll = throttle(() => {
    console.log("Scroll event handled");
}, 1000);

// Executes at most once per second
window.addEventListener('scroll', throttledScroll);


// Helper 8D: Retry with delay
async function retryWithDelay(fn, retries = 3, delay = 1000) {
    for (let i = 0; i < retries; i++) {
        try {
            return await fn();
        } catch (error) {
            console.log(`Attempt ${i + 1} failed, retrying in ${delay}ms...`);
            if (i === retries - 1) throw error;
            await sleep(delay);
        }
    }
}

// Usage:
// await retryWithDelay(() => fetch('/api/data'), 3, 2000);


// Helper 8E: Timeout promise (add timeout to any promise)
function withTimeout(promise, ms) {
    const timeout = new Promise((_, reject) => {
        setTimeout(() => reject(new Error('Timeout')), ms);
    });
    
    return Promise.race([promise, timeout]);
}

// Usage:
// await withTimeout(fetch('/api/data'), 5000); // 5 second timeout


/*
┌─────────────────────────────────────────────────────────────┐
│ QUICK REFERENCE SUMMARY                                     │
└─────────────────────────────────────────────────────────────┘

// Execute once after delay:
const timerId = setTimeout(callback, delay);
clearTimeout(timerId);

// Execute repeatedly at interval:
const intervalId = setInterval(callback, interval);
clearInterval(intervalId);

// With parameters:
setTimeout(callback, delay, arg1, arg2);
setInterval(callback, interval, arg1, arg2);

// Common delays:
1 second  = 1000ms
0.5 sec   = 500ms
60 FPS    = ~16ms (1000/60)

// Always remember:
1. Timing is approximate, not exact
2. JavaScript is single-threaded (asynchronous)
3. ALWAYS clean up intervals
4. Store timer IDs if you need to cancel
5. Use arrow functions for correct 'this' context

// Modern alternatives:
- requestAnimationFrame() for smooth animations
- Promise + async/await for cleaner async code
- Web Workers for background tasks
*/


// ============================================
// FORM SUBMISSIONS: HOW THEY WORK
// ============================================
// Understanding form data flow from HTML to server

/*
┌─────────────────────────────────────────────────────────────┐
│ WHAT IS FORM SUBMISSION?                                    │
└─────────────────────────────────────────────────────────────┘

Form submission is the process of sending user input data from a web page
to a server for processing.

THE BASIC FLOW:
1. User fills out form fields
2. User clicks submit button (or presses Enter)
3. Browser collects all input data with "name" attributes
4. Browser sends HTTP request to URL specified in "action"
5. Server receives and processes the data
6. Server sends back a response
7. Browser displays the response (new page or updates current page)

TWO MAIN APPROACHES:
1. Traditional Form Submission - Full page reload
2. JavaScript-Controlled Submission - AJAX/Fetch (no reload)


┌─────────────────────────────────────────────────────────────┐
│ 1. TRADITIONAL FORM SUBMISSION (Default Behavior)           │
└─────────────────────────────────────────────────────────────┘

When you submit a form WITHOUT JavaScript, the browser:
1. Collects all form data
2. Sends HTTP request (GET or POST)
3. Navigates to response page (full page reload)

HTML STRUCTURE:
<form action="/submit-endpoint" method="POST">
  <input type="text" name="username">
  <input type="email" name="email">
  <button type="submit">Submit</button>
</form>

KEY ATTRIBUTES:
- action: Where to send data (URL)
- method: How to send data (GET or POST)
- name: Identifies each field (REQUIRED for submission!)

*/

// Example 1A: Understanding what gets submitted
/*
HTML:
<form action="/register" method="POST">
  <input type="text" name="username" value="john_doe">
  <input type="email" name="email" value="john@example.com">
  <input type="password" name="password" value="secret123">
  <input type="checkbox" name="newsletter" checked>
  <button type="submit">Register</button>
</form>

When submitted, the following data is sent:
{
  username: "john_doe",
  email: "john@example.com",
  password: "secret123",
  newsletter: "on"  // checkbox sends "on" when checked, nothing when unchecked
}

IMPORTANT: Inputs WITHOUT a "name" attribute are NOT submitted!
*/


// Example 1B: GET vs POST
/*
GET METHOD:
- Data appears in URL: /search?query=javascript&category=tutorials
- Visible to everyone (address bar, history, bookmarks)
- Limited data size (~2000 characters)
- Can be cached by browser
- Can be bookmarked
- USE FOR: Search forms, filters, non-sensitive data

POST METHOD:
- Data sent in request body (not visible in URL)
- No size limit
- Cannot be cached
- Cannot be bookmarked
- More secure (but still need HTTPS!)
- USE FOR: Login, registration, file uploads, modifying data
*/

function demonstrateGetVsPost() {
    // GET example - Data in URL
    // <form action="/search" method="GET">
    //   <input name="q" value="javascript">
    //   <input name="sort" value="newest">
    // </form>
    // Submits to: /search?q=javascript&sort=newest
    
    // POST example - Data in body
    // <form action="/login" method="POST">
    //   <input name="username" value="john">
    //   <input name="password" value="secret">
    // </form>
    // Submits to: /login (data hidden in request body)
}


/*
┌─────────────────────────────────────────────────────────────┐
│ 2. JAVASCRIPT-CONTROLLED SUBMISSION (Modern Approach)       │
└─────────────────────────────────────────────────────────────┘

Intercept form submission with JavaScript to:
- Prevent page reload
- Validate data before sending
- Send data via AJAX/Fetch
- Update page dynamically with response
- Provide better user experience

PATTERN: preventDefault() + Fetch API
*/

// Example 2A: Basic JavaScript form submission
const form = document.querySelector('#myForm');

form.addEventListener('submit', function(event) {
    // STEP 1: Prevent default form submission (no page reload)
    event.preventDefault();
    
    // STEP 2: Get form data
    const formData = new FormData(form);
    // OR manually:
    // const username = document.querySelector('#username').value;
    // const email = document.querySelector('#email').value;
    
    // STEP 3: Send data to server (AJAX/Fetch)
    fetch('/api/submit', {
        method: 'POST',
        body: formData
        // FormData automatically sets correct Content-Type
    })
    .then(response => response.json())
    .then(data => {
        // STEP 4: Handle response (no page reload!)
        console.log('Success:', data);
        alert('Form submitted successfully!');
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Submission failed!');
    });
});


// Example 2B: Form submission with JSON
const loginForm = document.querySelector('#loginForm');

loginForm.addEventListener('submit', async function(event) {
    event.preventDefault(); // Prevent default submission
    
    // Get form values
    const username = document.querySelector('#username').value;
    const password = document.querySelector('#password').value;
    
    // Create JSON object
    const loginData = {
        username: username,
        password: password
    };
    
    try {
        // Send as JSON
        const response = await fetch('/api/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(loginData) // Convert to JSON string
        });
        
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        
        const result = await response.json();
        console.log('Login successful:', result);
        
        // Redirect or update UI
        window.location.href = '/dashboard';
        
    } catch (error) {
        console.error('Login failed:', error);
        alert('Login failed! Please try again.');
    }
});


// Example 2C: Using FormData object (easier!)
const registrationForm = document.querySelector('#registerForm');

registrationForm.addEventListener('submit', async function(event) {
    event.preventDefault();
    
    // FormData automatically collects all named inputs
    const formData = new FormData(registrationForm);
    
    // Can also add/modify data
    formData.append('timestamp', Date.now());
    formData.set('username', formData.get('username').toLowerCase());
    
    // Check what's in FormData (for debugging)
    console.log('=== Form Data ===');
    for (let [key, value] of formData.entries()) {
        console.log(`${key}: ${value}`);
    }
    
    try {
        const response = await fetch('/api/register', {
            method: 'POST',
            body: formData
            // DON'T set Content-Type for FormData! Browser does it automatically
        });
        
        const result = await response.json();
        
        if (response.ok) {
            alert('Registration successful!');
            registrationForm.reset(); // Clear form
        } else {
            alert('Registration failed: ' + result.message);
        }
        
    } catch (error) {
        console.error('Error:', error);
        alert('Network error! Please try again.');
    }
});


/*
┌─────────────────────────────────────────────────────────────┐
│ 3. WORKING WITH FORMDATA                                    │
└─────────────────────────────────────────────────────────────┘

FormData is a JavaScript object that makes collecting and sending
form data much easier.
*/

// Example 3A: Creating FormData from form
const myForm = document.querySelector('#myForm');
const formData = new FormData(myForm);

// FormData collects ALL inputs with "name" attribute
// <input name="username" value="john"> → username: "john"
// <input name="age" value="25"> → age: "25"


// Example 3B: Working with FormData methods
function demonstrateFormData() {
    const formData = new FormData();
    
    // ADD data
    formData.append('username', 'john_doe');
    formData.append('email', 'john@example.com');
    
    // SET data (overwrites if exists)
    formData.set('username', 'jane_doe'); // Replaces john_doe
    
    // GET data
    const username = formData.get('username'); // 'jane_doe'
    
    // CHECK if key exists
    const hasEmail = formData.has('email'); // true
    
    // DELETE data
    formData.delete('email');
    
    // GET ALL values for a key (useful for checkboxes)
    // Multiple inputs with same name:
    // <input name="interests" value="sports">
    // <input name="interests" value="music">
    const interests = formData.getAll('interests'); // ['sports', 'music']
    
    // ITERATE through all entries
    for (let [key, value] of formData.entries()) {
        console.log(`${key}: ${value}`);
    }
    
    // Convert to object (for easier debugging)
    const obj = Object.fromEntries(formData);
    console.log(obj); // { username: 'jane_doe' }
}


// Example 3C: FormData with file uploads
const uploadForm = document.querySelector('#uploadForm');

uploadForm.addEventListener('submit', async function(event) {
    event.preventDefault();
    
    const formData = new FormData();
    
    // Add text fields
    formData.append('title', document.querySelector('#title').value);
    formData.append('description', document.querySelector('#description').value);
    
    // Add file(s)
    const fileInput = document.querySelector('#fileInput');
    const file = fileInput.files[0]; // Single file
    
    if (file) {
        formData.append('file', file);
        // File object contains: name, size, type, lastModified
        console.log('Uploading:', file.name, file.size, 'bytes');
    }
    
    // Multiple files:
    // for (let file of fileInput.files) {
    //     formData.append('files[]', file);
    // }
    
    try {
        const response = await fetch('/api/upload', {
            method: 'POST',
            body: formData
            // Browser automatically sets Content-Type: multipart/form-data
        });
        
        const result = await response.json();
        console.log('Upload successful:', result);
        
    } catch (error) {
        console.error('Upload failed:', error);
    }
});


/*
┌─────────────────────────────────────────────────────────────┐
│ 4. FORM VALIDATION                                          │
└─────────────────────────────────────────────────────────────┘

Validate data BEFORE submitting to prevent errors and improve UX.

TWO TYPES:
1. HTML5 validation (built-in)
2. JavaScript validation (custom)
*/

// Example 4A: HTML5 validation (automatic)
/*
<form>
  <input type="email" required>  <!-- Must be valid email -->
  <input type="text" minlength="3" maxlength="20" required>  <!-- Length limits -->
  <input type="number" min="18" max="100">  <!-- Number range -->
  <input type="text" pattern="[A-Za-z]{3,}">  <!-- Regex pattern -->
  <button type="submit">Submit</button>
</form>

Browser automatically validates BEFORE submitting.
Shows error messages if validation fails.
*/


// Example 4B: JavaScript validation (custom logic)
const validatedForm = document.querySelector('#validatedForm');

validatedForm.addEventListener('submit', function(event) {
    event.preventDefault(); // Always prevent default first
    
    // Get form values
    const username = document.querySelector('#username').value.trim();
    const email = document.querySelector('#email').value.trim();
    const password = document.querySelector('#password').value;
    const confirmPassword = document.querySelector('#confirmPassword').value;
    
    // Clear previous errors
    clearErrors();
    
    // Validation checks
    let isValid = true;
    
    // Check username length
    if (username.length < 3) {
        showError('username', 'Username must be at least 3 characters');
        isValid = false;
    }
    
    // Check email format
    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailPattern.test(email)) {
        showError('email', 'Please enter a valid email address');
        isValid = false;
    }
    
    // Check password strength
    if (password.length < 8) {
        showError('password', 'Password must be at least 8 characters');
        isValid = false;
    }
    
    // Check passwords match
    if (password !== confirmPassword) {
        showError('confirmPassword', 'Passwords do not match');
        isValid = false;
    }
    
    // If all valid, submit
    if (isValid) {
        console.log('Form is valid, submitting...');
        submitForm(validatedForm);
    } else {
        console.log('Form has errors, not submitting');
    }
});

function showError(fieldName, message) {
    const field = document.querySelector(`#${fieldName}`);
    const errorDiv = document.createElement('div');
    errorDiv.className = 'error-message';
    errorDiv.textContent = message;
    errorDiv.style.color = 'red';
    field.parentNode.appendChild(errorDiv);
    field.style.borderColor = 'red';
}

function clearErrors() {
    const errors = document.querySelectorAll('.error-message');
    errors.forEach(error => error.remove());
    
    const inputs = document.querySelectorAll('input');
    inputs.forEach(input => input.style.borderColor = '');
}


// Example 4C: Real-time validation (as user types)
const emailInput = document.querySelector('#email');

emailInput.addEventListener('input', function(event) {
    const email = event.target.value;
    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    
    if (emailPattern.test(email)) {
        // Valid email
        emailInput.style.borderColor = 'green';
        emailInput.setCustomValidity(''); // Clear error
    } else if (email.length > 0) {
        // Invalid email (but user is typing)
        emailInput.style.borderColor = 'red';
        emailInput.setCustomValidity('Please enter a valid email');
    } else {
        // Empty
        emailInput.style.borderColor = '';
        emailInput.setCustomValidity('');
    }
});


/*
┌─────────────────────────────────────────────────────────────┐
│ 5. HANDLING DIFFERENT INPUT TYPES                           │
└─────────────────────────────────────────────────────────────┘

Different input types require different handling when collecting data.
*/

// Example 5A: Text inputs (text, email, password, etc.)
const textValue = document.querySelector('#textInput').value;
// Simple - just get .value


// Example 5B: Checkboxes
const checkbox = document.querySelector('#agreeTerms');
const isChecked = checkbox.checked; // true or false

// Multiple checkboxes with same name:
const checkboxes = document.querySelectorAll('input[name="interests"]:checked');
const selectedInterests = Array.from(checkboxes).map(cb => cb.value);
// Example: ['sports', 'music', 'reading']


// Example 5C: Radio buttons
const selectedGender = document.querySelector('input[name="gender"]:checked');
const genderValue = selectedGender ? selectedGender.value : null;


// Example 5D: Select dropdown
const selectElement = document.querySelector('#country');
const selectedValue = selectElement.value;
const selectedText = selectElement.options[selectElement.selectedIndex].text;


// Example 5E: Multiple select
const multiSelect = document.querySelector('#skills');
const selectedOptions = Array.from(multiSelect.selectedOptions).map(option => option.value);


// Example 5F: File input
const fileInput = document.querySelector('#fileUpload');
const file = fileInput.files[0]; // Single file
const files = Array.from(fileInput.files); // Multiple files

if (file) {
    console.log('File name:', file.name);
    console.log('File size:', file.size, 'bytes');
    console.log('File type:', file.type);
    console.log('Last modified:', file.lastModified);
}


// Example 5G: Complete form data collection
function collectAllFormData(formElement) {
    const data = {};
    
    // Text inputs
    data.username = formElement.querySelector('#username').value;
    data.email = formElement.querySelector('#email').value;
    
    // Checkbox (single)
    data.newsletter = formElement.querySelector('#newsletter').checked;
    
    // Checkboxes (multiple with same name)
    const interests = formElement.querySelectorAll('input[name="interests"]:checked');
    data.interests = Array.from(interests).map(cb => cb.value);
    
    // Radio button
    const gender = formElement.querySelector('input[name="gender"]:checked');
    data.gender = gender ? gender.value : null;
    
    // Select
    data.country = formElement.querySelector('#country').value;
    
    // File
    const fileInput = formElement.querySelector('#avatar');
    data.avatar = fileInput.files[0];
    
    return data;
}


/*
┌─────────────────────────────────────────────────────────────┐
│ 6. COMPLETE REAL-WORLD EXAMPLES                            │
└─────────────────────────────────────────────────────────────┘
*/

// Example 6A: Contact form with loading state
const contactForm = document.querySelector('#contactForm');
const submitButton = contactForm.querySelector('button[type="submit"]');

contactForm.addEventListener('submit', async function(event) {
    event.preventDefault();
    
    // Disable submit button and show loading
    submitButton.disabled = true;
    submitButton.textContent = 'Sending...';
    
    const formData = new FormData(contactForm);
    
    try {
        const response = await fetch('/api/contact', {
            method: 'POST',
            body: formData
        });
        
        const result = await response.json();
        
        if (response.ok) {
            // Success
            alert('Message sent successfully!');
            contactForm.reset();
        } else {
            // Server error
            alert('Error: ' + result.message);
        }
        
    } catch (error) {
        // Network error
        alert('Failed to send message. Please try again.');
        console.error('Error:', error);
        
    } finally {
        // Re-enable button
        submitButton.disabled = false;
        submitButton.textContent = 'Send Message';
    }
});


// Example 6B: Login form with error handling
const loginFormComplete = document.querySelector('#loginForm');

loginFormComplete.addEventListener('submit', async function(event) {
    event.preventDefault();
    
    const username = document.querySelector('#username').value.trim();
    const password = document.querySelector('#password').value;
    
    // Client-side validation
    if (!username || !password) {
        alert('Please fill in all fields');
        return;
    }
    
    try {
        const response = await fetch('/api/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ username, password })
        });
        
        const result = await response.json();
        
        if (response.ok) {
            // Success - store token and redirect
            localStorage.setItem('authToken', result.token);
            window.location.href = '/dashboard';
        } else {
            // Login failed
            if (response.status === 401) {
                alert('Invalid username or password');
            } else if (response.status === 429) {
                alert('Too many login attempts. Please try again later.');
            } else {
                alert('Login failed: ' + result.message);
            }
        }
        
    } catch (error) {
        console.error('Login error:', error);
        alert('Network error. Please check your connection.');
    }
});


// Example 6C: Registration form with progress feedback
const regFormComplete = document.querySelector('#registrationForm');
const progressDiv = document.createElement('div');

regFormComplete.addEventListener('submit', async function(event) {
    event.preventDefault();
    
    // Validate
    const password = document.querySelector('#password').value;
    const confirmPassword = document.querySelector('#confirmPassword').value;
    
    if (password !== confirmPassword) {
        alert('Passwords do not match!');
        return;
    }
    
    const formData = new FormData(regFormComplete);
    
    // Show progress
    progressDiv.textContent = 'Creating account...';
    regFormComplete.appendChild(progressDiv);
    
    try {
        const response = await fetch('/api/register', {
            method: 'POST',
            body: formData
        });
        
        const result = await response.json();
        
        if (response.ok) {
            progressDiv.textContent = 'Account created! Redirecting...';
            progressDiv.style.color = 'green';
            
            // Redirect after short delay
            setTimeout(() => {
                window.location.href = '/login';
            }, 2000);
            
        } else {
            // Handle specific errors
            progressDiv.textContent = 'Error: ' + result.message;
            progressDiv.style.color = 'red';
            
            // Highlight problem fields
            if (result.errors) {
                result.errors.forEach(error => {
                    const field = document.querySelector(`#${error.field}`);
                    if (field) {
                        field.style.borderColor = 'red';
                    }
                });
            }
        }
        
    } catch (error) {
        progressDiv.textContent = 'Network error. Please try again.';
        progressDiv.style.color = 'red';
        console.error('Error:', error);
    }
});


/*
┌─────────────────────────────────────────────────────────────┐
│ 7. FORM SUBMISSION PATTERNS & BEST PRACTICES                │
└─────────────────────────────────────────────────────────────┘
*/

// Pattern 7A: Reusable form submission function
async function submitForm(formElement, url, options = {}) {
    const formData = new FormData(formElement);
    
    const defaultOptions = {
        method: 'POST',
        body: formData
    };
    
    try {
        const response = await fetch(url, { ...defaultOptions, ...options });
        
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        
        return await response.json();
        
    } catch (error) {
        console.error('Form submission error:', error);
        throw error;
    }
}

// Usage:
// submitForm(myForm, '/api/submit')
//     .then(result => console.log('Success:', result))
//     .catch(error => alert('Submission failed'));


// Pattern 7B: Form with retry logic
async function submitWithRetry(formData, url, maxRetries = 3) {
    for (let i = 0; i < maxRetries; i++) {
        try {
            const response = await fetch(url, {
                method: 'POST',
                body: formData
            });
            
            if (response.ok) {
                return await response.json();
            }
            
            // Don't retry on client errors (4xx)
            if (response.status >= 400 && response.status < 500) {
                throw new Error('Client error: ' + response.status);
            }
            
        } catch (error) {
            if (i === maxRetries - 1) {
                throw error; // Last attempt failed
            }
            
            console.log(`Attempt ${i + 1} failed, retrying...`);
            await new Promise(resolve => setTimeout(resolve, 1000)); // Wait 1 second
        }
    }
}


// Pattern 7C: Form state management
class FormManager {
    constructor(formElement) {
        this.form = formElement;
        this.submitButton = formElement.querySelector('button[type="submit"]');
        this.isSubmitting = false;
    }
    
    setLoading(loading) {
        this.isSubmitting = loading;
        this.submitButton.disabled = loading;
        this.submitButton.textContent = loading ? 'Submitting...' : 'Submit';
    }
    
    showError(message) {
        const errorDiv = document.createElement('div');
        errorDiv.className = 'error';
        errorDiv.textContent = message;
        errorDiv.style.color = 'red';
        this.form.insertBefore(errorDiv, this.form.firstChild);
        
        // Remove after 5 seconds
        setTimeout(() => errorDiv.remove(), 5000);
    }
    
    showSuccess(message) {
        const successDiv = document.createElement('div');
        successDiv.className = 'success';
        successDiv.textContent = message;
        successDiv.style.color = 'green';
        this.form.insertBefore(successDiv, this.form.firstChild);
        
        setTimeout(() => successDiv.remove(), 5000);
    }
    
    async submit(url) {
        if (this.isSubmitting) return;
        
        this.setLoading(true);
        
        try {
            const formData = new FormData(this.form);
            const response = await fetch(url, {
                method: 'POST',
                body: formData
            });
            
            const result = await response.json();
            
            if (response.ok) {
                this.showSuccess('Submitted successfully!');
                this.form.reset();
                return result;
            } else {
                this.showError(result.message || 'Submission failed');
                return null;
            }
            
        } catch (error) {
            this.showError('Network error. Please try again.');
            console.error('Error:', error);
            return null;
            
        } finally {
            this.setLoading(false);
        }
    }
}

// Usage:
// const manager = new FormManager(document.querySelector('#myForm'));
// form.addEventListener('submit', (e) => {
//     e.preventDefault();
//     manager.submit('/api/submit');
// });


/*
┌─────────────────────────────────────────────────────────────┐
│ 8. BEST PRACTICES & TIPS                                    │
└─────────────────────────────────────────────────────────────┘

✅ DO:
1. ALWAYS use event.preventDefault() when handling with JavaScript
2. ALWAYS add "name" attributes to inputs (required for submission)
3. Validate data on BOTH client and server
4. Disable submit button during submission (prevent double-submit)
5. Show loading/progress indicators
6. Clear sensitive data (passwords) after submission
7. Use HTTPS for forms with sensitive data
8. Provide clear error messages
9. Reset form after successful submission
10. Handle network errors gracefully
11. Use FormData for file uploads
12. Store authentication tokens securely (localStorage, cookies)
13. Implement CSRF protection on server
14. Use appropriate HTTP methods (POST for mutations)
15. Test form submission thoroughly

❌ DON'T:
1. Don't forget preventDefault() (causes page reload)
2. Don't forget "name" attributes (data won't be sent)
3. Don't trust client-side validation only (always validate on server)
4. Don't submit multiple times (use loading state)
5. Don't expose sensitive data in URLs (use POST, not GET)
6. Don't hardcode API URLs (use environment variables)
7. Don't ignore error responses
8. Don't forget to re-enable submit button after completion
9. Don't submit empty forms
10. Don't forget accessibility (labels, ARIA attributes)
11. Don't store passwords in plain text
12. Don't assume network will always work


┌─────────────────────────────────────────────────────────────┐
│ QUICK REFERENCE SUMMARY                                     │
└─────────────────────────────────────────────────────────────┘

// Basic pattern:
form.addEventListener('submit', async (event) => {
    event.preventDefault();  // Stop page reload
    
    const formData = new FormData(form);  // Collect data
    
    try {
        const response = await fetch('/api/endpoint', {
            method: 'POST',
            body: formData
        });
        
        if (response.ok) {
            const result = await response.json();
            console.log('Success:', result);
            form.reset();
        } else {
            console.error('Error:', response.status);
        }
    } catch (error) {
        console.error('Network error:', error);
    }
});

// Key points:
1. preventDefault() - Stop default form submission
2. FormData - Collect form data easily
3. fetch() - Send data to server
4. async/await - Handle asynchronous operations
5. Error handling - try/catch for network errors
6. Response handling - Check response.ok
7. Reset form - Clear fields after success

// FormData methods:
formData.append(key, value)  - Add data
formData.get(key)            - Get value
formData.set(key, value)     - Set/overwrite value
formData.has(key)            - Check if exists
formData.delete(key)         - Remove data
formData.getAll(key)         - Get all values (for multiple inputs with same name)

// Form attributes:
action  - Where to send data
method  - GET or POST
name    - Identifies form data (REQUIRED on inputs!)
enctype - How to encode data (multipart/form-data for files)

// Input properties:
.value   - Get input value
.checked - Get checkbox/radio state
.files   - Get uploaded files
.validity - Check HTML5 validation state
.setCustomValidity() - Set custom validation message
*/
