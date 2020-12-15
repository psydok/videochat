package ru.csu.videochat.model.utilities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

public class Converter<T> {
    private T obj;
    private T needClass;

    public Converter(T obj, T needClass) {
        this.obj = obj;
        this.needClass = needClass;
    }

    public void toJSON(T obj) throws IOException {

    }

    public  T toJavaObject() throws IOException {

        return null;
    }
}
