package edu.nus.adproject.fragments;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import edu.nus.adproject.R;
import edu.nus.adproject.utils.ApiConfig;
import edu.nus.adproject.utils.PCMsgsUtil;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddPostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddPostFragment extends Fragment {

    private static final String ARG_USER_ID = "userId";  // Add a constant for userId
    private int userId;

    private EditText contentEditText;
    private Button submitPostButton;

    public AddPostFragment() {
        // Required empty public constructor
    }

    public static AddPostFragment newInstance(int userId) {
        AddPostFragment fragment = new AddPostFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getInt("userId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_post, container, false);

        contentEditText = view.findViewById(R.id.postET);
        submitPostButton = view.findViewById(R.id.postBtn);

        submitPostButton.setOnClickListener(v -> {
            String content = contentEditText.getText().toString().trim();
            if (!content.isEmpty()) {
                new SubmitPostTask(content).execute(); // Pass content directly to the AsyncTask
            } else {
                Toast.makeText(getActivity(), "Please enter some content", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private class SubmitPostTask extends AsyncTask<String, Void, Boolean> {
        private String content;

        SubmitPostTask(String content) {
            this.content = content;  // Assign the content passed in the constructor
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                String endpoint = ApiConfig.getBaseUrl()+"/pcmsgs/createPost"; // Your API endpoint

                // Use the utility class to post the message
                PCMsgsUtil.postMessage(endpoint, null, content, userId, new PCMsgsUtil.MessageCallback() {
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
                getActivity().getSupportFragmentManager().popBackStack();
            });
        }

        private void onPostFailure() {
            getActivity().runOnUiThread(() -> {
                Toast.makeText(getActivity(), "Failed to create post", Toast.LENGTH_SHORT).show();
            });
        }
    }
}