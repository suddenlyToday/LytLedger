package com.liuyitao.ledger;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.liuyitao.ledger.common.MoneyTypeEnum;
import com.liuyitao.ledger.common.StatisticTypeEnum;
import com.liuyitao.ledger.data.LedgerViewModel;
import com.liuyitao.ledger.data.entity.LedgerRecord;
import com.qmuiteam.qmui.arch.QMUIActivity;
import com.qmuiteam.qmui.arch.QMUISwipeBackActivityManager;
import com.qmuiteam.qmui.layout.QMUIButton;
import com.qmuiteam.qmui.layout.QMUIFrameLayout;
import com.qmuiteam.qmui.layout.QMUILinearLayout;
import com.qmuiteam.qmui.recyclerView.QMUIRVItemSwipeAction;
import com.qmuiteam.qmui.recyclerView.QMUISwipeAction;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIKeyboardHelper;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.pullLayout.QMUIPullLayout;
import com.qmuiteam.qmui.widget.textview.QMUISpanTouchFixTextView;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.text.Spanned.SPAN_INCLUSIVE_EXCLUSIVE;
import static com.liuyitao.ledger.common.Constants.EMPTY;
import static com.liuyitao.ledger.common.MoneyTypeEnum.INCOME;
import static com.liuyitao.ledger.common.MoneyTypeEnum.PAYOUT;
import static com.liuyitao.ledger.common.StatisticTypeEnum.DAY;
import static com.liuyitao.ledger.common.StatisticTypeEnum.MONTH;
import static com.liuyitao.ledger.common.StatisticTypeEnum.YEAR;
import static com.liuyitao.ledger.common.Utils.fmtMoney;

// done todo 按out或者in按钮能增加一个listview项，包括listView的删除和修改

// done todo 使用room存储账单数据
// id int
// money string
// type int 0=in 1=out
// isDelete int 0=false 1=true
// addDate long
// deleteDate long
// desc string
// tag string

// done todo 修改
// done todo 显示正确的当年当月当日统计信息
// todo 分页

// todo 显示正确的统计信息，主要是天月年的趋势图和饼图

// todo 美化in和out按钮

// todo 添加和设置tag的功能

// todo 皮肤管理

// todo 美化其他ui

// todo 增加自动llt测试

// todo 上线华为应用市场

// todo 使用jekins构造ci系统，规范化修改，使能代码可信
public class MainActivity extends QMUIActivity {
    @BindView(R.id.statisticsDiv)
    QMUILinearLayout statisticsDiv;
    @BindView(R.id.lineOfStatistics)
    View lineOfStatistics;
    @BindView(R.id.lineOfBtnDiv)
    View lineOfBtnDiv;
    @BindView(R.id.btn_payout)
    QMUIButton btnPayout;
    @BindView(R.id.btn_income)
    QMUIButton btnIncome;
    @BindView(R.id.btnDiv)
    QMUIFrameLayout btnDiv;
    @BindView(R.id.topBar)
    QMUITopBarLayout topBarLayout;
    @BindView(R.id.dayHeaderTv)
    QMUISpanTouchFixTextView dayHeaderTv;
    @BindView(R.id.dayIncomeTv)
    QMUISpanTouchFixTextView dayIncomeTv;
    @BindView(R.id.dayPayoutTv)
    QMUISpanTouchFixTextView dayPayoutTv;
    @BindView(R.id.monthHeaderTv)
    QMUISpanTouchFixTextView monthHeaderTv;
    @BindView(R.id.monthIncomeTv)
    QMUISpanTouchFixTextView monthIncomeTv;
    @BindView(R.id.monthPayoutTv)
    QMUISpanTouchFixTextView monthPayoutTv;
    @BindView(R.id.yearHeaderTv)
    QMUISpanTouchFixTextView yearHeaderTv;
    @BindView(R.id.yearIncomeTv)
    QMUISpanTouchFixTextView yearIncomeTv;
    @BindView(R.id.yearPayoutTv)
    QMUISpanTouchFixTextView yearPayoutTv;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.pull_layout)
    QMUIPullLayout pullLayout;

    private int mCurrentDialogStyle = com.qmuiteam.qmui.R.style.QMUI_Dialog;
    private RecordAdapter adapter;
    private LedgerViewModel ledgerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        QMUISwipeBackActivityManager.init(getApplication());

        initTopBar();

        initStatisticViewStyle();

        initRecycleView();

        ledgerViewModel = new ViewModelProvider(this).get(LedgerViewModel.class);
        ledgerViewModel.getAllLedgers().observe(this, adapter::submitList);

        ledgerViewModel.getDayMoneyCount(INCOME).observe(this, aDouble -> dayIncomeTv.setText(beatifyMoney(aDouble, INCOME)));

        ledgerViewModel.getDayMoneyCount(PAYOUT).observe(this, aDouble -> dayPayoutTv.setText(beatifyMoney(aDouble, PAYOUT)));

        ledgerViewModel.getMonthMoneyCount(INCOME).observe(this, aDouble -> monthIncomeTv.setText(beatifyMoney(aDouble, INCOME)));

        ledgerViewModel.getMonthMoneyCount(PAYOUT).observe(this, aDouble -> monthPayoutTv.setText(beatifyMoney(aDouble, PAYOUT)));

        ledgerViewModel.getYearMoneyCount(INCOME).observe(this, aDouble -> yearIncomeTv.setText(beatifyMoney(aDouble, INCOME)));

        ledgerViewModel.getYearMoneyCount(PAYOUT).observe(this, aDouble -> yearPayoutTv.setText(beatifyMoney(aDouble, PAYOUT)));
    }

    private Date getDate(int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -i);
        return calendar.getTime();
    }

    private void initTopBar() {
        topBarLayout.setTitle(R.string.app_name);
    }

    private void initStatisticViewStyle() {
        int screenWidth = QMUIDisplayHelper.getScreenWidth(this);
        dayHeaderTv.setWidth((int) (screenWidth * 0.2));
        dayIncomeTv.setWidth((int) (screenWidth * 0.32));
        dayPayoutTv.setWidth((int) (screenWidth * 0.32));

        monthHeaderTv.setWidth((int) (screenWidth * 0.2));
        monthIncomeTv.setWidth((int) (screenWidth * 0.32));
        monthPayoutTv.setWidth((int) (screenWidth * 0.32));

        yearHeaderTv.setWidth((int) (screenWidth * 0.2));
        yearIncomeTv.setWidth((int) (screenWidth * 0.32));
        yearPayoutTv.setWidth((int) (screenWidth * 0.32));

        dayHeaderTv.setText(beatifyHeader(getHeaderStr(DAY)));
        monthHeaderTv.setText(beatifyHeader(getHeaderStr(MONTH)));
        yearHeaderTv.setText(beatifyHeader(getHeaderStr(YEAR)));
    }

    private SpannableString beatifyHeader(String headerStr) {
        if (Objects.isNull(headerStr)) {
            return SpannableString.valueOf(EMPTY);
        }
        SpannableString res = new SpannableString(headerStr);
//        res.setSpan(new StyleSpan(Typeface.BOLD),0, headerStr.length(), SPAN_INCLUSIVE_EXCLUSIVE);
        res.setSpan(new RelativeSizeSpan(1.06f), 0, headerStr.length(), SPAN_INCLUSIVE_EXCLUSIVE);
        return res;
    }

    private String getHeaderStr(StatisticTypeEnum typeEnum) {
        switch (typeEnum) {
            case DAY:
                return getString(R.string.today_str);
            case MONTH:
                return getString(R.string.this_month_str);
            case YEAR:
                return getString(R.string.this_year_str);
            default:
                return EMPTY;
        }
    }

    private SpannableString beatifyMoney(Double money, MoneyTypeEnum type) {
        String moneyStr = type.getPrefix() + fmtMoney(money);
        SpannableString res = new SpannableString(moneyStr);
        res.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 0, moneyStr.length(), SPAN_INCLUSIVE_EXCLUSIVE);
        res.setSpan(new ForegroundColorSpan(getColor(type.getColorResId())), 0, moneyStr.length(), SPAN_INCLUSIVE_EXCLUSIVE);
        res.setSpan(new RelativeSizeSpan(1.06f), 0, moneyStr.length(), SPAN_INCLUSIVE_EXCLUSIVE);
        return res;
    }

    public void addIncome(View view) {
        showAddEditDialog(LedgerRecord.Builder.aLedgerRecord().type(INCOME).build());
    }

    public void addPayout(View view) {
        showAddEditDialog(LedgerRecord.Builder.aLedgerRecord().type(PAYOUT).build());
    }

    private void showAddEditDialog(LedgerRecord ledgerRecord) {
        AddEditDialogBuilder recordDialogBuilder = (AddEditDialogBuilder) new AddEditDialogBuilder(this, ledgerRecord);
        recordDialogBuilder.setSkinManager(QMUISkinManager.defaultInstance(this))
                .addAction(getString(R.string.cancel), (dialog, index) -> dialog.dismiss())
                .addAction(getString(R.string.save), (dialog, index) -> {
                    recordDialogBuilder.makeRecord();
                    ledgerViewModel.upsert(recordDialogBuilder.getRecord());
                    dialog.dismiss();
                });
        recordDialogBuilder.create(mCurrentDialogStyle).show();
        QMUIKeyboardHelper.showKeyboard(recordDialogBuilder.getNumberEditText(), true);
    }


    private void initRecycleView() {
        pullLayout.setActionListener(new QMUIPullLayout.ActionListener() {
            @Override
            public void onActionTriggered(@NonNull QMUIPullLayout.PullAction pullAction) {
                pullLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (pullAction.getPullEdge() == QMUIPullLayout.PULL_EDGE_TOP) {
                            onRefreshData();
                        } else if (pullAction.getPullEdge() == QMUIPullLayout.PULL_EDGE_BOTTOM) {
                            onLoadMore();
                        }
                        pullLayout.finishActionRun(pullAction);
                    }
                }, 3000);
            }
        });

        QMUIRVItemSwipeAction swipeAction = new QMUIRVItemSwipeAction(true, new QMUIRVItemSwipeAction.Callback() {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//                adapter.remove(viewHolder.getAdapterPosition());
                Toast.makeText(MainActivity.this, "onSwiped", Toast.LENGTH_LONG).show();
            }

            @Override
            public int getSwipeDirection(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                return QMUIRVItemSwipeAction.SWIPE_LEFT;
            }

            @Override
            public void onClickAction(QMUIRVItemSwipeAction swipeAction, RecyclerView.ViewHolder selected, QMUISwipeAction action) {
                super.onClickAction(swipeAction, selected, action);
                LedgerRecord dataOfPos = adapter.getDataOfPos(selected.getAdapterPosition());
                if (action == adapter.mDeleteAction) {
                    ledgerViewModel.delete(dataOfPos);
                } else if (action == adapter.mEditAction) {
                    showAddEditDialog(dataOfPos);
                } else {
                    swipeAction.clear();
                }
            }
        });
        swipeAction.attachToRecyclerView(recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this) {
            @Override
            public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        });

        adapter = new RecordAdapter(MainActivity.this);
        recyclerView.setAdapter(adapter);
    }

    private void onRefreshData() {
        recyclerView.scrollToPosition(0);
    }

    private void onLoadMore() {

    }
}