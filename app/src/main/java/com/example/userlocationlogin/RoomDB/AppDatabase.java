package com.example.userlocationlogin.RoomDB;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

import com.example.userlocationlogin.Models.User;
import com.example.userlocationlogin.Models.userLocation;

@Database(entities = {User.class, userLocation.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract UserDao userDao();
    public abstract UserLocationDao userLocationDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "user_database")
                    .fallbackToDestructiveMigration() // Clear database if version changes
                    .build();
        }
        return instance;
    }
}

