const fs=require('node:fs'),path=require('node:path');
const target=path.join(__dirname,'../android/app/src/main/assets');
fs.mkdirSync(target,{recursive:true});fs.cpSync(path.join(__dirname,'../web'),target,{recursive:true});console.log('Shared web assets copied to Android.');
