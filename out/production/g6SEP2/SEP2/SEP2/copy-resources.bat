@echo off
rem Copy FXML and CSS resources from src\View to out\production\g6SEP2\View
set SRC_DIR=%~dp0src\View
set OUT_DIR=%~dp0out\production\g6SEP2\View

if not exist "%OUT_DIR%" mkdir "%OUT_DIR%"
echo Copying FXML and CSS resources to "%OUT_DIR%"...
xcopy /Y /I "%SRC_DIR%\*.fxml" "%OUT_DIR%\" >nul 2>&1
if exist "%SRC_DIR%\styles.css" xcopy /Y /I "%SRC_DIR%\styles.css" "%OUT_DIR%\" >nul 2>&1
echo Done.
exit /b 0
