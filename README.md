# SIS TVM MMI — Cross-platform demo 0.1.0

ใช้ UI และ transaction flow ชุดเดียวกันจากโฟลเดอร์ web/ แบบออฟไลน์
Windows/Linux: Electron desktop runtime; Android: native Java Activity + Android WebView

## เปิดแอปที่ build แล้ว
- Windows x64: แตก ZIP ทั้งโฟลเดอร์ แล้วเปิด sis-tvm-mmi.exe (ต้องเก็บไฟล์ข้างเคียงไว้ด้วย)
- Linux x64: แตก tar.gz แล้วรัน ./sis-tvm-mmi จาก desktop session ของผู้ใช้ทั่วไป
  ห้ามรันเป็น root หรือปิด Chromium sandbox; Linux ต้องมีไลบรารี GTK/NSS/เสียงที่ Electron ต้องใช้
- Android 8 ขึ้นไป: ติดตั้ง APK ทดสอบ เปิดสิทธิ์ติดตั้งจากแหล่งที่เลือกเมื่อระบบถาม
  Android ต้องมี System WebView; อาจต้องยืนยันติดตั้ง debug APK ด้วยตนเอง
- Windows package และ Android debug APK ยังไม่ได้เซ็นด้วย certificate สำหรับการจำหน่าย

## ทดสอบ
เลือกสถานี → จำนวนตั๋ว → ชำระเงิน → กดปุ่มเหรียญ/ธนบัตร → ออก QR → พิมพ์ทดสอบ
ขยาย Demo controls ด้านล่างเพื่อทดสอบกระดาษหมด เงินทอนไม่พอ และดูประวัติ
ราคา/QR ใช้ทดสอบเท่านั้น; QR แต่ละใบมี DEMO และ valid:false ห้ามใช้เดินทางจริง
ประวัติรายการเก็บใน localStorage ของแอป (ไม่ใช่ financial database)
หากปิดแอประหว่างจำลองชำระเงิน ยอดเงินที่กำลังรับจะไม่ถูกกู้คืน

## พัฒนาต่อ
ต้องมี Node.js 22.12 ขึ้นไป และ npm
```
npm ci
npm test
npm start
npm run start:kiosk
```
ถ้า npm start แจ้งไม่มี Electron binary ให้รัน `npx install-electron` หนึ่งครั้ง

### Windows portable app (ใช้เครื่อง Windows x64)
```
npm run build:windows
```
ผลลัพธ์: release/SIS-TVM-MMI-win32-x64/sis-tvm-mmi.exe พร้อม runtime

### Linux app (ใช้เครื่อง Linux x64)
```
npm run build:linux
```
ผลลัพธ์: release/SIS-TVM-MMI-linux-x64/sis-tvm-mmi

### Android APK
ติดตั้ง Android Studio + SDK Platform 35 + Build Tools 35.0.0 + JDK 17
ตั้งค่า ANDROID_HOME/ANDROID_SDK_ROOT หรือ android/local.properties ด้วย sdk.dir ของเครื่องตนเอง
ใช้ Gradle 8.9 หรือ gradle wrapper ใน android/
```
node scripts/sync-android.cjs
cd android
# Windows:
gradlew.bat assembleDebug
# Linux:
./gradlew assembleDebug
```
ผลลัพธ์: android/app/build/outputs/apk/debug/app-debug.apk
Release: assembleRelease ให้ unsigned APK; ต้องจัด signing key เองและเก็บ key นอก repo

## โครงสร้าง
- web/: UI ไทย/อังกฤษ, map จาก MMI เดิม, QR encoder และ demo transaction
- desktop/: main process + isolated preload; renderer ไม่มีสิทธิ์ Node.js
- android/: Native Java wrapper, print ผ่าน Android Print Framework, ไม่ขอสิทธิ์ Internet
- scripts/: package และ sync shared UI
- tests/: transaction, QR generation, refund, paper-out, no-change, language, log persistence
- .github/workflows/build.yml: สั่ง build Windows/Linux/Android จาก GitHub Actions ได้หลังนำ source เข้า repo ของคุณ

## ขอบเขต
ไม่มีการเชื่อมอุปกรณ์รับเงิน เครื่องทอนเงิน เครื่องพิมพ์ serial/USB โดยตรง หรือ AFC
ปุ่มพิมพ์เปิด print dialog ของระบบ; driver/service ต้องติดตั้งบนเครื่องเอง
การพิมพ์ผ่าน Android WebView มีข้อจำกัดด้านขนาดกระดาษและ pagination ขึ้นกับ print service
แผนที่เป็น reference จากไฟล์ TVM BLE MMI; ไม่ใช่การจำลองหน้าจอเดิมแบบ pixel-perfect
ยังไม่ใช่ certified kiosk/production software และไม่มีการรับเงินจริง
หากใช้ production ต้องมี device adapters, AFC QR/API, fare table จริง, durable transaction/recovery,
reconciliation, authentication, deployment signing, watchdog และ acceptance tests บนอุปกรณ์จริง

## เอกสารอ้างอิง
https://www.electronjs.org/docs/latest/tutorial/application-distribution
https://developer.android.com/develop/ui/views/layout/webapps/webview
https://developer.android.com/develop/ui/views/layout/webapps/printing-html

## สิทธิ์การใช้
source ใหม่เตรียมให้ SIS ใช้พัฒนาต่อ; runtime และไลบรารี third-party ใช้ license ของแต่ละโครงการ
สถานะตรวจสอบ: transaction tests ผ่าน; Windows/Linux packages สร้างสำเร็จ; Android สร้างโดย SDK tools โดยตรงและตรวจ signature แล้ว
ยังไม่ได้เปิด UI บนเครื่อง Windows/Linux หรือ Android device/emulator; ยังไม่ได้พิมพ์กับเครื่องพิมพ์จริง
Gradle build ในสภาพแวดล้อมนี้ติด repository resolution; ใช้ Gradle/Android Studio build บนเครื่องพัฒนาตามขั้นตอนข้างต้น

QR encoder มาจาก qrcode-terminal/vendor/QRCode (Kazuhiko Arase, MIT)
รูปแผนที่มาจากไฟล์ผู้ใช้แนบ; สิทธิ์ในภาพยังคงเป็นของเจ้าของเดิม
