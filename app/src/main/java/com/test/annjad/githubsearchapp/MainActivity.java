package com.test.annjad.githubsearchapp;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindFloat;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.etGitTopic)
    EditText mEtGitTopic;

    @BindView(R.id.etGitLanguage)
    EditText mEtGitLanguage;

    @BindView(R.id.lvGitSearchResults)
    ListView mLvGitResults;

    @BindView(R.id.tvErrorMsg)
    TextView mTvErrorMsg;

    @BindView(R.id.pbLoading)
    ProgressBar mPbLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
    }



}
