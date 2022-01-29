package com.fmt.conflictproject.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;
import com.fmt.conflictproject.util.FlingHelper;
import java.util.Objects;

public class NestedScrollLayout extends NestedScrollView {

    ViewGroup mHeadView;//顶部控件
    ViewGroup mContentView;//内容控件
    int mVelocityY;//惯性滚动速度
    FlingHelper mFlingHelper;//处理惯性滑动速度与距离的转化
    int mConsumedY;//记录自身已经滚动的距离

    public NestedScrollLayout(@NonNull Context context) {
        this(context, null);
    }

    public NestedScrollLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NestedScrollLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mFlingHelper = new FlingHelper(getContext());
        //设置滚动监听事件
        setOnScrollChangeListener(new View.OnScrollChangeListener() {

            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                /*
                 * scrollY == 0 即还未滚动
                 * scrollY == getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()即滚动到顶部了
                 */
                //判断NestedScrollView是否滚动到顶部，若滚动到顶部，判断子控件是否需要继续滚动滚动
                if (scrollY == getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    dispatchChildFling();
                }
                //累计自身滚动的距离
                mConsumedY += scrollY - oldScrollY;
            }
        });
    }

    //将惯性滑动剩余的距离分发给子控件，继续惯性滑动
    private void dispatchChildFling() {
        if (mVelocityY != 0) {
            //将惯性滑动速度转化成距离
            double distance = mFlingHelper.getSplineFlingDistance(mVelocityY);
            //计算子控件应该滑动的距离 = 惯性滑动距离 - 已滑距离
            if (distance > mConsumedY) {
                RecyclerView recyclerView = getChildRecyclerView(mContentView);
                if (recyclerView != null) {
                    //将剩余滑动距离转化成速度交给子控件进行惯性滑动
                    int velocityY = mFlingHelper.getVelocityByDistance(distance - mConsumedY);
                    recyclerView.fling(0, velocityY);
                }
            }
        }

        mConsumedY = 0;
        mVelocityY = 0;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mHeadView = (ViewGroup) ((ViewGroup) getChildAt(0)).getChildAt(0);
        mContentView = (ViewGroup) ((ViewGroup) getChildAt(0)).getChildAt(1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //第一个要点：顶部悬浮效果
        //解决方式：将内容布局的高度设置为NestedScrollView的高度，即滑到顶了，自然就固定在顶部了
        ViewGroup.LayoutParams layoutParams = mContentView.getLayoutParams();
        layoutParams.height = getMeasuredHeight();
        mContentView.setLayoutParams(layoutParams);
    }

    /**
     * 嵌套滑动的两个角色：NestedScrollingParent3和NestedScrollingChild3，是由NestedScrollingChild3触发嵌套滑动，由NestedScrollingParent3触发不算嵌套滑动
     * 小结：子控件触发dispatchNestedPreScroll时会先调用支持嵌套滚动父控件的onNestedPreScroll让父控件先滚动，再执行
     * 自身的dispatchNestedScroll进行滚动
     */
    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        //super.onNestedPreScroll(target, dx, dy, consumed, type);
        /*
          第二个要点：先让NestedScrollingParent3滑动到顶部后，NestedScrollingChild3才可以滑动
          解决方法:由于NestedScrollView即实现了NestedScrollingParent3又实现了NestedScrollingChild3，
                  所以super.onNestedPreScroll(target, dx, dy, consumed, type)内部实现又会去调用父控件
                  的onNestedPreScroll方法，就会出现NestedScrollView无法滑动到顶部的想象，所以此处
                  注释掉super.onNestedPreScroll(target, dx, dy, consumed, type)，实现滑动逻辑
         */

        //向上滚动并且滚动的距离小于头部控件的高度，则此时父控件先滚动并记录消费的滚动距离
        boolean hideTop = dy > 0 && getScrollY() < mHeadView.getMeasuredHeight();

        if (hideTop) {
            //滚动到相应的滑动距离
            scrollBy(0, dy);
            //记录父控件消费的滚动记录，防止子控件重复滚动
            consumed[1] = dy;
        }
    }


    /**
     * 要点三：惯性滑动，父控件在滑动完成后，在通知子控件滑动,此时不是嵌套滚动
     * 解决方法:1.记录惯性滑动的速度
     * 2.将速度转化成距离
     * 3.计算子控件应该滑动的距离 = 惯性滑动距离 - 已滑距离
     * 4.将剩余滑动距离转化成速度交给子控件进行惯性滑动
     */
    @Override
    public void fling(int velocityY) {
        super.fling(velocityY);
        //3.1记录惯性滚动的速度
        if (velocityY <= 0) {
            mVelocityY = 0;
        } else {
            mVelocityY = velocityY;
        }
    }

    //递归获取子控件RecyclerView
    private RecyclerView getChildRecyclerView(ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View view = viewGroup.getChildAt(i);
            if (view instanceof RecyclerView && Objects.requireNonNull(((RecyclerView) view).getLayoutManager()).canScrollVertically()) {
                return (RecyclerView) view;
            } else if (viewGroup.getChildAt(i) instanceof ViewGroup) {
                RecyclerView childRecyclerView = getChildRecyclerView((ViewGroup) viewGroup.getChildAt(i));
                if (childRecyclerView != null && Objects.requireNonNull((childRecyclerView).getLayoutManager()).canScrollVertically()) {
                    return childRecyclerView;
                }
            }
        }
        return null;
    }
}
