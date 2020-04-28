package com.formindev.meetroom.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

public class DateUtils {
    public static final int DAYS_OF_WEEK = 7;
    public static LocalDateTime currentMonday;

    static {
        currentMonday = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS)
            .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    }

    public static final String[] hoursOfDay = new String[] {
            "00:00", "01:00", "02:00", "03:00", "04:00",
            "05:00", "06:00", "07:00", "08:00", "09:00",
            "10:00", "11:00", "12:00", "13:00", "14:00",
            "15:00", "16:00", "17:00", "18:00", "19:00",
            "20:00", "21:00", "22:00", "23:00", "24:00"
    };

    public static List<LocalDateTime> getDaysOfWeek() {
        List<LocalDateTime> currentWeek = new ArrayList<>();

        for (int i = 0; i < DAYS_OF_WEEK - 1; i++) {
            currentWeek.add(currentMonday.plusDays(i));
        }

        return currentWeek;
    }

    public static void setPrevWeek() {
        currentMonday = currentMonday.minusDays(DAYS_OF_WEEK);
    }

    public static void setNextWeek() {
        currentMonday = currentMonday.plusDays(DAYS_OF_WEEK);
    }
}
