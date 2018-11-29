package com.example.kmist.chessapp03;

import java.util.ArrayList;
import java.util.Date;

public class ArchivedGame {

    private String name;
    //TODO: fix this date thing, should be date but get date must return a string for text view
    private String date;
    private ArrayList<String> savedMoves;

    public ArchivedGame(String name, String date, ArrayList<String> savedMoves){
        this.name = name;
        this.date = date;
        this.savedMoves = savedMoves;
    }

    public String getName(){
        return name;
    }

    public String getDate(){
        return date;
    }

    public int getNumMoves(){
        return savedMoves.size()/2;
    }



}
