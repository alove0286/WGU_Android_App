package com.loveapps.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.loveapps.model.Course;

import com.loveapps.R;
import com.loveapps.model.DAO;
import com.loveapps.model.Term;

import java.util.List;

import static com.loveapps.R.id.listView;

public class CoursesActivity extends AppCompatActivity {

    private static final int EDITOR_REQUEST_CODE = 1001;
    private DAO dao;
    private List<Course> courses;
    private ArrayAdapter<Course> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long id = -1;
                Intent intent = new Intent(CoursesActivity.this, EditCourseActivity.class);
                intent.putExtra(DAO.CONTENT_ITEM_TYPE, id);
                intent.putExtra("term", getTermIDFromContext());
                startActivityForResult(intent, EDITOR_REQUEST_CODE);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Log.i("logging", "courses: ID is  " + getTermIDFromContext() );

        dao = new DAO(this);
        dao.open();
        String action = Intent.ACTION_EDIT;
        setTitle("Courses for " + dao.getTermById(getTermIDFromContext()).getTerm());
        dao.close();

        refreshDisplay();
    }

    private long getTermIDFromContext() {
        Intent intent = getIntent();
        return intent.getLongExtra(DAO.CONTENT_ITEM_TYPE, -1L);
    }

    private void refreshDisplay() {

        dao.open();
        courses = dao.findAllCourses(getTermIDFromContext());
        adapter = new ArrayAdapter<Course>(this,
                android.R.layout.simple_expandable_list_item_1,
                android.R.id.text1, courses);

        ListView listview = (ListView) findViewById(listView);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CoursesActivity.this, EditCourseActivity.class);
                Course course = (Course) parent.getItemAtPosition(position);
                intent.putExtra(DAO.CONTENT_ITEM_TYPE, course.getId());
                intent.putExtra("term", getTermIDFromContext());

                startActivityForResult(intent, EDITOR_REQUEST_CODE);
            }
        });

        dao.close();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        refreshDisplay();
    }

}
