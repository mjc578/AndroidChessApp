package com.example.kmist.chessapp03;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

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
    private TextView deleteText;
    private TextView playText;

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
            /*
            archivedGames = new ArrayList<>();
            archivedGames.add(new ArchivedGame("TGSOE", Calendar.getInstance().getTime(), new ArrayList<String>()));
            archivedGames.add(new ArchivedGame("EOGES", Calendar.getInstance().getTime(), new ArrayList<String>()));
            archivedGames.add(new ArchivedGame("bripers", Calendar.getInstance().getTime(), new ArrayList<String>()));
            archivedGames.add(new ArchivedGame("peppers", Calendar.getInstance().getTime(), new ArrayList<String>()));
            archivedGames.add(new ArchivedGame("TEgla", Calendar.getInstance().getTime(), new ArrayList<String>()));
            saveGames();
            */
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

                for(int i = 0; i < archiveList.getCount(); i++){
                    View v = archiveList.getChildAt(i);
                    v.findViewById(R.id.selected_archive_view).setVisibility(View.GONE);
                    v.findViewById(R.id.collapsed_archive_view).setVisibility(View.VISIBLE);
                }
                if(view.findViewById(R.id.selected_archive_view).getVisibility() == View.GONE){
                    view.findViewById(R.id.collapsed_archive_view).setVisibility(View.GONE);
                    view.findViewById(R.id.selected_archive_view).setVisibility(View.VISIBLE);

                    setDeleteListener(view, position);
                    setPlayListener(view, position);
                }
                else{
                    view.findViewById(R.id.collapsed_archive_view).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.selected_archive_view).setVisibility(View.GONE);
                }
            }
        });
    }

    private void setDeleteListener(View v, final int position){
        deleteText = (TextView) v.findViewById(R.id.selected_archive_delete);
        deleteText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                archivedGames.remove(position);
                                saveGames();
                                archiveList.setAdapter(archiveAdapter);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:

                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(ArchiveActivity.this);
                builder.setMessage("Delete this archived game?").setPositiveButton("Yes", dialogListener)
                        .setNegativeButton("No", dialogListener).show();
            }
        });

    }

    private void setPlayListener(View v, final int position){
        TextView ptv = (TextView) v.findViewById(R.id.selected_archive_play);
        ptv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ArchiveActivity.this, ReplayActivity.class);
                i.putExtra("archived_game_name", archivedGames.get(position).getName());
                i.putExtra("archived_game_moves", archivedGames.get(position).getSavedMoves());
                startActivity(i);
            }
        });
    }
}