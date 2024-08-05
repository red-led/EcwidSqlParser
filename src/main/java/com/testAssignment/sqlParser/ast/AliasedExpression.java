package com.testAssignment.sqlParser.ast;

public class AliasedExpression extends Expression {
    public final String alias;

    public AliasedExpression(Expression expression, String alias) {
        super(expression.value);
        this.alias = alias;
    }

    @Override
    public String toString() {
        return "AliasedExpression{" +
                "value=" + value + ", " +
                "alias=" + alias +
                '}';
    }
}
