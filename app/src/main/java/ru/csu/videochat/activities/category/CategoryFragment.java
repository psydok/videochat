package ru.csu.videochat.activities.category;


import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import ru.csu.videochat.R;
import ru.csu.videochat.model.CategoryModel;
import ru.csu.videochat.model.utilities.CategoryAdapter;
import ru.csu.videochat.presents.CategoryPresent;

public class CategoryFragment extends Fragment {
    private CategoryAdapter adapter;
    private TextView filterText;
    private ImageView filterImg;
    private RelativeLayout layoutFilter;
    private ChipGroup yourAgeChips;
    private Chip yourAgeChip;
    private ChipGroup companionAgeChips;
    private Button goneFilter;
    private ChipGroup groupExistChip;
    private CategoryPresent present;

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
        CategoryModel model = CategoryModel.getInstance(getContext());
        present = new CategoryPresent(this, model);

        initFilter(view);
        present.loadFilter();
        // Categories
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

    private void initFilter(View view) {
        groupExistChip = view.findViewById(R.id.existFilter);
        // init buttons for Layout Filter
        filterText = (TextView) view.findViewById(R.id.filterText);
        filterImg = (ImageView) view.findViewById(R.id.filterImg);
        setAnimate();
        setClickFilter();

        // Show layout
        layoutFilter = (RelativeLayout) view.findViewById(R.id.filterSetting);
        layoutFilter.setVisibility(View.GONE);

        // Filter your age
        ViewGroup yourViewGroup = layoutFilter.findViewById(R.id.yourAgeInclude);
        yourViewGroup.removeAllViews();
        View yourAgeView = LayoutInflater.from(getContext()).inflate(R.layout.item_filter_age, yourViewGroup);
        yourAgeChips = yourAgeView.findViewById(R.id.groupAges);
        yourAgeChips.setSingleSelection(true);
        yourAgeChips.setOnCheckedChangeListener((group, checkedId) -> {
            for (int i = 0; i < yourAgeChips.getChildCount(); i++) {
                Chip chip = (Chip) yourAgeChips.getChildAt(i);
                if (chip.isChecked()) {
                    yourAgeChip = chip;
                    return;
                }
            }
        });

        // Filter companion age
        ViewGroup companionViewGroup = layoutFilter.findViewById(R.id.companionAgeInclude);
        companionViewGroup.removeAllViews();
        View companionAgeView = LayoutInflater.from(getContext()).inflate(R.layout.item_filter_age, companionViewGroup);
        companionAgeChips = companionAgeView.findViewById(R.id.groupAges);

        // Filter finish
        goneFilter = (Button) view.findViewById(R.id.goneFilter);
        goneFilter.setOnClickListener(v -> {
            int countCompanionAges = companionAgeChips.getCheckedChipIds().size();
            if (countCompanionAges > 0) {
                if (yourAgeChips.getCheckedChipIds().size() == 0 || yourAgeChip == null) {
                    Toast.makeText(getContext(), "Установите свой возраст", Toast.LENGTH_SHORT).show();
                } else {
                    String yourAgeText = yourAgeChip.getText().toString();
                    String[] companionAges = new String[countCompanionAges];
                    int i = 0;
                    int j = 0;
                    while (i < companionAgeChips.getChildCount()) {
                        Chip chip = (Chip) companionAgeChips.getChildAt(i);
                        if (chip.isChecked()) {
                            companionAges[j] = chip.getText().toString();
                            j++;
                        }
                        i++;
                    }
                    present.saveFilter(yourAgeText, companionAges);
                    layoutFilter.setVisibility(View.GONE);
                }
            } else {
                present.cleanFilter();
                yourAgeChips.setSelectionRequired(false);
                layoutFilter.setVisibility(View.GONE);
            }
            present.loadFilter();
        });
    }

    public void showExistFilter(String yourAge, String[] ages) {
        groupExistChip.removeAllViews();
        groupExistChip.setVisibility(View.VISIBLE);
        for (String age : ages) {
            Chip chip = new Chip(getContext());
            chip.setText(age);
            groupExistChip.addView(chip);
            for (int i = 0; i < companionAgeChips.getChildCount(); i++) {
                Chip chipTemp = (Chip) companionAgeChips.getChildAt(i);
                if (chipTemp.getText().toString().equals(age)) {
                    chipTemp.setChecked(true);
                }
            }
        }

        for (int i = 0; i < yourAgeChips.getChildCount(); i++) {
            Chip chipTemp = (Chip) yourAgeChips.getChildAt(i);
            if (chipTemp.getText().toString().equals(yourAge)) {
                chipTemp.setChecked(true);
                break;
            }
        }
    }

    private void animateText(int fromColor, int toColor) {
        final float[] from = new float[3],
                to = new float[3];

        Color.colorToHSV(fromColor, from);
        Color.colorToHSV(toColor, to);

        ValueAnimator anim = ValueAnimator.ofFloat(0, 2);
        anim.setDuration(1000);

        final float[] hsv = new float[3];
        anim.addUpdateListener(animation -> {
            // Переход по каждой оси HSV (оттенок, насыщенность, значение)
            hsv[0] = from[0] + (to[0] - from[0]) * animation.getAnimatedFraction();
            hsv[1] = from[1] + (to[1] - from[1]) * animation.getAnimatedFraction();
            hsv[2] = from[2] + (to[2] - from[2]) * animation.getAnimatedFraction();

            filterText.setTextColor(Color.HSVToColor(hsv));
            filterImg.setColorFilter(Color.HSVToColor(hsv));
        });

        anim.start();
        anim.setRepeatCount(1);
        anim.setRepeatMode(ValueAnimator.REVERSE);
        anim.start();
    }

    private void setAnimate() {
        animateText(getContext().getColor(R.color.grey_500), getContext().getColor(R.color.white));
    }

    /**
     * Установить обработку нажатия текст на Фильтрации
     */
    private void setClickFilter() {
        filterText.setClickable(true);
        filterImg.setClickable(true);
        filterImg.setOnClickListener(v -> {
            onClickFilter();
        });
        filterText.setOnClickListener(v -> {
            onClickFilter();
        });
    }

    private void onClickFilter() {
        layoutFilter.setVisibility(View.VISIBLE);
    }
}
