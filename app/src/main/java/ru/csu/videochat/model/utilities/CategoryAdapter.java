package ru.csu.videochat.model.utilities;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import ru.csu.videochat.R;
import ru.csu.videochat.activities.chat.SearchChatActivity;

public class CategoryAdapter extends ArrayAdapter<String> {

    public CategoryAdapter(Context context) {
        super(context, R.layout.item_category);
    }

    public void setData(String[] list) {
        clear();
        addAll(list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String categoryTitle = getItem(position);
        Context context = getContext();

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_category, null);
        }

        TextView title = convertView.findViewById(R.id.nameCategory);
        title.setText(categoryTitle);
        title.setClickable(true);
        convertView.setClickable(true);
        convertView.setOnClickListener(v -> onClick(context, categoryTitle));
        title.setOnClickListener(v -> onClick(context, categoryTitle));

        return convertView;
    }

    private void onClick(Context context, String category) {
        Intent intent = new Intent(context, SearchChatActivity.class);
        if (category != null) {
            intent.putExtra(Constants.KEY_CATEGORY_ID, category);
            context.startActivity(intent);
        } else Log.e("Adapter", "Category is null");

    }
}
