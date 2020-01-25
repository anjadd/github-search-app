package com.test.annjad.githubsearchapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RepositoriesAdapter extends RecyclerView.Adapter<RepositoriesAdapter.RepositoryViewHolder> {

    private Context mContext;
    private List<Repository> mListRepos;
    final private ListItemClickListener mOnClickListener;

    /*
    Create a custom interface that specifies the listener’s behavior.
    Within that interface, define a void method called onListItemClick that takes
    an int as a parameter, which is the index of the list item that was clicked
    */
    public interface ListItemClickListener {

        void onListItemClick(int clickedItemIndex);
    }

    public RepositoriesAdapter(Context context, List<Repository> listRepos, ListItemClickListener clickListener) {
        /*
        You can get the data source using the constructor like this.
        You can send the data set to the Adapter through his constructor
        */
        mContext = context;
        mListRepos = listRepos;
        mOnClickListener = clickListener;
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

        /*
        In onBindView, you can have access to the the current clicked item and thus open a new activity from here and pass your data.
        But it is bad because:
         - It is not a good practice to open an Activity from a viewholder context
         - Note that onBindViewHolder is called as your views are populated during scrolling,
           meaning you will have numerous calls to setOnClickListener.

        holder.llListItem.setOnClickListener(view -> Toast.makeText(mContext, "You clicked on: " + mListRepos.get(position).getName(), Toast.LENGTH_SHORT).show());
        */
    }

    @Override
    public int getItemCount() {
        //tell the adapter how many list items are in your list
        return mListRepos == null ? 0 : mListRepos.size();
    }

    class RepositoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // You have to declare all your views here

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
            Also, you need to set a click listener to the ViewHolder, by calling setOnClickListener
            on the View passed into the constructor (use 'this' as the OnClickListener)
            */
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        /*
        After the adapter has access to a click listener, we need to pass it to a ViewHolder
        so that the view can invoke it. That is done by implementing a View.OnClickListener
        in the ViewHolder class and overriding its method onClick().
        In the onClick() method, pass position of the clicked item (getAdapterPosition())
        to mOnClickListener via its onListItemClick method.
        */
        @Override
        public void onClick(View view) {
            int clickedItemIndex = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedItemIndex);
        }
    }

}
