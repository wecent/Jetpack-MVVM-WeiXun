package com.wecent.weixun.widget;

import android.os.Handler;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.wecent.weixun.WXApplication;
import com.wecent.weixun.model.response.VideoPathResponse;
import com.wecent.weixun.network.BaseObserver;
import com.wecent.weixun.network.RxSchedulers;
import com.wecent.weixun.network.WeiXunApiManager;

import java.util.List;

/**
 * Created by Administrator on 2018/2/9 0009.
 */

public abstract class VideoPathDecoder {

    WeiXunApiManager mWeiXunApi;

    private static final String NICK = "chaychan";

    public void decodePath(final String srcUrl) {
        final WebView webView = new WebView(WXApplication.getContext());

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);//设置JS可用
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        ParseRelation relation = new ParseRelation(new IGetParamsListener() {
            @Override
            public void onGetParams(String r, String s) {
                sendRequest(srcUrl,r,s);
            }

            @Override
            public void onGetPath(String path) {
                onDecodeSuccess(path);
            }
        });

        webView.addJavascriptInterface(relation, NICK);//绑定JS和Java的联系类，以及使用到的昵称

//        webView.loadUrl("file:///android_asset/parse.html");
        webView.loadUrl(srcUrl);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
//                webView.loadUrl("javascript:getParseParam('" + srcUrl + "')");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        addJs(webView);
                    }
                },5000);

            }
        });
    }

    private void addJs(WebView webView){
        webView.loadUrl("javascript:(function  getVideoPath(){" +
                "var videos = document.getElementsByTagName(\"video\");" +
                "var path = videos[0].src;" +
                "window.chaychan.onGetPath(path);" +
                "})()");

    }

    private void sendRequest(final String srcUrl, String r, String s) {
        mWeiXunApi = WXApplication.getInstance().getApplicationComponent().getWeiXunApi();
        mWeiXunApi.getVideoDetail(srcUrl, r, s)
                .compose(RxSchedulers.<VideoPathResponse>applySchedulers())
                .subscribe(new BaseObserver<VideoPathResponse>() {
                    @Override
                    public void onSuccess(VideoPathResponse response) {
                        Logger.e(new Gson().toJson(response));
                        String url = "";
                        switch (response.retCode){
                            case 200:
                                //请求成功
                                List<VideoPathResponse.DataBean.VideoBean.DownloadBean> downloadList = response.data.video.download;
                                if (downloadList != null && downloadList.size() != 0){
                                    url = downloadList.get(downloadList.size() -1).url;//获取下载地址中最后一个地址，即超清
                                    Logger.e("videoUrl: ",url);
                                }
                                onDecodeSuccess(url);
                                break;
                            case 400:
                                decodePath(srcUrl);//如果请求失败，可能是生成的r s请求参数不正确，重新再调用
                                break;
                        }
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        Logger.e(e.getLocalizedMessage());
                        onDecodeFailure();
                    }
                });
    }

    public abstract void onDecodeSuccess(String url);

    public abstract void onDecodeFailure();

    private class ParseRelation {

        IGetParamsListener mListener;

        public ParseRelation(IGetParamsListener listener){
            mListener = listener;
        }

        @JavascriptInterface
        public void onReceiveParams(String r,String s) {
            mListener.onGetParams(r,s);
        }

        @JavascriptInterface
        public void onGetPath(String path){
            mListener.onGetPath(path);
        }
    }

    public interface IGetParamsListener {
        void onGetParams(String r, String s);
        void onGetPath(String path);
    }

}
