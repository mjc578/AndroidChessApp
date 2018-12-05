package com.example.kmist.chessapp03;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.List;

import board.Board;
import pieces.Pieces;

public class ChessBoardAdapter extends BaseAdapter {

    private Context mContext;
    private int ms = 0;
    private Pieces[] oneDboard;
    private LayoutInflater mli;
    private Integer[] squareColors = {
            R.drawable.light, R.drawable.dark, R.drawable.light, R.drawable.dark,
            R.drawable.light, R.drawable.dark, R.drawable.light, R.drawable.dark,
            R.drawable.dark, R.drawable.light, R.drawable.dark, R.drawable.light,
            R.drawable.dark, R.drawable.light, R.drawable.dark, R.drawable.light,
            R.drawable.light, R.drawable.dark, R.drawable.light, R.drawable.dark,
            R.drawable.light, R.drawable.dark, R.drawable.light, R.drawable.dark,
            R.drawable.dark, R.drawable.light, R.drawable.dark, R.drawable.light,
            R.drawable.dark, R.drawable.light, R.drawable.dark, R.drawable.light,
            R.drawable.light, R.drawable.dark, R.drawable.light, R.drawable.dark,
            R.drawable.light, R.drawable.dark, R.drawable.light, R.drawable.dark,
            R.drawable.dark, R.drawable.light, R.drawable.dark, R.drawable.light,
            R.drawable.dark, R.drawable.light, R.drawable.dark, R.drawable.light,
            R.drawable.light, R.drawable.dark, R.drawable.light, R.drawable.dark,
            R.drawable.light, R.drawable.dark, R.drawable.light, R.drawable.dark,
            R.drawable.dark, R.drawable.light, R.drawable.dark, R.drawable.light,
            R.drawable.dark, R.drawable.light, R.drawable.dark, R.drawable.light,
    };

    public ChessBoardAdapter(Context c, Board board){
        oneDboard = new Pieces[64];
        mli = LayoutInflater.from(c.getApplicationContext());
        int count = 0;
        for(int i = 7; i >= 0; i--){
            for(int j = 0; j < 8; j++){
                oneDboard[count] = board.getBoard()[j][i];
                count++;
            }
        }
        this.mContext = c;
    }

    @Override
    public int getCount() {
        return 64;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View squareView = convertView;
        if(squareView == null){
            squareView = mli.inflate(R.layout.square, parent, false);

            ImageView background = squareView.findViewById(R.id.color);
            background.setImageResource(squareColors[position]);
            background.setTag(squareColors[position]);

            ImageView piece = squareView.findViewById(R.id.piece);
            if(oneDboard[position] != null){
                piece.setImageResource(oneDboard[position].getResImage());
            }
        }

        return squareView;
    }
}
