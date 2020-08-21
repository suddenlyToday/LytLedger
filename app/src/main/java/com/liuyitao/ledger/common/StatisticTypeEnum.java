package com.liuyitao.ledger.common;

import java.util.Optional;

public enum StatisticTypeEnum {
    DAY(0),
    MONTH(1),
    YEAR(2);

    static StatisticTypeEnum[] arr = new StatisticTypeEnum[]{DAY, MONTH, YEAR};

    private final int value;

    StatisticTypeEnum(int value) {
        this.value = value;
    }

    public static Optional<StatisticTypeEnum> forValue(int value) {
        if (value > YEAR.value || value < DAY.value) {
            return Optional.empty();
        }
        return Optional.of(arr[value]);
    }

    public int getValue() {
        return value;
    }
}
