package es.egames.fragments;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import es.egames.R;
import es.egames.activities.DetailsUserActivity;
import es.egames.adapters.TabsAdapter;
import es.egames.model.User;

public class UserDetailsActivityFragment extends Fragment {

    public TabsAdapter adapter;

    public UserDetailsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_user_details, container, false);

        adapter = new TabsAdapter(getFragmentManager());

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        TabLayout tabs = (TabLayout) view.findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        DetailsUserActivity activity = (DetailsUserActivity) getActivity();
        User u = activity.user;
        adapter.addFragment(SoughtItemFragment.newInstance("personalgames",u), getString(R.string.personal_games));
        adapter.addFragment(SoughtItemFragment.newInstance("followers",u), getString(R.string.followers));
        adapter.addFragment(SoughtItemFragment.newInstance("followees",u), getString(R.string.followees));
        adapter.addFragment(QualificationFragment.newInstance(u) ,getString(R.string.qualifications));
        viewPager.setAdapter(adapter);
    }
}
