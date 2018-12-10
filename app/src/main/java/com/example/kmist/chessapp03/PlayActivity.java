package com.example.kmist.chessapp03;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

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
    private Board prevBoard;
    private GridView boardView;
    private boolean firstTouch;
    private int firstColor;
    private ChessBoardAdapter adapter;
    private boolean whiteTurn = true;
    private ArrayList<Position> savedMoves;
    private String saveGameText = "";
    private ArrayList<ArchivedGame> savedGames;
    private String gameOutcome = "Checkmate";

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

        savedGames = getSavedGames();
        savedMoves = new ArrayList<>();

        whosMove = (TextView) findViewById(R.id.whosMove);
        check = (TextView) findViewById(R.id.status);
        checkmateText = (TextView) findViewById(R.id.checkmate_text);
        winnerText = (TextView) findViewById(R.id.who_won);

        setSaveListener();
        setUndoListener();
        setGridViewListener();
        setResignListener();
        setDrawListener();
        setRandomListener();
    }

    private void setRandomListener(){
        randomButton = (Button) findViewById(R.id.random_move_button);
        randomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String color = "black";
                if(whiteTurn){
                    color = "white";
                }

                prevBoard = new Board();
                copyBoard(prevBoard, board);
                undoPressed = false;

                Position[] result = board.makeRandomMove(color);

                //TODO do checks on checkmate and check

                if(result == null){
                    Toast.makeText(PlayActivity.this, "Cannot make a random move", Toast.LENGTH_SHORT).show();
                    return;
                }
                savedMoves.add(result[0]);
                savedMoves.add(result[1]);
                checkForCheckOrMate();
                boardView.setAdapter(new ChessBoardAdapter(PlayActivity.this, board));
                whiteTurn = !whiteTurn;
                whosMove.setText(R.string.black_move);
                if(whiteTurn){
                    whosMove.setText(R.string.white_move);
                }
            }
        });
    }

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
                        prevBoard = new Board();
                        copyBoard(prevBoard, board);
                        //piece will only move if it can otherwise nothing happens
                        boolean s = board.getBoard()[prev.getFile()][prev.getRank()].move(curr, board);
                        //successful move, store the move and maintain the pawns
                        if(s){
                            undoPressed = false;
                            board.maintainPawn();
                            savedMoves.add(curr);
                            checkForCheckOrMate();
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
                    if(firstTouch){
                        savedMoves.remove(savedMoves.size() - 1);
                        firstTouch = !firstTouch;
                    }
                    savedMoves.remove(savedMoves.size() - 1);
                    savedMoves.remove(savedMoves.size() - 1);

                    copyBoard(board, prevBoard);
                    checkForCheckOrMate();

                    adapter = new ChessBoardAdapter(PlayActivity.this, board);
                    boardView.setAdapter(adapter);
                    undoPressed = true;
                    whiteTurn = !whiteTurn;
                    whosMove.setText(R.string.black_move);
                    if(whiteTurn){
                        whosMove.setText(R.string.white_move);
                    }
                }
            }
        });
    }

    private void setResignListener(){
        resignButton = (Button) findViewById(R.id.resign_button);
        resignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                whiteTurn = !whiteTurn;
                                gameOutcome = "resign";
                                hideGameViews();
                                showWinViews();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(PlayActivity.this);
                builder.setMessage("Resign?").setPositiveButton("Yes", dialogListener)
                        .setNegativeButton("No", dialogListener).show();
            }
        });
    }

    private void setDrawListener(){
        drawButton = (Button) findViewById(R.id.draw_button);
        drawButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                whiteTurn = !whiteTurn;
                                gameOutcome = "draw";
                                hideGameViews();
                                showWinViews();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(PlayActivity.this);
                builder.setMessage("Confirm to a draw?").setPositiveButton("Yes", dialogListener)
                        .setNegativeButton("No", dialogListener).show();
            }
        });
    }

    private void setSaveListener(){
        saveButton = (Button) findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog dialog = new AlertDialog.Builder(PlayActivity.this)
                        .setView(v)
                        .setTitle(R.string.enter_name)
                        .setPositiveButton(android.R.string.ok, null) //Set to null. We override the onclick
                        .setNegativeButton(android.R.string.cancel, null)
                        .create();

                final EditText input = new EditText(PlayActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                dialog.setView(input);

                dialog.setOnShowListener(new DialogInterface.OnShowListener() {

                    @Override
                    public void onShow(DialogInterface dialogInterface) {

                        Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                        button.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                saveGameText = input.getText().toString();
                                boolean exists = false;
                                for(ArchivedGame ag : savedGames){
                                    if(ag.getName().equals(saveGameText)){
                                        exists = true;
                                        break;
                                    }
                                }
                                if(saveGameText.equals("") || exists){
                                    if(exists){
                                        Toast.makeText(PlayActivity.this, "Game Name Already Exists", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    Toast.makeText(PlayActivity.this, "Enter a Game Name", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                ArchivedGame currentGame = new ArchivedGame(saveGameText, Calendar.getInstance(), savedMoves, gameOutcome);
                                savedGames.add(currentGame);
                                saveGames();

                                //Dismiss once everything is OK.
                                dialog.dismiss();
                            }
                        });
                    }
                });
                dialog.show();
            }
        });
    }

    @Override
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
        builder.setMessage("Return to main menu?").setPositiveButton("Yes", dialogListener)
                .setNegativeButton("No", dialogListener).show();
        return true;
    }

    private void hideGameViews(){
        undoButton.setVisibility(View.GONE);
        resignButton.setVisibility(View.GONE);
        randomButton.setVisibility(View.GONE);
        drawButton.setVisibility(View.GONE);
        whosMove.setVisibility(View.GONE);
        check.setVisibility(View.GONE);
    }

    private void showWinViews(){
        checkmateText.setVisibility(View.VISIBLE);
        winnerText.setVisibility(View.VISIBLE);
        saveButton.setVisibility(View.VISIBLE);

        if(gameOutcome.equals("draw")){
            checkmateText.setText(R.string.draw);
            winnerText.setText("");
            boardView.setOnItemClickListener(null);
            return;
        }
        else if(gameOutcome.equals("resign")){
            if(whiteTurn){
                checkmateText.setText(R.string.black_resigns);
            }
            else{
                checkmateText.setText(R.string.white_resigns);
            }
        }

        if(whiteTurn){
            winnerText.setText(R.string.white_wins);
        }
        else {
            winnerText.setText(R.string.black_wins);
        }

        boardView.setOnItemClickListener(null);
    }

    private String filename = "games.dat";

    private void saveGames() {
        FileOutputStream fos;
        ObjectOutputStream out;
        try {
            fos = this.openFileOutput(filename, MODE_PRIVATE);
            out = new ObjectOutputStream(fos);
            out.writeObject(savedGames);
            out.close();
        } catch (IOException ex) {}
    }

    private ArrayList<ArchivedGame> getSavedGames(){
        ArrayList<ArchivedGame> a = null;
        try {
            FileInputStream fis = openFileInput(filename);
            ObjectInputStream oin = new ObjectInputStream(fis);
            a = (ArrayList<ArchivedGame>) oin.readObject();
            oin.close();
            fis.close();
        } catch (Exception e){}
        if(a == null){
            a = new ArrayList<>();
        }
        return a;
    }

    private void copyBoard(Board boardToFill, Board boardToCopy){
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if(boardToCopy.getBoard()[i][j] != null){
                    boardToFill.getBoard()[i][j] = boardToCopy.getBoard()[i][j].copyPiece();
                }
                else{
                    boardToFill.getBoard()[i][j] = null;
                }
            }
        }
    }

    private void checkForCheckOrMate(){
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
            else{
                check.setText("");
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
            else{
                check.setText("");
            }
        }
    }
}