package com.testAssignment.sqlParser.ast;

import java.util.List;

public class Select {
    public final List<Expression> columns;
    public final List<FromItem> from;
    public final Expression where;
    public final List<Expression> groupBy;
    public final Expression having;
    public final List<Expression> orderBy;
    public final Integer limit;
    public final Integer offset;

    public Select(
        List<Expression> columns,
        List<FromItem> from,
        Expression where,
        List<Expression> groupBy,
        Expression having,
        List<Expression> orderBy,
        Integer limit,
        Integer offset
    ) {
        this.columns = columns;
        this.from = from;
        this.where = where;
        this.groupBy = groupBy;
        this.having = having;
        this.orderBy = orderBy;
        this.limit = limit;
        this.offset = offset;
    }

    @Override
    public String toString() {
        return "Select {\n" +
                "  columns=" + columns + "\n" +
                "  from=" + from + "\n" +
                "  where=" + where + "\n" +
                "  groupBy=" + groupBy + "\n" +
                "  having=" + having + "\n" +
                "  orderBy=" + orderBy + "\n" +
                "  limit=" + limit + "\n" +
                "  offset=" + offset + "\n" +
                '}';
    }
}
