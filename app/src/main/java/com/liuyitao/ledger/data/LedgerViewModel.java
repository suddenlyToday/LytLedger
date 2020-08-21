package com.liuyitao.ledger.data;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;

import com.liuyitao.ledger.common.MoneyTypeEnum;
import com.liuyitao.ledger.data.entity.LedgerRecord;
import com.liuyitao.ledger.data.repository.LedgerRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class LedgerViewModel extends AndroidViewModel {
    private LedgerRepository mRepository;

    public LedgerViewModel(Application application) {
        super(application);
        mRepository = new LedgerRepository(application);
    }

    public LiveData<PagedList<LedgerRecord>> getAllLedgers() {
        return mRepository.getAllLedgers();
    }

    public LiveData<Double> getDayMoneyCount(MoneyTypeEnum moneyType) {
        return mRepository.getMoneyCount(getDayBegin(), getDayEnd(), moneyType);
    }

    public LiveData<Double> getMonthMoneyCount(MoneyTypeEnum moneyType) {
        return mRepository.getMoneyCount(getMonthBegin(), getMonthEnd(), moneyType);
    }

    public LiveData<Double> getYearMoneyCount(MoneyTypeEnum moneyType) {
        return mRepository.getMoneyCount(getYearBegin(), getYearEnd(), moneyType);
    }

    public void upsert(LedgerRecord ledgerRecord) {
        if (Objects.isNull(ledgerRecord.addDate)) {
            mRepository.insert(ledgerRecord);
        } else {
            mRepository.update(ledgerRecord);
        }
    }

    public void delete(LedgerRecord ledgerRecord) {
        mRepository.delete(ledgerRecord);
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
