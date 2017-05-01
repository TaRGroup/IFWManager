package trumeet.IfwManager.ui.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import trumeet.IfwManager.R;
import trumeet.IfwManager.models.RuleGroup;
import trumeet.IfwManager.models.RuleItem;
import trumeet.IfwManager.ui.activities.XmlDetailTabActivity;
import trumeet.IfwManager.ui.adapters.RuleListAdapter;
import trumeet.IfwManager.server.firewall.IntentFirewall;

/**
 * Created by Trumeet on 2017/3/31.
 * @author Trumeet
 * @since 1.0
 */

public class IFWBlockListFragment extends Fragment {
    private static final String TAG = IFWBlockListFragment.class.getSimpleName();
    private IntentFirewall firewall;
    private RuleListAdapter mAdapter;
    private List<MultiItemEntity> mRuleList;

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firewall = getRootActivity().getFirewall();
        if (firewall == null) {
            LogUtils.w(TAG, "Missing firewall");
            getActivity().finish();
            return;
        }
        mRuleList = new ArrayList<>();
        mAdapter = new RuleListAdapter(mRuleList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup,
                             Bundle savedInstanceState) {
        RecyclerView recyclerView = new RecyclerView(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mAdapter.bindToRecyclerView(recyclerView);
        mAdapter.setEmptyView(R.layout.layout_empty);
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                MultiItemEntity entity = mAdapter.getItem(position);
                switch (entity.getItemType()) {
                    case RuleItem.TYPE_RULE :
                        RuleItem item = (RuleItem) entity;
                        RuleDetailFragment.newInstance(item)
                                .show(getChildFragmentManager(), "Detail");
                        break;
                }
            }
        });
        return recyclerView;
    }

    private XmlDetailTabActivity getRootActivity () {
        return (XmlDetailTabActivity)getActivity();
    }

    public void setMode (String mode) {
        LogUtils.i(TAG, "setMode -> " + mode);
        mRuleList.clear();
        mAdapter.notifyDataSetChanged();
        IntentFirewall.FirewallIntentResolver resolver;
        switch (mode) {
            case XmlDetailTabActivity.TAG_TAB_RECEIVERS :
                resolver = firewall.getBroadcastResolver();
                break;
            case XmlDetailTabActivity.TAG_TAB_SERVICES :
                resolver = firewall.getServiceResolver();
                break;
            case XmlDetailTabActivity.TAG_TAB_ACTIVITIES :
                resolver = firewall.getActivityResolver();
                break;
            default:
                LogUtils.w(TAG, "Unhandled mode -> " + mode);
                getActivity().finish();
                return;
        }
        for (IntentFirewall.FirewallIntentFilter firewallIntentFilter : resolver.getFilters()) {
            RuleGroup group = new RuleGroup();
            Iterator<String> iterator = firewallIntentFilter.actionsIterator();
            while (iterator != null && iterator.hasNext()) {
                RuleItem item = new RuleItem();
                item.setMode(RuleItem.MODE_ACTION);
                item.setDetail(iterator.next());
                group.addSubItem(item);
            }
            Iterator<String> categoriesIterator = firewallIntentFilter.categoriesIterator();
            while (categoriesIterator != null && categoriesIterator.hasNext()) {
                RuleItem item = new RuleItem();
                item.setMode(RuleItem.MODE_CATEGORIES);
                item.setDetail(categoriesIterator.next());
                group.addSubItem(item);
            }
            group.setEnable(firewallIntentFilter.getRule().getBlock());
            mRuleList.add(group);
            mAdapter.notifyDataSetChanged();
        }
        expandAll();
    }
    private void expandAll() {
        for (int i = 0; i <mRuleList.size() ; i++) {
            mAdapter.expand(i + mAdapter.getHeaderLayoutCount(), false, false);
        }
    }
}
