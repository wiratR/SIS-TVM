const {packager}=require('@electron/packager');
const path=require('node:path');
const platform=process.argv[2];
if(!['linux','win32'].includes(platform))throw Error('Specify linux or win32');
packager({dir:path.join(__dirname,'..'),name:'SIS-TVM-MMI',platform,arch:'x64',out:path.join(__dirname,'../release'),overwrite:true,asar:true,prune:true,ignore:[/^\/android($|\/)/,/^\/tests($|\/)/,/^\/scripts($|\/)/,/^\/release($|\/)/,/^\/\.github($|\/)/,/^\/build/],executableName:'sis-tvm-mmi',appCopyright:'SIS · Demo'}).then(p=>console.log(p.join('\n'))).catch(e=>{console.error(e);process.exit(1)});
