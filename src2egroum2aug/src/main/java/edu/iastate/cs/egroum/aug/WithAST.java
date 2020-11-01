package edu.iastate.cs.egroum.aug;

import org.eclipse.jdt.core.dom.CompilationUnit;

public class WithAST<T> {
    private final T element;
    private final CompilationUnit cu;
    public WithAST(T element, CompilationUnit cu)
    {
        this.cu = cu;
        this.element = element;
    }

    public T getElement()
    {
        return this.element;
    }

    public CompilationUnit getAST()
    {
        return this.cu;
    }
}
