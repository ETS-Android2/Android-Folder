package com.shahdivya.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class NoteMethods extends DatabaseHelper {
    public NoteMethods(Context context)
    {
        super(context);
    }
    public boolean create(Note note)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put("title",note.getTitle());
        contentValues.put("description",note.getDescription());
        SQLiteDatabase db = this.getWritableDatabase();
        boolean success = db.insert("Note",null,contentValues)>0;
        db.close();
        return success;
    }
    public ArrayList<Note> readNotes()
    {
        ArrayList<Note> notes = new ArrayList<>();
        String sQuery = "SELECT * FROM Note ORDER BY id ASC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sQuery,null);
        if (cursor.moveToFirst())
        {
            do {
                Note note;
                int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
                Log.i("hello",cursor.getString(cursor.getColumnIndex("id")));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String description = cursor.getString(cursor.getColumnIndex("description"));
                note = new Note(title,description);
                note.setId(id);
                notes.add(note);
            }while (cursor.moveToNext());
            cursor.close();
            db.close();
        }
        return notes;
    }
    public Note readSingleNote(int id)
    {
        Note note = null;
        String sQuery = "SELECT * FROM Note WHERE id ="+id;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(sQuery,null);
        if (cursor.moveToFirst())
        {
            int noteid = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
            Log.i("hello",cursor.getString(cursor.getColumnIndex("id")));
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String description = cursor.getString(cursor.getColumnIndex("description"));
            note = new Note(title,description);
            note.setId(noteid);
        }
        cursor.close();
        database.close();
        return note;
    }
    public boolean update(Note note)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put("title",note.getTitle());
        contentValues.put("description",note.getDescription());
        SQLiteDatabase database = this.getWritableDatabase();
        boolean update = database.update("Note",contentValues,"id ='"+note.getId()+"'",null)>0;
        database.close();
        return update;
    }
    public boolean delete(int id)
    {
        SQLiteDatabase database = this.getWritableDatabase();
        boolean delete = database.delete("Note","id ='"+id+"'",null)>0;
        database.close();
        return delete;
    }
}
