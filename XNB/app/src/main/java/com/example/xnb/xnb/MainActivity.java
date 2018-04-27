package com.example.xnb.xnb;

import android.app.Activity;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Set;

public class MainActivity extends Activity {
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = (WebView) findViewById(R.id.webview);
        WebSettings settings = webView.getSettings();
        webView.setVerticalScrollbarOverlay(true);
        // 设置与Js交互的权限
        settings.setJavaScriptEnabled(true);
        // 设置允许JS弹窗
        settings.setJavaScriptCanOpenWindowsAutomatically(true);

        String url = "file:///android_asset/text.html";
        webView.loadUrl(url);
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                // 根据协议的参数，判断是否是所需要的url(原理同方式2)
                // 一般根据scheme（协议格式） & authority（协议名）判断（前两个参数）
                //假定传入进来的 url = "js://webview?arg1=111&arg2=222"（同时也是约定好的需要拦截的）

                Uri uri = Uri.parse(message);
                // 如果url的协议 = 预先约定的 js 协议
                // 就解析往下解析参数
                Log.i("ceshi",uri.getScheme()+".....1");
                if ( uri.getScheme().equals("js")) {

                    // 如果 authority  = 预先约定协议里的 webview，即代表都符合约定的协议
                    // 所以拦截url,下面JS开始调用Android需要的方法
                    Log.i("ceshi",uri.getAuthority()+".....2");

                    if (uri.getAuthority().equals("webview")) {

                        //
                        // 执行JS所需要调用的逻辑
                        Log.i("ceshi","js调用了Android的方法"+".....3");
                        // 可以在协议上带有参数并传递到Android上
                        HashMap<String, String> params = new HashMap<>();
                        Set<String> collection = uri.getQueryParameterNames();
                        //参数result:代表消息框的返回值(输入值)
//                        result.confirm("js调用了Android的方法成功啦");
                        webView.loadUrl("https://www.baidu.com/");
                        Log.i("ceshi",uri.getQueryParameter("arg2")+".....4");
                        show();
//                        Intent intent=new Intent(MainActivity.this,TextActivity.class);
//                        startActivity(intent);
                    }
                    return true;
                }


                return super.onJsPrompt(view, url, message, defaultValue, result);
            }
        });





        // 设置缩放级别
        settings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        // 支持缩放
        settings.setSupportZoom(true);
        // 将网页内容以单列显示
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);



    }
    public  void show(){
        Toast.makeText(MainActivity.this,"nihao", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * 再点一次退出
     */
    private long mLastTime = 0;

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - mLastTime > 2000) {
            // 两次返回时间超出两秒
            Toast.makeText(this, "再点一次退出程序", Toast.LENGTH_SHORT).show();
            mLastTime = System.currentTimeMillis();
        } else {
            // 两次返回时间小于两秒，可以退出
            finish();
        }
    }

}
