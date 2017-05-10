package com.firrael.psychology.view;

import android.os.Bundle;
import android.widget.EditText;

import com.firrael.psychology.R;
import com.firrael.psychology.Utils;
import com.firrael.psychology.presenter.NamePresenter;
import com.firrael.psychology.view.base.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;
import nucleus.factory.RequiresPresenter;

/**
 * Created by Railag on 07.11.2016.
 */
@RequiresPresenter(NamePresenter.class)
public class NameFragment extends BaseFragment<NamePresenter> {

    @BindView(R.id.nameField)
    EditText nameField;

    @BindView(R.id.emailField)
    EditText emailField;

    @BindView(R.id.passwordField)
    EditText passwordField;

    public static NameFragment newInstance() {

        Bundle args = new Bundle();

        NameFragment fragment = new NameFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected String getTitle() {
        return getString(R.string.nameTitle);
    }

    @Override
    protected int getViewId() {
        return R.layout.fragment_name;
    }

    @OnClick(R.id.nextButton)
    public void login() {
        Utils.hideKeyboard(getActivity());
        getPresenter().save(nameField.getText().toString(), emailField.getText().toString(), passwordField.getText().toString());
        getMainActivity().toAgeScreen();
    }
}
