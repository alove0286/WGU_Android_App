package com.loveapps.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.loveapps.model.Course;
import com.loveapps.model.DAO;
import com.loveapps.model.Note;
import com.loveapps.R;

import java.util.List;

import static com.loveapps.R.id.listView;

public class NotesActivity extends AppCompatActivity {

    private static final int EDITOR_REQUEST_CODE = 1001;
    private DAO dao;
    private List<Note> notes;
    private ArrayAdapter<Note> adapter;
    private ListView lv;
    private Course courseEdit;
    private long courseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();

        courseId = intent.getLongExtra(DAO.CONTENT_ITEM_TYPE, -1L);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long id = -1;
                Intent getNoteScreenIntent = new Intent(NotesActivity.this, EditNoteActivity.class);
                getNoteScreenIntent.putExtra(DAO.CONTENT_ITEM_TYPE, id);
                getNoteScreenIntent.putExtra("course", courseId);
                startActivityForResult(getNoteScreenIntent, EDITOR_REQUEST_CODE);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        dao = new DAO(this);
        dao.open();



        courseEdit = dao.getCourseById(courseId);
        setTitle("Notes for " + courseEdit.getCourse());

        lv = (ListView) findViewById(listView);

        refreshDisplay();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(NotesActivity.this, EditNoteActivity.class);
                Note note = (Note) parent.getItemAtPosition(position);
                intent.putExtra(DAO.CONTENT_ITEM_TYPE, note.getId());
                intent.putExtra("course", courseId);
                startActivityForResult(intent, EDITOR_REQUEST_CODE);
            }
        });



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent();
                intent.putExtra(DAO.CONTENT_ITEM_TYPE, courseEdit.getId());
                intent.putExtra("term", courseEdit.getTermId());
                setResult(RESULT_OK, intent);
                finish();
                return(true);
        }

        return(super.onOptionsItemSelected(item));
    }

    private void refreshDisplay() {
        notes = dao.findAllNotes(courseId);
        adapter = new ArrayAdapter<Note>(this,
                android.R.layout.simple_expandable_list_item_1,
                android.R.id.text1, notes);

        lv.setAdapter(adapter);


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        refreshDisplay();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(DAO.CONTENT_ITEM_TYPE, courseEdit.getId());
        intent.putExtra("term", courseEdit.getTermId());
        setResult(RESULT_OK, intent);
        finish();
    }
}
