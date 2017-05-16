package com.firrael.psychology.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.firrael.psychology.R;
import com.firrael.psychology.model.StatisticsResult;
import com.firrael.psychology.view.adapter.ReactionResultsAdapter;
import com.firrael.psychology.view.base.SimpleFragment;

import butterknife.BindView;

/**
 * Created by Railag on 16.05.2017.
 */

public class StatisticsReactionFragment extends SimpleFragment {

    private final static String KEY_RESULTS = "results";

    @BindView(R.id.reactionResultsList)
    RecyclerView reactionResultsList;

    @Override
    protected String getTitle() {
        return getString(R.string.reactionTitle);
    }

    @Override
    protected int getViewId() {
        return R.layout.fragment_statistics_reaction;
    }

    public static StatisticsReactionFragment newInstance(StatisticsResult results) {

        Bundle args = new Bundle();
        args.putSerializable(KEY_RESULTS, results);

        StatisticsReactionFragment fragment = new StatisticsReactionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView(View v) {
        getMainActivity().showToolbar();
        getMainActivity().toggleArrow(true);

        Bundle args = getArguments();
        if (args != null) {
            StatisticsResult results = (StatisticsResult) args.getSerializable(KEY_RESULTS);

            LinearLayoutManager reactionManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            reactionResultsList.setLayoutManager(reactionManager);

            ReactionResultsAdapter reactionAdapter = new ReactionResultsAdapter();
            reactionAdapter.setAllResults(results.reactionResults);
            reactionResultsList.setAdapter(reactionAdapter);
        }
    }
}
