package com.fmt.conflictproject.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.fmt.conflictproject.R;
import com.fmt.conflictproject.adapter.RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chidehang on 2020-01-12
 */
public class RecyclerViewFragment extends Fragment {

    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false));
        final RecyclerAdapter adapter = new RecyclerAdapter(getContext(),getData());
        recyclerView.setAdapter(adapter);
        return view;
    }

    private List<String> getData() {
        List<String> data = new ArrayList<>();
        for(int i = 0; i < 100; i ++) {
            data.add("ChildView item " + i);
        }
        return data;
    }
}
