# UML Class Diagram Package Organization

## Package Structure and Class Assignment

### **model** package
- **Model** (interface)
- **ModelListener** (interface)
- **ModelManager** (class)
- **Vinyl** (class)
- **VinylList** (class)
- **State** (enum)

### **networking** package
- **VinylServer** (class)
- **ClientHandler** (class)
- **NetworkClient** (class)
- **Message** (class)
- **ServerLogger** (class)

### **viewmodel** package
- **AppVM** (class)
- **FrontVM** (class)
- **ManageVM** (class)
- **ViewState** (class)

### **view** package
- **ViewHandler** (class)
- **FrontController** (class)
- **ManageController** (class)

### **root** package (default package)
- **StartApplication** (class)
- **StartClient** (class)

---

## Package Layout Diagram

This diagram shows how the packages should be arranged relative to each other in your UML tool (e.g., Astah Professional):

```
┌─────────────────────────────────────────────────────────────────────┐
│                                                                     │
│                            ROOT PACKAGE                             │
│                    ┌──────────────────────┐                        │
│                    │  StartApplication    │                        │
│                    │  StartClient         │                        │
│                    └──────────┬───────────┘                        │
│                               │                                     │
└───────────────────────────────┼─────────────────────────────────────┘
                                │
                                │ creates / uses
                                ▼
┌─────────────────────────────────────────────────────────────────────┐
│                          VIEW PACKAGE                                │
│        ┌────────────────────────────────────────┐                   │
│        │  ViewHandler                           │                   │
│        │  FrontController                       │                   │
│        │  ManageController                      │                   │
│        └────────────┬───────────────────────────┘                   │
│                     │                                                │
└─────────────────────┼────────────────────────────────────────────────┘
                      │
                      │ uses
                      ▼
┌─────────────────────────────────────────────────────────────────────┐
│                       VIEWMODEL PACKAGE                              │
│        ┌────────────────────────────────────────┐                   │
│        │  AppVM                                 │                   │
│        │  FrontVM                               │                   │
│        │  ManageVM                              │                   │
│        │  ViewState                             │                   │
│        └────────────┬───────────────────────────┘                   │
│                     │                                                │
└─────────────────────┼────────────────────────────────────────────────┘
                      │
                      │ implements ModelListener / uses Model
                      ▼
┌─────────────────────────────────────────────────────────────────────┐
│                         MODEL PACKAGE                                │
│        ┌────────────────────────────────────────┐                   │
│        │  <<interface>> Model                   │                   │
│        │  <<interface>> ModelListener           │                   │
│        │  ModelManager                          │                   │
│        │  Vinyl                                 │                   │
│        │  VinylList                             │                   │
│        │  <<enum>> State                        │                   │
│        └────────────┬───────────────────────────┘                   │
│                     │                                                │
└─────────────────────┼────────────────────────────────────────────────┘
                      │
                      │ implements Model interface
                      ▼
┌─────────────────────────────────────────────────────────────────────┐
│                      NETWORKING PACKAGE                              │
│        ┌────────────────────────────────────────┐                   │
│        │  VinylServer                           │                   │
│        │  ClientHandler                         │                   │
│        │  NetworkClient                         │                   │
│        │  Message                               │                   │
│        │  ServerLogger                          │                   │
│        └────────────────────────────────────────┘                   │
│                                                                      │
└──────────────────────────────────────────────────────────────────────┘
```

---

## Alternative Horizontal Layout

If you prefer a horizontal layout for better use of screen space:

```
┌───────────────────┐
│   ROOT PACKAGE    │
│                   │
│ StartApplication  │
│ StartClient       │
└─────────┬─────────┘
          │
          ▼
┌──────────────────────────────────────────────────────────────────────┐
│                                                                      │
│  ┌───────────────┐     ┌─────────────────┐     ┌────────────────┐  │
│  │     VIEW      │────▶│   VIEWMODEL     │────▶│     MODEL      │  │
│  │               │     │                 │     │                │  │
│  │  ViewHandler  │     │  AppVM          │     │  Model         │  │
│  │  FrontCtrl    │     │  FrontVM        │     │  ModelManager  │  │
│  │  ManageCtrl   │     │  ManageVM       │     │  Vinyl         │  │
│  │               │     │  ViewState      │     │  VinylList     │  │
│  │               │     │                 │     │  State         │  │
│  └───────────────┘     └─────────────────┘     └───────┬────────┘  │
│                                                         │           │
│                                                         ▼           │
│                                        ┌─────────────────────────┐ │
│                                        │     NETWORKING          │ │
│                                        │                         │ │
│                                        │  VinylServer            │ │
│                                        │  ClientHandler          │ │
│                                        │  NetworkClient          │ │
│                                        │  Message                │ │
│                                        │  ServerLogger           │ │
│                                        └─────────────────────────┘ │
│                                                                      │
└──────────────────────────────────────────────────────────────────────┘
```

---

## Key Architectural Layers

The package organization follows the **MVVM (Model-View-ViewModel)** architecture pattern:

1. **View Layer** (view package)
   - User interface controllers
   - Handles user interaction
   - Binds to ViewModel

2. **ViewModel Layer** (viewmodel package)
   - Presentation logic
   - Observable data for View binding
   - Implements ModelListener to react to model changes

3. **Model Layer** (model package)
   - Business logic
   - Domain entities (Vinyl, State)
   - Data structures (VinylList)
   - Interfaces for loose coupling

4. **Networking Layer** (networking package)
   - Client-server communication
   - NetworkClient acts as a proxy to remote Model
   - VinylServer hosts the actual Model
   - JSON-based protocol (Message class)
   - Logging (ServerLogger - Multiton pattern)

5. **Application Entry Points** (root package)
   - StartApplication: Standalone mode with local ModelManager
   - StartClient: Client mode with NetworkClient proxy

---

## Design Patterns Used

1. **MVVM Pattern**: View ↔ ViewModel ↔ Model separation
2. **Observer Pattern**: ModelListener interface for event notifications
3. **Proxy Pattern**: NetworkClient implements Model but delegates to remote server
4. **Multiton Pattern**: ServerLogger (one instance per day)
5. **State Pattern**: Vinyl state management (via State enum)
6. **Singleton Pattern**: ServerLogger.getInstance() for current day
7. **Factory Pattern**: Message static factory methods

---

## Import/Export Notes for Astah Professional

### Compatible Features:
- All classes, interfaces, and enums
- Inheritance relationships (extends)
- Implementation relationships (implements)
- Association relationships (uses, has, contains)
- Multiplicity (aggregation, composition)
- Visibility modifiers (+, -, #)
- Static members ({static})

### Not Included (as requested):
- No notes or comments
- No package containers in the diagram
- Classes are shown without package nesting

### Usage Instructions:
1. Import the `ClassDiagram.puml` file into Astah using the PlantUML plugin
2. Manually organize classes into packages using this guide
3. Arrange packages according to the layout diagrams above
4. Use the "Auto Layout" feature if needed, then refine manually

---

## Class Count Summary

- **Total Classes**: 19
- **Interfaces**: 3 (Model, ModelListener, Runnable)
- **Enums**: 1 (State)
- **Concrete Classes**: 15

**By Package:**
- model: 6 (2 interfaces, 1 enum, 3 classes)
- networking: 5
- viewmodel: 4
- view: 3
- root: 2
