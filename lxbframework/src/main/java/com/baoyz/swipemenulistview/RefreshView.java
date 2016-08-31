package com.baoyz.swipemenulistview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Scroller;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * 弹性View<br />
 * 默认弹性关闭,调用setEnablePullDown和setEnablePullUp使能<br />
 * 配合ListView使用时,请调用setListViewScrollListener() 或者
 * 给ListView设置onScrollListener并且在OnScroll方法中调用onListViewScroll()
 *
 * @author planet2@qq.com
 */
public class RefreshView extends FrameLayout {
    static final String TAG = "RefreshView";
    private final Scroller mScroller;
    private final int mTouchSlop;
    private boolean mScolling = false;// 手指拖拽状态
    private boolean mMoving = false;// 松开手至，Scroller滑动状态
    private boolean pullDown = false;// 正在下拉中，反之代表正在上拉
    private Point mLastPoint = new Point(0, 0);

    private ListView listChild;
    // 这两个变量用来修复ListView仍然处于到顶部或者底部的状态, 但是由于slop判断失败导致禁止上拉或下拉的bug
    // 当slop不符合的时候,如果ListView处于顶部或者底部,不禁用下拉、上拉
    private boolean atListViewTop = false;
    private boolean atListViewBottom = false;

    private HeaderView header;
    private HeaderView footer;
    private TextView tooltip;
    private AlphaAnimation fadeIn;
    private AlphaAnimation fadeOut;

    private Animation mRotateUpAnimation;
    private Animation mRotateDownAnimation;
    private int minSnapDistance = 0;// 滑动多长距离可刷新
    private int headerheight = 0;

    private ArrayList<OnSnapListener> snapListeners = new ArrayList<OnSnapListener>();

    /**
     * 下拉上拉监听器
     */
    public interface OnSnapListener {
        /**
         * 下拉松开
         */
        public void onSnapToTop(int distance);

        /**
         * 上拉松开
         */
        public void onSnapToBottom(int distance);
    }

    private boolean enablePullDown = false;// 允许下拉
    private boolean enablePullUp = false;// 允许上拉

    public RefreshView(Context context) {
        super(context);
        mScroller = new Scroller(context);
        mTouchSlop = 5;
        init(context);
    }

    public RefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = 5;
        mScroller = new Scroller(context);
        init(context);
    }

    public RefreshView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        mTouchSlop = 5;
        mScroller = new Scroller(context);
        init(context);
    }

    @SuppressWarnings("deprecation")
    private void init(Context context) {
        headerheight = dip2px(50);
        // 这里设置下拉距离比header高度大一些
        minSnapDistance = (int) (headerheight * 1.3);

        // 添加header
        header = new HeaderView(getContext(), HeaderView.textPullToRefresh);
        LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT,
                headerheight);
        header.setOrientation(LinearLayout.HORIZONTAL);
        header.setGravity(Gravity.CENTER);
        addViewInLayout(header, -1, lp);
        // 添加footer
        footer = new HeaderView(getContext(), HeaderView.textPullToLoadMore);
        lp = new LayoutParams(LayoutParams.FILL_PARENT, headerheight);
        lp.gravity = Gravity.BOTTOM;
        footer.setOrientation(LinearLayout.HORIZONTAL);
        footer.setGravity(Gravity.CENTER);
        addViewInLayout(footer, -1, lp);
        // 刷新状态提示框
        int margin = dip2px(10);
        int padding = dip2px(6);
        tooltip = new TextView(context);
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(getResources().getColor(android.R.color.black));
        gd.setCornerRadius(8);
        gd.setAlpha(160);
        tooltip.setBackgroundDrawable(gd);
        tooltip.setPadding(padding, padding, padding, padding);
        tooltip.setVisibility(View.INVISIBLE);
        tooltip.setTextColor(getResources().getColor(android.R.color.white));
        lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        lp.setMargins(margin, margin, margin, margin);
        lp.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        addViewInLayout(tooltip, 0, lp);
        // 提示条渐变动画
        fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setDuration(600);
        fadeIn.setFillAfter(true);
        fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setDuration(600);
        fadeOut.setFillAfter(true);
        // 箭头翻转动画
        mRotateUpAnimation = new RotateAnimation(0, -180,
                RotateAnimation.RELATIVE_TO_SELF, .5f,
                RotateAnimation.RELATIVE_TO_SELF, .5f);
        mRotateUpAnimation.setDuration(250);
        mRotateUpAnimation.setFillAfter(true);
        mRotateUpAnimation.setInterpolator(new LinearInterpolator());

        mRotateDownAnimation = new RotateAnimation(-180, 0,
                RotateAnimation.RELATIVE_TO_SELF, .5f,
                RotateAnimation.RELATIVE_TO_SELF, .5f);
        mRotateDownAnimation.setDuration(250);
        mRotateDownAnimation.setFillAfter(true);
        mRotateDownAnimation.setInterpolator(new LinearInterpolator());
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        // header和footer显示到最前面
        header.bringToFront();
        footer.bringToFront();
        tooltip.bringToFront();
        // header和footer分别隐藏到上边和下边
        header.layout(header.getLeft(), header.getTop() - headerheight,
                header.getRight(), header.getBottom() - headerheight);
        footer.layout(footer.getLeft(), footer.getTop() + headerheight,
                footer.getRight(), footer.getBottom() + headerheight);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        float eventFloatY = ev.getY();
        if (enablePullDown || enablePullUp) {
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {// 记录第一次触摸Y
                mLastPoint.y = eventFloatY;
                mLastPoint.x = ev.getX();
                if (listChild != null) {
                    onListViewScroll(listChild);
                }
            }
            if (mMoving) {
                return true;
            }
            if (ev.getAction() == MotionEvent.ACTION_MOVE) {
                if (!mScolling) {
                    // 计算移动的Y和第一次触摸Y，大于touchSlop开始移动
                    // enablePullDown=true且slop>0时允许下拉
                    // enablePullUp=true且slop<0时允许上拉
                    float slop = eventFloatY - mLastPoint.y;
                    if (Math.abs(slop) >= mTouchSlop) {
                        // 判断斜度,如果接近横向滑动，不允许拉动
                        Point currentP = new Point(ev.getX(), ev.getY());
                        float grad = calculateGradient(mLastPoint, currentP);
                        if (grad < 1
                                && ((slop >= 0 && enablePullDown) || (slop <= 0 && enablePullUp))) { // 开始scroll
                            mScolling = true;
                            pullDown = slop >= 0;
                            ev.setAction(MotionEvent.ACTION_CANCEL);
                            super.dispatchTouchEvent(ev);
                        } else {// 如果slop正负与滑动方向不一致 或者在其他情况下 取消拉动监听
                            if (!atListViewTop)
                                setEnablePullDown(false);
                            if (!atListViewBottom)
                                setEnablePullUp(false);
                        }
                    }
                } else {
                    // 不允许倒着滚动
                    if (pullDown && getScrollY() <= 0 || !pullDown
                            && getScrollY() >= 0) {
                        // 根据初次触摸的Y和当前移动的Y的差, scroll相同的距离
                        float scrollY = eventFloatY - mLastPoint.y;
                        scrollTo(0, -(int) scrollY);
                        return true;
                    } else {
                        scrollTo(0, 0);
                        mScolling = false;
                        ev.setAction(MotionEvent.ACTION_DOWN);
                    }
                }
            } else if (ev.getAction() == MotionEvent.ACTION_CANCEL
                    || ev.getAction() == MotionEvent.ACTION_UP) {
                // 手指离开屏幕, 执行snapToTop或snapToBottom动画
                if (mScolling) {
                    mScolling = false;
                    mMoving = true;
                    mScroller.forceFinished(true);
                    mScroller.startScroll(0, getScrollY(), 0, -getScrollY(),
                            300);
                    invalidate();
                    return true;
                }
                // else{
                // refreshViewOnItemListener.OnItemListener();
                // }
            }
            return super.dispatchTouchEvent(ev);
        } else {
            return super.dispatchTouchEvent(ev);
        }
    }

    // 设置点击监听
    private RefreshViewOnItemListener refreshViewOnItemListener;

    public void setRefreshViewOnItemListener(
            RefreshViewOnItemListener refreshViewOnItemListener) {
        this.refreshViewOnItemListener = refreshViewOnItemListener;
    }

    interface RefreshViewOnItemListener {
        void OnItemListener();
    }

    public void addOnSnapListener(OnSnapListener listener) {
        snapListeners.add(listener);
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
        int distance = getScrollY();
        if (distance < 0) {
            if (-distance > minSnapDistance) {
                header.setReleaseToRefresh();
            } else {
                header.setPullToRefresh();
            }
        } else {
            if (distance > minSnapDistance) {
                footer.setReleaseToLoadMore();
            } else {
                footer.setPullToLoadMore();
            }
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            int currX = mScroller.getCurrX();
            int currY = mScroller.getCurrY();
            scrollTo(currX, currY);
            postInvalidate();
        }
        // 动画结束 判断是上拉松开 还是下拉松开 并通知OnSnapListner
        if (mScroller.isFinished()) {
            if (mMoving) {
                int distance = mScroller.getStartY();
                if (distance < 0 && -distance >= minSnapDistance) {
                    for (OnSnapListener l : snapListeners) {
                        l.onSnapToTop(-distance);
                        onSnapToTop();
                    }
                } else if (distance > 0 && distance >= minSnapDistance) {
                    for (OnSnapListener l : snapListeners) {
                        l.onSnapToBottom(distance);
                    }
                }
                mMoving = false;
            }
        }
    }

    private void onSnapToTop() {

    }

    public void showTooltip() {
        showTooltip("正在加载...", true);
    }

    public void showTooltipRefresh() {
        showTooltip("正在刷新...", true);
    }

    public void showTooltipLoadMore() {
        showTooltip("正在加载...", false);
    }

    public void showTooltip(String text, boolean top) {
        LayoutParams lp = (LayoutParams) tooltip.getLayoutParams();
        lp.gravity = (top ? Gravity.TOP : Gravity.BOTTOM)
                | Gravity.CENTER_HORIZONTAL;
        tooltip.setLayoutParams(lp);
        tooltip.setText(text);
        tooltip.clearAnimation();
        tooltip.startAnimation(fadeIn);
    }

    public void hideTooltip() {
        tooltip.clearAnimation();
        tooltip.startAnimation(fadeOut);
    }

    /**
     * 如果您的代码中没有用到ScrollListeer，可以调用此方法自动设置ScrollListener
     */
    public void setListViewScrollListener(final ListView listView) {
        listView.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView arg0, int arg1) {
            }

            @Override
            public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
                onListViewScroll(listView);
            }
        });
    }

    /**
     * 如果您已经设置了ListView的setOnScrollListener，请在onScroll中调用此方法 此方法用来设置下拉、上拉的使能状态:
     * 如果ListView滑动到顶部,使能PullDown,如果ListView滑动到底部使能PullUp
     */
    public void onListViewScroll(ListView listView) {
        if (listChild == null)
            listChild = listView;
        if (listView.getAdapter() == null
                || listView.getAdapter().getCount() == 0) {
            enablePullUp = true;
            atListViewBottom = true;
            enablePullDown = true;
            atListViewTop = true;
            return;
        }
        if (listView.getFirstVisiblePosition() == 0) {
            View top = listView.getChildAt(0);
            if (top != null && top.getTop() == 0) {
                enablePullDown = true;
                atListViewTop = true;
            } else {
                enablePullDown = false;
                atListViewTop = false;
            }
        } else {
            enablePullDown = false;
            atListViewTop = false;
        }

        if (listView.getAdapter() != null) {
            if (listView.getLastVisiblePosition() == listView.getAdapter()
                    .getCount() - 1) {
                View bottom = listView.getChildAt(listView.getChildCount() - 1);
                if (bottom != null) {
                    if (bottom.getBottom() <= listView.getBottom()) {
                        enablePullUp = true;
                        atListViewBottom = true;
                        return;
                    }
                }
            }
        }
        enablePullUp = false;
        atListViewBottom = false;
    }

    /**
     * 是否允许下拉 注意:允许下拉以后,Child不能获取下滑事件
     */
    public void setEnablePullDown(boolean enablePullDown) {
        this.enablePullDown = enablePullDown;
    }

    /**
     * 是否允许上拉 注意:允许上拉以后,Child不能获取上滑事件
     */
    public void setEnablePullUp(boolean enablePullUp) {
        this.enablePullUp = enablePullUp;
    }

    public class HeaderView extends LinearLayout {
        public static final String textReleaseToLoadMore = "松开立即加载...";
        public static final String textPullToLoadMore = "上拉加载更多...";
        public static final String textPullToRefresh = "下拉可以刷新...";
        public static final String textReleaseToRefresh = "松开立即刷新...";
        private boolean statePullToRefresh = true;
        private boolean statePullToLoadMore = true;
        private TextView text;
        private ImageView imageView;

        public HeaderView(Context context, String title) {
            super(context);
            imageView = new ImageView(getContext());
            if (title.equals(textPullToRefresh)) {
                imageView.setImageBitmap(getArrowBitmap(false));
            } else {
                imageView.setImageBitmap(getArrowBitmap(true));
            }
            LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            this.addViewInLayout(imageView, -1, lp);
            text = new TextView(context);
            text.setText(title);
            text.setGravity(Gravity.CENTER);
            text.setTextColor(context.getResources().getColor(
                    android.R.color.black));
            lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            this.addViewInLayout(text, -1, lp);
        }

        public void setText(String text) {
            this.text.setText(text);
        }

        /**
         * 设置状态：下拉刷新
         */
        public void setPullToRefresh() {
            if (statePullToRefresh)
                return;
            statePullToRefresh = true;
            setText(textPullToRefresh);
            imageView.startAnimation(mRotateDownAnimation);
        }

        /**
         * 设置状态：松开立即刷新
         */
        public void setReleaseToRefresh() {
            if (!statePullToRefresh)
                return;
            statePullToRefresh = false;
            setText(textReleaseToRefresh);
            imageView.startAnimation(mRotateUpAnimation);
        }

        /**
         * 设置状态：上拉加载更多
         */
        public void setPullToLoadMore() {
            if (statePullToLoadMore)
                return;
            statePullToLoadMore = true;
            setText(textPullToLoadMore);
            imageView.startAnimation(mRotateDownAnimation);
        }

        /**
         * 设置状态：松开立即加载更多
         */
        public void setReleaseToLoadMore() {
            if (!statePullToLoadMore)
                return;
            statePullToLoadMore = false;
            setText(textReleaseToLoadMore);
            imageView.startAnimation(mRotateUpAnimation);
        }
    }

    /**
     * 这里提供动画箭头图片 如果要替换箭头直接在此方法中获取Drawable或者在HeaderView中设置
     */
    private Bitmap getArrowBitmap(boolean up) {
        int width = (int) (headerheight * .6);
        int height = (int) (headerheight * .6);
        Bitmap bmp = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        Path path = new Path();
        float arrowHeight = (float) (width * .3);
        float arrowWidth = (float) (width * .7);
        float arrowPoint = width / 2 / 2;
        float barPad = (float) (height * .52);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        if (!up) {
            path.moveTo(arrowPoint + arrowWidth / 2, height);
            path.lineTo(arrowPoint + arrowWidth, height - arrowHeight);
            path.lineTo((float) (arrowPoint + arrowWidth * .75), height
                    - arrowHeight);
            path.lineTo((float) (arrowPoint + arrowWidth * .75), height
                    - barPad);
            path.lineTo((float) (arrowPoint + arrowWidth * .25), height
                    - barPad);
            path.lineTo((float) (arrowPoint + arrowWidth * .25), height
                    - arrowHeight);
            path.lineTo(arrowPoint, height - arrowHeight);
            path.close();
            path.moveTo((float) (arrowPoint + arrowWidth * .75),
                    (float) (barPad - barPad * .20));
            path.lineTo((float) (arrowPoint + arrowWidth * .25),
                    (float) (barPad - barPad * .20));
            path.lineTo((float) (arrowPoint + arrowWidth * .25),
                    (float) (barPad - barPad * .45));
            path.lineTo((float) (arrowPoint + arrowWidth * .75),
                    (float) (barPad - barPad * .45));
            path.close();
            path.moveTo((float) (arrowPoint + arrowWidth * .75),
                    (float) (barPad - barPad * .55));
            path.lineTo((float) (arrowPoint + arrowWidth * .25),
                    (float) (barPad - barPad * .55));
            path.lineTo((float) (arrowPoint + arrowWidth * .25),
                    (float) (barPad - barPad * .73));
            path.lineTo((float) (arrowPoint + arrowWidth * .75),
                    (float) (barPad - barPad * .73));
            path.close();
        } else {
            path.moveTo(arrowPoint + arrowWidth / 2, 0);
            path.lineTo(arrowPoint + arrowWidth, arrowHeight);
            path.lineTo((float) (arrowPoint + arrowWidth * .75), arrowHeight);
            path.lineTo((float) (arrowPoint + arrowWidth * .75), height
                    - barPad);
            path.lineTo((float) (arrowPoint + arrowWidth * .25), height
                    - barPad);
            path.lineTo((float) (arrowPoint + arrowWidth * .25), arrowHeight);
            path.lineTo(arrowPoint, arrowHeight);
            path.close();
            path.moveTo((float) (arrowPoint + arrowWidth * .75),
                    (float) (height - barPad + barPad * .10));
            path.lineTo((float) (arrowPoint + arrowWidth * .25),
                    (float) (height - barPad + barPad * .10));
            path.lineTo((float) (arrowPoint + arrowWidth * .25),
                    (float) (height - barPad + barPad * .35));
            path.lineTo((float) (arrowPoint + arrowWidth * .75),
                    (float) (height - barPad + barPad * .35));
            path.close();
            path.moveTo((float) (arrowPoint + arrowWidth * .75),
                    (float) (height - barPad + barPad * .45));
            path.lineTo((float) (arrowPoint + arrowWidth * .25),
                    (float) (height - barPad + barPad * .45));
            path.lineTo((float) (arrowPoint + arrowWidth * .25),
                    (float) (height - barPad + barPad * .63));
            path.lineTo((float) (arrowPoint + arrowWidth * .75),
                    (float) (height - barPad + barPad * .63));
            path.close();
        }
        // paint.setStyle(Paint.Style.STROKE);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor("#ffadada9"));
        canvas.drawPath(path, paint);
        return bmp;
    }

    /**
     * 计算两个坐标x/y的宽高比 如果x/y>1即x大于y，说明是横向滑动，不应该允许拖动
     *
     * @param p1
     * @param p2
     * @return
     */
    private float calculateGradient(Point p1, Point p2) {
        float dx = p1.x - p2.x;
        float dy = p1.y - p2.y;
        return Math.abs(dx / dy);
    }

    private class Point {
        float x, y;

        public Point(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}