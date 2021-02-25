package de.tu_darmstadt.stg.mudetect.aug.model.dot;

import de.tu_darmstadt.stg.mudetect.aug.model.DataFlowEdge;
import de.tu_darmstadt.stg.mudetect.aug.model.Edge;
import org.jgrapht.nio.Attribute;

import java.util.LinkedHashMap;
import java.util.Map;

public class AUGEdgeAttributeProvider {
    public Map<String, Attribute> getComponentAttributes(Edge edge) {
        final LinkedHashMap<String, Attribute> attributes = new LinkedHashMap<>();
        Attribute style;
        if (edge instanceof DataFlowEdge) {
            style = edge.isDirect() ?  new AUGDirectDataFlowEdgeAttribute() : new AUGIndirectDataFlowEdgeAttribute();
        } else {
            style = edge.isDirect() ? new AUGDirectControlFlowEdgeAttribute() : new AUGIndirectControlFlowEdgeAttribute();
        }
        attributes.put("style", style);
        return attributes;
    }
}
