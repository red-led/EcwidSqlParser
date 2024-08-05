package com.testAssignment.sqlParser.parser;

import com.testAssignment.sqlParser.tokenizer.TokenKind;
import com.testAssignment.sqlParser.tokenizer.Token;

abstract class AbstractQueryPartParser {
    protected final Parser parent;

    public AbstractQueryPartParser(Parser parent) {
        this.parent = parent;
    }

    protected Token getToken() {
        return parent.getToken();
    }

    protected boolean tokenIs(TokenKind kind) {
        return parent.tokenIs(kind);
    }

    protected void eatToken() {
        parent.eatToken();
    }
}
