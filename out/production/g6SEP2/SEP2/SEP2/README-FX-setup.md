JavaFX (OpenJFX) setup for this project (JDK 25)

Problem
- JDK 11+ no longer ships JavaFX. After upgrading to JDK 25, FXML/JavaFX classes are missing at runtime.

Goal
- Make it easy to run this project with JavaFX 25 on Windows. Provide a run script and instructions to install JavaFX.

Steps (quick)
1. Download JavaFX SDK 25 for Windows from Gluon: https://gluonhq.com/products/javafx/ or Maven Central (org.openjfx).
2. Unzip the SDK to a folder, for example: C:\javafx-sdk-25. The folder must contain a `lib` folder with jars like `javafx-base.jar`, `javafx-controls.jar`, `javafx-fxml.jar`, etc.
3. Set an environment variable `JAVAFX_HOME` to the SDK folder path (the parent that contains `lib`). Example (CMD):
   setx JAVAFX_HOME "C:\javafx-sdk-25"
   Then open a new terminal.

Run the app (compiled classes exist in `out\production\g6SEP2`)
1. From repository root open cmd.exe and run:
   run.bat

What `run.bat` does
- Verifies `JAVAFX_HOME` is set.
- Builds a CLASSPATH including the compiled classes and the bundled `lib` jars (gson & postgresql included if present).
- Runs the main class `View.Main` with `--module-path` pointing to `%JAVAFX_HOME%\lib` and `--add-modules javafx.controls,javafx.fxml` so JavaFX modules are available at runtime.

If you prefer a build tool
- Adding Maven/Gradle will make dependency management simpler. I can add a `pom.xml` or `build.gradle` that pulls org.openjfx artifacts and sets the run plugin if you want.

Troubleshooting
- If you get ClassNotFoundException for JavaFX classes, check that `%JAVAFX_HOME%\lib` exists and contains javafx jars.
- If FXMLLoader throws IllegalAccessException related to controllers, let me know the exact stack trace; we may need to add a `module-info.java` (modular project) and `opens` your `View` package to `javafx.fxml`.

Next steps I can do for you (pick any):
- Add a `pom.xml` that depends on OpenJFX 25 so you can run via Maven.
- Add a `module-info.java` and open the `View` package to JavaFX (if you want a modular app).
- Create a `build.bat` that recompiles sources with JavaFX on the module-path.


