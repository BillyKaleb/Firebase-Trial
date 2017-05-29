package com.kuliah.firebase.tesfiredb;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView onceSync;
    TextView updateSync;
    Button selectButton;
    Button signOuttButton;
    Button clearButton;
    Button updateButton;
    Button addButton;
    Button deleteButton;
    EditText nama;
    EditText alamatText;
    private static final int SIGN_IN_REQUEST_CODE = 1;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        onceSync = (TextView) findViewById(R.id.teksSyncOnce);
        updateSync = (TextView) findViewById(R.id.teksSync);

        selectButton = (Button) findViewById(R.id.btselect);
        selectButton.setOnClickListener(this);

        addButton = (Button) findViewById(R.id.btAdd);
        addButton.setOnClickListener(this);

        clearButton = (Button) findViewById(R.id.btClear);
        clearButton.setOnClickListener(this);
        /// listener
        updateButton = (Button) findViewById(R.id.btUpdate);
        updateButton.setOnClickListener(this);
        /// listener

        signOuttButton = (Button) findViewById(R.id.btSignOut);
        signOuttButton.setOnClickListener(this);
        /// listener

        deleteButton = (Button) findViewById(R.id.btDelete);
        deleteButton.setOnClickListener(this);
        // listener

        nama = (EditText) findViewById(R.id.namaText);
        alamatText = (EditText) findViewById(R.id.alamatText);


        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            // Start sign in/sign up activity
            startActivityForResult(
                    AuthUI.getInstance().createSignInIntentBuilder().setProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                            new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                            new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build(),
                            new AuthUI.IdpConfig.Builder(AuthUI.TWITTER_PROVIDER).build())).build(),
                    SIGN_IN_REQUEST_CODE
            );
        } else {
            // User is already signed in. Therefore, display
            // a welcome Toast

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            FirebaseDatabase.getInstance().getReference("anggota").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    onceSync.setText("");
                    //  Anggota temp = dataSnapshot.getValue(Anggota.class);
                    //  updateSync.append(temp.getNama()+ " : "+temp.getAlamat());
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Anggota post = postSnapshot.getValue(Anggota.class);
                        updateSync.append(post.getNama() + " : " + post.getAlamat() + " : " + postSnapshot.getKey() + " \n ");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }


            });


            Toast.makeText(this,
                    "Welcome " + FirebaseAuth.getInstance()
                            .getCurrentUser()
                            .getDisplayName(),
                    Toast.LENGTH_LONG)
                    .show();


        }


    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_IN_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                nama.setText(user.getDisplayName());
                Toast.makeText(this,
                        "Successfully signed in. Welcome!",
                        Toast.LENGTH_LONG)
                        .show();

                FirebaseDatabase.getInstance().getReference("anggota").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        onceSync.setText("");
                        //Anggota temp = dataSnapshot.getValue(Anggota.class);
                        //updateSync.setText(temp.getNama()+ " : "+temp.getAlamat());

                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Anggota post = postSnapshot.getValue(Anggota.class);
                            updateSync.append(post.getNama() + " : " + post.getAlamat() + " : " + postSnapshot.getKey() + " \n ");
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }


                });

            } else {
                Toast.makeText(this,
                        "We couldn't sign you in. Please try again later.",
                        Toast.LENGTH_LONG)
                        .show();
                finish();
            }
        }
    }


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btSignOut) {
            AuthUI.getInstance().signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(MainActivity.this,
                                    "You have been signed out.",
                                    Toast.LENGTH_LONG)
                                    .show();

                            // Close activity
                            finish();
                        }
                    });

        } else if (v.getId() == R.id.btDelete) {
            mDatabase = FirebaseDatabase.getInstance().getReference();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            mDatabase.child("anggota").child(user.getUid()).removeValue();

            Toast.makeText(MainActivity.this,
                    "DELETE DATA",
                    Toast.LENGTH_LONG)
                    .show();

        } else if (v.getId() == R.id.btAdd) {
            Anggota baru = new Anggota(nama.getText().toString(), alamatText.getText().toString());
            mDatabase = FirebaseDatabase.getInstance().getReference();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            mDatabase.child("anggota").child(user.getUid()).setValue(baru);

            Toast.makeText(MainActivity.this,
                    "ADD DATA",
                    Toast.LENGTH_LONG)
                    .show();

        } else if (v.getId() == R.id.btClear) {
            onceSync.setText("");
            updateSync.setText("");

        } else if (v.getId() == R.id.btUpdate) {
            Anggota baru = new Anggota(nama.getText().toString(), alamatText.getText().toString());
            mDatabase = FirebaseDatabase.getInstance().getReference();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            mDatabase.child("anggota").child(user.getUid()).setValue(baru);

            Toast.makeText(MainActivity.this,
                    "update DATA",
                    Toast.LENGTH_LONG)
                    .show();

        } else if (v.getId() == R.id.btselect) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            FirebaseDatabase.getInstance().getReference("anggota").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    //get single data
                    // Anggota temp = dataSnapshot.getValue(Anggota.class);
                    // onceSync.setText(temp.getNama()+ " : "+temp.getAlamat());

                    //listOf data
                    onceSync.setText("");
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Anggota post = postSnapshot.getValue(Anggota.class);
                        onceSync.append(post.getNama() + " : " + post.getAlamat() + " : " + postSnapshot.getKey() + " \n ");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }


            });

        }


    }
}
