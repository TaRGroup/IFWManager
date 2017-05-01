package trumeet.IfwManager.ui.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.blankj.utilcode.util.ClipboardUtils;
import com.blankj.utilcode.util.LogUtils;

import trumeet.IfwManager.R;
import trumeet.IfwManager.models.RuleItem;

/**
 * Created by Trumeet on 2017/4/1.
 * @author Trumeet
 * @since 1.0
 */

public class RuleDetailFragment extends DialogFragment {
    private static final String TAG = RuleDetailFragment.class.getSimpleName();
    private static final String EXTRA_RULE = RuleDetailFragment.class.getName()
             + ".EXTRA_RULE";

    private RuleItem mRule;

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRule = getArguments().getParcelable(EXTRA_RULE);
        if (mRule == null) {
            LogUtils.w(TAG, "Missing rule");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // The only reason you might override this method when using onCreateView() is
        // to modify any dialog characteristics. For example, the dialog includes a
        // title by default, but your custom layout might not need it. So here you can
        // remove the dialog title, but you must call the superclass to get the Dialog.
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    public static RuleDetailFragment newInstance (RuleItem ruleItem) {
        RuleDetailFragment fragment = new RuleDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_RULE, ruleItem);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (mRule == null) {
            return null;
        }
        Dialog dialog = getDialog();
        if (dialog != null) dialog.setCanceledOnTouchOutside(false);
        View view = inflater.inflate(R.layout.dialog_rule_detail, container, false);
        final EditText editText = (EditText) view.findViewById(R.id.edit_action);
        editText.setText(mRule.getDetail());
        RadioGroup type = (RadioGroup) view.findViewById(R.id.radio_group);
        type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.radio_type_action :
                        editText.setHint(R.string.rule_detail_type_action);
                        break;
                    case R.id.radio_type_categories :
                        editText.setHint(R.string.rule_detail_type_categories);
                        break;
                }
            }
        });
        switch (mRule.getMode()) {
            case RuleItem.MODE_ACTION :
                type.check(R.id.radio_type_action);
                break;
            case RuleItem.MODE_CATEGORIES :
                type.check(R.id.radio_type_categories);
                break;
        }
        view.findViewById(R.id.button_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        view.findViewById(R.id.button_copy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardUtils.copyText(editText.getText());
            }
        });
        view.findViewById(R.id.button_paste).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText(ClipboardUtils.getText());
            }
        });
        return view;
    }
}
