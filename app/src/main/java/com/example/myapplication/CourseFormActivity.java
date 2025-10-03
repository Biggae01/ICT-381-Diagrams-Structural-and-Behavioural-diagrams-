package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;

public class CourseFormActivity extends AppCompatActivity {

    public static final String EXTRA_MODE = "EXTRA_MODE";
    public static final String EXTRA_COURSE_CODE = "EXTRA_COURSE_CODE";
    public static final String EXTRA_COURSE_NAME = "EXTRA_COURSE_NAME";
    public static final String MODE_ADD = "MODE_ADD";
    public static final String MODE_EDIT = "MODE_EDIT";

    private TextInputEditText etCourseCode, etCourseName;
    private String currentMode;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_form);

        etCourseCode = findViewById(R.id.etCourseCode);
        etCourseName = findViewById(R.id.etCourseName);
        TextView tvFormTitle = findViewById(R.id.tvFormTitle);
        Button btnSaveCourse = findViewById(R.id.btnSaveCourse);

        Intent intent = getIntent();
        currentMode = intent.getStringExtra(EXTRA_MODE);

        if (MODE_EDIT.equals(currentMode)) {
            tvFormTitle.setText("Edit Course");
            String code = intent.getStringExtra(EXTRA_COURSE_CODE);
            String name = intent.getStringExtra(EXTRA_COURSE_NAME);
            etCourseCode.setText(code);
            etCourseName.setText(name);
        } else {
            tvFormTitle.setText("Add New Course");
        }

        btnSaveCourse.setOnClickListener(v -> saveCourse());
    }

    private void saveCourse() {
        String code = etCourseCode.getText().toString().trim();
        String name = etCourseName.getText().toString().trim();

        if (code.isEmpty() || name.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // In a real app, you would save this data to a database.
        // For now, we'll just show a confirmation toast.
        String message = MODE_EDIT.equals(currentMode) ? "Course updated: " : "Course added: ";
        Toast.makeText(this, message + code, Toast.LENGTH_LONG).show();

        finish(); // Close the activity and return to the main screen
    }
}