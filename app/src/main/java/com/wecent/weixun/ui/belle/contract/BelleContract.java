package com.wecent.weixun.ui.belle.contract;

import com.wecent.weixun.model.entity.BelleEntity;
import com.wecent.weixun.ui.base.BaseContract;

/**
 * desc:
 * author: wecent
 * date: 2018/9/27
 */
public interface BelleContract {

    interface View extends BaseContract.BaseView {

        void loadDetailData(String type, BelleEntity belleEntity);

        void loadMoreDetailData(String type, BelleEntity belleEntity);

    }

    interface Presenter extends BaseContract.BasePresenter<View> {

        void getDetailData(String type, int page);

    }
}
