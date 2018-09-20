package com.wecent.weixun.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wecent.weixun.R;
import com.wecent.weixun.model.entity.NewsDetail;
import com.wecent.weixun.utils.DateUtils;
import com.wecent.weixun.utils.ImageLoaderUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author ChayChan
 * @description: 新闻详情页头部
 * @date 2017/6/28  15:25
 */

public class NewsDetailHeaderView extends FrameLayout {

    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.tv_author)
    TextView tvAuthor;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.ll_info)
    public LinearLayout llInfo;
    @BindView(R.id.wv_content)
    WebView wvContent;

    private Context mContext;

    public NewsDetailHeaderView(Context context) {
        this(context, null);
    }

    public NewsDetailHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NewsDetailHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.layout_weixun_detail_header, this);
        ButterKnife.bind(this, this);
    }

    public void setDetail(NewsDetail detail, LoadWebListener listener) {
        mWebListener = listener;

        tvTitle.setText(detail.title);

        if (detail.media_user == null) {
            //如果没有用户信息
            llInfo.setVisibility(GONE);
        } else {
            if (!TextUtils.isEmpty(detail.media_user.avatar_url)) {
                ImageLoaderUtil.LoadImage(mContext, detail.media_user.avatar_url, ivAvatar);
            }
            tvAuthor.setText(detail.media_user.screen_name);
            tvTime.setText(DateUtils.getShortTime(detail.publish_time * 1000L));
        }

        if (TextUtils.isEmpty(detail.content))
            wvContent.setVisibility(GONE);

        wvContent.getSettings().setJavaScriptEnabled(true);//设置JS可用
//        wvContent.addJavascriptInterface(new ShowPicRelation(mContext), NICK);

        String htmlPart1 = "<!DOCTYPE HTML html>\n" +
                "<head><meta charset=\"utf-8\"/>\n" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, minimum-scale=1.0, user-scalable=no\"/>\n" +
                "</head>\n" +
                "<body>\n" +
                "<style> \n" +
                "img{width:100%!important;height:auto!important}\n" +
                " </style>";
        String htmlPart2 = "</body></html>";

        String html = htmlPart1 + detail.content + htmlPart2;

        wvContent.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
        wvContent.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                addJs(view);//添加多JS代码，为图片绑定点击函数
                if (mWebListener != null) {
                    mWebListener.onLoadFinished();
                }
            }
        });
    }

    /**
     * 添加JS代码，获取所有图片的链接以及为图片设置点击事件
     */
    private void addJs(WebView wv) {
        wv.loadUrl("javascript:(function  pic(){" +
                "var imgList = \"\";" +
                "var imgs = document.getElementsByTagName(\"img\");" +
                "for(var i=0;i<imgs.length;i++){" +
                "var img = imgs[i];" +
                "imgList = imgList + img.src +\";\";" +
                "img.onclick = function(){" +
                "window.chaychan.openImg(this.src);" +
                "}" +
                "}" +
                "window.chaychan.getImgArray(imgList);" +
                "})()");
    }

    private LoadWebListener mWebListener;

    /**
     * 页面加载的回调
     */
    public interface LoadWebListener {
        void onLoadFinished();
    }
}
