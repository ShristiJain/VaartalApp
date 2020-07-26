package com.example.vartalapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatsFragment extends Fragment {
    private View chatView;
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_groups;
    private DatabaseReference groupRef;

    public ChatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        chatView= inflater.inflate(R.layout.fragment_chats, container, false);
        groupRef = FirebaseDatabase.getInstance().getReference().child("groups");
        listView=chatView.findViewById(R.id.chats_list_view);
        list_of_groups=new ArrayList<String>();
        list_of_groups.add("Start your chat here");
        arrayAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,list_of_groups);
        listView.setAdapter(arrayAdapter);

        retrieveAndDisplayGroups();

        return chatView;
    }

    private void retrieveAndDisplayGroups() {
        groupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Set<String> set=new HashSet<>();
                Iterator iterator=snapshot.getChildren().iterator();
                while (iterator.hasNext()){
                    set.add(((DataSnapshot)iterator.next()).getKey());
                }
                list_of_groups.clear();
                list_of_groups.addAll(set);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
