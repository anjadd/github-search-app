package com.test.annjad.githubsearchapp;

import android.app.Activity;
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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

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

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    List<Repository> repoSearchList;
    RepositoriesAdapter adapter;
    LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);

        /*
        The layout manager decides how the data in the RecyclerView is displayed.
        The recycler view library provides the following build-in layout managers:
        LinearLayoutManager, GridLayoutManager and StaggeredGridLayoutManager.

        Set up the adapter with empty data, so it won't show error in log
        */
        layoutManager = new LinearLayoutManager(this);
        mRvResults.setLayoutManager(layoutManager);
        adapter = new RepositoriesAdapter(this, null);
        mRvResults.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRvResults.getContext(),
                layoutManager.getOrientation());
        mRvResults.addItemDecoration(dividerItemDecoration);
    }

    @OnClick(R.id.btnSearch)
    void onSearchClick(View view) {
        String topic = mEtGitTopic.getText().toString();
        String language = mEtGitLanguage.getText().toString();

        if (topic.isEmpty() || language.isEmpty()) {
            Toast.makeText(this, "You need to enter some search parameters", Toast.LENGTH_SHORT).show();
        } else {
            hideKeyboard(this);
            mPbLoading.setVisibility(View.VISIBLE);
            performGithubSearch(topic.trim(), language.trim());
        }
    }

    private void performGithubSearch(String topic, String language) {

        //API endpoint: https://api.github.com/search/repositories?q=smoothie&language:java&sort=stars&order=desc
        Map<String, String> filters = new HashMap<>();
        filters.put("q", topic);
        if (!language.isEmpty()) {
            language = language.replace(" ", "+");
            filters.put("language", language);
        }
        filters.put("sort", "stars");
        filters.put("order", "desc");

        Retrofit retrofit = RetrofitClient.getRetrofitClient();
        GithubApi service = retrofit.create(GithubApi.class);
        Single<Repositories> searchRepositories = service.getGithubSearchRepositories(filters);

        searchRepositories.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Repositories>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(Repositories repositories) {
                        loadResult(repositories);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mPbLoading.setVisibility(View.INVISIBLE);
                        mTvErrorMsg.setText(getString(R.string.api_request_fail_error_msg));
                        mTvErrorMsg.setVisibility(View.VISIBLE);
                        Log.e("Anja", e.getMessage());
                    }
                });
    }

    private void loadResult(Repositories repositories) {
        if (repositories == null) {
            mTvErrorMsg.setText(getString(R.string.no_results_msg));
            mTvErrorMsg.setVisibility(View.VISIBLE);
        } else {
            repoSearchList = repositories.getRepositories();
            mRvResults.setLayoutManager(layoutManager);
            adapter = new RepositoriesAdapter(this, repositories.getRepositories());
            mRvResults.setAdapter(adapter);

            /* This allows the RecyclerView to do some optimisations on your UI,
            like avoiding to invalidate the whole layout when adapter contents change */
            mRvResults.setHasFixedSize(true);
        }
        mPbLoading.setVisibility(View.GONE);
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

    @Override
    protected void onDestroy() {
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
        super.onDestroy();
    }

}
