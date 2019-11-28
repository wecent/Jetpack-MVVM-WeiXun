package com.wecent.weixun.ui.mine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.wecent.weixun.R;
import com.wecent.weixun.component.ApplicationComponent;
import com.wecent.weixun.ui.base.BaseActivity;
import com.wecent.weixun.widget.expand.StructureTextView;
import com.wecent.weixun.widget.expand.ExpandableTextView;

import butterknife.BindView;

/**
 * desc: ExpendActivity
 * author: wecent
 * date: 2019/11/25
 */
public class ExpandActivity extends BaseActivity {

    @BindView(R.id.tv_expand_number)
    ExpandableTextView tvExpandNumber;
    @BindView(R.id.tv_expand_letter)
    ExpandableTextView tvExpandLetter;
    @BindView(R.id.tv_normal_number)
    StructureTextView tvNormalNumber;
    @BindView(R.id.tv_normal_letter)
    AppCompatTextView tvNormalLetter;

    public static void launch(Activity context) {
        Intent intent = new Intent(context, ExpandActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_expend;
    }

    @Override
    public void initInjector(ApplicationComponent appComponent) {

    }

    @Override
    public void bindView(View view, Bundle savedInstanceState) {

    }

    @Override
    public void bindData() {
        tvExpandNumber.setText("哈哈哈523894758地方忽略上島咖啡撒旦教放假啊聖誕快樂房間阿453卡麗是否加阿卡麗5353的佛腳開拉時" +
                "哈哈哈523894758地方忽略上島咖啡撒旦教放假啊聖誕快樂房間阿453卡麗是否加阿卡麗5353的佛腳開拉時" +
                "哈哈哈523894758地方忽略上島咖啡撒旦教放假啊聖誕快樂房間阿453卡麗是否加阿卡麗5353的佛腳開拉時" +
                "哈哈哈523894758地方忽略上島咖啡撒旦教放假啊聖誕快樂房間阿453卡麗是否加阿卡麗5353的佛腳開拉時");
        tvExpandLetter.setText("dafgjlsdkjfskladfjklasdjfklsdjfklsdjfklasdjfklsdajfklsdjfksdjkfjklsd" +
                "dafgjlsdkjfskladfjklasdjfklsdjfklsdjdfadfasdfasdfasdfklasdjfklsdajfklsdjfksdjfjklsd" +
                "dafgjlsdkjfskladfjklasdjfklsdjdfsdafasfasfasffafklsdjfklasdjfklsdajfklsdjfkdjkjklsd" +
                "dafgjlsdkjfskladfjklasdjfklsdafasdfsafasfjfklsdjfklasdjfklsdajfklsdjfksdjkfjklsd");
        tvNormalNumber.setText("52389475834758237458073487583475872358723489574837583924578437589237" +
                "58927348572348907582347589234572348572389572893475578247589237589273458573892457239" +
                "457892347582347583475834572348573485723489758289577777754645645646456fsd46464677777" +
                "70000000000000000000000000000000000000000000000000000000000000000000000000000000收取");
        tvNormalLetter.setText("dafgjlsdkjfskladfjklasdjfklsdjfklsdjfklasdjfklsdajfklsdjfksdjkfjklsd" +
                "dafgjlsdkjfskladfjklasdjfklsdjfklsdjdfadfasdfasdfasdfklasdjfklsdajfklsdjfksdjfjklsd" +
                "dafgjlsdkjfskladfjklasdjfklsdjdfsdafasfasfasffafklsdjfklasdjfklsdajfklsdjfkdjkjklsd" +
                "dafgjlsdkjfskladfjklasdjfklsdafasdfsafasfjfklsdjfklasdjfklsdajfklsdjfksdjkfjklsd收取");
    }

    @Override
    public void onReload() {

    }
}
