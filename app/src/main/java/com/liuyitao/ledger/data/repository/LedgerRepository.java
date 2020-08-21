package com.liuyitao.ledger.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.liuyitao.ledger.common.Constants;
import com.liuyitao.ledger.common.MoneyTypeEnum;
import com.liuyitao.ledger.data.dao.LedgerDao;
import com.liuyitao.ledger.data.database.AppDatabase;
import com.liuyitao.ledger.data.entity.LedgerRecord;

import java.util.Date;

public class LedgerRepository {
    private LedgerDao ledgerDao;

    private PagedList.Config myPagingConfig = new PagedList.Config.Builder()
            .setPageSize(50)
            .setPrefetchDistance(100)
            .setEnablePlaceholders(true)
            .build();
    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    public LedgerRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        ledgerDao = db.userDao();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<PagedList<LedgerRecord>> getAllLedgers() {
        return new LivePagedListBuilder<>(this.ledgerDao.getAll(), myPagingConfig).build();
    }

    public LiveData<Double> getMoneyCount(Date begin, Date end, MoneyTypeEnum typeEnum) {
        return ledgerDao.getMoneyCount(begin, end, typeEnum);
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    public void insert(LedgerRecord ledgerRecord) {
        AppDatabase.dbWriteExecutor.submit(() -> {
            ledgerRecord.addDate = new Date();
            ledgerDao.insert(ledgerRecord);
        });
    }

    public void update(LedgerRecord ledgerRecord) {
        AppDatabase.dbWriteExecutor.submit(()->{
            ledgerRecord.modifyDate = new Date();
            ledgerDao.insert(ledgerRecord);
        });
    }

    public void delete(LedgerRecord ledgerRecord) {
        AppDatabase.dbWriteExecutor.submit(() -> {
            ledgerRecord.isDeleted = Constants.IS_DELETED_TRUE;
            ledgerRecord.deleteDate = new Date();
            ledgerDao.delete(ledgerRecord);
        });
    }
}
