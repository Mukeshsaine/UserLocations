package com.example.userlocationlogin.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.userlocationlogin.R;
import com.example.userlocationlogin.SharedPrefManager;
import com.example.userlocationlogin.databinding.ActivitySplBinding;
import com.squareup.picasso.Picasso;

public class SplActivity extends AppCompatActivity {

    ActivitySplBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivitySplBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Picasso.get().load("https://www.rawpixel.com/image/6483679/png-sticker-public-domain").into(binding.image);


        SharedPrefManager sharedPrefManager = SharedPrefManager.getInstance(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (sharedPrefManager.isUserLoggedIn()) {
                    navigateToDashboard();
                }else {
                   startActivity(new Intent(getApplicationContext(), MainActivity.class));
                   finish();
                }
            }
        }, 2000);
    }
    private void navigateToDashboard() {
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
        finish();
    }
}