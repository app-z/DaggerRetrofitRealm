package com.dmi.books.booksshop.Adapter;

import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dmi.books.booksshop.Model.RealmBook;
import com.dmi.books.booksshop.R;
import com.dmi.books.booksshop.helper.ItemTouchHelperAdapter;
import com.dmi.books.booksshop.helper.OnStartDragListener;

import butterknife.ButterKnife;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class BooksViewAdapter extends RecyclerView.Adapter<BooksViewAdapter.ViewHolder>
        implements ItemTouchHelperAdapter, RealmChangeListener {

    private final OnStartDragListener mDragStartListener;
    private final String curency;
    RealmResults<RealmBook> mBooks;

    public BooksViewAdapter(RealmResults<RealmBook> books, OnStartDragListener dragStartListener) {
        this.mBooks = books;
        mBooks.addChangeListener(this);
        mDragStartListener = dragStartListener;
        curency = "$";
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {

        // Start a drag whenever the handle view it touched
        viewHolder.title.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onStartDrag(viewHolder);
                }
                return false;
            }
        });

        String title = mBooks.get(position).getTitle();
        viewHolder.title.setText(title);

        String price = String.valueOf(mBooks.get(position).getPrice());
        viewHolder.price.setText(curency + String.valueOf(price));

    }

    @Override
    public int getItemCount() {
        return mBooks.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_row_layout, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onItemDismiss(int position) {
        mDragStartListener.onDeleted(position, getItem(position).getId());
        notifyItemRemoved(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        return false;
    }

    public RealmBook getItem(int position) {
        return mBooks.get(position);
    }

    @Override
    public void onChange(Object element) {
        // Because delete animation should finish first
        // notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        //        private TextView id;
        private TextView title;
        private TextView price;
        //        private TextView link;

        public ViewHolder(View view) {
            super(view);
            // id = (TextView) view.findViewById(R.id.id);
            title = (TextView) view.findViewById(R.id.title);
            price = (TextView) view.findViewById(R.id.price);
            // link = (TextView) view.findViewById(R.id.link);
        }

    }
}
