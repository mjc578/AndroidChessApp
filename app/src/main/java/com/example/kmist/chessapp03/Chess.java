package com.example.kmist.chessapp03;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Chess extends AppCompatActivity {

    private Button playButton;
    private Button archiveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chess);

        setPlayButton();
        setArchiveButton();
    }

    private void setPlayButton(){
        playButton = findViewById(R.id.playButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Chess.this, PlayActivity.class);
                startActivity(i);
            }
        });
    }

    private void setArchiveButton(){
        archiveButton = findViewById(R.id.archiveButton);
        archiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Chess.this, ArchiveActivity.class);
                startActivity(i);
            }
        });
    }
}
