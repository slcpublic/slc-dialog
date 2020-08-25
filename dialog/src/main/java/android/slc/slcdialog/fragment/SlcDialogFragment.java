package android.slc.slcdialog.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.slc.slcdialog.SlcPopup;
import android.util.SparseArray;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;

/**
 * Created by slc on 2019/3/8.
 */

public class SlcDialogFragment extends SlcBaseDialogFragment<AlertDialog.Builder> {

    /**
     * 获取对话框操作对象
     *
     * @param builder
     * @param fragmentManager
     * @param onDismissListener
     * @param onCancelListener
     * @param onClickListenerSparseArray
     * @param cancelable
     * @param key
     * @return
     */
    public static SlcPopup.AlertDialogOperate getAlertDialogOperate(AlertDialog.Builder builder,
                                                                    FragmentManager fragmentManager,
                                                                    DialogInterface.OnDismissListener onDismissListener,
                                                                    DialogInterface.OnCancelListener onCancelListener,
                                                                    SparseArray<DialogInterface.OnClickListener> onClickListenerSparseArray,
                                                                    boolean cancelable, String key, boolean isPositiveClickIsAutoDismiss) {
        SlcDialogFragment slcDialogFragment = new SlcDialogFragment();
        fillAlertDialogOperate(slcDialogFragment, builder, fragmentManager, onDismissListener, onCancelListener, onClickListenerSparseArray, cancelable, key, isPositiveClickIsAutoDismiss);
        return slcDialogFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return mBuilder.create();
    }
}
