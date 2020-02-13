package com.test.annjad.githubsearchapp.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.test.annjad.githubsearchapp.GithubApi;
import com.test.annjad.githubsearchapp.RetrofitClient;
import com.test.annjad.githubsearchapp.models.Repositories;
import com.test.annjad.githubsearchapp.models.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class GithubSearchRepository {
    /**
     * Here you need to access the web service - REST API or access a database cache
     **/

    private static final String TAG = "GithubSearchRepository";

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    //this is the data that we will fetch asynchronously
    private MutableLiveData<List<Repository>> repoSearchList = new MutableLiveData<>();

    private static GithubSearchRepository instance;

    public static GithubSearchRepository getInstance() {
        if (instance == null) {
            instance = new GithubSearchRepository();
        }
        return instance;
    }

    public MutableLiveData<List<Repository>> getGithubSearchRepositories(String topic, String language) {

        //API endpoint: https://api.github.com/search/repositories?q=smoothie&language:java&sort=stars&order=desc
        Map<String, String> filters = new HashMap<>();
        if (!topic.isEmpty()) {
            topic = topic.replace(" ", "+");
            filters.put("q", topic);
        }
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
//                        repoSearchList = new MutableLiveData<>();
                        repoSearchList.setValue(repositories.getRepositories());
                        Log.d(TAG, "onSuccess: repository 1: " +
                                repositories.getRepositories().get(0).getName());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.getMessage());
                    }
                });
        if (repoSearchList != null && repoSearchList.getValue() != null)
            Log.d(TAG, "getGithubSearchRepositories: " + repoSearchList.getValue().get(0).getName());
        return repoSearchList;
    }
}
