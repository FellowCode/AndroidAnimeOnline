package com.fellowcode.animewatcher.Utils;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Serialize {

    public static <V> void write(Context context, String key, V obj) {
        try {
            FileOutputStream fos = context.openFileOutput(getFilename(key), Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(obj);
            fos.close();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static public <V> V read(Context context, String key) {
        try {
            FileInputStream fis = context.openFileInput(getFilename(key));
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object o = ois.readObject();
            Log.d("request", String.valueOf(o));
            V obj = (V) o;
            Log.d("request", String.valueOf(obj));
            fis.close();
            ois.close();
            return obj;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();

        }
        return null;
    }

    static String getFilename(String key){
        return key + ".aw";
    }
}
