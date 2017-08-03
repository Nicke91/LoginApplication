package com.example.nicke.loginapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.nicke.loginapplication.SQLDatabase.SQLDatabaseManager;

/**
 * Created by Nicke on 7/20/2017.
 */

public class ActivityAllUsers extends AppCompatActivity {


    SQLDatabaseManager sqlDatabaseManager;
    TextView usersItem;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);

        sqlDatabaseManager = new SQLDatabaseManager(this);

        usersItem = (TextView) findViewById(R.id.all_users_item);
        usersItem.setText(sqlDatabaseManager.getIdAndUserName());


    }
}
