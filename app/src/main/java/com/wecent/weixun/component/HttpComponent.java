package com.wecent.weixun.component;

import com.wecent.weixun.ui.jandan.DetailFragment;
import com.wecent.weixun.ui.news.NewsArticleActivity;
import com.wecent.weixun.ui.news.NewsImageActivity;
import com.wecent.weixun.ui.news.NewsFragment;
import com.wecent.weixun.ui.news.WeiXunFragment;
import com.wecent.weixun.ui.video.VideoFragment;

import dagger.Component;

/**
 * desc: .
 * author: wecent .
 * date: 2017/9/2 .
 */
@Component(dependencies = ApplicationComponent.class)
public interface HttpComponent {

    void inject(VideoFragment videoFragment);

    void inject(com.wecent.weixun.ui.video.DetailFragment detailFragment);

    void inject(DetailFragment detailFragment);

    void inject(NewsImageActivity newsImageActivity);

    void inject(com.wecent.weixun.ui.news.DetailFragment detailFragment);

    void inject(NewsArticleActivity newsArticleActivity);

    void inject(NewsFragment newsFragment);

    void inject(WeiXunFragment weixunFragment);

}
