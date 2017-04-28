package es.egames.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import es.egames.R;
import es.egames.adapters.TabsAdapter;

public class GameTabsFragment extends android.support.v4.app.Fragment {

    public TabsAdapter adapter;

    public GameTabsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_tabs, container, false);

        adapter = new TabsAdapter(getChildFragmentManager());

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        // Set Tabs inside Toolbar
        TabLayout tabs = (TabLayout) view.findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        return view;
    }

    // Add Fragments to Tabs
    private void setupViewPager(ViewPager viewPager) {
        adapter.addFragment(GameDetailsFormList.newInstance("nearby"), getString(R.string.nearby));
        adapter.addFragment(GameDetailsFormList.newInstance("followed"), getString(R.string.followed));
        adapter.addFragment(GameDetailsFormList.newInstance("recommended"), getString(R.string.recommended));
        viewPager.setAdapter(adapter);
    }

}
