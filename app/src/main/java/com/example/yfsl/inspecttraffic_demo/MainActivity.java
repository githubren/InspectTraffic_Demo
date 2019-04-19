package com.example.yfsl.inspecttraffic_demo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.net.TrafficStats;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    double totalWifi;
    public static String currentWifiTraffic;
    public static String totalWifiTraffic;
    public static double currentWFSpeed = 0;
    public static double totalWF = 0;
    private TextView textView;
    private TextView showTotalTraffic;
    private WebView webView;
    private WifiManager wifiManager;
    public static int wifiStr;
    DecimalFormat df = new DecimalFormat(".##");
    private TextView tv;
    private Handler handler1 = null;
    int hours = 0,minutes = 0,seconds = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();
        //initWebView();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {
        webView = new WebView(this);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("https://www.baidu.com");
        setContentView(webView);
    }

    @SuppressLint("HandlerLeak")
    private void initData() {
        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        wifiStr = wifiManager.getWifiState();
        if (wifiStr != 3){
            Toast.makeText(this, "Wifi未连接！", Toast.LENGTH_SHORT).show();
        }
        else{
            @SuppressLint("HandlerLeak")
            final Handler handler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (msg.what == 1){
                        if (Double.parseDouble(currentWifiTraffic)<1){
                            textView.setText("0"+currentWifiTraffic+"MB/S");
                            Log.e("TAG","网速："+"0"+currentWifiTraffic+"MB/S");
                            showTotalTraffic.setText("0"+totalWifiTraffic+"MB");
                            Log.e("TAG","流量："+"0"+totalWifiTraffic+"MB");
                        }
                        else {
                            textView.setText(currentWifiTraffic+"MB/S");
                            showTotalTraffic.setText(totalWifiTraffic+"MB");
                        }
                    }
                }
            };
            if (wifiStr == 3){
                handler1 = new Handler(){
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        if (msg.what == 1){
                            tv.setText(new DecimalFormat("00").format(hours)+":"+new DecimalFormat("00").format(minutes)
                                    +":"+new DecimalFormat("00").format(seconds));
                        }
                    }
                };
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0;;i++){
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        seconds++;
                        Message msg1 = new Message();
                        msg1.what = 1;
                        handler1.sendMessage(msg1);
                        if (seconds == 60){
                            seconds = 0;
                            minutes++;
                            if (minutes == 60){
                                minutes = 0;
                                hours++;
                            }
                        }
                    }
                }
            }).start();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true){
                        double currentTime = System.currentTimeMillis();
                        double totalWifi01 = getWifiTraffic(currentTime);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        double frontTime = System.currentTimeMillis();
                        double totalWifi02 = getWifiTraffic(frontTime);
                        double errorTraffic = totalWifi02 - totalWifi01;
                        if (errorTraffic<512){
                            errorTraffic = 1;
                        }
                        currentWFSpeed = errorTraffic/1111500;
                        currentWifiTraffic = df.format(currentWFSpeed);
                        totalWF += currentWFSpeed;
                        totalWifiTraffic = df.format(totalWF);
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                    }
                }
            }).start();
        }
    }

    private double getWifiTraffic(double currentTime) {
        double rtotalGprs = TrafficStats.getTotalRxBytes();
        double ttotalGprs = TrafficStats.getTotalTxBytes();
        double rGprs = TrafficStats.getMobileRxBytes();
        double tGprs = TrafficStats.getMobileTxBytes();
        double rWifi = rtotalGprs - rGprs;
        double tWifi = ttotalGprs - tGprs;
        totalWifi = rWifi + tWifi;
        return totalWifi;
    }

    private void initView() {
        textView = findViewById(R.id.showTraffic);
        showTotalTraffic = findViewById(R.id.showTotalTraffic);
        tv = findViewById(R.id.showTime);
        webView = findViewById(R.id.webView);
    }
}
