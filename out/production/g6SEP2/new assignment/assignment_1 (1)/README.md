# Vinyl Lending Library - Client/Server System

## Assignment 2 - PRO2

This project implements a client-server architecture for a vinyl lending library system using Java sockets, JSON communication, MVVM pattern, Observer pattern, and Multiton logging.

---

## 📁 Project Structure

```
assignment_1 (1)/
├── src/
│   ├── networking/
│   │   ├── Message.java           # JSON message protocol
│   │   ├── ServerLogger.java      # Multiton logger
│   │   ├── VinylServer.java       # Multithreaded server
│   │   ├── ClientHandler.java     # Per-client thread handler
│   │   └── NetworkClient.java     # Client-side socket wrapper
│   ├── model/
│   │   ├── Model.java              # Model interface
│   │   ├── ModelManager.java       # Server-side model implementation
│   │   ├── ModelListener.java      # Observer interface
│   │   ├── Vinyl.java              # Vinyl entity
│   │   ├── State.java              # Vinyl states enum
│   │   └── VinylList.java          # Vinyl collection
│   ├── viewmodel/
│   │   ├── AppVM.java              # Main ViewModel
│   │   ├── FrontVM.java            # Front view ViewModel
│   │   ├── ManageVM.java           # Manage view ViewModel
│   │   └── ViewState.java          # Shared view state
│   ├── view/
│   │   ├── ViewHandler.java        # View management
│   │   ├── front/
│   │   │   ├── Front.fxml
│   │   │   └── FrontController.java
│   │   └── manage/
│   │       ├── Manage.fxml
│   │       └── ManageController.java
│   ├── StartApplication.java       # Original standalone app
│   ├── StartClient.java            # New client application
│   └── UserSimulator.java          # User simulation (if present)
├── lib/
│   └── gson-2.10.1.jar             # GSON library for JSON
├── server_logs/                     # Created at runtime
├── AssignmentGuide.html            # Interactive documentation
├── PROTOCOL_DOCUMENTATION.md       # Protocol specification
└── README.md                       # This file
```

---

## 🚀 How to Run

### Prerequisites
- Java 8 or higher
- JavaFX SDK (if not included in your JDK)
- GSON library (already included in `lib/` folder)

### Step 1: Compile the Code

Open a terminal in the project root directory and run:

```bash
cd "c:\Users\leond\Aa VIA\new assignment\assignment_1 (1)"

# Compile all Java files
javac -cp "src;lib\gson-2.10.1.jar" -d bin src\networking\*.java src\model\*.java src\viewmodel\*.java src\view\*.java src\view\front\*.java src\view\manage\*.java src\*.java
```

If you don't have a `bin` directory:
```bash
mkdir bin
```

### Step 2: Start the Server

In one terminal window:

```bash
java -cp "src;lib\gson-2.10.1.jar" networking.VinylServer
```

You should see:
```
Vinyl Server started on port 8080
Waiting for clients...
```

The server will:
- Listen on port 8080
- Create log files in `server_logs/` directory
- Initialize with 10 sample vinyls
- Handle multiple client connections

### Step 3: Start the Client(s)

In another terminal window (or multiple windows for multiple clients):

```bash
java -cp "src;lib\gson-2.10.1.jar" StartClient
```

The client window will open and automatically connect to the server.

### Running Multiple Clients

You can run multiple clients simultaneously to see the real-time broadcasting in action:
- Open multiple terminal windows
- Run `StartClient` in each
- Operations in one client will be immediately visible in all other clients

---

## 🎯 Features

### Client Features
1. **View all vinyls** - Automatically loaded on startup
2. **Reserve vinyl** - Reserve for later borrowing
3. **Borrow vinyl** - Take a vinyl out
4. **Return vinyl** - Return a borrowed vinyl
5. **Real-time updates** - See changes made by other users instantly

### Server Features
1. **Multithreaded** - One thread per connected client
2. **Broadcasting** - Sends updates to all connected clients
3. **Comprehensive logging** - All communications logged with IP, timestamp, content
4. **JSON protocol** - All messages in JSON format
5. **Error handling** - Graceful error responses

---

## 📊 Design Patterns Used

### 1. Observer Pattern
- **ModelListener** interface defines observer contract
- Server observes model changes to broadcast updates
- ViewModels observe model to update UI

### 2. MVVM Pattern
- **Model**: Business logic (Vinyl, ModelManager, NetworkClient)
- **View**: FXML + Controllers
- **ViewModel**: Mediator with Observable properties

### 3. Multiton Pattern
- **ServerLogger** creates one instance per day
- Each day gets its own log file
- Thread-safe singleton-like access

### 4. State Pattern
- Vinyl has 5 states: AVAILABLE, BORROWED, RESERVED, BORROWED_RESERVED, REMOVED
- State transitions enforced by business rules

---

## 📝 Protocol Documentation

See `PROTOCOL_DOCUMENTATION.md` for detailed protocol specification.

### Message Types

**Client → Server:**
- `GET_ALL_VINYLS` - Request vinyl list
- `RESERVE` - Reserve a vinyl
- `BORROW` - Borrow a vinyl
- `RETURN` - Return a vinyl

**Server → Client (Broadcasts):**
- `VINYL_UPDATED` - Vinyl state changed
- `VINYL_REMOVED` - Vinyl removed
- `VINYL_ADDED` - New vinyl added

### Example Message

```json
{
  "type": "BORROW",
  "data": "{\"vinyl\":{\"title\":\"Thriller\",\"artist\":\"Michael Jackson\",\"year\":\"1982\"},\"userName\":\"Alice\"}",
  "success": true,
  "error": null
}
```

---

## 🔍 Wireshark Analysis

For the assignment Wireshark requirement, see the interactive guide:

**Open `AssignmentGuide.html` in your browser** for step-by-step instructions.

### Quick Steps:
1. Start Wireshark on loopback interface
2. Apply filter: `tcp.port == 8080`
3. Start server and client
4. Perform a borrow operation
5. Look for:
   - 3-way handshake (SYN, SYN-ACK, ACK)
   - IP addresses and port numbers
   - Application layer JSON messages

---

## 📚 Documentation

### Interactive Guide
Open `AssignmentGuide.html` in any web browser for:
- Complete Wireshark capture guide
- Detailed implementation explanations
- Design pattern descriptions
- All with expandable sections and examples

### Protocol Documentation
See `PROTOCOL_DOCUMENTATION.md` for:
- Message format specification
- All message types
- Request/response examples
- Error handling

---

## 🔧 Troubleshooting

### "Connection refused" error
- Make sure the server is running before starting the client
- Check that port 8080 is not being used by another application

### "Class not found" error
- Make sure you include the GSON library in the classpath: `-cp "src;lib\gson-2.10.1.jar"`

### JavaFX issues
- If JavaFX is not included in your JDK, download JavaFX SDK
- Add to classpath: `--module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml`

### Server not broadcasting updates
- Check server logs in `server_logs/` directory
- Verify client is still connected
- Check for exceptions in console

---

## 📦 Submission Checklist

For assignment submission, include:

✅ **Source Code**
- All `.java` files in `src/` directory
- Organized in packages

✅ **External Libraries**
- `lib/gson-2.10.1.jar`

✅ **FXML Files**
- `view/front/Front.fxml`
- `view/manage/Manage.fxml`

✅ **Documentation**
- `PROTOCOL_DOCUMENTATION.md` - Protocol specification
- `AssignmentGuide.html` - Interactive guide
- `README.md` - This file

✅ **UML Class Diagram**
- Create in Astah showing:
  - MVVM structure
  - Observer pattern
  - Socket-related classes
  - Singleton/Multiton logger

✅ **Wireshark Printout**
- Annotated screenshots showing:
  1. 3-way handshake (SYN, SYN-ACK, ACK)
  2. IP addresses and port numbers
  3. Application layer BORROW request and response

---

## 🎓 Assignment Requirements Met

- ✅ Java Sockets for client-server communication
- ✅ Multithreaded server (one thread per client)
- ✅ JSON format for all messages (including errors)
- ✅ Client can get all vinyls, reserve, borrow, return
- ✅ Client receives broadcast messages for state changes
- ✅ MVVM design pattern maintained
- ✅ Observer pattern for event-driven updates
- ✅ Multiton pattern for logging (one instance per day)
- ✅ Logs include IP address, date, time, and message content
- ✅ Protocol documentation provided

---

## 👨‍💻 Author

Assignment 2 - PRO2
Date: May 4, 2026

---

## 📞 Support

For questions about the implementation, refer to:
1. `AssignmentGuide.html` - Interactive documentation
2. `PROTOCOL_DOCUMENTATION.md` - Protocol details
3. Source code comments
4. Server logs in `server_logs/`

---

**Good luck with your assignment! 🎵**
