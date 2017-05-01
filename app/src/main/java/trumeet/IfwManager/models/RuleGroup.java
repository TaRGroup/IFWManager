package trumeet.IfwManager.models;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by Trumeet on 2017/3/31.
 * A rule group, include may rules.
 * @author Trumeet
 * @since 1.0
 */

public class RuleGroup extends AbstractExpandableItem<RuleItem> implements MultiItemEntity {

    @Override
    public int getLevel() {
        return 0;
    }

    private boolean enable;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    @Override
    public int getItemType() {
        return RuleItem.TYPE_RULE_GROUP;
    }
}
