#!/bin/bash
# APK Builder Script for Panda Task Android App (Unix/Linux/Mac)
# This script builds both debug and release APKs

echo "========================================"
echo "Panda Task APK Builder"
echo "========================================"
echo

# Check if gradlew exists
if [ ! -f "./gradlew" ]; then
    echo "ERROR: gradlew not found!"
    echo "Make sure you're running this from the android-app directory"
    exit 1
fi

# Make gradlew executable
chmod +x ./gradlew

echo "Step 1: Cleaning previous builds..."
./gradlew clean
if [ $? -ne 0 ]; then
    echo "ERROR: Clean failed!"
    exit 1
fi

echo
echo "Step 2: Building debug APK..."
./gradlew assembleDebug
if [ $? -ne 0 ]; then
    echo "ERROR: Debug build failed!"
    exit 1
fi

echo
echo "Step 3: Building release APK (unsigned)..."
./gradlew assembleRelease
if [ $? -ne 0 ]; then
    echo "ERROR: Release build failed!"
    exit 1
fi

echo
echo "========================================"
echo "BUILD COMPLETED SUCCESSFULLY!"
echo "========================================"
echo
echo "APK Files created:"
echo "Debug APK:   app/build/outputs/apk/debug/app-debug.apk"
echo "Release APK: app/build/outputs/apk/release/app-release-unsigned.apk"
echo
echo "To install on device:"
echo "adb install app/build/outputs/apk/debug/app-debug.apk"
echo
echo "Or copy the APK file to your Android device and install manually."
echo "(Make sure 'Unknown sources' is enabled in device settings)"
echo