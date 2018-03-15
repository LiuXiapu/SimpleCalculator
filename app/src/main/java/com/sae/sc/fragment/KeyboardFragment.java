package com.sae.sc.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


import com.sae.sc.R;
import com.sae.sc.data.ButtonID;
import com.sae.sc.listener.KeyboardListener;
import com.sae.sc.view.CalcButton;


public class KeyboardFragment extends Fragment implements View.OnClickListener, View.OnLongClickListener {
    public static final String TAG = "KeyboardFragment";

    @Nullable
    private KeyboardListener mListener;

    public static KeyboardFragment newInstance() {
        KeyboardFragment fragment = new KeyboardFragment();
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
        return inflater.inflate(R.layout.fragment_keyboard, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addEvent(view);
    }


    private void addEvent(View view) {
        for (int id : ButtonID.getIdBasic()) {
            try {
                View v = view.findViewById(id);

                if (v != null) {
                    v.setOnClickListener(this);
                    v.setOnLongClickListener(this);
                } else {
                    View padBasic = view.findViewById(R.id.pad_basic);
                    v = padBasic.findViewById(id);
                    if (v != null) {
                        v.setOnClickListener(this);
                        v.setOnLongClickListener(this);
                    } else {
                        View padAdvance = view.findViewById(R.id.pad_advance);
                        v = padAdvance.findViewById(id);
                        if (v != null) {
                            v.setOnClickListener(this);
                            v.setOnLongClickListener(this);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    @SuppressWarnings("UnCompleted")
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
            case R.id.btn_equal:
                mListener.onEqual();
                break;
            default:
                if (view instanceof CalcButton) {
                    CalcButton calcButton = (CalcButton) view;
                    String text = calcButton.getText().toString();
                    if (text.length() >= 2) {
                        mListener.onInsert(text + "(");
                    } else {
                        mListener.onInsert(((Button) view).getText().toString());
                    }
                }
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof KeyboardListener) {
            mListener = (KeyboardListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement KeyboardListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
