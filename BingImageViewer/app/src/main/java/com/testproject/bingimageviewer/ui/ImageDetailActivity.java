package com.testproject.bingimageviewer.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.testproject.bingimageviewer.Constants;
import com.testproject.bingimageviewer.R;

import org.w3c.dom.Text;

public class ImageDetailActivity extends Activity implements View.OnClickListener{

    private ImageView imageView;
    private EditText brandEditText, priceEditText;
    private TextView categoryTextView, dateTextView;
    private Button submitButton;

    private String imageId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);

        setupViews();

        populateData();
    }

    private void setupViews() {
        imageView = (ImageView) findViewById(R.id.image_detail_imageView);
        brandEditText = (EditText) findViewById(R.id.image_detail_editText_brand);
        priceEditText = (EditText) findViewById(R.id.image_detail_editText_price);
        categoryTextView = (TextView) findViewById(R.id.image_detail_category_textView);
        dateTextView = (TextView) findViewById(R.id.image_detail_date_textView);
        submitButton = (Button) findViewById(R.id.image_detail_button_submit);
    }

    private void populateData() {
        imageId = getIntent().getExtras().getString(Constants.INTENT_KEY_IMAGE_ID);

        String imageUrl = getIntent().getExtras().getString(Constants.INTENT_KEY_IMAGE_URL);
        Picasso.with(this).load(imageUrl).into(imageView);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_detail_category_textView:
                break;
            case R.id.image_detail_date_textView:
                break;
            case R.id.image_detail_button_submit:
                break;
        }
    }
}
