package com.liuyitao.ledger;

import android.content.Context;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.annotation.NonNull;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.liuyitao.ledger.data.entity.LedgerRecord;
import com.qmuiteam.qmui.skin.QMUISkinHelper;
import com.qmuiteam.qmui.skin.QMUISkinValueBuilder;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;

import java.util.Optional;

public class AddEditDialogBuilder extends QMUIDialog.AutoResizeDialogBuilder {
    private final LedgerRecord record;
    private EditText numberEditText;
    private EditText descEditText;

    public AddEditDialogBuilder(Context context, LedgerRecord entity) {
        super(context);
        this.record = entity;
    }

    public EditText getNumberEditText() {
        return numberEditText;
    }

    public EditText getDescEditText() {
        return descEditText;
    }

    @Override
    public View onBuildContent(@NonNull QMUIDialog dialog, @NonNull Context context) {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new ScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        int padding = QMUIDisplayHelper.dp2px(context, 20);
        layout.setPadding(padding, padding, padding, padding);

        numberEditText = new TextInputEditText(context);
        numberEditText.setHint(context.getString(R.string.pls_input_money_num));
        numberEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        numberEditText.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
        LinearLayout.LayoutParams editTextLP = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        editTextLP.bottomMargin = QMUIDisplayHelper.dp2px(context, 15);
        numberEditText.setLayoutParams(editTextLP);
        setInputEditSkin(numberEditText);
        layout.addView(getTextInputLayout4EditText(context, numberEditText));

        // todo : add buutons to show the tags, and the last button is to add a new tag

        descEditText = new TextInputEditText(context);
        descEditText.setHint(context.getString(R.string.click_add_desc));
        descEditText.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        descEditText.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
        setInputEditSkin(descEditText);
        layout.addView(getTextInputLayout4EditText(context, descEditText));

        Optional.ofNullable(this.record.money).ifPresent(money -> numberEditText.setText(String.valueOf(money)));
        Optional.ofNullable(this.record.desc).ifPresent(descEditText::setText);
        return layout;
    }

    public void makeRecord() {
        record.money = Double.parseDouble(numberEditText.getText().toString());
        record.desc = descEditText.getText().toString();
    }

    public LedgerRecord getRecord() {
        return record;
    }

    private TextInputLayout getTextInputLayout4EditText(Context context, EditText editText) {
        TextInputLayout textInputLayout = new TextInputLayout(context);
        textInputLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        textInputLayout.addView(editText);
        return textInputLayout;
    }

    private void setInputEditSkin(EditText editT) {
        QMUISkinValueBuilder builder = QMUISkinValueBuilder.acquire();
        builder.textColor(com.qmuiteam.qmui.R.attr.qmui_skin_support_dialog_edit_text_color);
        builder.hintColor(com.qmuiteam.qmui.R.attr.qmui_skin_support_dialog_edit_text_hint_color);
        QMUISkinHelper.setSkinValue(editT, builder);
        QMUISkinValueBuilder.release(builder);
    }
}
