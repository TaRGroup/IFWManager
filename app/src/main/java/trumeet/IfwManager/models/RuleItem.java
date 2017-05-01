package trumeet.IfwManager.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Pair;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by Trumeet on 2017/3/27.
 * A rule item
 * @author Trumeet
 * @since 1.0
 */

public class RuleItem implements MultiItemEntity, Parcelable {
    public static final String MODE_ACTION = "action";
    public static final String MODE_CATEGORIES = "categories";

    public static final int TYPE_RULE_GROUP = 0;
    public static final int TYPE_RULE = 1;
    private String mode = MODE_ACTION;
    private String detail;

    public RuleItem () {
        // Empty
    }

    protected RuleItem(Parcel in) {
        mode = in.readString();
        detail = in.readString();
    }

    public static final Creator<RuleItem> CREATOR = new Creator<RuleItem>() {
        @Override
        public RuleItem createFromParcel(Parcel in) {
            return new RuleItem(in);
        }

        @Override
        public RuleItem[] newArray(int size) {
            return new RuleItem[size];
        }
    };

    @Override
    public int getItemType() {
        return TYPE_RULE;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mode);
        dest.writeString(detail);
    }
}
