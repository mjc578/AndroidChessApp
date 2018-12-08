package com.example.kmist.chessapp03;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import pieces.Position;

public class ArchivedGame implements Serializable {

    private String name;
    private Calendar date;
    private ArrayList<Position> savedMoves;
    private String gameOutcome;

    public ArchivedGame(String name, Calendar date, ArrayList<Position> savedMoves, String gameOutcome){
        this.name = name;
        this.date = date;
        this.savedMoves = savedMoves;
        this.gameOutcome = gameOutcome;
    }

    public String getName(){
        return name;
    }

    public Calendar getDateAsDate(){
        return date;
    }

    public String getDate(){
        return new SimpleDateFormat("M/dd/yy").format(date.getTime());
    }

    public int getNumMoves(){
        return savedMoves.size()/2;
    }

    public ArrayList<Position> getSavedMoves(){
        return savedMoves;
    }

    public String getGameOutcome() {
        return gameOutcome;
    }
}
