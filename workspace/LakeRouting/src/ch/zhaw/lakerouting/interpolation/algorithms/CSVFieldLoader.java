package ch.zhaw.lakerouting.interpolation.algorithms;

import com.csvreader.CsvReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.AbstractList;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: mhk
 * Date: 11.03.12
 * Time: 18:41
 */
public class CSVFieldLoader implements FieldLoader{
    private AbstractList<AbstractList<Float>> field;

    @Override
    public boolean loadRessource(URI identifier) throws UnsupportedOperationException{
        if ( !(identifier.getScheme().equalsIgnoreCase("file")) )
            throw new UnsupportedOperationException("Sorry, we support only file://-handler so far!");

        CsvReader filereader;
        int columns;

        try{
            filereader = new CsvReader(identifier.getPath(), ',', Charset.forName("UTF-8"));
        } catch (FileNotFoundException f) {
            f.printStackTrace();
            return false;
        }

        try {
            field = new ArrayList<AbstractList<Float>>();
            filereader.readHeaders();
            String[] headers = filereader.getHeaders();
            columns = filereader.getHeaderCount();
            AbstractList<Float> header = new ArrayList<Float>();
            for (int k = 1; k < columns; k++) {
                header.add(Float.parseFloat(headers[k]));
            }
            field.add(header);

            while(filereader.readRecord()) {
                AbstractList<Float> line = new ArrayList<Float>();
                for(int k = 0; k < columns; k++) {
                    line.add(Float.parseFloat(filereader.get(k)));
                }
                field.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public Float[][] convertToArray() {
        Float[][] arr = new Float[field.size()][field.get(0).size()];
        for (int i = 0; i < field.size(); i++) {
            arr[i] = field.get(i).toArray(new Float[field.get(0).size()]);
        }
        return arr;
    }
}
