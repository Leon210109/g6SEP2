@echo off
rem Run the app with JavaFX 25.
rem Requires JAVAFX_HOME to point at your JavaFX SDK folder (the one that contains a lib subfolder).
rem Example: setx JAVAFX_HOME "C:\javafx-sdk-25"  (then open a new terminal)

if "%JAVAFX_HOME%"=="" (
  echo ERROR: JAVAFX_HOME is not set.
  echo Please set it to your JavaFX SDK folder and reopen this terminal.
  echo Example:  setx JAVAFX_HOME "C:\javafx-sdk-25"
  pause
  exit /b 1
)

set REPO_ROOT=%~dp0
set APP_CLASSES=%REPO_ROOT%out\production\g6SEP2
set CLASSPATH=%APP_CLASSES%;%REPO_ROOT%lib\gson-2.11.0.jar;%REPO_ROOT%lib\postgresql-42.7.11.jar
set MODULEPATH=%JAVAFX_HOME%\lib

echo Running with JAVAFX_HOME=%JAVAFX_HOME%

java --module-path "%MODULEPATH%" --add-modules javafx.controls,javafx.fxml -cp "%CLASSPATH%" View.Main %*

