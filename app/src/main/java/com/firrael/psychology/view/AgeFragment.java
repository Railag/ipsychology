package com.firrael.psychology.view;

import android.os.Bundle;
import android.widget.EditText;

import com.firrael.psychology.R;
import com.firrael.psychology.Utils;
import com.firrael.psychology.presenter.AgePresenter;
import com.firrael.psychology.view.base.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;
import nucleus.factory.RequiresPresenter;

/**
 * Created by Railag on 07.11.2016.
 */
@RequiresPresenter(AgePresenter.class)
public class AgeFragment extends BaseFragment<AgePresenter> {

    @BindView(R.id.ageField)
    EditText ageField;

    public static AgeFragment newInstance() {

        Bundle args = new Bundle();

        AgeFragment fragment = new AgeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected String getTitle() {
        return getString(R.string.ageTitle);
    }

    @Override
    protected int getViewId() {
        return R.layout.fragment_age;
    }

    @OnClick(R.id.nextButton)
    public void login() {
        Utils.hideKeyboard(getActivity());
        //startLoading();
        //getPresenter().request(nameField.getText().toString(), passwordField.getText().toString());
        getPresenter().save(ageField.getText().toString());
        getMainActivity().toTimeScreen();
        // TODO on success save
    }

   /* public void onSuccess(UserResult result) {
        stopLoading();
        if (result == null) {
            onError(new IllegalArgumentException());
            return;
        }
        if (result.invalid()) {
            toast(result.error);
            return;
        }
        toast("success login");
        User.save(result, getActivity());
        getMainActivity().updateNavigationMenu();
        getMainActivity().toTests();
    }

    public void onError(Throwable error) {
        error.printStackTrace();
        stopLoading();
        toast(error.getMessage());
    }*/
}
