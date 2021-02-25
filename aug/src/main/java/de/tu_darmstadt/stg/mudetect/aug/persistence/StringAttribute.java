package de.tu_darmstadt.stg.mudetect.aug.persistence;

import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.AttributeType;

public class StringAttribute implements Attribute {
    private final String attr;

    public StringAttribute(String s) {
        this.attr = s;
    }

    @Override
    public String getValue() {
        return attr;
    }

    @Override
    public AttributeType getType() {
        return AttributeType.STRING;
    }
}
