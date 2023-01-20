package org.example.client.notificationservice;

public class NotificationMQ<T> {
    String type;
    T data;

    public NotificationMQ() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
