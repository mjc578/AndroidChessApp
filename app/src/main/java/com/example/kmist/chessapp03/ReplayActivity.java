package com.example.kmist.chessapp03;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;

import board.Board;
import pieces.King;
import pieces.Position;

public class ReplayActivity extends AppCompatActivity {

    Board board;
    GridView boardView;
    Button nextButton;
    Button rewatchButton;
    ArrayList<Position> savedMoves;
    ArrayList<Position> usedMoves;
    TextView outcome;
    TextView whosMove;
    TextView whoWon;
    TextView checkStatus;
    String game_result;
    boolean whiteTurn = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replay);

        Intent i = getIntent();
        Bundle args = i.getBundleExtra("BUNDLE");
        savedMoves = (ArrayList<Position>) args.getSerializable("ARRAYLIST");
        String gameName = i.getStringExtra("NAME");
        game_result = i.getStringExtra("OUTCOME");

        //make a back button
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        ab.setTitle(gameName);
        getSupportActionBar().setTitle(gameName);

        board = new Board();
        board.populate();

        usedMoves = new ArrayList<>();

        whosMove = findViewById(R.id.whosMove_replay);
        whoWon = findViewById(R.id.who_won_replay);
        outcome = findViewById(R.id.checkmate_text_replay);
        checkStatus = findViewById(R.id.status_replay);

        setNextButtonListener();

        boardView = findViewById(R.id.replay_board);
        ChessBoardAdapter adapter = new ChessBoardAdapter(this, board);
        boardView.setAdapter(adapter);

        if(savedMoves.isEmpty()){
            endGameToggles();
        }
    }

    public boolean onOptionsItemSelected(MenuItem item){
        DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        Intent i = new Intent(ReplayActivity.this, ArchiveActivity.class);
                        startActivity(i);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(ReplayActivity.this);
        builder.setMessage("Return to Archive Menu?").setPositiveButton("Yes", dialogListener)
                .setNegativeButton("No", dialogListener).show();
        return true;
    }

    private void setNextButtonListener(){
        nextButton = findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(savedMoves.isEmpty()){
                    return;
                }
                Position p1 = savedMoves.remove(0);
                Position p2 = savedMoves.remove(0);

                usedMoves.add(p1);
                usedMoves.add(p2);

                board.getBoard()[p1.getFile()][p1.getRank()].move(p2, board);

                whiteTurn = !whiteTurn;

                String color = "black";
                if(whiteTurn){
                    color = "white";
                }
                King k = (King) board.atPosition(board.getPositionKing(color, board));
                if(k.isInCheck(board)){
                    checkStatus.setText(R.string.check);
                }
                else{
                    checkStatus.setText("");
                }

                whosMove.setText(R.string.black_move);
                if(whiteTurn){
                    whosMove.setText(R.string.white_move);
                }

                boardView.setAdapter(new ChessBoardAdapter(ReplayActivity.this, board));

                if (savedMoves.isEmpty()) {
                    endGameToggles();
                }
            }
        });
    }

    private void setRewatchButtonListener(){
        rewatchButton = findViewById(R.id.rewatch);
        rewatchButton.setVisibility(View.VISIBLE);
        rewatchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savedMoves.addAll(usedMoves);
                usedMoves.clear();
                nextButton.setVisibility(View.VISIBLE);
                whosMove.setVisibility(View.VISIBLE);
                checkStatus.setVisibility(View.VISIBLE);
                outcome.setVisibility(View.INVISIBLE);
                whoWon.setVisibility(View.INVISIBLE);
                rewatchButton.setVisibility(View.INVISIBLE);
                checkStatus.setText("");

                board = new Board();
                board.populate();

                boardView.setAdapter(new ChessBoardAdapter(ReplayActivity.this, board));
                if(savedMoves.isEmpty()){
                    endGameToggles();
                }
            }
        });
    }

    public void endGameToggles(){
        nextButton.setVisibility(View.INVISIBLE);
        whosMove.setVisibility(View.INVISIBLE);
        checkStatus.setVisibility(View.INVISIBLE);
        outcome.setVisibility(View.VISIBLE);
        whoWon.setVisibility(View.VISIBLE);
        whoWon.setText(R.string.white_wins);
        if(whiteTurn){
            whoWon.setText(R.string.black_wins);
        }
        if(game_result.equals("draw")){
            outcome.setText(R.string.draw);
            whoWon.setVisibility(View.INVISIBLE);
        }
        else if(game_result.equals("resign")){
            outcome.setText(R.string.black_resigns);
            if(whiteTurn){
                outcome.setText(R.string.white_resigns);
            }
            whoWon.setText(R.string.white_wins);
            if(whiteTurn){
                whoWon.setText(R.string.black_wins);
            }
        }
        setRewatchButtonListener();
    }
}