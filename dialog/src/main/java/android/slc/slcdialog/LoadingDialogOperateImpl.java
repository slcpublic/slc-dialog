package android.slc.slcdialog;

import android.app.Dialog;
import android.view.View;
import android.widget.TextView;

/**
 * @author slc
 * @date 2021/4/29 11:51
 */
public class LoadingDialogOperateImpl implements SlcPopup.LoadingDialogOperate {
    private final Dialog mLoadingDialog;
    private final boolean mCancelable;
    private final String mKey;

    public LoadingDialogOperateImpl(Dialog loadingDialog, boolean cancelable, String key) {
        this.mLoadingDialog = loadingDialog;
        this.mCancelable = cancelable;
        this.mKey = key;
    }

    @Override
    public void dismiss() {
        this.mLoadingDialog.dismiss();
    }

    @Override
    public void show() {
        this.mLoadingDialog.show();
        SlcPopup.addOperate(getKey(), this);
    }

    @Override
    public boolean isCancelable() {
        return this.mCancelable;
    }

    @Override
    public String getKey() {
        return this.mKey;
    }

    @Override
    public Dialog getDialog() {
        return this.mLoadingDialog;
    }

    @Override
    public void updateMessage(String message) {
        if (this.mLoadingDialog != null) {
            View decorView = this.mLoadingDialog.getWindow().getDecorView();
            if (decorView != null) {
                TextView messageTextView = this.mLoadingDialog.findViewById(android.R.id.message);
                if (messageTextView != null) {
                    messageTextView.setText(message);
                }
            }
        }
    }

}
