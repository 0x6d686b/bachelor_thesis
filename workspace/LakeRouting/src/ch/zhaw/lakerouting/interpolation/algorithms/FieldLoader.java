package ch.zhaw.lakerouting.interpolation.algorithms;

import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: mhk
 * Date: 11.03.12
 * Time: 18:39
 */
public interface FieldLoader<E> {
    boolean loadRessource (URL identifier) throws UnsupportedOperationException;
    E convertToArray();
}
