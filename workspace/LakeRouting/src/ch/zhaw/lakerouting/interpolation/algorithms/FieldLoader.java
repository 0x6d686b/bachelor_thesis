package ch.zhaw.lakerouting.interpolation.algorithms;

import java.net.URI;

/**
 * Created by IntelliJ IDEA.
 * User: mhk
 * Date: 11.03.12
 * Time: 18:39
 */
public interface FieldLoader {
    boolean loadRessource (URI identifier) throws UnsupportedOperationException;
    Float[][] convertToArray();
}
