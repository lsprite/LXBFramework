package com.lxb.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.lxb.framework.R;

/**
 * Created by Administrator on 2016/4/6.
 */
public class MyProgressDialog extends Dialog {
    Context context;
    private TextView tipTextView;

    public MyProgressDialog(Context context) {
        super(context, R.style.custom_dialog_theme);
        this.context = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.progresswheel_lay, null);
        tipTextView = (TextView) view.findViewById(R.id.tipTextView);
        setContentView(view);
    }

    public MyProgressDialog(Context context, String message) {
        super(context, R.style.custom_dialog_theme);
        this.context = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.progresswheel_lay, null);
        tipTextView = (TextView) view.findViewById(R.id.tipTextView);
        tipTextView.setText(message);
        setContentView(view);
    }
}
