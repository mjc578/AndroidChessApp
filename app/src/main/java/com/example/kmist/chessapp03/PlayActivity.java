package com.example.kmist.chessapp03;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

                }
            }
        });
    }


}
