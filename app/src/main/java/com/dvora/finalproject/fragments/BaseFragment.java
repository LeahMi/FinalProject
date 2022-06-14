package com.dvora.finalproject.fragments;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment {
    protected ICallbackFragment mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ICallbackFragment){
            mListener= (ICallbackFragment) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener=null;
    }


}

