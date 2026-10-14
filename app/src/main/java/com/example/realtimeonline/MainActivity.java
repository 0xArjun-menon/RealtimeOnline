package com.example.realtimeonline;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;

    private EditText usernameInput, phoneInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameInput = findViewById(R.id.editTextText2);
        phoneInput = findViewById(R.id.editTextText);
        Button saveButton = findViewById(R.id.button);
        Button retrieveButton = findViewById(R.id.button2);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("StudentData");

        // Save button
        saveButton.setOnClickListener(v -> {String username = usernameInput.getText().toString().trim();String phone = phoneInput.getText().toString().trim();

            if (username.isEmpty() || phone.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                return;
            }
            addDataToFirebase(username, phone);
        });
        retrieveButton.setOnClickListener(v -> {String username = usernameInput.getText().toString().trim();
            if (username.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please enter a username", Toast.LENGTH_SHORT).show();
                return;
            }

            retrieveDataFromFirebase(username);
        });
    }
    private void addDataToFirebase(String username, String phone) {
        person newPerson = new person(username, phone);

        databaseReference.child(username).setValue(newPerson)
                .addOnSuccessListener(unused -> Toast.makeText(MainActivity.this, "Data saved successfully", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(MainActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show());
    }
    private void retrieveDataFromFirebase(String username) {
        databaseReference.child(username)
                .addListenerForSingleValueEvent(
                        new ValueEventListener() {

                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    person savedPerson = snapshot.getValue(person.class);

                                    if (savedPerson != null) {
                                        usernameInput.setText(savedPerson.getName());
                                        phoneInput.setText(savedPerson.getNumber());

                                        Toast.makeText(MainActivity.this, "Data retrieved successfully", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(MainActivity.this, "No data found", Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {Toast.makeText(MainActivity.this, "Retrieval failed: " + error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
    }
}
