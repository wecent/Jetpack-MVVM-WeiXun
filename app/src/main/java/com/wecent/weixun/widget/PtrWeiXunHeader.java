package com.wecent.weixun.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import com.wecent.weixun.R;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;

public class PtrWeiXunHeader extends View implements PtrUIHandler {
    /** 状态-闲置 */
    public static final byte STATE_IDLE = 0;
    /** 状态-下拉去刷新 */
    public static final byte STATE_PULL_TO_REFRESH = 1;
    /** 状态-释放去刷新 */
    public static final byte STATE_RELEASE_TO_REFRESH = 2;
    /** 状态-刷新中 */
    public static final byte STATE_REFRESHING = 3;
    /** 状态-刷新完成 */
    public static final byte STATE_COMPLETED = 4;
    /** 当前状态 */
    private byte mState = STATE_IDLE;

    private static final float STEP_IDLE = 0f;
    /** 第一段和第二段分割界线 */
    private static final float STEP_HALF_OF_PULL = 0.7f;
    /** 下拉刷新和释放刷新分割界线 */
    private static final float STEP_RELEASE_TO_REFRESH = 1.0f;

    private final AccelerateDecelerateInterpolator adi = new AccelerateDecelerateInterpolator();
    private final OvershootInterpolator oi = new OvershootInterpolator();
    private final RectF ovalBlueBack = new RectF();
    private final RectF ovalLoading = new RectF();
    private final Paint paint = new Paint();
    private Bitmap bmpSuccess, bmpFailure;

    private int mWidth;
    private int mHeight;
    private int centerX;
    private int centerY;
    private float mLoadingStrokeWidth;
    private int radiusBlue;
    private int radiusWhite;

    private float mAnimPercent;
    private ValueAnimator valueAnimator;

    private boolean refreshResult = false;
    private float currPercent;

    public PtrWeiXunHeader(Context context) {
        this(context, null);
    }

    public PtrWeiXunHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final float scale = getResources().getDisplayMetrics().density;
        int heightSize = MeasureSpec.makeMeasureSpec((int) (80 * scale + 0.5f), MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightSize);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        centerX = mWidth / 2;
        centerY = mHeight / 2;

        radiusBlue = mHeight / 4;
        radiusWhite = radiusBlue / 2;
        mLoadingStrokeWidth = radiusBlue / 8;

        ovalBlueBack.left = centerX - radiusBlue;
        ovalBlueBack.right = centerX + radiusBlue;
        ovalBlueBack.top = centerY - radiusBlue;
        ovalBlueBack.bottom = centerY + radiusBlue;

        ovalLoading.left = centerX - radiusWhite;
        ovalLoading.right = centerX + radiusWhite;
        ovalLoading.top = centerY - radiusWhite;
        ovalLoading.bottom = centerY + radiusWhite;

        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.outWidth = radiusBlue;
        opt.outHeight = radiusBlue;
        // R路径需要重新导
        bmpSuccess = BitmapFactory.decodeResource(getResources(), R.drawable.img_anim_loading, opt);
        bmpFailure = BitmapFactory.decodeResource(getResources(), R.drawable.img_anim_loading, opt);
    }

    private Bitmap getRefreshResultBitmap() {
        return refreshResult ? bmpSuccess : bmpFailure;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(0xFFFFFFFF);
        paint.setAntiAlias(true);

        switch (mState) {
            case STATE_REFRESHING:
                paint.setColor(0xFF00A8E1);
                paint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(centerX, centerY, radiusBlue, paint);

                paint.setColor(0xFFFFFFFF);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(mLoadingStrokeWidth);
                final int s = (int) mAnimPercent;
                final float mp;
                switch (s) {
                    case 0:
                        mp = mAnimPercent;
                        canvas.drawArc(ovalLoading, 90 + (360 * mp), 30 + 240 * (1 - mp), false, paint);
                        break;
                    case 1:
                        mp = mAnimPercent - 1;
                        canvas.drawArc(ovalLoading, 90 + (180 * mp), 30 + 240 * mp, false, paint);
                        break;
                    case 2:
                        mp = mAnimPercent - 2;
                        canvas.drawArc(ovalLoading, -90 + (360 * mp), 30 + 240 * (1 - mp), false, paint);
                        break;
                    case 3:
                        mp = mAnimPercent - 3;
                        canvas.drawArc(ovalLoading, -90 + (180 * mp), 30 + 240 * mp, false, paint);
                        break;
                }
                break;
            case STATE_COMPLETED:
                paint.setColor(0xFF00A8E1);
                paint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(centerX, centerY, radiusBlue, paint);

                canvas.save();
                canvas.scale(mAnimPercent, mAnimPercent, centerX, centerY);
                final Bitmap bmpResult = getRefreshResultBitmap();
                canvas.drawBitmap(bmpResult, centerX - bmpResult.getWidth() / 2, centerY - bmpResult.getHeight() / 2, paint);
                canvas.restore();
                break;
            case STATE_RELEASE_TO_REFRESH:
                paint.setColor(0xFF00A8E1);
                paint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(centerX, centerY, radiusBlue, paint);

                paint.setColor(0xFFFFFFFF);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(mLoadingStrokeWidth);
                canvas.drawArc(ovalLoading, 90, 270, false, paint);
                break;
            case STATE_PULL_TO_REFRESH:
            default:
                final float p = (currPercent - STEP_IDLE) / (STEP_RELEASE_TO_REFRESH - STEP_IDLE);
                canvas.save();
                canvas.translate(0, (radiusBlue * 1.8f) * (1 - p));
                if (currPercent < STEP_HALF_OF_PULL) {
                    final float p1 = (currPercent - STEP_IDLE) / (STEP_HALF_OF_PULL - STEP_IDLE);
                    paint.setColor(Color.argb((int) (255 * p1), 90, 177, 232));
                    paint.setStyle(Paint.Style.FILL);
                    canvas.drawArc(ovalBlueBack, -90, 360 * p1, true, paint);
                } else {
                    final float p2 = (currPercent - STEP_HALF_OF_PULL) / (STEP_RELEASE_TO_REFRESH - STEP_HALF_OF_PULL);
                    paint.setColor(0xFF00A8E1);
                    paint.setStyle(Paint.Style.FILL);
                    canvas.drawCircle(centerX, centerY, radiusBlue, paint);

                    paint.setColor(Color.argb((int) (255 * p2), 255, 255, 255));
                    paint.setStyle(Paint.Style.STROKE);
                    paint.setStrokeWidth(mLoadingStrokeWidth);
                    canvas.drawArc(ovalLoading, -90 + 180 * p2, 270, false, paint);
                }
                canvas.restore();
                break;
        }
    }

    private void doRefreshingAnim() {
        if (valueAnimator != null) {
            valueAnimator.cancel();
            valueAnimator = null;
        }

        valueAnimator = ValueAnimator.ofFloat(0f, 1f, 2f, 3f, 4f);
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setDuration(2000);
        valueAnimator.setInterpolator(adi);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimPercent = (Float) animation.getAnimatedValue();
                invalidate();
            }
        });

        mState = STATE_REFRESHING;
        valueAnimator.start();
    }

    private void doCompleteAnim() {
        if (valueAnimator != null) {
            valueAnimator.cancel();
            valueAnimator = null;
        }

        valueAnimator = ValueAnimator.ofFloat(0f, 1f);
        valueAnimator.setDuration(400);
        valueAnimator.setInterpolator(oi);

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimPercent = (Float) animation.getAnimatedValue();
                invalidate();
            }
        });

        mState = STATE_COMPLETED;
        valueAnimator.start();
    }

    @Override
    public void onUIReset(PtrFrameLayout ptrFrameLayout) {
        mState = STATE_IDLE;
    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout ptrFrameLayout) {
    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout ptrFrameLayout) {
        doRefreshingAnim();
    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout ptrFrameLayout) {
    }

    @Override
    public void onUIPositionChange(PtrFrameLayout ptrFrameLayout, boolean b, byte b1, PtrIndicator ptrIndicator) {
        currPercent = ptrIndicator.getCurrentPercent();

        if (mState != STATE_REFRESHING && mState != STATE_COMPLETED) {
            mState = currPercent < STEP_RELEASE_TO_REFRESH ? STATE_PULL_TO_REFRESH : STATE_RELEASE_TO_REFRESH;
            invalidate();
        }
    }

    public void refreshComplete(boolean refreshResult, final PtrFrameLayout frame) {
        this.refreshResult = refreshResult;
        doCompleteAnim();
        postDelayed(new Runnable() {
            @Override
            public void run() {
                frame.refreshComplete();
            }
        }, 1000);
    }
}