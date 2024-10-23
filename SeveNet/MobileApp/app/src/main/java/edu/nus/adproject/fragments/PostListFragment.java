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
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.nus.adproject.R;
import edu.nus.adproject.activities.LoginActivity;
import edu.nus.adproject.adapters.PostAdapter;
import edu.nus.adproject.models.PCMsg;
import edu.nus.adproject.services.PostsService;

public class PostListFragment extends Fragment implements PostAdapter.OnPostInteractionListener {
    private static final String ARG_POSTS = "posts";
    private static final String ARG_USER_ID = "userId";  // Add a constant for userId
    private ListView postListView;
    private TextView fragementTitle;
    private PostAdapter postAdapter;
    private List<PCMsg> posts;
    private int userId;
    private PostsService postsService;
    private ImageView mLogoutMenu;
    SharedPreferences pref;

    public static PostListFragment newInstance(List<PCMsg> posts,int userId) {
        PostListFragment fragment = new PostListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_POSTS, new ArrayList<>(posts));
        args.putInt(ARG_USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            posts = getArguments().getParcelableArrayList(ARG_POSTS);
            userId = getArguments().getInt(ARG_USER_ID);
        }
        postsService = new PostsService();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_list, container, false);
        postListView = view.findViewById(R.id.postsLV);
        fragementTitle = view.findViewById(R.id.Title);
        fragementTitle.setText("News Feed");

        mLogoutMenu = view.findViewById(R.id.logoutMenu);
        mLogoutMenu.setOnClickListener(v -> showPopupLogOutMenu(v));

        if (posts == null) {
            posts = new ArrayList<>();
        }

        postAdapter = new PostAdapter(getContext(), posts, this, userId, requireActivity());
        postListView.setAdapter(postAdapter);

        fetchAndDisplayPosts();

        return view;
    }

    @Override
    public void onCommentClick(int postId) {
        PostDetailFragment fragment = PostDetailFragment.newInstance(postId,userId);
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_content, fragment)
                .addToBackStack(null)
                .commit();
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

    private void fetchAndDisplayPosts() {
        new FetchPostsTask().execute(userId);
    }

    private class FetchPostsTask extends AsyncTask<Integer, Void, List<PCMsg>> {

        @Override
        protected List<PCMsg> doInBackground(Integer... params) {
            int userId = params[0];
            return postsService.getPosts(userId); // Replace with your method to fetch posts
        }

        @Override
        protected void onPostExecute(List<PCMsg> newPosts) {
            if (newPosts != null) {
                Log.d("PostListFragment", "New posts received: " + newPosts.size());
                posts.clear();
                posts.addAll(newPosts);
                postAdapter.notifyDataSetChanged(); // Refresh the list
            }
        }
    }

    @Override
    public void dataChanged() {
        fetchAndDisplayPosts(); // Call this method to refresh the posts
    }

}