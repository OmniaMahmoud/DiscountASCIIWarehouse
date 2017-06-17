package mobile.ucis.discountasciiwarehouse;

import android.annotation.SuppressLint;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SearchView;

import java.lang.reflect.Field;

import item.ItemLoader;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        /*definitions*/
        final GridView allItemsGrid = (GridView) findViewById(R.id.grid);
        final CheckBox stockOnly = (CheckBox) findViewById(R.id.checkBox);
        final SearchView tagSearch = (SearchView) findViewById(R.id.searchView);
        final SwipeRefreshLayout loadMore = (SwipeRefreshLayout) findViewById(R.id.loadMore);
        final ItemLoader loader = new ItemLoader(getApplicationContext(), allItemsGrid);

        tagSearch.setIconified(false);
        /*this part for removing the search view close button*/
        Field searchField = null;
        try {
            searchField = SearchView.class.getDeclaredField("mCloseButton");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        searchField.setAccessible(true);
        ImageView mSearchCloseButton = null;
        try {
            mSearchCloseButton = (ImageView) searchField.get(tagSearch);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if (mSearchCloseButton != null) {
            mSearchCloseButton.setEnabled(false);
        }
        /*loading home items*/
        loader.loadHomeItems(0, 0, true);
        /*loading more items on swipe to bottom each swipe loads the next four items*/
        loadMore.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (stockOnly.isChecked()) {
                    if (tagSearch.getQuery().toString().isEmpty()) {
                        loader.loadHomeItems(allItemsGrid.getCount(), 1, false);
                    } else {
                        loader.loadItemsSearch(allItemsGrid.getCount(), 1, tagSearch.getQuery().toString(), false);
                    }

                } else {

                    if (tagSearch.getQuery().toString().isEmpty()) {
                        loader.loadHomeItems(allItemsGrid.getCount(), 0, false);
                    } else {
                        loader.loadItemsSearch(allItemsGrid.getCount(), 0, tagSearch.getQuery().toString(), false);
                    }
                }
                if (loadMore.isRefreshing()) {
                    loadMore.setRefreshing(false);
                }

            }

        });
        /*loading more items on scroll up each scroll loads the next four items*/
        allItemsGrid.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (!view.canScrollVertically(1) && scrollState == SCROLL_STATE_IDLE) {

                    if (stockOnly.isChecked()) {
                        if (tagSearch.getQuery().toString().isEmpty()) {
                            loader.loadHomeItems(allItemsGrid.getCount(), 1, false);
                        } else {
                            loader.loadItemsSearch(allItemsGrid.getCount(), 1, tagSearch.getQuery().toString(), false);
                        }

                    } else {

                        if (tagSearch.getQuery().toString().isEmpty()) {
                            loader.loadHomeItems(allItemsGrid.getCount(), 0, false);
                        } else {
                            loader.loadItemsSearch(allItemsGrid.getCount(), 0, tagSearch.getQuery().toString(), false);
                        }
                    }

                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        /*loading the items currently available in the stock*/
        stockOnly.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                allItemsGrid.setAdapter(null);
                if (isChecked) {
                    if (tagSearch.getQuery().toString().isEmpty()) {
                        loader.loadHomeItems(0, 1, true);
                    } else {
                        loader.loadItemsSearch(0, 1, tagSearch.getQuery().toString(), true);
                    }

                } else {
                    if (tagSearch.getQuery().toString().isEmpty()) {
                        loader.loadHomeItems(0, 0, true);
                    } else {
                        loader.loadItemsSearch(0, 0, tagSearch.getQuery().toString(), true);
                    }
                }
            }
        });
        /*loading items the user search for by tags*/
        tagSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                allItemsGrid.setAdapter(null);
                if (newText != null) {
                    if (stockOnly.isChecked()) {
                        loader.loadItemsSearch(0, 1, newText, true);
                    } else {
                        loader.loadItemsSearch(0, 0, newText, true);
                    }
                } else {
                    if (stockOnly.isChecked()) {
                        loader.loadHomeItems(0, 1, true);
                    } else {
                        loader.loadHomeItems(0, 0, true);
                    }
                }
                return true;
            }
        });

    }

}