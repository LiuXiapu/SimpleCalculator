package com.sae.sc.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


import com.sae.sc.R;
import com.sae.sc.data.ButtonID;
import com.sae.sc.listener.BaseCalculatorKeyboardListener;
import com.sae.sc.listener.KeyboardListener;
import com.sae.sc.view.CalcButton;

import java.util.ArrayList;
import java.util.List;

import static com.sae.sc.data.ButtonID.getIDBaseNumbers;
import static com.sae.sc.data.ButtonID.getIDBases;


public class BaseCalculatorKeyboardFragment extends Fragment implements View.OnClickListener, View.OnLongClickListener {
    public static final String TAG = "BaseCalculatorKeyboardFragment";

    @Nullable
    private BaseCalculatorKeyboardListener mListener;

    public static BaseCalculatorKeyboardFragment newInstance() {
        BaseCalculatorKeyboardFragment fragment = new BaseCalculatorKeyboardFragment();
        fragment.setArguments(new Bundle());
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_base_calculator_keyboard, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        saveButtons(view);
        addEvent(view);
    }

    private List<CalcButton> numbers;
    private List<CalcButton> bases;

    private int defaultColor;

    private void saveButtons(View view) {
        numbers = new ArrayList<>();
        for (int id : getIDBaseNumbers()) {
            numbers.add((CalcButton) view.findViewById(id));
        }

        bases = new ArrayList<>();
        for (int id : getIDBases()) {
            CalcButton button = (CalcButton) view.findViewById(id);
            button.getPaint().setFakeBoldText(true);
            bases.add(button);
        }
        defaultColor = bases.get(0).getCurrentTextColor();

        changeViewToDec();
    }


    private void addEvent(View view) {
        for (int id : ButtonID.getIdBasic()) {
            try {
                View v = view.findViewById(id);

                if (v != null) {
                    v.setOnClickListener(this);
                    v.setOnLongClickListener(this);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public boolean onLongClick(View view) {
        if (mListener == null) {
            return false;
        }
        switch (view.getId()) {
            case R.id.btn_delete:
                mListener.onClear();
                return true;
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        if (mListener == null) {
            return;
        }

        switch (view.getId()) {
            case R.id.btn_delete:
                mListener.onDelete();
                break;
            case R.id.btn_dot:
                Toast.makeText(getActivity(), "浮点数转换功能0.0.2版本预定开放", Toast.LENGTH_SHORT).show();
                //mListener.onInsert(((Button) view).getText().toString());
                break;
            case R.id.dec:
                changeViewToDec();
                mListener.onTransfer("dec");
                break;
            case R.id.oct:
                changeViewToOct();
                mListener.onTransfer("oct");
                break;
            case R.id.hex:
                changeViewToHex();
                mListener.onTransfer("hex");
                break;
            case R.id.bin:
                changeViewToBin();
                mListener.onTransfer("bin");
                break;
            default:
                if (view instanceof CalcButton) {
                    mListener.onInsert(((Button) view).getText().toString());
                }
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseCalculatorKeyboardListener) {
            mListener = (BaseCalculatorKeyboardListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement BaseCalculatorKeyboardListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void changeViewToDec() {
        for (CalcButton button: bases) {
            button.setTextColor(defaultColor);
        }
        bases.get(0).setTextColor(Color.RED);

        for (int i = 0; i < 10; ++ i) {
            numbers.get(i).setEnabled(true);
        }
        for (int i = 10; i < 16; ++ i) {
            numbers.get(i).setEnabled(false);
        }
    }

    private void changeViewToHex() {
        for (CalcButton button: bases) {
            button.setTextColor(defaultColor);
        }
        bases.get(1).setTextColor(Color.RED);

        for (int i = 0; i < 16; ++ i) {
            numbers.get(i).setEnabled(true);
        }
    }

    private void changeViewToBin() {
        for (CalcButton button: bases) {
            button.setTextColor(defaultColor);
        }
        bases.get(2).setTextColor(Color.RED);

        for (int i = 0; i < 2; ++ i) {
            numbers.get(i).setEnabled(true);
        }
        for (int i = 2; i < 16; ++ i) {
            numbers.get(i).setEnabled(false);
        }
    }

    private void changeViewToOct() {
        for (CalcButton button: bases) {
            button.setTextColor(defaultColor);
        }
        bases.get(3).setTextColor(Color.RED);

        for (int i = 0; i < 8; ++ i) {
            numbers.get(i).setEnabled(true);
        }
        for (int i = 8; i < 16; ++ i) {
            numbers.get(i).setEnabled(false);
        }
    }

}
