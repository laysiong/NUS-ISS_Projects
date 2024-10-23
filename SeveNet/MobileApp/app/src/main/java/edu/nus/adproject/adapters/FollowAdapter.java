package edu.nus.adproject.adapters;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import java.util.List;

import edu.nus.adproject.R;
import edu.nus.adproject.fragments.ProfileFragment;
import edu.nus.adproject.models.User;
import edu.nus.adproject.services.FollowsService;

public class FollowAdapter extends ArrayAdapter<User> {
    // Callback interface for notifying the fragment
    public interface OnFollowActionListener {
        void onFollowActionCompleted();
    }

    private Context context;
    private List<User> usersList;  // List of users to display
    private List<User> followingList; // List of users the current user is following
    private int currentUserId;
    private FollowsService followerSerivce;
    private OnFollowActionListener followActionListener; // Add the callback listener
    private FragmentActivity fragmentActivity;

    public FollowAdapter(@NonNull Context context, @NonNull List<User> usersList, @NonNull List<User> followingList, int currentUserId, OnFollowActionListener listener,  FragmentActivity fragmentActivity) {
        super(context, 0);
        this.context = context;
        this.usersList = usersList;
        this.followingList = followingList;
        this.currentUserId = currentUserId;
        this.followerSerivce = new FollowsService(); // Initialize the followerService
        this.followActionListener = listener; // Initialize the listener
        this.fragmentActivity = fragmentActivity; // Store the activity reference

    }

    @Override
    public int getCount() {
        return usersList != null ? usersList.size() : 0;
    }

    static class ViewHolder{
        TextView usernameTextView;
        TextView nameTextView;
        Button typeButton;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (position >= usersList.size()) {
            Log.e("FollowAdapter", "Invalid position: " + position + " for list size: " + usersList.size());
            return convertView; // Early return to avoid accessing invalid list position
        }

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_follow_user, parent, false);

            holder = new ViewHolder();

            // Find views in item_follow_user.xml
            holder.usernameTextView = convertView.findViewById(R.id.Username);
            holder.nameTextView = convertView.findViewById(R.id.Name);
            holder.typeButton = convertView.findViewById(R.id.TypeButton);
            convertView.setTag(holder); // Store holder with view
        }else{
            holder = (ViewHolder) convertView.getTag(); // Retrieve holder from view
        }

        User user = usersList.get(position);

        // Set user details
        holder.usernameTextView.setText(user.getUsername());
        holder.nameTextView.setText(user.getName());

        holder.usernameTextView.setOnClickListener(v -> loadFragment(ProfileFragment.newInstance(currentUserId, user.getId())));

        // Determine the correct button text based on the scenario
        boolean iFollowThem = isFollowing(user);
        boolean theyFollowMe = usersList.contains(user); // Since usersList is followers or following list

        if (theyFollowMe && iFollowThem) {
            // Mutual follow, show "Unfollow" button
            holder.typeButton.setText("Unfollow");
        } else if (theyFollowMe && !iFollowThem) {
            // They follow me but I don't follow them, show "Follow" button
            holder.typeButton.setText("Follow");
        } else if (iFollowThem) {
            // I follow them but they don't follow me, should be in the following list with "Unfollow"
            holder.typeButton.setText("Unfollow");
        }

        // Handle follow/unfollow button click
        holder.typeButton.setOnClickListener(v -> {
            if (iFollowThem) {
                unfollowUser(currentUserId, user.getId());
            } else {
                followUser(currentUserId, user.getId());
            }
            holder.typeButton.setText(iFollowThem ? "Follow" : "Unfollow");

        });

        return convertView;
    }

    private void loadFragment(Fragment fragment) {
        fragmentActivity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_content, fragment)
                .addToBackStack(null)
                .commit();
    }

    // Check if the user is in the following list
    private boolean isFollowing(User user) {
        for (User followingUser : followingList) {
            if (followingUser.getId() == user.getId()) {
                return true;
            }
        }
        return false;
    }

    private void followUser(int currentUserId, int followUserId) {
        new FollowUserTask().execute(currentUserId, followUserId);
    }

    private void unfollowUser(int currentUserId, int followUserId) {
        new UnfollowUserTask().execute(currentUserId, followUserId);
    }

    private class FollowUserTask extends AsyncTask<Integer, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Integer... params) {
            int currentUserId = params[0];
            int followUserId = params[1];
            return followerSerivce.followUser(currentUserId, followUserId);
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                Toast.makeText(context, "Followed successfully", Toast.LENGTH_SHORT).show();
                followActionListener.onFollowActionCompleted(); // Notify the fragment
            } else {
                Toast.makeText(context, "Failed to follow user", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class UnfollowUserTask extends AsyncTask<Integer, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Integer... params) {
            int currentUserId = params[0];
            int followUserId = params[1];
            return followerSerivce.unfollowUser(currentUserId, followUserId);
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                Toast.makeText(context, "Unfollowed successfully", Toast.LENGTH_SHORT).show();
                followActionListener.onFollowActionCompleted(); // Notify the fragment
            } else {
                Toast.makeText(context, "Failed to unfollow user", Toast.LENGTH_SHORT).show();
            }
        }
    }
}