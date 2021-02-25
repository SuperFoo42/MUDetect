package de.tu_darmstadt.stg.mudetect.aug.persistence;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import de.tu_darmstadt.stg.mudetect.aug.model.Edge;
import de.tu_darmstadt.stg.mudetect.aug.model.Node;
import de.tu_darmstadt.stg.mudetect.aug.model.actions.ArrayAccessNode;
import de.tu_darmstadt.stg.mudetect.aug.model.actions.ArrayAssignmentNode;
import de.tu_darmstadt.stg.mudetect.aug.model.actions.ArrayCreationNode;
import de.tu_darmstadt.stg.mudetect.aug.model.actions.AssignmentNode;
import de.tu_darmstadt.stg.mudetect.aug.model.actions.BreakNode;
import de.tu_darmstadt.stg.mudetect.aug.model.actions.CastNode;
import de.tu_darmstadt.stg.mudetect.aug.model.actions.CatchNode;
import de.tu_darmstadt.stg.mudetect.aug.model.actions.ConstructorCallNode;
import de.tu_darmstadt.stg.mudetect.aug.model.actions.ContinueNode;
import de.tu_darmstadt.stg.mudetect.aug.model.actions.InfixOperatorNode;
import de.tu_darmstadt.stg.mudetect.aug.model.actions.MethodCallNode;
import de.tu_darmstadt.stg.mudetect.aug.model.actions.NullCheckNode;
import de.tu_darmstadt.stg.mudetect.aug.model.actions.ReturnNode;
import de.tu_darmstadt.stg.mudetect.aug.model.actions.SuperConstructorCallNode;
import de.tu_darmstadt.stg.mudetect.aug.model.actions.SuperMethodCallNode;
import de.tu_darmstadt.stg.mudetect.aug.model.actions.ThrowNode;
import de.tu_darmstadt.stg.mudetect.aug.model.actions.TypeCheckNode;
import de.tu_darmstadt.stg.mudetect.aug.model.actions.UnaryOperatorNode;
import de.tu_darmstadt.stg.mudetect.aug.model.controlflow.ContainsEdge;
import de.tu_darmstadt.stg.mudetect.aug.model.controlflow.ExceptionHandlingEdge;
import de.tu_darmstadt.stg.mudetect.aug.model.controlflow.FinallyEdge;
import de.tu_darmstadt.stg.mudetect.aug.model.controlflow.OrderEdge;
import de.tu_darmstadt.stg.mudetect.aug.model.controlflow.RepetitionEdge;
import de.tu_darmstadt.stg.mudetect.aug.model.controlflow.SelectionEdge;
import de.tu_darmstadt.stg.mudetect.aug.model.controlflow.SynchronizationEdge;
import de.tu_darmstadt.stg.mudetect.aug.model.controlflow.ThrowEdge;
import de.tu_darmstadt.stg.mudetect.aug.model.data.AnonymousClassMethodNode;
import de.tu_darmstadt.stg.mudetect.aug.model.data.AnonymousObjectNode;
import de.tu_darmstadt.stg.mudetect.aug.model.data.ConstantNode;
import de.tu_darmstadt.stg.mudetect.aug.model.data.ExceptionNode;
import de.tu_darmstadt.stg.mudetect.aug.model.data.LiteralNode;
import de.tu_darmstadt.stg.mudetect.aug.model.data.VariableNode;
import de.tu_darmstadt.stg.mudetect.aug.model.dataflow.DefinitionEdge;
import de.tu_darmstadt.stg.mudetect.aug.model.dataflow.ParameterEdge;
import de.tu_darmstadt.stg.mudetect.aug.model.dataflow.QualifierEdge;
import de.tu_darmstadt.stg.mudetect.aug.model.dataflow.ReceiverEdge;
import de.tu_darmstadt.stg.mudetect.aug.model.dot.AUGDotExporter;
import de.tu_darmstadt.stg.mudetect.aug.visitors.NodeVisitor;
import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.AttributeType;

import java.util.HashMap;
import java.util.Map;

public class PersistenceAUGDotExporter extends AUGDotExporter {
    static final String SOURCE_LINE_NUMBER = "l";
    static final String DECLARING_TYPE = "t";
    static final String METHOD_SIGNATURE = "s";
    static final String TARGET_TYPE = "t";
    static final String CATCH_TYPE = "t";
    static final String OPERATOR = "o";
    static final String DATA_NAME = "n";
    static final String DATA_TYPE = "t";
    static final String DATA_VALUE = "v";

    static final BiMap<Class, String> AUG_ELEMENT_TYPE_TO_LABEL = HashBiMap.create();

    static {
        AUG_ELEMENT_TYPE_TO_LABEL.put(ArrayAccessNode.class, "A");
        AUG_ELEMENT_TYPE_TO_LABEL.put(ArrayAssignmentNode.class, "AA");
        AUG_ELEMENT_TYPE_TO_LABEL.put(ArrayCreationNode.class, "AC");
        AUG_ELEMENT_TYPE_TO_LABEL.put(AssignmentNode.class, "As");
        AUG_ELEMENT_TYPE_TO_LABEL.put(BreakNode.class, "B");
        AUG_ELEMENT_TYPE_TO_LABEL.put(CastNode.class, "C");
        AUG_ELEMENT_TYPE_TO_LABEL.put(CatchNode.class, "Ca");
        AUG_ELEMENT_TYPE_TO_LABEL.put(ConstructorCallNode.class, "I");
        AUG_ELEMENT_TYPE_TO_LABEL.put(ContinueNode.class, "Co");
        AUG_ELEMENT_TYPE_TO_LABEL.put(InfixOperatorNode.class, "IO");
        AUG_ELEMENT_TYPE_TO_LABEL.put(MethodCallNode.class, "MC");
        AUG_ELEMENT_TYPE_TO_LABEL.put(NullCheckNode.class, "N");
        AUG_ELEMENT_TYPE_TO_LABEL.put(ReturnNode.class, "Rt");
        AUG_ELEMENT_TYPE_TO_LABEL.put(SuperConstructorCallNode.class, "SI");
        AUG_ELEMENT_TYPE_TO_LABEL.put(SuperMethodCallNode.class, "SC");
        AUG_ELEMENT_TYPE_TO_LABEL.put(ThrowNode.class, "T");
        AUG_ELEMENT_TYPE_TO_LABEL.put(TypeCheckNode.class, "TC");
        AUG_ELEMENT_TYPE_TO_LABEL.put(UnaryOperatorNode.class, "UO");

        AUG_ELEMENT_TYPE_TO_LABEL.put(AnonymousClassMethodNode.class, "ACM");
        AUG_ELEMENT_TYPE_TO_LABEL.put(AnonymousObjectNode.class, "AO");
        AUG_ELEMENT_TYPE_TO_LABEL.put(ConstantNode.class, "CN");
        AUG_ELEMENT_TYPE_TO_LABEL.put(ExceptionNode.class, "E");
        AUG_ELEMENT_TYPE_TO_LABEL.put(LiteralNode.class, "L");
        AUG_ELEMENT_TYPE_TO_LABEL.put(VariableNode.class, "V");

        AUG_ELEMENT_TYPE_TO_LABEL.put(ContainsEdge.class, "Con");
        AUG_ELEMENT_TYPE_TO_LABEL.put(ExceptionHandlingEdge.class, "H");
        AUG_ELEMENT_TYPE_TO_LABEL.put(FinallyEdge.class, "F");
        AUG_ELEMENT_TYPE_TO_LABEL.put(OrderEdge.class, "O");
        AUG_ELEMENT_TYPE_TO_LABEL.put(RepetitionEdge.class, "Re");
        AUG_ELEMENT_TYPE_TO_LABEL.put(SelectionEdge.class, "S");
        AUG_ELEMENT_TYPE_TO_LABEL.put(SynchronizationEdge.class, "Sy");
        AUG_ELEMENT_TYPE_TO_LABEL.put(ThrowEdge.class, "TE");

        AUG_ELEMENT_TYPE_TO_LABEL.put(DefinitionEdge.class, "D");
        AUG_ELEMENT_TYPE_TO_LABEL.put(ParameterEdge.class, "P");
        AUG_ELEMENT_TYPE_TO_LABEL.put(QualifierEdge.class, "Q");
        AUG_ELEMENT_TYPE_TO_LABEL.put(ReceiverEdge.class, "R");
    }

    public PersistenceAUGDotExporter() {
        super(PersistenceAUGDotExporter::getNodeType, PersistenceAUGDotExporter::getEdgeType, node -> new NodeAttributeProvider(new AttributeProvider()).getComponentAttributes(node), null);
    }

    private static String getNodeType(Node node) {
        return AUG_ELEMENT_TYPE_TO_LABEL.get(node.getClass());
    }

    private static String getEdgeType(Edge edge) {
        return AUG_ELEMENT_TYPE_TO_LABEL.get(edge.getClass());
    }

    static class NodeAttributeProvider {
        private final AttributeProvider provider;

        private NodeAttributeProvider(AttributeProvider provider) {
            this.provider = provider;
        }

        public Map<String, Attribute> getComponentAttributes(Node node) {
            return node.apply(provider);
        }
    }

    private static class AttributeProvider implements NodeVisitor<Map<String, Attribute>> {
        @Override
        public Map<String, Attribute> visit(ArrayAccessNode node) {
            HashMap<String, Attribute> attributes = new HashMap<>();
            attributes.put(DECLARING_TYPE, new StringAttribute(node.getDeclaringTypeName()));
            node.getSourceLineNumber().ifPresent(lineNumber -> attributes.put(SOURCE_LINE_NUMBER, new SourceLineAttribute(lineNumber)));
            return attributes;
        }

        @Override
        public Map<String, Attribute> visit(ArrayAssignmentNode node) {
            HashMap<String, Attribute> attributes = new HashMap<>();
            attributes.put(DECLARING_TYPE, new StringAttribute(node.getDeclaringTypeName()));
            node.getSourceLineNumber().ifPresent(lineNumber -> attributes.put(SOURCE_LINE_NUMBER, new SourceLineAttribute(lineNumber)));
            return attributes;
        }

        @Override
        public Map<String, Attribute> visit(ArrayCreationNode node) {
            HashMap<String, Attribute> attributes = new HashMap<>();
            attributes.put(DECLARING_TYPE, new StringAttribute(node.getDeclaringTypeName()));
            node.getSourceLineNumber().ifPresent(lineNumber -> attributes.put(SOURCE_LINE_NUMBER, new SourceLineAttribute(lineNumber)));
            return attributes;
        }

        @Override
        public Map<String, Attribute> visit(AssignmentNode node) {
            HashMap<String, Attribute> attributes = new HashMap<>();
            node.getSourceLineNumber().ifPresent(lineNumber -> attributes.put(SOURCE_LINE_NUMBER, new SourceLineAttribute(lineNumber)));
            return attributes;
        }

        @Override
        public Map<String, Attribute> visit(BreakNode node) {
            HashMap<String, Attribute> attributes = new HashMap<>();
            node.getSourceLineNumber().ifPresent(lineNumber -> attributes.put(SOURCE_LINE_NUMBER, new SourceLineAttribute(lineNumber)));
            return attributes;
        }

        @Override
        public Map<String, Attribute> visit(CastNode node) {
            HashMap<String, Attribute> attributes = new HashMap<>();
            attributes.put(TARGET_TYPE, new StringAttribute(node.getTargetType()));
            node.getSourceLineNumber().ifPresent(lineNumber -> attributes.put(SOURCE_LINE_NUMBER, new SourceLineAttribute(lineNumber)));
            return attributes;

        }

        @Override
        public Map<String, Attribute> visit(CatchNode node) {
            HashMap<String, Attribute> attributes = new HashMap<>();
            attributes.put(CATCH_TYPE, new StringAttribute(node.getExceptionType()));
            node.getSourceLineNumber().ifPresent(lineNumber -> attributes.put(SOURCE_LINE_NUMBER, new SourceLineAttribute(lineNumber)));
            return attributes;

        }

        @Override
        public Map<String, Attribute> visit(ConstructorCallNode node) {
            HashMap<String, Attribute> attributes = new HashMap<>();
            attributes.put(DECLARING_TYPE, new StringAttribute(node.getDeclaringTypeName()));
            node.getSourceLineNumber().ifPresent(lineNumber -> attributes.put(SOURCE_LINE_NUMBER, new SourceLineAttribute(lineNumber)));
            return attributes;

        }

        @Override
        public Map<String, Attribute> visit(ContinueNode node) {
            HashMap<String, Attribute> attributes = new HashMap<>();
            node.getSourceLineNumber().ifPresent(lineNumber -> attributes.put(SOURCE_LINE_NUMBER, new SourceLineAttribute(lineNumber)));
            return attributes;

        }

        @Override
        public Map<String, Attribute> visit(InfixOperatorNode node) {
            HashMap<String, Attribute> attributes = new HashMap<>();
            attributes.put(OPERATOR, new StringAttribute(node.getOperator()));
            node.getSourceLineNumber().ifPresent(lineNumber -> attributes.put(SOURCE_LINE_NUMBER, new SourceLineAttribute(lineNumber)));
            return attributes;

        }

        @Override
        public Map<String, Attribute> visit(MethodCallNode node) {
            HashMap<String, Attribute> attributes = new HashMap<>();
            attributes.put(DECLARING_TYPE, new StringAttribute(node.getDeclaringTypeName()));
            attributes.put(METHOD_SIGNATURE, new StringAttribute(node.getMethodSignature()));
            node.getSourceLineNumber().ifPresent(lineNumber -> attributes.put(SOURCE_LINE_NUMBER, new SourceLineAttribute(lineNumber)));
            return attributes;
        }

        @Override
        public Map<String, Attribute> visit(NullCheckNode node) {
            HashMap<String, Attribute> attributes = new HashMap<>();
            node.getSourceLineNumber().ifPresent(lineNumber -> attributes.put(SOURCE_LINE_NUMBER, new SourceLineAttribute(lineNumber)));
            return attributes;

        }

        @Override
        public Map<String, Attribute> visit(ReturnNode node) {
            HashMap<String, Attribute> attributes = new HashMap<>();
            node.getSourceLineNumber().ifPresent(lineNumber -> attributes.put(SOURCE_LINE_NUMBER, new SourceLineAttribute(lineNumber)));
            return attributes;

        }

        @Override
        public Map<String, Attribute> visit(SuperConstructorCallNode node) {
            HashMap<String, Attribute> attributes = new HashMap<>();
            attributes.put(DECLARING_TYPE, new StringAttribute(node.getDeclaringTypeName()));
            node.getSourceLineNumber().ifPresent(lineNumber -> attributes.put(SOURCE_LINE_NUMBER, new SourceLineAttribute(lineNumber)));
            return attributes;

        }

        @Override
        public Map<String, Attribute> visit(ThrowNode node) {
            HashMap<String, Attribute> attributes = new HashMap<>();
            node.getSourceLineNumber().ifPresent(lineNumber -> attributes.put(SOURCE_LINE_NUMBER, new SourceLineAttribute(lineNumber)));
            return attributes;

        }

        @Override
        public Map<String, Attribute> visit(TypeCheckNode node) {
            HashMap<String, Attribute> attributes = new HashMap<>();
            attributes.put(TARGET_TYPE, new StringAttribute(node.getTargetTypeName()));
            node.getSourceLineNumber().ifPresent(lineNumber -> attributes.put(SOURCE_LINE_NUMBER, new SourceLineAttribute(lineNumber)));
            return attributes;

        }

        @Override
        public Map<String, Attribute> visit(UnaryOperatorNode node) {
            HashMap<String, Attribute> attributes = new HashMap<>();
            attributes.put(OPERATOR, new StringAttribute(node.getOperator()));
            node.getSourceLineNumber().ifPresent(lineNumber -> attributes.put(SOURCE_LINE_NUMBER, new SourceLineAttribute(lineNumber)));
            return attributes;

        }

        @Override
        public Map<String, Attribute> visit(AnonymousClassMethodNode node) {
            HashMap<String, Attribute> attributes = new HashMap<>();
            attributes.put(DATA_TYPE, new StringAttribute(node.getType()));
            attributes.put(METHOD_SIGNATURE, new StringAttribute(node.getName()));
            return attributes;

        }

        @Override
        public Map<String, Attribute> visit(AnonymousObjectNode node) {
            HashMap<String, Attribute> attributes = new HashMap<>();
            attributes.put(DATA_TYPE, new StringAttribute(node.getType()));
            return attributes;

        }

        @Override
        public Map<String, Attribute> visit(ConstantNode node) {
            HashMap<String, Attribute> attributes = new HashMap<>();
            attributes.put(DATA_TYPE, new StringAttribute(node.getType()));
            attributes.put(DATA_NAME, new StringAttribute(node.getName()));
            attributes.put(DATA_VALUE, new StringAttribute(node.getValue()));
            return attributes;

        }

        @Override
        public Map<String, Attribute> visit(ExceptionNode node) {
            HashMap<String, Attribute> attributes = new HashMap<>();
            attributes.put(DATA_TYPE, new StringAttribute(node.getType()));
            attributes.put(DATA_NAME, new StringAttribute(node.getName()));
            return attributes;

        }

        @Override
        public Map<String, Attribute> visit(LiteralNode node) {
            HashMap<String, Attribute> attributes = new HashMap<>();
            attributes.put(DATA_TYPE, new StringAttribute(node.getType()));
            attributes.put(DATA_VALUE, new StringAttribute(node.getValue()));
            return attributes;
        }

        @Override
        public Map<String, Attribute> visit(VariableNode node) {
            HashMap<String, Attribute> attributes = new HashMap<>();
            attributes.put(DATA_TYPE, new StringAttribute(node.getType()));
            attributes.put(DATA_NAME, new StringAttribute(node.getName()));
            return attributes;
        }
    }

    static class SourceLineAttribute implements Attribute {
        private final int line;

        SourceLineAttribute(int line) {
            this.line = line;
        }

        @Override
        public String getValue() {
            return String.valueOf(this.line);
        }

        @Override
        public AttributeType getType() {
            return AttributeType.STRING;
        }
    }

}
