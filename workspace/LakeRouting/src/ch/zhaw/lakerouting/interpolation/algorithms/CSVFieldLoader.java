/*
 * Copyright (c) 2012, M. Hablützel, F. Yükseldi, J. Ambühl
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the ZHAW, MeteoSchweiz nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL M. Hablützel, F. Yükseldi, J. Ambühl BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

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
    public final boolean loadRessource(URI identifier){
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
            header.add(0.0);
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
    public final Double[][] convertToArray() {
        Double[][] arr = new Double[field.size()][field.get(0).size()];
        for (int i = 0; i < field.size(); i++) {
            arr[i] = field.get(i).toArray(new Double[field.get(0).size()]);
        }
        return arr;
    }
}
