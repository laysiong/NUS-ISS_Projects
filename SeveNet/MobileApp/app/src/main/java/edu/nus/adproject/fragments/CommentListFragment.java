package edu.nus.adproject.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import edu.nus.adproject.R;
import edu.nus.adproject.adapters.CommentAdapter;
import edu.nus.adproject.models.Comment;
import edu.nus.adproject.services.PostsService;

public class CommentListFragment extends Fragment implements CommentAdapter.OnCommentInteractionListener {

    private static final  String ARG_USER_ID = "userId";
    private static final  String ARG_POSTS_ID = "postId";
    private static final String ARG_COMMENTS = "comments";

    private int postId;
    private int userId;
    private List<Comment> commentList;
    private RecyclerView commentRecyclerView ;
    private CommentAdapter adapter;


    public CommentListFragment() {}

    public static CommentListFragment newInstance(int postId, int userId, List<Comment> comments) {
        CommentListFragment fragment = new CommentListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_USER_ID, userId);
        args.putInt(ARG_POSTS_ID, postId);
        args.putParcelableArrayList(ARG_COMMENTS, (ArrayList<? extends Parcelable>) comments);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            postId=getArguments().getInt("postId");
            userId = getArguments().getInt("userId");
            commentList = getArguments().getParcelableArrayList(ARG_COMMENTS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_comment_list, container, false);
        commentRecyclerView = view.findViewById(R.id.commentRV);

        // Initialize the ListView and adapter
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CommentAdapter(getContext(), commentList, userId,this,getActivity());
        commentRecyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void commentChanged() {
        refreshComments();
    }

    private void refreshComments() {
        new FetchUpdatedCommentsTask().execute(postId);
    }

    private class FetchUpdatedCommentsTask extends AsyncTask<Integer, Void, List<Comment>> {
        @Override
        protected List<Comment> doInBackground(Integer... params) {
            int postId = params[0];
            PostsService postsService = new PostsService();
            return postsService.fetchPostsById(postId).getCommentList();
        }

        @Override
        protected void onPostExecute(List<Comment> updatedComments) {
            if (updatedComments != null) {
                commentList.clear();
                commentList.addAll(updatedComments);
                adapter.notifyDataSetChanged();
            }
        }
    }


}
