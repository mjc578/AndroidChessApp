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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import board.Board;
import pieces.King;
import pieces.Position;

public class PlayActivity extends AppCompatActivity {

    private boolean undoPressed = true;
    private Button undoButton;
    private Button resignButton;
    private Button drawButton;
    private Button randomButton;
    private Button saveButton;
    private TextView checkmateText;
    private TextView winnerText;
    private TextView whosMove;
    private TextView check;
    private Board board;
    private GridView boardView;
    private boolean firstTouch;
    private int firstColor;
    private ChessBoardAdapter adapter;
    private boolean whiteTurn = true;
    private ArrayList<Position> savedMoves;

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

        savedMoves = new ArrayList<>();

        whosMove = (TextView) findViewById(R.id.whosMove);
        check = (TextView) findViewById(R.id.status);
        checkmateText = (TextView) findViewById(R.id.checkmate_text);
        winnerText = (TextView) findViewById(R.id.who_won);
        resignButton = (Button) findViewById(R.id.resign_button);
        drawButton = (Button) findViewById(R.id.draw_button);
        randomButton = (Button) findViewById(R.id.random_move_button);
        saveButton = (Button) findViewById(R.id.save_button);

        whosMove.setText(R.string.white_move);

        setUndoListener();
        setGridViewListener();
    }

    //set listener for the gridview
    private void setGridViewListener(){

        boardView = (GridView) findViewById(R.id.board);
        adapter = new ChessBoardAdapter(this, board);
        boardView.setAdapter(adapter);

        AdapterView.OnItemClickListener boardListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageView background = (ImageView) view.findViewById(R.id.color);

                int rank = Math.abs((position / 8) - 7);
                int file = position % 8;
                Position curr = new Position(Position.toChar(file + 1), rank + 1);
                //don't even ACKNOWLEDGE the first move selection of an empty space
                if(board.getBoard()[file][rank] == null && !firstTouch){
                    return;
                }
                //selecting a first move
                if (!firstTouch) {
                    //the other play is trying to select the other player's piece, how naughty!!!
                    if((whiteTurn && board.atPosition(curr).getColor().equals("black")) || (!whiteTurn && board.atPosition(curr).getColor().equals("white"))){
                        return;
                    }
                    firstColor = (Integer) background.getTag();
                    background.setImageResource(R.drawable.selected);
                    savedMoves.add(curr);
                    firstTouch = true;
                }
                //selecting a second move, how spicy
                else{
                    Position prev = savedMoves.get(savedMoves.size() - 1);
                    //they selected the same spot, they were having doubts, how pitiful
                    if(file == prev.getFile() && rank == prev.getRank()){
                        background.setImageResource(firstColor);
                        //they had doubts so remove their first move
                        savedMoves.remove(savedMoves.size() - 1);
                    }
                    //they selected another space! how daring! check if they can move there
                    else{
                        //piece will only move if it can otherwise nothing happens
                        boolean s = board.getBoard()[prev.getFile()][prev.getRank()].move(curr, board);
                        //successful move, store the move and maintain the pawns
                        if(s){
                            board.maintainPawn();
                            savedMoves.add(curr);
                            //check if the move put opponent in check or checkmate
                            if(whiteTurn){
                                King k = (King) board.atPosition(board.getPositionKing("black", board));
                                if(k.isInCheck(board)){
                                    if(k.isCheckmated(board)){
                                        //end the game, hide all buttons
                                        hideGameViews();
                                        showWinViews();
                                    }
                                    check.setText(R.string.check);
                                }
                            }
                            else{
                                King k = (King) board.atPosition(board.getPositionKing("white", board));
                                if(k.isInCheck(board)){
                                    if(k.isCheckmated(board)){
                                        //end the game, hide all buttons
                                        hideGameViews();
                                        showWinViews();
                                    }
                                    check.setText(R.string.check);
                                }
                            }
                            whiteTurn = !whiteTurn;
                            if(!whiteTurn){
                                whosMove.setText(R.string.black_move);
                            }
                            else{
                                whosMove.setText(R.string.white_move);
                            }
                        }
                        //unsuccessful move, remove the first position from the array list
                        else{
                            savedMoves.remove(savedMoves.size() - 1);
                        }
                        adapter = new ChessBoardAdapter(PlayActivity.this, board);
                        boardView.setAdapter(adapter);
                    }

                    firstTouch = false;
                }
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

    public void hideGameViews(){
        undoButton.setVisibility(View.GONE);
        resignButton.setVisibility(View.GONE);
        randomButton.setVisibility(View.GONE);
        drawButton.setVisibility(View.GONE);
        whosMove.setVisibility(View.GONE);
        check.setVisibility(View.GONE);
    }

    public void showWinViews(){
        checkmateText.setVisibility(View.VISIBLE);
        winnerText.setVisibility(View.VISIBLE);
        saveButton.setVisibility(View.VISIBLE);

        if(whiteTurn){
            winnerText.setText(R.string.white_wins);
        }
        else {
            winnerText.setText(R.string.black_wins);
        }

        boardView.setOnItemClickListener(null);
    }
}