package com.firrael.psychology.view;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.firrael.psychology.App;
import com.firrael.psychology.R;
import com.firrael.psychology.presenter.MainPresenter;
import com.firrael.psychology.view.results.AttentionVolumeResultsFragment;
import com.firrael.psychology.view.results.ComplexMotorReactionResultsFragment;
import com.firrael.psychology.view.results.ReactionResultsFragment;
import com.firrael.psychology.view.results.ResultScreen;
import com.firrael.psychology.view.tests.AttentionVolumeTestFragment;
import com.firrael.psychology.view.tests.ComplexMotorReactionTestFragment;
import com.firrael.psychology.view.tests.ReactionTestFragment;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import butterknife.ButterKnife;
import nucleus.factory.RequiresPresenter;
import nucleus.view.NucleusAppCompatActivity;

@RequiresPresenter(MainPresenter.class)
public class MainActivity extends NucleusAppCompatActivity<MainPresenter> {

    private static final String TAG_MAIN = "mainTag";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.loading)
    AVLoadingIndicatorView loading;

    private FirebaseAnalytics analytics;

    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        App.setMainActivity(this);

        analytics = FirebaseAnalytics.getInstance(this);

        toSplash();
    }


    @Override
    public void onBackPressed() {

        if (currentFragment instanceof ResultScreen) {
            toLanding();
            return;
        }

        if (currentFragment instanceof LandingFragment) {
            finish();
            return;
        }

        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void startLoading() {
        loading.setVisibility(View.VISIBLE);
    }

    public void stopLoading() {
        loading.setVisibility(View.GONE);
    }

    private <T extends Fragment> void setFragment(final T fragment) {
        runOnUiThread(() -> {
            currentFragment = fragment;

            final FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

            // TODO custom transaction animations
            fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
            fragmentTransaction.replace(R.id.mainFragment, fragment, TAG_MAIN);
            fragmentTransaction.commitAllowingStateLoss();

        });
    }

    public void toSplash() {
        setFragment(SplashFragment.newInstance());
    }

    public void toLogin() {
        setFragment(LoginFragment.newInstance());
    }

    public void toTests() {
        setFragment(TestsFragment.newInstance());
    }

    public void toNameScreen() {
        setFragment(NameFragment.newInstance());
    }

    public void toAgeScreen() {
        setFragment(AgeFragment.newInstance());
    }

    public void toTimeScreen() {
        setFragment(TimeFragment.newInstance());
    }

    public void toLanding() {
        setFragment(LandingFragment.newInstance());
    }

    public void toInfo() {
        setFragment(InfoFragment.newInstance());
    }

    public void toStatistics() {
        setFragment(StatisticsFragment.newInstance());
    }


    public void toSettings() {
        setFragment(SettingsFragment.newInstance());
    }

    public void toInstructionFragment(InstructionFragment.Test test) {
        setFragment(InstructionFragment.newInstance(test));
    }

    public void toAttentionVolumeTest() {
        setFragment(AttentionVolumeTestFragment.newInstance());
    }

    public void toReactionTest() {
        setFragment(ReactionTestFragment.newInstance());
    }

    public void toComplexMotorReactionTest() {
        setFragment(ComplexMotorReactionTestFragment.newInstance());
    }

    public void toReactionResults(Bundle args) {
        setFragment(ReactionResultsFragment.newInstance(args));
    }

    public void toAttentionVolumeResults(Bundle args) {
        setFragment(AttentionVolumeResultsFragment.newInstance(args));
    }

    public void toComplexMotorReactionResults(Bundle args) {
        setFragment(ComplexMotorReactionResultsFragment.newInstance(args));
    }
}