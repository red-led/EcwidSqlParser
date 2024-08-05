package com.testAssignment.sqlParser.ast;

public class JoinItem {
    public final FromItem fromItem;
    public final JoinType joinType;
    public final Expression on;

    public JoinItem(FromItem joinItem, JoinType joinType) {
        this.fromItem = joinItem;
        this.joinType = joinType;
        this.on = null;
    }

    public JoinItem(FromItem joinItem, JoinType joinType, Expression on) {
        this.fromItem = joinItem;
        this.joinType = joinType;
        this.on = on;
    }

    @Override
    public String toString() {
        return "JoinItem{" +
                "from=" + fromItem +
                ", joinType='" + joinType + '\'' +
                ", on=" + on +
                '}';
    }
}
