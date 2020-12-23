package ru.csu.videochat.model.utilities;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jp.wasabeef.picasso.transformations.CropSquareTransformation;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;
import ru.csu.videochat.R;
import ru.csu.videochat.activities.chat.SearchChatActivity;
import ru.csu.videochat.model.CategoryModel;

public class CategoryAdapter extends ArrayAdapter<Pair<String, String>> {
    private Context context;
//    private List<Pair<String, String>> data = new ArrayList<>();
//    private IItemUpdateListener iItemUpdateListener;
//
//    public interface IItemUpdateListener {
//        void onItemUpdate(View view, int position);
//    }
//
//    public CategoryAdapter(Context context) {
//        this.context = context;
//    }
//
//    public void setData(List<Pair<String, String>> pairs) {
//        this.data.clear();
//        this.data.addAll(pairs);
//    }
//
//    @NonNull
//    @Override
//    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
//        return new CategoryHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {
//        Pair<String, String> pair = data.get(position);
//        holder.title.setText(pair.first);
//        Picasso.with(context)
//                .load(pair.second)
//                .placeholder(R.drawable.shape_category)
//                .error(R.drawable.shape_category)
//                .into(holder.backgroundCategory);
//    }
//
//    @Override
//    public int getItemCount() {
//        return data.size();
//    }
//
//    public class CategoryHolder extends RecyclerView.ViewHolder implements {
//        TextView title;
//        TextView countChats;
//        ImageView backgroundCategory;
//
//        public CategoryHolder(@NonNull View itemView) {
//            super(itemView);
//            title = itemView.findViewById(R.id.nameCategory);
//            backgroundCategory = itemView.findViewById(R.id.backgroundCategory);
//            countChats = itemView.findViewById(R.id.countChats);
//
//            title.setClickable(true);
//            backgroundCategory.setClickable(true);
//            countChats.setClickable(true);
//
//            ite.setOnClickListener(v -> onClick(context, categoryTitle));
//            title.setOnClickListener(v -> onClick(context, categoryTitle));
//            countChats.setOnClickListener(v -> onClick(context, categoryTitle));
//            backgroundCategory.setOnClickListener(v -> onClick(context, categoryTitle));
//        }
//
//        public void setCount() {
//            if (CategoryModel.isNetworkAvailable(context)) {
//                CategoryModel.getCountChats(title.getText().toString(), count -> {
//                    countChats.setText(String.valueOf(count));
//                    if (count == -1)
//                        countChats.setVisibility(View.GONE);
//                });
//            } else countChats.setVisibility(View.GONE);
//        }
//    }
//
//    public void setIItemUpdateListener(IItemUpdateListener iItemUpdateListener) {
//        this.iItemUpdateListener = iItemUpdateListener;
//    }

    public interface ICompleteCallback {
        void onComplete(int count);
    }

    public CategoryAdapter(Context context) {
        super(context, R.layout.item_category);
    }

    public void setData(List<Pair<String, String>> list) {
        clear();
        addAll(list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Pair<String, String> category = getItem(position);
        Context context = getContext();

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_category, null);
        }
        String categoryTitle = category.first;
        String categoryUrl = category.second;

        TextView title = convertView.findViewById(R.id.nameCategory);
        ImageView backgroundCategory = convertView.findViewById(R.id.backgroundCategory);

        if (categoryUrl.isEmpty()) {
        } else {
            final int radius = 12;
            final int margin = 0;
            final Transformation transformation = new RoundedCornersTransformation(radius, margin);
            Picasso.with(context)
                    .load(category.second)
                    .transform(transformation)
                    .transform(new CropSquareTransformation())
                    .error(R.drawable.shape_category)
                    .resize(200,200)
                    .into(backgroundCategory);
        }
        title.setText(categoryTitle);
        title.setClickable(true);

        TextView countChats = convertView.findViewById(R.id.countChats);
        countChats.setClickable(true);
        if (CategoryModel.isNetworkAvailable(context)) {
            CategoryModel.getCountChats(categoryTitle, count -> {
                countChats.setText(String.valueOf(count));
                if (count == -1)
                    countChats.setVisibility(View.GONE);
            });
        } else countChats.setVisibility(View.GONE);

        convertView.setClickable(true);
        convertView.setOnClickListener(v -> onClick(context, categoryTitle));
        title.setOnClickListener(v -> onClick(context, categoryTitle));
        countChats.setOnClickListener(v -> onClick(context, categoryTitle));
        backgroundCategory.setOnClickListener(v -> onClick(context, categoryTitle));
        backgroundCategory.setClickable(true);


        return convertView;
    }

    private void onClick(Context context, String category) {
        if (CategoryModel.isNetworkAvailable(context)) {
            Intent intent = new Intent(context, SearchChatActivity.class);
            if (category != null) {
                intent.putExtra(Constants.KEY_CATEGORY_ID, category);
                context.startActivity(intent);
            } else Log.e("CAdapter", "Name category is NULL!!!");
        } else
            Toast.makeText(context, context.getString(R.string.fail_connect_ethe), Toast.LENGTH_SHORT).show();
    }


}
