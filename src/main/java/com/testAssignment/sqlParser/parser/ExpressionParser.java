package com.testAssignment.sqlParser.parser;

import com.testAssignment.sqlParser.ast.AliasedExpression;
import com.testAssignment.sqlParser.ast.Expression;
import com.testAssignment.sqlParser.tokenizer.TokenKind;

import java.util.ArrayList;
import java.util.List;

final class ExpressionParser extends AbstractQueryPartParser
{
    public ExpressionParser(Parser parent) {
        super(parent);
    }

    public List<Expression> parseExpressionList(boolean aliasesAllowed) {
        var expressionList = new ArrayList<Expression>();

        while (true) {
            var expression = this.parseExpression();
            if (expression == null) {
                return null;
            }

            if (aliasesAllowed && this.tokenIs(TokenKind.KW_AS)) {
                var alias = this.parseAlias();

                if (alias != null) {
                    expression = new AliasedExpression(expression, alias);
                }
            }

            expressionList.add(expression);

            if (!this.tokenIs(TokenKind.COMMA)) {
                return expressionList;
            }
            this.eatToken();
        }
    }

    public Expression parseExpression() {
        var expression = this.parseConstant();

        if (expression == null) {
            expression = this.parseColumnReferenceOrFunctionCall();
        }

        if (expression == null) {
            return null;
        }

        if (this.getToken().getKind().isOperator()) {
            var operator = this.getToken().getKind().value;
            this.eatToken();

            var rightExpression = this.parseExpression();
            if (rightExpression == null) {
                return null;
            }

            // Cutting corners here. Just string representation is fine this task
            return new Expression(expression.value + ' ' + operator + ' ' + rightExpression.value);
        }

        return expression;
    }

    private String parseAlias() {
        if (!this.tokenIs(TokenKind.KW_AS)) {
            return null;
        }
        this.eatToken();

        if (this.tokenIs(TokenKind.IDENTIFIER)) {
            var alias = this.getToken().getValue();
            this.eatToken();
            return alias;
        }

        return null;
    }

    private Expression parseConstant() {
        if (this.tokenIs(TokenKind.INT)) {
            var value = this.getToken().getValue();
            this.eatToken();

            return new Expression(value);
        } else if (this.tokenIs(TokenKind.STRING)) {
            var value = this.getToken().getValue();
            this.eatToken();

            return new Expression('\'' + value + '\'');
        }

        return null;
    }

    private Expression parseColumnReferenceOrFunctionCall() {
        if (this.tokenIs(TokenKind.ASTERISK)) {
            this.eatToken();

            return new Expression("*");
        }

        if (this.tokenIs(TokenKind.IDENTIFIER)) {
            var value = this.getToken().getValue();
            this.eatToken();

            if (this.tokenIs(TokenKind.DOT)) {
                this.eatToken();

                var column = this.parseTableColumnName();
                if (column == null) {
                    return null;
                }

                return Expression.fromTableColumn(value, column);
            } else if (this.tokenIs(TokenKind.PAR_OPEN)) {
                var arguments = this.parseFunctionArguments();
                if (arguments == null) {
                    return null;
                }

                return Expression.fromFunctionCall(value, arguments);
            }

            return new Expression(value);
        }

        return null;
    }

    private String parseTableColumnName() {
        if (this.tokenIs(TokenKind.IDENTIFIER)) {
            var value = this.getToken().getValue();
            this.eatToken();
            return value;
        }

        return null;
    }

    private List<Expression> parseFunctionArguments() {
        if (!this.tokenIs(TokenKind.PAR_OPEN)) {
            return null;
        }
        this.eatToken();

        var arguments = this.parseExpressionList(false);

        if (!this.tokenIs(TokenKind.PAR_CLOSE)) {
            return null;
        }
        this.eatToken();

        return arguments;
    }
}
