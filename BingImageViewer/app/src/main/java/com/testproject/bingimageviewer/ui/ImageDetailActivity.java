package com.testproject.bingimageviewer.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

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

    private boolean isCategorySelected, isDateSelected;

    private String[] categories = new String[] { "Art", "Automobiles", "Books", "Films", "Food" , "Photography", "Places", "Travel"};

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

        categoryTextView.setOnClickListener(this);
        dateTextView.setOnClickListener(this);
        submitButton.setOnClickListener(this);
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
                showCategoryPicker();
                break;
            case R.id.image_detail_date_textView:
                break;
            case R.id.image_detail_button_submit:
                break;
        }
    }

    private void showCategoryPicker() {

        final AlertDialog.Builder dialog = new AlertDialog.Builder(ImageDetailActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.number_picker_dialog, null);
        dialog.setTitle(getString(R.string.image_detail_select_category));
        dialog.setView(dialogView);
        final NumberPicker picker = (NumberPicker) dialogView.findViewById(R.id.dialog_number_picker);
        picker.setMinValue(0);
        picker.setMaxValue(categories.length - 1);
        picker.setDisplayedValues(categories);
        dialog.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int pos = picker.getValue();
                String selectedCategory = categories[pos];
                categoryTextView.setText(selectedCategory);
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();

    }
}
