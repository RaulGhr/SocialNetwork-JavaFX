package com.example.socialnetworkfx.formats;

import java.time.format.DateTimeFormatter;

public class TimeFormat {
    public static DateTimeFormatter dateFormatter() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }
}
