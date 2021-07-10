package com.codepath.nytimes.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.nytimes.R;
import com.codepath.nytimes.models.BestSellerBook;
import com.codepath.nytimes.models.PopularArticle;
import com.codepath.nytimes.networking.CallbackResponse;
import com.codepath.nytimes.networking.NYTimesApiClient;
import com.codepath.nytimes.ui.books.BestSellerBooksFragment;
import com.codepath.nytimes.ui.books.BestSellerBooksRecyclerViewAdapter;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ContentLoadingProgressBar progressBar = (ContentLoadingProgressBar) view.findViewById(R.id.progress);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rvPopularArticles);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        view.findViewById(R.id.flPopularArticle).setBackgroundColor(Color.parseColor(sharedPreferences.getString("BorderColor", "#121212")));

        Context context = view.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        updateAdapter(progressBar, recyclerView);
        getActivity().setTitle(getString(R.string.action_bar_home));
        return view;

        //getActivity().setTitle(getString(R.string.action_bar_home));
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_home, container, false);

    }
    private void updateAdapter(final ContentLoadingProgressBar progressBar, final RecyclerView recyclerView) {
        progressBar.show();
        NYTimesApiClient nyTimesApiClient = new NYTimesApiClient();
        nyTimesApiClient.getPopularArticlesByQuery(new CallbackResponse<List<PopularArticle>>() {
            @Override
            public void onSuccess(List<PopularArticle> models) {
                progressBar.hide();
                recyclerView.setAdapter(new PopularArticleRecyclerViewAdapter(models));
                Log.d("BestSellerBooksFragment", "response successful");
            }

            @Override
            public void onFailure(Throwable error) {
                progressBar.hide();
                Log.e("BestSellerBooksFragment", error.getMessage());
            }
        });

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}