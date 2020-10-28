package de.tu_darmstadt.stg.mudetect.aug.model.actions;

import de.tu_darmstadt.stg.mudetect.aug.visitors.NodeVisitor;
import org.eclipse.jdt.core.dom.ASTNode;

public class ArrayCreationNode extends ConstructorCallNode {
    public ArrayCreationNode(String baseType, ASTNode astNode) {
        super(baseType, -1, astNode);
    }

    public ArrayCreationNode(String baseType, int sourceLineNumber) {
        super(baseType, sourceLineNumber);
    }

    public ArrayCreationNode(String baseType, int sourceLineNumber, ASTNode astNode) {
        super(baseType, sourceLineNumber, astNode);
    }

    @Override
    public boolean isCoreAction() {
        return false;
    }

    @Override
    public <R> R apply(NodeVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
