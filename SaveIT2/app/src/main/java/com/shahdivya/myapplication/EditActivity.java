package com.shahdivya.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class EditActivity extends AppCompatActivity {

    EditText editTitle,editDescribe;
    Button btncancel,btnsave;
    LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        final Intent intent = getIntent();
        linearLayout = findViewById(R.id.btn_holder);
        editTitle = findViewById(R.id.edit_title);
        editDescribe = findViewById(R.id.edit_description);
        btncancel = findViewById(R.id.cancelBtn);
        btnsave = findViewById(R.id.saveBtn);
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                TransitionManager.beginDelayedTransition(linearLayout);
                btnsave.setVisibility(View.GONE);
                btncancel.setVisibility(View.GONE);
            }
        });
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Note note = new Note(editTitle.getText().toString(),editDescribe.getText().toString());
                note.setId(intent.getIntExtra("id",1));
                if (new NoteMethods(EditActivity.this).update(note))
                {
                    Toast.makeText(EditActivity.this,"Updated",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(EditActivity.this,"Not Updated",Toast.LENGTH_SHORT).show();
                }
                onBackPressed();
                TransitionManager.beginDelayedTransition(linearLayout);
                btnsave.setVisibility(View.GONE);
                btncancel.setVisibility(View.GONE);
            }
        });
        editDescribe.setText(intent.getStringExtra("description"));
        editTitle.setText(intent.getStringExtra("title"));
    }
}