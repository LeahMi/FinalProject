package com.dvora.finalproject.fragments;

import android.os.Bundle;

public interface ICallbackFragment {
    void showFragment(int fragmentID);

    void showFragment(int fragmentID, Bundle bundle);
}
