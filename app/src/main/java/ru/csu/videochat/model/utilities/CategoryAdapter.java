package ru.csu.videochat.model.utilities;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import ru.csu.videochat.R;
import ru.csu.videochat.activities.chat.SearchChatActivity;
import ru.csu.videochat.model.CategoryModel;

public class CategoryAdapter extends ArrayAdapter<String> {

    public interface ICompleteCallback {
        void onComplete(int count);
    }

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

        TextView countChats = convertView.findViewById(R.id.countChats);
        countChats.setClickable(true);
        CategoryModel.getCountChats(categoryTitle, count -> countChats.setText(String.valueOf(count)));

        convertView.setClickable(true);
        convertView.setOnClickListener(v -> onClick(context, categoryTitle));
        title.setOnClickListener(v -> onClick(context, categoryTitle));
        countChats.setOnClickListener(v -> onClick(context, categoryTitle));

        return convertView;
    }

    private void onClick(Context context, String category) {
        if (CategoryModel.isNetworkAvailable(context)) {
            Intent intent = new Intent(context, SearchChatActivity.class);
            if (category != null) {
                intent.putExtra(Constants.KEY_CATEGORY_ID, category);
                context.startActivity(intent);
            } else Log.e("Adapter", "Category is null");
        } else
            Toast.makeText(context, context.getString(R.string.fail_connect_ethe), Toast.LENGTH_SHORT).show();

    }
}
