package com.wecent.weixun.loader.tranform;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.wecent.weixun.utils.SizeUtils;

import java.nio.charset.Charset;
import java.security.MessageDigest;

/**
 * desc:
 * author: wecent
 * date: 2018/9/27
 */
public class GlideCircleTransformation extends BitmapTransformation {
    private static final String ID = "com.bumptech.glide.transformations.GlideRoundTransform";
    private static final byte[] ID_BYTES = ID.getBytes(Charset.forName("UTF-8"));
    private Paint mBorderPaint;
    private float mBorderWidth;

    public GlideCircleTransformation() {
    }

    public GlideCircleTransformation(int borderWidth, int borderColor) {
        super();
        mBorderWidth = SizeUtils.dp2px(borderWidth);
        mBorderPaint = new Paint();
        mBorderPaint.setDither(true);
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(borderColor);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setStrokeWidth(mBorderWidth);
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return circleCrop(pool, toTransform);
    }

    private Bitmap circleCrop(BitmapPool pool, Bitmap source) {
        if (source == null) return null;

        int size = Math.min(source.getWidth(), source.getHeight());
        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;

        Bitmap square = Bitmap.createBitmap(source, x, y, size, size);
        Bitmap circle = pool.get(size, size, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(circle);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(square, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        float r = size / 2f;
        canvas.drawCircle(r, r, r, paint);
        if (mBorderPaint != null) {
            float borderRadius = r - mBorderWidth / 2;
            canvas.drawCircle(r, r, borderRadius, mBorderPaint);
        }
        return circle;
    }

    @Override
    public int hashCode() {
        return ID.hashCode();
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return obj instanceof GlideCircleTransformation;
    }
}