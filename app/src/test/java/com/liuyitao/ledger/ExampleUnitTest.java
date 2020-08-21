package com.liuyitao.ledger;

import org.junit.Test;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    DateFormat dateFormat = DateFormat.getDateTimeInstance();
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
        System.out.println(new Date().getTime());
        System.out.println(dateFormat.format(getDayBegin()));
        System.out.println(dateFormat.format(getDayEnd()));

        System.out.println(dateFormat.format(getMonthBegin()));
        System.out.println(dateFormat.format(getMonthEnd()));

        System.out.println(dateFormat.format(getYearBegin()));
        System.out.println(dateFormat.format(getYearEnd()));

        System.out.println(dateFormat.format(getDate(113)));
    }


    private Date getDate(int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -i);
        return calendar.getTime();
    }

    private Date getDayBegin() {
        Calendar calendar = Calendar.getInstance();
        clearTimeFields(calendar);
        return calendar.getTime();
    }

    private Date getDayEnd() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        clearTimeFields(calendar);
        return calendar.getTime();
    }

    private void clearTimeFields(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    private Date getMonthBegin() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        clearTimeFields(calendar);
        return calendar.getTime();
    }

    private Date getMonthEnd() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONDAY, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        clearTimeFields(calendar);
        return calendar.getTime();
    }

    private Date getYearBegin() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        clearTimeFields(calendar);
        return calendar.getTime();
    }

    private Date getYearEnd() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 1);
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        clearTimeFields(calendar);
        return calendar.getTime();
    }
}