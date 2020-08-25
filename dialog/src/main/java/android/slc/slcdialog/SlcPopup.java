package android.slc.slcdialog;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.slc.slcdialog.fragment.SlcBottomDialogFragment;
import android.slc.slcdialog.fragment.SlcDialogFragment;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.AnimRes;
import androidx.annotation.ArrayRes;
import androidx.annotation.AttrRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.ListPopupWindow;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.SlcBottomSheetAlertDialog;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by slc on 2019/3/8.
 */

public class SlcPopup {
    private static Map<String, BaseOperate> operateMap = new LinkedHashMap<>();

    public static void addOperate(String key, BaseOperate baseOperate) {
        if (!TextUtils.isEmpty(key) && baseOperate != null) {
            operateMap.put(key, baseOperate);
        }
    }

    public static void removeOperate(String key) {
        if (!TextUtils.isEmpty(key)) {
            operateMap.remove(key);
        }
    }

    public static void dismissByKey(String key) {
        BaseOperate baseOperate = getOperateByKey(key);
        if (baseOperate != null) {
            //removeOperate(key);
            baseOperate.dismiss();
        }
    }

    public static BaseOperate getOperateByKey(String key) {
        return operateMap.get(key);
    }

    /**
     * Return the width of screen, in pixel.
     *
     * @return the width of screen, in pixel
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            //noinspection ConstantConditions
            wm.getDefaultDisplay().getRealSize(point);
        } else {
            //noinspection ConstantConditions
            wm.getDefaultDisplay().getSize(point);
        }
        return point.x;
    }

    /**
     * Return the height of screen, in pixel.
     *
     * @return the height of screen, in pixel
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            //noinspection ConstantConditions
            wm.getDefaultDisplay().getRealSize(point);
        } else {
            //noinspection ConstantConditions
            wm.getDefaultDisplay().getSize(point);
        }
        return point.y;
    }

    public static abstract class BaseBuilder<T extends BaseBuilder, O extends BaseOperate> {
        boolean mCancelable = true;
        String mKey;
        int mMaxWidth, mMaxHeight;

        public BaseBuilder() {
            mKey = String.valueOf(System.currentTimeMillis()) + this.hashCode();
        }

        public abstract Context getContext();

        int dip2px(float dipValue) {
            final float scale = getContext().getResources().getDisplayMetrics().density;
            return (int) (dipValue * scale + 0.5f);
        }

        public T setCancelable(boolean cancelable) {
            this.mCancelable = cancelable;
            return (T) this;
        }

        public T setKey(String key) {
            mKey = key;
            return (T) this;
        }

        /**
         * 设置最大宽度
         *
         * @param maxWidth
         * @return
         */
        public T setMaxWidth(int maxWidth) {
            mMaxWidth = maxWidth;
            return (T) this;
        }

        /**
         * 设置最大高度
         *
         * @param maxHeight
         * @return
         */
        public T setMaxHeight(int maxHeight) {
            mMaxHeight = maxHeight;
            return (T) this;
        }

        /**
         * 设置最大高度和宽度
         *
         * @param maxHeight
         * @param maxWidth
         * @return
         */
        public T setMaxWidthAndHeight(int maxWidth, int maxHeight) {
            setMaxWidth(maxWidth);
            setMaxHeight(maxHeight);
            return (T) this;
        }

        /**
         * 创建对话框，
         */
        public abstract O create();
    }

    /**
     * 加载对话框
     */
    public static class LoadingBuilder extends BaseBuilder<LoadingBuilder, DialogOperate> {
        private Dialog mLoadingDialog;
        private View mLoadingView;
        private CharSequence mMessage;
        private DialogInterface.OnDismissListener mOnDismissListener;
        private DialogInterface.OnCancelListener mOnCancelListener;
        private DialogInterface.OnShowListener mOnShowListener;


        public LoadingBuilder(@NonNull Context context) {
            this(context, 0);
        }

        public LoadingBuilder(@NonNull Context context, @StyleRes int themeResId) {
            super();
            mLoadingDialog = new Dialog(context, themeResId);
            mCancelable = false;
        }

        @Override
        public Context getContext() {
            return mLoadingDialog.getContext();
        }


        public LoadingBuilder setView(int layoutResId) {
            return setView(LayoutInflater.from(getContext()).inflate(layoutResId, null));
        }

        public LoadingBuilder setView(View view) {
            this.mLoadingView = view;
            return this;
        }

        public LoadingBuilder setMessage(int messageId) {
            return setMessage(getContext().getString(messageId));
        }

        public LoadingBuilder setMessage(@Nullable CharSequence message) {
            this.mMessage = message;
            return this;
        }

        public LoadingBuilder setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
            this.mOnDismissListener = onDismissListener;
            return this;
        }

        public LoadingBuilder setOnCancelListener(DialogInterface.OnCancelListener onCancelListener) {
            this.mOnCancelListener = onCancelListener;
            return this;
        }

        public LoadingBuilder setOnShowListener(DialogInterface.OnShowListener onShowListener) {
            this.mOnShowListener = onShowListener;
            return this;
        }

        @Override
        public DialogOperate create() {
            if (mLoadingView == null) {
                mLoadingView = LayoutInflater.from(getContext()).inflate(R.layout.slc_def_loading, null);
            }
            TextView messageTextView = mLoadingView.findViewById(android.R.id.message);
            if (messageTextView != null) {
                messageTextView.setText(mMessage);
            }
            mLoadingDialog.setContentView(mLoadingView);
            mLoadingDialog.getWindow().getDecorView().addOnLayoutChangeListener((View v, int left, int top, int right, int bottom,
                                                                                 int oldLeft, int oldTop,
                                                                                 int oldRight, int oldBottom) -> {
                ViewGroup.LayoutParams contentViewLayoutParams = v.getLayoutParams();
                if (contentViewLayoutParams != null) {
                    if (mMaxWidth != 0 && v.getWidth() > mMaxWidth) {
                        contentViewLayoutParams.width = mMaxWidth;
                    }
                    if (mMaxHeight != 0 && v.getHeight() > mMaxHeight) {
                        contentViewLayoutParams.height = mMaxHeight;
                    }
                    v.setLayoutParams(contentViewLayoutParams);
                }
            });
            WeakReference<DialogInterface.OnDismissListener> weakReferenceOnDismissListener = new WeakReference<>(this.mOnDismissListener);
            mLoadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    SlcPopup.removeOperate(mKey);
                    DialogInterface.OnDismissListener onDismissListener = weakReferenceOnDismissListener.get();
                    if (onDismissListener != null) {
                        onDismissListener.onDismiss(dialog);
                    }
                }
            });
            mLoadingDialog.setOnCancelListener(new WeakReference<>(this.mOnCancelListener).get());
            mLoadingDialog.setOnShowListener(new WeakReference<>(this.mOnShowListener).get());
            mLoadingDialog.setCancelable(mCancelable);
            return new DialogOperate() {
                private LoadingBuilder loadingBuilder = LoadingBuilder.this;

                @Override
                public Dialog getDialog() {
                    return mLoadingDialog;
                }

                @Override
                public void dismiss() {
                    mLoadingDialog.dismiss();
                    loadingBuilder = null;
                    /*LoadingBuilder.this.mOnCancelListener=null;
                    LoadingBuilder.this.mOnDismissListener=null;
                    LoadingBuilder.this.mOnShowListener=null;*/
                }

                @Override
                public void show() {
                    mLoadingDialog.show();
                    SlcPopup.addOperate(getKey(), this);
                }

                @Override
                public boolean isCancelable() {
                    return mCancelable;
                }

                @Override
                public String getKey() {
                    return mKey;
                }
            };
        }
    }

    public static abstract class AlertDialogBuilder<T extends AlertDialogBuilder, O extends DialogOperate> extends BaseBuilder<T, O> {
        AlertDialog.Builder mAlertDialogBuilder;
        WeakReference<FragmentManager> mSupportFragmentManagerReference;
        public static final int ALL_CLICK_LISTENER = -100, ITEM_CLICK_LISTENER = -101;
        SparseArray<DialogInterface.OnClickListener> mDialogOnClickListenerSparseArray = new SparseArray<>();
        OnClickListenerDef mOnClickListenerDef = new OnClickListenerDef();
        DialogInterface.OnDismissListener mOnDismissListener;
        DialogInterface.OnCancelListener mOnCancelListener;
        DialogInterface.OnShowListener mOnShowListener;
        boolean mIsPositiveClickIsAutoDismiss = true;

        private static class OnClickListenerDef implements DialogInterface.OnClickListener {
            private WeakReference<SparseArray<DialogInterface.OnClickListener>> clickListenerWeakReference;

            private OnClickListenerDef() {

            }

            public void setOnClickListener(SparseArray<DialogInterface.OnClickListener> dialogOnClickListenerSparseArray) {
                clickListenerWeakReference = new WeakReference<>(dialogOnClickListenerSparseArray);
            }

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (clickListenerWeakReference.get() != null) {
                    SparseArray<DialogInterface.OnClickListener> dialogOnClickListenerSparseArray = clickListenerWeakReference.get();
                    DialogInterface.OnClickListener itemOnClickListenerOfAll = dialogOnClickListenerSparseArray.get(ALL_CLICK_LISTENER);
                    if (which >= 0) {
                        DialogInterface.OnClickListener itemOnClickListener = dialogOnClickListenerSparseArray.get(ITEM_CLICK_LISTENER);
                        if (itemOnClickListener != null) {
                            itemOnClickListener.onClick(dialog, which);
                        }
                    } else {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                            case DialogInterface.BUTTON_NEGATIVE:
                            case DialogInterface.BUTTON_NEUTRAL:
                                DialogInterface.OnClickListener itemOnClickListener = dialogOnClickListenerSparseArray.get(which);
                                if (itemOnClickListener != null) {
                                    itemOnClickListener.onClick(dialog, which);
                                }
                                break;
                        }
                    }
                    if (itemOnClickListenerOfAll != null) {
                        itemOnClickListenerOfAll.onClick(dialog, which);
                    }
                }
            }
        }

        public AlertDialogBuilder(@NonNull Context context) {
            this(context, 0);
        }

        public AlertDialogBuilder(@NonNull Context context, int themeResId) {
            super();
            mSupportFragmentManagerReference = new WeakReference<>(((FragmentActivity) context).getSupportFragmentManager());
            mAlertDialogBuilder = new AlertDialog.Builder(context, themeResId);
        }

        @Override
        public Context getContext() {
            return mAlertDialogBuilder.getContext();
        }


        public T setCustomTitle(@Nullable View customTitleView) {
            mAlertDialogBuilder.setCustomTitle(customTitleView);
            return (T) this;
        }

        public T setTitle(@StringRes int titleId) {
            mAlertDialogBuilder.setTitle(titleId);
            return (T) this;
        }

        public T setTitle(@Nullable CharSequence titleMsg) {
            mAlertDialogBuilder.setTitle(titleMsg);
            return (T) this;
        }

        public T setMessage(@StringRes int messageId) {
            mAlertDialogBuilder.setMessage(messageId);
            return (T) this;
        }

        public T setMessage(@Nullable CharSequence message) {
            mAlertDialogBuilder.setMessage(message);
            return (T) this;
        }

        public T setView(int layoutResId) {
            mAlertDialogBuilder.setView(layoutResId);
            return (T) this;
        }

        public T setView(View view) {
            mAlertDialogBuilder.setView(view);
            return (T) this;
        }

        public T setIcon(@DrawableRes int iconId) {
            mAlertDialogBuilder.setIcon(iconId);
            return (T) this;
        }

        public T setIcon(@Nullable Drawable icon) {
            mAlertDialogBuilder.setIcon(icon);
            return (T) this;
        }

        public T setIconAttribute(@AttrRes int attrId) {
            mAlertDialogBuilder.setIconAttribute(attrId);
            return (T) this;
        }

        public T setDefPositiveAndNegativeButton() {
            setPositiveButton(R.string.def_positive);
            setNegativeButton(R.string.def_negative);
            return (T) this;
        }

        public T setPositiveClickIsAutoDismiss(boolean isAutoDismiss) {
            this.mIsPositiveClickIsAutoDismiss = isAutoDismiss;
            return (T) this;
        }

        public T setPositiveButton(@StringRes int textId) {
            setPositiveButton(textId, null);
            return (T) this;
        }

        public T setPositiveButton(@StringRes int textId, DialogInterface.OnClickListener listener) {
            if (listener != null) {
                mDialogOnClickListenerSparseArray.append(DialogInterface.BUTTON_POSITIVE, listener);
            }
            mAlertDialogBuilder.setPositiveButton(textId, mOnClickListenerDef);
            return (T) this;
        }

        public T setPositiveButton(CharSequence text) {
            return setPositiveButton(text, null);
        }

        public T setPositiveButton(CharSequence text, DialogInterface.OnClickListener listener) {
            if (listener != null) {
                mDialogOnClickListenerSparseArray.append(DialogInterface.BUTTON_POSITIVE, listener);
            }
            mAlertDialogBuilder.setPositiveButton(text, mOnClickListenerDef);
            return (T) this;
        }

        public T setPositiveButtonIcon(Drawable icon) {
            mAlertDialogBuilder.setPositiveButtonIcon(icon);
            return (T) this;
        }

        public T setNegativeButton(@StringRes int textId) {
            return setNegativeButton(textId, null);
        }

        public T setNegativeButton(@StringRes int textId, DialogInterface.OnClickListener listener) {
            if (listener != null) {
                mDialogOnClickListenerSparseArray.append(DialogInterface.BUTTON_NEGATIVE, listener);
            }
            mAlertDialogBuilder.setNegativeButton(textId, mOnClickListenerDef);
            return (T) this;
        }

        public T setNegativeButton(CharSequence text, DialogInterface.OnClickListener listener) {
            if (listener != null) {
                mDialogOnClickListenerSparseArray.append(DialogInterface.BUTTON_NEGATIVE, listener);
            }
            mAlertDialogBuilder.setNegativeButton(text, mOnClickListenerDef);
            return (T) this;
        }

        public T setNegativeButtonIcon(Drawable icon) {
            mAlertDialogBuilder.setNegativeButtonIcon(icon);
            return (T) this;
        }

        public T setNeutralButton(@StringRes int textId) {
            return setNeutralButton(textId, null);
        }

        public T setNeutralButton(@StringRes int textId, DialogInterface.OnClickListener listener) {
            if (listener != null) {
                mDialogOnClickListenerSparseArray.append(DialogInterface.BUTTON_NEUTRAL, listener);
            }
            mAlertDialogBuilder.setNeutralButton(textId, mOnClickListenerDef);
            return (T) this;
        }

        public T setNeutralButton(CharSequence text) {
            return setNeutralButton(text, null);
        }

        public T setNeutralButton(CharSequence text, DialogInterface.OnClickListener listener) {
            if (listener != null) {
                mDialogOnClickListenerSparseArray.append(DialogInterface.BUTTON_NEUTRAL, listener);
            }
            mAlertDialogBuilder.setNeutralButton(text, mOnClickListenerDef);
            return (T) this;
        }

        /**
         * 设置按钮监听
         *
         * @param listener
         * @return
         */
        public T setOnClickListener(DialogInterface.OnClickListener listener) {
            if (listener != null) {
                mDialogOnClickListenerSparseArray.append(ALL_CLICK_LISTENER, listener);
            }
            return (T) this;
        }

        public T setNeutralButtonIcon(Drawable icon) {
            mAlertDialogBuilder.setNeutralButtonIcon(icon);
            return (T) this;
        }

        public T setOnCancelListener(DialogInterface.OnCancelListener onCancelListener) {
            this.mOnCancelListener = onCancelListener;
            return (T) this;
        }

        public T setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
            this.mOnDismissListener = onDismissListener;
            return (T) this;
        }

        public T setOnKeyListener(DialogInterface.OnKeyListener onKeyListener) {
            mAlertDialogBuilder.setOnKeyListener(onKeyListener);
            return (T) this;
        }

        public T setOnShowListener(DialogInterface.OnShowListener onShowListener) {
            this.mOnShowListener = onShowListener;
            return (T) this;
        }
    }

    public static class NativeAlertDialogBuilder extends AlertDialogBuilder<NativeAlertDialogBuilder, AlertDialogOperate> {

        public NativeAlertDialogBuilder(@NonNull Context context) {
            super(context);
        }

        public NativeAlertDialogBuilder(@NonNull Context context, @StyleRes int themeResId) {
            super(context, themeResId);
        }

        public NativeAlertDialogBuilder setItems(@ArrayRes int itemsId) {
            return setItems(itemsId, null);
        }

        public NativeAlertDialogBuilder setItems(@ArrayRes int itemsId, DialogInterface.OnClickListener listener) {
            if (listener != null) {
                this.mDialogOnClickListenerSparseArray.append(ITEM_CLICK_LISTENER, listener);
            }
            this.mAlertDialogBuilder.setItems(itemsId, mOnClickListenerDef);
            return this;
        }

        public NativeAlertDialogBuilder setItems(CharSequence[] items) {
            return setItems(items, null);
        }

        public NativeAlertDialogBuilder setItems(CharSequence[] items, DialogInterface.OnClickListener listener) {
            if (listener != null) {
                this.mDialogOnClickListenerSparseArray.append(ITEM_CLICK_LISTENER, listener);
            }
            this.mAlertDialogBuilder.setItems(items, mOnClickListenerDef);
            return this;
        }

        public NativeAlertDialogBuilder setAdapter(ListAdapter adapter) {
            return setAdapter(adapter, null);
        }

        public NativeAlertDialogBuilder setAdapter(ListAdapter adapter, DialogInterface.OnClickListener listener) {
            if (listener != null) {
                this.mDialogOnClickListenerSparseArray.append(ITEM_CLICK_LISTENER, listener);
            }
            this.mAlertDialogBuilder.setAdapter(adapter, mOnClickListenerDef);
            return this;
        }

        public NativeAlertDialogBuilder setCursor(Cursor cursor, String labelColumn) {
            return setCursor(cursor, null, labelColumn);
        }

        public NativeAlertDialogBuilder setCursor(Cursor cursor, DialogInterface.OnClickListener listener, String labelColumn) {
            if (listener != null) {
                this.mDialogOnClickListenerSparseArray.append(ITEM_CLICK_LISTENER, listener);
            }
            this.mAlertDialogBuilder.setCursor(cursor, mOnClickListenerDef, labelColumn);
            return this;
        }

        public NativeAlertDialogBuilder setMultiChoiceItems(@ArrayRes int itemsId, boolean[] checkedItems, DialogInterface.OnMultiChoiceClickListener listener) {
            this.mAlertDialogBuilder.setMultiChoiceItems(itemsId, checkedItems, listener);
            return this;
        }

        public NativeAlertDialogBuilder setMultiChoiceItems(CharSequence[] items, boolean[] checkedItems, DialogInterface.OnMultiChoiceClickListener listener) {
            this.mAlertDialogBuilder.setMultiChoiceItems(items, checkedItems, listener);
            return this;
        }

        public NativeAlertDialogBuilder setMultiChoiceItems(Cursor cursor, String isCheckedColumn, String labelColumn, DialogInterface.OnMultiChoiceClickListener listener) {
            this.mAlertDialogBuilder.setMultiChoiceItems(cursor, isCheckedColumn, labelColumn, listener);
            return this;
        }

        public NativeAlertDialogBuilder setSingleChoiceItems(@ArrayRes int itemsId, int checkedItem) {
            return setSingleChoiceItems(itemsId, checkedItem, null);
        }

        public NativeAlertDialogBuilder setSingleChoiceItems(@ArrayRes int itemsId, int checkedItem, DialogInterface.OnClickListener listener) {
            if (listener != null) {
                this.mDialogOnClickListenerSparseArray.append(ITEM_CLICK_LISTENER, listener);
            }
            this.mAlertDialogBuilder.setSingleChoiceItems(itemsId, checkedItem, mOnClickListenerDef);
            return this;
        }

        public NativeAlertDialogBuilder setSingleChoiceItems(Cursor cursor, int checkedItem, String labelColumn) {
            return setSingleChoiceItems(cursor, checkedItem, labelColumn, null);
        }

        public NativeAlertDialogBuilder setSingleChoiceItems(Cursor cursor, int checkedItem, String labelColumn, DialogInterface.OnClickListener listener) {
            if (listener != null) {
                this.mDialogOnClickListenerSparseArray.append(ITEM_CLICK_LISTENER, listener);
            }
            this.mAlertDialogBuilder.setSingleChoiceItems(cursor, checkedItem, labelColumn, mOnClickListenerDef);
            return this;
        }

        public NativeAlertDialogBuilder setSingleChoiceItems(CharSequence[] items, int checkedItem) {
            return setSingleChoiceItems(items, checkedItem, null);
        }

        public NativeAlertDialogBuilder setSingleChoiceItems(CharSequence[] items, int checkedItem, DialogInterface.OnClickListener listener) {
            if (listener != null) {
                this.mDialogOnClickListenerSparseArray.append(ITEM_CLICK_LISTENER, listener);
            }
            this.mAlertDialogBuilder.setSingleChoiceItems(items, checkedItem, mOnClickListenerDef);
            return this;
        }

        public NativeAlertDialogBuilder setSingleChoiceItems(ListAdapter adapter, int checkedItem) {
            return setSingleChoiceItems(adapter, checkedItem, null);
        }

        public NativeAlertDialogBuilder setSingleChoiceItems(ListAdapter adapter, int checkedItem, DialogInterface.OnClickListener listener) {
            if (listener != null) {
                this.mDialogOnClickListenerSparseArray.append(ITEM_CLICK_LISTENER, listener);
            }
            this.mAlertDialogBuilder.setSingleChoiceItems(adapter, checkedItem, mOnClickListenerDef);
            return this;
        }

        public NativeAlertDialogBuilder setOnItemSelectedListener(AdapterView.OnItemSelectedListener listener) {
            mAlertDialogBuilder.setOnItemSelectedListener(listener);
            return this;
        }

        public AlertDialogOperate create() {
            mOnClickListenerDef.setOnClickListener(mDialogOnClickListenerSparseArray);
            AlertDialogOperate alertDialogOperate = SlcDialogFragment.getAlertDialogOperate(mAlertDialogBuilder, mSupportFragmentManagerReference.get(),
                    this.mOnDismissListener, this.mOnCancelListener, mDialogOnClickListenerSparseArray, this.mCancelable, this.mKey, this.mIsPositiveClickIsAutoDismiss);
            mAlertDialogBuilder = null;
            mSupportFragmentManagerReference = null;
            return alertDialogOperate;
        }

    }

    public static abstract class BottomAlertDialogBuilder<T extends BottomAlertDialogBuilder, O extends DialogOperate> extends BaseBuilder<T, O> {
        SlcBottomSheetAlertDialog.Builder mAlertDialogBuilder;
        WeakReference<FragmentManager> mSupportFragmentManagerReference;
        public static final int ALL_CLICK_LISTENER = -100, ITEM_CLICK_LISTENER = -101;
        SparseArray<DialogInterface.OnClickListener> mDialogOnClickListenerSparseArray = new SparseArray<>();
        OnClickListenerDef mOnClickListenerDef = new OnClickListenerDef();
        DialogInterface.OnDismissListener mOnDismissListener;
        DialogInterface.OnCancelListener mOnCancelListener;
        DialogInterface.OnShowListener mOnShowListener;
        boolean mIsPositiveClickIsAutoDismiss = true;

        private static class OnClickListenerDef implements DialogInterface.OnClickListener {
            private WeakReference<SparseArray<DialogInterface.OnClickListener>> clickListenerWeakReference;

            private OnClickListenerDef() {

            }

            public void setOnClickListener(SparseArray<DialogInterface.OnClickListener> dialogOnClickListenerSparseArray) {
                clickListenerWeakReference = new WeakReference<>(dialogOnClickListenerSparseArray);
            }

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (clickListenerWeakReference.get() != null) {
                    SparseArray<DialogInterface.OnClickListener> dialogOnClickListenerSparseArray = clickListenerWeakReference.get();
                    DialogInterface.OnClickListener itemOnClickListenerOfAll = dialogOnClickListenerSparseArray.get(ALL_CLICK_LISTENER);
                    if (which >= 0) {
                        DialogInterface.OnClickListener itemOnClickListener = dialogOnClickListenerSparseArray.get(ITEM_CLICK_LISTENER);
                        if (itemOnClickListener != null) {
                            itemOnClickListener.onClick(dialog, which);
                        }
                    } else {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                            case DialogInterface.BUTTON_NEGATIVE:
                            case DialogInterface.BUTTON_NEUTRAL:
                                DialogInterface.OnClickListener itemOnClickListener = dialogOnClickListenerSparseArray.get(which);
                                if (itemOnClickListener != null) {
                                    itemOnClickListener.onClick(dialog, which);
                                }
                                break;
                        }
                    }
                    if (itemOnClickListenerOfAll != null) {
                        itemOnClickListenerOfAll.onClick(dialog, which);
                    }
                }
            }
        }

        public BottomAlertDialogBuilder(@NonNull Context context) {
            this(context, 0);
        }

        public BottomAlertDialogBuilder(@NonNull Context context, int themeResId) {
            super();
            mSupportFragmentManagerReference = new WeakReference<>(((FragmentActivity) context).getSupportFragmentManager());
            mAlertDialogBuilder = new SlcBottomSheetAlertDialog.Builder(context, themeResId);
        }

        @Override
        public Context getContext() {
            return mAlertDialogBuilder.getContext();
        }

        public T setHideable(boolean hideable) {
            mAlertDialogBuilder.setHideable(hideable);
            return (T) this;
        }

        public T setBottomSheetCallback(BottomSheetBehavior.BottomSheetCallback bottomSheetCallback) {
            mAlertDialogBuilder.setBottomSheetCallback(bottomSheetCallback);
            return (T) this;
        }

        public T setCustomTitle(@Nullable View customTitleView) {
            mAlertDialogBuilder.setCustomTitle(customTitleView);
            return (T) this;
        }

        public T setTitle(@StringRes int titleId) {
            mAlertDialogBuilder.setTitle(titleId);
            return (T) this;
        }

        public T setTitle(@Nullable CharSequence titleMsg) {
            mAlertDialogBuilder.setTitle(titleMsg);
            return (T) this;
        }

        public T setMessage(@StringRes int messageId) {
            mAlertDialogBuilder.setMessage(messageId);
            return (T) this;
        }

        public T setMessage(@Nullable CharSequence message) {
            mAlertDialogBuilder.setMessage(message);
            return (T) this;
        }

        public T setView(int layoutResId) {
            mAlertDialogBuilder.setView(layoutResId);
            return (T) this;
        }

        public T setView(View view) {
            mAlertDialogBuilder.setView(view);
            return (T) this;
        }

        public T setIcon(@DrawableRes int iconId) {
            mAlertDialogBuilder.setIcon(iconId);
            return (T) this;
        }

        public T setIcon(@Nullable Drawable icon) {
            mAlertDialogBuilder.setIcon(icon);
            return (T) this;
        }

        public T setIconAttribute(@AttrRes int attrId) {
            mAlertDialogBuilder.setIconAttribute(attrId);
            return (T) this;
        }

        public T setDefPositiveAndNegativeButton() {
            setPositiveButton(R.string.def_positive);
            setNegativeButton(R.string.def_negative);
            return (T) this;
        }

        public T setPositiveClickIsAutoDismiss(boolean isAutoDismiss) {
            this.mIsPositiveClickIsAutoDismiss = isAutoDismiss;
            return (T) this;
        }

        public T setPositiveButton(@StringRes int textId) {
            setPositiveButton(textId, null);
            return (T) this;
        }

        public T setPositiveButton(@StringRes int textId, DialogInterface.OnClickListener listener) {
            if (listener != null) {
                mDialogOnClickListenerSparseArray.append(DialogInterface.BUTTON_POSITIVE, listener);
            }
            mAlertDialogBuilder.setPositiveButton(textId, mOnClickListenerDef);
            return (T) this;
        }

        public T setPositiveButton(CharSequence text) {
            return setPositiveButton(text, null);
        }

        public T setPositiveButton(CharSequence text, DialogInterface.OnClickListener listener) {
            if (listener != null) {
                mDialogOnClickListenerSparseArray.append(DialogInterface.BUTTON_POSITIVE, listener);
            }
            mAlertDialogBuilder.setPositiveButton(text, mOnClickListenerDef);
            return (T) this;
        }

        public T setPositiveButtonIcon(Drawable icon) {
            mAlertDialogBuilder.setPositiveButtonIcon(icon);
            return (T) this;
        }

        public T setNegativeButton(@StringRes int textId) {
            return setNegativeButton(textId, null);
        }

        public T setNegativeButton(@StringRes int textId, DialogInterface.OnClickListener listener) {
            if (listener != null) {
                mDialogOnClickListenerSparseArray.append(DialogInterface.BUTTON_NEGATIVE, listener);
            }
            mAlertDialogBuilder.setNegativeButton(textId, mOnClickListenerDef);
            return (T) this;
        }

        public T setNegativeButton(CharSequence text, DialogInterface.OnClickListener listener) {
            if (listener != null) {
                mDialogOnClickListenerSparseArray.append(DialogInterface.BUTTON_NEGATIVE, listener);
            }
            mAlertDialogBuilder.setNegativeButton(text, mOnClickListenerDef);
            return (T) this;
        }

        public T setNegativeButtonIcon(Drawable icon) {
            mAlertDialogBuilder.setNegativeButtonIcon(icon);
            return (T) this;
        }

        public T setNeutralButton(@StringRes int textId) {
            return setNeutralButton(textId, null);
        }

        public T setNeutralButton(@StringRes int textId, DialogInterface.OnClickListener listener) {
            if (listener != null) {
                mDialogOnClickListenerSparseArray.append(DialogInterface.BUTTON_NEUTRAL, listener);
            }
            mAlertDialogBuilder.setNeutralButton(textId, mOnClickListenerDef);
            return (T) this;
        }

        public T setNeutralButton(CharSequence text) {
            return setNeutralButton(text, null);
        }

        public T setNeutralButton(CharSequence text, DialogInterface.OnClickListener listener) {
            if (listener != null) {
                mDialogOnClickListenerSparseArray.append(DialogInterface.BUTTON_NEUTRAL, listener);
            }
            mAlertDialogBuilder.setNeutralButton(text, mOnClickListenerDef);
            return (T) this;
        }

        /**
         * 设置按钮监听
         *
         * @param listener
         * @return
         */
        public T setOnClickListener(DialogInterface.OnClickListener listener) {
            if (listener != null) {
                mDialogOnClickListenerSparseArray.append(ALL_CLICK_LISTENER, listener);
            }
            return (T) this;
        }

        public T setNeutralButtonIcon(Drawable icon) {
            mAlertDialogBuilder.setNeutralButtonIcon(icon);
            return (T) this;
        }

        public T setOnCancelListener(DialogInterface.OnCancelListener onCancelListener) {
            this.mOnCancelListener = onCancelListener;
            return (T) this;
        }

        public T setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
            this.mOnDismissListener = onDismissListener;
            return (T) this;
        }

        public T setOnKeyListener(DialogInterface.OnKeyListener onKeyListener) {
            mAlertDialogBuilder.setOnKeyListener(onKeyListener);
            return (T) this;
        }

        public T setOnShowListener(DialogInterface.OnShowListener onShowListener) {
            this.mOnShowListener = onShowListener;
            return (T) this;
        }
    }

    public static class BottomNativeAlertDialogBuilder extends BottomAlertDialogBuilder<BottomNativeAlertDialogBuilder, AlertDialogOperate> {

        public BottomNativeAlertDialogBuilder(@NonNull Context context) {
            super(context);
        }

        public BottomNativeAlertDialogBuilder(@NonNull Context context, @StyleRes int themeResId) {
            super(context, themeResId);
        }

        public BottomNativeAlertDialogBuilder setItems(@ArrayRes int itemsId) {
            return setItems(itemsId, null);
        }

        public BottomNativeAlertDialogBuilder setItems(@ArrayRes int itemsId, DialogInterface.OnClickListener listener) {
            if (listener != null) {
                this.mDialogOnClickListenerSparseArray.append(ITEM_CLICK_LISTENER, listener);
            }
            this.mAlertDialogBuilder.setItems(itemsId, mOnClickListenerDef);
            return this;
        }

        public BottomNativeAlertDialogBuilder setItems(CharSequence[] items) {
            return setItems(items, null);
        }

        public BottomNativeAlertDialogBuilder setItems(CharSequence[] items, DialogInterface.OnClickListener listener) {
            if (listener != null) {
                this.mDialogOnClickListenerSparseArray.append(ITEM_CLICK_LISTENER, listener);
            }
            this.mAlertDialogBuilder.setItems(items, mOnClickListenerDef);
            return this;
        }

        public BottomNativeAlertDialogBuilder setAdapter(ListAdapter adapter) {
            return setAdapter(adapter, null);
        }

        public BottomNativeAlertDialogBuilder setAdapter(ListAdapter adapter, DialogInterface.OnClickListener listener) {
            if (listener != null) {
                this.mDialogOnClickListenerSparseArray.append(ITEM_CLICK_LISTENER, listener);
            }
            this.mAlertDialogBuilder.setAdapter(adapter, mOnClickListenerDef);
            return this;
        }

        public BottomNativeAlertDialogBuilder setCursor(Cursor cursor, String labelColumn) {
            return setCursor(cursor, null, labelColumn);
        }

        public BottomNativeAlertDialogBuilder setCursor(Cursor cursor, DialogInterface.OnClickListener listener, String labelColumn) {
            if (listener != null) {
                this.mDialogOnClickListenerSparseArray.append(ITEM_CLICK_LISTENER, listener);
            }
            this.mAlertDialogBuilder.setCursor(cursor, mOnClickListenerDef, labelColumn);
            return this;
        }

        public BottomNativeAlertDialogBuilder setMultiChoiceItems(@ArrayRes int itemsId, boolean[] checkedItems, DialogInterface.OnMultiChoiceClickListener listener) {
            this.mAlertDialogBuilder.setMultiChoiceItems(itemsId, checkedItems, listener);
            return this;
        }

        public BottomNativeAlertDialogBuilder setMultiChoiceItems(CharSequence[] items, boolean[] checkedItems, DialogInterface.OnMultiChoiceClickListener listener) {
            this.mAlertDialogBuilder.setMultiChoiceItems(items, checkedItems, listener);
            return this;
        }

        public BottomNativeAlertDialogBuilder setMultiChoiceItems(Cursor cursor, String isCheckedColumn, String labelColumn, DialogInterface.OnMultiChoiceClickListener listener) {
            this.mAlertDialogBuilder.setMultiChoiceItems(cursor, isCheckedColumn, labelColumn, listener);
            return this;
        }

        public BottomNativeAlertDialogBuilder setSingleChoiceItems(@ArrayRes int itemsId, int checkedItem) {
            return setSingleChoiceItems(itemsId, checkedItem, null);
        }

        public BottomNativeAlertDialogBuilder setSingleChoiceItems(@ArrayRes int itemsId, int checkedItem, DialogInterface.OnClickListener listener) {
            if (listener != null) {
                this.mDialogOnClickListenerSparseArray.append(ITEM_CLICK_LISTENER, listener);
            }
            this.mAlertDialogBuilder.setSingleChoiceItems(itemsId, checkedItem, mOnClickListenerDef);
            return this;
        }

        public BottomNativeAlertDialogBuilder setSingleChoiceItems(Cursor cursor, int checkedItem, String labelColumn) {
            return setSingleChoiceItems(cursor, checkedItem, labelColumn, null);
        }

        public BottomNativeAlertDialogBuilder setSingleChoiceItems(Cursor cursor, int checkedItem, String labelColumn, DialogInterface.OnClickListener listener) {
            if (listener != null) {
                this.mDialogOnClickListenerSparseArray.append(ITEM_CLICK_LISTENER, listener);
            }
            this.mAlertDialogBuilder.setSingleChoiceItems(cursor, checkedItem, labelColumn, mOnClickListenerDef);
            return this;
        }

        public BottomNativeAlertDialogBuilder setSingleChoiceItems(CharSequence[] items, int checkedItem) {
            return setSingleChoiceItems(items, checkedItem, null);
        }

        public BottomNativeAlertDialogBuilder setSingleChoiceItems(CharSequence[] items, int checkedItem, DialogInterface.OnClickListener listener) {
            if (listener != null) {
                this.mDialogOnClickListenerSparseArray.append(ITEM_CLICK_LISTENER, listener);
            }
            this.mAlertDialogBuilder.setSingleChoiceItems(items, checkedItem, mOnClickListenerDef);
            return this;
        }

        public BottomNativeAlertDialogBuilder setSingleChoiceItems(ListAdapter adapter, int checkedItem) {
            return setSingleChoiceItems(adapter, checkedItem, null);
        }

        public BottomNativeAlertDialogBuilder setSingleChoiceItems(ListAdapter adapter, int checkedItem, DialogInterface.OnClickListener listener) {
            if (listener != null) {
                this.mDialogOnClickListenerSparseArray.append(ITEM_CLICK_LISTENER, listener);
            }
            this.mAlertDialogBuilder.setSingleChoiceItems(adapter, checkedItem, mOnClickListenerDef);
            return this;
        }

        public BottomNativeAlertDialogBuilder setOnItemSelectedListener(AdapterView.OnItemSelectedListener listener) {
            mAlertDialogBuilder.setOnItemSelectedListener(listener);
            return this;
        }

        public AlertDialogOperate create() {
            mOnClickListenerDef.setOnClickListener(mDialogOnClickListenerSparseArray);
            AlertDialogOperate alertDialogOperate = SlcBottomDialogFragment.getAlertDialogOperate(mAlertDialogBuilder, mSupportFragmentManagerReference.get(),
                    this.mOnDismissListener, this.mOnCancelListener, mDialogOnClickListenerSparseArray, this.mCancelable, this.mKey, this.mIsPositiveClickIsAutoDismiss);
            mAlertDialogBuilder = null;
            mSupportFragmentManagerReference = null;
            return alertDialogOperate;
        }

    }

    public static class PopupWindowBuilder extends BaseBuilder<PopupWindowBuilder, PopupOperate> {
        Context mContext;
        ListPopupWindow mListPopupWindow;

        public PopupWindowBuilder(Context context) {
            this.mContext = context;
            mListPopupWindow = new ListPopupWindow(context);
        }

        @Override
        public Context getContext() {
            return this.mContext;
        }

        public PopupWindowBuilder setAdapter(@Nullable ListAdapter adapter) {
            mListPopupWindow.setAdapter(adapter);
            return this;
        }

        public PopupWindowBuilder setPromptPosition(int position) {
            mListPopupWindow.setPromptPosition(position);
            return this;
        }

        public PopupWindowBuilder setModal(boolean modal) {
            mListPopupWindow.setModal(modal);
            return this;
        }

        public PopupWindowBuilder setSoftInputMode(int mode) {
            mListPopupWindow.setSoftInputMode(mode);
            return this;
        }

        public PopupWindowBuilder setListSelector(Drawable selector) {
            mListPopupWindow.setListSelector(selector);
            return this;
        }

        public PopupWindowBuilder setBackgroundDrawable(@Nullable Drawable d) {
            mListPopupWindow.setBackgroundDrawable(d);
            return this;
        }

        public PopupWindowBuilder setAnimationStyle(@StyleRes int animationStyle) {
            mListPopupWindow.setAnimationStyle(animationStyle);
            return this;
        }

        public PopupWindowBuilder setAnchorView(@Nullable View anchor) {
            mListPopupWindow.setAnchorView(anchor);
            return this;
        }

        public PopupWindowBuilder setHorizontalOffset(int offset) {
            mListPopupWindow.setHorizontalOffset(offset);
            return this;
        }

        public PopupWindowBuilder setVerticalOffset(int offset) {
            mListPopupWindow.setVerticalOffset(offset);
            return this;
        }

        public PopupWindowBuilder setDropDownGravity(int gravity) {
            mListPopupWindow.setDropDownGravity(gravity);
            return this;
        }

        public PopupWindowBuilder setWidth(int width) {
            mListPopupWindow.setWidth(width);
            return this;
        }

        public PopupWindowBuilder setContentWidth(int width) {
            mListPopupWindow.setContentWidth(width);
            return this;
        }

        public PopupWindowBuilder setHeight(int height) {
            mListPopupWindow.setHeight(height);
            return this;
        }

        public PopupWindowBuilder setWindowLayoutType(int layoutType) {
            mListPopupWindow.setWindowLayoutType(layoutType);
            return this;
        }

        public PopupWindowBuilder setOnItemClickListener(@Nullable AdapterView.OnItemClickListener clickListener) {
            mListPopupWindow.setOnItemClickListener(clickListener);
            return this;
        }

        public PopupWindowBuilder setOnItemSelectedListener(@Nullable AdapterView.OnItemSelectedListener selectedListener) {
            mListPopupWindow.setOnItemSelectedListener(selectedListener);
            return this;
        }

        public PopupWindowBuilder setView(@Nullable View view) {
            mListPopupWindow.setPromptView(view);
            return this;
        }

        public PopupWindowBuilder setOnDismissListener(@Nullable PopupWindow.OnDismissListener listener) {
            mListPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    SlcPopup.removeOperate(mKey);
                    if (listener != null) {
                        listener.onDismiss();
                    }
                }
            });
            return this;
        }

        @Override
        public PopupOperate create() {
            mListPopupWindow.setModal(mCancelable);
            PopupOperate popupOperate = new PopupOperate() {
                @Override
                public ListPopupWindow getListPopupWindow() {
                    return mListPopupWindow;
                }

                @Override
                public void postShow() {
                    mListPopupWindow.postShow();
                    SlcPopup.addOperate(mKey, this);
                }

                @Override
                public void dismiss() {
                    mListPopupWindow.dismiss();
                }

                @Override
                public void show() {
                    mListPopupWindow.show();
                    SlcPopup.addOperate(mKey, this);
                }

                @Override
                public boolean isCancelable() {
                    return mCancelable;
                }

                @Override
                public String getKey() {
                    return mKey;
                }
            };
            return popupOperate;
        }
    }

    public static class ShadowPopupWindowBuilder extends BaseBuilder<ShadowPopupWindowBuilder, BaseOperate> {
        private Context mContext;
        private FrameLayout mRootView;
        private FrameLayout mContentParentView;
        private boolean mIsEnsureContentParentGravity;
        private int mDuration = -1;
        private int mPositiveAnimRes = R.style.SlcShadowAnimDef;
        private int mNegativeAnimRes = R.style.SlcShadowAnimNegativeDef;
        protected Drawable mBgDrawable;
        protected int mMaxWidth, mMaxHeight;
        protected int mDirection;
        private OnContentViewInitListener mOnContentViewInitListener;

        public ShadowPopupWindowBuilder(@NonNull Context context) {
            super();
            this.mContext = context;
            init();
        }

        private void init() {
            mRootView = new FrameLayout(mContext);
            mRootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            mContentParentView = new FrameLayout(mContext);
            mContentParentView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            mRootView.addView(mContentParentView);
        }

        @Override
        public Context getContext() {
            return mContext;
        }

        public ShadowPopupWindowBuilder setAnimRes(@AnimRes int animRes) {
            this.mPositiveAnimRes = animRes;
            return this;
        }

        public ShadowPopupWindowBuilder setAnimNegativeAnimRes(@AnimRes int animRes) {
            this.mNegativeAnimRes = animRes;
            return this;
        }

        public ShadowPopupWindowBuilder setBgDrawable(Drawable drawable) {
            this.mBgDrawable = drawable;
            return this;
        }

        public ShadowPopupWindowBuilder setBgDrawableRes(int drawableRes) {
            setBgDrawable(mContext.getResources().getDrawable(drawableRes));
            return this;
        }

        @Deprecated
        @Override
        public ShadowPopupWindowBuilder setMaxHeight(int maxHeight) {
            this.mMaxHeight = maxHeight;
            return this;
        }

        @Deprecated
        @Override
        public ShadowPopupWindowBuilder setMaxWidth(int maxWidth) {
            this.mMaxWidth = maxWidth;
            return this;
        }

        @Deprecated
        @Override
        public ShadowPopupWindowBuilder setMaxWidthAndHeight(int maxWidth, int maxHeight) {
            setMaxHeight(maxHeight);
            setMaxWidth(maxWidth);
            return this;
        }

        public ShadowPopupWindowBuilder getLightDuration(int duration) {
            this.mDuration = duration;
            return this;
        }

        public ShadowPopupWindowBuilder setAnchor(@NonNull View anchor) {
            int anchorHeight = anchor.getHeight();
            int[] anchorLocation = new int[2];
            anchor.getLocationOnScreen(anchorLocation);
            int heightPixels = getScreenHeight(mContext);
            FrameLayout.LayoutParams contentParentViewLayoutParams =
                    (FrameLayout.LayoutParams) mContentParentView.getLayoutParams();
            if (anchorLocation[1] + anchorHeight / 2 <= heightPixels / 2) {
                contentParentViewLayoutParams.height = heightPixels - anchorLocation[1] - anchorHeight;
                contentParentViewLayoutParams.gravity = Gravity.BOTTOM;
                mDirection = Gravity.BOTTOM;
            } else {
                contentParentViewLayoutParams.height = anchorLocation[1];
                contentParentViewLayoutParams.gravity = Gravity.TOP;
                mDirection = Gravity.TOP;
            }
            mIsEnsureContentParentGravity = true;
            ensureContentViewGravity();
            return this;
        }

        /*public ShadowPopupWindowBuilder setAnchor(@NonNull View anchor, int xOff, int yOff) {
            setAnchor(anchor);
            return this;
        }*/
        public ShadowPopupWindowBuilder setContentView(@LayoutRes int contentLayout) {
            setContentView(LayoutInflater.from(mContext).inflate(contentLayout, null));
            return this;
        }

        public ShadowPopupWindowBuilder setContentView(@NonNull View contentView) {
            contentView.setOnClickListener(v -> {
            });
            mContentParentView.removeAllViews();
            mContentParentView.addView(contentView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            ensureContentViewGravity();
            return this;
        }

        protected void ensureContentViewGravity() {
            if (mContentParentView.getChildCount() != 0 && mIsEnsureContentParentGravity) {
                FrameLayout.LayoutParams contentViewLayoutParams =
                        (FrameLayout.LayoutParams) mContentParentView.getChildAt(0).getLayoutParams();
                FrameLayout.LayoutParams contentViewParentLayoutParams =
                        (FrameLayout.LayoutParams) mContentParentView.getLayoutParams();
                contentViewLayoutParams.gravity = contentViewParentLayoutParams.gravity == Gravity.BOTTOM ? Gravity.TOP :
                        Gravity.BOTTOM;
            }
        }

        public ShadowPopupWindowBuilder setContentViewInitListener(OnContentViewInitListener onContentViewInitListener) {
            this.mOnContentViewInitListener = onContentViewInitListener;
            return this;
        }

        @Override
        public BaseOperate create() {
            int[] attr = new int[]{android.R.attr.windowEnterAnimation, android.R.attr.windowEnterAnimation};
            TypedArray array = mContext.getTheme().obtainStyledAttributes(mDirection == Gravity.BOTTOM ? mNegativeAnimRes :
                    mPositiveAnimRes, attr);
            int enterAnimId = array.getResourceId(0, mDirection == Gravity.BOTTOM ? R.anim.top_shadow_in_def :
                    R.anim.bottom_shadow_in_def);
            int exitAnimId = array.getResourceId(1, mDirection == Gravity.BOTTOM ? R.anim.top_shadow_out_def :
                    R.anim.bottom_shadow_out_def);
            array.recycle();
            if (mBgDrawable != null) {
                mContentParentView.getChildAt(0).setBackground(mBgDrawable);
            }
            if (mOnContentViewInitListener != null) {
                mOnContentViewInitListener.onContentViewInit(mContentParentView.getChildAt(0));
            }
            return new BaseOperate() {
                @Override
                public void dismiss() {
                    SlcPopup.removeOperate(getKey());
                    exitAnim(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            ((ViewGroup) ((Activity) mContext).getWindow().getDecorView()).removeView(mRootView);
                        }
                    });
                }


                @Override
                public void show() {
                    mRootView.setOnClickListener(l -> {
                        dismiss();
                    });
                    mContentParentView.setOnClickListener(l -> {
                        if (isCancelable()) {
                            dismiss();
                        }
                    });
                    ((ViewGroup) ((Activity) mContext).getWindow().getDecorView()).addView(mRootView);
                    enterAnim();
                    SlcPopup.addOperate(getKey(), this);
                }

                @Override
                public boolean isCancelable() {
                    return mCancelable;
                }

                @Override
                public String getKey() {
                    return mKey;
                }

                private ValueAnimator enterAnim() {
                    Animation viewAnimation = AnimationUtils.loadAnimation(mContext, enterAnimId);
                    mContentParentView.getChildAt(0).setAnimation(viewAnimation);
                    ValueAnimator valueAnimator = ValueAnimator.ofInt(0x00000000, 0x80000000);
                    valueAnimator.setEvaluator(new ArgbEvaluator());
                    valueAnimator.setDuration(mDuration == -1 ? viewAnimation.getDuration() : mDuration);
                    valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
                    valueAnimator.addUpdateListener((ValueAnimator animation) -> {
                        mContentParentView.setBackgroundColor((Integer) animation.getAnimatedValue());
                    });
                    valueAnimator.start();
                    return valueAnimator;
                }

                private ValueAnimator exitAnim(Animator.AnimatorListener animationListener) {
                    Animation viewAnimation = AnimationUtils.loadAnimation(mContext, exitAnimId);
                    mContentParentView.getChildAt(0).startAnimation(viewAnimation);
                    ValueAnimator valueAnimator = ValueAnimator.ofInt(0x80000000, 0x00000000);
                    valueAnimator.setEvaluator(new ArgbEvaluator());
                    valueAnimator.setDuration(mDuration == -1 ? viewAnimation.getDuration() : mDuration);
                    valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
                    valueAnimator.addUpdateListener((ValueAnimator animation) -> {
                        mContentParentView.setBackgroundColor((Integer) animation.getAnimatedValue());
                    });
                    valueAnimator.addListener(animationListener);
                    valueAnimator.start();
                    return valueAnimator;
                }
            };
        }

        public interface OnContentViewInitListener {
            void onContentViewInit(View view);
        }
    }

    private static Map<Integer, PointF> offsetByViewHashCode = new HashMap<>();

    private static PointF getOffsetByViewHashCode(int hashCode) {
        PointF pointF = offsetByViewHashCode.get(hashCode);
        offsetByViewHashCode.remove(hashCode);
        return pointF;
    }

    @SuppressWarnings("All")
    public static void addAutoAnchor(View autoAnchor) {
        autoAnchor.setOnTouchListener((View v, MotionEvent event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                float x = event.getRawX();
                float y = event.getRawY();
                int[] location = new int[2];
                v.getLocationOnScreen(location);
                PointF pointF = new PointF((x - location[0]), y - location[1] - v.getHeight());
                offsetByViewHashCode.put(autoAnchor.hashCode(), pointF);
            }
            return false;
        });
    }

    public interface BaseOperate {
        void dismiss();

        void show();

        boolean isCancelable();

        String getKey();

    }

    public interface DialogOperate extends BaseOperate {
        Dialog getDialog();
    }

    public interface AlertDialogOperate extends DialogOperate {

    }

    public interface PopupOperate extends BaseOperate {
        ListPopupWindow getListPopupWindow();

        void postShow();
    }

}
