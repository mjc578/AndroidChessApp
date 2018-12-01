package com.example.kmist.chessapp03;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class ReplayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replay);

        String gn = getIntent().getStringExtra("archived_game_name");
        ArrayList<String> ag = getIntent().getStringArrayListExtra("archived_game_moves");

        TextView n = findViewById(R.id.game_name);
        n.setText(gn);
        TextView nm = findViewById(R.id.num_moves);
        nm.setText(Integer.toString(ag.size()));
    }
}
