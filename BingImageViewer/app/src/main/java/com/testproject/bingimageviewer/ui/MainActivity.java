package com.testproject.bingimageviewer.ui;

import android.app.Activity;
import android.content.Context;
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

import com.testproject.bingimageviewer.Common;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupViews();
    }

    private void setupViews() {

        searchView =(SearchView) findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.imageGridView);

        imageGridAdapter = new ImageGridAdapter(imageDetailList, this);
        mLayoutManager = new GridLayoutManager(this, Constants.GRID_NUMBER_OF_COLUMNS);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(imageGridAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
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
        if(validateSearchField()) {
            getImagesFromServer();
         }
         else {
           showValidationAlert();
        }
    }

    private boolean validateSearchField() {
        if(searchView.getQuery() != null || searchView.getQuery().equals(""))
            return true;
        return false;
    }


    private void getImagesFromServer() {

        HashMap<String, String> headers = WebServiceManager.getSharedInstance().getHeaders();

        ApiInterface apiService =
                ApiClient.getClient(headers).create(ApiInterface.class);


        Map<String, String> queryMap = new HashMap<String, String>();
        queryMap.put(Constants.QUERY_PARAM_SEARCH_QUERY, String.valueOf(searchView.getQuery()));
        queryMap.put(Constants.QUERY_PARAM_ASPECT, Constants.QUERY_PARAM_SQUARE);


        Call<SearchResult> call = apiService.searchImages(queryMap);
        call.enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                //Utility.hideProgressDialog();
                if (response != null) {
                    if (response.isSuccessful()) {

                        SearchResult searchResult = response.body();
                        imageDetailList.addAll(searchResult.getImageList());
                        imageGridAdapter.setPropertyList(imageDetailList);
                        imageGridAdapter.notifyDataSetChanged();


                    } else {
                        //Handle failure
                        Toast.makeText(MainActivity.this, "No Results found", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                //Utility.hideProgressDialog();
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });
    }

    private void showValidationAlert() {
        Common.showAlertWithMessage(this, "", getString(R.string.search_empty_message));
    }



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
