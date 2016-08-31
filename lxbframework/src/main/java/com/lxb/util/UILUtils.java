package com.lxb.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public final class UILUtils {

    private static DisplayImageOptions options;

    public static void displayImage(String imgurl,
                                    ImageView imageView) {
        try {
            initOptions();
            ImageLoader.getInstance()
                    .displayImage(imgurl, imageView, options, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void displayLocalImage(String imgurl,
                                         ImageView imageView) {
        try {
            initOptions();
            ImageLoader.getInstance().displayImage("file://" + imgurl, imageView,
                    options, new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String s, View view) {
                        }

                        @Override
                        public void onLoadingFailed(String s, View view, FailReason failReason) {
                        }

                        @Override
                        public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                            LogUtil.log("bitmap大小", BitmapUtil.getBitmapSize(bitmap) + "");
                        }

                        @Override
                        public void onLoadingCancelled(String s, View view) {
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void displayLocalImageEqualWindows(final Context context,
                                                     String imgurl, ImageView imageView) {
        try {
            initOptions();
            ImageLoader.getInstance().displayImage("file://" + imgurl, imageView,
                    options, new ImageLoadingListener() {

                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                            // TODO Auto-generated method stub
//                        System.out.println("onLoadingStarted");
                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view,
                                                    FailReason failReason) {
                            // TODO Auto-generated method stub
//                        System.out.println("onLoadingFailed");
                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view,
                                                      Bitmap loadedImage) {
                            // TODO Auto-generated method stub
                            LogUtil.log("bitmap大小", BitmapUtil.getBitmapSize(loadedImage) + "");
//                        System.out.println("onLoadingComplete");
                            try {
                                int width = PhoneInfo
                                        .getDisplayWidthMetrics(context);
                                int height = loadedImage.getHeight() * width
                                        / loadedImage.getWidth();
                                LayoutParams layoutParams = view.getLayoutParams();
                                layoutParams.width = width;
                                layoutParams.height = height;
                                System.out.println("图片" + imageUri + "的正式长宽:"
                                        + width + "," + height);
                                view.setLayoutParams(layoutParams);
                            } catch (Exception e) {
                                // TODO: handle exception
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {
                            // TODO Auto-generated method stub
//                        System.out.println("onLoadingComplete");
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //考虑本地图片可能很大,类似选本地图片时再进行压缩
//    public static void displayLocalImageEqualWindowsCompress(final Context context,
//                                                             String imgurl, ImageView imageView) {
//        try {
//            int width = Math.round(PhoneInfo
//                    .getDisplayWidthMetrics(context) * 0.6f);
//            int height = BitmapUtil.getLocalImgHeigh(imgurl) * width
//                    / BitmapUtil.getLocalImgWidth(imgurl);
//            LayoutParams layoutParams = imageView.getLayoutParams();
//            layoutParams.width = width;
//            layoutParams.height = height;
//            imageView.setLayoutParams(layoutParams);
//        } catch (Exception e) {
//            // TODO: handle exception
//            e.printStackTrace();
//        }
//        initOptions();
//        ImageLoader.getInstance().displayImage("file://" + imgurl, imageView,
//                options, new ImageLoadingListener() {
//
//                    @Override
//                    public void onLoadingStarted(String imageUri, View view) {
//                        // TODO Auto-generated method stub
////                        System.out.println("onLoadingStarted");
//                    }
//
//                    @Override
//                    public void onLoadingFailed(String imageUri, View view,
//                                                FailReason failReason) {
//                        // TODO Auto-generated method stub
////                        System.out.println("onLoadingFailed");
//                    }
//
//                    @Override
//                    public void onLoadingComplete(String imageUri, View view,
//                                                  Bitmap loadedImage) {
//                        // TODO Auto-generated method stub
//                        LogUtil.log("bitmap大小", BitmapUtil.getBitmapSize(loadedImage) + "");
////                        System.out.println("onLoadingComplete");
//                        try {
//                            int width = PhoneInfo
//                                    .getDisplayWidthMetrics(context);
//                            int height = loadedImage.getHeight() * width
//                                    / loadedImage.getWidth();
//                            LayoutParams layoutParams = view.getLayoutParams();
//                            layoutParams.width = width;
//                            layoutParams.height = height;
//                            System.out.println("图片" + imageUri + "的正式长宽:"
//                                    + width + "," + height);
//                            view.setLayoutParams(layoutParams);
//                        } catch (Exception e) {
//                            // TODO: handle exception
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onLoadingCancelled(String imageUri, View view) {
//                        // TODO Auto-generated method stub
////                        System.out.println("onLoadingComplete");
//                    }
//                });
//    }

    public static void displayImageEqualWindows(final Context context,
                                                String imgurl, ImageView imageView) {
        try {
            initOptions();
            ImageLoader.getInstance().displayImage(imgurl, imageView, options,
                    new ImageLoadingListener() {

                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                            // TODO Auto-generated method stub
//                        System.out.println("onLoadingStarted");
                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view,
                                                    FailReason failReason) {
                            // TODO Auto-generated method stub
//                        System.out.println("onLoadingFailed");
                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view,
                                                      Bitmap loadedImage) {
                            // TODO Auto-generated method stub
//                        System.out.println("onLoadingComplete");
                            try {
                                int width = PhoneInfo
                                        .getDisplayWidthMetrics(context);
                                int height = loadedImage.getHeight() * width
                                        / loadedImage.getWidth();
                                LayoutParams layoutParams = view.getLayoutParams();
                                layoutParams.width = width;
                                layoutParams.height = height;
                                System.out.println("图片" + imageUri + "的正式长宽:"
                                        + width + "," + height);
                                view.setLayoutParams(layoutParams);
                            } catch (Exception e) {
                                // TODO: handle exception
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {
                            // TODO Auto-generated method stub
//                        System.out.println("onLoadingComplete");
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void initOptions() {
        if (options == null) {
            options = new DisplayImageOptions.Builder().cacheInMemory(true)
                    .cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565) // 设置图片的解码类型
                    .delayBeforeLoading(100)
                    /**
                     * 设置图片以如何的编码方式显示 imageScaleType(ImageScaleType imageScaleType)
                     * EXACTLY :图像将完全按比例缩小的目标大小
                     * EXACTLY_STRETCHED:图片会缩放到目标大小完全
                     * IN_SAMPLE_INT:图像将被二次采样的整数倍
                     * IN_SAMPLE_POWER_OF_2:图片将降低2倍，直到下一减少步骤，使图像更小的目标大小
                     *  NONE:图片不会调整
                     */
                    .imageScaleType(ImageScaleType.EXACTLY)
                    .build();
        }
    }

    // private static void initOptionsWithDefault() {
    // if (optionsWithDefault == null) {
    // optionsWithDefault = new DisplayImageOptions.Builder()
    // .showStubImage(R.drawable.da)
    // .showImageForEmptyUri(R.drawable.loading_error)
    // .showImageOnFail(R.drawable.loading_error)
    // .cacheInMemory(true).cacheOnDisc(true)
    // .imageScaleType(ImageScaleType.EXACTLY)
    // .bitmapConfig(Bitmap.Config.RGB_565) // 设置图片的解码类型
    // .build();
    // }
    // }
}
