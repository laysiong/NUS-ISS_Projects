package edu.nus.adproject.fragments;

import android.content.DialogInterface; // <-- Add this line
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.List;

import edu.nus.adproject.R;
import edu.nus.adproject.adapters.LabelAdapter;
import edu.nus.adproject.models.Label;
import edu.nus.adproject.services.LabelService;
import edu.nus.adproject.services.ReportService;

public class ReportFormFragment extends DialogFragment {

    private static final  String ARG_USER_ID = "userId";
    private int userId;
    private static final  String ARG_REPORT_ID = "reportId";
    private int reportId;
    private static final  String ARG_REPORT_TYPE = "reporttype";
    private String reporttype;

    private EditText cotentEditText;
    private Button subbmitButton;
    private ListView reportype;

    private LabelAdapter labelAdapter;


    public ReportFormFragment(){
        // Required empty public constructor
    }

    public static ReportFormFragment newInstance(int userId, int reportId, String reporttype){
        ReportFormFragment fragment = new ReportFormFragment();
        Bundle args =  new Bundle();
        args.putInt(ARG_USER_ID, userId);
        args.putInt(ARG_REPORT_ID, reportId);
        args.putString(ARG_REPORT_TYPE, reporttype);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            userId = getArguments().getInt("userId");
            reportId = getArguments().getInt("reportId");
            reporttype = getArguments().getString("reporttype");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_report_form, container, false);

        cotentEditText = view.findViewById(R.id.ReportMsg);
        subbmitButton = view.findViewById(R.id.submitBtn);
        reportype = view.findViewById(R.id.ReportLabel);

        // Fetch the labels in the background
        new FetchLabelsTask().execute();

        subbmitButton.setOnClickListener(v -> {
            String content = cotentEditText.getText().toString().trim();

            // Use the custom adapter to get the selected label's ID
            int labelId = labelAdapter.getSelectedLabelId();
            if (labelId != -1) { // Valid label selected
                if (!content.isEmpty()) {
                    new SubmitReportTask(labelId, content).execute();
                } else {
                    Toast.makeText(getActivity(), "Please enter some content", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "Please select a label", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private class FetchLabelsTask extends AsyncTask<Void, Void, List<Label>> {

        @Override
        protected List<Label> doInBackground(Void... voids) {
            return new LabelService().getLabel();
        }

        @Override
        protected void onPostExecute(List<Label> labels) {
            if (labels != null && !labels.isEmpty()) {
                labelAdapter = new LabelAdapter(getContext(), labels);
                reportype.setAdapter(labelAdapter);
            } else {
                Toast.makeText(getContext(), "Failed to load labels", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        // Handle the cancel action
        Toast.makeText(getActivity(), "Report was canceled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        // Handle the dismiss action
        Toast.makeText(getActivity(), "Dialog dismissed", Toast.LENGTH_SHORT).show();
    }

    private class SubmitReportTask extends AsyncTask<String, Void, Boolean> {
        private String content;
        private int labelId;

        SubmitReportTask(int labelId, String content){
            this.labelId = labelId;
            this.content = content;
        }

        @Override
        protected Boolean doInBackground(String... params){
            try {
//                  String endpoint = "http://10.0.2.2:8080/api/pcmsgs/createPost"; // Your API endpoint

                    // Use the utility class to post the message
                    ReportService.postReportTask(reporttype,reportId, userId, labelId, content, new ReportService.MessageCallback() {
                    @Override
                    public void onSuccess() {
                        onPostSuccess();
                    }

                    @Override
                    public void onFailure() {
                        onPostFailure();
                    }
                });

                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (!success) {
                onPostFailure();
            }
        }

        private void onPostSuccess() {
            getActivity().runOnUiThread(() -> {
                Toast.makeText(getActivity(), "Post created successfully", Toast.LENGTH_SHORT).show();
                // Navigate back or refresh the list of posts
                dismiss(); // This will only dismiss the ReportFormFragment
            });
        }

        private void onPostFailure() {
            getActivity().runOnUiThread(() -> {
                Toast.makeText(getActivity(), "Failed to create post", Toast.LENGTH_SHORT).show();
            });
        }

    }

}
