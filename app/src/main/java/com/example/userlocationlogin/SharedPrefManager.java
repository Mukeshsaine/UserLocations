package com.example.userlocationlogin;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
        private static final String SHARED_PREF_NAME = "user_shared_pref";
        private static final String KEY_USERNAME = "username";
        private static final String KEY_PASSWORD = "password";
        private static final String KEY_EMAIL = "email";
        private static final String KEY_PHOME = "phone";

        private static SharedPrefManager instance;
        private SharedPreferences sharedPreferences;
        private SharedPreferences.Editor editor;

        private SharedPrefManager(Context context) {
            sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
        }

        public static synchronized SharedPrefManager getInstance(Context context) {
            if (instance == null) {
                instance = new SharedPrefManager(context);
            }
            return instance;
        }

        public void saveUser(String username, String password, String email, String phone) {
            editor.putString(KEY_USERNAME, username);
            editor.putString(KEY_PASSWORD, password);
            editor.putString(KEY_EMAIL, email);
            editor.putString(KEY_PHOME, phone);
            editor.apply();
        }

        public String getUsername() {
            return sharedPreferences.getString(KEY_USERNAME, null);
        }
        public String getUseremail() {
            return sharedPreferences.getString(KEY_EMAIL, null);
        }

        public String getPassword() {
            return sharedPreferences.getString(KEY_PASSWORD, null);
        }

        public boolean isUserLoggedIn() {
            return getUsername() != null;
        }

        public void logout() {
            editor.clear();
            editor.apply();
        }
    }
