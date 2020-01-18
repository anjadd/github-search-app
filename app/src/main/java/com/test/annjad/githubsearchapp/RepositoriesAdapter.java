package com.test.annjad.githubsearchapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RepositoriesAdapter extends RecyclerView.Adapter<RepositoriesAdapter.RepositoryViewHolder> {

    private Context mContext;
    private List<Repository> mListRepos;

    public RepositoriesAdapter(Context context, List<Repository> listRepos) {
        /*
        You can get the data source using the constructor like this.
        You can send the data set to the Adapter through his constructor
        */
        mContext = context;
        mListRepos = listRepos;
    }

    @NonNull
    @Override
    public RepositoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // this method creates the view (list item) by inflating it from the XML layout

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);
        return new RepositoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RepositoryViewHolder holder, final int position) {
        // this method populates the list item with data

        holder.repoName.setText(mListRepos.get(position).getName());
        holder.repoLanguage.setText(mListRepos.get(position).getLanguage());
        holder.repoStars.setText(String.valueOf(mListRepos.get(position).getScore()));

        holder.llListItem.setOnClickListener(view -> Toast.makeText(mContext, "You clicked on: " + mListRepos.get(position).getName(), Toast.LENGTH_SHORT).show());
    }

    @Override
    public int getItemCount() {
        //tell the adapter how many list items are in your list
        return mListRepos == null ? 0 : mListRepos.size();
    }

    class RepositoryViewHolder extends RecyclerView.ViewHolder {

        // You have to declare all your views here

        /*
        You need to declare the parent view also, so you can add an onClickListener on it
        (handle the click on list item rows)
        */

        @BindView(R.id.llListItem)
        LinearLayout llListItem;

        @BindView(R.id.repoName)
        TextView repoName;

        @BindView(R.id.repoLanguage)
        TextView repoLanguage;

        @BindView(R.id.repoStars)
        TextView repoStars;

        public RepositoryViewHolder(@NonNull View itemView) {
            /*
            The input itemView is actually the linear layout which represents one row in our app
            You can use Butterknife outside of an Activity (perform binding on arbitrary objects)
            but you need to supply your own view root, besides the context.
            */
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

}
