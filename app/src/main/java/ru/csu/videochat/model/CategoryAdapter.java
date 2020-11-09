package ru.csu.videochat.model;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ru.csu.videochat.R;
import ru.csu.videochat.activities.chat.ChatActivity;
import ru.csu.videochat.model.entries.CategoryChat;
import ru.csu.videochat.model.Constants;

public class CategoryAdapter extends ArrayAdapter<CategoryChat> {

    public CategoryAdapter(Context context) {
        super(context, R.layout.item_category);
    }

    public void setData(CategoryChat[] list) {
        clear();
        addAll(list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CategoryChat categoryChat = getItem(position);
        Context context = getContext();

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_category, null);
        }

        TextView title = convertView.findViewById(R.id.nameCategory);
        title.setText(categoryChat.getName());
        title.setClickable(true);
        convertView.setClickable(true);
        convertView.setOnClickListener(v -> onClick(context, categoryChat.getName()));
        title.setOnClickListener(v -> onClick(context, categoryChat.getName()));

        return convertView;
    }

    private void onClick(Context context, String category) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(Constants.KEY_CATEGORY_ID, category);
        intent.putExtra(Constants.KEY_CATEGORY_ID, category);

        context.startActivity(intent);
    }
}
