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
import ru.csu.videochat.model.CategoryAdapter;
import ru.csu.videochat.model.entries.CategoryChat;
import ru.csu.videochat.model.PreferenceManager;

public class CategoryFragment extends Fragment {
    private static final CategoryChat[] data = {
            new CategoryChat("Знакомства", null),
            new CategoryChat("Фильмы", null),
            new CategoryChat("Игры", null)
    };
    private PreferenceManager preferenceManager;
    private CategoryAdapter adapter;


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

    public void showCategories(CategoryChat[] categories) {
        adapter.setData(categories);
    }

//    public void sendFCMTokenToDatabase(String token, String category) {
//        FirebaseFirestore database = FirebaseFirestore.getInstance();
//        DocumentReference categoryReference = database.collection(Constants.KEY_COLLECTION_CATEGORIES)
//                .document(preferenceManager.getString(Constants.KEY_CATEGORY_ID));
//        CollectionReference documentReference = categoryReference.collection(Constants.KEY_COLLECTION_CATEGORIES_CHATS);
//        categoryReference.update(Constants.KEY_FCM_TOKEN, token)
//                .addOnSuccessListener(aVoid -> Toast.makeText(getContext(), "Token updated successfully", Toast.LENGTH_SHORT).show())
//                .addOnFailureListener(e ->Toast.makeText(getContext(), "Unable to send token: " + e.getMessage(), Toast.LENGTH_SHORT).show());
//    }
//
//    public void deleteFCMTokenToDatabase(String token, String category) {
//        Toast.makeText(getContext(), "Соединение прервано...", Toast.LENGTH_SHORT).show();
//        FirebaseFirestore database = FirebaseFirestore.getInstance();
//        DocumentReference categoryReference = database.collection(Constants.KEY_COLLECTION_CATEGORIES)
//                .document(preferenceManager.getString(Constants.KEY_CATEGORY_ID));
//        DocumentReference documentReference = categoryReference.collection(Constants.KEY_COLLECTION_CATEGORIES_CHATS)
//                .document(preferenceManager.getString(Constants.KEY_CHAT_ID));
//        documentReference.delete();
//    }


}
