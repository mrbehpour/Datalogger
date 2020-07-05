package ir.saa.android.datalogger;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import mycomponents.MyUtilities;

public class SvgActivity extends AppCompatActivity implements View.OnClickListener {
//    Button valid;
//    Button refuse;

    public  void adjustFontScale(Configuration configuration, Float fontSize) {

        configuration.fontScale = (float) fontSize;
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        metrics.scaledDensity = configuration.fontScale * metrics.density;
       getBaseContext().getResources().updateConfiguration(configuration, metrics);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_svg);
        if(G.RTL==false){
            adjustFontScale(getResources().getConfiguration(),(float)1.5);
        }
        adjustFontScale(getResources().getConfiguration(),(float)1.5);
        WebView wv = (WebView) findViewById(R.id.webView1);
//        String htmlString = "<html>\n" +
//                "<head>\n" +
//                "</head>\n" +
//                "<body>\n" +
//                "\t<div>\n" +
//                "\t\t <button type=\"button\" id=\"ok\" style=\"font-weight: 700; margin-right: 20px;\" onclick=\"validClick();\">J'accepte</button>\n" +
//                "\t\t <button type=\"button\" id=\"no\" onclick=\"refuseClick();\">Je refuse</button>\n" +
//                "\t</div>\n" +
//                "\t<script language=\"javascript\">\n" +
//                "\n" +
//                "\t   function validClick()\n" +
//                "\t   {\n" +
//                "\t\t  valid.performClick();\n" +
//                "\t\t  document.getElementById(\"ok\").value = \"J'accepte\";\n" +
//                "\t   }\n" +
//                "\t   function refuseClick()\n" +
//                "\t   {\n" +
//                "\t\t  refuse.performClick();\n" +
//                "\t\t  document.getElementById(\"no\").value = \"Je refuse\";\n" +
//                "\t   }\n" +
//                "\n" +
//                "\t</script>\n" +
//                "</body>\n" +
//                "</html>";

//        valid = new Button(G.context);
//        valid.setOnClickListener(this);
//        refuse = new Button(G.context);
//        refuse.setOnClickListener(this);

        WebSettings ws = wv.getSettings();
        ws.setJavaScriptEnabled(true);

        class JsFunc {
            public String functionName;
            public String value1;
        }

        class JsObject {
            @JavascriptInterface
            public String toString() { return "hello world"; }

            @JavascriptInterface
            public String toString2() { return "heeeeeeyyyyy"; }

            @JavascriptInterface
            public String passToAndroid(String str) {
                return str;
            }
            @JavascriptInterface
            public void callFunction(Object obj) {

            }
        }
        wv.getSettings().setJavaScriptEnabled(true);
        //myWebview.getSettings().setPluginsEnabled(true);
        wv.getSettings().setAllowFileAccess(true);
        wv.getSettings().setLoadWithOverviewMode(false);
        wv.getSettings().setUseWideViewPort(false);
        JsObject jsObj = new JsObject();
        wv.addJavascriptInterface(jsObj, "injectedObject");

        String strHtml = "<meta content=\"text/html; charset=utf-8\" http-equiv=\"content-type\" />\n" +
                "<html>\n" +
                "<head>\n" +
                "<script type='text/javascript'>"+
                MyUtilities.getStringContentFromFile(G.context,"htmls/js/jquery-3.3.1.min.js")+
                "</script>"+
                "<script type='text/javascript'>"+
                MyUtilities.getStringContentFromFile(G.context,"htmls/js/jquery.mobile-1.4.5.js")+
                "</script>"+
                "<script>$(document).ready(function() {  $('body').css( 'background-color', 'yellow');  injectedObject.passToAndroid('Salam IMAN'); });function showSecretMessage(sval) { $('body').html(sval); };</script>"+
                "<title></title>\n" +
                "<style type=\"text/css\">\n" +
                "body {\n" +
                "    background-color: #000000;\n" +
                "}\n" +
                "\n" +
                "/*@font-face {\n" +
                "    font-family: byekan;\n" +
                "    src: url('file:///android_asset/fonts/byekan.ttf');\n" +
                "}\n" +
                "\n" +
                "p {\n" +
                "    color: #000000;\n" +
                "    font-family: byekan,tahoma, sans-serif;\n" +
                "}*/\n" +
                ".Install_BG_Image {\n" +
                "    position: fixed;\n" +
                "    height: 120%;\n" +
                "    left: 0%;\n" +
                "    top: 0%;\n" +
                "}\n" +
                "</style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <img class=\"Install_BG_Image\" src=\"file:///android_asset/htmls/images/2.jpg\" />\n" +
                "    <img class=\"Install_BG_Image\" src=\"file:///android_asset/htmls/images/5.jpg\" />\n" +
                "    <img class=\"Install_BG_Image\" src=\"file:///android_asset/htmls/images/6.jpg\" />\n" +
                "    <img class=\"Install_BG_Image\" src=\"file:///android_asset/htmls/images/7.jpg\" />\n" +
                "    <img class=\"Install_BG_Image\" src=\"file:///android_asset/htmls/images/8.jpg\" />\n" +
                "    <img class=\"Install_BG_Image\" src=\"file:///android_asset/htmls/images/1.jpg\" />\n" +
                "    <img class=\"Install_BG_Image\" src=\"file:///android_asset/htmls/images/3.jpg\" />\n" +
                "    <img class=\"Install_BG_Image\" src=\"file:///android_asset/htmls/images/4.jpg\" />\n" +
                "\n" +
                "</body>\n" +
                "</html>";

//        wv.setWebViewClient(new WebViewClient(){
//            @Override
//            public void onReceivedError(WebView view, int errorCode,String description, String failingUrl) {
//                Log.i("Err",description);
//            }
//        });

        wv.loadData(strHtml, "text/html", null);

//        JsFunc jsFunc = new JsFunc();
//        jsFunc.functionName = "SomeFunction";
//        jsFunc.value1 = "asfasfasf";



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                RunJsOnWebview(wv,"showSecretMessage('Lets Goooooo');");
            }
        },1000);
    }

    public void RunJsOnWebview(WebView wv,String str){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            wv.evaluateJavascript(str, null);
        } else {
            wv.loadUrl("javascript:"+str);
        }
    }

    @Override
    public void onClick(View v) {
//        if (v.equals(valid)) {
//            Log.i("TAG","");
//        } else if (v.equals(refuse)) {
//            Log.i("TAG","");
//        }
    }
}
