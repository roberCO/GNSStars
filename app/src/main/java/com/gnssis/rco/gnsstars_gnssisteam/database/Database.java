package com.gnssis.rco.gnsstars_gnssisteam.database;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gnssis.rco.gnsstars_gnssisteam.entity.Message;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

public class Database {

    private DatabaseReference databaseReference;

    public Database() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("messages");
        createListenerMethods(databaseReference);
    }

    private void createListenerMethods(DatabaseReference databaseReference) {
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                Message newMessage = dataSnapshot.getValue(Message.class);
                System.out.println("Message: " + newMessage.toString());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                GenericTypeIndicator<HashMap<String, Object>> genericTypeIndicator = new GenericTypeIndicator<HashMap<String, Object>>(){};
                HashMap<String, Object> messageList = dataSnapshot.getValue(genericTypeIndicator);

                for (Object o : messageList.values()) {
                    System.out.println(o.toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    public void saveMessage(Message message) {
        databaseReference.push().setValue(message);
    }
}
