package ru.csu.videochat.activities.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import ru.csu.videochat.R;
import ru.csu.videochat.interfaces.IUsersListener;
import ru.csu.videochat.model.utilities.Constants;
import ru.csu.videochat.model.SearchChatModel;
import ru.csu.videochat.model.entries.User;
import ru.csu.videochat.presents.SearchChatPresent;

public class SearchChatActivity extends AppCompatActivity implements IUsersListener {
    private String selectedCategory;
    private SearchChatPresent present;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_chat);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                selectedCategory = extras.getString(Constants.KEY_CATEGORY_ID);
            }
        }

        init();
        searchFreeUser();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
    }

    @Override
    protected void onDestroy() {
        present.detachView();
        getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onDestroy();
    }

    private void searchFreeUser() {
        SearchChatModel searchChatModel = SearchChatModel.getInstance(this);
        present = new SearchChatPresent(searchChatModel, this);
        present.getChats(selectedCategory, this);
    }

    private void init() {
        TextView category = (TextView) findViewById(R.id.selectCategory);
        if (selectedCategory != null)
            category.setText(getString(R.string.select_category, selectedCategory));
        ImageView close = findViewById(R.id.icon_close_search);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    public void initiativeVideoMeeting(User user) {
        if (user.getUidToken() == null || user.getUidToken().trim().isEmpty()) {
            Log.e("Notify", "User: " + user);
        } else {
            Toast.makeText(this, "Собеседник найден. Соединяем...", Toast.LENGTH_SHORT).show();
            present.connectVideo();
            finish();
        }
    }
}