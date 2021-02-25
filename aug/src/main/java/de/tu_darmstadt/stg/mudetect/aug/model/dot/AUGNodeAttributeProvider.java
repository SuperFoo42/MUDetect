package de.tu_darmstadt.stg.mudetect.aug.model.dot;

import de.tu_darmstadt.stg.mudetect.aug.model.ActionNode;
import de.tu_darmstadt.stg.mudetect.aug.model.DataNode;
import de.tu_darmstadt.stg.mudetect.aug.model.Node;
import org.jgrapht.nio.Attribute;

import java.util.LinkedHashMap;
import java.util.Map;

public class AUGNodeAttributeProvider {
    public Map<String, Attribute> getComponentAttributes(Node node) {
        final LinkedHashMap<String, Attribute> attributes = new LinkedHashMap<>();
        if (node instanceof ActionNode) {
            attributes.put("shape", new AUGActionNodeAttribute());
        } else if (node instanceof DataNode) {
            attributes.put("shape", new AUGDataNodeAttribute());
        }
        return attributes;
    }
}
