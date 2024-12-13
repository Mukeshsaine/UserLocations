package com.example.userlocationlogin.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.userlocationlogin.R;
import com.example.userlocationlogin.RoomDB.AppDatabase;
import com.example.userlocationlogin.Models.User;
import com.example.userlocationlogin.SharedPrefManager;
import com.example.userlocationlogin.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding binding;
    SharedPrefManager sharedPrefManager;
    private AppDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        database = AppDatabase.getInstance(this);


        sharedPrefManager = SharedPrefManager.getInstance(this);

        binding.register.setOnClickListener(v -> {
            userRegister();
        });


    }

    private void userRegister() {
        String username = binding.name.getText().toString().trim();
        String email = binding.email.getText().toString().trim();
        String phone = binding.phone.getText().toString().trim();
        String password = binding.password.getText().toString().trim();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)|| TextUtils.isEmpty(email)|| TextUtils.isEmpty(phone)) {
            showToast("All fields are required");
        } else {
            sharedPrefManager.saveUser(username, password, phone, email);
            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setPhone(phone);
            user.setPassword(password);

            new Thread(() -> {
                database.userDao().insert(user);
                runOnUiThread(() -> {
                    binding.name.setText("");
                    binding.email.setText("");
                    binding.password.setText("");
                    binding.phone.setText("");
                    showToast("User added");
                    navigateToDashboard();
                });
            }).start();
            showToast("Registered successfully!");
        }


    }
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    private void navigateToDashboard() {
        startActivity(new Intent(this, DashboardActivity.class));
        finish();
    }
}