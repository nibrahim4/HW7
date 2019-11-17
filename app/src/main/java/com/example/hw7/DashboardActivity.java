package com.example.hw7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DashboardActivity extends AppCompatActivity {

    public Button btn_listOfUsers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        setTitle("Home Page");

        btn_listOfUsers = findViewById(R.id.btn_ListOfUsers);
        btn_listOfUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToUsers = new Intent(DashboardActivity.this, UsersActivity.class);
                startActivity(intentToUsers);
            }
        });
    }
}
