package com.wecent.weixun.loader.tranform;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.wecent.weixun.utils.SizeUtils;

import java.nio.charset.Charset;
import java.security.MessageDigest;

/**
 * desc:
 * author: wecent
 * date: 2018/9/27
 */
public class RoundBitmapTranformation extends BitmapTransformation {
    private int radius;
    private static final String ID = "com.bumptech.glide.transformations.FillSpace";
    private static final byte[] ID_BYTES = ID.getBytes(Charset.forName("UTF-8"));

    public RoundBitmapTranformation(int radius) {
        this.radius = SizeUtils.dp2px(radius);
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        return roundCrop(pool, toTransform);
    }

    /**
     * Glide 使用 LruBitmapPool 作为默认的 BitmapPool 。
     * LruBitmapPool 是一个内存中的固定大小的 BitmapPool，
     * 使用 LRU 算法清理。默认大小基于设备的分辨率和密度，
     * 同时也考虑内存类和 isLowRamDevice 的返回值。
     * 具体的计算通过 Glide 的 MemorySizeCalculator 来完成，
     * 与 Glide 的 MemoryCache 的大小检测方法相似。
     *
     * @param pool
     * @param source
     * @return
     */
    private Bitmap roundCrop(BitmapPool pool, Bitmap source) {
        if (source == null) return null;

        Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());
        canvas.drawRoundRect(rectF, radius, radius, paint);
        return result;
    }

    /**
     * 占位图圆角
     */
    public static RequestBuilder<Drawable> loadTransform(Context context, @DrawableRes int placeholderId, int radius) {
        RequestOptions myOptions = new RequestOptions()
                .transforms(new CenterCrop(), new RoundBitmapTranformation(SizeUtils.dp2px(radius)))
                .placeholder(placeholderId)
                .error(placeholderId);
        return Glide.with(context)
                .load(placeholderId)
                .apply(myOptions);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof RoundBitmapTranformation;
    }

    @Override
    public int hashCode() {
        return ID.hashCode();
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);
    }
}
