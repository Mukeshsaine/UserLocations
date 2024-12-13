package com.example.userlocationlogin.RoomDB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.userlocationlogin.Models.User;
import com.example.userlocationlogin.Models.userLocation;

import java.util.List;

@Dao
public interface UserLocationDao {

    @Insert
    void insertLocationUser(userLocation location);

    @Update
    void updateLocationUser(userLocation location);

    @Delete
    void deleteLocationUser(userLocation location);

    @Query("SELECT * FROM user_tableLocation")
    List<User> getAllUsersLocation();

    @Query("SELECT * FROM user_tableLocation WHERE id = :userId")
    User getUserById(int userId);
}
