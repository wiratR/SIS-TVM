const {contextBridge,ipcRenderer}=require('electron');
contextBridge.exposeInMainWorld('tvmDesktop',{print:()=>ipcRenderer.invoke('tvm:print')});
