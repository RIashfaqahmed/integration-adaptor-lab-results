package uk.nhs.digital.nhsconnect.lab.results.model.edifact.message;

import java.util.ArrayList;
import java.util.List;

public final class Split {
    private static final char ESC_CHAR = '?';
    private static final char SEGMENT_TERMINATOR = '\'';
    private static final char FIELD_TERMINATOR = '+';
    private static final char SUB_FIELD_TERMINATOR = ':';

    private Split() { }

    public static String[] bySegmentTerminator(String input) {
        return splitString(input, SEGMENT_TERMINATOR);
    }

    public static String[] byPlus(final String input) {
        return splitString(input, FIELD_TERMINATOR);
    }

    public static String[] byColon(final String input) {
        return splitString(input, SUB_FIELD_TERMINATOR);
    }

    private static String[] splitString(final String input, final char separator) {
        List<String> parts = new ArrayList<>();
        StringBuilder sb = new StringBuilder();

        int adjacentEscCharsCount = 0;
        char[] charArray = input.toCharArray();
        for (char c : charArray) {
            if (c == ESC_CHAR) {
                adjacentEscCharsCount++;
            } else if (c == separator && adjacentEscCharsCount % 2 == 0) {
                adjacentEscCharsCount = 0;
                parts.add(sb.toString());
                sb.setLength(0);
                continue;
            } else {
                adjacentEscCharsCount = 0;
            }

            sb.append(c);
        }
        parts.add(sb.toString());

        return parts.toArray(String[]::new);
    }
}
