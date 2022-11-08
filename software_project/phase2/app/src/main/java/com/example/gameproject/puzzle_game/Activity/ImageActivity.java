package com.example.gameproject.puzzle_game.Activity;

import java.util.ArrayList;
import java.util.Objects;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import com.example.gameproject.R;
import com.example.gameproject.User;
import com.example.gameproject.puzzle_game.GameController.CustomImageManager;

import android.Manifest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;

import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ImageActivity extends AppCompatActivity {

    Uri selectedImage;
    private final int RESULT_LOAD_IMAGE = 1;
    private ArrayList<Bitmap> oldCustomImages = new ArrayList<>();
    private ArrayList<Bitmap> selectedImages = new ArrayList<>();
    private static final int STORAGE_PERMISSION_CODE = 1;
    private AlertDialog ad;

    private User currentUser;


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_puzzle_game_select_image);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);

        currentUser = (User) getIntent().getSerializableExtra("user");
        assert currentUser != null;
        String customImagesCode = currentUser.get("puzzle_game_custom_images");

        showOldCustomizedImages(customImagesCode);

        if(ContextCompat.checkSelfPermission(ImageActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {

            String[] permissionArray = {Manifest.permission.READ_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions(ImageActivity.this,
                    permissionArray,
                    STORAGE_PERMISSION_CODE);
        }
        else {
            alertDialogForCameraImage();
        }

        Button addImageButton, saveSelectionButton, cancelButton;
        addImageButton = findViewById(R.id.add_image_button);
        saveSelectionButton = findViewById(R.id.save_image_selection_button);
        cancelButton = findViewById(R.id.cancel_button);

        addImageButton.setOnClickListener(view1 -> {
            ad.dismiss();
            alertDialogForCameraImage();
        });

        saveSelectionButton.setOnClickListener(view12 -> {
            Bitmap[] imageList = new Bitmap[selectedImages.size()];
            imageList = selectedImages.toArray(imageList);
            String value = CustomImageManager.saveImageList(imageList, customImagesCode,
                    getApplicationContext());
            currentUser.set("puzzle_game_custom_images", value);
            currentUser.write();
            finish();
        });

        cancelButton.setOnClickListener(view1 -> {
            finish();
        });
    }

    private void showOldCustomizedImages(String customImagesCode) {
        Bitmap[] savedImages = CustomImageManager.getImageList(customImagesCode, getApplicationContext());
        for (Bitmap image : savedImages) {
            if (image != null) {
                oldCustomImages.add(image);
            }
        }
        for (int i = 0; i < oldCustomImages.size(); i++) {
            showImageInLayout(oldCustomImages.get(i));
        }
    }

    void pickImageFromGallery() {

        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == RESULT_LOAD_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {

                selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();
                Bitmap imageBitmap = BitmapFactory.decodeFile(picturePath);
                showImageInLayout(imageBitmap);
                selectedImages.add(imageBitmap);
            }
        }
    }

    private void showImageInLayout(Bitmap image) {
        ImageView imageView = new ImageView(ImageActivity.this);
        imageView.setImageBitmap(image);
        int imageId = View.generateViewId();
        imageView.setId(imageId);

        Button deleteButton = new Button(ImageActivity.this);
        deleteButton.setBackgroundResource(R.drawable.close_button);

        LinearLayout linearImages = findViewById(R.id.linear_images);
        RelativeLayout relativeLayout = new RelativeLayout(getApplicationContext());

        int dp100 =  (int) (100 * imageView.getResources().getDisplayMetrics().density);
        int dp10 = (int) (10 * imageView.getResources().getDisplayMetrics().density);

        RelativeLayout.LayoutParams imageLayoutParams =
                new RelativeLayout.LayoutParams(dp100, dp100);
        imageLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        imageView.setLayoutParams(imageLayoutParams);
        relativeLayout.addView(imageView, imageLayoutParams);

        RelativeLayout.LayoutParams deleteButtonLayoutParams =
                new RelativeLayout.LayoutParams(dp10, dp10);
        deleteButtonLayoutParams.addRule(RelativeLayout.ALIGN_RIGHT, imageId);
        deleteButtonLayoutParams.addRule(RelativeLayout.ALIGN_TOP, imageId);
        deleteButton.setLayoutParams(deleteButtonLayoutParams);
        relativeLayout.addView(deleteButton);

        LinearLayout.LayoutParams linearLayoutParams =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayoutParams.topMargin = dp10;
        relativeLayout.setLayoutParams(linearLayoutParams);

        linearImages.addView(relativeLayout);

        deleteButton.setOnClickListener(view -> {
            linearImages.removeView(relativeLayout);
            selectedImages.remove(image);
        });
    }

    public void alertDialogForCameraImage() {
        AlertDialog.Builder adb = new AlertDialog.Builder(ImageActivity.this);
        adb.setTitle("Pick Image From Gallery: ");
        adb.setNegativeButton("Gallery", (dialog, which) -> pickImageFromGallery());
        ad = adb.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                            @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);

            if (requestCode == STORAGE_PERMISSION_CODE) {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(ImageActivity.this,
                            "Gallery Access Permission Granted",
                            Toast.LENGTH_SHORT)
                            .show();
                    alertDialogForCameraImage();
                }
                else {
                    Toast.makeText(ImageActivity.this,
                            "Gallery Access Permission Denied",
                            Toast.LENGTH_SHORT)
                            .show();
                    Intent backIntent = new Intent(this, PuzzleGameIntroActivity.class);
                    backIntent.putExtra("user", currentUser);
                    startActivity(backIntent);
                }
            }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ad.dismiss();
        Intent intent = new Intent(this, PuzzleGameIntroActivity.class);
        intent.putExtra("user", currentUser);
        intent.putExtra("continue_customization", true);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
