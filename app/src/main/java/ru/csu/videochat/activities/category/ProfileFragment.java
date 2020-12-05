package ru.csu.videochat.activities.category;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ru.csu.videochat.R;
import ru.csu.videochat.activities.auth.AuthActivity;

public class ProfileFragment extends Fragment implements View.OnClickListener {
    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    private TextView mStatusTextView;
    private TextView mDetailTextView;
    private ListView mListViewLinks;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        mStatusTextView = view.findViewById(R.id.status);
        mDetailTextView = view.findViewById(R.id.detail);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        mStatusTextView.setText(getString(R.string.emailpassword_status_fmt,
                user.getEmail(), user.isEmailVerified()));
        mDetailTextView.setText(getString(R.string.other_links));
        view.findViewById(R.id.signOutButton).setOnClickListener(this);
        view.findViewById(R.id.addLink).setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.addLink:
                Toast.makeText(getContext(), "togo", Toast.LENGTH_SHORT).show();
                break;
            case R.id.signOutButton:
                mAuth.signOut();
                Toast.makeText(getContext(), getString(R.string.signed_out), Toast.LENGTH_LONG).show();
                startActivity(new Intent(getContext(), AuthActivity.class));
                break;
        }
    }
}
