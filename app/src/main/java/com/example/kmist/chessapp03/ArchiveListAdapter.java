package com.example.kmist.chessapp03;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ArchiveListAdapter extends ArrayAdapter<ArchivedGame> {

    ArrayList<ArchivedGame> archiveList;


    public ArchiveListAdapter(Context context, ArrayList<ArchivedGame> archiveList){
        super(context, 0, archiveList);
        this.archiveList = archiveList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // check if view is being reused, else inflate
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.archive_list_entry, parent, false);
        }
        //from the ArrayAdapter, get the project that should go in its corresponding place in the list
        //ex. If the list entry is the 2nd entry per se, position then equals 1, corresponding to the
        //second ArrayAdaptor element, so getItem gets the object at the 2nd index
        ArchivedGame currentArchive = getItem(position);

        TextView archiveName = (TextView) listItemView.findViewById(R.id.archive_name);
        archiveName.setText(currentArchive.getName());

        TextView projectNumEntries = (TextView) listItemView.findViewById(R.id.archive_move_number);
        String numRecs = Integer.toString(currentArchive.getNumMoves()) + " " + getContext().getString(R.string.num_moves);
        projectNumEntries.setText(numRecs); //0 Recording(s)

        //get textview of the date the project was created/updated and then set it
        TextView projectDate = (TextView) listItemView.findViewById(R.id.archive_entry_date);
        projectDate.setText(currentArchive.getDate());

        return listItemView;
    }

    //method for retrieving the saved project list when resuming/creating activity
    public ArrayList<ArchivedGame> getProjectList(){
        return archiveList;
    }
}
