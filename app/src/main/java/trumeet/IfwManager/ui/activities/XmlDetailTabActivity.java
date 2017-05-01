package trumeet.IfwManager.ui.activities;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;

import com.blankj.utilcode.util.LogUtils;

import java.io.File;

import trumeet.IfwManager.BuildConfig;
import trumeet.IfwManager.R;
import trumeet.IfwManager.server.firewall.IntentFirewall;
import trumeet.IfwManager.ui.fragments.IFWBlockListFragment;

/**
 * Created by Trumeet on 2017/3/31.
 * @author Trumeet
 * @since 1.0
 */

public class XmlDetailTabActivity extends Activity {
    public static final String EXTRA_XML_FILE = XmlDetailTabActivity.class.getName()
             + ".EXTRA_XML_FILE";
    private static final String TAG = XmlDetailTabActivity.class.getSimpleName();
    public static final String TAG_TAB_RECEIVERS = "receivers";
    public static final String TAG_TAB_ACTIVITIES = "activities";
    public static final String TAG_TAB_SERVICES = "services";
    private IntentFirewall firewall;
    private File xml;

    private TabHost mTab;

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        xml = (File)getIntent().getSerializableExtra(EXTRA_XML_FILE);
        if (xml == null) {
            LogUtils.w(TAG, "XML file missing");
            finish();
        }
        setContentView(R.layout.fragment_xml_detail_tab);
        mTab = (TabHost) findViewById(R.id.tab);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle(xml.getName());
        new LoadRuleDetailTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        return super.onCreateOptionsMenu(menu);
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

    private void setupUI () {
        mTab.setup();
        final View frame = LayoutInflater.from(this).inflate(R.layout.frame
                , mTab.getTabContentView());
        mTab.addTab(mTab.newTabSpec(TAG_TAB_RECEIVERS)
                .setIndicator(getString(R.string.detail_tabs_receivers))
                .setContent(new TabHost.TabContentFactory() {
                    @Override
                    public View createTabContent(String tag) {
                        return frame;
                    }
                })
        );
        mTab.addTab(mTab.newTabSpec(TAG_TAB_ACTIVITIES)
                .setIndicator(getString(R.string.detail_tabs_activities))
                .setContent(new TabHost.TabContentFactory() {
                    @Override
                    public View createTabContent(String tag) {
                        return frame;
                    }
                })
        );
        mTab.addTab(mTab.newTabSpec(TAG_TAB_SERVICES)
                .setIndicator(getString(R.string.detail_tabs_services))
                .setContent(new TabHost.TabContentFactory() {
                    @Override
                    public View createTabContent(String tag) {
                        return frame;
                    }
                })
        );

        final IFWBlockListFragment fragment = new IFWBlockListFragment();
        getFragmentManager().beginTransaction()
                .replace(R.id.frame, fragment)
                .commitAllowingStateLoss();
        mTab.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                fragment.setMode(tabId);
            }
        });
        //fragment.setMode(XmlDetailTabActivity.TAG_TAB_RECEIVERS);
        mTab.post(new Runnable() {
            @Override
            public void run() {
                mTab.setCurrentTab(0);
            }
        });
    }

    private class LoadRuleDetailTask extends AsyncTask<Void, String, Throwable> {
        @Override
        protected void onPreExecute () {
        }

        @Override
        protected Throwable doInBackground(Void... params) {
            try {
                if (firewall != null)
                    return null;
                firewall = new IntentFirewall();
                firewall.readRules(xml);
            } catch (Throwable e) {
                return e;
            }
            return null;
        }

        @Override
        protected void onPostExecute (Throwable param) {
            if (param != null) {
                LogUtils.e(TAG, param);
                if (BuildConfig.DEBUG) param.printStackTrace();
                return;
            }
            setupUI();
        }
    }

    public IntentFirewall getFirewall () {
        return firewall;
    }
}
