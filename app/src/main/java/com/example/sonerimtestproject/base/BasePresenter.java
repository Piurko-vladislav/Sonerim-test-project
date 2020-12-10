package com.example.sonerimtestproject.base;

import androidx.annotation.NonNull;

public interface BasePresenter<T> {
    void takeView(@NonNull T view);
    void dropView();
}

