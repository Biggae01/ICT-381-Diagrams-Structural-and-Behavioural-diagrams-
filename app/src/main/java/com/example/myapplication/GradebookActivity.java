package com.example.myapplication;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

public class GradebookActivity extends AppCompatActivity {

    String[] courses = {"ICT 351", "ICT 381", "ICS 341", "ICT 372", "ICT 361"};
    String[] students = {"Edwin", "Chatumba", "Nissi", "Mapalo", "Valentine", "Racheal", "Kafiswe", "Adon", "Beatrice"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gradebook);

        // Enable back arrow and set title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Gradebook");
        }

        Spinner spinner = findViewById(R.id.spinnerCourse);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, courses);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        ListView listGrades = findViewById(R.id.listGrades);
        ArrayAdapter<String> studentAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                students
        );
        listGrades.setAdapter(studentAdapter);

        Button btnExport = findViewById(R.id.btnExportCSV);
        btnExport.setOnClickListener(v ->
                Toast.makeText(GradebookActivity.this,
                        "Grades exported to CSV",
                        Toast.LENGTH_SHORT).show()
        );
    }

    // Handle back button click
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // go back to CourseListActivity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
