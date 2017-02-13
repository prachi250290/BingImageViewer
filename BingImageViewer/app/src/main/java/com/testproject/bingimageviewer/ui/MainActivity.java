package com.testproject.bingimageviewer.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.testproject.bingimageviewer.Constants;
import com.testproject.bingimageviewer.R;
import com.testproject.bingimageviewer.manager.WebServiceManager;
import com.testproject.bingimageviewer.model.ImageDetail;
import com.testproject.bingimageviewer.model.SearchResult;
import com.testproject.bingimageviewer.webservice.ApiClient;
import com.testproject.bingimageviewer.webservice.ApiInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends Activity implements SearchView.OnQueryTextListener{

    private SearchView searchView;

    private RecyclerView recyclerView;
    private ImageGridAdapter imageGridAdapter;
    private List<ImageDetail> imageDetailList = new ArrayList<>();

    LinearLayoutManager mLayoutManager;

    private static final String TAG = MainActivity.class.getSimpleName();

    private int offset = Constants.QUERY_PARAM_DEFAULT_OFFSET;
    private int nextOffsetAddCount = Constants.QUERY_PARAM_NEXT_OFFSET_ADD_COUNT_DEFAULT;

    private boolean isLastPage = false;
    private boolean isLoading = false;

    private static int IMAGE_PAGE_SIZE = Constants.QUERY_PARAM_COUNT_VALUE;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupViews();
    }

    private void setupViews() {

        searchView =(SearchView) findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(this);

        //Set layout snd listeners to recycler view
        recyclerView = (RecyclerView) findViewById(R.id.imageGridView);
        imageGridAdapter = new ImageGridAdapter(imageDetailList, this);
        mLayoutManager = new GridLayoutManager(this, Constants.GRID_NUMBER_OF_COLUMNS);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(imageGridAdapter);
        recyclerView.addOnScrollListener(recyclerViewOnScrollListener);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                openImageDetailScreen(imageDetailList.get(position));
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void openImageDetailScreen(ImageDetail imageDetail) {
        Intent imageDetailIntent = new Intent(MainActivity.this, ImageDetailActivity.class);
        imageDetailIntent.putExtra(Constants.INTENT_KEY_IMAGE_ID, imageDetail.getImageId());
        imageDetailIntent.putExtra(Constants.INTENT_KEY_IMAGE_URL, imageDetail.getContentUrl());
        startActivity(imageDetailIntent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchImages();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    private void searchImages() {
          imageDetailList.clear();
          getImagesFromServer();

    }


    private void getImagesFromServer() {

        HashMap<String, String> headers = WebServiceManager.getSharedInstance().getHeaders();

        ApiInterface apiService =
                ApiClient.getClient(headers).create(ApiInterface.class);

        isLoading = true;

        int offSetWithNextOffsetAddCount = offset + nextOffsetAddCount;


        //Define query params hashmap
        Map<String, String> queryMap = new HashMap<String, String>();
        queryMap.put(Constants.QUERY_PARAM_SEARCH_QUERY, String.valueOf(searchView.getQuery()));
        queryMap.put(Constants.QUERY_PARAM_ASPECT, Constants.QUERY_PARAM_SQUARE);
        queryMap.put(Constants.QUERY_PARAM_COUNT, String.valueOf(Constants.QUERY_PARAM_COUNT_VALUE));
        queryMap.put(Constants.QUERY_PARAM_OFFSET, String.valueOf(offSetWithNextOffsetAddCount));



        Call<SearchResult> call = apiService.searchImages(queryMap);
        call.enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {

                isLoading = false;

                if (response != null) {
                    if (response.isSuccessful()) {

                        SearchResult searchResult = response.body();
                        List<ImageDetail> imageDetails = searchResult.getImageList();
                        imageDetailList.addAll(imageDetails);

                        //Set images to adapter
                        imageGridAdapter.setImageList(imageDetailList);
                        imageGridAdapter.notifyDataSetChanged();

                        offset = offset + IMAGE_PAGE_SIZE;
                        nextOffsetAddCount = searchResult.getNextOffsetAddCount();


                        //If the number of records fetched are less than the max limit
                        if (imageDetails.size() < IMAGE_PAGE_SIZE) {
                            isLastPage = true;
                        }


                    } else {
                        //Handle failure
                        Toast.makeText(MainActivity.this, "No Results found", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {

                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });
    }
    

    private RecyclerView.OnScrollListener
            recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView,
                                         int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int visibleItemCount = mLayoutManager.getChildCount();
            int totalItemCount = mLayoutManager.getItemCount();
            int firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition();

            if (!isLoading && !isLastPage) {
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0
                        && totalItemCount >= IMAGE_PAGE_SIZE) {
                    getImagesFromServer();
                }
            }
        }
    };



    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private MainActivity.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final MainActivity.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }
}
