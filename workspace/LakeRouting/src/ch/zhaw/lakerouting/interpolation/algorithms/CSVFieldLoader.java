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
    private AbstractList<AbstractList<Double>> field;

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
            field = new ArrayList<AbstractList<Double>>();
            filereader.readHeaders();
            String[] headers = filereader.getHeaders();
            columns = filereader.getHeaderCount();
            AbstractList<Double> header = new ArrayList<Double>();
            for (int k = 1; k < columns; k++) {
                header.add(Double.parseDouble(headers[k]));
            }
            field.add(header);

            while(filereader.readRecord()) {
                AbstractList<Double> line = new ArrayList<Double>();
                for(int k = 0; k < columns; k++) {
                    line.add(Double.parseDouble(filereader.get(k)));
                }
                field.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public Double[][] convertToArray() {
        Double[][] arr = new Double[field.size()][field.get(0).size()];
        for (int i = 0; i < field.size(); i++) {
            arr[i] = field.get(i).toArray(new Double[field.get(0).size()]);
        }
        return arr;
    }
}
