package com.project.stsfaculty.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.stsfaculty.Adapters.PDFAdapter;
import com.project.stsfaculty.Model.DocumentModel;
import com.project.stsfaculty.R;

import java.util.ArrayList;
import java.util.List;

public class Fragment1 extends Fragment {

    public RecyclerView recyclerView;
    public PDFAdapter adapter;

    public TextView noDocumentsTextView;

    public List<DocumentModel> modelList;

    public FirebaseDatabase database;
    public FirebaseAuth auth;
    public FirebaseUser user;

    public String HODEmail;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment1, container, false);

        modelList = new ArrayList<>();

        noDocumentsTextView = view.findViewById(R.id.noDocumentsTextView_id);

        recyclerView = view.findViewById(R.id.recyclerview_id);
        adapter = new PDFAdapter(modelList, getContext());

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        // Getting data from firebase
        database.getReference().child("HODOFFACULTY").child(user.getEmail().replace(".",""))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        HODEmail = snapshot.getValue(String.class);

                        setData(HODEmail);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        return view;
    }

    public void setData(String HODEmail){
        database.getReference().child("PDFs").child(HODEmail.replace(".",""))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        modelList.clear();
                        if(snapshot.hasChildren())
                        {
                            for(DataSnapshot snapshot1: snapshot.getChildren())
                            {
                                DocumentModel model = new DocumentModel();
                                model = snapshot1.getValue(DocumentModel.class);

                                modelList.add(model);
                            }
                            noDocumentsTextView.setVisibility(View.INVISIBLE);
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}
