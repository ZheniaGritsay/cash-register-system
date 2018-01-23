package com.projects.controller.util;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

@RunWith(JUnitParamsRunner.class)
public class DateUtilTest {

    private Object[][] parametersForDateConforms() {
        return new Object[][]{{"01-23-2018 12:12", true}, {"", false}, {"01-02-2017", false}, {"01-02", false},
                {"01-02-20", false}, {"2018-02-23", false}, {"2018/02/23 14:05", false}, {"2018-02-23 13", false}};
    }

    @Test
    public void parseDate() {
        LocalDateTime ldt = DateUtil.parseDate("01-18-2018 23:00", DateUtil.MM_DD_YYYY_HH_MM_PATTERN);

        assertNotNull(ldt);
        assertEquals(18, ldt.getDayOfMonth());
        assertEquals(1, ldt.getMonthValue());
        assertEquals(2018, ldt.getYear());
        assertEquals(23, ldt.getHour());
        assertEquals(0, ldt.getMinute());
    }

    @Test
    @Parameters
    public void dateConforms(String date, boolean expected) {
        assertEquals(expected, DateUtil.dateConforms(date, DateUtil.MM_DD_YYYY_HH_MM_REGEX));
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerException() {
        DateUtil.dateConforms(null, null);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionNullDate() {
        DateUtil.dateConforms(null, DateUtil.MM_DD_YYYY_HH_MM_REGEX);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionNullRegex() {
        DateUtil.dateConforms("01-05-2006", null);
    }
}
