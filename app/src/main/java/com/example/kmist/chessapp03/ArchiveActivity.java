package com.example.kmist.chessapp03;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import java.util.Calendar;
import java.util.Collections;

import comparators.CompareByDate;
import comparators.CompareByName;


public class ArchiveActivity extends AppCompatActivity {

    private String filename = "games.dat";
    private ArchiveListAdapter archiveAdapter;
    private ArrayList<ArchivedGame> archivedGames;
    private ListView archiveList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive);

        //make a back button
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.archive_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        
        switch (id) {
            case R.id.sort_by_name:
                if(archivedGames == null || archivedGames.isEmpty()){
                    return false;
                }
                Collections.sort(archivedGames, new CompareByName());
                archiveList.setAdapter(archiveAdapter);
                return true;

            case R.id.sort_by_date:
                if(archivedGames == null || archivedGames.isEmpty()){
                    return false;
                }
                Collections.sort(archivedGames, new CompareByDate());
                archiveList.setAdapter(archiveAdapter);
                return true;

            case android.R.id.home:
                Intent i = new Intent(ArchiveActivity.this, Chess.class);
                startActivity(i);
                return true;

            default:
                return super.onOptionsItemSelected(item);
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
        TextView deleteText = (TextView) v.findViewById(R.id.selected_archive_delete);
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
                Bundle args = new Bundle();
                args.putSerializable("ARRAYLIST",(Serializable) archivedGames.get(position).getSavedMoves());
                i.putExtra("BUNDLE", args);
                i.putExtra("NAME", archivedGames.get(position).getName());
                i.putExtra("OUTCOME", archivedGames.get(position).getGameOutcome());
                startActivity(i);
            }
        });
    }
}