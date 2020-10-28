package de.tu_darmstadt.stg.mudetect.aug.model.actions;

import de.tu_darmstadt.stg.mudetect.aug.model.ActionNode;
import de.tu_darmstadt.stg.mudetect.aug.model.BaseNode;
import org.eclipse.jdt.core.dom.ASTNode;

public abstract class OperatorNode extends BaseNode implements ActionNode {
    private final String operator;

    OperatorNode(String operator) {
        this.operator = operator;
    }

    OperatorNode(String operator, int sourceLineNumber) {
        super(sourceLineNumber);
        this.operator = operator;
    }

    OperatorNode(String operator, int sourceLineNumber, ASTNode astNode) {
        super(sourceLineNumber, astNode);
        this.operator = operator;
    }

    public String getOperator() {
        return operator;
    }

    @Override
    public boolean isCoreAction() {
        return false;
    }
}
