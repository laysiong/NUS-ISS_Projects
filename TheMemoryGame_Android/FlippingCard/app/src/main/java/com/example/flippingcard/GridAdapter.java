package com.example.flippingcard;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class GridAdapter extends ArrayAdapter<Object> {

    private final Context context;
    private String[] images;
    private boolean[] flippedState; // To track if each item is flipped or not
    private Map<String, Bitmap> downloadedImages; // To store downloaded bitmaps
    private int itemHeight; // Variable to store item height
    MediaPlayer mysound;

    public GridAdapter(Context context, String[] images, Map<String, Bitmap> downloadedImages,int itemHeight) {
        super(context, R.layout.grid_item);
        this.context = context;
        this.images = images;
        this.flippedState = new boolean[images.length]; // Initialize flipped state array
        this.downloadedImages = downloadedImages != null ? downloadedImages : new HashMap<>(); // Initialize the map for downloaded images
        this.itemHeight = dpToPx(context, itemHeight); // Set item height
        Arrays.fill(flippedState, false); // Initially, all items are not flipped

        addAll(new Object[images.length]);
    }

    @NonNull
    @Override
    public View getView(int pos, View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                    Activity.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.grid_item, parent, false);

        }

        ImageView imageView = convertView.findViewById(R.id.grid_image);

        // Set the height of the item
        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        layoutParams.height = itemHeight;
        layoutParams.width = itemHeight; // Ensure it remains square
        imageView.setLayoutParams(layoutParams);

        // The Problem is here
        if (downloadedImages.containsKey(images[pos])) {
            // If the image is downloaded, use the downloaded image
            if (flippedState[pos]) {
                imageView.setImageResource(R.drawable.cardback); // Show card back if flipped
            } else {
                imageView.setImageBitmap(downloadedImages.get(images[pos]));
            }
        } else {
            // Use the default image if not downloaded
            imageView.setImageResource(R.drawable.empty_slot);
        }

        return convertView;
    }

    public void updateImage(String imageName, Bitmap bitmap) {

        downloadedImages.put(imageName, bitmap);
        //Log.d("if statement before setting image", downloadedImages.get(imageName).toString());
        notifyDataSetChanged();
    }
    public Map<String, Bitmap> getDownloadedImages() {
        return downloadedImages;
    }


    public static int dpToPx(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    //function flipall (input boolean)
    public void flipAll(boolean flipped) {
        Arrays.fill(flippedState, flipped);
        notifyDataSetChanged(); // Notify adapter to update view
    }

    // Method to perform flip animation on the ImageView at position `pos`
    public void flipAnimation(ImageView imageView, int pos) {
        Animator setOut = AnimatorInflater.loadAnimator(context, R.animator.card_flip_out);
        Animator setIn = AnimatorInflater.loadAnimator(context, R.animator.card_flip_in);

        if (imageView != null) {

            setIn.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    // Update the flipped state and notify data set changed after the first animation
                    flippedState[pos] = !flippedState[pos];
                    notifyDataSetChanged();

                    // Start the second animation after updating the data
                    setOut.setTarget(imageView);
                    setOut.start();
                }
            });
            setIn.start();
        }
    }

    public void correctImage(View view) {
        Animator correctMatchAnimator = AnimatorInflater.loadAnimator(context, R.animator.correct_matches);

        if (view != null) {
            setSound(R.raw.correct_sound_effects);
            correctMatchAnimator.setTarget(view);
            correctMatchAnimator.start();
        }
    }

    public void wrongChoice(View view) {
        Animator wrongAnim = AnimatorInflater.loadAnimator(context, R.animator.wrong_matches);

        if (view != null) {
            wrongAnim.setTarget(view);
            setSound(R.raw.wrong_sound_effect);
            wrongAnim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    view.setTranslationX(0); // Reset position
                }
            });

            wrongAnim.start();
        }

    }

    public <soundeffect> void setSound(int soundeffect){
        mysound = MediaPlayer.create(context, soundeffect);

        mysound.start();
        mysound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
    }









}
