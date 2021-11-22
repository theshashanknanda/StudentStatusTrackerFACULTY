package com.project.stsfaculty.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.project.stsfaculty.Model.DocumentModel;
import com.project.stsfaculty.R;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import papaya.in.sendmail.SendMail;

public class Fragment2 extends Fragment {

    public Spinner enrollmentSpinner;
    public Spinner documentTypeSpinner;
    public Button uploadPDFButton;
    public EditText nameEditText, emailEditText, noteForStudent;

    public FirebaseDatabase database;
    public FirebaseAuth auth;
    public FirebaseUser user;
    public StorageReference storage;

    public String[] enrollmentNumbers =
            {"206330316001","206330316002","206330316003","206330316004","206330316005","206330316006","206330316007","206330316008","206330316009",
                    "206330316010","206330316011","206330316012","206330316013","206330316014","206330316015","206330316016","206330316017","206330316018","206330316019","206330316020","206330316021",
                    "206330316022","206330316023","206330316024","206330316025","206330316026","206330316027","206330316028","206330316029","206330316030","206330316031","206330316032","206330316033",
                    "206330316034","206330316035","206330316036","206330316037","206330316038","206330316039","206330316040","206330316041","206330316042","206330316043","206330316044","206330316045",
                    "206330316046","206330316047","206330316048","206330316049","206330316050","206330316051","206330316052","206330316053","206330316054","206330316055","206330316056","206330316057",
                    "206330316058","206330316059","206330316060","206330316061","206330316062","206330316063","206330316064","206330316065","206330316066","206330316067"};

    public String[] documentType = {"AC", "Rejoin", "Transfer", "Detain"};

    public String name, email, enrollment, documenttype, url, note;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment2, container, false);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        storage = FirebaseStorage.getInstance().getReference();

        enrollmentSpinner = view.findViewById(R.id.enrollmentSpinner_id);
        documentTypeSpinner = view.findViewById(R.id.documentTypeSpinner_id);
        uploadPDFButton = view.findViewById(R.id.uploadPDFButton);
        nameEditText = view.findViewById(R.id.studentNameEditText_id);
        emailEditText = view.findViewById(R.id.studentEmailEditText_id);
        noteForStudent = view.findViewById(R.id.noteForStudentEditText_id);

        ArrayAdapter aa = new ArrayAdapter(view.getContext() ,android.R.layout.simple_spinner_item, enrollmentNumbers);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        enrollmentSpinner.setAdapter(aa);

        enrollmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                enrollment = enrollmentNumbers[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                enrollment = enrollmentNumbers[0];
            }
        });

        ArrayAdapter a2 = new ArrayAdapter(view.getContext() ,android.R.layout.simple_spinner_item, documentType);
        a2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        documentTypeSpinner.setAdapter(a2);

        documentTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                documenttype = documentType[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                documenttype = documentType[0];
            }
        });

        uploadPDFButton.setOnClickListener(v -> {
            name = nameEditText.getText().toString().trim();
            email = emailEditText.getText().toString().trim();
            note = noteForStudent.getText().toString().trim();

            if(!name.isEmpty() && !email.isEmpty())
            {
                SendMail mail = new SendMail("studenttrackersystem@gmail.com", "sts_sts_sts",
                        email,
                        "LJ Reminder",
                        note);

                mail.execute();

                selectPDF();
            }
            else
            {
                Toast.makeText(view.getContext(), "Fields Empty", Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }

    public void selectPDF(){
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Choose PDF To Upload"), 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && data != null && data.getData() != null)
        {
            uploadPDF(data.getData());
        }
    }

    public void uploadPDF(Uri data){

        ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setMessage("Uploading Data");
        dialog.show();

        StorageReference reference = storage.child("Uploads/"+enrollment);
        reference.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                        while(!uri.isComplete());
                        Uri url = uri.getResult();

                        DocumentModel model = new DocumentModel();
                        model.setName(name);
                        model.setEmail(email);
                        model.setDocumentType(documenttype);
                        model.setEnrollment(enrollment);
                        model.setUrl(url.toString());


                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                        LocalDateTime now = LocalDateTime.now();
                        model.setDate(dtf.format(now));

                        database.getReference().child("HODOFFACULTY").child(user.getEmail().replace(".",""))
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String HODEmail = snapshot.getValue(String.class);

                                        database.getReference().child("PDFs").child(HODEmail.replace(".",""))
                                                .child(model.getEnrollment())
                                                .setValue(model)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {

                                                        // Uploaded Successfully
                                                        dialog.dismiss();
                                                        Toast.makeText(getView().getContext(), "PDF Uploaded Successfully", Toast.LENGTH_LONG).show();

                                                        nameEditText.setText("");
                                                        emailEditText.setText("");
                                                        noteForStudent.setText("");
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(getView().getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                                                        dialog.dismiss();
                                                    }
                                                });
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        dialog.dismiss();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
}
