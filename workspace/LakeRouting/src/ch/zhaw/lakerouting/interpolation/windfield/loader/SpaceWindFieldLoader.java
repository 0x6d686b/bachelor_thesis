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

package ch.zhaw.lakerouting.interpolation.windfield.loader;

import ch.zhaw.lakerouting.datatypes.WindVector;

import java.io.*;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: mhk
 * Date: 20.03.12
 * Time: 14:53
 */
public class SpaceWindFieldLoader implements WindFieldLoader {
    private static final Pattern HEADER_START_PATTERN = Pattern.compile("\\d{4}[\\D&&\\S]{3}\\d{4}");

    private AbstractList<AbstractList<Object>> field;

    @Override
    public final boolean loadRessource(URI identifier) {
        if ( !(identifier.getScheme().equalsIgnoreCase("file")) )
            throw new UnsupportedOperationException("Sorry, we support only file://-handler so far!");

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(identifier.getPath());
        } catch (FileNotFoundException f) {
            f.printStackTrace();
            return false;
        }

        read(fis);

        return true;
    }

    @Override
    public final WindVector[][] convertToArray() {
        WindVector[][] arr = new WindVector[field.size()-1][field.get(0).size()-1];
        for (int i = 0; i < field.size(); i++) {
            for (int j = 0; j < field.size(); j++) {
                // TODO: Boundary problem? Double check!
                arr[i][j] = (WindVector) field.get(i+1).get(j+1);
            }
        }
        return arr;
    }

    private void read(InputStream fis) {
        Scanner scanner = new Scanner(fis, Charset.forName("UTF-8").toString());
        try {
            while (scanner.hasNextLine()) {
                // does not take in account of empty lines -> new field
                if (scanner.hasNext(HEADER_START_PATTERN)) {
                    processHeader(scanner.next());
                }
                field.add(processLine(scanner.next()));
            }
        } finally {
            scanner.close();
        }
    }
    
    private AbstractList<Object> processLine(String input) {
        int i = 0;
        AbstractList<Object> arr = new ArrayList<Object>();

        Scanner scanner = new Scanner(input);
        scanner.useDelimiter(" ");
        try {
            while(scanner.hasNext()) {

                // Do we have the row identifier (the latitude)?
                if (i == 0) {
                    arr.add(scanner.nextDouble());
                    i++;
                    continue;
                }

                // We should now only have some wind vectors left
                WindVector v;
                try {
                    v = new WindVector(scanner.nextDouble(), scanner.nextDouble());
                } catch (NoSuchElementException e) {
                    e.printStackTrace();
                    throw new RuntimeException("FATAL! Input file contains a incomplete value pair!");
                }

                arr.add(v);
                i++;
            }
        } finally {
            scanner.close();
        }
        return arr;
    }
    
    private AbstractList<Object> processHeader(String input) {
        int i = 0;
        AbstractList<Object> arr = new ArrayList<Object>();

        Scanner scanner = new Scanner(input);
        scanner.useDelimiter(" ");
        try {
            while(scanner.hasNext()) {
                if (i == 0) {
                    arr.add(scanner.next(HEADER_START_PATTERN));
                    i++;
                    continue;
                }

                arr.add(scanner.hasNextDouble());
                i++;
            }
        } finally {
            scanner.close();
        }
        return arr;    
    }
}
