package com.firrael.psychology.view;

import android.os.Bundle;

import com.firrael.psychology.R;
import com.firrael.psychology.model.User;
import com.firrael.psychology.view.base.SimpleFragment;

import butterknife.OnClick;

/**
 * Created by Railag on 21.03.2017.
 */

public class LandingFragment extends SimpleFragment {

    public static LandingFragment newInstance() {

        Bundle args = new Bundle();

        LandingFragment fragment = new LandingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected String getTitle() {
        return getString(R.string.landingScreenTitle);
    }

    @Override
    protected int getViewId() {
        return R.layout.fragment_landing;
    }

    @OnClick(R.id.infoButton)
    public void goToInfo() {
        getMainActivity().toInfo();
    }

    @OnClick(R.id.testsButton)
    public void goToTests() {
        getMainActivity().toTests();
    }

    @OnClick(R.id.statisticsButton)
    public void toStatistics() {
        getMainActivity().toStatistics();
    }

    @OnClick(R.id.settingsButton)
    public void toSettings() {
        getMainActivity().toSettings();
    }

    @OnClick(R.id.logoutButton)
    public void logout() {
        User.logout(getActivity());
        getMainActivity().toSplash();
    }
}
