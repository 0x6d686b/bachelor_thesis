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

package ch.zhaw.lakerouting.interpolation.windfield;

import ch.zhaw.lakerouting.datatypes.Node;
import ch.zhaw.lakerouting.interpolation.algorithms.InterpolationAlgorithm;
import ch.zhaw.lakerouting.interpolation.windfield.loader.WindFieldLoader;
import org.joda.time.DateTime;
import org.joda.time.Interval;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * A container for a List of Windfields.
 *
 * <p>To handle a List of Windfields easier and provide some helping functionality
 * the WindfieldContainer provides some useful wrapper methods.</p>
 *
 * <p>Each Windfield represents a field of WindVectors at a certain time.</p>
 *
 * @author Mathias Hablützel
 * @since 1.0
 * @version 1.0-stable
 */

/*
 I'm just wondering if this should not actually implement the List interface ...
 mhk - 20.05.12
 */
public class WindfieldContainer {
    /** Date and time of the first Windfield */
    private DateTime starttime;
    /** Date and time of the last Windfield */
    private DateTime endtime;
    /** Interval between two Windfields */
    private Interval delta;
    /** List of all the Windfields */
    private List<Windfield> fields;

    /**
     * Loads all Windfields from an URI with the specified loader class.
     * @param identifier ressource locator
     * @param loader class specifing how to read the ressource
     * @return returns true on success otherwise false
     */
    public final boolean bulkLoadWindfield (URI identifier, WindFieldLoader loader) {
        try {
            fields = loader.loadRessource(identifier);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        // Ugly ...
        this.starttime = fields.get(0).getMetadata().getDate();
        this.endtime = fields.get(fields.size()-1).getMetadata().getDate();
        this.delta = new Interval(starttime, fields.get(1).getMetadata().getDate());
        return true;
    }

    /**
     * Interpolates all Windfields on a given List of Coordinates with the indicated
     * InterpolationAlgorithm.
     * @param coordinates two-dimensional List of Coordinates
     * @param algorithm interpolation algorithm
     * @return
     */
    public final WindfieldContainer bulkInterpolateOnDecisionNet (List<List<Node>> coordinates, InterpolationAlgorithm algorithm) {
        WindfieldContainer container = new WindfieldContainer();
        container.fields = new ArrayList<Windfield>();
        container.starttime = this.starttime;
        container.endtime = this.endtime;
        container.delta = this.delta;
        // WTF?
        for (int i = 0; i < this.fields.size(); i++) {
            container.fields.add(new Windfield().setField(this.get(i).getMetadata(),this.get(i).interpolateOnDecisionNet(coordinates,algorithm)));
        }
        return container;
    }

    /**
     * Primitive getter for the Windfield at index.
     * @param index index of the Windfield
     * @return Windfield at index
     */
    public final Windfield get(int index) {
        return fields.get(index);
    }

    /**
     * Primitive getter for the DateTime of the first Windfield.
     * @return a DateTime object of the first Windfield
     */
    public DateTime getStarttime() {
        return this.starttime;
    }

    /**
     * Primitive getter for the DateTime of the last Windfield.
     * @return a DateTime object of the last Windfield
     */
    public DateTime getEndtime() {
        return this.endtime;
    }

    /**
     * Primitve getter for the Interval between two Windfields.
     *
     * <p>However note that for the calculation of the interval only the first two Windfields
     * will be considered, meaning the first interval will be used for all consequent Windfields.</p>
     * @return a Interval between two Windfields.
     */
    public Interval getDelta() {
        return this.delta;
    }

    /**
     * Size of container.
     * @return number of stored Windfields
     */
    public int size() {
        return fields.size();
    }

}
