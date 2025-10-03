package com.example.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;
import java.util.List;

public class RollCallActivity extends AppCompatActivity {

    private ArrayList<Student> studentList;
    private StudentAttendanceAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roll_call);

        TextView tvRollCallTitle = findViewById(R.id.tvRollCallTitle);
        ListView lvStudents = findViewById(R.id.lvStudents);
        Button btnSaveAttendance = findViewById(R.id.btnSaveAttendance);
        Button btnExportAttendance = findViewById(R.id.btnExportAttendance);

        String courseCode = getIntent().getStringExtra("COURSE_CODE");
        tvRollCallTitle.setText("Roll Call for " + courseCode);

        // Create a sample list of students
        studentList = new ArrayList<>();
        studentList.add(new Student("Edwin Mwansa"));
        studentList.add(new Student("Adon Chinyamuka"));
        studentList.add(new Student("Racheal Sililo"));
        studentList.add(new Student("Kafiswe Chimputu"));
        studentList.add(new Student("Elijah Manda"));
        studentList.add(new Student("Beatrice Mulenga"));
        studentList.add(new Student("Chatumba Mwanza"));
        studentList.add(new Student("Mapalo Sanika"));
        studentList.add(new Student("Nissi Masobano"));
        studentList.add(new Student("Valentine Musonda"));

        adapter = new StudentAttendanceAdapter(this, studentList);
        lvStudents.setAdapter(adapter);

        // **MODIFIED**: The save button now calls a confirmation method first
        btnSaveAttendance.setOnClickListener(v -> confirmAndSaveAttendance());
        btnExportAttendance.setOnClickListener(v -> exportAttendance());
    }

    /**
     * Checks for absent students and shows a warning if any are found.
     */
    private void confirmAndSaveAttendance() {
        int absentCount = 0;
        for (Student student : studentList) {
            if (!student.isPresent()) {
                absentCount++;
            }
        }

        // If students are missing, show the warning dialog
        if (absentCount > 0) {
            new AlertDialog.Builder(this)
                    .setTitle("Confirm Attendance")
                    .setMessage("You have not marked " + absentCount + " student(s) as present. Are you sure you want to save?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("Continue & Save", (dialog, which) -> {
                        // If the user clicks "Continue", then save.
                        saveAttendance();
                    })
                    .setNegativeButton("Cancel", null) // "Cancel" does nothing, just dismisses the dialog.
                    .show();
        } else {
            // If everyone is present, save immediately without a warning.
            saveAttendance();
        }
    }

    /**
     * This method contains the original save logic. It's now called only after confirmation.
     */
    private void saveAttendance() {
        int presentCount = 0;
        for (Student student : studentList) {
            if (student.isPresent()) {
                presentCount++;
            }
        }
        String message = "Attendance saved: " + presentCount + " of " + studentList.size() + " students present.";
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        finish(); // Go back to the main screen
    }

    private void exportAttendance() {
        StringBuilder csvData = new StringBuilder();
        csvData.append("Student Name,Present\n");
        for (Student student : studentList) {
            csvData.append(student.getName()).append(",").append(student.isPresent()).append("\n");
        }
        Toast.makeText(this, "Exporting attendance data...", Toast.LENGTH_SHORT).show();
        System.out.println("--- CSV EXPORT ---");
        System.out.println(csvData.toString());
        System.out.println("------------------");
    }
}

// --- Data Model and Custom Adapter (No changes needed here) ---

class Student {
    private String name;
    private boolean isPresent;

    public Student(String name) {
        this.name = name;
        this.isPresent = false;
    }

    public String getName() { return name; }
    public boolean isPresent() { return isPresent; }
    public void setPresent(boolean present) { isPresent = present; }
}

class StudentAttendanceAdapter extends ArrayAdapter<Student> {
    public StudentAttendanceAdapter(Context context, List<Student> students) {
        super(context, 0, students);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_student, parent, false);
        }

        Student student = getItem(position);

        TextView tvStudentName = convertView.findViewById(R.id.tvStudentName);
        SwitchMaterial swPresent = convertView.findViewById(R.id.swPresent);

        if (student != null) {
            tvStudentName.setText(student.getName());
            swPresent.setChecked(student.isPresent());
            // **MODIFIED**: Set a click listener for the switch to update the student's data
            convertView.setOnClickListener(v -> {
                // Toggle the switch's visual state
                swPresent.setChecked(!swPresent.isChecked());
                // Update the actual data for the student
                student.setPresent(swPresent.isChecked());
            });
        }

        return convertView;
    }
}