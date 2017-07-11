package com.uestcpg.remotedoctor.views;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import com.uestcpg.remotedoctor.R;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * Created by Administrator on 2016/12/7.
 *
 */

public class LoadingDialog extends Dialog{

    @InjectView(R.id.dialog_tip)
    TextView mDialogTv;
    public LoadingDialog(Context context) {
        super(context, R.style.customprogressdialog);
        setContentView(R.layout.loading_dialog);
        ButterKnife.inject(this);
    }

    //设置提示标题
    public void setTip(String tip){
        mDialogTv.setText(tip);
    }

}
