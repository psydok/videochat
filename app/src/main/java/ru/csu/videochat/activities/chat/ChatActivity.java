package ru.csu.videochat.activities.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import ru.csu.videochat.R;
import ru.csu.videochat.interfaces.IUsersListener;
import ru.csu.videochat.model.Constants;
import ru.csu.videochat.model.ModelDB;
import ru.csu.videochat.model.entries.Chats;
import ru.csu.videochat.model.entries.User;
import ru.csu.videochat.presents.ChatPresent;

public class ChatActivity extends AppCompatActivity implements IUsersListener {
    private String selectedCategory;
    private Chats chats;
    private ChatPresent present;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                selectedCategory = extras.getString(Constants.KEY_CATEGORY_ID);
            }
        }

        init();
        searchFreeUser();
    }

    private void searchFreeUser() {
        ModelDB modelDB = new ModelDB(this, "2");
        present = new ChatPresent(modelDB, this);
        present.getChats(selectedCategory, this);
    }

    private void init() {
        TextView category = (TextView) findViewById(R.id.selectCategory);
        if (selectedCategory != null)
            category.setText(getString(R.string.select_category, selectedCategory));

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_nav_search);
        MenuItem itemNext = bottomNavigationView.getMenu().findItem(R.id.next);
        itemNext.setEnabled(false);
        itemNext.setVisible(false);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.exit:
                    finish();
                    break;
                case R.id.next:
                    Toast.makeText(this, "togo", Toast.LENGTH_SHORT).show();
                    break;
            }
            return true;
        });

    }

    public void setConnectVideoUser(Chats chats) {

        this.chats = chats;

    }


    public void initiativeVideoMeeting(User user) {
        if (user.getUidToken() == null || user.getUidToken().trim().isEmpty()) {
            Toast.makeText(this, user.getUidToken(), Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "Собеседник найден. Соединяем...", Toast.LENGTH_SHORT).show();
        }
    }
}