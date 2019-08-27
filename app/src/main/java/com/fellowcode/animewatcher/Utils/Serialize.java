package com.fellowcode.animewatcher.Utils;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Serialize<T> {

    private String filename;
    private Context context;
    private FileOutputStream fos;
    private ObjectOutputStream oos;
    private FileInputStream fis;
    private ObjectInputStream ois;

    public Serialize(Context context, String key) {
        this.filename = key + ".aw";
        this.context = context;
    }

    private void write() throws IOException {
        fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
        oos = new ObjectOutputStream(fos);
    }

    public void write(T obj) {
        try {
            write();
            oos.writeObject(obj);
            close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void wrArray(T[] objects) {
        try {
            write();
            oos.writeObject(objects);
            close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void wrList(ArrayList<T> objects) {
        try {
            write();
            oos.writeObject(objects);
            close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void rdSetup() throws IOException {
        fis = context.openFileInput(filename);
        ois = new ObjectInputStream(fis);
    }

    public T read() {
        try {
            rdSetup();
            T obj = (T) ois.readObject();
            Log.d("request", String.valueOf(obj));
            close();
            return obj;
        } catch (IOException e) {
            Log.d("request", "azaza");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();

        }
        return null;
    }

    public T[] rdArray(){
        try {
            rdSetup();
            T[] objects = (T[]) ois.readObject();
            close();
            return objects;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<T> rdList() {
        try {
            rdSetup();
            ArrayList<T> objects = (ArrayList) ois.readObject();
            close();
            return objects;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void close() throws IOException {
        if (oos != null)
            oos.close();
        if (fos != null)
            fos.close();
        if (ois != null)
            ois.close();
        if (fis != null)
            fis.close();
    }

}
