package com.fellowcode.animewatcher.Utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class AjaxHandler {

    private static final String TAG = "AjaxHandler";
    private final Context context;

    public AjaxHandler(Context context) {
        this.context = context;
    }

    public void ajaxBegin() {
        Log.w(TAG, "AJAX Begin");
        Toast.makeText(context, "AJAX Begin", Toast.LENGTH_SHORT).show();
    }

    public void ajaxDone() {
        Log.w(TAG, "AJAX Done");
        Toast.makeText(context, "AJAX Done", Toast.LENGTH_SHORT).show();
    }
}
