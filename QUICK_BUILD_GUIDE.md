# 🚀 Quick APK Build Guide

## Fastest Way to Get Your APK

### Step 1: Prerequisites
- **Download Android Studio**: https://developer.android.com/studio
- **OR** Install Java 8+ for command line build

### Step 2: Build APK

**Option A - Simple Script (Recommended):**
```bash
# Windows
cd android-app
build-apk.bat

# Mac/Linux  
cd android-app
./build-apk.sh
```

**Option B - Android Studio:**
1. Open `android-app` folder in Android Studio
2. Wait for sync to complete
3. Build → Generate Signed Bundle/APK → APK → Build

### Step 3: Install APK
```bash
# Via ADB (if device connected)
adb install app/build/outputs/apk/debug/app-debug.apk

# OR copy app-debug.apk to device and install manually
```

## What You'll Get

✅ **Native Android App** (6-8MB APK)
- Perfect replica of your PWA
- Offline-capable with Room database  
- 60fps smooth animations
- Material 3 dark theme + green accent
- All features: Today, All Tasks, Plan, Settings

## Expected Build Time
- **First build**: 5-15 minutes (downloads dependencies)
- **Subsequent**: 1-3 minutes

## Files Created
- `app-debug.apk` - Ready to install and test
- `app-release-unsigned.apk` - For Play Store (needs signing)

That's it! Your PWA is now a native Android app. 🎉