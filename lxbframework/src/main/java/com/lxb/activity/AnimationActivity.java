package com.lxb.activity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.nineoldandroids.view.ViewHelper;

/**
 * Created by Administrator on 2016/3/7.
 */
public class AnimationActivity extends BaseActivity {
    // 动画时间
    private int AnimationDuration = 1000;
    // 正在执行的动画数量
    private int number = 0;
    // 是否完成清理
    public static final int CLEAN_ANIMATION = 113;//
    private boolean isAnimationClean = false;
    private FrameLayout animation_viewGroup;
    private View ani_str_view;//动画起始view
    private View ani_end_view;//动画目的view
    //
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            dismissWaitDialog();
            switch (msg.what) {
                case CLEAN_ANIMATION:
                    try {
                        animation_viewGroup.removeAllViews();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    isAnimationClean = false;
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        animation_viewGroup = createAnimLayout();
//        ani_end_view=findViewById(R.id.ani_end_view);
//        ani_str_view=findViewById(R.id.ani_str_view);
        int[] start_location = new int[2];
        int[] end_location = new int[2];
        ani_str_view.getLocationInWindow(start_location);
        ani_end_view.getLocationInWindow(end_location);
        //调用动画
        doAnim(start_location, end_location, null);
    }

    // =动画
    private void doAnim(int[] start_location, int[] end_location, Drawable drawable) {
        if (!isAnimationClean) {
            try {
                if (start_location != null && end_location != null && drawable != null) {
                    setAnim(start_location, end_location, drawable);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                animation_viewGroup.removeAllViews();
                isAnimationClean = false;
                setAnim(start_location, end_location, drawable);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                isAnimationClean = true;
            }
        }
    }

    /**
     * @param
     * @return void
     * @throws
     * @Description: 创建动画层
     */
    private FrameLayout createAnimLayout() {
        ViewGroup rootView = (ViewGroup) this.getWindow().getDecorView();
        FrameLayout animLayout = new FrameLayout(this);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        animLayout.setLayoutParams(lp);
        animLayout.setBackgroundResource(android.R.color.transparent);
        rootView.addView(animLayout);
        return animLayout;

    }

    /**
     * @param vg       动画运行的层 这里是frameLayout
     * @param view     要运行动画的View
     * @param location 动画的起始位置
     * @return
     * @deprecated 将要执行动画的view 添加到动画层
     */
    private View addViewToAnimLayout(ViewGroup vg, View view, int[] location) {
        int x = location[0];
        int y = location[1];
        vg.addView(view);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(dip2px(this,
                90), dip2px(this, 90));
        lp.leftMargin = x;
        lp.topMargin = y;
        view.setPadding(5, 5, 5, 5);
        view.setLayoutParams(lp);

        return view;
    }

    /**
     * dip，dp转化成px 用来处理不同分辨路的屏幕
     *
     * @param context
     * @param dpValue
     * @return
     */
    private int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 动画效果设置
     *
     * @param drawable 将要加入购物车的商品
     */
    private void setAnim(int[] start_location, int[] end_location, Drawable drawable) {

        Animation mScaleAnimation = new ScaleAnimation(1.5f, 0.0f, 1.5f, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.1f, Animation.RELATIVE_TO_SELF,
                0.1f);
        mScaleAnimation.setDuration(AnimationDuration);
        mScaleAnimation.setFillAfter(true);
        final ImageView iview = new ImageView(this);
        iview.setImageDrawable(drawable);
        final View view = addViewToAnimLayout(animation_viewGroup, iview,
                start_location);
//        view.setAlpha(0.6f);
        ViewHelper.setAlpha(view, 0.6f);
        int endX = end_location[0] - start_location[0];
        int endY = end_location[1] - start_location[1];

        Animation mTranslateAnimation = new TranslateAnimation(0, endX, 0, endY);
        Animation mRotateAnimation = new RotateAnimation(0, 180,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);// 旋转动画
        mRotateAnimation.setDuration(AnimationDuration);
        mTranslateAnimation.setDuration(AnimationDuration);
        AnimationSet mAnimationSet = new AnimationSet(true);

        mAnimationSet.setFillAfter(true);
        mAnimationSet.addAnimation(mRotateAnimation);
        mAnimationSet.addAnimation(mScaleAnimation);
        mAnimationSet.addAnimation(mTranslateAnimation);

        mAnimationSet.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub
                number++;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // TODO Auto-generated method stub

                number--;
                if (number == 0) {
                    isAnimationClean = true;
                    handler.sendEmptyMessage(CLEAN_ANIMATION);
                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

        });
        view.startAnimation(mAnimationSet);

    }

    /**
     * 内存过低时及时处理动画产生的未处理冗余
     */
    @Override
    public void onLowMemory() {
        // TODO Auto-generated method stub
        isAnimationClean = true;
        try {
            animation_viewGroup.removeAllViews();
        } catch (Exception e) {
            e.printStackTrace();
        }
        isAnimationClean = false;
        super.onLowMemory();
    }
}
