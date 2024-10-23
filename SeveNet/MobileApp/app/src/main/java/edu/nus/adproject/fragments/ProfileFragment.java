package edu.nus.adproject.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import edu.nus.adproject.R;
import edu.nus.adproject.activities.AppActivity;
import edu.nus.adproject.activities.LoginActivity;
import edu.nus.adproject.adapters.PostAdapter;
import edu.nus.adproject.models.PCMsg;
import edu.nus.adproject.services.FollowsService;
import edu.nus.adproject.services.PostsService;
import edu.nus.adproject.utils.ApiConfig;
import edu.nus.adproject.utils.PCMsgsUtil;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements PostAdapter.OnPostInteractionListener {

    private static final String ARG_USER_ID = "userId";
    private int userId;
    private static  String ARG_CUR_USER_ID = "CurrUserId";
    private int CurrUserId;

    private TextView usernameTextView;
    private TextView actualNameTextView;
    private TextView emailCountryJoinTextView;
    private TextView linkToFollowing;
    private TextView linkToFollowers;
    private TextView FollowingCount;
    private TextView FollowCount;

    private ImageView overflowMenu;
    private ImageView followbutton;

    private LinearLayout postsContainer;
    private PostAdapter postAdapter;
    private List<PCMsg> posts;
    private PostsService postsService;
    private FollowsService followsService;
    private boolean followState;

    private ImageView mLogoutMenu;
    private LinearLayout mBackBtn;

    SharedPreferences pref;

    public static ProfileFragment newInstance(int CurrUserId,int userId) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_USER_ID, userId);
        args.putInt(ARG_CUR_USER_ID, CurrUserId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getInt(ARG_USER_ID);
            CurrUserId = getArguments().getInt(ARG_CUR_USER_ID);
        }
        postsService = new PostsService();
        followsService = new FollowsService();
    }

    //View Controller
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        new FetchUsersPostsTask().execute(userId);

        usernameTextView = view.findViewById(R.id.profileNameTxt);
        actualNameTextView = view.findViewById(R.id.usernameTxt);
        emailCountryJoinTextView = view.findViewById(R.id.emailCountryJoin);

        linkToFollowing = view.findViewById(R.id.followingTxt);
        linkToFollowers = view.findViewById(R.id.followerTxt);

        FollowingCount = view.findViewById(R.id.followingNumTxt);
        FollowCount = view.findViewById(R.id.followerNumTxt);

        postsContainer = view.findViewById(R.id.postsContainer);
        followbutton = view.findViewById(R.id.followBtn);
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

        if (posts == null) {
            posts = new ArrayList<>();
            Log.d("PostListFragment", "No posts found, initializing empty list.");
        }

        linkToFollowing = view.findViewById(R.id.followingTxt);
        linkToFollowers = view.findViewById(R.id.followerTxt);


        linkToFollowing.setOnClickListener(v -> navigateToFollowersFollowing(true));
        linkToFollowers.setOnClickListener(v -> navigateToFollowersFollowing(false));

        // Fetch the user data
        new FetchUserDataTask().execute(userId);
        new FetchIsFollower().execute(userId);
        new CountFollower().execute(userId);

        if(CurrUserId == userId){
            overflowMenu.setVisibility(View.GONE);
            followbutton.setVisibility(View.GONE);
        }else{
            followbutton.setOnClickListener(v -> {
                    new FollowOrUnfollow(followState).execute(CurrUserId,userId);
                    });
            overflowMenu.setOnClickListener(v -> showPopupMenu(v, userId));
        }
        return view;
    }

    private void fetchAndDisplayPosts() {
        new FetchUsersPostsTask().execute(userId);
    }

    private class FetchUsersPostsTask extends AsyncTask<Integer, Void, List<PCMsg>> {

        @Override
        protected List<PCMsg> doInBackground(Integer... params) {
            int userId = params[0];
            return postsService.getUserPosts(userId);
        }

        @Override
        protected void onPostExecute(List<PCMsg> posts) {
            if (posts != null) {
                updatePostList(posts);
            }
        }
    }

    @Override
    public void dataChanged() {
        fetchAndDisplayPosts(); // Call this method to refresh the posts
    }

    private void updatePostList(List<PCMsg> posts) {
        if (postAdapter == null) {
            // Initialize PostAdapter
            postAdapter = new PostAdapter(getContext(), posts, this, userId,requireActivity());

            // Use PostAdapter to add views to the container
            for (int i = 0; i < postAdapter.getCount(); i++) {
                View postView = postAdapter.getView(i, null, postsContainer);
                postsContainer.addView(postView);
            }
        } else {
            postAdapter.clear();
            postAdapter.addAll(posts);
            postAdapter.notifyDataSetChanged();
        }
    }

    private void navigateToFollowersFollowing(boolean isFollowing) {
        FollowersFollowingFragment fragment = FollowersFollowingFragment.newInstance(userId, isFollowing);
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_content, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onCommentClick(int postId) {
        // This method will be called when the comment button is clicked
        // Replace the current fragment with PostDetailFragment and pass the postId
        Log.d("test", "onCreate: CommentListActivity started");

        PostDetailFragment fragment = PostDetailFragment.newInstance(postId,userId);
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_content, fragment)
                .addToBackStack(null)
                .commit();
    }

    //to run start to check
    private class FetchIsFollower extends AsyncTask<Integer, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Integer... params) {
            int userId = params[0];
            return followsService.isfollower(CurrUserId, userId);
        }
        @Override
        protected void onPostExecute(Boolean result) {
            if (result != null) {
                if (result) {
                    followbutton.setImageResource(R.drawable.added_friend);
                } else {
                    followbutton.setImageResource(R.drawable.add_friend);
                }
                followState = result;
            }
        }
    }
    class FollowCounts {
        int followingCount;
        int followerCount;

        FollowCounts(int followingCount, int followerCount) {
            this.followingCount = followingCount;
            this.followerCount = followerCount;
        }
    }

    private class CountFollower extends AsyncTask<Integer,Void, FollowCounts>{
        @Override
        protected FollowCounts doInBackground(Integer... params) {
            int userId = params[0];
            int followingCount = followsService.followingCount(userId);
            int followerCount = followsService.followerCount(userId);

            return new FollowCounts(followingCount, followerCount);
        }

        @Override
        protected void onPostExecute(FollowCounts result) {
            // Use result.followingCount and result.followerCount
            FollowCount.setText(String.valueOf(result.followerCount));
            FollowingCount.setText(String.valueOf(result.followingCount));

        }
    }

    //to click to follow/unfollow the user
   private class FollowOrUnfollow extends AsyncTask<Integer, Void, Boolean> {
        private boolean followState;

        FollowOrUnfollow(boolean followState) {
            this.followState = followState;
        }

        @Override
        protected Boolean doInBackground(Integer... params) {
            int currentUser = params[0];
            int userId = params[1];

            try {
                if (!followState) {
                    followsService.followUser(currentUser, userId);
                } else {
                    followsService.unfollowUser(currentUser, userId);
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                onPostSuccess();
            } else {
                onPostFailure();
            }
        }

        private void onPostSuccess() {
            Toast.makeText(getActivity(), "Operation successful", Toast.LENGTH_SHORT).show();
            if (followState) {
                followbutton.setImageResource(R.drawable.added_friend);
            } else {
                followbutton.setImageResource(R.drawable.add_friend);
            }
            followState = !followState; // Toggle the follow state
            new FetchIsFollower().execute(userId);
        }

        private void onPostFailure() {
            Toast.makeText(getActivity(), "Operation failed", Toast.LENGTH_SHORT).show();
        }
    }



    private class FetchUserDataTask extends AsyncTask<Integer, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(Integer... params) {
            int userId = params[0];
            try {
                URL url = new URL(ApiConfig.getBaseUrl()+"/user/findById/" + userId);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                reader.close();

                return new JSONObject(result.toString());

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(JSONObject user) {
            if (user != null) {
                try {
                    String username = user.getString("username");
                    String name = user.getString("name");
                    String email = user.getString("email");
                    String country = user.getString("country");
                    String joinDate = user.getString("joinDate");

                    usernameTextView.setText(username);
                    actualNameTextView.setText(name);
                    emailCountryJoinTextView.setText(email + " | " + country + " | Joined: " + joinDate);

                    // Set up click listeners for following and followers if needed
                    // linkToFollowing.setOnClickListener(...);
                    // linkToFollowers.setOnClickListener(...);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void showPopupMenu(View view, int userId) {
        PopupMenu popup = new PopupMenu(getActivity(), view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.pcmsgs_menu, popup.getMenu());

        MenuItem deleteItem = popup.getMenu().findItem(R.id.action_delete);
        deleteItem.setVisible(false);

        MenuItem hideItem = popup.getMenu().findItem(R.id.action_hide);
        hideItem.setVisible(false);

//        MenuItem blockItem = popup.getMenu().findItem(R.id.action_block);
//        blockItem.setVisible(true);

        popup.setOnMenuItemClickListener(item -> handleMenuItemClick(item, userId));
        popup.show();
    }

    private boolean handleMenuItemClick(MenuItem item, int userId) {
        int id = item.getItemId();
        if (id == R.id.action_report) {
            reportPost(userId);
            Log.d("ReportProfile", "User id = " + userId);
            return true;
        } else {
            return false;
        }
    }

    private void reportPost(int userId) {
        // Implement your report logic here
        ReportFormFragment reportFormFragment = ReportFormFragment.newInstance(CurrUserId,userId,"user");
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

}