package com.example.kmist.chessapp03;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import board.Board;

public class PlayActivity extends AppCompatActivity {

    //when undo button is pressed, set to true, and on start of game
    //set to be false when a regular move has been made
    private boolean undoPressed = true;

    private Button undoButton;
    private Board board;
    private GridView boardView;
    private boolean firstTouch;
    private int firstColor;
    private int[] firstCoords;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        //make a back button
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        //make the board
        board = new Board();
        //populate the board
        board.populate();

        firstCoords = new int[2];
        firstCoords[0] = -1;
        firstCoords[1] = -1;

        setUndoListener();
        setGridViewListener();

    }

    //set listener for the gridview
    private void setGridViewListener(){

        boardView = (GridView) findViewById(R.id.board);
        ChessBoardAdapter adapter = new ChessBoardAdapter(this, board);
        boardView.setAdapter(adapter);

        AdapterView.OnItemClickListener boardListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                ImageView background = (ImageView) view.findViewById(R.id.color);



                int rank = Math.abs(position/8 - 7);
                int file = position%8;

                //selecting a first move
                if (!firstTouch) {
                    firstColor = (Integer) background.getTag();
                    background.setImageResource(R.drawable.selected);
                    firstCoords = new int[2];
                    firstCoords[0] = file;
                    firstCoords[1] = rank;
                    firstTouch = true;
                }
                //selecting a second move, how spicy
                else{
                    //they selected the same spot, they were having doubts, how pitiful
                    if(file == firstCoords[0] && rank == firstCoords[1]){
                        background.setImageResource(firstColor);
                    }
                    //they selected another space! how daring! check if they can move there
                    else{

                    }

                    firstTouch = false;
                }
                Log.e("position", "file is :" + file + "  rank is :" + rank);
                Log.e("piece name: ", "piece is: " + board.getBoard()[file][rank] + " " + board.getBoard()[file][rank].getName());
            }

        };
        boardView.setOnItemClickListener(boardListener);
    }

    //set listener for undo button
    private void setUndoListener(){
        undoButton = findViewById(R.id.undo_button);
        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(undoPressed){
                    Toast.makeText(PlayActivity.this, "Cannot Undo", Toast.LENGTH_SHORT).show();
                }
                else{
                    //do the undoing...
                    undoPressed = true;
                }
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item){
        DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        Intent i = new Intent(PlayActivity.this, Chess.class);
                        startActivity(i);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(PlayActivity.this);
        builder.setMessage("Return to main menu? (Game will not be saved)").setPositiveButton("Yes", dialogListener)
                .setNegativeButton("No", dialogListener).show();
        return true;
    }
}
