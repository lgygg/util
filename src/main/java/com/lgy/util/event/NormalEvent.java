package com.lgy.util.event;

public class NormalEvent<T> {
    private T data;
    private String tag;

    public NormalEvent(T data, String tag) {
        this.data = data;
        this.tag = tag;
    }

    public NormalEvent() {

    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
