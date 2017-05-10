package com.firrael.psychology.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.firrael.psychology.R;
import com.firrael.psychology.model.StatisticsResult;
import com.firrael.psychology.model.User;
import com.firrael.psychology.presenter.StatisticsPresenter;
import com.firrael.psychology.view.adapter.ComplexResultsAdapter;
import com.firrael.psychology.view.adapter.FocusingResultsAdapter;
import com.firrael.psychology.view.adapter.ReactionResultsAdapter;
import com.firrael.psychology.view.adapter.StabilityResultsAdapter;
import com.firrael.psychology.view.adapter.StressResultsAdapter;
import com.firrael.psychology.view.adapter.VolumeResultsAdapter;
import com.firrael.psychology.view.base.BaseFragment;

import java.util.Collections;

import butterknife.BindView;
import nucleus.factory.RequiresPresenter;

/**
 * Created by Railag on 03.05.2017.
 */
@RequiresPresenter(StatisticsPresenter.class)
public class StatisticsFragment extends BaseFragment<StatisticsPresenter> {

    @BindView(R.id.reactionResultsList)
    RecyclerView reactionResultsList;
    @BindView(R.id.complexResultsList)
    RecyclerView complexResultsList;
    @BindView(R.id.volumeResultsList)
    RecyclerView volumeResultsList;
    @BindView(R.id.focusingResultsList)
    RecyclerView focusingResultsList;
    @BindView(R.id.stabilityResultsList)
    RecyclerView stabilityResultsList;
    @BindView(R.id.stressResultsList)
    RecyclerView stressResultsList;

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
        startLoading();

        LinearLayoutManager reactionManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        reactionResultsList.setLayoutManager(reactionManager);

        LinearLayoutManager complexManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        complexResultsList.setLayoutManager(complexManager);

        LinearLayoutManager volumeManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        volumeResultsList.setLayoutManager(volumeManager);

        LinearLayoutManager focusingManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        focusingResultsList.setLayoutManager(focusingManager);

        LinearLayoutManager stabilityManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        stabilityResultsList.setLayoutManager(stabilityManager);

        LinearLayoutManager stressManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        stressResultsList.setLayoutManager(stressManager);

        fetchData();
    }

    private void fetchData() {
        User user = User.get(getActivity());
        getPresenter().fetch(user.getId());
    }

    private StatisticsResult sortResults(StatisticsResult result) {
        Collections.sort(result.volumeResults);
        Collections.sort(result.complexResults);
        Collections.sort(result.focusingResults);
        Collections.sort(result.reactionResults);
        Collections.sort(result.stressResults);
        Collections.sort(result.stabilityResults);
        return result;
    }

    private void fillUi(StatisticsResult result) {
        VolumeResultsAdapter volumeAdapter = new VolumeResultsAdapter();
        volumeAdapter.setAllResults(result.volumeResults);
        volumeResultsList.setAdapter(volumeAdapter);

        ComplexResultsAdapter complexAdapter = new ComplexResultsAdapter();
        complexAdapter.setAllResults(result.complexResults);
        complexResultsList.setAdapter(complexAdapter);

        FocusingResultsAdapter focusingAdapter = new FocusingResultsAdapter();
        focusingAdapter.setAllResults(result.focusingResults);
        focusingResultsList.setAdapter(focusingAdapter);

        ReactionResultsAdapter reactionAdapter = new ReactionResultsAdapter();
        reactionAdapter.setAllResults(result.reactionResults);
        reactionResultsList.setAdapter(reactionAdapter);

        StressResultsAdapter stressAdapter = new StressResultsAdapter();
        stressAdapter.setAllResults(result.stressResults);
        stressResultsList.setAdapter(stressAdapter);

        StabilityResultsAdapter stabilityAdapter = new StabilityResultsAdapter();
        stabilityAdapter.setAllResults(result.stabilityResults);
        stabilityResultsList.setAdapter(stabilityAdapter);
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

        StatisticsResult sortedResult = sortResults(result);
        fillUi(sortedResult);
    }

    public void onError(Throwable throwable) {
        stopLoading();
        throwable.printStackTrace();
    }
}