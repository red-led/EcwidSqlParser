package com.testAssignment.sqlParser.ast;

public class FromItemQuery implements FromItem {
    public final Select query;

    public FromItemQuery(Select query) {
        this.query = query;
    }

    @Override
    public String toString() {
        return "FromItemQuery{" +
                "query=" + query +
                '}';
    }
}
