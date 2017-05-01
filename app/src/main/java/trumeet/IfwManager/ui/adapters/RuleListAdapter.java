package trumeet.IfwManager.ui.adapters;

import android.widget.Switch;
import android.widget.TextView;

import com.blankj.utilcode.util.Utils;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

import trumeet.IfwManager.R;
import trumeet.IfwManager.models.RuleGroup;
import trumeet.IfwManager.models.RuleItem;

/**
 * Created by Trumeet on 2017/3/27.
 */

public class RuleListAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {
    public RuleListAdapter (List<MultiItemEntity> rules) {
        super(rules);
        addItemType(RuleItem.TYPE_RULE_GROUP, R.layout.group);
        addItemType(RuleItem.TYPE_RULE, R.layout.item);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiItemEntity item) {
        switch (item.getItemType()) {
            case RuleItem.TYPE_RULE:
                RuleItem ruleItem = (RuleItem) item;
                TextView title = helper.getView(R.id.text1);
                title.setText(ruleItem.getDetail());
                break;
            case RuleItem.TYPE_RULE_GROUP:
                RuleGroup group = (RuleGroup) item;
                Switch enable = helper.getView(R.id.switch_enable);
                enable.setChecked(group.isEnable());
                TextView summary = helper.getView(android.R.id.title);
                summary.setText(Utils.getContext().getString(
                        R.string.block_list_group_summary,
                        String.valueOf(group.getSubItems().size())
                ));
                break;
        }
    }
}
