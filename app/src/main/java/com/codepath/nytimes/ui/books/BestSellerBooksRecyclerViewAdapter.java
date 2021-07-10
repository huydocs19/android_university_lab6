package com.codepath.nytimes.ui.books;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.nytimes.R;
import com.codepath.nytimes.models.BestSellerBook;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link BestSellerBook} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class BestSellerBooksRecyclerViewAdapter extends RecyclerView.Adapter<BestSellerBooksRecyclerViewAdapter.BookViewHolder> {

    private final List<BestSellerBook> books;
    private final OnListFragmentInteractionListener mListener;
    SharedPreferences sharedPreferences;

    public BestSellerBooksRecyclerViewAdapter(List<BestSellerBook> items, OnListFragmentInteractionListener listener) {
        books = items;
        mListener = listener;
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_best_seller_book, parent, false);
        sharedPreferences = parent.getContext().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        view.setBackgroundColor(Color.parseColor(sharedPreferences.getString("BackgroundColor", "#FFFFFF")));
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BookViewHolder holder, int position) {
        BestSellerBook bestSellerBook = books.get(position);
        holder.mItem = bestSellerBook;
        holder.mBookTitle.setText(bestSellerBook.title);
        holder.mBookTitle.setTextColor(Color.parseColor(sharedPreferences.getString("TextColor", "#121212")));
        holder.mBookAuthor.setText(bestSellerBook.author);
        holder.mBookAuthor.setTextColor(Color.parseColor(sharedPreferences.getString("TextColor", "#121212")));
        holder.mBookRanking.setText(String.format("%d", bestSellerBook.rank));
        holder.mBookRanking.setTextColor(Color.parseColor(sharedPreferences.getString("TextColor", "#121212")));
        holder.mBookDescription.setText(bestSellerBook.description);
        holder.mBookDescription.setTextColor(Color.parseColor(sharedPreferences.getString("TextColor", "121212")));
        Glide.with(holder.mView)
                .load(bestSellerBook.bookImageUrl)
                .centerInside()
                .into(holder.mBookImage);

        holder.mBuyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(holder.mItem);
            }
        });
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onItemClick(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public class BookViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mBookTitle;
        public final ImageView mBookImage;
        public final TextView mBookAuthor;
        private final TextView mBookRanking;
        private final TextView mBookDescription;
        private final Button mBuyButton;
        public BestSellerBook mItem;

        public BookViewHolder(View view) {
            super(view);
            mView = view;
            mBookImage = (ImageView) view.findViewById(R.id.book_image);
            mBookTitle = (TextView) view.findViewById(R.id.book_title);
            mBookAuthor = (TextView) view.findViewById(R.id.book_author);
            mBookRanking = (TextView) view.findViewById(R.id.ranking);
            mBookDescription = (TextView) view.findViewById(R.id.book_description);
            mBuyButton = (Button) view.findViewById(R.id.buy_button);
        }

        @Override
        public String toString() {
            return mBookTitle.toString() + " '" + mBookAuthor.getText() + "'";
        }
    }
}
