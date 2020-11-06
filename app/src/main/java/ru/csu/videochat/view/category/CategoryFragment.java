package ru.csu.videochat.view.category;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ru.csu.videochat.R;

public class CategoryFragment extends Fragment {
    private GridView gvMain;
    private ArrayAdapter<String> adapter;
    private String[] data = {"Шутер", "Игры", "Аниме", "Фильмы", "По душам", "Чай-Кофе"};

    public static CategoryFragment newInstance() {
        return new CategoryFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        adapter = new ArrayAdapter<String>(getContext(), R.layout.item_category, R.id.nameCategory, data);
        gvMain = (GridView) view.findViewById(R.id.gridViewCategory);
        gvMain.setAdapter(adapter);


        return view;
    }
}
