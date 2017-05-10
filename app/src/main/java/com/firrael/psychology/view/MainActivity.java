package com.firrael.psychology.view;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.firrael.psychology.App;
import com.firrael.psychology.FcmMessagingService;
import com.firrael.psychology.R;
import com.firrael.psychology.RConnectorService;
import com.firrael.psychology.model.Result;
import com.firrael.psychology.model.User;
import com.firrael.psychology.presenter.MainPresenter;
import com.firrael.psychology.view.results.AttentionStabilityResultsFragment;
import com.firrael.psychology.view.results.AttentionVolumeResultsFragment;
import com.firrael.psychology.view.results.ComplexMotorReactionResultsFragment;
import com.firrael.psychology.view.results.FocusingResultsFragment;
import com.firrael.psychology.view.results.ReactionResultsFragment;
import com.firrael.psychology.view.results.ResultScreen;
import com.firrael.psychology.view.results.StressResistanceResultsFragment;
import com.firrael.psychology.view.tests.AttentionStabilityTestFragment;
import com.firrael.psychology.view.tests.AttentionVolumeTestFragment;
import com.firrael.psychology.view.tests.ComplexMotorReactionTestFragment;
import com.firrael.psychology.view.tests.FocusingTestFragment;
import com.firrael.psychology.view.tests.ReactionTestFragment;
import com.firrael.psychology.view.tests.StressResistanceTestFragment;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import butterknife.ButterKnife;
import nucleus.factory.RequiresPresenter;
import nucleus.view.NucleusAppCompatActivity;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static butterknife.ButterKnife.findById;

@RequiresPresenter(MainPresenter.class)
public class MainActivity extends NucleusAppCompatActivity<MainPresenter> {

    private final static String TAG = MainActivity.class.getSimpleName();

    private static final String TAG_MAIN = "mainTag";
    private static final int PHOTO_CODE = 1;

    private final static int PN_GROUP_INVITE = 1;
    private final static int PN_GROUP_CALL = 2;

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

        // TODO check for google play services
/*        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        if (code != ConnectionResult.SUCCESS) {
            GoogleApiAvailability.getInstance().showErrorDialogFragment(this, code, 1);
        }*/

        setSupportActionBar(toolbar);


        App.setMainActivity(this);

        if (!User.isFcmSaved(this)) {
            User user = User.get(this);
            String fcmToken = user.getFcmToken(this);
            if (!TextUtils.isEmpty(fcmToken)) {
                RConnectorService service = App.restService();

                service.sendFCMToken(user.getId(), fcmToken)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::onFcmTokenSuccess, this::onFcmTokenError);
            }
        }

        analytics = FirebaseAnalytics.getInstance(this);

        Intent intent = getIntent();
        if (intent != null) {
            handlePN(intent);
        }
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


    void makePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, PHOTO_CODE);
        } else
            Toast.makeText(this, getString(R.string.image_capture_no_camera), Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_CODE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ImageView userImage = findById(this, R.id.userImage);
            RoundedBitmapDrawable circularBitmapDrawable =
                    RoundedBitmapDrawableFactory.create(MainActivity.this.getResources(), imageBitmap);
            circularBitmapDrawable.setCircular(true);
            userImage.setImageDrawable(circularBitmapDrawable);

            getPresenter().saveImage(imageBitmap);
        }
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
        //toTests();
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
        //    toInstructionFragment(InstructionFragment.Test.STRESS_RESISTANCE);
    }


    public void toSettings() {
        // TODO
        setFragment(SettingsFragment.newInstance());
        //    toInstructionFragment(InstructionFragment.Test.COMPLEX_MOTOR_REACTION);
    }

    public void toInstructionFragment(InstructionFragment.Test test) {
        setFragment(InstructionFragment.newInstance(test));
    }

    public void toAttentionVolumeTest() {
        setFragment(AttentionVolumeTestFragment.newInstance());
    }

    public void toFocusingTest() {
        setFragment(FocusingTestFragment.newInstance());
    }

    public void toAttentionStabilityTest() {
        setFragment(AttentionStabilityTestFragment.newInstance());
    }

    public void toReactionTest() {
        setFragment(ReactionTestFragment.newInstance());
    }

    public void toStressResistanceTest() {
        setFragment(StressResistanceTestFragment.newInstance());
    }

    public void toComplexMotorReactionTest() {
        setFragment(ComplexMotorReactionTestFragment.newInstance());
    }

    public void toFocusingResults(Bundle args) {
        setFragment(FocusingResultsFragment.newInstance(args));
    }

    public void toReactionResults(Bundle args) {
        setFragment(ReactionResultsFragment.newInstance(args));
    }

    public void toAttentionVolumeResults(Bundle args) {
        setFragment(AttentionVolumeResultsFragment.newInstance(args));
    }

    public void toAttentionStabilityResults(Bundle args) {
        setFragment(AttentionStabilityResultsFragment.newInstance(args));
    }

    public void toStressResistanceResults(Bundle args) {
        setFragment(StressResistanceResultsFragment.newInstance(args));
    }

    public void toComplexMotorReactionResults(Bundle args) {
        setFragment(ComplexMotorReactionResultsFragment.newInstance(args));
    }


    /*public void toMyGroups() {
        setFragment(MyGroupsFragment.newInstance());
    }

    public void toNewGroup() {
        setFragment(NewGroupFragment.newInstance());
    }

    public void toGroupMemberScreen(Group group) {
        setFragment(GroupMemberFragment.newInstance(group));
    }

    public void toGroupCreatorScreen(Group group) {
        setFragment(GroupCreatorFragment.newInstance(group));
    }

    public void toWebrtcScreen(String host) {
        Intent intent = new Intent(this, WebrtcActivity.class);
        intent.putExtra(WebrtcActivity.HOST, host);
        startActivity(intent);
    }

    public void updateNavigationMenu() {
        View headerView = navigationView.getHeaderView(0);

        ImageView userImage = findById(headerView, R.id.userImage);
        TextView userLogin = findById(headerView, R.id.userLogin);
        TextView userEmail = findById(headerView, R.id.userEmail);

        User user = User.get(this);
        Glide.with(this).load(user.getProfileImageUrl()).asBitmap().centerCrop().into(new BitmapImageViewTarget(userImage) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(MainActivity.this.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                userImage.setImageDrawable(circularBitmapDrawable);
            }
        });

        userLogin.setText(user.getLogin());
        userEmail.setText(user.getEmail());
    }

    public void onSuccessSaveImage(ImageResult result) {
        User.get(this).setProfileImageUrl(result.getUrl());
        updateNavigationMenu();
    }

    public void onErrorSaveImage(Throwable throwable) {
        throwable.printStackTrace();
    }


    public void onSuccessAddUser(AddUserResult result) {
        if (result.invalid()) {
            Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show();
            return;
        }

        toMyGroups();
    }


*/
    public void onErrorAddUser(Throwable throwable) {
        throwable.printStackTrace();
    }


    public void onFcmTokenSuccess(Result result) {
        if (result.invalid()) {
            Log.e(TAG, result.error);
            return;
        }

        Log.i(TAG, result.result);

        User.fcmSaved(getBaseContext());
    }

    public void onFcmTokenError(Throwable throwable) {
        throwable.printStackTrace();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handlePN(intent);
    }

    public void handlePN(Intent intent) {
        String action = intent.getAction();
        if (action != null) {
            if (action.equalsIgnoreCase(Intent.ACTION_MAIN)) {
                toSplash();
                return;
            }
        }

        Bundle args = intent.getExtras();
        if (args != null) {
            if (args.containsKey(FcmMessagingService.PN_CODE_KEY)) {
                int pnCode = args.getInt(FcmMessagingService.PN_CODE_KEY);
                String data = args.getString(FcmMessagingService.PN_DATA_KEY, "");
                switch (pnCode) {
                    case PN_GROUP_INVITE:
                        /*User user = User.get(this);
                        long userId = user.getId();
                        try {
                            long groupId = Long.valueOf(data);
                            getPresenter().addToGroup(userId, groupId);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }*/
                        break;
                    case PN_GROUP_CALL:
                        //    toWebrtcScreen(data);
                        break;
                }
            }
        } else { // no custom PN
            toSplash();
        }
    }
}