package com.moandjiezana.toml;

import java.util.concurrent.atomic.AtomicInteger;

class ValueReaders {

    static final ValueReaders VALUE_READERS = new ValueReaders();
    private static final ValueReader[] READERS = new ValueReader[] { MultilineStringValueReader.MULTILINE_STRING_VALUE_READER, MultilineLiteralStringValueReader.MULTILINE_LITERAL_STRING_VALUE_READER, LiteralStringValueReader.LITERAL_STRING_VALUE_READER, StringValueReaderWriter.STRING_VALUE_READER_WRITER, DateValueReaderWriter.DATE_VALUE_READER_WRITER, NumberValueReaderWriter.NUMBER_VALUE_READER_WRITER, BooleanValueReaderWriter.BOOLEAN_VALUE_READER_WRITER, ArrayValueReader.ARRAY_VALUE_READER, InlineTableValueReader.INLINE_TABLE_VALUE_READER};

    Object convert(String value, AtomicInteger index, Context context) {
        String substring = value.substring(index.get());
        ValueReader[] errors = ValueReaders.READERS;
        int i = errors.length;

        for (int j = 0; j < i; ++j) {
            ValueReader valueParser = errors[j];

            if (valueParser.canRead(substring)) {
                return valueParser.read(value, index, context);
            }
        }

        Results.Errors results_errors = new Results.Errors();

        results_errors.invalidValue(context.identifier.getName(), substring, context.line.get());
        return results_errors;
    }
}
