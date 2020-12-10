package com.example.sonerimtestproject.base;

import androidx.annotation.StringRes;

public interface BaseView {
    String getString(@StringRes int stringResId);
    void showToast(String string);
}