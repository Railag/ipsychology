package com.firrael.psychology.view;

import android.os.Bundle;

import com.firrael.psychology.R;
import com.firrael.psychology.presenter.TestsFragmentPresenter;
import com.firrael.psychology.view.base.BaseFragment;

import butterknife.OnClick;
import nucleus.factory.RequiresPresenter;

/**
 * Created by Railag on 07.11.2016.
 */
@RequiresPresenter(TestsFragmentPresenter.class)
public class TestsFragment extends BaseFragment<TestsFragmentPresenter> {

    public static TestsFragment newInstance() {

        Bundle args = new Bundle();

        TestsFragment fragment = new TestsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected String getTitle() {
        return getString(R.string.landingTitle);
    }

    @Override
    protected int getViewId() {
        return R.layout.fragment_tests;
    }

    @OnClick(R.id.volume)
    public void toVolume() {
        getMainActivity().toInstructionFragment(InstructionFragment.Test.ATTENTION_VOLUME);
    }

    @OnClick(R.id.complex)
    public void toComplex() {
        getMainActivity().toInstructionFragment(InstructionFragment.Test.COMPLEX_MOTOR_REACTION);
    }

    @OnClick(R.id.reaction)
    public void toReaction() {
        getMainActivity().toInstructionFragment(InstructionFragment.Test.REACTION);
    }

}
