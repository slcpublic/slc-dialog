package android.slc.slcdialog.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.slc.slcdialog.SlcPopup;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.lang.ref.WeakReference;

/**
 * Created by slc on 2019/3/8.
 */

public abstract class SlcBaseDialogFragment<T> extends AppCompatDialogFragment implements SlcPopup.AlertDialogOperate {
    protected T mBuilder;
    protected FragmentManager fragmentManager;
    protected String mKey;
    protected boolean isPositiveClickIsAutoDismiss = true;
    protected DialogInterface.OnDismissListener onDismissListener;
    protected DialogInterface.OnCancelListener onCancelListener;
    protected SparseArray<DialogInterface.OnClickListener> onClickListenerSparseArray;

    /**
     * 获取对话框操作对象
     *
     * @param fragmentManager
     * @param onDismissListener
     * @param onCancelListener
     * @param onClickListenerSparseArray
     * @param cancelable
     * @param key
     * @return
     */
    public static <T> void fillAlertDialogOperate(SlcBaseDialogFragment<T> slcDialogFragment,
                                                                         T builder,
                                                                         FragmentManager fragmentManager,
                                                                         DialogInterface.OnDismissListener onDismissListener,
                                                                         DialogInterface.OnCancelListener onCancelListener,
                                                                         SparseArray<DialogInterface.OnClickListener> onClickListenerSparseArray,
                                                                         boolean cancelable, String key, boolean isPositiveClickIsAutoDismiss) {
        slcDialogFragment.setDialog(builder, fragmentManager);
        slcDialogFragment.setOnDismissListener(onDismissListener);
        slcDialogFragment.setOnCancelListener(onCancelListener);
        slcDialogFragment.setOnClickOfEnsure(onClickListenerSparseArray);
        slcDialogFragment.setCancelable(cancelable);
        slcDialogFragment.setKey(key);
        slcDialogFragment.setIsPositiveClickIsAutoDismiss(isPositiveClickIsAutoDismiss);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        if (getShowsDialog()) {
            setShowsDialog(false);
        }
        super.onActivityCreated(savedInstanceState);
        setShowsDialog(true);
        View view = getView();
        if (view != null) {
            if (view.getParent() != null) {
                throw new IllegalStateException(
                        "DialogFragment can not be attached to a container view");
            }
            getDialog().setContentView(view);
        }
        final Activity activity = getActivity();
        if (activity != null) {
            getDialog().setOwnerActivity(activity);
        }
        getDialog().setCancelable(isCancelable());
        getDialog().setOnCancelListener(new MyOnCancelListener(this));
        getDialog().setOnDismissListener(new MyOnDismissListener(this));
        if (savedInstanceState != null) {
            Bundle dialogState = savedInstanceState.getBundle("android:savedDialogState");
            if (dialogState != null) {
                getDialog().onRestoreInstanceState(dialogState);
            }
        }
    }


    /**
     * 静态内部类对外部持有弱引用，防止内存泄漏
     */
    private static class MyOnCancelListener implements DialogInterface.OnCancelListener {
        private WeakReference<DialogInterface.OnCancelListener> onCancelListenerWeakReference;

        MyOnCancelListener(SlcBaseDialogFragment slcDialogFragment) {
            onCancelListenerWeakReference = new WeakReference<>(slcDialogFragment);
        }

        @Override
        public void onCancel(DialogInterface dialog) {
            DialogInterface.OnCancelListener onCancelListener = onCancelListenerWeakReference.get();
            if (onCancelListener != null) {
                onCancelListener.onCancel(dialog);
            }
        }
    }

    /**
     * 静态内部类对外部持有弱引用，防止内存泄漏
     */
    private static class MyOnDismissListener implements DialogInterface.OnDismissListener {
        private WeakReference<DialogInterface.OnDismissListener> onDismissListenerWeakReference;

        MyOnDismissListener(SlcBaseDialogFragment slcDialogFragment) {
            onDismissListenerWeakReference = new WeakReference<>(slcDialogFragment);
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            DialogInterface.OnDismissListener onDismissListener = onDismissListenerWeakReference.get();
            if (onDismissListener != null) {
                onDismissListener.onDismiss(dialog);
            }
        }
    }

    @Override
    public abstract Dialog onCreateDialog(Bundle savedInstanceState);

    /**
     * 设置dialog
     *
     * @param t
     */
    public void setDialog(T t, FragmentManager fragmentManager) {
        this.mBuilder = t;
        this.fragmentManager = fragmentManager;
    }

    /**
     * 设置监听消失
     *
     * @param onDismissListener
     */
    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    /**
     * 设置取消监听
     *
     * @param onCancelListener
     */
    public void setOnCancelListener(DialogInterface.OnCancelListener onCancelListener) {
        this.onCancelListener = onCancelListener;
    }

    /**
     * 确保按钮不被回收
     *
     * @param onClickListenerSparseArray
     */
    public void setOnClickOfEnsure(SparseArray<DialogInterface.OnClickListener> onClickListenerSparseArray) {
        this.onClickListenerSparseArray = onClickListenerSparseArray;
    }

    /**
     * 设置KEY
     *
     * @param key
     */
    public void setKey(String key) {
        this.mKey = key;
    }

    /**
     * 设置正极按钮是否自动取消对框
     *
     * @param isPositiveClickIsAutoDismiss
     */
    public void setIsPositiveClickIsAutoDismiss(boolean isPositiveClickIsAutoDismiss) {
        this.isPositiveClickIsAutoDismiss = isPositiveClickIsAutoDismiss;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        SlcPopup.removeOperate(mKey);
        this.onClickListenerSparseArray.clear();
        this.onClickListenerSparseArray = null;
        mBuilder = null;
        if (onDismissListener != null) {
            onDismissListener.onDismiss(dialog);
        }
    }

    @Override
    public String getKey() {
        return mKey;
    }

    @Override
    public void show() {
        if (mBuilder == null) {
            throw new NullPointerException("dialog is null");
        }
        SlcPopup.addOperate(mKey, this);
        show(fragmentManager, String.valueOf(hashCode()));
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!isPositiveClickIsAutoDismiss && getDialog() instanceof AlertDialog) {
            AlertDialog alertDialog = (AlertDialog) getDialog();
            Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(v -> {
                if (onClickListenerSparseArray != null) {
                    DialogInterface.OnClickListener itemOnClickListener = onClickListenerSparseArray.get(DialogInterface.BUTTON_POSITIVE);
                    if (itemOnClickListener != null) {
                        itemOnClickListener.onClick(alertDialog, DialogInterface.BUTTON_POSITIVE);
                    }
                    itemOnClickListener = onClickListenerSparseArray.get(SlcPopup.AlertDialogBuilder.ALL_CLICK_LISTENER);
                    if (itemOnClickListener != null) {
                        itemOnClickListener.onClick(alertDialog, DialogInterface.BUTTON_POSITIVE);
                    }
                }
            });
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        if (onCancelListener != null) {
            onCancelListener.onCancel(dialog);
        }
    }

    @Override
    public void onDestroyView() {
        fragmentManager = null;
        super.onDestroyView();
    }
}
