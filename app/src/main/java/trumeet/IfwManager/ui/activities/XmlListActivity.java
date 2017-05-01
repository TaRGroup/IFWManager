package trumeet.IfwManager.ui.activities;

import android.os.Bundle;

import trumeet.IfwManager.ui.fragments.XmlListFragment;

/**
 * Created by Trumeet on 2017/3/27.
 * @author Trumeet
 * @since 1.0
 */

public class XmlListActivity extends SimpleFragmentActivity {
    private boolean mShowedDetailFragment = false;
    private XmlListFragment mXmlListFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            mXmlListFragment = new XmlListFragment();
            turnFragment(mXmlListFragment);
        }
    }
}
