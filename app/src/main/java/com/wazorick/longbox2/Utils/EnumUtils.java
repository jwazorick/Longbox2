package com.example.longbox2.Utils;

import com.example.longbox2.Enums.Conditions;
import com.example.longbox2.Enums.PublicationFormat;

import java.util.ArrayList;
import java.util.List;

public class EnumUtils {
    public static List<String> getAllConditions() {
        List<String> conditions = new ArrayList<>();
        conditions.add(Conditions.NEAR_MINT.toString());
        conditions.add(Conditions.VERY_FINE.toString());
        conditions.add(Conditions.FINE.toString());
        conditions.add(Conditions.VERY_GOOD.toString());
        conditions.add(Conditions.GOOD.toString());
        conditions.add(Conditions.FAIR.toString());
        conditions.add(Conditions.POOR.toString());
        return conditions;
    }

    public static List<String> getAllFormats() {
        List<String> formats = new ArrayList<>();
        formats.add(PublicationFormat.SINGLE_ISSUE.toString());
        formats.add(PublicationFormat.HARDCOVER.toString());
        formats.add(PublicationFormat.SOFTCOVER.toString());
        formats.add(PublicationFormat.DIGITAL.toString());
        return formats;
    }

    public static PublicationFormat getFormatFromString(String format) {
        if(format.equalsIgnoreCase(PublicationFormat.SINGLE_ISSUE.toString())) {
            return PublicationFormat.SINGLE_ISSUE;
        } else if(format.equalsIgnoreCase(PublicationFormat.HARDCOVER.toString())) {
            return PublicationFormat.HARDCOVER;
        } else if(format.equalsIgnoreCase(PublicationFormat.SOFTCOVER.toString())) {
            return PublicationFormat.SOFTCOVER;
        } else {
            return PublicationFormat.DIGITAL;
        }
    }
}
