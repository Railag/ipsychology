package com.firrael.psychology.view;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.firrael.psychology.R;
import com.firrael.psychology.model.User;
import com.firrael.psychology.model.UserResult;
import com.firrael.psychology.presenter.LoginPresenter;
import com.firrael.psychology.view.base.BaseFragment;

import butterknife.BindView;
import nucleus.factory.RequiresPresenter;

/**
 * Created by Railag on 31.05.2016.
 */
@RequiresPresenter(LoginPresenter.class)
public class LoginFragment extends BaseFragment<LoginPresenter> {

    @BindView(R.id.loginField)
    EditText loginField;
    @BindView(R.id.passwordField)
    EditText passwordField;

    public static LoginFragment newInstance() {

        Bundle args = new Bundle();

        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected String getTitle() {
        return getString(R.string.login);
    }

    @Override
    protected int getViewId() {
        return R.layout.fragment_login;
    }

    @Override
    protected void initView(View v) {
        getMainActivity().showToolbar();
        getMainActivity().toggleArrow(true);

        passwordField.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                getPresenter().request(loginField.getText().toString(), passwordField.getText().toString());
                startLoading();

                return true;
            } else {
                return false;
            }
        });
    }

    public void onSuccess(UserResult result) {
        stopLoading();
        if (result == null) {
            onError(new IllegalArgumentException());
            return;
        }
        if (result.invalid()) {
            toast(result.error);
            return;
        }
        User.save(result, getActivity());
        getMainActivity().toLanding();
    }

    public void onError(Throwable error) {
        error.printStackTrace();
        stopLoading();
        toast(error.getMessage());
    }
}
