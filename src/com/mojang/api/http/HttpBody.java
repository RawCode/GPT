package com.mojang.api.http;

public class HttpBody {

    private String bodyString;

    public HttpBody(String bodyString) {
        this.bodyString = bodyString;
        System.out.println(bodyString);
    }

    public byte[] getBytes() {
        return bodyString != null ? bodyString.getBytes() : new byte[0];
    }

}
