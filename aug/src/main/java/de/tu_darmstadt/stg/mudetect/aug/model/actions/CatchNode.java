package de.tu_darmstadt.stg.mudetect.aug.model.actions;

import de.tu_darmstadt.stg.mudetect.aug.model.ActionNode;
import de.tu_darmstadt.stg.mudetect.aug.model.BaseNode;
import de.tu_darmstadt.stg.mudetect.aug.visitors.NodeVisitor;
import org.eclipse.jdt.core.dom.ASTNode;

public class CatchNode extends BaseNode implements ActionNode {
    private final String exceptionType;

    public CatchNode(String exceptionType) {
        this.exceptionType = exceptionType;
    }

    public CatchNode(String exceptionType, int sourceLineNumber) {
        super(sourceLineNumber);
        this.exceptionType = exceptionType;
    }

    public CatchNode(String exceptionType, int sourceLineNumber, ASTNode astNode) {
        super(sourceLineNumber, astNode);
        this.exceptionType = exceptionType;
    }

    public String getExceptionType() {
        return exceptionType;
    }

    @Override
    public boolean isCoreAction() {
        return true;
    }

    @Override
    public <R> R apply(NodeVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
