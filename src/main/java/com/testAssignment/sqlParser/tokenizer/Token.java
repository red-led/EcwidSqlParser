package com.testAssignment.sqlParser.tokenizer;

public class Token {
    private final TokenKind kind;
    private final String value;

    public Token(TokenKind kind) {
        this.kind = kind;
        this.value = null;
    }

    public Token(TokenKind kind, String value) {
        this.kind = kind;
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public TokenKind getKind() {
        return this.kind;
    }

    @Override
    public String toString() {
        if (value != null) {
            return "Token{" +
                    kind +
                    ", value='" + value + '\'' +
                    '}';
        } else {
            return "Token{" + kind + '}';
        }
    }
}
