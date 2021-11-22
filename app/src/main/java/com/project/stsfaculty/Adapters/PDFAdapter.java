package com.project.stsfaculty.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.stsfaculty.Model.DocumentModel;
import com.project.stsfaculty.R;

import java.util.List;

public class PDFAdapter extends RecyclerView.Adapter<PDFAdapter.ViewHolder> {
    public List<DocumentModel> modelList;
    public Context context;

    public PDFAdapter(List<DocumentModel> modelList, Context context){
        this.modelList = modelList;
        this.context = context;
    }

    @NonNull
    @Override
    public PDFAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.pdf_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PDFAdapter.ViewHolder holder, int position) {
        DocumentModel model = new DocumentModel();
        model = modelList.get(position);

        holder.name.setText("Name: " + model.getName());
        holder.enrollment.setText("Enrollment: " + model.getEnrollment());
        holder.type.setText("Type: " + model.getDocumentType());
        holder.date.setText("Date: " + model.getDate());

        holder.itemView.setOnClickListener(v -> {

            DocumentModel model2 = new DocumentModel();
            model2 = modelList.get(position);

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setType("application/pdf");
            intent.setData(Uri.parse(model2.getUrl()));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name, enrollment, type, date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.pdfName_id);
            enrollment = itemView.findViewById(R.id.pdfenrollment_id);
            type = itemView.findViewById(R.id.pdfdocumentType_id);
            date = itemView.findViewById(R.id.pdfdate_id);
        }
    }
}
