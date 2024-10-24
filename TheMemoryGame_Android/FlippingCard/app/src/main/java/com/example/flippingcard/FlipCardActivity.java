package com.example.flippingcard;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.DecimalFormat;
import android.icu.text.NumberFormat;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FlipCardActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    // prevent double click on same images -
        // after matched, ignore it. -
    // shuffle array list -
    // card flip -
        // and rotate -
    // If Counter is 6, stop timer, win -
        // end game -
    // If timer end, lose, go back main screen? or ask to try again? pop out??? -
    // Forfeit or Pause?, end the game, go back main screen. -
    // save instance so rotate will work
    // win go back to first activity. -
    // sizing of the cards -

    GridView gridView;
    private GridAdapter gridAdapter;
    private Button Forfeit;

    String firstClickedItem;
    boolean clickEnabled;
    boolean ForfeitEnabled = true;
    String[] selectedImageNames;
    private int firstClickedPosition = -1;
    private Set<Integer> matchedPositions; // To track matched item positions
    private ImageView previousClickedImageView; // Store the ImageView of the previous clicked item
    private boolean tryagain;


    //game-mode (two-player or single-player)
    private boolean game_mode;
    private boolean player_turn;
    private Map<String, Integer> scoreboard;

    //Timer and Number of Images Matches
    TextView numMatchTextView;
    TextView Timer;
    int matchCounter = 0;

    private CountDownTimer countDownTimer;
    private long timeLeftInMillis = 100000; // using 100000

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flipcard);

        // Retrieve selected indices from the intent extras
        Intent intent = getIntent();
        tryagain = intent.getBooleanExtra("tryagain", false);

        ArrayList<String> selectedIndices = intent.getStringArrayListExtra("selectedIndices");
        ArrayList<String> imageFilePaths = intent.getStringArrayListExtra("imageFilePaths");
        Forfeit = findViewById(R.id.Forfeit);


        game_mode = intent.getBooleanExtra("game_mode", false); // 'false' is the default value
        Log.d("game_mode",game_mode == true ? "Two Player":"Single Player");


        twoplayer_turn(game_mode);

        if (selectedIndices != null && imageFilePaths != null) {
            // Debug: Log the selected indices
            Log.d("SelectedIndices", selectedIndices.toString());

            numMatchTextView = findViewById(R.id.numMatchTextView);
            selectedImageNames = duplicateStringArray(selectedIndices);
            Timer = findViewById(R.id.timer);
            matchedPositions = new HashSet<>();
            clickEnabled = true;

            // Set initial text with match counter
            updateMatchCounter();

            gridView = findViewById(R.id.gridView);
            if (gridView != null) {
                // Load bitmaps from file paths
                Map<String, Bitmap> downloadedImages = new HashMap<>();
                for (String filePath : imageFilePaths) {
                    String imageName = new File(filePath).getName();
                    Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                    downloadedImages.put(imageName, bitmap);
                    //Log.d("SelectedIndices", imageFilePaths.toString());
                    Log.d("Map BitMap", filePath.toString());
                }

                // Populate your GridAdapter with the selected image names
                gridAdapter = new GridAdapter(FlipCardActivity.this, selectedImageNames, downloadedImages,102);
                gridView.setAdapter(gridAdapter);
                gridAdapter.flipAll(true);
                gridView.setOnItemClickListener(this);
            }

            startCountdownTimer();
            Forfeit.setOnClickListener(this::onForfeit);
        } else {
            Log.e("FlipCard", "SelectedIndices is null");
        }

    }

    private void twoplayer_turn(boolean game_mode){
        // Initialize scoreboard for two-player mode
        if (game_mode) {
            scoreboard = new HashMap<>();
            scoreboard.put("Player 1", 0);
            scoreboard.put("Player 2", 0);
            player_turn = true; // Player 1 starts
            player_scoreboardUI();
            player_turnUI();
        }else{
            TextView PlayerTurnBoard = findViewById(R.id.PlayerTurn);
            PlayerTurnBoard.setText(getString(R.string.singleplayer));
        }

    }


    private void player_scoreboardUI(){
        TextView Player1Score = findViewById(R.id.Player1Score);
        TextView Player2Score = findViewById(R.id.Player2Score);
        Player1Score.setVisibility(View.VISIBLE);
        Player2Score.setVisibility(View.VISIBLE);

        Integer player1Score = scoreboard.get("Player 1");
        Integer player2Score = scoreboard.get("Player 2");

        Player1Score.setText("P1 : " + String.valueOf(player1Score));
        Player2Score.setText("P2 : "+  String.valueOf(player2Score));
    }

    private void player_turnUI(){
        TextView PlayerTurnBoard = findViewById(R.id.PlayerTurn);
        PlayerTurnBoard.setText(player_turn ? "Player 1's Turn" : "Player 2's Turn");
    }

    private void add_playerscore (){
        String currentPlayer = player_turn ? "Player 1" : "Player 2";
        //int currentScore = scoreboard.get(currentPlayer) != null ? scoreboard.get(currentPlayer) : 0;
        scoreboard.put(currentPlayer, scoreboard.get(currentPlayer) + 1);
        player_scoreboardUI();
    }

    private String checkWinner (){
        String results;
        int score1 = scoreboard.getOrDefault("Player 1", 0);
        int score2 = scoreboard.getOrDefault("Player 2", 0);

        if (score1>score2){
            results = "Player 1";
        }else if (score1<score2){
            results = "Player 2";
        }else{
//          results = "It's a tie with both players having " + score1 + " points!";
            results="draw";
        }

        Log.d("checkWinner",String.valueOf(results));
        return results;
    }

    private void endGame(){
        Intent intent = new Intent(FlipCardActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void postendGame(){
        countDownTimer.cancel();
        ForfeitEnabled = false;
    }

    private void onForfeit(View view) {

        if (!ForfeitEnabled){
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Forfeit");
        builder.setMessage("Are you sure you want to forfeit the game?");
        countDownTimer.cancel();

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle the user confirming the forfeit
                forfeitGame();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Dismiss the dialog
                dialog.dismiss();
                startCountdownTimer();
            }
        });

        //builder.setCancelable(false);
        builder.show();
    }

    private void forfeitGame() {
        // Logic to handle forfeiting the game
        Toast.makeText(this, "You have forfeited the game.", Toast.LENGTH_SHORT).show();
        endGame();
    }


    private void showTryAgainDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Game Over");
        builder.setMessage("You Lose! Do you want to try again?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Restart the game or navigate to the appropriate screen
                Log.d("try again","Try Again with this set");
                restartActivity();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Dismiss the dialog and finish the activity
                dialog.dismiss();
                endGame();
            }
        });

        builder.setCancelable(false);
        builder.show();
    }


    private void rematchAgain() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("It is a DRAW!");
        builder.setMessage("Do you want a rematch?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Restart the game or navigate to the appropriate screen
                restartActivity();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Dismiss the dialog and finish the activity
                dialog.dismiss();
                endGame();
            }
        });

        builder.setCancelable(false);
        builder.show();
    }

    private void restartActivity() {
        Intent intent = getIntent();
        intent.putExtra("tryagain", true);
        intent.putExtra("game_mode", game_mode);
        finish();
        startActivity(intent);
    }
    /* ---------------------  On Click --------------------- */

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        //current selected
        //String clickedItem = selectedImageNames[i] == null? "Nothing" : selectedImageNames[i];
        String clickedItem = selectedImageNames[i];
        ImageView imageView = view.findViewById(R.id.grid_image);

        // Disable click handling if not enabled or if the item is already matched
        if (!clickEnabled || i == firstClickedPosition || matchedPositions.contains(i)) {
            return;
        }

        //if first click is null or the user repeatedly click on same item
        if (firstClickedItem == null) {
            // First Pair of Images
            firstClickedItem = clickedItem;
            firstClickedPosition = i;

        } else {
            // Second Pair of Images

            //to grab previous image slot
            View firstClickedView = adapterView.getChildAt(firstClickedPosition);
            previousClickedImageView = firstClickedView.findViewById(R.id.grid_image);

            //if next click is same name and it is in different pos then match
            if (firstClickedItem.equals(clickedItem) && i != firstClickedPosition) {
                Log.d("Match", "Items " + firstClickedItem + " and " + clickedItem + " match.");


                ++matchCounter;
                //multi player
                if(game_mode) {
                    add_playerscore();
                }
                updateMatchCounter();

                matchedPositions.add(i);
                matchedPositions.add(firstClickedPosition);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        gridAdapter.correctImage(firstClickedView);
                        gridAdapter.correctImage(view);
                    }
                }, 800);

                // i need to reset here so i can carry on
                resetTracker();

            } else{
                // Reset after User pick the wrong card
                // Delay before flipping back the cards
                clickEnabled = false;


                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //multi player
                        if(game_mode && matchCounter != 6) {
                            player_turn = !player_turn;
                            player_turnUI();
                        }

                        // Flip back both cards after delay
                        gridAdapter.flipAnimation(imageView,i);
                        gridAdapter.flipAnimation(previousClickedImageView,firstClickedPosition);

                        // Perform wrong choice animation
                        gridAdapter.wrongChoice(previousClickedImageView);
                        gridAdapter.wrongChoice(imageView);

                        // Reset the tracker
                        resetTracker();
                        clickEnabled = true;
                    }
                }, 1000);
            }
        }
        gridAdapter.flipAnimation(imageView,i);
    }

    /* */
    private void resetTracker() {
        firstClickedItem = null;
        firstClickedPosition = -1;
    }


    //Count matches Image
    private void updateMatchCounter() {
        numMatchTextView.setText(getString(R.string.numMatch, matchCounter));

        String results = "You win";

        if(game_mode){
            results = checkWinner();
        }

        if (matchCounter == 6 && results == "draw"){
            postendGame();

            rematchAgain();
            Toast.makeText(FlipCardActivity.this, "DRAW", Toast.LENGTH_SHORT).show();
        }

        else if(matchCounter == 6){
            Log.d("You win", "test");
            postendGame();

            if(game_mode) {
                results = results + " Win";
            }

            TextView PlayerTurnBoard = findViewById(R.id.PlayerTurn);
            PlayerTurnBoard.setText(results);

            Toast.makeText(FlipCardActivity.this, results, Toast.LENGTH_SHORT).show();

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    endGame();
                }
            }, 5000);
        }
    }


    private String[] duplicateStringArray(ArrayList<String> selectedIndices) {
        ArrayList<String> newList = selectedIndices;
        if (!tryagain){
            newList.addAll(selectedIndices);
        }
        Collections.shuffle(newList);
        return newList.stream().toArray(String[]::new);
    }



    //TIMER
    private void startCountdownTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {

            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                NumberFormat f = new DecimalFormat("00");
                long hour = (millisUntilFinished / 3600000) % 24;
                long min = (millisUntilFinished / 60000) % 60;
                long sec = (millisUntilFinished / 1000) % 60;
                Timer.setText(f.format(hour) + ":" + f.format(min) + ":" + f.format(sec));
            }

            @Override
            public void onFinish() {
                Timer.setText("00:00:00");
                Toast.makeText(FlipCardActivity.this, "Game Ended!", Toast.LENGTH_SHORT).show();
                ForfeitEnabled = false;
                clickEnabled = false;
                showTryAgainDialog();
            }
        };

        countDownTimer.start();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }


}

