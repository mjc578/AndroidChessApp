package com.example.kmist.chessapp03;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ArchiveActivity extends AppCompatActivity implements Serializable {

    ArchiveListAdapter archiveAdapter;
    ArrayList<ArchivedGame> archivedGames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive);

        ListView archiveList = (ListView) findViewById(R.id.archive_list_view);
        TextView noGames = (TextView) findViewById(R.id.no_games_text_view);

        //load the archive list from the serial file and if it be null, set the list view to say empty otherwise set adapter
        try {
            archivedGames = readApp();
        } catch (Exception e) {}

        /*
        Just a test entry for the archive list, testing out the date stuff

        archivedGames = new ArrayList<>();
        archivedGames.add(new ArchivedGame("The Greatest Show on Earth", Calendar.getInstance().getTime(), new ArrayList<String>()));

        */
        if(archivedGames == null){
            archiveList.setVisibility(View.GONE);
        }
        else {
            noGames.setVisibility(View.GONE);
            ArchiveListAdapter listAdapter = new ArchiveListAdapter(this, archivedGames);
            archiveList.setAdapter(listAdapter);
        }
    }

    public static final String storeDir = "docs";
    public static final String storeFile = "games.ser";

    public static ArrayList<ArchivedGame> readApp() throws IOException, ClassNotFoundException {

        BufferedReader br = new BufferedReader(new FileReader("docs/games.ser"));
        if (br.readLine() == null) {
            br.close();
            return null;
        }
        ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(storeDir + File.separator + storeFile));
        ArrayList<ArchivedGame> archivedGames = (ArrayList<ArchivedGame>) ois.readObject();
        br.close();
        ois.close();
        return archivedGames;
    }

    public static void writeApp(List<ArchivedGame> archivedGames) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storeDir + File.separator + storeFile));
        oos.writeObject(archivedGames);
    }
}
