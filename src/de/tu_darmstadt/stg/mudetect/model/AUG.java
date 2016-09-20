package de.tu_darmstadt.stg.mudetect.model;

import egroum.EGroumEdge;
import egroum.EGroumNode;
import org.jgrapht.graph.DirectedMultigraph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

public class AUG extends DirectedMultigraph<EGroumNode, EGroumEdge> {

    private final Location location;

    public AUG(String name, String filePath) {
        super(EGroumEdge.class);
        this.location = new Location(filePath, name);
    }

    public Location getLocation() {
        return location;
    }

    public int getNodeSize() {
        return vertexSet().size();
    }

    public int getEdgeSize() {
        return edgeSet().size();
    }

    public Map<String, Set<EGroumEdge>> getInEdgesByType(EGroumNode node) {
        return getEdgesByType(node, edge -> getEdgeTarget(edge) == node);
    }

    public Map<String, Set<EGroumEdge>> getOutEdgesByType(EGroumNode node) {
        return getEdgesByType(node, edge -> getEdgeSource(edge) == node);
    }

    private Map<String, Set<EGroumEdge>> getEdgesByType(EGroumNode node, Predicate<EGroumEdge> condition) {
        Map<String, Set<EGroumEdge>> inEdgesByType = new HashMap<>();
        for (EGroumEdge edge : edgesOf(node)) {
            if (condition.test(edge)) {
                String edgeType = edge.getLabel();
                if (!inEdgesByType.containsKey(edgeType)) {
                    inEdgesByType.put(edgeType, new HashSet<>());
                }
                inEdgesByType.get(edgeType).add(edge);
            }
        }
        return inEdgesByType;
    }
}