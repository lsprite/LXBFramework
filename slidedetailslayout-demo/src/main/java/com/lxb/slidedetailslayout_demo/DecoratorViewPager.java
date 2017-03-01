package com.lxb.slidedetailslayout_demo;

/**
 * Created by Administrator on 2017/3/1.
 */

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * viewpage 和listview 相互冲突 将父view 传递到viewpage 里面
 * <p>
 * 使用父类的方法 parent.requestDisallowInterceptTouchEvent(true);
 * <p>
 * 当 requestDisallowInterceptTouchEvent 如果为true的时候 表示:父view 不拦截子view的touch 事件
 * <p>
 * 这个方法只是改变flag
 *
 * @author baozi
 */
public class DecoratorViewPager extends ViewPager {
    private static final String TAG = "xujun";

    int lastX = -1;
    int lastY = -1;

    public DecoratorViewPager(Context context) {
        super(context);
    }

    public DecoratorViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int x = (int) ev.getRawX();
        int y = (int) ev.getRawY();
        int dealtX = 0;
        int dealtY = 0;

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                dealtX = 0;
                dealtY = 0;
                // 保证子View能够接收到Action_move事件
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                dealtX += Math.abs(x - lastX);
                dealtY += Math.abs(y - lastY);
                // 这里是够拦截的判断依据是左右滑动，读者可根据自己的逻辑进行是否拦截
                if (dealtX >= dealtY) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                } else {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                lastX = x;
                lastY = y;
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
            case MotionEvent.ACTION_UP:
                break;

        }
        return super.dispatchTouchEvent(ev);
    }

}