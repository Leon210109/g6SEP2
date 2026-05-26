# Vinyl Lending Library - Network Protocol Documentation

## Overview
This document describes the communication protocol between the client and server in the Vinyl Lending Library system. All messages are exchanged in JSON format over TCP sockets.

## Connection Details
- **Protocol**: TCP
- **Default Port**: 8080
- **Default Host**: localhost
- **Message Format**: JSON (one message per line, terminated with newline)

## Message Structure

All messages follow this base structure:

```json
{
  "type": "MESSAGE_TYPE",
  "data": "message-specific data (can be JSON string)",
  "success": true/false,
  "error": "error message if success is false"
}
```

### Fields:
- **type** (String): Identifies the message type
- **data** (String): Contains the message payload (often a JSON-encoded string)
- **success** (Boolean): Indicates if the operation was successful (responses only)
- **error** (String): Contains error message if success is false (responses only)

---

## Client → Server Messages

### 1. GET_ALL_VINYLS
Request a list of all vinyls in the library.

**Request:**
```json
{
  "type": "GET_ALL_VINYLS",
  "data": "",
  "success": true,
  "error": null
}
```

**Response:**
```json
{
  "type": "GET_ALL_VINYLS_RESPONSE",
  "data": "[{\"title\":\"Thriller\",\"artist\":\"Michael Jackson\",\"year\":\"1982\",\"state\":\"AVAILABLE\",...}, ...]",
  "success": true,
  "error": null
}
```

The `data` field contains a JSON array of Vinyl objects.

---

### 2. RESERVE
Reserve a vinyl for a specific user.

**Request:**
```json
{
  "type": "RESERVE",
  "data": "{\"vinyl\":{\"title\":\"Thriller\",\"artist\":\"Michael Jackson\",\"year\":\"1982\"},\"userName\":\"Alice\"}",
  "success": true,
  "error": null
}
```

**Success Response:**
```json
{
  "type": "RESERVE_RESPONSE",
  "data": "Vinyl reserved successfully",
  "success": true,
  "error": null
}
```

**Error Response:**
```json
{
  "type": "RESERVE_RESPONSE",
  "data": "",
  "success": false,
  "error": "Vinyl is already borrowed"
}
```

---

### 3. BORROW
Borrow a vinyl for a specific user.

**Request:**
```json
{
  "type": "BORROW",
  "data": "{\"vinyl\":{\"title\":\"Thriller\",\"artist\":\"Michael Jackson\",\"year\":\"1982\"},\"userName\":\"Bob\"}",
  "success": true,
  "error": null
}
```

**Success Response:**
```json
{
  "type": "BORROW_RESPONSE",
  "data": "Vinyl borrowed successfully",
  "success": true,
  "error": null
}
```

**Error Response:**
```json
{
  "type": "BORROW_RESPONSE",
  "data": "",
  "success": false,
  "error": "Vinyl is not available"
}
```

---

### 4. RETURN
Return a previously borrowed vinyl.

**Request:**
```json
{
  "type": "RETURN",
  "data": "{\"title\":\"Thriller\",\"artist\":\"Michael Jackson\",\"year\":\"1982\"}",
  "success": true,
  "error": null
}
```

**Success Response:**
```json
{
  "type": "RETURN_RESPONSE",
  "data": "Vinyl returned successfully",
  "success": true,
  "error": null
}
```

**Error Response:**
```json
{
  "type": "RETURN_RESPONSE",
  "data": "",
  "success": false,
  "error": "Vinyl is not borrowed"
}
```

---

## Server → Client Messages (Broadcasts)

These messages are sent from the server to all connected clients when the state changes.

### 1. VINYL_UPDATED
Sent when a vinyl's state changes (borrowed, reserved, returned).

```json
{
  "type": "VINYL_UPDATED",
  "data": "{\"title\":\"Thriller\",\"artist\":\"Michael Jackson\",\"year\":\"1982\",\"state\":\"BORROWED\",\"borrowName\":\"Bob\",...}",
  "success": true,
  "error": null
}
```

### 2. VINYL_REMOVED
Sent when a vinyl is removed from the library.

```json
{
  "type": "VINYL_REMOVED",
  "data": "{\"title\":\"Thriller\",\"artist\":\"Michael Jackson\",\"year\":\"1982\",...}",
  "success": true,
  "error": null
}
```

### 3. VINYL_ADDED
Sent when a new vinyl is added to the library.

```json
{
  "type": "VINYL_ADDED",
  "data": "{\"title\":\"New Album\",\"artist\":\"Artist Name\",\"year\":\"2024\",\"state\":\"AVAILABLE\",...}",
  "success": true,
  "error": null
}
```

---

## Vinyl Object Structure

Each vinyl object in the `data` field has the following structure:

```json
{
  "title": "Album Title",
  "artist": "Artist Name",
  "year": "Release Year",
  "state": "AVAILABLE|BORROWED|RESERVED|BORROWED_RESERVED|REMOVED",
  "markedForRemoval": false,
  "reserveName": "Name of user who reserved (or null)",
  "borrowName": "Name of user who borrowed (or null)"
}
```

### States:
- **AVAILABLE**: Vinyl is available for borrowing
- **BORROWED**: Vinyl is currently borrowed
- **RESERVED**: Vinyl is reserved by someone
- **BORROWED_RESERVED**: Vinyl is borrowed and also reserved by someone else
- **REMOVED**: Vinyl has been removed from the library

---

## Error Handling

All errors from the server are returned in the standard message format with:
- `success: false`
- `error: "Error description"`

Common error scenarios:
- Vinyl not found
- Invalid state transition (e.g., borrowing an already borrowed vinyl)
- Network errors
- JSON parsing errors

---

## Communication Flow Example

### Example: Borrow Operation

1. **Client** connects to server on port 8080
2. **Client** sends GET_ALL_VINYLS request
3. **Server** responds with list of vinyls
4. **Client** sends BORROW request for a specific vinyl
5. **Server** processes the request and updates the model
6. **Server** sends BORROW_RESPONSE to requesting client
7. **Server** broadcasts VINYL_UPDATED to all connected clients
8. **All clients** update their local view to reflect the new state

---

## Implementation Notes

### Thread Safety
- Server uses synchronized blocks to ensure thread-safe access to shared resources
- Each client connection is handled in a separate thread
- Broadcast messages are sent to all clients when state changes occur

### JSON Serialization
- Uses Google GSON library for JSON serialization/deserialization
- Custom TypeToken used for deserializing lists of objects

### Logging
- All communication is logged with:
  - Timestamp
  - IP address and port
  - Message type and content
- Logs are stored in `server_logs/` directory
- Separate log file for each day (Multiton pattern)

### Observer Pattern
- Server implements ModelListener to receive notifications about model changes
- Clients implement ModelListener to receive broadcast notifications
- Ensures all clients stay synchronized with server state

---

## Testing with Wireshark

To capture and analyze the protocol:

1. Start Wireshark and capture on the loopback interface
2. Apply filter: `tcp.port == 8080`
3. Start the server
4. Start one or more clients
5. Perform operations (borrow, reserve, return)
6. Observe the TCP 3-way handshake, application layer messages, and connection teardown

Look for:
- SYN, SYN-ACK, ACK packets in the 3-way handshake
- JSON messages in the TCP payload
- Source/destination IP addresses and ports
