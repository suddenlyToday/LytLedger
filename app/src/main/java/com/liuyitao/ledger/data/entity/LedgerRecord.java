package com.liuyitao.ledger.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.liuyitao.ledger.common.Constants;
import com.liuyitao.ledger.common.MoneyTypeEnum;

import java.util.Date;

@Entity(indices = {@Index("addDate"), @Index("tag"), @Index({"type","addDate"})})
public class LedgerRecord {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "money")
    public Double money;

    // 0=in,2=out
    @ColumnInfo(name = "type")
    public MoneyTypeEnum type;

    // 0=notdeleted 1=deleted
    @ColumnInfo(name = "deleted", defaultValue = Constants.IS_DELETED_FALSE + "")
    public Integer isDeleted;

    @ColumnInfo(name = "addDate")
    public Date addDate;

    @ColumnInfo(name = "modifyDate")
    public Date modifyDate;

    @ColumnInfo(name = "deleteDate")
    public Date deleteDate;

    @ColumnInfo(name = "desc")
    public String desc;

    @ColumnInfo(name = "tag")
    public String tag;


    public static final class Builder {
        private int uid;
        private Double money;
        // 0=in,2=out
        private MoneyTypeEnum type;
        private Integer isDeleted;
        private Date addDate;
        private Date modifyDate;
        private Date deleteDate;
        private String desc;
        private String tag;

        private Builder() {
        }

        public static Builder aLedgerRecord() {
            return new Builder();
        }

        public Builder uid(int uid) {
            this.uid = uid;
            return this;
        }

        public Builder money(Double money) {
            this.money = money;
            return this;
        }

        public Builder type(MoneyTypeEnum type) {
            this.type = type;
            return this;
        }

        public Builder isDeleted(Integer isDeleted) {
            this.isDeleted = isDeleted;
            return this;
        }

        public Builder addDate(Date addDate) {
            this.addDate = addDate;
            return this;
        }

        public Builder modifyDate(Date modifyDate) {
            this.modifyDate = modifyDate;
            return this;
        }

        public Builder deleteDate(Date deleteDate) {
            this.deleteDate = deleteDate;
            return this;
        }

        public Builder desc(String desc) {
            this.desc = desc;
            return this;
        }

        public Builder tag(String tag) {
            this.tag = tag;
            return this;
        }

        public LedgerRecord build() {
            LedgerRecord ledgerRecord = new LedgerRecord();
            ledgerRecord.type = this.type;
            ledgerRecord.desc = this.desc;
            ledgerRecord.uid = this.uid;
            ledgerRecord.addDate = this.addDate;
            ledgerRecord.money = this.money;
            ledgerRecord.isDeleted = this.isDeleted;
            ledgerRecord.modifyDate = this.modifyDate;
            ledgerRecord.deleteDate = this.deleteDate;
            ledgerRecord.tag = this.tag;
            return ledgerRecord;
        }
    }
}
