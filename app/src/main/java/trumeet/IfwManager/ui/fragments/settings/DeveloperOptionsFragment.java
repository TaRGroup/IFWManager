package trumeet.IfwManager.ui.fragments.settings;

import android.content.Context;
import android.os.Bundle;

import trumeet.IfwManager.R;
import trumeet.IfwManager.ui.fragments.SettingsFragment;

/**
 * Created by Trumeet on 2017/4/2.
 */

public class DeveloperOptionsFragment extends SettingsFragment {
    /**
     * Preference file were development settings prefs are stored.
     */
    public static final String PREF_FILE = "development";

    /**
     * Whether to show the development settings to the user.  Default is false.
     */
    public static final String PREF_SHOW = "show";

    @Override
    public String getTitle(Context context) {
        return context.getString(R.string.developer_title);
    }
}
