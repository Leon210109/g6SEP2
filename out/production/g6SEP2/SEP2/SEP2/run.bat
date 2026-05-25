@echo off
rem Run the app with JavaFX 25. Requires JAVAFX_HOME to point at JavaFX SDK folder (the one that contains lib)
rem Example: setx JAVAFX_HOME "C:\javafx-sdk-25" (open a new terminal afterwards)

nSETLOCAL ENABLEDELAYEDEXPANSION

nif "%JAVAFX_HOME%"=="" (
  echo ERROR: JAVAFX_HOME is not set. Please set it to your JavaFX SDK folder (contains lib) and reopen terminal.
  echo Example: setx JAVAFX_HOME "C:\javafx-sdk-25"
  pause
  exit /b 1
)

nrem Determine paths (script location may contain spaces)
set REPO_ROOT=%~dp0
set APP_CLASSES="%REPO_ROOT%out\production\g6SEP2"
set LIB_DIR="%REPO_ROOT%lib"

nrem Build classpath -- add any jars in lib you rely on (gson, postgresql)
set CLASSPATH=%APP_CLASSES%;%LIB_DIR%\gson-2.11.0.jar;%LIB_DIR%\postgresql-42.7.11.jar

nrem JavaFX module path
set MODULEPATH=%JAVAFX_HOME%\lib

necho Running with JAVAFX_HOME=%JAVAFX_HOME%

nrem Run the application (non-modular). Main class: View.Main
java --module-path "%MODULEPATH%" --add-modules javafx.controls,javafx.fxml -cp "%CLASSPATH%" View.Main %*

nENDLOCAL

