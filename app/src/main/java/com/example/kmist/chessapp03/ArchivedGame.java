package com.example.kmist.chessapp03;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ArchivedGame implements Serializable {

    private String name;
    private Date date;
    private ArrayList<String> savedMoves;

    public ArchivedGame(String name, Date date, ArrayList<String> savedMoves){
        this.name = name;
        this.date = date;
        this.savedMoves = savedMoves;
    }

    public String getName(){
        return name;
    }

    public String getDate(){
        return new SimpleDateFormat("M/dd/yy").format(date);
    }

    public int getNumMoves(){
        return savedMoves.size()/2;
    }

    public ArrayList<String> getSavedMoves(){
        return savedMoves;
    }

}
