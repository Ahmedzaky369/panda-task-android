@echo off
REM APK Builder Script for Panda Task Android App
REM This script builds both debug and release APKs

echo ========================================
echo Panda Task APK Builder
echo ========================================
echo.

REM Check if gradlew exists
if not exist "gradlew.bat" (
    echo ERROR: gradlew.bat not found!
    echo Make sure you're running this from the android-app directory
    pause
    exit /b 1
)

echo Step 1: Cleaning previous builds...
call gradlew.bat clean
if %errorlevel% neq 0 (
    echo ERROR: Clean failed!
    pause
    exit /b 1
)

echo.
echo Step 2: Building debug APK...
call gradlew.bat assembleDebug
if %errorlevel% neq 0 (
    echo ERROR: Debug build failed!
    pause
    exit /b 1
)

echo.
echo Step 3: Building release APK (unsigned)...
call gradlew.bat assembleRelease
if %errorlevel% neq 0 (
    echo ERROR: Release build failed!
    pause
    exit /b 1
)

echo.
echo ========================================
echo BUILD COMPLETED SUCCESSFULLY!
echo ========================================
echo.
echo APK Files created:
echo Debug APK:   app\build\outputs\apk\debug\app-debug.apk
echo Release APK: app\build\outputs\apk\release\app-release-unsigned.apk
echo.
echo To install on device:
echo adb install app\build\outputs\apk\debug\app-debug.apk
echo.
echo Or copy the APK file to your Android device and install manually.
echo (Make sure "Unknown sources" is enabled in device settings)
echo.
pause