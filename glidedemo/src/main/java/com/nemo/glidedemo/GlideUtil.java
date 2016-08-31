package com.nemo.glidedemo;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

/**
 * Created by Administrator on 2016/4/20.
 */
public class GlideUtil {
    public static void displayImage(final Context context, final String imgurl,
                                    final ImageView imageView) {
        Glide.with(context)
                .load(imgurl)
                .placeholder(R.drawable.image_loading)
                .error(R.drawable.image_error)
                .crossFade()//淡入淡出
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        System.out.println("Drawable的长宽:"
                                + resource.getIntrinsicWidth() + "," + resource.getIntrinsicHeight());
                        return false;
                    }
                })
                .into(imageView);
    }

    public static void displayLocalImageEqualWindows(final Context context, final String imgurl,
                                                     final ImageView imageView) {
        Glide.with(context)
                .load(imgurl)
                .placeholder(R.drawable.image_loading)
                .error(R.drawable.image_error)
                .crossFade()//淡入淡出
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        try {
                            System.out.println("Drawable的长宽:"
                                    + resource.getIntrinsicWidth() + "," + resource.getIntrinsicHeight());
                            int width =
                                    getDisplayWidthMetrics(context);
                            int height = resource.getIntrinsicHeight() * width
                                    / resource.getIntrinsicWidth();
                            ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
                            layoutParams.width = width;
                            layoutParams.height = height;
                            imageView.setLayoutParams(layoutParams);
                            System.out.println("ImageView的长宽:"
                                    + width + "," + height);
                        } catch (Exception e) {
                            // TODO: handle exception
                            e.printStackTrace();
                        }
                        return false;
                    }
                })
                .into(imageView);
    }

    /**
     * 获取屏幕宽
     *
     * @param context
     * @return
     */
    private static int getDisplayWidthMetrics(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

}
