package ru.csu.videochat.presents;

import androidx.annotation.NonNull;

import ru.csu.videochat.activities.category.CategoryFragment;
import ru.csu.videochat.model.CategoryModel;
import ru.csu.videochat.model.utilities.Constants;
import ru.csu.videochat.model.utilities.PreferenceManager;

public class CategoryPresent {
    private CategoryFragment view;
    private CategoryModel model;

    public CategoryPresent(CategoryFragment view, CategoryModel model) {
        this.view = view;
        this.model = model;
    }

    public void detachView() {
        view = null;
    }

    public void saveFilter(@NonNull String yourAge, String[] companionAges) {
        PreferenceManager preferenceManager = new PreferenceManager(view.getContext());
        preferenceManager.cleanPreference();
        preferenceManager.putString(Constants.KEY_YOUR_AGE, yourAge);
        preferenceManager.putStringArray(Constants.KEY_COMPANION_AGES, companionAges);
    }

    public void cleanFilter() {
        PreferenceManager preferenceManager = new PreferenceManager(view.getContext());
        preferenceManager.cleanPreference();
    }

}
