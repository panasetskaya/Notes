package com.example.notes2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerViewNotes;
    private final ArrayList<Note> notes = new ArrayList<>();
    private NotesAdapter adapter1;
    private NotesDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null) {
            actionBar.hide();
        }
        recyclerViewNotes = findViewById(R.id.RecyclerViewNotes);
        dbHelper = new NotesDBHelper(this);
        if (!isRegister()) {
            Toast.makeText(this, "Здесь будут ваши заметки", Toast.LENGTH_LONG).show();
        } else {
            SQLiteDatabase database = dbHelper.getWritableDatabase();
            getData(database);
            adapter1 = new NotesAdapter(notes);
            recyclerViewNotes.setLayoutManager(new LinearLayoutManager(this));
            recyclerViewNotes.setAdapter(adapter1);
            adapter1.setOnNoteClickListener(new NotesAdapter.OnNoteClickListener() {
                @Override
                public void onNoteClick(int position) {
                    Toast.makeText(MainActivity.this, "Short click", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onLongClick(int position) {
                    Toast.makeText(MainActivity.this, "Long click", Toast.LENGTH_LONG).show();
                }
            });
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    removeItem(viewHolder.getAdapterPosition(), adapter1, database);
                }
            });
            itemTouchHelper.attachToRecyclerView(recyclerViewNotes);
        }
    }

    private void removeItem(int position, NotesAdapter adapter, SQLiteDatabase database) {
        int id = notes.get(position).getId();
        String[] whereArgs = new String[] {Integer.toString(id)};
        String where = NotesContract.NotesEntry._ID + " = ?";
        database.delete(NotesContract.NotesEntry.TABLE_NAME, where, whereArgs);
        getData(database);
        adapter.notifyDataSetChanged();
    }

    public boolean isRegister() {
        try (SQLiteDatabase db = dbHelper.getReadableDatabase()) {
            return (DatabaseUtils.queryNumEntries(db, NotesContract.NotesEntry.TABLE_NAME) != 0);
        }
    }

    public void onClickAddNote(View view) {
        Intent intent = new Intent(this,AddNewNoteActivity.class);
        startActivity(intent);
    }

    private void getData(SQLiteDatabase database) {
        notes.clear();
        Cursor cursor = database.query(NotesContract.NotesEntry.TABLE_NAME,null,null,null,null,null, NotesContract.NotesEntry.COLUMN_PRIORITY);
        while (cursor.moveToNext()) {
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(NotesContract.NotesEntry._ID));
            @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(NotesContract.NotesEntry.COLUMN_TITLE));
            @SuppressLint("Range") String desc = cursor.getString(cursor.getColumnIndex(NotesContract.NotesEntry.COLUMN_DESCRIPTION));
            @SuppressLint("Range") int dayOfWeek = cursor.getInt(cursor.getColumnIndex(NotesContract.NotesEntry.COLUMN_DAY_OF_WEEK));
            @SuppressLint("Range") int priority = cursor.getInt(cursor.getColumnIndex(NotesContract.NotesEntry.COLUMN_PRIORITY));
            Note note = new Note(id, title,desc,dayOfWeek,priority);
            notes.add(note);
        }
        cursor.close();
    }
}