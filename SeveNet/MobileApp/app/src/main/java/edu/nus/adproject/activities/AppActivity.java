package edu.nus.adproject.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;

import edu.nus.adproject.R;
import edu.nus.adproject.fragments.AddPostFragment;
import edu.nus.adproject.fragments.NotificationFragment;
import edu.nus.adproject.fragments.PostDetailFragment;
import edu.nus.adproject.fragments.PostListFragment;
import edu.nus.adproject.fragments.ProfileFragment;
import edu.nus.adproject.fragments.TrendFragment;
import edu.nus.adproject.models.PCMsg;
import edu.nus.adproject.models.User;
import edu.nus.adproject.services.PostsService;

public class AppActivity extends AppCompatActivity {

    private User user;
    private PostsService postsService;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_app);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        user = intent.getParcelableExtra("User");
        userId= user.getId();
        postsService = new PostsService();

        if (user != null) {
            // Fetch posts after user logs in
            new FetchPostsTask().execute(userId);
        }

        //Home
        findViewById(R.id.home).setOnClickListener(view -> {
            // Reload the posts when home is clicked
            if (user != null) {
                new FetchPostsTask().execute(userId);
            }
        });

        //Trend
        findViewById(R.id.trend).setOnClickListener(view -> {
            new FetchHotPostsTask().execute();
        });

        //Sumbit Post
        findViewById(R.id.post).setOnClickListener(view -> loadFragment(AddPostFragment.newInstance(userId)));


        findViewById(R.id.notification).setOnClickListener(view -> loadFragment(NotificationFragment.newInstance(user)));

        //User Profile
        findViewById(R.id.profile).setOnClickListener(view -> loadFragment(ProfileFragment.newInstance(user.getId(),userId)));


    }
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_content, fragment)
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }

    public class FetchPostsTask extends AsyncTask<Integer, Void, List<PCMsg>> {

        @Override
        protected List<PCMsg> doInBackground(Integer... params) {
            int userId = params[0];
            return postsService.getPosts(userId);
        }

        @Override
        protected void onPostExecute(List<PCMsg> posts) {
            if (posts != null) {
                loadHomeFragment(posts);
                Toast.makeText(AppActivity.this, "Success to fetch posts.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(AppActivity.this, "Failed to fetch posts.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class FetchHotPostsTask extends AsyncTask<Void, Void, List<PCMsg>> {

        @Override
        protected List<PCMsg> doInBackground(Void... voids) {
            return postsService.getHotPosts();
        }

        @Override
        protected void onPostExecute(List<PCMsg> posts) {
            if (posts != null) {
                loadTrendFragment(posts);
                Toast.makeText(AppActivity.this, "Fetched hot posts successfully.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(AppActivity.this, "Failed to fetch hot posts.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void loadTrendFragment(List<PCMsg> posts) {
        loadFragment(TrendFragment.newInstance(posts, userId));
    }
    private void loadHomeFragment(List<PCMsg> posts) {
        loadFragment(PostListFragment.newInstance(posts,userId));
    }


}