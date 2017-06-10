package com.android.shubham.yify;

/**
 * Created by shubham on 09-Jun-17.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by shubham on 17-May-17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHandler> {
    private ArrayList<MovieItem> mMovieData;
    MovieAdapter(){

    }
    /*final*/ private ListItemClickListener mOnClickListener;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }
    public MovieAdapter( ListItemClickListener listener) {
        mOnClickListener = listener;

    }
    @Override
    public MovieAdapterViewHandler onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.rv_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new MovieAdapterViewHandler(view);

    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHandler holder, int position) {
        MovieItem movie = mMovieData.get(position);
        holder.tv_name.setText(movie.getName());

        Picasso.with(holder.iv_poster.getContext())
                .load(movie.getPosterUrl()).resize(500,500).centerCrop().into(holder.iv_poster);
        // holder.bind(mMovieList.get(position), mListener);
    }

    @Override
    public int getItemCount() {

        if (null == mMovieData) return 0;
        return mMovieData.size();
    }

    public class MovieAdapterViewHandler extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final TextView tv_name;
        public final ImageView iv_poster;

        MovieAdapterViewHandler(View view){
            super(view);
            tv_name = (TextView) view.findViewById(R.id.tv_item);
            iv_poster = (ImageView) view.findViewById(R.id.iv_poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }

    void setmMovieData(ArrayList<MovieItem> x){
        mMovieData = x;
        notifyDataSetChanged();
    }

}
