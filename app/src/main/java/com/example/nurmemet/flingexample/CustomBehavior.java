package com.example.nurmemet.flingexample;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.ScrollerCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by nurmemet on 8/25/2016.
 */
public class CustomBehavior extends CoordinatorLayout.Behavior<View> {
    private ScrollerCompat mScroller;
    private Runnable mFlingRunnable = null;

    public CustomBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = ScrollerCompat.create(context);
    }


    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed) {
        consumed[0] = dx / 2;
        consumed[1] = dy / 2;

    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        child.offsetTopAndBottom(dyConsumed);

    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        return true;
    }

    @Override
    public boolean onNestedFling(CoordinatorLayout coordinatorLayout, View child, View target, float velocityX, float velocityY, boolean consumed) {
        NestedScrollView scrollView=(NestedScrollView)target ;
        int height = scrollView.getHeight() - scrollView.getPaddingBottom() - scrollView.getPaddingTop();
        int bottom = scrollView.getChildAt(0).getHeight();
        mScroller.fling(child.getLeft(),child.getTop(), (int) velocityX, (int) velocityY, 0, 0, coordinatorLayout.getPaddingTop(),  Math.max(0, bottom - height));
        if (mFlingRunnable != null) {
            child.removeCallbacks(mFlingRunnable);
            mFlingRunnable=null;
        }
        if (mFlingRunnable == null) {
            mFlingRunnable = new FlingRunnable(coordinatorLayout, child);
            ViewCompat.postOnAnimation(child, mFlingRunnable);
        }
        return false;
    }


    @Override
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, View child, View target, float velocityX, float velocityY) {
        return false;
    }


    private class FlingRunnable implements Runnable {
        private final CoordinatorLayout mParent;
        private final View mLayout;

        FlingRunnable(CoordinatorLayout parent, View layout) {
            mParent = parent;
            mLayout = layout;
        }

        @Override
        public void run() {
            if (mLayout != null && mScroller != null) {
                if (mScroller.computeScrollOffset()) {
                    ViewCompat.offsetTopAndBottom(mLayout,mScroller.getCurrY()-mLayout.getTop());
                    ViewCompat.postOnAnimation(mLayout, this);
                } else {
                    onFlingFinished(mParent, mLayout);
                }
            }
        }
    }


    void onFlingFinished(CoordinatorLayout parent, View layout) {
        // no-op
    }



}
