package de.tu_darmstadt.stg.mudetect.aug.model.data;

import de.tu_darmstadt.stg.mudetect.aug.visitors.NodeVisitor;
import org.eclipse.jdt.core.dom.ASTNode;

public class ExceptionNode extends VariableNode {
    public ExceptionNode(String exceptionType, String variableName) {
        super(exceptionType, variableName);
    }

    public ExceptionNode(String exceptionType, String variableName, ASTNode astNode) {
        super(exceptionType, variableName, astNode);
    }

    @Override
    public <R> R apply(NodeVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
