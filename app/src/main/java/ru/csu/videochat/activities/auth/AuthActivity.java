package ru.csu.videochat.activities.auth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ru.csu.videochat.R;
import ru.csu.videochat.activities.category.MainActivity;
import ru.csu.videochat.network.CommunicationWithServer;

public class AuthActivity extends BaseActivity implements View.OnClickListener {
    public static Context contextApp;
    private static final String TAG = "EmailPassword";

    private TextView mStatusTextView;
    // private TextView mDetailTextView;
    private EditText mEmailField;
    private EditText mPasswordField;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        contextApp = getApplicationContext();

        // Inner memory
        sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);

        // Views
        mStatusTextView = findViewById(R.id.status);
        // mDetailTextView = findViewById(R.id.detail);
        mEmailField = findViewById(R.id.fieldEmail);
        mPasswordField = findViewById(R.id.fieldPassword);

        // Buttons
        findViewById(R.id.emailSignInButton).setOnClickListener(this);
        findViewById(R.id.emailCreateAccountButton).setOnClickListener(this);
        findViewById(R.id.signOutButton).setOnClickListener(this);
        findViewById(R.id.verifyEmailButton).setOnClickListener(this);
        findViewById(R.id.reloadButton).setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
            } else {
                Log.d(TAG, "onAuthStateChanged:signed_out");
            }
            updateUI(user);
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        String key = "isFirstStart";
//        if (sharedPreferences.getBoolean(key, true)) {
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putBoolean(key, false).apply();
//            editor.apply();
//            editor.commit();
//            mStatusTextView.setVisibility(View.INVISIBLE);
//        } else mStatusTextView.setVisibility(View.VISIBLE);
    }

    private void createAccount(String email, String password) {
        if (!validateForm()) {
            return;
        }

        showProgressBar();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        CommunicationWithServer.sendMessageRegister(email, password);
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        Toast.makeText(AuthActivity.this, getString(R.string.register_failed),
                                Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }

                    hideProgressBar();
                });
    }

    private void signIn(String email, String password) {
        if (!validateForm()) {
            return;
        }
        showProgressBar();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        CommunicationWithServer.sendMessageAuth(email, password);
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        Toast.makeText(AuthActivity.this, getString(R.string.auth_failed),
                                Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }

                    if (!task.isSuccessful()) {
                        mStatusTextView.setText(R.string.auth_failed);
                    }
                    hideProgressBar();
                });
    }

    private void signOut() {
        mAuth.signOut();
        updateUI(null);
    }

    private void sendEmailVerification() {
        findViewById(R.id.verifyEmailButton).setEnabled(false);

        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(AuthActivity.this,
                                getString(R.string.desc_send_email) + " " + user.getEmail(),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AuthActivity.this,
                                getString(R.string.send_email_failed),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError(getString(R.string.required));
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password) || password.length() < 6) {
            mPasswordField.setError(getString(R.string.required_password));
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }

    private void updateUI(FirebaseUser user) {
        hideProgressBar();

        if (user != null) {
            if (user.isEmailVerified()) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } else {
                mStatusTextView.setVisibility(View.VISIBLE);
                mStatusTextView.setText(getString(R.string.emailpassword_status_fmt,
                        user.getEmail(), (user.isEmailVerified()) ? "да" : "Необходимо подтвердить email."));
                //    mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));

                findViewById(R.id.emailPasswordButtons).setVisibility(View.GONE);
                findViewById(R.id.emailPasswordFields).setVisibility(View.GONE);
                findViewById(R.id.signedInButtons).setVisibility(View.VISIBLE);

                findViewById(R.id.verifyEmailButton).setEnabled(!user.isEmailVerified());
            }
        } else {
            mStatusTextView.setVisibility(View.GONE);
            mStatusTextView.setText(R.string.signed_out);
//            mDetailTextView.setText(null);
            findViewById(R.id.emailPasswordButtons).setVisibility(View.VISIBLE);
            findViewById(R.id.emailPasswordFields).setVisibility(View.VISIBLE);
            findViewById(R.id.signedInButtons).setVisibility(View.GONE);
        }
    }

    private void updateUser() {
        FirebaseUser user = mAuth.getCurrentUser();
        user.reload();
        updateUI(user);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        switch (i) {
            case R.id.emailCreateAccountButton:
                createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString());
                break;
            case R.id.emailSignInButton:
                signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
                break;
            case R.id.signOutButton:
                signOut();
                break;
            case R.id.verifyEmailButton:
                sendEmailVerification();
                break;
            case R.id.reloadButton:
                updateUser();
                break;
        }
    }
}
