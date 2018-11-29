package com.example.kmist.chessapp03;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.Serializable;

import board.Board;
import pieces.Position;

public class PlayActivity extends AppCompatActivity implements Serializable {

    //when undo button is pressed, set to true, and on start of game
    //set to be false when a regular move has been made
    private boolean undoPressed = true;

    private Button undoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        //make a back button
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        setUndoListener();

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
