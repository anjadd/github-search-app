package com.test.annjad.githubsearchapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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

    @BindView(R.id.lvGitSearchResults)
    ListView mLvGitResults;

    @BindView(R.id.tvErrorMsg)
    TextView mTvErrorMsg;

    @BindView(R.id.pbLoading)
    ProgressBar mPbLoading;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);
    }

    @OnClick(R.id.btnSearch)
    void onSearchClick(View view) {

        String topic = mEtGitTopic.getText().toString();
        String language = mEtGitLanguage.getText().toString();
        if (topic.isEmpty() || language.isEmpty()) {
            Toast.makeText(this, "You need to enter some search parameters", Toast.LENGTH_SHORT).show();
        } else {
            performGithubSearch(topic.trim(), language.trim());
            mPbLoading.setVisibility(View.VISIBLE);
        }
    }

    private void performGithubSearch(String topic, String language) {

        //API endpoint: https://api.github.com/search/repositories?q=smoothie&language:java&sort=stars&order=desc
        Map<String, String> filters = new HashMap<>();
        filters.put("q", topic);
        filters.put("language", language);
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
            RepositoriesAdapter adapter = new RepositoriesAdapter(this, R.layout.listview_item, repositories.getRepositories());
            mLvGitResults.setAdapter(adapter);
        }
        mPbLoading.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onDestroy() {
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
        super.onDestroy();
    }

}
