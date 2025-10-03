package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private RecyclerView rvCourses;
    private CourseAdapter courseAdapter;
    private ArrayList<Course> courseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Toolbar Setup
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // 2. New Course Button Setup
        MaterialButton btnNewCourse = findViewById(R.id.btnNewCourse);
        btnNewCourse.setOnClickListener(v -> {
            Intent intent = new Intent(this, CourseFormActivity.class);
            intent.putExtra(CourseFormActivity.EXTRA_MODE, CourseFormActivity.MODE_ADD);
            startActivity(intent);
        });

        // 3. Spinner Setup
        Spinner courseSpinner = findViewById(R.id.spinnerCourses);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.courses_array, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseSpinner.setAdapter(spinnerAdapter);
        courseSpinner.setOnItemSelectedListener(this);

        // 4. RecyclerView Setup
        rvCourses = findViewById(R.id.rvCourses);
        rvCourses.setLayoutManager(new LinearLayoutManager(this));

        loadCourseData();

        courseAdapter = new CourseAdapter(this, courseList);
        rvCourses.setAdapter(courseAdapter);
    }

    private void loadCourseData() {
        courseList = new ArrayList<>();
        courseList.add(new Course("CS101", "Intro to Computer Science", "45 students", "0 sessions", "87%"));
        courseList.add(new Course("MATH201", "Calculus II", "32 students", "12 sessions", "92%"));
        courseList.add(new Course("ENG102", "Academic Writing", "28 students", "6 sessions", "78%"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_gradebook) {
            Toast.makeText(this, "Opening Global Gradebook...", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, GradebookActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position > 0) {
            String selectedCourse = parent.getItemAtPosition(position).toString();
            Toast.makeText(this, "Selected: " + selectedCourse, Toast.LENGTH_SHORT).show();
            for (int i = 0; i < courseList.size(); i++) {
                if (courseList.get(i).getCode().equals(selectedCourse)) {
                    rvCourses.smoothScrollToPosition(i);
                    break;
                }
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) { }
}

class Course {
    private String code, name, studentCount, sessionCount, percentage;
    public Course(String code, String name, String studentCount, String sessionCount, String percentage) {
        this.code = code; this.name = name; this.studentCount = studentCount; this.sessionCount = sessionCount; this.percentage = percentage;
    }
    public String getCode() { return code; }
    public String getName() { return name; }
    public String getStudentCount() { return studentCount; }
    public String getSessionCount() { return sessionCount; }
    public String getPercentage() { return percentage; }
}

class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {
    private Context context;
    private List<Course> courseList;

    public CourseAdapter(Context context, List<Course> courseList) {
        this.context = context;
        this.courseList = courseList;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_course, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Course course = courseList.get(position);
        holder.tvCourseCode.setText(course.getCode());
        holder.tvCourseName.setText(course.getName());
        holder.tvStudents.setText(course.getStudentCount());
        holder.tvSessions.setText(course.getSessionCount());
        holder.tvPercentage.setText(course.getPercentage());

        holder.btnRollCall.setOnClickListener(v -> {
            Intent intent = new Intent(context, RollCallActivity.class);
            intent.putExtra("COURSE_CODE", course.getCode());
            context.startActivity(intent);
        });
        holder.btnEditCourse.setOnClickListener(v -> {
            Intent intent = new Intent(context, CourseFormActivity.class);
            intent.putExtra(CourseFormActivity.EXTRA_MODE, CourseFormActivity.MODE_EDIT);
            intent.putExtra(CourseFormActivity.EXTRA_COURSE_CODE, course.getCode());
            intent.putExtra(CourseFormActivity.EXTRA_COURSE_NAME, course.getName());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public static class CourseViewHolder extends RecyclerView.ViewHolder {
        TextView tvCourseCode, tvCourseName, tvStudents, tvSessions, tvPercentage;
        ImageButton btnEditCourse;
        Button btnRollCall;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCourseCode = itemView.findViewById(R.id.tvCourseCode);
            tvCourseName = itemView.findViewById(R.id.tvCourseName);
            tvStudents = itemView.findViewById(R.id.tvStudents);
            tvSessions = itemView.findViewById(R.id.tvSessions);
            tvPercentage = itemView.findViewById(R.id.tvPercentage);
            btnEditCourse = itemView.findViewById(R.id.btnEditCourse);
            btnRollCall = itemView.findViewById(R.id.btnRollCall);
        }
    }
}