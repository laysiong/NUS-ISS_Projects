package com.example.flippingcard;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    //check if there image in the folder direction -

    Thread bkgdThread;
    private EditText user_input;
    private ProgressBar progressBar;

    private GridView gridView;
    private Set<String> selectedIndices = new HashSet<>();
    private GridAdapter gridAdapter;

    private Button searchbtn;
    private Button start;
    private TextView downloadstate;
    private boolean multiplayer_mode;

    String[] flowerImages = new String[20];
    private int imagesCount = 0;
    private File dir;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user_input = findViewById(R.id.user_url);
        searchbtn = findViewById(R.id.url_get);
        downloadstate = findViewById(R.id.progressState);
        start = findViewById(R.id.startButton);
        dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        downloadstate.setText(getString(R.string.instruction));
        progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(100);

        gridView = findViewById(R.id.gridView);
        if (gridView != null) {
            gridAdapter = new GridAdapter(MainActivity.this, flowerImages, null,80);
            gridView.setAdapter(gridAdapter);
            gridView.setOnItemClickListener(this);
        }

        searchbtn.setOnClickListener(this::onClickSearch);
        start.setOnClickListener(this); // Set onClickListener for the startButton

    }



    /* ---------------------  UI updates --------------------- */

    public Boolean startable(){
        return imagesCount == flowerImages.length;
    }

    public void startbtnUI(){
        if (selectedIndices.size() == 6) {
            activeStartBtn();
        } else {
            defaultStartBtn();
        }
    }

    private void defaultStartBtn(){
        start.setBackgroundColor(ContextCompat.getColor(this,R.color.btninactive));
        start.setTextColor(ContextCompat.getColor(this,R.color.black));
    }

    private void activeStartBtn(){
        start.setBackgroundColor(ContextCompat.getColor(this,R.color.btnactive));
        start.setTextColor(ContextCompat.getColor(this,R.color.white));
    }

    public void showSearchUI()
    {
        searchbtn.setText(getString(R.string.SearchBtn));
        downloadstate.setText(getString(R.string.instruction));
        //downloadstate.setText(getString(R.string.downloading,imagesCount));
    }

    public void showCancelUI() {
        searchbtn.setText(getString(R.string.Cancel));
        //downloadstate.setText(getString(R.string.instruction));
        downloadstate.setText(getString(R.string.downloading,imagesCount));
    }

    public void selectImageUI()
    {
        searchbtn.setText(getString(R.string.SearchBtn));
        downloadstate.setText(getString(R.string.selectimg));
    }


    public void resetSelectImage () {
        if (selectedIndices.size() == 0) {
            return;
        }

        //Log.d("gridView Child", String.valueOf(gridView.getChildCount()));
        for (int i = 0; i < gridView.getChildCount(); i++) {
            View gridItem = gridView.getChildAt(i);

            ImageView imageView = gridItem.findViewById(R.id.grid_image);
            imageView.clearColorFilter();
        }
        selectedIndices.clear();
        gridAdapter.notifyDataSetChanged();
    }


    public void resetImageDownloaderUI (){
        Arrays.fill(flowerImages, "");
        imagesCount = 0;
        gridAdapter.notifyDataSetChanged();
        showSearchUI();
        //downloadstate.setText(getString(R.string.downloading,imagesCount));
        progressBar.setVisibility(View.GONE); // Hide progress bar on interruption
    }



    /* ---------------------  Event --------------------- */

    //To select the item
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        if (!startable()) {
            Toast.makeText(MainActivity.this, "Please type url where you wish grab 20 images from.", Toast.LENGTH_SHORT).show();
            return;
        }
        // Print the selected image name
        String selectedImageName = flowerImages[i]; // Assuming flowerImages is your array of image names
        Log.d("Selected Image", selectedImageName);

        int color = getColor(R.color.activeRow);
        ImageView imageView = view.findViewById(R.id.grid_image);

        // Toggle selection state of the clicked item
        if (selectedIndices.contains(selectedImageName)) {
            selectedIndices.remove(selectedImageName);
            imageView.clearColorFilter();
        } else {
            // Limit to 6 selections
            if (selectedIndices.size() < 6) {
                selectedIndices.add(selectedImageName);
                imageView.setColorFilter(color);
            } else {

                Toast.makeText(MainActivity.this, "You can select up to 6 images", Toast.LENGTH_SHORT).show();
            }
        }
        startbtnUI();
    }



    //Start the Game
    @Override
    public void onClick(View view) {

        if (!startable()) {
            Toast.makeText(MainActivity.this, "There is no enough images to start the game", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedIndices.size() < 6 ) {
            Toast.makeText(MainActivity.this, "You must select up to 6 images", Toast.LENGTH_SHORT).show();
            return;
        }

        selectGameMode(view);
    }

    private String saveImageToInternalStorage(String imageName, Bitmap bitmap) {
        File directory = getFilesDir();
        File file = new File(directory, imageName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void selectGameMode(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Game Mode");
        builder.setMessage("Which game mode will you prefer?");

        builder.setPositiveButton("Single Player", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle the user confirming the forfeit
                multiplayer_mode = false;
                nextActvity();
            }
        });

        builder.setNegativeButton("Two Player", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Dismiss the dialog
                multiplayer_mode = true;
                nextActvity();
            }
        });

        //builder.setCancelable(false);
        builder.show();
    }


    private void nextActvity(){
        // Create an Intent to start the FlipCard activity
        Intent intent = new Intent(MainActivity.this, FlipCardActivity.class);

        // Convert selectedIndices set to ArrayList for passing in Intent
        ArrayList<String> selectedIndicesList = new ArrayList<>(selectedIndices);
        intent.putStringArrayListExtra("selectedIndices", selectedIndicesList);

        // Save images to internal storage and pass file paths
        ArrayList<String> filePaths = new ArrayList<>();
        for (String selectedImage : selectedIndicesList) {
            Bitmap bitmap = gridAdapter.getDownloadedImages().get(selectedImage);
            if (bitmap != null) {
                String filePath = saveImageToInternalStorage(selectedImage, bitmap);
                filePaths.add(filePath);
            }
        }
        intent.putStringArrayListExtra("imageFilePaths", filePaths);
        intent.putExtra("game_mode",multiplayer_mode);

        // Start the FlipCard activity
        startActivity(intent);
        finish();
    }



    //Search Game
    private void onClickSearch(View view){
        user_input =findViewById(R.id.user_url);
        String imgURL = user_input.getText().toString().trim();
        closeKeyboard();

        Log.d("Search",String.valueOf(URLUtil.isValidUrl(imgURL)));

        if (imgURL.isEmpty() || !URLUtil.isValidUrl(imgURL)) {
            Toast.makeText(MainActivity.this, "Please enter a valid URL", Toast.LENGTH_SHORT).show();
            return;
        }

        File[] files = dir.listFiles();
        if(files != null){
            for (File file : files) {
                file.delete();
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    resetSelectImage();
                    resetImageDownloaderUI();
                    startbtnUI();
                }
            });
        }


        if (bkgdThread == null) {
            startDownloadImage(imgURL);
            showCancelUI();
        }
        else {
            bkgdThread.interrupt();
            showSearchUI();
        }
    }
    // Method to close the keyboard
    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    /* ---------------------  Function (Downloading) --------------------- */
    protected void startDownloadImage(String imgURL) {

        // creating a background thread
        bkgdThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    //String imgURL = "https://stocksnap.io";

                    // Step 1: Fetch HTML Content
                    GetURLHtml getHtml = new GetURLHtml();
                    String htmlContent = getHtml.fetchHtmlContent(imgURL);

                    if(htmlContent == "Error"){
                        Log.d("test","unable to grab html content");

                        bkgdThread = null;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "Fail to Fetch images - Try using other valid URL", Toast.LENGTH_SHORT).show();
                                resetImageDownloaderUI();
                            }
                        });
                        return;
                    }


                    // Step 2: Extract Image URLs
                    List<String> imageUrls = getHtml.extractImageUrls(htmlContent, imgURL);
                    int maxImages = Math.min(imageUrls.size(), 20);

                    for (int i = 0; i < maxImages; i++) {
                        if (Thread.currentThread().isInterrupted()) {
                            Log.d("Downloaded interrupted", "stop");
                            bkgdThread = null;

                            // Update flowerImages and notify adapter on the UI thread
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    resetImageDownloaderUI();
                                }
                            });
                            return;
                        }

                        String imageUrl = imageUrls.get(i);
                        System.out.println("Image URL: " + imageUrl);
                        String destFilename = UUID.randomUUID().toString() +
                                imageUrl.substring(imageUrl.lastIndexOf("."));
                        //System.out.println("Image URL: " + destFilename);
                        File destFile = new File(dir, destFilename);
                        //System.out.println("File dir: " + dir);


                        // Running downloader in background thread
                        ImageDownloader imgDL = new ImageDownloader(dir);

                        //running downloader in background thread
                        if (imgDL.downloadImage(imageUrl, destFile)) {
                            Bitmap bitmap = BitmapFactory.decodeFile(destFile.getAbsolutePath());

                            int position = i; // Match position in flowerImages array

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    imagesCount = position + 1;
                                    downloadstate.setText(getString(R.string.downloading, imagesCount));
                                    updateImageView(position, destFilename, bitmap);

                                    // Update overall progress
                                    int overallProgress = (int) ((imagesCount / (float) maxImages) * 100);
                                    progressBar.setVisibility(View.VISIBLE);
                                    progressBar.setProgress(overallProgress);
                                }
                            });
                        }

                    }

                } catch (Exception e) {
                    Log.e("Download error", "Error downloading images", e);
                    imagesCount = 0;
                    bkgdThread = null;

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            resetImageDownloaderUI();
                            showSearchUI();
                            Toast.makeText(MainActivity.this, "Fail to download images - Try using other valid URL", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                //End of Download (Reset Default)
                bkgdThread = null;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        selectImageUI();
                        progressBar.setVisibility(View.GONE); // Hide progress bar on interruption
                    }
                });
            }
        });

        bkgdThread.start();


        Log.d( "Download End", "startDownloadImage: Ended");

    }


    protected void updateImageView(int position, String imageName, Bitmap bitmap) {
        if (gridAdapter != null) {
            flowerImages[position] = imageName; // Update the flowerImages array
            gridAdapter.updateImage(imageName, bitmap); // Update the adapter
            gridAdapter.notifyDataSetChanged(); // Notify adapter to refresh the views
        }
    }



}