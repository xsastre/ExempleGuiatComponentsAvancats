package docencia.xaviersastre.taskmanager.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import docencia.xaviersastre.taskmanager.R;

public class ProfileFragment extends Fragment {

    private EditText etName, etUsername, etPassword;
    private Button btnSave;
    private SharedPreferences sharedPreferences;

    private static final String PREFS_NAME = "UserProfile";
    private static final String KEY_NAME = "name";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        etName = v.findViewById(R.id.etProfileName);
        etUsername = v.findViewById(R.id.etProfileUsername);
        etPassword = v.findViewById(R.id.etProfilePassword);
        btnSave = v.findViewById(R.id.btnSaveProfile);

        if (getContext() != null) {
            sharedPreferences = getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            loadProfileData();
        }

        btnSave.setOnClickListener(view -> saveProfileData());

        return v;
    }

    private void loadProfileData() {
        String name = sharedPreferences.getString(KEY_NAME, "");
        String username = sharedPreferences.getString(KEY_USERNAME, "");
        String password = sharedPreferences.getString(KEY_PASSWORD, "");

        etName.setText(name);
        etUsername.setText(username);
        etPassword.setText(password);
    }

    private void saveProfileData() {
        String name = etName.getText().toString().trim();
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (name.isEmpty() || username.isEmpty() || password.isEmpty()) {
            Toast.makeText(getContext(), "Si us plau, omple tots els camps", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_PASSWORD, password);
        editor.apply();

        Toast.makeText(getContext(), "Perfil desat correctament", Toast.LENGTH_SHORT).show();
    }
}
