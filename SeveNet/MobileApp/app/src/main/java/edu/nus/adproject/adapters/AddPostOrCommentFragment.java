package edu.nus.adproject.adapters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import edu.nus.adproject.R;
import edu.nus.adproject.utils.ApiConfig;
import edu.nus.adproject.utils.PCMsgsUtil;


public class AddPostOrCommentFragment extends Fragment {

    private static final String ARG_IS_COMMENT = "isComment";
    private static final String ARG_POST_ID = "postId";
    private static final String ARG_USER_ID = "userId";

    private boolean isComment;
    private int postId;
    private int userId;

    private EditText contentEditText;
    private Button submitButton;

    public AddPostOrCommentFragment() {
        // Required empty public constructor
    }

    public static AddPostOrCommentFragment newInstance(boolean isComment, Integer postId, int userId) {
        AddPostOrCommentFragment fragment = new AddPostOrCommentFragment();
        Bundle args = new Bundle();
        args.putBoolean("isComment", isComment);
        if (postId != null) {
            args.putInt("postId", postId);
        }
        args.putInt("userId", userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isComment = getArguments().getBoolean("isComment");
            if (getArguments().containsKey("postId")) {
                postId = getArguments().getInt("postId");
            }
            userId = getArguments().getInt("userId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_post_or_comment, container, false);
        contentEditText = view.findViewById(R.id.contentEditText);
        submitButton = view.findViewById(R.id.submitButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = contentEditText.getText().toString().trim();
                if (!content.isEmpty()) {
                    String endpoint = isComment ? ApiConfig.getBaseUrl()+"/pcmsgs/createComment" : ApiConfig.getBaseUrl()+"/pcmsgs/createPost";
                    PCMsgsUtil.postMessage(endpoint, isComment ? postId : null, content, userId, new PCMsgsUtil.MessageCallback() {
                        @Override
                        public void onSuccess() {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity(), "Operation successful", Toast.LENGTH_SHORT).show();
                                    getActivity().getSupportFragmentManager().popBackStack();
                                }
                            });
                        }

                        @Override
                        public void onFailure() {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity(), "Operation failed", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), "Please enter content", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}