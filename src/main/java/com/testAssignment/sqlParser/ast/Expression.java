package com.testAssignment.sqlParser.ast;

import java.util.List;
import java.util.stream.Collectors;

public class Expression {
    public final String value;

    public Expression(String value) {
        this.value = value;
    }

    public static Expression fromFunctionCall(String functionName, List<Expression> arguments) {
        var argumentsString = arguments.stream()
                .map(Expression::toString)
                .collect(Collectors.joining(", "));

        return new Expression(functionName + '(' + argumentsString + ')');
    }

    public static Expression fromTableColumn(String table, String column) {
        return new Expression(table + '.' + column);
    }

    @Override
    public String toString() {
        return value;
    }
}
