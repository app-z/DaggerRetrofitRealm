package com.dmi.books.booksshop.UI;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import com.dmi.books.booksshop.Adapter.BooksViewAdapter;
import com.dmi.books.booksshop.App;
import com.dmi.books.booksshop.AppUtils;
import com.dmi.books.booksshop.Model.Book;
import com.dmi.books.booksshop.Model.RealmBook;
import com.dmi.books.booksshop.R;
import com.dmi.books.booksshop.Restapi;
import com.dmi.books.booksshop.helper.OnStartDragListener;
import com.dmi.books.booksshop.helper.RecyclerItemClickListener;
import com.dmi.books.booksshop.helper.SimpleItemTouchHelperCallback;

import java.util.List;

import javax.inject.Inject;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import io.realm.Realm;
import io.realm.Sort;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MainActivity extends AppCompatActivity implements OnStartDragListener {

    private static final String TAG = "MainActivity";
    private static final int BOOK_PAGE_SIZE = 20;

    BooksViewAdapter mBooksViewAdapter;
    private RecyclerView mRecyclerView;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private int mCurrentPage = 0;

    private ItemTouchHelper mItemTouchHelper;
    private LinearLayoutManager mLayoutManager;

    @Inject
    Retrofit retrofit;

    @Inject
    Realm mRealm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ((App) getApplication()).getApplicationComponent().inject(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.listViewBooks);
        mBooksViewAdapter = new BooksViewAdapter(
                mRealm.where(RealmBook.class).findAllSorted("id", Sort.ASCENDING), this);
        mRecyclerView.setAdapter(mBooksViewAdapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mBooksViewAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator() {
            @Override
            public void onAnimationFinished(RecyclerView.ViewHolder viewHolder) {
                super.onAnimationFinished(viewHolder);
                if (viewHolder.getLayoutPosition() == mLayoutManager.findLastCompletelyVisibleItemPosition()) {
                    // mBooksViewAdapter.notifyDataSetChanged();
                    Log.d(TAG, "onAnimationFinished : " + viewHolder.getLayoutPosition());
                }
            }
        });

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(MainActivity.this, mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        RealmBook book = mBooksViewAdapter.getItem(position);

                        String bookId = book.getId();
                        //Intent myIntent = new Intent(MainActivity.this, DetailedActivity.class);
                        //myIntent.putExtra(DetailedActivity.BOOK_ID_PARAM, bookId);
                        //startActivity(myIntent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                })
        );

        // Set up swipe refresh layout.
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_container);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mCurrentPage = 0;
                if (!mRealm.isInTransaction())
                    mRealm.beginTransaction();
                mRealm.deleteAll();
                mRealm.commitTransaction();
                fetchBooks(false);
            }
        });


    }

    /*
        Get data from feed (Books)
     */
    private void fetchBooks(final boolean nextReq) {

        if (!mSwipeRefreshLayout.isRefreshing())
            findViewById(R.id.loadingIndicator).setVisibility(View.VISIBLE);

        final int pageIdx = nextReq ? mCurrentPage + 1 : mCurrentPage;
        Call<List<Book>> books = retrofit.create(Restapi.class).getSecureListBooks(pageIdx * BOOK_PAGE_SIZE, BOOK_PAGE_SIZE);

        //Enque the call
        books.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                List<Book> listBooks = response.body();
                if (AppUtils.DEBUG) {   // Test begin
                    for (Book book : listBooks) {
                        String name = book.getTitle();
                        Log.i(TAG, "name : " + name);
                    }
                }   // Test end
                if (listBooks.size() >= BOOK_PAGE_SIZE && nextReq)
                    mCurrentPage++;

                if (listBooks.size() > 0) {
                    //updateLocalProviderData(listBooks);
                    mRealm.beginTransaction();
                    for (Book book : listBooks) {
                        RealmBook bookRealm = mRealm.createObject(RealmBook.class);
                        bookRealm.setId(book.getId());
                        bookRealm.setTitle(book.getTitle());
                        bookRealm.setPrice(book.getPrice());
                        bookRealm.setLink(book.getLink());
                    }
                    mRealm.commitTransaction();
                }

                findViewById(R.id.loadingIndicator).setVisibility(View.GONE);
                mSwipeRefreshLayout.setRefreshing(false);
                Crouton.makeText(MainActivity.this,
                        "Book list cache updated ", Style.INFO).show();
            }


            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                findViewById(R.id.loadingIndicator).setVisibility(View.GONE);
                mSwipeRefreshLayout.setRefreshing(false);
                Crouton.makeText(MainActivity.this,
                        R.string.error_response_message, Style.ALERT).show();
                Log.e(TAG, t.toString());

            }
        });

    }


    public void addBook(View view) {
        if (!mRealm.isInTransaction())
            mRealm.beginTransaction();
        RealmBook book = mRealm.createObject(RealmBook.class);
        book.setId("1" + mRealm.where(RealmBook.class).count());
        book.setTitle("Added !!");
        book.setLink("http;//rerer/rerer/trytry");
        book.setPrice(12332d);
        mRealm.commitTransaction();
        mBooksViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void onDeleted(int position, String id) {
        RealmBook realmBooks = mRealm.where(RealmBook.class).equalTo("id", id).findFirst();
        if (realmBooks != null) {
            if (!mRealm.isInTransaction())
                mRealm.beginTransaction();

            realmBooks.deleteFromRealm();

            mRealm.commitTransaction();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }
}
