package com.liuyitao.ledger.data;

import android.util.Log;

import androidx.room.TypeConverter;

import com.liuyitao.ledger.common.MoneyTypeEnum;

import java.util.Date;
import java.util.Objects;

import static com.liuyitao.ledger.common.Utils.infoLog;

public class Converters {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static Integer moneyTypeEnumToInteger(MoneyTypeEnum value) {
        if (Objects.equals(value, MoneyTypeEnum.INCOME)) {
            infoLog(value.toString() + ",return 0");
            return 0;
        } else if (Objects.equals(value, MoneyTypeEnum.PAYOUT)) {
            infoLog(value.toString() + ",return 1");

            return 1;
        } else {
            Log.e("converter", "error money type :" + value);
            return null;
        }
    }

    @TypeConverter
    public static MoneyTypeEnum fromMoneyTypeEnum(Integer value) {
        if (Objects.equals(value, 1)) {
            infoLog(value.toString() + ",return payout");
            return MoneyTypeEnum.PAYOUT;
        } else if (Objects.equals(value, 0)) {
            infoLog(value.toString() + ",return income");
            return MoneyTypeEnum.INCOME;
        } else {
            Log.e("converter", "error integer type :" + value);
            return null;
        }
    }
}

