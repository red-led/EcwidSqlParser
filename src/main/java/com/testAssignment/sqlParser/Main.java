package com.testAssignment.sqlParser;

import com.testAssignment.sqlParser.ast.Select;
import com.testAssignment.sqlParser.parser.Parser;
import com.testAssignment.sqlParser.tokenizer.Token;
import com.testAssignment.sqlParser.tokenizer.Tokenizer;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Tokenizer t = new Tokenizer();
        Parser p = new Parser();

        String example = """
                sElEct
                    a,b,
                    'string literal' as this_is_my_alias,
                    count(*),
                    function(with, 'list', of, 5, arguments)
                from
                    my_table,
                    (select all from my_second_table)
                    left join (select * from other_table) on b = c
                where
                    length(a) > 0
                group by
                     a, b
                having
                    COUNT(*) > 1 AND SUM(book.cost) > 500
                order by
                    a, b
                limit
                    10
                offset
                    5
                """;

        List<Token> tokens = t.tokenize(example);
        if (tokens == null) {
            System.out.println("Tokenizer was unable to comprehend given query");
            return;
        }

        Select result = p.parse(tokens);
        if (result == null) {
            System.out.println("Parser was unable to comprehend given query");
            return;
        }

        System.out.println("Result is: " + result);
    }
}