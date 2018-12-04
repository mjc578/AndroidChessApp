package com.example.kmist.chessapp03;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.List;

public class ChessBoardAdapter extends BaseAdapter {
    private Context mContext;
    private int count = 0;

    public ChessBoardAdapter(Context c){
        this.mContext = c;
    }

    public void increment (int count){
        count++;
    }


    private Integer[] squareColors = {
            R.drawable.light, R.drawable.dark, R.drawable.light, R.drawable.dark, R.drawable.light, R.drawable.dark, R.drawable.light, R.drawable.dark,
            R.drawable.dark, R.drawable.light, R.drawable.dark, R.drawable.light, R.drawable.dark, R.drawable.light, R.drawable.dark, R.drawable.light,
            R.drawable.light, R.drawable.dark, R.drawable.light, R.drawable.dark, R.drawable.light, R.drawable.dark, R.drawable.light, R.drawable.dark,
            R.drawable.dark, R.drawable.light, R.drawable.dark, R.drawable.light, R.drawable.dark, R.drawable.light, R.drawable.dark, R.drawable.light,
            R.drawable.light, R.drawable.dark, R.drawable.light, R.drawable.dark, R.drawable.light, R.drawable.dark, R.drawable.light, R.drawable.dark,
            R.drawable.dark, R.drawable.light, R.drawable.dark, R.drawable.light, R.drawable.dark, R.drawable.light, R.drawable.dark, R.drawable.light,
            R.drawable.light, R.drawable.dark, R.drawable.light, R.drawable.dark, R.drawable.light, R.drawable.dark, R.drawable.light, R.drawable.dark,
            R.drawable.dark, R.drawable.light, R.drawable.dark, R.drawable.light, R.drawable.dark, R.drawable.light, R.drawable.dark, R.drawable.light



    };



    /*
    boolean bw = true; //true if black
    private Integer[][] chessboard;

    //sets colors for squares of chess board
    public void setSquareColors(){
        for (int i=0; i<8; i++){
            for (int j=0; j<8; j++){
                if (bw == true){
                    chessboard[i][j] = R.drawable.dark;
                    bw = false;

                }
                else{
                    chessboard[i][j] = R.drawable.light;
                    bw = true;
                }
            }
        }
    }
    */

    @Override
    public int getCount() {
        return squareColors.length;
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
        ImageView imageView = new ImageView(mContext);
        imageView.setLayoutParams(new GridView.LayoutParams(90,90));
        imageView.setImageResource(squareColors[position]);
        return imageView;
    }


















}
