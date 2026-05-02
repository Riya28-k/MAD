package com.example.listview;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    ListView listViewSubjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listViewSubjects = findViewById(R.id.listViewSubjects);

        // List of subjects
        String[] subjects = {"Math", "Physics", "Chemistry", "Biology", "English", "Computer Science"};

        // Simple ArrayAdapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                subjects
        );

        listViewSubjects.setAdapter(adapter);
    }
}
