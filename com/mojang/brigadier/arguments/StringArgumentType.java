package com.mojang.brigadier.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Arrays;
import java.util.Collection;

public class StringArgumentType implements ArgumentType {

    private final StringArgumentType.StringType type;

    private StringArgumentType(StringArgumentType.StringType type) {
        this.type = type;
    }

    public static StringArgumentType word() {
        return new StringArgumentType(StringArgumentType.StringType.SINGLE_WORD);
    }

    public static StringArgumentType string() {
        return new StringArgumentType(StringArgumentType.StringType.QUOTABLE_PHRASE);
    }

    public static StringArgumentType greedyString() {
        return new StringArgumentType(StringArgumentType.StringType.GREEDY_PHRASE);
    }

    public static String getString(CommandContext context, String name) {
        return (String) context.getArgument(name, String.class);
    }

    public StringArgumentType.StringType getType() {
        return this.type;
    }

    public String parse(StringReader reader) throws CommandSyntaxException {
        if (this.type == StringArgumentType.StringType.GREEDY_PHRASE) {
            String text = reader.getRemaining();

            reader.setCursor(reader.getTotalLength());
            return text;
        } else {
            return this.type == StringArgumentType.StringType.SINGLE_WORD ? reader.readUnquotedString() : reader.readString();
        }
    }

    public String toString() {
        return "string()";
    }

    public Collection getExamples() {
        return this.type.getExamples();
    }

    public static String escapeIfRequired(String input) {
        char[] achar = input.toCharArray();
        int i = achar.length;

        for (int j = 0; j < i; ++j) {
            char c = achar[j];

            if (!StringReader.isAllowedInUnquotedString(c)) {
                return escape(input);
            }
        }

        return input;
    }

    private static String escape(String input) {
        StringBuilder result = new StringBuilder("\"");

        for (int i = 0; i < input.length(); ++i) {
            char c = input.charAt(i);

            if (c == 92 || c == 34) {
                result.append('\\');
            }

            result.append(c);
        }

        result.append("\"");
        return result.toString();
    }

    public static enum StringType {

        SINGLE_WORD(new String[] { "word", "words_with_underscores"}), QUOTABLE_PHRASE(new String[] { "\"quoted phrase\"", "word", "\"\""}), GREEDY_PHRASE(new String[] { "word", "words with spaces", "\"and symbols\""});

        private final Collection examples;

        private StringType(String... examples) {
            this.examples = Arrays.asList(examples);
        }

        public Collection getExamples() {
            return this.examples;
        }
    }
}
