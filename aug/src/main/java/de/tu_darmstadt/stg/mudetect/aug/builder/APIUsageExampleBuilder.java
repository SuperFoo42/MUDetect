package de.tu_darmstadt.stg.mudetect.aug.builder;

import de.tu_darmstadt.stg.mudetect.aug.model.APIUsageExample;
import de.tu_darmstadt.stg.mudetect.aug.model.Edge;
import de.tu_darmstadt.stg.mudetect.aug.model.Location;
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
import org.eclipse.jdt.core.dom.ASTNode;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class APIUsageExampleBuilder {
    private final Map<String, Node> nodeMap = new HashMap<>();
    private final Set<Edge> edges = new HashSet<>();
    private final Location location;

    public static APIUsageExampleBuilder buildAUG(Location location) {
        return new APIUsageExampleBuilder(location);
    }

    private APIUsageExampleBuilder(Location location) {
        this.location = location;
    }

    // Action Nodes

    public APIUsageExampleBuilder withArrayAccess(String nodeId, String arrayTypeName, int sourceLineNumber, ASTNode astNode) {
        return withNode(nodeId, new ArrayAccessNode(arrayTypeName, sourceLineNumber, astNode));
    }

    public APIUsageExampleBuilder withArrayAssignment(String nodeId, String baseType, int sourceLineNumber, ASTNode astNode) {
        return withNode(nodeId, new ArrayAssignmentNode(baseType, sourceLineNumber, astNode));
    }

    public APIUsageExampleBuilder withArrayCreation(String nodeId, String baseType, int sourceLineNumber, ASTNode astNode) {
        return withNode(nodeId, new ArrayCreationNode(baseType, sourceLineNumber, astNode));
    }

    public APIUsageExampleBuilder withAssignment(String nodeId, int sourceLineNumber, ASTNode astNode) {
        return withNode(nodeId, new AssignmentNode(sourceLineNumber, astNode));
    }

    public APIUsageExampleBuilder withBreak(String nodeId, int sourceLineNumber, ASTNode astNode) {
        return withNode(nodeId, new BreakNode(sourceLineNumber, astNode));
    }

    public APIUsageExampleBuilder withCast(String nodeId, String targetType, int sourceLineNumber, ASTNode astNode) {
        return withNode(nodeId, new CastNode(targetType, sourceLineNumber, astNode));
    }

    public APIUsageExampleBuilder withConstructorCall(String nodeId, String typeName, int sourceLineNumber, ASTNode astNode) {
        return withNode(nodeId, new ConstructorCallNode(typeName, sourceLineNumber, astNode));
    }

    public APIUsageExampleBuilder withContinue(String nodeId, int sourceLineNumber, ASTNode astNode) {
        return withNode(nodeId, new ContinueNode(sourceLineNumber, astNode));
    }

    public APIUsageExampleBuilder withInfixOperator(String nodeId, String operator, int sourceLineNumber, ASTNode astNode) {
        return withNode(nodeId, new InfixOperatorNode(operator, sourceLineNumber, astNode));
    }

    public APIUsageExampleBuilder withMethodCall(String nodeId, String declaringTypeName, String methodSignature, int sourceLineNumber, ASTNode astNode) {
        return withNode(nodeId, new MethodCallNode(declaringTypeName, methodSignature, sourceLineNumber, astNode));
    }

    public APIUsageExampleBuilder withNullCheck(String nodeId, int sourceLineNumber, ASTNode astNode) {
        return withNode(nodeId, new NullCheckNode(sourceLineNumber, astNode));
    }

    public APIUsageExampleBuilder withReturn(String nodeId, int sourceLineNumber, ASTNode astNode) {
        return withNode(nodeId, new ReturnNode(sourceLineNumber, astNode));
    }

    public APIUsageExampleBuilder withSuperConstructorCall(String nodeId, String superTypeName, int sourceLineNumber, ASTNode astNode) {
        return withNode(nodeId, new SuperConstructorCallNode(superTypeName, sourceLineNumber, astNode));
    }

    public APIUsageExampleBuilder withSuperMethodCall(String nodeId, String declaringTypeName, String methodSignature, int sourceLineNumber, ASTNode astNode) {
        return withNode(nodeId, new SuperMethodCallNode(declaringTypeName, methodSignature, sourceLineNumber, astNode));
    }

    public APIUsageExampleBuilder withThrow(String nodeId, int sourceLineNumber, ASTNode astNode) {
        return withNode(nodeId, new ThrowNode(sourceLineNumber, astNode));
    }

    public APIUsageExampleBuilder withCatch(String nodeId, String exceptionType, int sourceLineNumber, ASTNode astNode) {
        return withNode(nodeId, new CatchNode(exceptionType, sourceLineNumber, astNode));
    }

    public APIUsageExampleBuilder withTypeCheck(String nodeId, String targetTypeName, int sourceLineNumber, ASTNode astNode) {
        return withNode(nodeId, new TypeCheckNode(targetTypeName, sourceLineNumber, astNode));
    }

    public APIUsageExampleBuilder withUnaryOperator(String nodeId, String operator, int sourceLineNumber, ASTNode astNode) {
        return withNode(nodeId, new UnaryOperatorNode(operator, sourceLineNumber, astNode));
    }

    // Data Nodes

    public APIUsageExampleBuilder withAnonymousClassMethod(String nodeId, String baseType, String methodSignature, ASTNode astNode) {
        return withNode(nodeId, new AnonymousClassMethodNode(baseType, methodSignature, astNode));
    }

    public APIUsageExampleBuilder withAnonymousObject(String nodeId, String typeName, ASTNode astNode) {
        return withNode(nodeId, new AnonymousObjectNode(typeName, astNode));
    }

    public APIUsageExampleBuilder withException(String nodeId, String typeName, String variableName, ASTNode astNode) {
        return withNode(nodeId, new ExceptionNode(typeName, variableName, astNode));
    }

    public APIUsageExampleBuilder withLiteral(String nodeId, String typeName, String value, ASTNode astNode) {
        return withNode(nodeId, new LiteralNode(typeName, value, astNode));
    }

    public APIUsageExampleBuilder withVariable(String nodeId, String dataTypeName, String variableName, ASTNode astNode) {
        return withNode(nodeId, new VariableNode(dataTypeName, variableName, astNode));
    }

    public APIUsageExampleBuilder withConstant(String nodeId, String dataType, String dataName, String dataValue, ASTNode astNode) {
        return withNode(nodeId, new ConstantNode(dataType, dataName, dataValue, astNode));
    }

    // Data-Flow Edges

    public APIUsageExampleBuilder withDefinitionEdge(String sourceNodeId, String targetNodeId) {
        return withEdge(new DefinitionEdge(getNode(sourceNodeId), getNode(targetNodeId)));
    }

    public APIUsageExampleBuilder withParameterEdge(String sourceNodeId, String targetNodeId) {
        return withEdge(new ParameterEdge(getNode(sourceNodeId), getNode(targetNodeId)));
    }

    public APIUsageExampleBuilder withQualifierEdge(String sourceNodeId, String targetNodeId) {
        return withEdge(new QualifierEdge(getNode(sourceNodeId), getNode(targetNodeId)));
    }

    public APIUsageExampleBuilder withReceiverEdge(String sourceNodeId, String targetNodeId) {
        return withEdge(new ReceiverEdge(getNode(sourceNodeId), getNode(targetNodeId)));
    }

    // Control-Flow Edges

    public APIUsageExampleBuilder withContainsEdge(String sourceNodeId, String targetNodeId) {
        return withEdge(new ContainsEdge(getNode(sourceNodeId), getNode(targetNodeId)));
    }

    public APIUsageExampleBuilder withExceptionHandlingEdge(String sourceNodeId, String targetNodeId) {
        return withEdge(new ExceptionHandlingEdge(getNode(sourceNodeId), getNode(targetNodeId)));
    }

    public APIUsageExampleBuilder withFinallyEdge(String sourceNodeId, String targetNodeId) {
        return withEdge(new FinallyEdge(getNode(sourceNodeId), getNode(targetNodeId)));
    }

    public APIUsageExampleBuilder withOrderEdge(String sourceNodeId, String targetNodeId) {
        return withEdge(new OrderEdge(getNode(sourceNodeId), getNode(targetNodeId)));
    }

    public APIUsageExampleBuilder withRepetitionEdge(String sourceNodeId, String targetNodeId) {
        return withEdge(new RepetitionEdge(getNode(sourceNodeId), getNode(targetNodeId)));
    }

    public APIUsageExampleBuilder withSelectionEdge(String sourceNodeId, String targetNodeId) {
        return withEdge(new SelectionEdge(getNode(sourceNodeId), getNode(targetNodeId)));
    }

    public APIUsageExampleBuilder withSynchronizationEdge(String sourceNodeId, String targetNodeId) {
        return withEdge(new SynchronizationEdge(getNode(sourceNodeId), getNode(targetNodeId)));
    }

    public APIUsageExampleBuilder withThrowEdge(String sourceNodeId, String targetNodeId) {
        return withEdge(new ThrowEdge(getNode(sourceNodeId), getNode(targetNodeId)));
    }

    // helpers

    private APIUsageExampleBuilder withNode(String nodeId, Node node) {
        nodeMap.put(nodeId, node);
        return this;
    }

    private APIUsageExampleBuilder withEdge(Edge edge) {
        edges.add(edge);
        return this;
    }

    private Node getNode(String nodeId) {
        if (!nodeMap.containsKey(nodeId)) {
            throw new IllegalArgumentException("node with id '" + nodeId + "' does not exist");
        }
        return nodeMap.get(nodeId);
    }

    public APIUsageExample build() {
        APIUsageExample aug = new APIUsageExample(location);
        for (Node node : nodeMap.values()) {
            aug.addVertex(node);
            node.setGraph(aug);
        }
        for (Edge edge : edges) {
            aug.addEdge(edge.getSource(), edge.getTarget(), edge);
        }
        return aug;
    }
}
