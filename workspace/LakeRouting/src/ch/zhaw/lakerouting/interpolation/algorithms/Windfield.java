package ch.zhaw.lakerouting.interpolation.algorithms;

import java.net.URI;

/**
 * Created by IntelliJ IDEA.
 * User: mhk
 * Date: 12.03.12
 * Time: 16:27
 */
public class Windfield {
    private Double[][] field;
    private boolean loadArray (FieldLoader field) {
        this.field = field.convertToArray().clone();
        if (this.field != null)
            return true;
        return false;
    }
    public Double[][] getRange (double x, double y) {
        int from_x = 1;
        int from_y = 1;
        int to_x = 2;
        int to_y = 2;
        for (int i = 0; i < field.length; i++) {
            if ( x <= field[i][0] ) {
                from_x = i;
                to_x = i+1;
                break;
            }
        }
        for (int i = 0; i < field[0].length; i++) {
            if( y <= field[0][i] ) {
                from_y = i;
                to_y = i+1;
                break;
            }
        }
        return new Double[][] {{field[from_x][from_y],field[from_x][to_y]},{field[to_x][from_y],field[to_x][to_y]}};
    }

    public boolean loadField (FieldLoader field, URI uri) {
        if (!field.loadRessource(uri))
            return false;
        this.loadArray(field);
        return true;
    }

}
