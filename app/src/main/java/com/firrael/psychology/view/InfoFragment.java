package com.firrael.psychology.view;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firrael.psychology.R;
import com.firrael.psychology.Utils;
import com.firrael.psychology.model.User;
import com.firrael.psychology.model.UserResult;
import com.firrael.psychology.presenter.InfoPresenter;
import com.firrael.psychology.view.base.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;
import nucleus.factory.RequiresPresenter;

/**
 * Created by Railag on 21.03.2017.
 */

@RequiresPresenter(InfoPresenter.class)
public class InfoFragment extends BaseFragment<InfoPresenter> {

    @BindView(R.id.displayLayout)
    LinearLayout displayLayout;

    @BindView(R.id.editLayout)
    LinearLayout editLayout;

    @BindView(R.id.login)
    TextView login;

    @BindView(R.id.email)
    TextView email;

    @BindView(R.id.age)
    TextView age;

    @BindView(R.id.time)
    TextView time;

    @BindView(R.id.loginEdit)
    TextView loginEdit;

    @BindView(R.id.emailEdit)
    EditText emailEdit;

    @BindView(R.id.ageEdit)
    EditText ageEdit;

    @BindView(R.id.timeEdit)
    EditText timeEdit;

    @BindView(R.id.editButton)
    FloatingActionButton editButton;

    public static InfoFragment newInstance() {

        Bundle args = new Bundle();

        InfoFragment fragment = new InfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected String getTitle() {
        return getString(R.string.info);
    }

    @Override
    protected int getViewId() {
        return R.layout.fragment_info;
    }

    @Override
    protected void initView(View v) {
        User user = User.get(getActivity());

        login.setText(user.getLogin());
        email.setText(user.getEmail());
        time.setText(String.valueOf(user.getTime()));
        age.setText(String.valueOf(user.getAge()));

        loginEdit.setText(user.getLogin());
        emailEdit.setText(user.getEmail());
        timeEdit.setText(String.valueOf(user.getTime()));
        ageEdit.setText(String.valueOf(user.getAge()));
    }


    @OnClick(R.id.editButton)
    public void editMode() {
        displayLayout.setVisibility(View.GONE);
        editLayout.setVisibility(View.VISIBLE);
        editButton.setVisibility(View.GONE);
    }

    @OnClick(R.id.backButton)
    public void viewMode() {
        displayLayout.setVisibility(View.VISIBLE);
        editLayout.setVisibility(View.GONE);
        editButton.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.saveButton)
    public void save() {
        String email = emailEdit.getText().toString();
        String time = timeEdit.getText().toString();
        String age = ageEdit.getText().toString();

        Utils.hideKeyboard(getActivity());
        startLoading();

        long userId = User.get(getActivity()).getId();

        getPresenter().save(userId, email, time, age);
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
        toast("success update");

        User.save(result, getActivity());

        getMainActivity().toLanding();
    }

    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }
}
