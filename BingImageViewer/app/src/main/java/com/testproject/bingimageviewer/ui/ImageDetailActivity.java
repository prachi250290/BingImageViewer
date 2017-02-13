package com.testproject.bingimageviewer.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.testproject.bingimageviewer.Common;
import com.testproject.bingimageviewer.Constants;
import com.testproject.bingimageviewer.R;
import com.testproject.bingimageviewer.model.ImageInfo;
import com.testproject.bingimageviewer.repository.ImageInfoRepository;

import java.util.Calendar;

public class ImageDetailActivity extends Activity implements View.OnClickListener{

    private ImageView imageView;
    private EditText brandEditText, priceEditText;
    private TextView categoryTextView, dateTextView;
    private Button submitButton;

    private String imageId;

    private boolean isCategorySelected, isDateSelected, isBrandEntered, isPriceEntered;

    private ImageInfo imageInfo;

    private String[] categories = new String[] { "Art", "Automobiles", "Books", "Films", "Food" , "Photography", "Places", "Shopping", "Travel"};

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

        //Add text watchers
        brandEditText.addTextChangedListener(new TextWatcherForDetails(brandEditText));
        priceEditText.addTextChangedListener(new TextWatcherForDetails(priceEditText));

    }

    private void populateData() {
        imageId = getIntent().getExtras().getString(Constants.INTENT_KEY_IMAGE_ID);

        String imageUrl = getIntent().getExtras().getString(Constants.INTENT_KEY_IMAGE_URL);
        Picasso.with(this).load(imageUrl).into(imageView);

        imageInfo = new ImageInfo();
        imageInfo.setUrl(imageUrl);
        imageInfo.setImageId(imageId);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_detail_category_textView:
                showCategoryPicker();
                break;
            case R.id.image_detail_date_textView:
                showDatePicker();
                break;
            case R.id.image_detail_button_submit:
                saveImageInfo();
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
                isCategorySelected = true;
                int pos = picker.getValue();
                String selectedCategory = categories[pos];
                categoryTextView.setText(selectedCategory);
                imageInfo.setCategory(selectedCategory);
                validateFields();
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


    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(ImageDetailActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                dateTextView.setText(String.format("%02d", monthOfYear+1) + "/" + String.format("%02d", dayOfMonth) + "/" + year);
                long date = getDate(year, monthOfYear, dayOfMonth);
                imageInfo.setDate(date);
                isDateSelected = true;
                validateFields();
            }
        }, getCalender().get(Calendar.YEAR), getCalender().get(Calendar.MONTH), getCalender().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private Calendar getCalender() {
        return Calendar.getInstance();
    }

    private long getDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return calendar.getTimeInMillis();
    }

    private void  saveImageInfo() {

        imageInfo.setBrand(brandEditText.getText().toString());
        imageInfo.setPrice(Float.parseFloat(priceEditText.getText().toString()));

        ImageInfoRepository imageInfoRepository = new ImageInfoRepository(this);
        long insertId = imageInfoRepository.saveImageInfo(imageInfo);

        //If the insert operation succeds
        if(insertId != -1) {
            Common.showAlertWithMessage(this, "", getString(R.string.record_saved_msg));
        }
    }


    private void validateFields() {
        if(isFormValid()) {
            submitButton.setEnabled(true);
        }
        else {
            submitButton.setEnabled(false);
        }
    }

    private boolean isFormValid() {
        if(isDateSelected && isCategorySelected && isBrandEntered && isPriceEntered) {
            return true;
        }
        return false;
    }


    private class TextWatcherForDetails implements TextWatcher {

        private EditText editTextAssignedTo;

        public TextWatcherForDetails(EditText editText) {
            this.editTextAssignedTo = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            switch (this.editTextAssignedTo.getId()) {
                case R.id.image_detail_editText_brand:
                    isBrandEntered = s.toString().trim().equals("") ? false :true;
                    break;
                case R.id.image_detail_editText_price:
                    isPriceEntered = s.toString().trim().equals("") ? false :true;
                    break;
            }
            validateFields();
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }


}
