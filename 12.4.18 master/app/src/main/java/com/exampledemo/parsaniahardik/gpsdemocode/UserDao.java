package com.exampledemo.parsaniahardik.gpsdemocode;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM User")
    List<User> getAllUsers();

    @Insert
    void insert(User... users);


    //@Query( "DELETE FROM appdatabase")
    //public void delete();

   // @Update
   // void update(User... Users);

}