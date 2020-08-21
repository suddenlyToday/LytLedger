package com.liuyitao.ledger.common;

import android.util.Log;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Objects;

import static com.liuyitao.ledger.common.Constants.EMPTY;

public final class Utils {
    private static final NumberFormat DECIMAL_FORMAT = new DecimalFormat("#.###");

    private Utils(){}

    public static void infoLog(String s) {
        Log.i(Constants.APPNAME, s);
    }

    public static String fmtMoney(Double money) {
        if (Objects.isNull(money)) {
            return EMPTY;
        }
        return DECIMAL_FORMAT.format(money);
    }
}
