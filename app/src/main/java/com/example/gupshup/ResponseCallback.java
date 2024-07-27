package com.example.gupshup;

public interface ResponseCallback {
    void onResponse(String response);
    void onError(Throwable throwable);
}
