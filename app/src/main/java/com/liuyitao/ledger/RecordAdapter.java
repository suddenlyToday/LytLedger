package com.liuyitao.ledger;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;

import com.liuyitao.ledger.data.entity.LedgerRecord;
import com.qmuiteam.qmui.recyclerView.QMUISwipeAction;
import com.qmuiteam.qmui.recyclerView.QMUISwipeViewHolder;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.text.Spanned.SPAN_INCLUSIVE_EXCLUSIVE;
import static com.liuyitao.ledger.common.Utils.fmtMoney;

public class RecordAdapter extends PagedListAdapter<LedgerRecord, QMUISwipeViewHolder> {

    private final DateFormat dateFormat = DateFormat.getDateTimeInstance();
    private List<LedgerRecord> mData = new ArrayList<>();

    final QMUISwipeAction mDeleteAction;
    final QMUISwipeAction mEditAction;
    final Context context;

    public RecordAdapter(Context context) {
        super(DIFF_CALLBACK);
        QMUISwipeAction.ActionBuilder builder = new QMUISwipeAction.ActionBuilder()
                .textSize(QMUIDisplayHelper.sp2px(context, 14))
                .textColor(Color.WHITE)
                .paddingStartEnd(QMUIDisplayHelper.dp2px(context, 14));

        this.context = context;
        mDeleteAction = builder.text(context.getString(R.string.delete_record)).backgroundColor(Color.RED).build();
        mEditAction = builder.text(context.getString(R.string.edit_record)).backgroundColor(Color.BLUE).build();
    }

    public void setData(@Nullable List<LedgerRecord> list) {
        mData.clear();
        if (list != null) {
            mData.addAll(list);
        }
        notifyDataSetChanged();
    }

    public LedgerRecord getDataOfPos(int position) {
        return mData.get(position);
    }

    @NonNull
    @Override
    public QMUISwipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_list_item_1, parent, false);
        final QMUISwipeViewHolder vh = new QMUISwipeViewHolder(view);
        vh.addSwipeAction(mDeleteAction);
        vh.addSwipeAction(mEditAction);
        // todo show detail when click
        view.setOnClickListener(v -> Toast.makeText(context,
                "click position=" + vh.getAdapterPosition(), Toast.LENGTH_SHORT).show());
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull QMUISwipeViewHolder holder, int position) {
        TextView textView = holder.itemView.findViewById(R.id.text);
        LedgerRecord ledgerRecord = mData.get(position);
        textView.setText(beatifyRecord(ledgerRecord));
    }

    private SpannableString beatifyRecord(LedgerRecord record) {
        String dateStr = dateFormat.format(record.addDate);
        String moneyStr = record.type.getPrefix() + fmtMoney(record.money);
        String str = dateStr + " " + moneyStr;

        SpannableString res = new SpannableString(str);
        res.setSpan(new RelativeSizeSpan(0.88f), 0, dateStr.length(), SPAN_INCLUSIVE_EXCLUSIVE);

        int moneyStrStart = dateStr.length() + 1;
        res.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), moneyStrStart, str.length(), SPAN_INCLUSIVE_EXCLUSIVE);
        res.setSpan(new ForegroundColorSpan(context.getColor(record.type.getColorResId())), moneyStrStart, str.length(), SPAN_INCLUSIVE_EXCLUSIVE);
        res.setSpan(new RelativeSizeSpan(1.06f), moneyStrStart, str.length(), SPAN_INCLUSIVE_EXCLUSIVE);

        return res;
    }

    @Override
    public int getItemCount() {
        return mData==null? 0 : mData.size();
    }

    private static DiffUtil.ItemCallback<LedgerRecord> DIFF_CALLBACK = new DiffUtil.ItemCallback<LedgerRecord>() {
        @Override
        public boolean areItemsTheSame(@NonNull LedgerRecord oldItem, @NonNull LedgerRecord newItem) {
            return oldItem.uid == newItem.uid;
        }

        @Override
        public boolean areContentsTheSame(@NonNull LedgerRecord oldItem, @NonNull LedgerRecord newItem) {
            return Objects.equals(oldItem, newItem);
        }
    };
}
