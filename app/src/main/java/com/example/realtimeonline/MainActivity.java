
package com.example.realtimeonline;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    EditText usernameInput, phoneInput;
    Button saveButton, retrieveButton;
    person person;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        usernameInput = findViewById(R.id.editTextText2);
        phoneInput = findViewById(R.id.editTextText);
        saveButton = findViewById(R.id.button);
        retrieveButton = findViewById(R.id.button2);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference =
                firebaseDatabase.getReference("StudentData");
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username =
                        usernameInput.getText().toString().trim();

                String phone =
                        phoneInput.getText().toString().trim();
                if (username.isEmpty() || phone.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                addDataToFirebase(username, phone);
            }
        });
        retrieveButton.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "Enter the username to retrieve its data", Toast.LENGTH_SHORT).show();
        });
    }
    private void addDataToFirebase(String username, String phone) {
        person = new person(username, phone);

        databaseReference.push().setValue(person)
                .addOnSuccessListener(unused -> {Toast.makeText(MainActivity.this, "Data saved successfully", Toast.LENGTH_SHORT).show();
                    usernameInput.setText("");
                    phoneInput.setText("");
                })
                .addOnFailureListener(e -> Toast.makeText(MainActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show()
                );
    }


    }

