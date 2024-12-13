package com.example.userlocationlogin.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.userlocationlogin.R;
import com.example.userlocationlogin.SharedPrefManager;
import com.example.userlocationlogin.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    final boolean[] isPasswordVisible = {false};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SharedPrefManager sharedPrefManager = SharedPrefManager.getInstance(this);

        binding.passwordToggle.setOnClickListener(view -> {
            isPasswordVisible[0] = !isPasswordVisible[0];
            if (isPasswordVisible[0]) {
                binding.password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
               // binding.passwordToggle.setImageResource(R.drawable.ic_visibility_on);
            } else {
                binding.password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
               // binding.passwordToggle.setImageResource(R.drawable.ic_visibility_off);
            }
            // Ensure the cursor stays at the end
            binding.password.setSelection(binding.password.getText().length());
        });

        binding.register.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            finish();
        });
        binding.login.setOnClickListener(v ->{
            String userEmail = binding.email.getText().toString().trim();
            String password = binding.password.getText().toString().trim();

            if (TextUtils.isEmpty(userEmail) || TextUtils.isEmpty(password)) {
                Toast.makeText(MainActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
            } else {
                String savedUseremial = sharedPrefManager.getUseremail();
                String savedPassword = sharedPrefManager.getPassword();

                if (userEmail.equals(savedUseremial) && password.equals(savedPassword)) {
                    navigateToDashboard();
                } else {
                    Toast.makeText(MainActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
    private void navigateToDashboard() {
        startActivity(new Intent(MainActivity.this, DashboardActivity.class));
        finish();
    }
}