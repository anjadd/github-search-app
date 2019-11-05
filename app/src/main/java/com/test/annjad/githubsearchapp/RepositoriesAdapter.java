package com.test.annjad.githubsearchapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class RepositoriesAdapter extends ArrayAdapter<Repository> {

    public RepositoriesAdapter(@NonNull Context context, int resource, @NonNull List<Repository> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listviewItem = convertView;
        if (listviewItem == null) {
            listviewItem = LayoutInflater.from(getContext()).inflate(R.layout.listview_item, parent, false);
        }

        Repository currentRepository = getItem(position);

        if (currentRepository != null) {
            TextView mRepoName = listviewItem.findViewById(R.id.repoName);
            mRepoName.setText(currentRepository.getName());

            TextView mRepoLanguage = listviewItem.findViewById(R.id.repoLanguage);
            mRepoLanguage.setText(currentRepository.getLanguage());

            TextView mRepoStars = listviewItem.findViewById(R.id.repoStars);
            mRepoStars.setText(String.valueOf(currentRepository.getScore()));
        }

        return listviewItem;
    }
}
