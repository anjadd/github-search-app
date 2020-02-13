package com.test.annjad.githubsearchapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.test.annjad.githubsearchapp.adapters.RepositoriesAdapter;
import com.test.annjad.githubsearchapp.models.Repository;
import com.test.annjad.githubsearchapp.viewmodels.GithubRepositoryViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements RepositoriesAdapter.ListItemClickListener {

    private static final String TAG = "MainActivity";

    @BindView(R.id.etGitTopic)
    EditText mEtGitTopic;

    @BindView(R.id.etGitLanguage)
    EditText mEtGitLanguage;

    @BindView(R.id.rvGitSearchResults)
    RecyclerView mRvResults;

    @BindView(R.id.tvErrorMsg)
    TextView mTvErrorMsg;

    @BindView(R.id.pbLoading)
    ProgressBar mPbLoading;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private String topic, language;
    private List<Repository> mRepoSearchList;
    private RepositoriesAdapter adapter;
    private LinearLayoutManager layoutManager;
    private GithubRepositoryViewModel mGithubRepositoryViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);


        /*
         * Associate the ViewModel with the activity or fragment that it's designed for - declare
         * the ViewModel object and set it up
         */
        mGithubRepositoryViewModel = new ViewModelProvider(this).get(GithubRepositoryViewModel.class);
        /*
         * Next, you need to observe changes done to the objects in our ViewModel - the LiveData objects
         */
        checkIfViewModelIsChanged();
        initRecyclerView();
        initSearchFiltersForTesting();
    }

    private void initSearchFiltersForTesting() {
        mEtGitTopic.setText("MVVM");
        mEtGitLanguage.setText("Android Java");
    }

    private void initRecyclerView() {
    /*
    The layout manager decides how the data in the RecyclerView is displayed.
    The recycler view library provides the following build-in layout managers:
    LinearLayoutManager, GridLayoutManager and StaggeredGridLayoutManager.

    Set up the adapter with empty data, so it won't show error in log
    */
        layoutManager = new LinearLayoutManager(this);
        mRvResults.setLayoutManager(layoutManager);
        adapter = new RepositoriesAdapter(this, null, this);
        mRvResults.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRvResults.getContext(),
                layoutManager.getOrientation());
        mRvResults.addItemDecoration(dividerItemDecoration);
    }

    @OnClick(R.id.btnSearch)
    void onSearchClick(View view) {
        topic = mEtGitTopic.getText().toString();
        language = mEtGitLanguage.getText().toString();

        if (topic.isEmpty() || language.isEmpty()) {
            Toast.makeText(this, "You need to enter some search parameters", Toast.LENGTH_SHORT).show();
        } else {
            hideKeyboard(this);
            showProgressBar();
            mGithubRepositoryViewModel.getRepositories(topic.trim(), language.trim()).observe(this, new Observer<List<Repository>>() {
                @Override
                public void onChanged(List<Repository> repositories) {
                    String isListEmpty = repositories == null ? "empty" : "not empty";
                    Log.d(TAG, "onChanged: Search is clicked, list is: " + isListEmpty);
                    loadResult(repositories);
                }
            });
        }
    }


    private void checkIfViewModelIsChanged() {
        Log.d(TAG, "checkIfViewModelIsChanged: started");
        if (topic == null && language == null){
            return;
        }
        mGithubRepositoryViewModel.getRepositories(topic, language).observe(this, new Observer<List<Repository>>() {
            @Override
            public void onChanged(List<Repository> repositories) {
                Log.d(TAG, "onChanged: started");
//                    adapter.notifyDataSetChanged();
                adapter = new RepositoriesAdapter(getApplicationContext(), repositories, MainActivity.this);
                mRvResults.setAdapter(adapter);
            }
        });

    }

    private void loadResult(List<Repository> repositories) {
        if (repositories == null) {
            mTvErrorMsg.setText(getString(R.string.no_results_msg));
            mTvErrorMsg.setVisibility(View.VISIBLE);
        } else {
            mRepoSearchList = repositories;
            mRvResults.setLayoutManager(layoutManager);
            adapter = new RepositoriesAdapter(this, repositories, this);
            mRvResults.setAdapter(adapter);

            /* This allows the RecyclerView to do some optimisations on your UI,
            like avoiding to invalidate the whole layout when adapter contents change */
            mRvResults.setHasFixedSize(true);
        }
        hideProgressBar();
    }


    private void showProgressBar() {
        mPbLoading.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        mPbLoading.setVisibility(View.GONE);
    }

    /**
     * 9. In the Activity class implement the Click Listener interface and you will have to override its method
     * in order to show a toast message, open a new activity, open a web page etc.
     **/
    @Override
    public void onListItemClick(int clickedItemIndex) {
        /*
        How to open the selected repository from the list in a browser:
        To open a web page, use the ACTION_VIEW action and specify the web URL in the intent data.
        Use Uri.parse to parse the String URL into a Uri
        Create an Intent with Intent.ACTION_VIEW and the webpage Uri as parameters
        */

        Uri repoWebPage = Uri.parse(mRepoSearchList.get(clickedItemIndex).getHtmlUrl());
        Intent intent = new Intent(Intent.ACTION_VIEW, repoWebPage);

        /*
        Now you need to ask the Android system if there's an app that can handle your request
        If there isn't an app installed that can handle your intent, your app will crash
        */
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
