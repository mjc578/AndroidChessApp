package comparators;

import com.example.kmist.chessapp03.ArchivedGame;

import java.util.Comparator;

public class CompareByDate implements Comparator<ArchivedGame> {
    @Override
    public int compare(ArchivedGame ag1, ArchivedGame ag2) {
        return ag1.getDateAsDate().compareTo(ag2.getDateAsDate());
    }
}
