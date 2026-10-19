'use strict';
const {app,BrowserWindow,ipcMain,Menu}=require('electron');
const path=require('node:path');
let win;
app.setName('SIS TVM MMI');
const locked=app.requestSingleInstanceLock();
if(!locked)app.quit();
else {
 app.on('second-instance',()=>{if(win){if(win.isMinimized())win.restore();win.focus();}});
 app.whenReady().then(()=>{
  Menu.setApplicationMenu(null);
  win=new BrowserWindow({width:1280,height:900,minWidth:720,minHeight:600,title:'SIS TVM MMI · DEMO',backgroundColor:'#e9edf4',kiosk:process.argv.includes('--kiosk'),webPreferences:{preload:path.join(__dirname,'preload.cjs'),nodeIntegration:false,contextIsolation:true,sandbox:true}});
  win.webContents.setWindowOpenHandler(()=>({action:'deny'}));
  win.webContents.on('will-navigate',e=>e.preventDefault());
  win.webContents.session.setPermissionRequestHandler((_wc,_permission,cb)=>cb(false));
  ipcMain.handle('tvm:print',async event=>{
    if(event.sender!==win.webContents)throw Error('Invalid sender');
    return new Promise(resolve=>win.webContents.print({silent:false,printBackground:true},(ok,reason)=>resolve({ok,reason:reason||''})));
  });
  win.loadFile(path.join(__dirname,'../web/index.html'));
 });
 app.on('window-all-closed',()=>app.quit());
}
