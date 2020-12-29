package ru.csu.videochat.activities.category;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import ru.csu.videochat.R;
import ru.csu.videochat.activities.auth.AuthActivity;
import ru.csu.videochat.model.CategoryModel;
import ru.csu.videochat.model.utilities.DialogCreateLink;
import ru.csu.videochat.model.utilities.PreferenceManager;
import ru.csu.videochat.network.CommunicationWithServer;

public class ProfileFragment extends Fragment implements View.OnClickListener {
    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    public static final int SELECT_IMAGE = 12;
    private TextView mStatusTextView;
    private ImageView mBack;
    private FirebaseAuth mAuth;
    private ImageView mImgProfile;
    private TextInputLayout inputLink;
    private TextInputEditText editLink;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        mStatusTextView = view.findViewById(R.id.status);
        mBack = view.findViewById(R.id.back);
        mImgProfile = view.findViewById(R.id.imgProfile_view);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user.isAnonymous()) {
            getActivity().finish();
        }
        String email = user.getEmail();
        if (email.length() > 15) {
            String subEmail = email.substring(0, 15) + "...";
            mStatusTextView.setText(subEmail);
        } else mStatusTextView.setText(email);


        view.findViewById(R.id.createAvatar).setOnClickListener(this);
        view.findViewById(R.id.signOutButton).setOnClickListener(this);
        view.findViewById(R.id.addLink).setOnClickListener(this);
        init();

        return view;
    }

    private void init() {
        mBack.setClickable(true);
        mBack.setOnClickListener(v ->
                ((BottomNavigationView) getActivity().findViewById(R.id.bottom_navigation))
                        .setSelectedItemId(R.id.category));
        Uri avatar = PreferenceManager.getAvatar(getContext());
        if (avatar != null) {
            Picasso.with(getContext())
                    .load(avatar)
                    .transform(new CropCircleTransformation())
                    .into(mImgProfile);
        }
    }

    private boolean validateLink(String textLink) {
        textLink = textLink.trim();
        return (!textLink.isEmpty() && textLink.startsWith("https")
                && Patterns.WEB_URL.matcher(textLink).matches());
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case 0:
                if (resultCode == Activity.RESULT_OK) {
                    String link = editLink.getText().toString();
                    if (validateLink(link)) {
                        CategoryModel.isServerAvailable(CommunicationWithServer.getServer(),
                                check -> {
                                    if (check) {
                                        CommunicationWithServer.sendMessageLink(
                                                PreferenceManager.getToken(getContext()),
                                                link);
                                        Toast.makeText(getContext(), "Сохранено на сервере",
                                                Toast.LENGTH_LONG).show();
                                    } else
                                        Toast.makeText(getContext(), "Сервер временно не доступен для записи",
                                                Toast.LENGTH_LONG).show();
                                });
                    } else
                        Toast.makeText(getContext(), "Неправильная ссылка", Toast.LENGTH_LONG).show();
                }
                break;
            case SELECT_IMAGE:
                if (resultCode == Activity.RESULT_OK) {
                    Uri selectedImage = data.getData();
                    getActivity().getContentResolver().takePersistableUriPermission(selectedImage,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    PreferenceManager.putAvatar(getContext(), selectedImage.toString());
                    Picasso.with(getContext())
                            .load(selectedImage)
                            .resize(100, 100)
                            .transform(new CropCircleTransformation())
                            .into(mImgProfile);
                }
                break;
        }
    }


    private void initDialogCreateTask() {
        DialogCreateLink dialog = DialogCreateLink.newInstance();
        dialog.setTargetFragment(this, 0);
        FragmentManager fm = getParentFragmentManager();

        dialog.show(fm.beginTransaction(), "Create task - show");
        fm.executePendingTransactions();

        inputLink = dialog.getDialog().findViewById(R.id.inputLink);
        editLink = dialog.getDialog().findViewById(R.id.editLink);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.createAvatar:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_IMAGE);
                break;
            case R.id.addLink:
                initDialogCreateTask();
                break;
            case R.id.signOutButton:
                mAuth.signOut();
                Toast.makeText(getContext(), getString(R.string.signed_out), Toast.LENGTH_LONG).show();
                startActivity(new Intent(getContext(), AuthActivity.class));
                break;
        }
    }

}
