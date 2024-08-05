package com.testAssignment.sqlParser.parser;

import com.testAssignment.sqlParser.ast.*;
import com.testAssignment.sqlParser.tokenizer.TokenKind;

import java.util.ArrayList;
import java.util.List;

final class FromItemsParser extends AbstractQueryPartParser {
    private final ExpressionParser expressionParser;

    public FromItemsParser(Parser parent, ExpressionParser expressionParser) {
        super(parent);
        this.expressionParser = expressionParser;
    }

    public List<FromItem> parseFromItems() {
        var fromItemsList = new ArrayList<FromItem>();

        while (true) {
            var fromItem = this.parseFromItem();
            if (fromItem == null) {
                return null;
            }

            var joins = this.parseJoinList();
            if (!joins.isEmpty()) {
                fromItem = new FromItemJoined(fromItem, joins);
            }
            fromItemsList.add(fromItem);

            if (!this.tokenIs(TokenKind.COMMA)) {
                return fromItemsList;
            }
            this.eatToken();
        }
    }

    private FromItem parseFromItem() {
        if (this.tokenIs(TokenKind.IDENTIFIER)) {
            var value = this.getToken().getValue();
            this.eatToken();

            return new FromItemTable(value);
        } else if (this.tokenIs(TokenKind.PAR_OPEN)) {
            eatToken();

            var subSelect = this.parent.parseSelect();

            if (subSelect == null) {
                return null;
            }

            if (!this.tokenIs(TokenKind.PAR_CLOSE)) {
                return null;
            }
            this.eatToken();

            return new FromItemQuery(subSelect);
        }
        return null;
    }

    private ArrayList<JoinItem> parseJoinList() {
        var joins = new ArrayList<JoinItem>();

        while (true) {
            var join = this.parseJoin();
            if (join == null) {
                break;
            }
            joins.add(join);
        }

        return joins;
    }

    private JoinItem parseJoin() {
        var joinType = this.parseJoinType();
        var joinItem = this.parseFromItem();

        if (joinItem == null) {
            return null;
        }

        if (!this.tokenIs(TokenKind.KW_ON)) {
            return new JoinItem(joinItem, joinType);
        }
        this.eatToken();

        var onExpression = this.expressionParser.parseExpression();
        if (onExpression == null) {
            return null;
        }

        return new JoinItem(joinItem, joinType, onExpression);
    }

    private JoinType parseJoinType() {
        JoinType joinType;

        if (this.tokenIs(TokenKind.KW_JOIN)) {
            this.eatToken();
            return JoinType.INNER;
        } else if (this.tokenIs(TokenKind.KW_INNER)) {
            this.eatToken();

            if (!this.tokenIs(TokenKind.KW_JOIN)) {
                return null;
            }
            this.eatToken();

            return JoinType.INNER;
        } else if ((joinType = this.getOuterJoinType()) != null) {
            this.eatToken();

            if (this.tokenIs(TokenKind.KW_OUTER)) {
                this.eatToken();
            }

            if (!this.tokenIs(TokenKind.KW_JOIN)) {
                return null;
            }
            this.eatToken();

            return joinType;
        } else {
            return null;
        }
    }

    private JoinType getOuterJoinType() {
        return switch (this.getToken().getKind()) {
            case TokenKind.KW_LEFT -> JoinType.LEFT;
            case TokenKind.KW_RIGHT -> JoinType.RIGHT;
            case TokenKind.KW_FULL -> JoinType.FULL;
            default -> null;
        };
    }
}
