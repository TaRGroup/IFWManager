package trumeet.IfwManager.ui.fragments;

import android.content.Context;
import android.preference.PreferenceFragment;

/**
 * Created by Trumeet on 2017/4/2.
 * @author Trumeet
 * @since 1.0
 */

public abstract class SettingsFragment extends PreferenceFragment {
    public abstract String getTitle(Context context);
}
