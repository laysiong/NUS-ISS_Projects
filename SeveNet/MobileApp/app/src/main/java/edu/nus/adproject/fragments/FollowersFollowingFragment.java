package edu.nus.adproject.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.os.AsyncTask;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;


import java.util.ArrayList;
import java.util.List;

import edu.nus.adproject.R;
import edu.nus.adproject.activities.LoginActivity;
import edu.nus.adproject.adapters.FollowAdapter;
import edu.nus.adproject.models.User;
import edu.nus.adproject.services.FollowsService;

public class FollowersFollowingFragment extends Fragment implements FollowAdapter.OnFollowActionListener {
    private static final String ARG_IS_FOLLOWING = "isFollowing";
    private boolean followtab;

    private static final String ARG_USER_ID = "userId";
    private int userId;
    private TabLayout tabLayout;
    private ListView followListView;
    private FollowAdapter followAdapter;
    private FollowsService followsService;

    private List<User> followersList = new ArrayList<>();
    private List<User> followingList = new ArrayList<>();

    private ImageView mLogoutMenu;
    private LinearLayout mBackBtn;

    SharedPreferences pref;

    public static FollowersFollowingFragment newInstance(int userId, boolean isFollowing) {
        FollowersFollowingFragment fragment = new FollowersFollowingFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_USER_ID, userId);
        args.putBoolean(ARG_IS_FOLLOWING, isFollowing);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getInt(ARG_USER_ID);
            followtab = getArguments().getBoolean(ARG_IS_FOLLOWING, false);
        }
        followsService = new FollowsService();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_followers_following, container, false);

        followListView = view.findViewById(R.id.followListView);
        tabLayout = view.findViewById(R.id.tabLayout);

        // Add tabs for "Followers" and "Following"
        tabLayout.addTab(tabLayout.newTab().setText("Followers").setContentDescription("Tab for Followers"));
        tabLayout.addTab(tabLayout.newTab().setText("Following").setContentDescription("Tab for Following"));

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


        // Load both followers and following lists at once
        loadFollowersAndFollowingData();

        // Handle tab selection
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    Log.d("FollowersFollowingFragment", "Followers tab selected");
                    Log.d("FollowersFollowingFragment", "Followers list size: " + followersList.size());
                    updateListView(followersList);
                } else {
                    Log.d("FollowersFollowingFragment", "Following tab selected");
                    Log.d("FollowersFollowingFragment", "Following list size: " + followingList.size());
                    updateListView(followingList);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Log.d("FollowersFollowingFragment", "Tab unselected: " + tab.getPosition());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Log.d("FollowersFollowingFragment", "Tab reselected: " + tab.getPosition());
            }
        });

        return view;
    }

    // Method to refresh data
    private void refreshData() {
        loadFollowersAndFollowingData();
    }

    private void loadFollowersAndFollowingData() {
        new FetchFollowersAndFollowingTask().execute(userId);
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

    private void updateListView(List<User> users) {
        followAdapter = new FollowAdapter(getContext(), users, followingList, userId, this, requireActivity());
        followListView.setAdapter(followAdapter);
        followAdapter.notifyDataSetChanged();

        Log.d("FetchFollowersAndFollowingTask", "updateListView called");
    }
    private class FetchFollowersAndFollowingTask extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... params) {
            int userId = params[0];

            // Query both followers and following lists
            followersList = followsService.getFollowersOrFollowing(userId, false); // false for followers
            followingList = followsService.getFollowersOrFollowing(userId, true);  // true for following

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
//            Log.d("FetchFollowersAndFollowingTask", "onPostExecute called");
//            // Initially show followers
//            updateListView(followtab ? followersList : followingList);
            Log.d("FetchFollowersAndFollowingTask", "onPostExecute called");

            // Check which tab is selected and show the corresponding list
            int selectedTabPosition = tabLayout.getSelectedTabPosition();
            if (selectedTabPosition == 0) {
                updateListView(followersList);
            } else {
                updateListView(followingList);
            }
        }
    }

    @Override
    public void onFollowActionCompleted() {
        // Refresh data when a follow/unfollow action is completed
        refreshData();
    }

}

