package trumeet.IfwManager.ui.fragments.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceCategory;
import android.preference.Preference;

import com.blankj.utilcode.util.AppUtils;

import trumeet.IfwManager.R;
import trumeet.IfwManager.ui.activities.SettingsActivity;
import trumeet.IfwManager.ui.fragments.SettingsFragment;

/**
 * Created by Trumeet on 2017/4/1.
 * Main settings fragment
 * @see trumeet.IfwManager.R.xml.settings_main
 * @author Trumeet
 */

public class SettingsMainFragment extends SettingsFragment implements Preference.OnPreferenceClickListener{
    private static final String KEY_ABOUT = "about";
    private static final String KEY_ABOUT_VERSION = "about_version";
    private static final String KEY_DEVELOPMENT_OPTIONS = "development";

    static final int TAPS_TO_BE_A_DEVELOPER = 7;
    int mDevHitCountdown;
    private boolean mRemovedDeveloperOptions = false;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        addPreferencesFromResource(R.xml.settings_main);
        // About Version Preference
        Preference aboutVersion = findPreference(KEY_ABOUT_VERSION);
        aboutVersion.setSummary(AppUtils.getAppVersionName(getActivity()));
        aboutVersion.setOnPreferenceClickListener(this);
    }

    @Override
    public String getTitle(Context context) {
        return context.getString(R.string.settings_title);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        switch (preference.getKey()) {
            case KEY_ABOUT_VERSION :
                if (mDevHitCountdown > 0) {
                    mDevHitCountdown--;
                    if (mDevHitCountdown == 0) {
                        getActivity().getSharedPreferences(DeveloperOptionsFragment.PREF_FILE,
                                Context.MODE_PRIVATE).edit().putBoolean(
                                DeveloperOptionsFragment.PREF_SHOW, true).apply();
                        // This is good time to index the Developer Options
                        startActivity(new Intent(getActivity(), SettingsActivity.class));
                        getActivity().finish();
                    }
                }
                return true;
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        mDevHitCountdown = getActivity().getSharedPreferences(DeveloperOptionsFragment.PREF_FILE,
                Context.MODE_PRIVATE).getBoolean(DeveloperOptionsFragment.PREF_SHOW,
                false) ? -1 : TAPS_TO_BE_A_DEVELOPER;
        if (mDevHitCountdown != -1 && !mRemovedDeveloperOptions) {
            ((PreferenceCategory)findPreference(KEY_ABOUT)).removePreference(
                    findPreference(KEY_DEVELOPMENT_OPTIONS)
            );
            mRemovedDeveloperOptions = true;
        }
    }
}
