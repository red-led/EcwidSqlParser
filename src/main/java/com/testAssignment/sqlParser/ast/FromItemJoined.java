package com.testAssignment.sqlParser.ast;

import java.util.List;

public class FromItemJoined implements FromItem {
    public final FromItem fromItem;
    public final List<JoinItem> joinItems;

    public FromItemJoined(FromItem fromItem, List<JoinItem> joinedTables) {
        this.fromItem = fromItem;
        this.joinItems = joinedTables;
    }

    @Override
    public String toString() {
        return "FromItemJoined{" +
                "table=" + fromItem +
                ", join=" + joinItems +
                '}';
    }
}
