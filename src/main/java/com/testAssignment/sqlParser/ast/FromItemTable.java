package com.testAssignment.sqlParser.ast;

public class FromItemTable implements FromItem {
    public final String value;

    public FromItemTable(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "FromItemTable{" +  value + '}';
    }
}
