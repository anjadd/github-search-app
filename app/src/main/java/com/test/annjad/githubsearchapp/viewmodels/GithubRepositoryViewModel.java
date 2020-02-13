package com.test.annjad.githubsearchapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.test.annjad.githubsearchapp.models.Repository;
import com.test.annjad.githubsearchapp.repositories.GithubSearchRepository;

import java.util.List;

public class GithubRepositoryViewModel extends ViewModel {

    private static final String TAG = "GithubRepositoryViewMod";

    /**
     * MutableLiveData can be changed, thus the name mutable.
     * LiveData is immutable, thus it can't be directly changed, it can only be observed.
     * You can observe changes to LiveData, but you can't change LiveData
     */
    private MutableLiveData<List<Repository>> mRepositories;

    private GithubSearchRepository mGitRepo = GithubSearchRepository.getInstance();

/*    public void init() {
        if (mRepositories != null) {
            return;
        }
        mRepositories = new MutableLiveData<>();
    }*/

    /**
     * LiveData is immutable, thus it can't be directly changed, it can only be observed.
     * You can observe changes to LiveData, but you can't change LiveData
     * So you will call this method to get the data
     */
    public LiveData<List<Repository>> getRepositories(String topic, String language) {
        if(mRepositories == null){
            mRepositories = new MutableLiveData<>();
            getRepositoriesFromGithubSearchRepo(topic, language);
        }
        return mRepositories;
    }

    public void getRepositoriesFromGithubSearchRepo(String topic, String language) {
        mRepositories.setValue((mGitRepo.getGithubSearchRepositories(topic, language)).getValue());
    }

}
