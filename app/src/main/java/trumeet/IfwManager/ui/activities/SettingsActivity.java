package trumeet.IfwManager.ui.activities;

import android.os.Bundle;
import android.view.MenuItem;

import trumeet.IfwManager.ui.fragments.SettingsFragment;
import trumeet.IfwManager.ui.fragments.settings.DeveloperOptionsFragment;
import trumeet.IfwManager.ui.fragments.settings.SettingsMainFragment;

/**
 * Created by Trumeet on 2017/4/1.
 * @author Trumeet
 * @since 1.0
 */

public class SettingsActivity extends SimpleFragmentActivity {
    public static final String FRAGMENT_DEVELOPER_OPTIONS = "developer_options";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String fragmentName = getIntent().getAction();
        SettingsFragment fragment;
        if (fragmentName != null) {
            switch (fragmentName) {
                case FRAGMENT_DEVELOPER_OPTIONS :
                    fragment = new DeveloperOptionsFragment();
                    break;
                default:
                    fragment = new SettingsMainFragment();
                    break;
            }
        } else {
            fragment = new SettingsMainFragment();
        }
        turnFragment(fragment);
        setTitle(fragment.getTitle(this));
        getActionBar().setDisplayHomeAsUpEnabled(true);
        showProgress(false, null);
    }
    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
