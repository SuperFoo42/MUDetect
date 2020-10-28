package de.tu_darmstadt.stg.mudetect.aug.model.actions;

import de.tu_darmstadt.stg.mudetect.aug.model.ActionNode;
import de.tu_darmstadt.stg.mudetect.aug.model.BaseNode;
import de.tu_darmstadt.stg.mudetect.aug.visitors.NodeVisitor;
import org.eclipse.jdt.core.dom.ASTNode;

public class ReturnNode extends BaseNode implements ActionNode {
    public ReturnNode() {}

    public ReturnNode(int sourceLineNumber) {
        super(sourceLineNumber);
    }
    public ReturnNode(int sourceLineNumber, ASTNode astNode) {
        super(sourceLineNumber, astNode);
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
