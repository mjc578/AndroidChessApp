package comparators;

import com.example.kmist.chessapp03.ArchivedGame;

import java.util.Comparator;

public class CompareByName implements Comparator<ArchivedGame> {
    @Override
    public int compare(ArchivedGame ag1, ArchivedGame ag2) {
        return ag1.getName().toLowerCase().compareTo(ag2.getName().toLowerCase());
    }
}
