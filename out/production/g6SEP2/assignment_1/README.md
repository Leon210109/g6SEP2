# Vinyl Library — Simplified MVVM rewrite (no Maven)

This repository was adjusted to not use Maven at all per your request. The project now uses plain javac/java commands for building and running. If you prefer a build tool in the future, it can be re-added, but by default this repo no longer depends on Maven.

What changed
- `pom.xml` was removed/replaced with a placeholder. No Maven usage.
- JUnit test files (which required Maven/JUnit) were disabled.
- The project contains a small non-GUI smoke test (`src/TestModelRunner.java`) to verify model behavior without needing extra dependencies.

Quick model smoke test (no JavaFX, no Maven)
1) Compile and run the model smoke test (Windows cmd.exe):

```cmd
javac --release 8 -d out src\model\*.java src\TestModelRunner.java
java -cp out TestModelRunner
```

This will compile the model classes and run a small program that exercises add/reserve/borrow/return/remove on vinyl records and prints the state transitions.

Run the GUI (JavaFX) without Maven
- To run the JavaFX application without Maven you must have JavaFX SDK available locally and pass the module path to javac/java. Example (replace C:\path\to\javafx-sdk\lib with your JavaFX lib path):

```cmd
set JAVAFX_LIB=C:\path\to\javafx-sdk-17\lib
javac --release 17 -d out --module-path "%JAVAFX_LIB%" --add-modules javafx.controls,javafx.fxml src\*.java src\model\*.java src\view\*.java src\view\front\*.java src\view\manage\*.java src\viewmodel\*.java
java --module-path "%JAVAFX_LIB%" --add-modules javafx.controls,javafx.fxml -cp out StartApplication
```

Notes
- The README above contains the exact commands to build and run the model test and instructions for running the GUI if you have JavaFX.
- If you want me to add small .bat scripts (e.g., `build-model.bat`, `run-gui.bat`) to streamline building/running without Maven, I can add them.

If you want the JUnit test restored without Maven, I can vendor the JUnit jar(s) into a `lib/` folder and add a small `run-tests.bat` that compiles tests with that jar on the classpath; say "vendor junit" if you want that.
