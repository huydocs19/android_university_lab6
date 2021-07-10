package com.codepath.nytimes;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.codepath.nytimes.ui.books.BestSellerBooksFragment;
import com.codepath.nytimes.ui.home.HomeFragment;
import com.codepath.nytimes.ui.search.ArticleResultFragment;
import com.codepath.nytimes.ui.settings.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private static String HOME_TAG = "home";
    private static String BOOKS_TAG = "books";
    private static String ARTICLES_TAG = "articles";
    private static String SETTINGS_TAG = "settings";
    private final String SELECTED_ITEM_ID_KEY = "menuItemSelected";

    BottomNavigationView bottomNavigationView;
    HomeFragment homeFragment;
    ArticleResultFragment articleResultFragment;
    BestSellerBooksFragment bestSellerBooksFragment;
    SettingsFragment settingsFragment;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final FragmentManager fragmentManager = getSupportFragmentManager();
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        articleResultFragment = (ArticleResultFragment) fragmentManager.findFragmentByTag(ARTICLES_TAG);
        if (articleResultFragment == null) {
            articleResultFragment = ArticleResultFragment.newInstance();
        }
        bestSellerBooksFragment = (BestSellerBooksFragment) fragmentManager.findFragmentByTag(BOOKS_TAG);
        if (bestSellerBooksFragment == null) {
            bestSellerBooksFragment = BestSellerBooksFragment.newInstance();
        }
        homeFragment = (HomeFragment) fragmentManager.findFragmentByTag(HOME_TAG);
        if (homeFragment == null) {
            homeFragment = HomeFragment.newInstance();
        }
        settingsFragment = (SettingsFragment) fragmentManager.findFragmentByTag(SETTINGS_TAG);
        if (settingsFragment == null) {
            settingsFragment = SettingsFragment.newInstance();
        }


        // handle navigation selection
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment fragment;
                        String TAG ="";
                        switch (item.getItemId()) {
                            case R.id.nav_article_search:
                                fragment = articleResultFragment;
                                TAG = ARTICLES_TAG;
                                break;
                            case R.id.nav_settings:
                                fragment = settingsFragment;
                                TAG = SETTINGS_TAG;
                                break;
                            case R.id.nav_bestselling_books:
                                fragment = bestSellerBooksFragment;
                                TAG = BOOKS_TAG;
                                break;
                            case R.id.nav_home:
                            default:
                                fragment = homeFragment;
                                TAG = HOME_TAG;
                                break;
                        }
                        fragmentManager.beginTransaction().replace(R.id.fragment_content, fragment, TAG).commit();
                        return true;
                    }
                });

        if (savedInstanceState != null) {
            int selected_bottom_item = savedInstanceState.getInt(SELECTED_ITEM_ID_KEY);
            bottomNavigationView.setSelectedItemId(selected_bottom_item);
        } else {
            // Set default selection
            bottomNavigationView.setSelectedItemId(R.id.nav_home);
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(SELECTED_ITEM_ID_KEY, bottomNavigationView.getSelectedItemId());
        super.onSaveInstanceState(outState);
    }
}
