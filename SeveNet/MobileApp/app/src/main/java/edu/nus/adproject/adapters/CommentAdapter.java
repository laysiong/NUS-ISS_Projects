package edu.nus.adproject.adapters;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.nus.adproject.R;
import edu.nus.adproject.fragments.ReportFormFragment;
import edu.nus.adproject.models.Comment;
import edu.nus.adproject.services.CommentService;
import edu.nus.adproject.services.PostsService;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    public interface OnCommentInteractionListener  {
        void commentChanged();
    }

    private Context context;
    private List<Comment> comments;
    private int userId;  // The current user ID
    private OnCommentInteractionListener listener;
    private CommentService commentServiceService; // Make sure to initialize this
    private PostsService postsService;

    //for comment
    private FragmentActivity fragmentActivity; // Add this

    public CommentAdapter(Context context, List<Comment> comments, int userId, OnCommentInteractionListener listener, FragmentActivity fragmentActivity) {
        this.context = context;
        this.comments = comments;
        this.listener = listener;
        this.userId = userId;
        this.commentServiceService = new CommentService();
        this.postsService = new PostsService();
        this.fragmentActivity = fragmentActivity;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = comments.get(position);


        if ("show".equals(comment.getStatus())) {
            holder.commentUsername.setText(comment.getUser_id());
            holder.commentContent.setText(comment.getContent());
            holder.commentTimestamp.setText(comment.getTimeStamp());
        } else {
            holder.commentUsername.setText("Removed");
            holder.commentContent.setText("Comments have been removed.");
            holder.commentTimestamp.setText(" ");
        }

        new FetchRepliesTask(comment.getId(), holder.childRecyclerView).execute();
        new FetchLikeCountTask(holder.likeBtn).execute(comment.getId());
        new FetchCommentCountTask(holder.commentCountBtn).execute(comment.getId());
        new CheckUserLikeTask(holder.likeBtn).execute(userId, comment.getId());

        holder.viewBtn.setOnClickListener(v -> {
            if (holder.childRecyclerView.getVisibility() == View.VISIBLE) {
                holder.childRecyclerView.setVisibility(View.GONE); // Hide comments
                holder.viewBtn.setText("+"); // Change button text to "+"
            } else {
                holder.childRecyclerView.setVisibility(View.VISIBLE); // Show comments
                holder.viewBtn.setText("-"); // Change button text to "-"
            }
        });


        holder.commentCountBtn.setOnClickListener(v -> {
            Toast.makeText(context, "Reply to comment", Toast.LENGTH_SHORT).show();
            FragmentTransaction transaction = fragmentActivity.getSupportFragmentManager().beginTransaction();
            AddPostOrCommentFragment addPostOrCommentFragment = AddPostOrCommentFragment.newInstance(true, comment.getId(), userId); // Using comment ID for reply
            transaction.replace(R.id.fragment_content, addPostOrCommentFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        holder.likeBtn.setOnClickListener(v -> {
            new ToggleLikeTask(holder.likeBtn, userId, comment.getId()).execute();
        });
        // Handle the overflow menu
        holder.overflowMenu.setOnClickListener(v -> showPopupMenu(v, comment));
    }

    private class FetchLikeCountTask extends AsyncTask<Integer, Void, Integer> {
        private final Button likeButton;

        FetchLikeCountTask(Button likeButton) {
            this.likeButton = likeButton;
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            int commentsId = params[0];
            return postsService.countLikesByPCMsgId(commentsId);
        }

        @Override
        protected void onPostExecute(Integer likeCount) {
            likeButton.setText(String.format("(%d)", likeCount));
        }
    }

    private class FetchCommentCountTask extends AsyncTask<Integer, Void, Integer> {
        private final Button commentButton;

        FetchCommentCountTask(Button commentButton) {
            this.commentButton = commentButton;
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            int commentsId = params[0];
            return postsService.countAllCommentsByPostId(commentsId);
        }

        @Override
        protected void onPostExecute(Integer commentCount) {
            commentButton.setText(String.format("(%d)", commentCount));
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
        private int commentId;

        ToggleLikeTask(Button likeButton, int userId, int commentId) {
            this.likeButton = likeButton;
            this.userId = userId;
            this.commentId = commentId;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            return postsService.togglelike(userId,commentId);
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                // Re-check if the post is liked or not after toggling
                new CheckUserLikeTask(likeButton).execute();
                // Optionally update the like count here by making another API call
                new FetchLikeCountTask(likeButton).execute(commentId);

            } else {
                // Handle failure case
                Toast.makeText(context, "Failed to like/unlike the post", Toast.LENGTH_SHORT).show();
            }
            if (listener != null) {
                listener.commentChanged(); // Ensure listener is not null
            } else {
                Log.e("PostAdapter", "Listener is null, dataChanged() not called");
            }
        }
    }




    private class FetchRepliesTask extends AsyncTask<Void, Void, List<Comment>> {
        private int commentId;
        private RecyclerView childRecyclerView;

        FetchRepliesTask(int commentId, RecyclerView childRecyclerView) {
            this.commentId = commentId;
            this.childRecyclerView = childRecyclerView;
        }

        @Override
        protected List<Comment> doInBackground(Void... voids) {
            // Fetch the replies for this comment
            return commentServiceService.fetchChildCommentsFromAPI(commentId);
        }

        @Override
        protected void onPostExecute(List<Comment> replies) {
            if (replies != null && !replies.isEmpty()) {
                CommentAdapter childAdapter = new CommentAdapter(context, replies, userId, listener,fragmentActivity);
                childRecyclerView.setAdapter(childAdapter);
                childRecyclerView.setLayoutManager(new LinearLayoutManager(context));
                childRecyclerView.setVisibility(View.GONE);
            }
        }
    }


    @Override
    public int getItemCount() {
        return comments.size();
    }


    class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView commentUsername, commentContent, commentTimestamp;
        ImageView overflowMenu;
        RecyclerView childRecyclerView;
        Button viewBtn, commentCountBtn, likeBtn;

        CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            commentUsername = itemView.findViewById(R.id.commentUsername);
            commentContent = itemView.findViewById(R.id.commentContent);
            commentTimestamp = itemView.findViewById(R.id.commentTimestamp);
            overflowMenu = itemView.findViewById(R.id.overflowMenu);
            childRecyclerView = itemView.findViewById(R.id.childCommentRecyclerView);
            viewBtn = itemView.findViewById(R.id.viewBtn);
            commentCountBtn = itemView.findViewById(R.id.commentCount);
            likeBtn = itemView.findViewById(R.id.likeBtn);
        }
    }


    private void showPopupMenu(View view, Comment comment) {
            PopupMenu popup = new PopupMenu(context, view);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.pcmsgs_menu, popup.getMenu());

            // Get the comment's user ID
            int commentUserId = comment.getuser_id();

            // If the current user is not the author of the comment, hide the delete and hide options
            if (commentUserId != userId) {
                MenuItem deleteItem = popup.getMenu().findItem(R.id.action_delete);
                MenuItem hideItem = popup.getMenu().findItem(R.id.action_hide);
                deleteItem.setVisible(false);
                hideItem.setVisible(false);
            }

            popup.setOnMenuItemClickListener(item -> handleMenuItemClick(item, comment));
            popup.show();
        }

    private boolean handleMenuItemClick(MenuItem item, Comment comment) {
        int id = item.getItemId();
        if (id == R.id.action_report) {
            reportPost(comment);
            return true;
        } else if (id == R.id.action_delete) {
            deletePost(comment);
            return true;
        } else if (id == R.id.action_hide) {
            hidePost(comment);
            return true;
        } else {
            return false;
        }
    }

    private void reportPost(Comment comment) {
        // Implement your report logic here
        ReportFormFragment reportFormFragment = ReportFormFragment.newInstance(userId, comment.getId(), "comment");
        reportFormFragment.show(((FragmentActivity) context).getSupportFragmentManager(), "ReportFormFragment");
    }

    private void deletePost(Comment comment) {
        // Implement your delete logic here
        new PostsService.DeletePostTask(context).execute(comment.getId());
    }

    private void hidePost(Comment comment) {
        // Implement your hide logic here
        new PostsService.HidePostTask(context).execute(comment.getId());
    }

}
