package com.sis.tvm;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.print.PrintManager;
import android.content.Context;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings;
import android.view.View;
import android.view.WindowManager;

public class MainActivity extends Activity {
 private WebView web;
 @Override public void onCreate(Bundle state) {
  super.onCreate(state);
  getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
  web=new WebView(this);
  web.setPadding(0,0,0,0);
  web.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener(){public android.view.WindowInsets onApplyWindowInsets(View v,android.view.WindowInsets insets){v.setPadding(insets.getSystemWindowInsetLeft(),insets.getSystemWindowInsetTop(),insets.getSystemWindowInsetRight(),insets.getSystemWindowInsetBottom());return insets;}});
  setContentView(web);
  WebSettings s=web.getSettings();s.setJavaScriptEnabled(true);s.setDomStorageEnabled(true);
  s.setAllowFileAccess(false);s.setAllowContentAccess(false);s.setAllowFileAccessFromFileURLs(false);s.setAllowUniversalAccessFromFileURLs(false);
  s.setMixedContentMode(WebSettings.MIXED_CONTENT_NEVER_ALLOW);
  web.setWebViewClient(new WebViewClient(){
   @Override public boolean shouldOverrideUrlLoading(WebView view,String url){return true;}
   @Override public boolean shouldOverrideUrlLoading(WebView view,android.webkit.WebResourceRequest req){return true;}
  });
  web.addJavascriptInterface(new PrintBridge(),"TvmAndroid");
  web.loadUrl("file:///android_asset/index.html");
 }
 public class PrintBridge {
  @JavascriptInterface public void printTickets(){runOnUiThread(new Runnable(){public void run(){
   PrintManager manager=(PrintManager)getSystemService(Context.PRINT_SERVICE);
   if(manager!=null)manager.print("SIS TVM Demo Ticket",web.createPrintDocumentAdapter("SIS-TVM-Demo"),null);
  }});}
 }
 @Override public void onBackPressed(){new AlertDialog.Builder(this).setTitle("Exit demo?").setMessage("Any simulated payment in progress will be discarded.").setPositiveButton("Exit",new android.content.DialogInterface.OnClickListener(){public void onClick(android.content.DialogInterface d,int w){finish();}}).setNegativeButton("Cancel",null).show();}
 @Override protected void onDestroy(){if(web!=null){web.removeJavascriptInterface("TvmAndroid");web.destroy();}super.onDestroy();}
}
