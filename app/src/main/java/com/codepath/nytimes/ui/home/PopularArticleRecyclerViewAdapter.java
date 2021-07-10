package com.codepath.nytimes.ui.home;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.codepath.nytimes.R;
import com.codepath.nytimes.models.MetaData;
import com.codepath.nytimes.models.PopularArticle;
import com.codepath.nytimes.ui.books.OnListFragmentInteractionListener;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PopularArticleRecyclerViewAdapter extends RecyclerView.Adapter<PopularArticleRecyclerViewAdapter.PopularArticleViewHolder>{
    public List<PopularArticle> articleList;





    public PopularArticleRecyclerViewAdapter(List<PopularArticle> articles) {

        this.articleList = articles;


    }

    @NonNull
    @Override
    public PopularArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        // Inflate the custom layout
        View articleView = inflater.inflate(R.layout.fragment_home_popular_article, parent, false);
        // Return a new holder instance
        return new PopularArticleViewHolder(articleView);




    }

    @Override
    public void onBindViewHolder(@NonNull final PopularArticleViewHolder holder, int position) {


        // Get the data model based on position
        PopularArticle article = articleList.get(position);

        // Set item views based on views and data model
        holder.headlineView.setText(article.title);
        holder.snippetView.setText(article.articleAbstract);


        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Date date = new Date();
        try {
            date = (Date)formatter.parse(article.publishDate);
        } catch (ParseException e) {
            Log.e("PArticleRRVAdapter", "onBindViewHolder", e);

        }
        SimpleDateFormat newFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
        String publishedDate = newFormat.format(date);
        holder.dateView.setText(publishedDate);

        if (article.multimedia.size() > 0) {
            List<MetaData> media = article.multimedia.get(0).mediaMetaData;

            if (media.size() > 0) {
                Glide.with(holder.mView).load(media.get(media.size() - 1).url)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.imagenotfound)
                        .listener(new RequestListener<Drawable>() {
                                      PopularArticleViewHolder aViewHolder = holder;
                                      @Override
                                      public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                          // log exception
                                          Log.e("ArticleResultsRVAdapter", "Error loading image", e);
                                          aViewHolder.articleImageView.setVisibility(View.GONE);
                                          return false; // important to return false so the error placeholder can be placed
                                      }

                                      @Override
                                      public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                          return false;
                                      }
                                  }
                        )
                        .into(holder.articleImageView);
            } else {
                holder.articleImageView.setVisibility(View.GONE);
            }

        } else {
            holder.articleImageView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class PopularArticleViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public TextView headlineView;
        public TextView snippetView;
        public TextView dateView;
        public ImageView articleImageView;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public PopularArticleViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            mView = itemView;
            dateView = itemView.findViewById(R.id.popular_article_pub_date);
            headlineView = itemView.findViewById(R.id.popular_article_headline);
            snippetView = itemView.findViewById(R.id.popular_article_snippet);
            articleImageView  = itemView.findViewById(R.id.popular_article_photo);
        }
    }






}

