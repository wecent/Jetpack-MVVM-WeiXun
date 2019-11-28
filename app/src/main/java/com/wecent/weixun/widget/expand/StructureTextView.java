package com.wecent.weixun.widget.expand;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * desc: 解决TextView纯数字或纯字母排序，以及数字和文字、字母和文字混合导致的换行混乱问题
 *       有待优化：实现可展开和收起效果
 * author: wecent
 * date: 2019/11/25
 */
public class StructureTextView extends AppCompatTextView {

    //内容填充画笔
    private Paint contentPaint;
    //控件宽度
    private int contentWidth = 0;
    //控件高度
    private int contentHeight = 0;
    //文字测量工具
    private Paint.FontMetricsInt textFm;
    //文字显示区的宽度
    private int textWidth;
    //单行文字的高度
    private int singleLineHeight;

    public StructureTextView(Context context) {
        super(context);
        init();
    }

    public StructureTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StructureTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        contentPaint = getPaint();
        contentPaint.setTextSize(getTextSize());
        contentPaint.setAntiAlias(true);
        contentPaint.setStrokeWidth(2);
        contentPaint.setColor(getCurrentTextColor());
        contentPaint.setTextAlign(Paint.Align.LEFT);
        textFm = contentPaint.getFontMetricsInt();
        singleLineHeight = Math.abs(textFm.top - textFm.bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        contentWidth = getMeasuredWidth();
        textWidth = contentWidth - getPaddingLeft() - getPaddingRight();
        contentHeight = (int) getContentHeight();
        setMeasuredDimension(contentWidth, contentHeight);
    }

    private float getContentHeight() {
        char[] textChars = getText().toString().toCharArray();
        float ww = 0.0f;
        int count = 0;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < textChars.length; i++) {
            float v = contentPaint.measureText(textChars[i] + "");
            if (ww + v <= textWidth) {
                sb.append(textChars[i]);
                ww += v;
            } else {
                count++;
                sb = new StringBuilder();
                ww = 0.0f;
                ww += v;
                sb.append(textChars[i]);
            }
        }
        if (sb.toString().length() != 0) {
            count++;
        }
        return count * singleLineHeight + getLineSpacingExtra() * (count - 1) + getPaddingBottom() + getPaddingTop();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawText(canvas);
    }

    /**
     * 循环遍历画文字
     *
     * @param canvas
     */
    private void drawText(Canvas canvas) {

        char[] textChars = getText().toString().toCharArray();
        float ww = 0.0f;
        float startL = 0.0f;
        float startT = 0.0f;
        startL += getPaddingLeft();
        startT += getPaddingTop() + singleLineHeight / 2 + (textFm.bottom - textFm.top) / 2 - textFm.bottom;
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < textChars.length; i++) {
            float v = contentPaint.measureText(textChars[i] + "");
            if (ww + v <= textWidth) {
                sb.append(textChars[i]);
                ww += v;
            } else {
                canvas.drawText(sb.toString(), startL, startT, contentPaint);
                startT += singleLineHeight + getLineSpacingExtra();
                sb = new StringBuilder();
                ww = 0.0f;
                ww += v;
                sb.append(textChars[i]);
            }
        }

        if (sb.toString().length() > 0) {
            canvas.drawText(sb.toString(), startL, startT, contentPaint);
        }
    }
}
