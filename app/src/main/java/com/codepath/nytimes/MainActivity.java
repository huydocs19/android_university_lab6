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

    HomeFragment homeFragment = HomeFragment.newInstance();
    ArticleResultFragment articleResultFragment = ArticleResultFragment.newInstance();
    BestSellerBooksFragment bestSellerBooksFragment = BestSellerBooksFragment.newInstance();
    SettingsFragment settingsFragment = SettingsFragment.newInstance();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final FragmentManager fragmentManager = getSupportFragmentManager();
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);


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
        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
    }
}
