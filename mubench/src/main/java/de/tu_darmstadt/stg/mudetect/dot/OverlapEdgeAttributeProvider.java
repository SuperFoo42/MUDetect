package de.tu_darmstadt.stg.mudetect.dot;

import de.tu_darmstadt.stg.mudetect.aug.model.Edge;
import de.tu_darmstadt.stg.mudetect.aug.model.dot.AUGEdgeAttributeProvider;
import de.tu_darmstadt.stg.mudetect.aug.persistence.StringAttribute;
import de.tu_darmstadt.stg.mudetect.model.Overlap;
import org.jgrapht.nio.Attribute;

import java.util.Map;

class OverlapEdgeAttributeProvider extends AUGEdgeAttributeProvider {
    private final Overlap violation;
    private final String unmappedNodeColor;

    OverlapEdgeAttributeProvider(Overlap violation, String unmappedNodeColor) {
        this.unmappedNodeColor = unmappedNodeColor;
        this.violation = violation;
    }

    @Override
    public Map<String, Attribute> getComponentAttributes(Edge edge) {
        final Map<String, Attribute> attributes = super.getComponentAttributes(edge);
        if (!violation.mapsEdge(edge)) {
            attributes.put("color", new StringAttribute(unmappedNodeColor));
            attributes.put("fontcolor", new StringAttribute(unmappedNodeColor));
        }
        return attributes;
    }
}
