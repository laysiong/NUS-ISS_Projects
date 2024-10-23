package edu.nus.adproject.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import edu.nus.adproject.R;
import edu.nus.adproject.activities.LoginActivity;
import edu.nus.adproject.adapters.CommentAdapter;
import edu.nus.adproject.adapters.PostAdapter;
import edu.nus.adproject.models.PCMsg;
import edu.nus.adproject.services.PostsService;

public class PostDetailFragment extends Fragment{

    private static final String TAG = "PostDetailFragment";

    private static final String ARG_POST_ID = "postId";
    private static final String ARG_USER_ID = "userId";  // Add a constant for userId

    private int postId;
    private int userId;

    private TextView pfNameTxt;
    private TextView postTxt;
    private TextView timeStamp;
    private Button commentBtn;
    private Button likeBtn;
    private ImageView overflowMenu;
    private PostsService postsService; // Make sure to initialize this

    private ImageView mLogoutMenu;
    private LinearLayout mBackBtn;

    SharedPreferences pref;

    public static PostDetailFragment newInstance(int postId, int userId) {
        PostDetailFragment fragment = new PostDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POST_ID, postId);
        args.putInt(ARG_USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            postId = getArguments().getInt(ARG_POST_ID);
            userId = getArguments().getInt(ARG_USER_ID);
        }
        postsService = new PostsService(); // Initialize PostsService
        Log.d(TAG, "onCreate: Fragment created");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_detail, container, false);
        pfNameTxt = view.findViewById(R.id.pfNameTxt);
        postTxt = view.findViewById(R.id.postTxt);
        timeStamp = view.findViewById(R.id.timeStamp);
        commentBtn = view.findViewById(R.id.commentBtn);
        likeBtn = view.findViewById(R.id.likeBtn);
        overflowMenu = view.findViewById(R.id.overflowMenu);

        mLogoutMenu = view.findViewById(R.id.logoutMenu);
        mLogoutMenu.setOnClickListener(v -> showPopupLogOutMenu(v));

        mBackBtn = view.findViewById(R.id.backBtn);
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getFragmentManager() != null && getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStack();  // Pop the top fragment off the back stack
                } else {
                    requireActivity().onBackPressed();  // If no fragments in back stack, handle as activity back press
                }
            }
        });

        // Set up the comments button click listener
        commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                AddPostOrCommentFragment addPostOrCommentFragment = AddPostOrCommentFragment.newInstance(true, postId, userId); // Assuming user ID is 1 for demonstration
                transaction.replace(R.id.fragment_content, addPostOrCommentFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        new FetchPostDetailsTask().execute(postId);

        // Fetch and set like count
        new FetchLikeCountTask(likeBtn).execute(postId);
        new FetchCommentCountTask(commentBtn).execute(postId);
        new CheckUserLikeTask(likeBtn).execute(userId, postId);

        likeBtn.setOnClickListener(v -> {
            new ToggleLikeTask(likeBtn, userId, postId).execute();
            // Trigger a refresh within the adapter
        });

        //pop out menu for post detail at top
        overflowMenu.setOnClickListener(v -> showPopupMenu(v, postId));

        return view;
    }

    //Small Menu
    private void showPopupMenu(View view, int postId) {
        PopupMenu popup = new PopupMenu(getContext(), view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.pcmsgs_menu, popup.getMenu());

        MenuItem deleteItem = popup.getMenu().findItem(R.id.action_delete);
        deleteItem.setVisible(false);

        MenuItem hideItem = popup.getMenu().findItem(R.id.action_hide);
        hideItem.setVisible(false);

        popup.setOnMenuItemClickListener(item -> handleMenuItemClick(item, postId));
        popup.show();
    }


    private boolean handleMenuItemClick(MenuItem item, int postId) {
        int id = item.getItemId();
        if (id == R.id.action_report) {
            reportPost(postId);
            return true;
        } else {
            return false;
        }
    }

    private void reportPost(int reportId) {
        // Implement your report logic here
        ReportFormFragment reportFormFragment = ReportFormFragment.newInstance(userId,reportId,"post");
        reportFormFragment.show(getActivity().getSupportFragmentManager(), "ReportFormFragment");
    }

    private void showPopupLogOutMenu(View view) {
        PopupMenu popup = new PopupMenu(requireContext(), view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.logout_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(item -> handleMenuLogout(item));
        popup.show();
    }

    private boolean handleMenuLogout(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            pref = requireContext().getSharedPreferences("user_credential", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.remove("username");
            editor.remove("password");
            editor.commit();

            Intent intent = new Intent(requireContext(), LoginActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }

    //Get Post Details and Comments
    private class FetchPostDetailsTask extends AsyncTask<Integer, Void, PCMsg> {
        @Override
        protected PCMsg doInBackground(Integer... params) {
            int postId = params[0];
            PostsService postsService = new PostsService();
            return postsService.fetchPostsById(postId); // Use the fetchPostsById method
        }

        @Override
        protected void onPostExecute(PCMsg post) {
            Log.d("run","system again");
            if (post != null) {
                pfNameTxt.setText(post.getUser().getUsername());
                postTxt.setText(post.getContent());
                timeStamp.setText(post.getTimeStamp());


                Log.d("check post commets", "num" + post.getCommentList().size());
                // Pass comments to CommentListFragment
                CommentListFragment commentListFragment = CommentListFragment.newInstance(postId, userId, post.getCommentList());
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.replace(R.id.comments_container, commentListFragment).commit();

            } else {
                // Handle the case where post is null (e.g., display an error message)
                Toast.makeText(getContext(), "Failed to load post details", Toast.LENGTH_SHORT).show();
            }

        }
    }

    // AsyncTask to fetch like count and set it in the like button text
    private class FetchLikeCountTask extends AsyncTask<Integer, Void, Integer> {
        private final Button likeButton;

        FetchLikeCountTask(Button likeButton) {
            this.likeButton = likeButton;
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            int postId = params[0];
            return postsService.countLikesByPCMsgId(postId);
        }

        @Override
        protected void onPostExecute(Integer likeCount) {
            likeButton.setText(String.format("Like (%d)", likeCount));
        }
    }

    // AsyncTask to fetch comment count and set it in the comment button text
    private class FetchCommentCountTask extends AsyncTask<Integer, Void, Integer> {
        private final Button commentButton;

        FetchCommentCountTask(Button commentButton) {
            this.commentButton = commentButton;
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            int postId = params[0];
            return postsService.countAllCommentsByPostId(postId);
        }

        @Override
        protected void onPostExecute(Integer commentCount) {
            commentButton.setText(String.format("Comment (%d)", commentCount));
        }
    }


    private class ToggleLikeTask extends AsyncTask<Void, Void, Boolean> {
        private Button likeButton;
        private int userId;
        private int postId;

        ToggleLikeTask(Button likeButton, int userId, int postId) {
            this.likeButton = likeButton;
            this.userId = userId;
            this.postId = postId;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            return postsService.togglelike(userId,postId);
        }

        @Override
        protected void onPostExecute(Boolean success) {
            new FetchLikeCountTask(likeBtn).execute(postId);
            new FetchCommentCountTask(commentBtn).execute(postId);
            new CheckUserLikeTask(likeBtn).execute(userId, postId);
        }
    }

    //Check if user like the button
    private class CheckUserLikeTask extends AsyncTask<Integer, Void, Boolean> {
        private Button likeButton;

        CheckUserLikeTask(Button likeButton) {
            this.likeButton = likeButton;
        }

        @Override
        protected Boolean doInBackground(Integer... params) {
            int userId = params[0];
            int postId = params[1];
            return postsService.CheckUserLikeTask(userId,postId);
        }

        @Override
        protected void onPostExecute(Boolean results) {
            likeButton.setCompoundDrawablesWithIntrinsicBounds(results ? R.drawable.ic_like : R.drawable.ic_heart_unfilled, 0, 0, 0);
        }
    }

}

