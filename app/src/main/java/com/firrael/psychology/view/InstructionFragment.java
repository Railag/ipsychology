package com.firrael.psychology.view;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.firrael.psychology.R;
import com.firrael.psychology.view.base.SimpleFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Railag on 20.03.2017.
 */

public class InstructionFragment extends SimpleFragment {

    private final static String TYPE = "type";

    public enum Test {
        REACTION,
        ATTENTION_VOLUME,
        COMPLEX_MOTOR_REACTION
    }

    public static InstructionFragment newInstance(Test test) {

        Bundle args = new Bundle();
        args.putSerializable(TYPE, test);

        InstructionFragment fragment = new InstructionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.instructionText)
    TextView instructionText;

    private Test test = Test.REACTION;

    @Override
    protected String getTitle() {
        return getString(R.string.instruction);
    }

    @Override
    protected int getViewId() {
        return R.layout.fragment_instruction;
    }

    @Override
    protected void initView(View v) {
        Bundle args = getArguments();
        if (args != null && args.containsKey(TYPE)) {
            test = (Test) args.getSerializable(TYPE);

            String instruction = "";

            switch (test) {
                case REACTION:
                    instruction = getString(R.string.instruction_reaction);
                    break;
                case ATTENTION_VOLUME:
                    instruction = getString(R.string.instruction_attention_volume);
                    break;
                case COMPLEX_MOTOR_REACTION:
                    instruction = getString(R.string.instruction_complex_motor_reaction);
                    break;
            }

            instructionText.setText(instruction);
        }
    }

    @OnClick(R.id.start)
    public void start() {
        switch (test) {
            case REACTION:
                getMainActivity().toReactionTest();
                break;
            case ATTENTION_VOLUME:
                getMainActivity().toAttentionVolumeTest();
                break;
            case COMPLEX_MOTOR_REACTION:
                getMainActivity().toComplexMotorReactionTest();
                break;
        }
    }
}
