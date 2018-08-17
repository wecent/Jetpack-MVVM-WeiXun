package com.wecent.weixun.component;

import com.wecent.weixun.ui.jandan.JdDetailFragment;
import com.wecent.weixun.ui.news.ArticleReadActivity;
import com.wecent.weixun.ui.news.ImageBrowseActivity;
import com.wecent.weixun.ui.news.NewsFragment;
import com.wecent.weixun.ui.video.DetailFragment;
import com.wecent.weixun.ui.video.VideoFragment;

import dagger.Component;

/**
 * desc: .
 * author: Will .
 * date: 2017/9/2 .
 */
@Component(dependencies = ApplicationComponent.class)
public interface HttpComponent {

    void inject(VideoFragment videoFragment);

    void inject(DetailFragment detailFragment);

    void inject(JdDetailFragment jdDetailFragment);

    void inject(ImageBrowseActivity imageBrowseActivity);

    void inject( com.wecent.weixun.ui.news.DetailFragment detailFragment);

    void inject(ArticleReadActivity articleReadActivity);

    void inject(NewsFragment newsFragment);

}
