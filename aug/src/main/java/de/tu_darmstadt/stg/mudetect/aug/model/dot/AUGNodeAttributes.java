package de.tu_darmstadt.stg.mudetect.aug.model.dot;

import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.AttributeType;

class AUGActionNodeAttribute implements Attribute {

    @Override
    public String getValue() {
        return "box";
    }

    @Override
    public AttributeType getType() {
        return AttributeType.STRING;
    }
}

class AUGDataNodeAttribute implements Attribute {

    @Override
    public String getValue() {
        return "ellipse";
    }

    @Override
    public AttributeType getType() {
        return AttributeType.STRING;
    }
}
