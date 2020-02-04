package com.test.annjad.githubsearchapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity {

    @BindView(R.id.tvRepoName)
    TextView mTvRepoName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        loadDetails();
    }


    private void loadDetails() {
        Intent mainRepoIntent = getIntent();
        if (mainRepoIntent.hasExtra("repoName")) {
            String selectedRepo = mainRepoIntent.getStringExtra("repoName");
            mTvRepoName.setText(selectedRepo);
        }
    }
}
