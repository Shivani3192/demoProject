package com.example.spatel.giphyexample.utils.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bluelinelabs.conductor.Controller;

/**
 * Created by spatel on 01-04-2018.
 */
public abstract class BaseController extends ButterKnifeController{
    protected BaseController() { }

    protected BaseController(Bundle args) {
        super(args);
    }

    protected ActionBar getActionBar() {
        AppCompatActivity appCompatActivity = ((AppCompatActivity)getActivity());
        return appCompatActivity != null ? appCompatActivity.getSupportActionBar() : null;
    }

    @Override
    protected void onAttach(@NonNull View view) {
        setTitle();
        super.onAttach(view);
    }

    protected void setTitle() {
        Controller parentController = getParentController();
        while (parentController != null) {
            if (parentController instanceof BaseController && ((BaseController)parentController).getTitle() != null) {
                return;
            }
            parentController = parentController.getParentController();
        }

        String title = getTitle();
        ActionBar actionBar = getActionBar();
        if (title != null && actionBar != null) {
            actionBar.setTitle(title);
        }
    }

    protected String getTitle() {
        return null;
    }
}
