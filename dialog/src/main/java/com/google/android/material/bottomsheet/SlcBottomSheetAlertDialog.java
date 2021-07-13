//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.google.android.material.bottomsheet;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemSelectedListener;

import androidx.annotation.AnimRes;
import androidx.annotation.ArrayRes;
import androidx.annotation.AttrRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.annotation.StringRes;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.SlcAlertController;
import androidx.appcompat.app.AppCompatDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;

import android.slc.slcdialog.R;

import java.lang.ref.WeakReference;

@Deprecated
public class SlcBottomSheetAlertDialog extends AppCompatDialog implements DialogInterface {
    static final int LAYOUT_HINT_NONE = 0;
    static final int LAYOUT_HINT_SIDE = 1;
    final SlcAlertController mAlert;
    private Drawable mWindowBackground;
    private BottomSheetBehavior<FrameLayout> behavior;
    boolean cancelable;
    boolean isHideable;
    private boolean canceledOnTouchOutside;
    private boolean canceledOnTouchOutsideSet;
    private int animRes;
    private SlcBottomSheetAlertDialogCallback bottomSheetCallback;

    protected SlcBottomSheetAlertDialog(@NonNull Context context) {
        this(context, 0);
    }

    protected SlcBottomSheetAlertDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, resolveDialogTheme(context, themeResId));
        this.cancelable = true;
        this.isHideable = true;
        this.canceledOnTouchOutside = true;
        setBottomSheetCallback(new SlcBottomSheetAlertDialogCallback());
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        this.animRes = R.style.Animation_MaterialComponents_BottomSheetDialog_SlcPopup;
        this.mAlert = new SlcAlertController(this.getContext(), this, this.getWindow());
    }

    protected SlcBottomSheetAlertDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        this(context, 0);
        this.setCancelable(cancelable);
        this.setOnCancelListener(cancelListener);
        this.canceledOnTouchOutside = true;
        setBottomSheetCallback(new SlcBottomSheetAlertDialogCallback());
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        this.cancelable = cancelable;
        this.isHideable = true;
    }

    public static class SlcBottomSheetAlertDialogCallback extends BottomSheetBehavior.BottomSheetCallback {
        protected WeakReference<SlcBottomSheetAlertDialog> sheetAlertDialogWr;

        public SlcBottomSheetAlertDialogCallback() {
        }

        public void setSheetAlertDialogWr(SlcBottomSheetAlertDialog sheetAlertDialogWr) {
            this.sheetAlertDialogWr = new WeakReference<>(sheetAlertDialogWr);
        }

        public void onStateChanged(@NonNull View bottomSheet, @BottomSheetBehavior.State int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                SlcBottomSheetAlertDialog slcBottomSheetAlertDialog = sheetAlertDialogWr.get();
                if (slcBottomSheetAlertDialog != null) {
                    slcBottomSheetAlertDialog.cancel();
                }
            }
        }

        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();
        if (window != null) {
            window.setWindowAnimations(animRes);
            mWindowBackground = window.getDecorView().getBackground();
            if (mWindowBackground instanceof InsetDrawable) {
                InsetDrawable insetWindowBackground = (InsetDrawable) mWindowBackground;
                mWindowBackground = insetWindowBackground.getDrawable();
            }
            window.getDecorView().setBackground(new ColorDrawable(Color.TRANSPARENT));
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.getDecorView().setPadding(0, 0, 0, 0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            }
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
        this.mAlert.installContent();
    }

    public Button getButton(int whichButton) {
        return this.mAlert.getButton(whichButton);
    }

    public ListView getListView() {
        return this.mAlert.getListView();
    }

    @Override
    public void setCancelable(boolean cancelable) {
        super.setCancelable(cancelable);
        this.cancelable = cancelable;
    }

    public void setHideable(boolean hideable) {
        isHideable = hideable;
    }

    public void setTitle(CharSequence title) {
        super.setTitle(title);
        this.mAlert.setTitle(title);
    }

    public void setCustomTitle(View customTitleView) {
        this.mAlert.setCustomTitle(customTitleView);
    }

    public void setMessage(CharSequence message) {
        this.mAlert.setMessage(message);
    }

    public void setView(View view) {
        this.mAlert.setView(view);
    }

    public void setView(View view, int viewSpacingLeft, int viewSpacingTop, int viewSpacingRight, int viewSpacingBottom) {
        this.mAlert.setView(view, viewSpacingLeft, viewSpacingTop, viewSpacingRight, viewSpacingBottom);
    }

    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    void setButtonPanelLayoutHint(int layoutHint) {
        this.mAlert.setButtonPanelLayoutHint(layoutHint);
    }

    public void setButton(int whichButton, CharSequence text, Message msg) {
        this.mAlert.setButton(whichButton, text, (OnClickListener) null, msg, (Drawable) null);
    }

    public void setButton(int whichButton, CharSequence text, OnClickListener listener) {
        this.mAlert.setButton(whichButton, text, listener, (Message) null, (Drawable) null);
    }

    public void setButton(int whichButton, CharSequence text, Drawable icon, OnClickListener listener) {
        this.mAlert.setButton(whichButton, text, listener, (Message) null, icon);
    }

    public void setIcon(int resId) {
        this.mAlert.setIcon(resId);
    }

    public void setIcon(Drawable icon) {
        this.mAlert.setIcon(icon);
    }

    public void setIconAttribute(int attrId) {
        TypedValue out = new TypedValue();
        this.getContext().getTheme().resolveAttribute(attrId, out, true);
        this.mAlert.setIcon(out.resourceId);
    }

    public void setAnimRes(int animRes) {
        if (animRes != 0) {
            this.animRes = animRes;
        }
    }

    public void setBottomSheetCallback(SlcBottomSheetAlertDialogCallback bottomSheetCallback) {
        this.bottomSheetCallback = bottomSheetCallback;
        if (this.bottomSheetCallback != null) {
            this.bottomSheetCallback.setSheetAlertDialogWr(this);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return this.mAlert.onKeyDown(keyCode, event) ? true : super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return this.mAlert.onKeyUp(keyCode, event) ? true : super.onKeyUp(keyCode, event);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResId) {
        super.setContentView(this.wrapInBottomSheet(layoutResId, (View) null, (ViewGroup.LayoutParams) null));
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(this.wrapInBottomSheet(0, view, (ViewGroup.LayoutParams) null));
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(this.wrapInBottomSheet(0, view, params));
    }

    private View wrapInBottomSheet(int layoutResId, View view, ViewGroup.LayoutParams params) {
        FrameLayout container = (FrameLayout) View.inflate(this.getContext(), R.layout.slc_popup_design_bottom_sheet_dialog, (ViewGroup) null);
        CoordinatorLayout coordinator = container.findViewById(R.id.coordinator);
        if (layoutResId != 0 && view == null) {
            view = this.getLayoutInflater().inflate(layoutResId, coordinator, false);
        }
        FrameLayout bottomSheet = coordinator.findViewById(R.id.design_bottom_sheet);
        bottomSheet.setBackground(mWindowBackground);
        this.behavior = BottomSheetBehavior.from(bottomSheet);
        this.behavior.addBottomSheetCallback(this.bottomSheetCallback);
        this.behavior.setHideable(this.isHideable);
        if (params == null) {
            bottomSheet.addView(view);
        } else {
            bottomSheet.addView(view, params);
        }

        coordinator.findViewById(R.id.touch_outside).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SlcBottomSheetAlertDialog.this.cancelable && SlcBottomSheetAlertDialog.this.isShowing() && SlcBottomSheetAlertDialog.this.shouldWindowCloseOnTouchOutside()) {
                    SlcBottomSheetAlertDialog.this.cancel();
                }
            }
        });
        ViewCompat.setAccessibilityDelegate(bottomSheet, new AccessibilityDelegateCompat() {
            public void onInitializeAccessibilityNodeInfo(View host, AccessibilityNodeInfoCompat info) {
                super.onInitializeAccessibilityNodeInfo(host, info);
                if (SlcBottomSheetAlertDialog.this.cancelable) {
                    info.addAction(AccessibilityNodeInfoCompat.ACTION_DISMISS);
                    info.setDismissable(true);
                } else {
                    info.setDismissable(false);
                }
            }

            public boolean performAccessibilityAction(View host, int action, Bundle args) {
                if (action == AccessibilityNodeInfoCompat.ACTION_DISMISS && SlcBottomSheetAlertDialog.this.cancelable) {
                    SlcBottomSheetAlertDialog.this.cancel();
                    return true;
                } else {
                    return super.performAccessibilityAction(host, action, args);
                }
            }
        });
        bottomSheet.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent event) {
                return true;
            }
        });
        return container;
    }

    boolean shouldWindowCloseOnTouchOutside() {
        if (!this.canceledOnTouchOutsideSet) {
            TypedArray a = this.getContext().obtainStyledAttributes(new int[]{16843611});
            this.canceledOnTouchOutside = a.getBoolean(0, true);
            a.recycle();
            this.canceledOnTouchOutsideSet = true;
        }

        return this.canceledOnTouchOutside;
    }

    /*private static int resolveDialogTheme(Context context, int themeId) {
        if (themeId == 0) {
            TypedValue outValue = new TypedValue();
            if (context.getTheme().resolveAttribute(android.support.design.R.attr.bottomSheetDialogTheme, outValue, true)) {
                themeId = outValue.resourceId;
            } else {
                themeId = R.style.Theme_AppCompat_Light_BottomSheet_Dialog_Alert1;
            }
        }

        return themeId;
    }*/

    static int resolveDialogTheme(@NonNull Context context, @StyleRes int resid) {
        if ((resid >>> 24 & 255) >= 1) {
            return resid;
        } else {
            TypedValue outValue = new TypedValue();
            context.getTheme().resolveAttribute(androidx.appcompat.R.attr.alertDialogTheme, outValue, true);
            return outValue.resourceId;
        }
    }

    public static class Builder {
        private final SlcAlertController.AlertParams P;
        private final int mTheme;
        private int mAnimRes;
        private boolean mHideable = true;
        private SlcBottomSheetAlertDialogCallback mBottomSheetCallback = new SlcBottomSheetAlertDialogCallback();


        public Builder(@NonNull Context context) {
            this(context, SlcBottomSheetAlertDialog.resolveDialogTheme(context, 0));
        }

        public Builder(@NonNull Context context, @StyleRes int themeResId) {
            this.P = new SlcAlertController.AlertParams(new ContextThemeWrapper(context, SlcBottomSheetAlertDialog.resolveDialogTheme(context, themeResId)));
            this.mTheme = themeResId;
        }

        @NonNull
        public Context getContext() {
            return this.P.mContext;
        }

        public Builder setHideable(boolean hideable) {
            this.mHideable = hideable;
            return this;
        }

        public Builder setTitle(@StringRes int titleId) {
            this.P.mTitle = this.P.mContext.getText(titleId);
            return this;
        }

        public Builder setTitle(@Nullable CharSequence title) {
            this.P.mTitle = title;
            return this;
        }

        public Builder setCustomTitle(@Nullable View customTitleView) {
            this.P.mCustomTitleView = customTitleView;
            return this;
        }

        public Builder setMessage(@StringRes int messageId) {
            this.P.mMessage = this.P.mContext.getText(messageId);
            return this;
        }

        public Builder setMessage(@Nullable CharSequence message) {
            this.P.mMessage = message;
            return this;
        }

        public Builder setIcon(@DrawableRes int iconId) {
            this.P.mIconId = iconId;
            return this;
        }

        public Builder setIcon(@Nullable Drawable icon) {
            this.P.mIcon = icon;
            return this;
        }

        public Builder setIconAttribute(@AttrRes int attrId) {
            TypedValue out = new TypedValue();
            this.P.mContext.getTheme().resolveAttribute(attrId, out, true);
            this.P.mIconId = out.resourceId;
            return this;
        }

        public Builder setAnimRes(@AnimRes int animRes) {
            this.mAnimRes = animRes;
            return this;
        }

        public Builder setBottomSheetCallback(SlcBottomSheetAlertDialogCallback bottomSheetCallback) {
            this.mBottomSheetCallback = bottomSheetCallback;
            return this;
        }

        public Builder setPositiveButton(@StringRes int textId, OnClickListener listener) {
            this.P.mPositiveButtonText = this.P.mContext.getText(textId);
            this.P.mPositiveButtonListener = listener;
            return this;
        }

        public Builder setPositiveButton(CharSequence text, OnClickListener listener) {
            this.P.mPositiveButtonText = text;
            this.P.mPositiveButtonListener = listener;
            return this;
        }

        public Builder setPositiveButtonIcon(Drawable icon) {
            this.P.mPositiveButtonIcon = icon;
            return this;
        }

        public Builder setNegativeButton(@StringRes int textId, OnClickListener listener) {
            this.P.mNegativeButtonText = this.P.mContext.getText(textId);
            this.P.mNegativeButtonListener = listener;
            return this;
        }

        public Builder setNegativeButton(CharSequence text, OnClickListener listener) {
            this.P.mNegativeButtonText = text;
            this.P.mNegativeButtonListener = listener;
            return this;
        }

        public Builder setNegativeButtonIcon(Drawable icon) {
            this.P.mNegativeButtonIcon = icon;
            return this;
        }

        public Builder setNeutralButton(@StringRes int textId, OnClickListener listener) {
            this.P.mNeutralButtonText = this.P.mContext.getText(textId);
            this.P.mNeutralButtonListener = listener;
            return this;
        }

        public Builder setNeutralButton(CharSequence text, OnClickListener listener) {
            this.P.mNeutralButtonText = text;
            this.P.mNeutralButtonListener = listener;
            return this;
        }

        public Builder setNeutralButtonIcon(Drawable icon) {
            this.P.mNeutralButtonIcon = icon;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            this.P.mCancelable = cancelable;
            return this;
        }

        public Builder setOnCancelListener(OnCancelListener onCancelListener) {
            this.P.mOnCancelListener = onCancelListener;
            return this;
        }

        public Builder setOnDismissListener(OnDismissListener onDismissListener) {
            this.P.mOnDismissListener = onDismissListener;
            return this;
        }

        public Builder setOnKeyListener(OnKeyListener onKeyListener) {
            this.P.mOnKeyListener = onKeyListener;
            return this;
        }

        public Builder setItems(@ArrayRes int itemsId, OnClickListener listener) {
            this.P.mItems = this.P.mContext.getResources().getTextArray(itemsId);
            this.P.mOnClickListener = listener;
            return this;
        }

        public Builder setItems(CharSequence[] items, OnClickListener listener) {
            this.P.mItems = items;
            this.P.mOnClickListener = listener;
            return this;
        }

        public Builder setAdapter(ListAdapter adapter, OnClickListener listener) {
            this.P.mAdapter = adapter;
            this.P.mOnClickListener = listener;
            return this;
        }

        public Builder setCursor(Cursor cursor, OnClickListener listener, String labelColumn) {
            this.P.mCursor = cursor;
            this.P.mLabelColumn = labelColumn;
            this.P.mOnClickListener = listener;
            return this;
        }

        public Builder setMultiChoiceItems(@ArrayRes int itemsId, boolean[] checkedItems, OnMultiChoiceClickListener listener) {
            this.P.mItems = this.P.mContext.getResources().getTextArray(itemsId);
            this.P.mOnCheckboxClickListener = listener;
            this.P.mCheckedItems = checkedItems;
            this.P.mIsMultiChoice = true;
            return this;
        }

        public Builder setMultiChoiceItems(CharSequence[] items, boolean[] checkedItems, OnMultiChoiceClickListener listener) {
            this.P.mItems = items;
            this.P.mOnCheckboxClickListener = listener;
            this.P.mCheckedItems = checkedItems;
            this.P.mIsMultiChoice = true;
            return this;
        }

        public Builder setMultiChoiceItems(Cursor cursor, String isCheckedColumn, String labelColumn, OnMultiChoiceClickListener listener) {
            this.P.mCursor = cursor;
            this.P.mOnCheckboxClickListener = listener;
            this.P.mIsCheckedColumn = isCheckedColumn;
            this.P.mLabelColumn = labelColumn;
            this.P.mIsMultiChoice = true;
            return this;
        }

        public Builder setSingleChoiceItems(@ArrayRes int itemsId, int checkedItem, OnClickListener listener) {
            this.P.mItems = this.P.mContext.getResources().getTextArray(itemsId);
            this.P.mOnClickListener = listener;
            this.P.mCheckedItem = checkedItem;
            this.P.mIsSingleChoice = true;
            return this;
        }

        public Builder setSingleChoiceItems(Cursor cursor, int checkedItem, String labelColumn, OnClickListener listener) {
            this.P.mCursor = cursor;
            this.P.mOnClickListener = listener;
            this.P.mCheckedItem = checkedItem;
            this.P.mLabelColumn = labelColumn;
            this.P.mIsSingleChoice = true;
            return this;
        }

        public Builder setSingleChoiceItems(CharSequence[] items, int checkedItem, OnClickListener listener) {
            this.P.mItems = items;
            this.P.mOnClickListener = listener;
            this.P.mCheckedItem = checkedItem;
            this.P.mIsSingleChoice = true;
            return this;
        }

        public Builder setSingleChoiceItems(ListAdapter adapter, int checkedItem, OnClickListener listener) {
            this.P.mAdapter = adapter;
            this.P.mOnClickListener = listener;
            this.P.mCheckedItem = checkedItem;
            this.P.mIsSingleChoice = true;
            return this;
        }

        public Builder setOnItemSelectedListener(OnItemSelectedListener listener) {
            this.P.mOnItemSelectedListener = listener;
            return this;
        }

        public Builder setView(int layoutResId) {
            this.P.mView = null;
            this.P.mViewLayoutResId = layoutResId;
            this.P.mViewSpacingSpecified = false;
            return this;
        }

        public Builder setView(View view) {
            this.P.mView = view;
            this.P.mViewLayoutResId = 0;
            this.P.mViewSpacingSpecified = false;
            return this;
        }

        /**
         * @deprecated
         */
        @Deprecated
        @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
        public SlcBottomSheetAlertDialog.Builder setView(View view, int viewSpacingLeft, int viewSpacingTop, int viewSpacingRight, int viewSpacingBottom) {
            this.P.mView = view;
            this.P.mViewLayoutResId = 0;
            this.P.mViewSpacingSpecified = true;
            this.P.mViewSpacingLeft = viewSpacingLeft;
            this.P.mViewSpacingTop = viewSpacingTop;
            this.P.mViewSpacingRight = viewSpacingRight;
            this.P.mViewSpacingBottom = viewSpacingBottom;
            return this;
        }

        /**
         * @deprecated
         */
        @Deprecated
        public Builder setInverseBackgroundForced(boolean useInverseBackground) {
            this.P.mForceInverseBackground = useInverseBackground;
            return this;
        }

        @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
        public SlcBottomSheetAlertDialog.Builder setRecycleOnMeasureEnabled(boolean enabled) {
            this.P.mRecycleOnMeasure = enabled;
            return this;
        }

        public SlcBottomSheetAlertDialog create() {
            SlcBottomSheetAlertDialog dialog = new SlcBottomSheetAlertDialog(this.P.mContext, this.mTheme);
            dialog.setHideable(mHideable);
            dialog.setAnimRes(mAnimRes);
            this.P.apply(dialog.mAlert);
            dialog.setCancelable(this.P.mCancelable);
            if (this.P.mCancelable) {
                dialog.setCanceledOnTouchOutside(true);
            }
            dialog.setBottomSheetCallback(mBottomSheetCallback);
            dialog.setOnCancelListener(this.P.mOnCancelListener);
            dialog.setOnDismissListener(this.P.mOnDismissListener);
            if (this.P.mOnKeyListener != null) {
                dialog.setOnKeyListener(this.P.mOnKeyListener);
            }
            return dialog;
        }

        public SlcBottomSheetAlertDialog show() {
            SlcBottomSheetAlertDialog dialog = this.create();
            dialog.show();
            return dialog;
        }
    }
}
