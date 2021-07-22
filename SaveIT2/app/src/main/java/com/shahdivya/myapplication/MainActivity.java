package com.shahdivya.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Instrumentation;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    ImageButton imageButton;
    ArrayList<Note> notes;
    RecyclerView recyclerView;
    NoteAdapter noteAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageButton = findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater =(LayoutInflater) MainActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.note_input,null,false);
                final EditText editTitle = view.findViewById(R.id.title1);
                final EditText editDesp = view.findViewById(R.id.description1);
                new AlertDialog.Builder(MainActivity.this)
                        .setView(view)
                        .setTitle("Add Notes")
                        .setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String title = editTitle.getText().toString();
                                String description = editDesp.getText().toString();
                                Note note = new Note(title,description);
                                boolean is = new NoteMethods(MainActivity.this).create(note);
                                if (is)
                                {
                                    Toast.makeText(MainActivity.this,"Saved",Toast.LENGTH_SHORT).show();
                                    load();
                                }else
                                {
                                    Toast.makeText(MainActivity.this,"Not Saved",Toast.LENGTH_SHORT).show();
                                }
                                load();
                            }
                        }).show();
            }
        });
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target)
            {
                return false;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction)
            {
                new NoteMethods(MainActivity.this).delete(notes.get(viewHolder.getAdapterPosition()).getId());
                notes.remove(viewHolder.getAdapterPosition());
                noteAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        load();
    }

    public ArrayList<Note> readNotes()
    {
        return new NoteMethods(this).readNotes();
    }

    public void load()
    {
        notes = readNotes();
        noteAdapter = new NoteAdapter(notes, this, new ItemClicked() {
            @Override
            public void onClick(int position, View view) {
                editNote(notes.get(position).getId(),view);
            }
        });
        recyclerView.setAdapter(noteAdapter);
    }

    public void editNote(int noteId, View view)
    {
        NoteMethods noteMethods = new NoteMethods(this);
        Note note = noteMethods.readSingleNote(noteId);
        Intent intent = new Intent(this,EditActivity.class);
        intent.putExtra("title",note.getTitle());
        intent.putExtra("description",note.getDescription());
        intent.putExtra("id",note.getId());

        //ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this,view, Objects.requireNonNull(ViewCompat.getTransitionName(view)));
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1)
        {
            load();
        }
    }
}