package trumeet.IfwManager.ui.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.blankj.utilcode.util.LogUtils;

import java.io.File;
import java.io.FilenameFilter;

import trumeet.IfwManager.BuildConfig;
import trumeet.IfwManager.R;
import trumeet.IfwManager.ui.activities.SettingsActivity;
import trumeet.IfwManager.ui.activities.XmlDetailTabActivity;
import trumeet.IfwManager.ui.activities.XmlListActivity;
import trumeet.IfwManager.workspace.Workspace;

/**
 * Created by Trumeet on 2017/3/31.
 * Show XML List
 * @author Trumeet
 * @since 1.0
 */

public class XmlListFragment extends PreferenceFragment {
    private static final String TAG = XmlListFragment.class.getSimpleName();

    @Override
    public void onCreate (Bundle bundle) {
        super.onCreate(bundle);
        setHasOptionsMenu(true);
        new InitWorkspaceTask().execute();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings :
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private XmlListActivity getRootActivity () {
        return (XmlListActivity) getActivity();
    }

    private class InitWorkspaceTask extends AsyncTask<Void, String, Throwable> {
        private PreferenceScreen screen;
        private boolean hasXml = false;
        InitWorkspaceTask () {
            screen = getPreferenceScreen();
            if (screen == null) {
                screen = getPreferenceManager().createPreferenceScreen(getActivity());
            } else {
                screen.removeAll();
            }
        }
        @Override
        protected void onPreExecute () {
            getRootActivity().setText(null);
            getRootActivity().showProgress(true, getString(R.string.workspace_init));
        }

        @Override
        protected Throwable doInBackground(Void... params) {
            try {
                Workspace workspace = Workspace.getCurrent();
                if (workspace == null) {
                    workspace = new Workspace(Workspace.ID_CURRENT
                            , getActivity());
                    workspace.init();
                    workspace.fillBySystemProfile();
                    Workspace.setCurrent(workspace);
                }
                File[] files = workspace.getRuleDir().listFiles(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        return name.endsWith(".xml");
                    }
                });
                if (files == null) {
                    return null;
                }
                for (final File file : files) {
                    Preference preference = new Preference(getActivity());
                    preference.setTitle(file.getName());
                    preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                        @Override
                        public boolean onPreferenceClick(Preference preference) {
                            // TODO: Show dialog?
                            startActivity(new Intent(getActivity(),
                                    XmlDetailTabActivity.class)
                            .putExtra(XmlDetailTabActivity.EXTRA_XML_FILE,
                                    file));
                            return false;
                        }
                    });
                    screen.addPreference(preference);
                    hasXml = true;
                }
            } catch (Throwable e) {
                return e;
            }
            return null;
        }

        @Override
        protected void onPostExecute (Throwable param) {
            getRootActivity().showProgress(false, null);
            if (param != null) {
                LogUtils.e(TAG, param);
                if (BuildConfig.DEBUG) param.printStackTrace();
                getRootActivity().setTextColor(Color.RED);
                getRootActivity().setText(getString(R.string.workspace_init_error));
                return;
            }
            if (screen == null || !hasXml) {
                getRootActivity().setTextColor(Color.BLACK);
                getRootActivity().setText(getString(R.string.empty));
            } else {
                setPreferenceScreen(screen);
            }
        }
    }
}
