package edu.nus.adproject.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import edu.nus.adproject.R;
import edu.nus.adproject.fragments.ProfileFragment;
import edu.nus.adproject.fragments.ReportFormFragment;
import edu.nus.adproject.models.PCMsg;
import edu.nus.adproject.services.PostsService;


import android.util.Log;
import android.widget.Toast;

import java.util.List;


public class PostAdapter extends ArrayAdapter<PCMsg> {

    // Define an interface for handling post interactions
    public interface OnPostInteractionListener {
        void onCommentClick(int postId);
        void dataChanged();
    }
    private Context context;
    private List<PCMsg> posts;
    private OnPostInteractionListener listener;
    private PostsService postsService;
    private int currentUserId;
    private boolean isLiked;
    private FragmentActivity fragmentActivity;

    public PostAdapter(@NonNull Context context, @NonNull List<PCMsg> posts, OnPostInteractionListener listener, int currentUserId, FragmentActivity fragmentActivity) {
        super(context, 0, posts);
        this.context = context;
        this.posts = posts;
        this.listener = listener;
        this.postsService = new PostsService();
        this.currentUserId = currentUserId;
        this.fragmentActivity = fragmentActivity; // Store the activity reference
    }

    private void loadFragment(Fragment fragment) {
        fragmentActivity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_content, fragment)
                .addToBackStack(null)
                .commit();
    }

    public void updatePosts(List<PCMsg> newPosts) {
        this.posts.clear();
        this.posts.addAll(newPosts);
        notifyDataSetChanged(); // Notify the adapter that the data has changed
    }

    // ViewHolder pattern: static inner class to hold views
    static class ViewHolder {
        TextView pfNameTxt;
        TextView postTxt;
        Button commentBtn;
        Button likeBtn;
        TextView timeStamp;
        ImageView overflowMenu;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @Nullable ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.post_row, parent, false);

            holder = new ViewHolder();
            holder.pfNameTxt = convertView.findViewById(R.id.pfNameTxt);
            holder.postTxt = convertView.findViewById(R.id.postTxt);
            holder.commentBtn = convertView.findViewById(R.id.commentBtn);
            holder.likeBtn = convertView.findViewById(R.id.likeBtn);
            holder.timeStamp = convertView.findViewById(R.id.timeStamp);
            holder.overflowMenu = convertView.findViewById(R.id.overflowMenu);
            convertView.setTag(holder); // Store holder with view

        }else{
            holder = (ViewHolder) convertView.getTag(); // Retrieve holder from view
        }

        PCMsg post = posts.get(position);
        // Bind data to the views
        if (post != null) {
            holder.pfNameTxt.setText(post.getUser().getName());
            holder.postTxt.setText(post.getContent());
            // Format the timestamp and display it
            holder.timeStamp.setText(post.getTimeStamp());

            // Check if the post is hidden
            if ("hide".equals(post.getStatus())) {
                // Apply grey tint to the background
                convertView.setBackgroundColor(Color.parseColor("#D3D3D3")); // Light grey
            } else {
                // Set default background color
                convertView.setBackgroundColor(Color.TRANSPARENT);
            }

            // Fetch and set like count in the button text
            new FetchLikeCountTask(holder.likeBtn).execute(post.getId());
            // Fetch and set comment count in the button text
            new FetchCommentCountTask(holder.commentBtn).execute(post.getId());
            // Boolean check user if like the posts
            new CheckUserLikeTask(holder.likeBtn).execute(currentUserId, post.getId());


            // Set click listeners for buttons if necessary
            holder.commentBtn.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onCommentClick(post.getId());
                }
            });

            // Handle click on the username to load the profile
            holder.pfNameTxt.setOnClickListener(v -> loadFragment(ProfileFragment.newInstance(currentUserId, post.getUser().getId())));

            holder.likeBtn.setOnClickListener(v -> {
                new ToggleLikeTask(holder.likeBtn, currentUserId, post.getId()).execute();
                // Trigger a refresh within the adapter
            });

            holder.overflowMenu.setOnClickListener(v -> showPopupMenu(v, post));
        }
        return convertView;
    }


    // AsyncTask to fetch like count and set it in the like button text
    private class FetchLikeCountTask extends AsyncTask<Integer, Void, Integer> {
        private Button likeButton;
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
        private Button commentButton;

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
            if (success) {
                // Re-check if the post is liked or not after toggling
                new CheckUserLikeTask(likeButton).execute();
                // Optionally update the like count here by making another API call
                new FetchLikeCountTask(likeButton).execute(postId);

            } else {
                // Handle failure case
                Toast.makeText(context, "Failed to like/unlike the post", Toast.LENGTH_SHORT).show();
            }
            if (listener != null) {
                listener.dataChanged(); // Ensure listener is not null
            } else {
                Log.e("PostAdapter", "Listener is null, dataChanged() not called");
            }
        }
    }


    private void showPopupMenu(View view, PCMsg post) {
        PopupMenu popup = new PopupMenu(context, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.pcmsgs_menu, popup.getMenu());

        // Get the current user ID and the comment's user ID
        int postUserId = post.getUser().getId();

        // If the current user is not the author of the comment, hide the delete and hide options
        if (postUserId != currentUserId) {
            MenuItem deleteItem = popup.getMenu().findItem(R.id.action_delete);
            MenuItem hideItem = popup.getMenu().findItem(R.id.action_hide);
            deleteItem.setVisible(false);
            hideItem.setVisible(false);
        }

        popup.setOnMenuItemClickListener(item -> handleMenuItemClick(item, post));
        popup.show();
    }

    private boolean handleMenuItemClick(MenuItem item, PCMsg post) {
        int id = item.getItemId();
        if (id == R.id.action_report) {
            reportPost(post);
            return true;
        } else if (id == R.id.action_delete) {
            deletePost(post);
            return true;
        } else if (id == R.id.action_hide) {
            hidePost(post);
            return true;
        } else {
            return false;
        }
    }

    private void reportPost(PCMsg post) {
        // Implement your report logic here
        ReportFormFragment reportFormFragment = ReportFormFragment.newInstance(currentUserId,post.getId(),"post");
        reportFormFragment.show(((FragmentActivity) context).getSupportFragmentManager(), "ReportFormFragment");
    }

    private void deletePost(PCMsg post) {
        new PostsService.DeletePostTask(context).execute(post.getId());
        Toast.makeText(context, "Deleted post", Toast.LENGTH_SHORT).show();
    }

    private void hidePost(PCMsg post) {
        // Implement your hide logic here
        new PostsService.HidePostTask(context).execute(post.getId());
        Toast.makeText(context, "Post hidden", Toast.LENGTH_SHORT).show();
    }


}

