package com.testAssignment.sqlParser.tokenizer;

import java.util.*;

public class Tokenizer {
    private static final String WHITESPACES = " \t\r\n";
    private static final Map<String, TokenKind> TOKEN_KINDS;

    private String source;
    private int position;

    static {
        Map<String, TokenKind> tempTokenKinds = new HashMap<>();
        for (TokenKind kind : TokenKind.values()) {
            if (kind.value != null) {
                tempTokenKinds.put(kind.value, kind);
            }
        }
        TOKEN_KINDS = tempTokenKinds;
    }

    public List<Token> tokenize(String source) {
        this.source = source;
        this.position = 0;
        List<Token> tokens = new ArrayList<>();

        Token token;

        do {
            token = this.getToken();
            if (token == null) {
                return null;
            }
            tokens.add(token);
        } while (token.getKind() != TokenKind.EOF);

        return tokens;
    }

    private char getChar()
    {
        return this.source.charAt(this.position);
    }

    private void eatChar()
    {
        this.position++;
    }

    private Token getToken() {
        this.consumeWhitespaces();

        if (this.position >= this.source.length()) {
            return new Token(TokenKind.EOF);
        }

        var ch = this.getChar();

        if (ch >= '0' && ch <= '9') {
            return this.consumeInt();
        } else if (this.isFirstIdentifierChar(ch)) {
            return this.consumeIdentifier();
        } else if (ch == '\'') {
            return this.consumeString();
        } else {
            return this.consumeOther();
        }
    }

    private void consumeWhitespaces() {
        while (true) {
            if (this.position >= this.source.length()) {
                break;
            }

            var currentChar = this.getChar();
            if (WHITESPACES.indexOf(currentChar) != -1) {
                eatChar();
            } else {
                break;
            }
        }
    }

    private Token consumeInt() {
        var value = new StringBuilder();

        while (true) {
            var ch = this.getChar();

            if (ch >= '0' && ch <= '9') {
                value.append(ch);
                this.eatChar();
            } else {
                break;
            }
        }

        return new Token(TokenKind.INT, value.toString());
    }

    private Token consumeIdentifier() {
        var valueBuilder = new StringBuilder();

        while (true) {
            if (this.position >= this.source.length()) {
                break;
            }

            var ch = this.getChar();
            if (this.isIdentifierChar(ch)) {
                valueBuilder.append(ch);
                this.eatChar();
            } else {
                break;
            }
        }

        var value = valueBuilder.toString().toLowerCase();

        if (TOKEN_KINDS.containsKey(value)) {
            return new Token(TOKEN_KINDS.get(value));
        } else {
            return new Token(TokenKind.IDENTIFIER, value);
        }
    }

    private boolean isFirstIdentifierChar(char ch) {
        var ch_lower = Character.toLowerCase(ch);
        return ch == '_'
                || ch_lower >= 'a' && ch_lower <= 'z';
    }

    private boolean isIdentifierChar(char ch) {
        return this.isFirstIdentifierChar(ch)
                || ch >= '0' && ch <= '9';
    }

    private Token consumeString() {
        var valueBuilder = new StringBuilder();
        char ch;

        eatChar();
        ch = this.getChar();

        do {
            valueBuilder.append(ch);
            eatChar();
            ch = this.getChar();
        } while (ch != '\'');

        eatChar();

        return new Token(TokenKind.STRING, valueBuilder.toString());
    }

    private Token consumeOther() {
        var valueBuilder = new StringBuilder();
        TokenKind found = null;

        while (true) {
            var ch = this.getChar();
            valueBuilder.append(ch);

            var value = valueBuilder.toString();
            var similar = this.getSimilarTokens(value);

            if (similar.contains(value)) {
                found = TOKEN_KINDS.get(value);
            }

            if (similar.size() == 1) {
                this.eatChar();
                return new Token(found);
            } else if (similar.isEmpty()) {
                if (found == null) {
                    return null;
                }
                return new Token(found);
            }

            this.eatChar();
        }
    }

    private Set<String> getSimilarTokens(String value)
    {
        Set<String> found = new HashSet<>();

        for (Map.Entry<String, TokenKind> entry : TOKEN_KINDS.entrySet()) {
            String key = entry.getKey();

            if (key.startsWith(value)) {
                found.add(entry.getKey());
            }
        }

        return found;
    }
}
