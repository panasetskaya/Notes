package com.example.notes2;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

public class AddNewNoteActivity extends AppCompatActivity {

    private EditText editTextTitle;
    private EditText editTextDesc;
    private RadioGroup radioGroupPr;
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private RadioButton radioButton3;
    private Spinner spinnerDayWeek;
    private int priority;
    private String title;
    private String description;
    private int day;
    private int radioButton1id;
    private int radioButton2id;
    private int radioButton3id;

    private NotesDBHelper dbHelper;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_note);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null) {
            actionBar.hide();
        }
        dbHelper = new NotesDBHelper(this);
        database = dbHelper.getWritableDatabase();
        editTextDesc = findViewById(R.id.editTextNewDesc);
        editTextTitle = findViewById(R.id.editTextNewTitle);
        spinnerDayWeek = findViewById(R.id.spinnerDayWeek);
        radioGroupPr = findViewById(R.id.RadioGroupPriorities);
        radioButton1 = findViewById(R.id.radioButton1);
        radioButton1id = radioButton1.getId();
        radioButton2 = findViewById(R.id.radioButton2);
        radioButton2id = radioButton2.getId();
        radioButton3 = findViewById(R.id.radioButton3);
        radioButton3id = radioButton3.getId();

    }

    public void onClickConfirmNewNote(View view) {
        title = editTextTitle.getText().toString().trim();
        description = editTextDesc.getText().toString().trim();
        day = spinnerDayWeek.getSelectedItemPosition();
        int checkedRadioButtonId = radioGroupPr.getCheckedRadioButtonId();
        int a = radioButton1id;
        int b = radioButton2id;
        if (checkedRadioButtonId==a) {
            priority = 1;
        } else if (checkedRadioButtonId==b) {
            priority = 2;
        } else {
            priority = 3;
        }
        if (isFilled(title,description)) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(NotesContract.NotesEntry.COLUMN_TITLE, title);
            contentValues.put(NotesContract.NotesEntry.COLUMN_DESCRIPTION, description);
            contentValues.put(NotesContract.NotesEntry.COLUMN_DAY_OF_WEEK, day+1);
            contentValues.put(NotesContract.NotesEntry.COLUMN_PRIORITY, priority);
            database.insert(NotesContract.NotesEntry.TABLE_NAME, null, contentValues);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, R.string.warning_empty_fields, Toast.LENGTH_LONG).show();
        }
    }

    private boolean isFilled(String title, String description) {
        return !title.isEmpty() && !description.isEmpty();
    }
}