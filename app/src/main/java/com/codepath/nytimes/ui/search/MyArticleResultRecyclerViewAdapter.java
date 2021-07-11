package com.codepath.nytimes.ui.search;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import com.codepath.nytimes.models.Article;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Recycler view of articles from search result
 */
public class MyArticleResultRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<Article> articleList = new ArrayList<>();
    public static final int VIEW_TYPE_LOADING = 0;
    public static final int VIEW_TYPE_ARTICLE = 1;
    public static final int VIEW_TYPE_FIRST_PAGE_ARTICLE = 2;
    SharedPreferences sharedPreferences;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        sharedPreferences = parent.getContext().getSharedPreferences("Settings", Context.MODE_PRIVATE);

        if (viewType == VIEW_TYPE_ARTICLE) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_article_result, parent, false);
            view.setBackgroundColor(Color.parseColor(sharedPreferences.getString("BackgroundColor", "#FFFFFF")));
            return new ArticleViewHolder(view);
        } else if (viewType == VIEW_TYPE_FIRST_PAGE_ARTICLE) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_article_result_first_page, parent, false);
            view.setBackgroundColor(Color.parseColor(sharedPreferences.getString("BackgroundColor", "#FFFFFF")));
            return new FirstPageArticleViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.article_progress, parent, false);
            view.setBackgroundColor(Color.parseColor(sharedPreferences.getString("BackgroundColor", "#FFFFFF")));
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FirstPageArticleViewHolder) {
            // do something
            FirstPageArticleViewHolder articleViewHolder = (FirstPageArticleViewHolder) holder;
            articleViewHolder.firstPageHeader.setText(holder.itemView.getContext().getString(R.string.first_page, articleList.get(position).sectionName));
            articleViewHolder.firstPageHeader.setTextColor(Color.parseColor(sharedPreferences.getString("TextColor", "#121212")));
        }
        if (holder instanceof ArticleViewHolder) {
            Article article = articleList.get(position);
            final ArticleViewHolder articleViewHolder = (ArticleViewHolder) holder;
            articleViewHolder.headlineView.setText(article.headline.main);
            articleViewHolder.headlineView.setTextColor(Color.parseColor(sharedPreferences.getString("TextColor", "#121212")));
            articleViewHolder.snippetView.setText(article.snippet);
            articleViewHolder.snippetView.setTextColor(Color.parseColor(sharedPreferences.getString("TextColor", "#121212")));
            try {
                SimpleDateFormat utcDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+SSS", Locale.getDefault());
                Date date = utcDateFormat.parse(article.publishDate);
                SimpleDateFormat newDateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
                articleViewHolder.dateView.setText(newDateFormat.format(date));
                articleViewHolder.dateView.setTextColor(Color.parseColor(sharedPreferences.getString("TextColor", "#121212")));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (article.multimedia.size() > 0) {
                Glide.with(articleViewHolder.mView).load("http://static01.nytimes.com/" + article.multimedia.get(0).url)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.imagenotfound)
                        .listener(new RequestListener<Drawable>() {
                                      ArticleViewHolder aViewHolder = articleViewHolder;
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
                        .into(articleViewHolder.articleImageView);
            } else {
                articleViewHolder.articleImageView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position >= articleList.size()) {
            return VIEW_TYPE_LOADING;
        } else if ("1".equals(articleList.get(position).printPage)) {
            return VIEW_TYPE_FIRST_PAGE_ARTICLE;
        } else {
            return VIEW_TYPE_ARTICLE;
        }
    }

    @Override
    public int getItemCount() {
        return (articleList.size() == 0) ? 0 : articleList.size() + 1;
    }

    void setNewArticles(List<Article> articles) {
        articleList.clear();
        articleList.addAll(articles);
    }

    void addArticles(List<Article> articles) {
        articleList.addAll(articles);
    }

    static class LoadingViewHolder extends RecyclerView.ViewHolder {

        LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    static class ArticleViewHolder extends RecyclerView.ViewHolder {
        final TextView headlineView;
        final TextView snippetView;
        final TextView dateView;
        public ImageView articleImageView;
        public final View mView;
        ArticleViewHolder(View view) {
            super(view);
            mView = view;
            dateView = view.findViewById(R.id.article_pub_date);
            headlineView = view.findViewById(R.id.article_headline);
            snippetView = view.findViewById(R.id.article_snippet);
            articleImageView  = view.findViewById(R.id.article_photo);
        }
    }

    static class FirstPageArticleViewHolder extends ArticleViewHolder {
        final TextView firstPageHeader;

        FirstPageArticleViewHolder(View view) {
            super(view);
            firstPageHeader = view.findViewById(R.id.first_page_header);
        }
    }
}
