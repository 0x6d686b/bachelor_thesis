package ch.zhaw.lakerouting.interpolation.algorithms;

import com.csvreader.CsvReader;
import java.io.FileNotFoundException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.AbstractList;

/**
 * Created by IntelliJ IDEA.
 * User: mhk
 * Date: 11.03.12
 * Time: 18:41
 */
public class CSVFieldLoader implements FieldLoader{
    private CsvReader filereader;
    private AbstractList field;

    @Override
    public boolean loadRessource(URL identifier) throws UnsupportedOperationException{
        if ( !(identifier.getProtocol().equalsIgnoreCase("file://")) )
            throw new UnsupportedOperationException("Sorry, we support only file://-handler so far!");

        try{
            filereader = new CsvReader(identifier.getPath(), ',', Charset.forName("UTF-8"));
        } catch (FileNotFoundException f) {
            f.printStackTrace();
            return false;
        }


        return true;
    }

    @Override
    public Object convertToArray() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
