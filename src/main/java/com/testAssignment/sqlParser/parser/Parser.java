package com.testAssignment.sqlParser.parser;

import com.testAssignment.sqlParser.ast.Expression;
import com.testAssignment.sqlParser.ast.FromItem;
import com.testAssignment.sqlParser.ast.Select;
import com.testAssignment.sqlParser.tokenizer.TokenKind;
import com.testAssignment.sqlParser.tokenizer.Token;

import java.util.ArrayList;
import java.util.List;

public final class Parser {
    private List<Token> tokens;
    private int position;
    private final ExpressionParser expressionParser;
    private final FromItemsParser tablesListParser;

    public Parser() {
        this.expressionParser = new ExpressionParser(this);
        this.tablesListParser = new FromItemsParser(this, this.expressionParser);
    }

    public Select parse(List<Token> tokens) {
        this.tokens = tokens;
        this.position = 0;

        var select = this.parseSelect();

        if (select == null) {
            return null;
        }

        if (!this.tokenIs(TokenKind.EOF)) {
            return null;
        }

        return select;
    }

    Select parseSelect() {
        var fields = this.parsePartSelect();
        if (fields == null) {
            return null;
        }

        var from = this.parsePartFrom();
        if (from == null) {
            return null;
        }

        var where = this.parsePartWhere();

        var groupBy = this.parsePartGroupBy();
        if (groupBy == null) {
            return null;
        }

        var having = this.parsePartHaving();
        if (having == null) {
            return null;
        }

        var orderBy = this.parsePartOrderBy();
        if (orderBy == null) {
            return null;
        }

        var limit = this.parsePartLimit();
        var offset = this.parsePartOffset();

        return new Select(
            fields,
            from,
            where,
            groupBy,
            having,
            orderBy,
            limit,
            offset
        );
    }

    private List<Expression> parsePartSelect() {
        if (this.tokenIs(TokenKind.KW_SELECT)) {
            this.eatToken();
        } else {
            return null;
        }

        return this.expressionParser.parseExpressionList(true);
    }

    private List<FromItem> parsePartFrom() {
        if (!this.tokenIs(TokenKind.KW_FROM)) {
            return null;
        }
        this.eatToken();

        return this.tablesListParser.parseFromItems();
    }

    private Expression parsePartWhere() {
        if (!this.tokenIs(TokenKind.KW_WHERE)) {
            return null;
        }
        this.eatToken();

        return this.expressionParser.parseExpression();
    }

    private List<Expression> parsePartGroupBy() {
        if (!this.tokenIs(TokenKind.KW_GROUP)) {
            return new ArrayList<>();
        }
        this.eatToken();

        if (!this.tokenIs(TokenKind.KW_BY)) {
            return null;
        }
        this.eatToken();

        return this.expressionParser.parseExpressionList(false);
    }

    private Expression parsePartHaving() {
        if (!this.tokenIs(TokenKind.KW_HAVING)) {
            return new Expression("true");
        }
        this.eatToken();

        return this.expressionParser.parseExpression();
    }

    private List<Expression> parsePartOrderBy() {
        if (!this.tokenIs(TokenKind.KW_ORDER)) {
            return new ArrayList<>();
        }
        this.eatToken();

        if (!this.tokenIs(TokenKind.KW_BY)) {
            return null;
        }
        this.eatToken();

        return this.expressionParser.parseExpressionList(false);
    }

    private Integer parsePartLimit() {
        if (!this.tokenIs(TokenKind.KW_LIMIT)) {
            return null;
        }
        this.eatToken();

        if (!this.tokenIs(TokenKind.INT)) {
            return null;
        }
        var value = Integer.parseInt(this.getToken().getValue());
        this.eatToken();

        return value;
    }

    private Integer parsePartOffset() {
        if (!this.tokenIs(TokenKind.KW_OFFSET)) {
            return null;
        }
        this.eatToken();

        if (!this.tokenIs(TokenKind.INT)) {
            return null;
        }
        var value = Integer.parseInt(this.getToken().getValue());
        this.eatToken();

        return value;
    }

    boolean tokenIs(TokenKind kind) {
        return this.getToken().getKind() == kind;
    }

    Token getToken() {
        return this.tokens.get(this.position);
    }

    void eatToken() {
        this.position++;
    }
}
