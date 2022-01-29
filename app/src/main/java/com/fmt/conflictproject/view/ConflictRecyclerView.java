package com.fmt.conflictproject.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fmt.conflictproject.adapter.RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 不可滚动的RecyclerView
 */
public class ConflictRecyclerView extends RecyclerView {

    public ConflictRecyclerView(@NonNull Context context) {
        super(context);
        init();
    }

    public ConflictRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ConflictRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        RecyclerAdapter bannerAdapter = new RecyclerAdapter(getContext(),getBanner());
        setAdapter(bannerAdapter);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        return false;
    }

    private List<String> getBanner() {
        List<String> data = new ArrayList<>();
        data.add("ParentView item 0");
        data.add("ParentView item 1");
        data.add("ParentView item 2");
        return data;
    }
}
