package edu.nus.adproject.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import edu.nus.adproject.R;
import edu.nus.adproject.adapters.PostAdapter;

public class NewFeed extends AppCompatActivity
        implements AdapterView.OnItemClickListener {
    private final String[] posts = {};

    private final String[] profiles = {};

    private final String[] comments = {};

    private final int[] likes = {};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_feed);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//        ListView mPostLV = findViewById(R.id.postLV);
//        if (mPostLV != null) {
//            mPostLV.setAdapter(new PostAdapter(this, posts, profiles, comments, likes));
//            mPostLV.setOnItemClickListener(this);
//        }
    }

    @Override
    public void onItemClick(AdapterView<?> av, View v, int pos, long id) {

    }
}