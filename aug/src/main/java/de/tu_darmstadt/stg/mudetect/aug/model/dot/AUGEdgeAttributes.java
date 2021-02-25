package de.tu_darmstadt.stg.mudetect.aug.model.dot;

import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.AttributeType;

class AUGDirectDataFlowEdgeAttribute implements Attribute {

    @Override
    public String getValue() {
        return "solid";
    }

    @Override
    public AttributeType getType() {
        return AttributeType.STRING;
    }
}

class AUGIndirectDataFlowEdgeAttribute implements Attribute {

    @Override
    public String getValue() {
        return "dotted";
    }

    @Override
    public AttributeType getType() {
        return AttributeType.STRING;
    }
}

class AUGDirectControlFlowEdgeAttribute implements Attribute {

    @Override
    public String getValue() {
        return "bold";
    }

    @Override
    public AttributeType getType() {
        return AttributeType.STRING;
    }
}

class AUGIndirectControlFlowEdgeAttribute implements Attribute {

    @Override
    public String getValue() {
        return "dashed";
    }

    @Override
    public AttributeType getType() {
        return AttributeType.STRING;
    }
}