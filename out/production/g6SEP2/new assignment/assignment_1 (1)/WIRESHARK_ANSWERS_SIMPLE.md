# ASSIGNMENT 2 - WIRESHARK ANALYSIS ANSWERS

## 1. Three-Way Handshake Segments

The 3-way handshake segments are identified by examining the TCP flags:

| Packet | Frame | Direction | TCP Flags | Flag Values | Description |
|--------|-------|-----------|-----------|-------------|-------------|
| **1st** | 79 | Client → Server | 0x0002 | SYN=1, ACK=0 | Client initiates connection (SYN) |
| **2nd** | 80 | Server → Client | 0x0012 | SYN=1, ACK=1 | Server responds with synchronization (SYN-ACK) |
| **3rd** | 81 | Client → Server | 0x0010 | SYN=0, ACK=1 | Client acknowledges (ACK) - connection established |

**How they are identified:**
- **Frame 79 (SYN):** Only the SYN flag is set (0x0002)
- **Frame 80 (SYN-ACK):** Both SYN and ACK flags are set (0x0012)
- **Frame 81 (ACK):** Only the ACK flag is set (0x0010), SYN is now cleared

---

## 2. IP Addresses and Port Numbers

**Client:**
- IP Address: `127.0.0.1`
- Port Number: `49934`

**Server:**
- IP Address: `127.0.0.1`
- Port Number: `8080`

**Complete Socket:**
```
Client: 127.0.0.1:49934  ↔  Server: 127.0.0.1:8080
```

**Note:** Both run on localhost (127.0.0.1). The client uses ephemeral port 49934 (assigned by OS), while the server listens on well-known port 8080.

---

## 3. Application Layer BORROW Messages

### BORROW Request (Client → Server):

```json
{
  "type": "BORROW",
  "data": "{\"vinyl\":{\"title\":\"Abbey Road\",\"artist\":\"Beatles\",\"year\":\"1969\",\"state\":\"AVAILABLE\",\"markedForRemoval\":false},\"userName\":\"Manager\"}",
  "success": true
}
```

**Key Information:**
- **Type:** BORROW
- **Vinyl:** Abbey Road by Beatles (1969)
- **User:** Manager
- **Initial State:** AVAILABLE

---

### BORROW Response (Server → Client):

```json
{
  "type": "BORROW_RESPONSE",
  "data": "Vinyl borrowed successfully",
  "success": true
}
```

**Key Information:**
- **Type:** BORROW_RESPONSE
- **Result:** Vinyl borrowed successfully
- **Status:** Success = true

---

### Additional Broadcast (Server → All Clients):

The server also broadcasted an update to all connected clients:

```json
{
  "type": "VINYL_UPDATED",
  "data": "{\"title\":\"Abbey Road\",\"artist\":\"Beatles\",\"year\":\"1969\",\"state\":\"BORROWED\",\"markedForRemoval\":false,\"borrowName\":\"Manager\"}",
  "success": true
}
```

**Key Information:**
- **Type:** VINYL_UPDATED
- **New State:** BORROWED (changed from AVAILABLE)
- **Borrowed By:** Manager

---

## Summary

✅ All three requirements documented:
1. TCP handshake identified by SYN/SYN-ACK/ACK flag patterns
2. Socket connection: 127.0.0.1:49934 (client) ↔ 127.0.0.1:8080 (server)
3. BORROW request/response captured showing JSON message exchange

✅ Protocol features observed:
- All messages in JSON format as required
- Request-response pattern implemented
- Server broadcasts state changes to all clients
- Error handling with success/error fields
