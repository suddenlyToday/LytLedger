package com.liuyitao.ledger.data.dao;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.liuyitao.ledger.common.MoneyTypeEnum;
import com.liuyitao.ledger.data.entity.LedgerRecord;

import java.util.Date;


@Dao
public interface LedgerDao {
    @Query("SELECT * FROM LedgerRecord order by addDate desc")
    DataSource.Factory<Integer,LedgerRecord> getAll();

    @Query("SELECT SUM(money) from LedgerRecord where type = :type and addDate >= :begin and addDate < :end")
    LiveData<Double> getMoneyCount(Date begin, Date end, MoneyTypeEnum type);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(LedgerRecord ledgerRecord);

    @Delete
    void delete(LedgerRecord ledgerRecord);
}
