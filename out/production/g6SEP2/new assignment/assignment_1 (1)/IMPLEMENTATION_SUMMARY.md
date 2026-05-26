# Implementation Summary - Assignment 2

## ✅ Complete Implementation

### Files Created

#### Networking Package (Client-Server Communication)
1. **Message.java** - JSON message protocol
   - Handles serialization/deserialization with GSON
   - Factory methods for all message types
   - Request/response message creation
   - Broadcast message creation

2. **ServerLogger.java** - Multiton Logger
   - One instance per day (Multiton pattern)
   - Logs to both console and file
   - Includes timestamp, IP address, port, message type, and content
   - Thread-safe implementation
   - Creates daily log files in `server_logs/` directory

3. **VinylServer.java** - Multithreaded Server
   - Listens on port 8080
   - Accepts multiple client connections
   - Creates one thread per client (ClientHandler)
   - Implements ModelListener to broadcast updates
   - Comprehensive error handling

4. **ClientHandler.java** - Per-Client Thread Handler
   - Handles communication with one client
   - Processes requests: GET_ALL_VINYLS, RESERVE, BORROW, RETURN
   - Sends responses in JSON format
   - Logs all incoming/outgoing messages
   - Thread-safe vinyl operations

5. **NetworkClient.java** - Client-Side Socket Wrapper
   - Implements Model interface
   - Connects to server via TCP socket
   - Sends requests and receives responses
   - Listens for server broadcasts in separate thread
   - Notifies local listeners of broadcast updates

#### Application Files
6. **StartClient.java** - Client Application Entry Point
   - Uses NetworkClient instead of local ModelManager
   - Connects to server on startup
   - Gracefully disconnects on shutdown
   - Same UI as original application

#### Documentation
7. **PROTOCOL_DOCUMENTATION.md** - Complete Protocol Specification
   - Message format details
   - All message types documented
   - Request/response examples
   - Error handling specification
   - Communication flow examples

8. **AssignmentGuide.html** - Interactive Guide Website
   - Overview section explaining the system
   - Step-by-step Wireshark capture guide
   - Detailed implementation explanations
   - Design pattern descriptions
   - All with expandable dropdown sections
   - Professional styling with CSS

9. **README.md** - Comprehensive Project Documentation
   - How to compile and run
   - Project structure
   - Features overview
   - Design patterns used
   - Troubleshooting guide
   - Submission checklist

#### External Libraries
10. **gson-2.10.1.jar** - Downloaded and included in lib/

---

## 🎯 Assignment Requirements Coverage

### ✅ Socket Communication
- **Requirement:** Use Java Sockets to connect client and server
- **Implementation:** 
  - Server uses `ServerSocket` on port 8080
  - Client uses `Socket` to connect
  - TCP for reliable communication
  - BufferedReader/PrintWriter for message exchange

### ✅ Multithreaded Server
- **Requirement:** Server must be multithreaded
- **Implementation:**
  - Main thread accepts connections
  - One `ClientHandler` thread per client
  - Thread-safe access to shared vinyl list
  - Synchronized blocks protect critical sections

### ✅ JSON Protocol
- **Requirement:** All messages in JSON format, errors included
- **Implementation:**
  - All messages use Message class with JSON serialization
  - GSON library for JSON conversion
  - Error responses use same message format
  - `success` and `error` fields in all responses

### ✅ Client Functionality
- **Requirement:** Get all vinyls, reserve, borrow, return
- **Implementation:**
  - `GET_ALL_VINYLS` - retrieves full vinyl list
  - `RESERVE` - reserves a vinyl for a user
  - `BORROW` - borrows a vinyl for a user
  - `RETURN` - returns a vinyl

### ✅ Broadcasting
- **Requirement:** Receive broadcasted messages when vinyl changes state
- **Implementation:**
  - Server implements ModelListener
  - Broadcasts `VINYL_UPDATED` when state changes
  - Broadcasts `VINYL_REMOVED` when vinyl deleted
  - Broadcasts `VINYL_ADDED` when vinyl added
  - All connected clients receive updates

### ✅ MVVM Pattern
- **Requirement:** Use MVVM design pattern with at least two views
- **Implementation:**
  - **Model:** Vinyl, State, Model interface, ModelManager, NetworkClient
  - **View:** Front.fxml, Manage.fxml, FrontController, ManageController
  - **ViewModel:** AppVM, FrontVM, ManageVM
  - Two views maintained from Assignment 1

### ✅ Observer Pattern
- **Requirement:** Must use Observer design pattern
- **Implementation:**
  - `ModelListener` interface defines observer
  - `Model` interface has add/remove listener methods
  - Server observes model to broadcast changes
  - ViewModels observe model to update UI
  - Proper notify methods for all events

### ✅ Singleton/Multiton
- **Requirement:** Singleton or Multiton for logging
- **Implementation:**
  - **Multiton pattern** in ServerLogger
  - One instance per day
  - Static getInstance() method
  - HashMap stores instances by date
  - Thread-safe implementation

### ✅ Comprehensive Logging
- **Requirement:** Log all communication with IP, date, time
- **Implementation:**
  - Timestamp: yyyy-MM-dd HH:mm:ss
  - IP address and port for each message
  - Message type (INCOMING, OUTGOING, SERVER, ERROR, CONNECT, DISCONNECT)
  - Full message content
  - Daily log files: `server_logs/server_YYYY-MM-DD.log`

---

## 📊 Design Patterns Identified in Code

### 1. Observer Pattern
- **Classes:** ModelListener, Model, VinylServer, AppVM
- **Purpose:** Event-driven updates between model and views/server

### 2. MVVM (Model-View-ViewModel)
- **Model:** Business logic layer
- **View:** UI layer (FXML + Controllers)
- **ViewModel:** Mediator with Observable properties

### 3. Multiton Pattern
- **Class:** ServerLogger
- **Purpose:** One logger instance per day for organized logging

### 4. State Pattern
- **Class:** State enum, Vinyl
- **States:** AVAILABLE, BORROWED, RESERVED, BORROWED_RESERVED, REMOVED

### 5. Factory Pattern (partial)
- **Class:** Message
- **Purpose:** Factory methods for creating different message types

---

## 🔍 Wireshark Capture Instructions

The `AssignmentGuide.html` file provides detailed step-by-step instructions for:

1. **Finding the 3-way handshake**
   - SYN packet identification
   - SYN-ACK packet identification
   - ACK packet identification
   - Where to find TCP flags

2. **Finding IP addresses and ports**
   - Client IP and port
   - Server IP and port
   - Socket tuple (IP:Port pairs)

3. **Finding application layer messages**
   - BORROW request JSON
   - BORROW response JSON
   - Using "Follow TCP Stream"
   - How to annotate printouts

---

## 🚀 How to Test

### Start Server
```bash
cd "c:\Users\leond\Aa VIA\new assignment\assignment_1 (1)"
java -cp "src;lib\gson-2.10.1.jar" networking.VinylServer
```

### Start Client(s)
```bash
java -cp "src;lib\gson-2.10.1.jar" StartClient
```

### Test Multi-Client Broadcasting
1. Start server
2. Start first client
3. Start second client
4. In first client, borrow a vinyl
5. Observe second client automatically updates
6. Check server logs in `server_logs/` directory

---

## 📁 What to Submit

1. **Source Code** - All `.java` files in `src/` directory
2. **External Libraries** - `lib/gson-2.10.1.jar`
3. **FXML Files** - `view/front/Front.fxml`, `view/manage/Manage.fxml`
4. **Protocol Documentation** - `PROTOCOL_DOCUMENTATION.md`
5. **Interactive Guide** - `AssignmentGuide.html`
6. **README** - `README.md`
7. **UML Class Diagram** - Create in Astah showing all patterns
8. **Wireshark Annotated Printout** - Follow guide in AssignmentGuide.html

---

## ✨ Additional Features Implemented

- Graceful shutdown handling
- Connection error handling
- Timeout protection
- Thread-safe operations
- Auto-flush logging for immediate writes
- Detailed error messages
- Interactive HTML documentation
- Professional styling and UI

---

## 🎓 Learning Outcomes Demonstrated

1. **Socket Programming** - TCP client-server communication
2. **Multithreading** - Thread creation, synchronization, thread safety
3. **JSON** - Serialization, deserialization with GSON
4. **Design Patterns** - Observer, MVVM, Multiton, State
5. **Network Protocols** - Understanding TCP handshake, ports, IP addresses
6. **Logging** - Comprehensive system logging
7. **Error Handling** - Robust error management
8. **Documentation** - Technical writing and user guides

---

## 📞 Support Resources

1. **Interactive Guide:** Open `AssignmentGuide.html` in browser
2. **Protocol Details:** See `PROTOCOL_DOCUMENTATION.md`
3. **How to Run:** See `README.md`
4. **Server Logs:** Check `server_logs/` directory
5. **Source Code:** Well-commented with JavaDoc

---

**Status: ✅ COMPLETE**

All assignment requirements have been implemented and tested.
All documentation has been created.
System is ready for demonstration and submission.

---

**Date Completed:** May 4, 2026
