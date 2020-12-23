package ru.csu.videochat.activities.category;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import ru.csu.videochat.R;
import ru.csu.videochat.model.CategoryModel;
import ru.csu.videochat.model.utilities.CategoryAdapter;
import ru.csu.videochat.model.utilities.PreferenceManager;
import ru.csu.videochat.presents.CategoryPresent;

public class CategoryFragment extends Fragment {
    private CategoryAdapter adapter;
    private TextView mLoginText;
    private ImageView mImgProfile;
    private LinearLayout layoutPerson;
    //    private ChipGroup yourAgeChips;
//    private Chip yourAgeChip;
//    private ChipGroup companionAgeChips;
//    private Button goneFilter;
    private ChipGroup groupExistChip;
    private CategoryPresent present;
    private ProgressBar mProgressBar;

    private ThemeListener themeListener;
    private boolean isExistThread;

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
        mLoginText = (TextView) view.findViewById(R.id.loginText);
        mProgressBar = view.findViewById(R.id.progressBar);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        mLoginText.setText(currentUser.getEmail());
        mImgProfile = (ImageView) view.findViewById(R.id.imgProfile_view);

        Uri avatar = PreferenceManager.getAvatar(getContext());
        if (avatar != null) {

            Picasso.with(getContext())
                    .load(avatar)
                    .transform(new CropCircleTransformation())
                    .into(mImgProfile);
        }
        // initFilter(view);
        // present.loadFilter();

        // Categories
        adapter = new CategoryAdapter(getContext());
        GridView gvMain = (GridView) view.findViewById(R.id.gridViewCategory);
        gvMain.setAdapter(adapter);

//        RecyclerView recyclerView = view.findViewById(R.id.gridViewCategory);
//        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
//        recyclerView.setAdapter(adapter);
        present.loadCategories();

        //слушатель тем
        isExistThread = true;
        themeListener = new ThemeListener();
        themeListener.execute();

        mImgProfile.setClickable(true);
        mLoginText.setClickable(true);

        mImgProfile.setOnClickListener(v -> ((BottomNavigationView) getActivity().findViewById(R.id.bottom_navigation)).setSelectedItemId(R.id.profile));

        mLoginText.setOnClickListener(v -> ((BottomNavigationView) getActivity().findViewById(R.id.bottom_navigation)).setSelectedItemId(R.id.profile));

        return view;
    }


    private class ThemeListener extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            while (isExistThread) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                present.loadCategories();
            }
            return null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isExistThread = false;
    }

    public void showCategories(List<Pair<String, String>> categories) {
        mProgressBar.setVisibility(View.GONE);
        adapter.setData(categories);
    }

    private void initFilter(View view) {
        groupExistChip = view.findViewById(R.id.existFilter);
        // init buttons for Layout Filter

        // setAnimate();
        //setClickFilter();

        // Show layout
        layoutPerson = (LinearLayout) view.findViewById(R.id.personInfo);
        layoutPerson.setVisibility(View.GONE);

        // Filter your age
//        ViewGroup yourViewGroup = layoutFilter.findViewById(R.id.yourAgeInclude);
//        yourViewGroup.removeAllViews();
//        View yourAgeView = LayoutInflater.from(getContext()).inflate(R.layout.item_filter_age, yourViewGroup);
//        yourAgeChips = yourAgeView.findViewById(R.id.groupAges);
//        yourAgeChips.setSingleSelection(true);
//        yourAgeChips.setOnCheckedChangeListener((group, checkedId) -> {
//            for (int i = 0; i < yourAgeChips.getChildCount(); i++) {
//                Chip chip = (Chip) yourAgeChips.getChildAt(i);
//                if (chip.isChecked()) {
//                    yourAgeChip = chip;
//                    return;
//                }
//            }
//        });
//
//        // Filter companion age
//        ViewGroup companionViewGroup = layoutFilter.findViewById(R.id.companionAgeInclude);
//        companionViewGroup.removeAllViews();
//        View companionAgeView = LayoutInflater.from(getContext()).inflate(R.layout.item_filter_age, companionViewGroup);
//        companionAgeChips = companionAgeView.findViewById(R.id.groupAges);
//
//        // Filter finish
//        goneFilter = (Button) view.findViewById(R.id.goneFilter);
//        goneFilter.setOnClickListener(v -> {
//            int countCompanionAges = companionAgeChips.getCheckedChipIds().size();
//            if (countCompanionAges > 0) {
//                if (yourAgeChips.getCheckedChipIds().size() == 0 || yourAgeChip == null) {
//                    Toast.makeText(getContext(), "Установите свой возраст", Toast.LENGTH_SHORT).show();
//                } else {
//                    String yourAgeText = yourAgeChip.getText().toString();
//                    String[] companionAges = new String[countCompanionAges];
//                    int i = 0;
//                    int j = 0;
//                    while (i < companionAgeChips.getChildCount()) {
//                        Chip chip = (Chip) companionAgeChips.getChildAt(i);
//                        if (chip.isChecked()) {
//                            companionAges[j] = chip.getText().toString();
//                            j++;
//                        }
//                        i++;
//                    }
//                    present.saveFilter(yourAgeText, companionAges);
//                    layoutFilter.setVisibility(View.GONE);
//                }
//            } else {
//                present.cleanFilter();
//                yourAgeChips.setSelectionRequired(false);
//                layoutFilter.setVisibility(View.GONE);
//            }
//            present.loadFilter();
//        });
    }

    public void showExistFilter(String yourAge, String[] ages) {
//        groupExistChip.removeAllViews();
//        groupExistChip.setVisibility(View.VISIBLE);
//        for (String age : ages) {
//            Chip chip = new Chip(getContext());
//            chip.setText(age);
//            groupExistChip.addView(chip);
//            for (int i = 0; i < companionAgeChips.getChildCount(); i++) {
//                Chip chipTemp = (Chip) companionAgeChips.getChildAt(i);
//                if (chipTemp.getText().toString().equals(age)) {
//                    chipTemp.setChecked(true);
//                }
//            }
//        }
//
//        for (int i = 0; i < yourAgeChips.getChildCount(); i++) {
//            Chip chipTemp = (Chip) yourAgeChips.getChildAt(i);
//            if (chipTemp.getText().toString().equals(yourAge)) {
//                chipTemp.setChecked(true);
//                break;
//            }
//        }
    }

    private void animateText(int fromColor, int toColor) {
//        final float[] from = new float[3],
//                to = new float[3];
//
//        Color.colorToHSV(fromColor, from);
//        Color.colorToHSV(toColor, to);
//
//        ValueAnimator anim = ValueAnimator.ofFloat(0, 2);
//        anim.setDuration(1000);
//
//        final float[] hsv = new float[3];
//        anim.addUpdateListener(animation -> {
//            // Переход по каждой оси HSV (оттенок, насыщенность, значение)
//            hsv[0] = from[0] + (to[0] - from[0]) * animation.getAnimatedFraction();
//            hsv[1] = from[1] + (to[1] - from[1]) * animation.getAnimatedFraction();
//            hsv[2] = from[2] + (to[2] - from[2]) * animation.getAnimatedFraction();
//
//            loginText.setTextColor(Color.HSVToColor(hsv));
//            filterImg.setColorFilter(Color.HSVToColor(hsv));
//        });
//
//        anim.start();
//        anim.setRepeatCount(1);
//        anim.setRepeatMode(ValueAnimator.REVERSE);
//        anim.start();
    }

    private void setAnimate() {
        //   animateText(getContext().getColor(R.color.grey_500), getContext().getColor(R.color.white));
    }

    /**
     * Установить обработку нажатия текст на Фильтрации
     */
//    private void setClickFilter() {
//        loginText.setClickable(true);
//        filterImg.setClickable(true);
//        filterImg.setOnClickListener(v -> {
//            onClickFilter();
//        });
//        loginText.setOnClickListener(v -> {
//            onClickFilter();
//        });
//    }

//    private void onClickFilter() {
//        layoutPerson.setVisibility(View.VISIBLE);
//    }

}
