package com.codepath.nytimes.ui.search;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.codepath.nytimes.R;
import com.codepath.nytimes.models.Article;
import com.codepath.nytimes.networking.CallbackResponse;
import com.codepath.nytimes.networking.NYTimesApiClient;

import java.util.List;


/**
 * A fragment representing a list of Items.
 * <p/>
 * interface.
 */
public class ArticleResultFragment extends Fragment {

    private NYTimesApiClient client = new NYTimesApiClient();
    private RecyclerView recyclerView;
    private ContentLoadingProgressBar progressSpinner;
    private String savedQuery;
    private EndlessRecyclerViewScrollListener scrollListener;
    MyArticleResultRecyclerViewAdapter adapter = new MyArticleResultRecyclerViewAdapter();


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ArticleResultFragment() {
    }

    public static ArticleResultFragment newInstance() {
        ArticleResultFragment fragment = new ArticleResultFragment();
        return fragment;
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
//         TODO (checkpoint #4): Uncomment this code when you implement the search menu
        SearchView item = (SearchView) menu.findItem(R.id.action_search).getActionView();
        item.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                loadNewArticlesByQuery(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_article_result_list, container, false);


        recyclerView = view.findViewById(R.id.list);
        progressSpinner = view.findViewById(R.id.progress);
        // Set the adapter
        Context context = view.getContext();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadNextDataFromApi(page);
            }
        };
        recyclerView.addOnScrollListener(scrollListener);
        getActivity().setTitle(getString(R.string.action_bar_search));
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void loadNewArticlesByQuery(String query) {
        Log.d("ArticleResultFragment", "load article with query " + query);
        progressSpinner.show();
        savedQuery = query;
        scrollListener.resetState();
        client.getArticlesByQuery(new CallbackResponse<List<Article>>() {
            @Override
            public void onSuccess(List<Article> models) {
                MyArticleResultRecyclerViewAdapter adapter = (MyArticleResultRecyclerViewAdapter) recyclerView.getAdapter();
                adapter.setNewArticles(models);
                adapter.notifyDataSetChanged();
                Log.d("ArticleResultFragment", "successfully loaded articles");
                progressSpinner.hide();
            }

            @Override
            public void onFailure(Throwable error) {
                Log.d("ArticleResultFragment", "failure load article " + error.getMessage());
                progressSpinner.hide();
            }
        }, query);
    }

    private void loadNextDataFromApi(final int page) {
        client.getArticlesByQuery(new CallbackResponse<List<Article>>() {
            @Override
            public void onSuccess(List<Article> models) {
                MyArticleResultRecyclerViewAdapter adapter = (MyArticleResultRecyclerViewAdapter) recyclerView.getAdapter();
                adapter.addArticles(models);
                adapter.notifyDataSetChanged();
                Log.d("ArticleResultFragment", String.format("successfully loaded articles from page %d", page));
            }

            @Override
            public void onFailure(Throwable error) {
                Log.d("ArticleResultFragment", "failure load article " + error.getMessage());
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }, savedQuery, page);
    }

}
