package com.testAssignment.sqlParser.tokenizer;

import java.util.List;

public enum TokenKind {
    KW_SELECT("select"),
    KW_FROM("from"),
    KW_WHERE("where"),
    KW_AS("as"),
    KW_INNER("inner"),
    KW_OUTER("outer"),
    KW_LEFT("left"),
    KW_RIGHT("right"),
    KW_FULL("full"),
    KW_JOIN("join"),
    KW_ON("on"),
    KW_GROUP("group"),
    KW_BY("by"),
    KW_HAVING("having"),
    KW_ORDER("order"),
    KW_LIMIT("limit"),
    KW_OFFSET("offset"),

    PAR_OPEN("("),
    PAR_CLOSE(")"),

    OP_GREATER(">"),
    OP_GREATER_OR_EQUALS(">="),
    OP_LESS("<"),
    OP_LESS_OR_EQUALS("<="),
    OP_EQUALS("="),
    OP_AND("and"),
    OP_OR("or"),

    COMMA(","),
    DOT("."),
    ASTERISK("*"),

    IDENTIFIER,
    STRING,
    INT,

    EOF;

    private static final List<TokenKind> OPERATORS = List.of(
        TokenKind.OP_EQUALS,
        TokenKind.OP_GREATER,
        TokenKind.OP_GREATER_OR_EQUALS,
        TokenKind.OP_LESS,
        TokenKind.OP_LESS_OR_EQUALS,
        TokenKind.OP_AND,
        TokenKind.OP_OR
    );

    public final String value;
    public final Integer precedence;

    TokenKind(String value) {
        this.value = value;
        this.precedence = null;
    }

    TokenKind() {
        this.value = null;
        this.precedence = null;
    }

    public boolean isOperator() {
        return OPERATORS.contains(this);
    }
}
