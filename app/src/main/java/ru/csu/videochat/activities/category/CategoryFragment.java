package ru.csu.videochat.activities.category;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ru.csu.videochat.R;
import ru.csu.videochat.model.utilities.CategoryAdapter;

public class CategoryFragment extends Fragment {
    private CategoryAdapter adapter;
    private static final String[] data = {
            "Знакомства",
            "Фильмы",
            "Игры",
            "Общение",
            "Книги",
            "Помощь"
    };

    public static CategoryFragment newInstance() {
        return new CategoryFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        adapter = new CategoryAdapter(getContext());
        GridView gvMain = (GridView) view.findViewById(R.id.gridViewCategory);
        gvMain.setAdapter(adapter);

        if (data != null)
            showCategories(data);

        return view;
    }

    public void showCategories(String[] categories) {
        adapter.setData(categories);
    }
}
