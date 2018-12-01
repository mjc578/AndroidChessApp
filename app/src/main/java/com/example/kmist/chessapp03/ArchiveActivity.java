package com.example.kmist.chessapp03;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class ArchiveActivity extends AppCompatActivity implements Serializable {

    private String filename = "games.dat";
    private ArchiveListAdapter archiveAdapter;
    private ArrayList<ArchivedGame> archivedGames;
    private ListView archiveList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive);

        archiveList = (ListView) findViewById(R.id.archive_list_view);
        TextView noGames = (TextView) findViewById(R.id.no_games_text_view);

        setListListener();

        FileInputStream fis;
        ObjectInputStream oin;

        try {
            fis = openFileInput(filename);
            oin = new ObjectInputStream(fis);
            archivedGames = (ArrayList<ArchivedGame>) oin.readObject();
            oin.close();
        } catch (Exception e){}

        if(archivedGames == null){
            archiveList.setVisibility(View.GONE);
        }
        else{
            archiveAdapter = new ArchiveListAdapter(this, archivedGames);
            noGames.setVisibility(View.GONE);
            archiveList.setAdapter(archiveAdapter);
        }
    }

    public void saveGames() {

        FileOutputStream fos;
        ObjectOutputStream out;
        try {
            fos = this.openFileOutput(filename, MODE_PRIVATE);
            out = new ObjectOutputStream(fos);
            out.writeObject(archivedGames);
            out.close();
        } catch (IOException ex) {}
    }

    private void setListListener(){
        archiveList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // When clicked, show a toast with the TextView text
                view.findViewById(R.id.collapsed_archive_view).setVisibility(View.GONE);
                view.findViewById(R.id.selected_archive_view).setVisibility(View.VISIBLE);
            }
        });
    }
}