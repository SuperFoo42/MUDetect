package de.tu_darmstadt.stg.mudetect.aug.model;

import de.tu_darmstadt.stg.mudetect.aug.visitors.BaseAUGLabelProvider;
import org.eclipse.jdt.core.dom.ASTNode;

import java.util.Optional;

public abstract class BaseNode implements Node {
    private static int nextNodeId = 0;

    private final int id;
    private final int sourceLineNumber;
    private APIUsageGraph aug;
    private final ASTNode correspondingASTNode;

    protected BaseNode() {
        this(-1, null);
    }

    protected BaseNode(ASTNode correspondingASTNode) {
        this(-1, correspondingASTNode);
    }

    protected BaseNode(int sourceLineNumber) {
        this.sourceLineNumber = sourceLineNumber;
        this.id = nextNodeId++;
        this.correspondingASTNode = null;
    }

    protected BaseNode(int sourceLineNumber, ASTNode correspondingASTNode) {
        this.sourceLineNumber = sourceLineNumber;
        this.correspondingASTNode = correspondingASTNode;
        this.id = nextNodeId++;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setGraph(APIUsageGraph aug) {
        this.aug = aug;
    }

    @Override
    public APIUsageGraph getGraph() {
        return aug;
    }

    public Optional<Integer> getSourceLineNumber() {
        return sourceLineNumber > -1 ? Optional.of(sourceLineNumber) : Optional.empty();
    }

    @Override
    public Node clone() {
        try {
            return (Node) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("All nodes must be cloneable", e);
        }
    }

    @Override
    public String toString() {
        String type = getClass().getSimpleName();
        if (type.endsWith("Node")) {
            type = type.substring(0, type.length() - 4);
        }
        return type + ":" + new BaseAUGLabelProvider().getLabel(this);
    }

    @Override
    public Optional<ASTNode> getCorrespondingASTNode() {
        return Optional.ofNullable(correspondingASTNode);
    }
}
