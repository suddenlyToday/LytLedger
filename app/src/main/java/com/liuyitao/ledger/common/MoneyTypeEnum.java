package com.liuyitao.ledger.common;

import com.liuyitao.ledger.R;

public enum MoneyTypeEnum {
    INCOME{
        @Override
        public int getColorResId() {
            return R.color.qmui_config_color_red;
        }

        @Override
        public String getPrefix() {
            return "+";
        }
    },
    PAYOUT{
        @Override
        public int getColorResId() {
            return R.color.qmui_config_color_blue;
        }

        @Override
        public String getPrefix() {
            return "-";
        }
    };

    public abstract int getColorResId();
    public abstract String getPrefix();
}
