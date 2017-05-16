package com.firrael.psychology.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.firrael.psychology.R;
import com.firrael.psychology.model.StatisticsResult;
import com.firrael.psychology.model.User;
import com.firrael.psychology.presenter.StatisticsPresenter;
import com.firrael.psychology.view.base.BaseFragment;

import java.util.Collections;

import butterknife.BindView;
import butterknife.OnClick;
import nucleus.factory.RequiresPresenter;

/**
 * Created by Railag on 03.05.2017.
 */
@RequiresPresenter(StatisticsPresenter.class)
public class StatisticsFragment extends BaseFragment<StatisticsPresenter> {

    @BindView(R.id.reaction)
    Button reaction;
    @BindView(R.id.distribution)
    Button distribution;
    @BindView(R.id.complex)
    Button complex;

    private StatisticsResult results;

    public static StatisticsFragment newInstance() {

        Bundle args = new Bundle();

        StatisticsFragment fragment = new StatisticsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected String getTitle() {
        return getString(R.string.statistics);
    }

    @Override
    protected int getViewId() {
        return R.layout.fragment_statistics;
    }

    @Override
    protected void initView(View v) {
        getMainActivity().showToolbar();
        getMainActivity().toggleArrow(true);

        startLoading();

        fetchData();
    }

    private void fetchData() {
        User user = User.get(getActivity());
        getPresenter().fetch(user.getId());
    }

    private StatisticsResult sortResults(StatisticsResult result) {
        Collections.sort(result.volumeResults);
        Collections.sort(result.complexResults);
        Collections.sort(result.reactionResults);
        return result;
    }

    public void onSuccess(StatisticsResult result) {
        stopLoading();

        if (result == null) {
            onError(new IllegalArgumentException());
            return;
        }
        if (result.invalid()) {
            toast(result.error);
            return;
        }

        results = sortResults(result);

        complex.setEnabled(true);
        reaction.setEnabled(true);
        distribution.setEnabled(true);
    }

    public void onError(Throwable throwable) {
        stopLoading();
        throwable.printStackTrace();
    }

    @OnClick({R.id.reaction, R.id.distribution, R.id.complex})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.reaction:
                getMainActivity().toStatisticsReaction(results);
                break;
            case R.id.distribution:
                getMainActivity().toStatisticsComplex(results);
                break;
            case R.id.complex:
                getMainActivity().toStatisticsDistribution(results);
                break;
        }
    }
}