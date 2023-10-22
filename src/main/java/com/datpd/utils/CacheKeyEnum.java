package com.datpd.utils;

public enum CacheKeyEnum {

    USER_DTO("user");

    private static final String PREFIX = "wiinvent";

    private final String key;

    CacheKeyEnum(String key) {
        this.key = key;
    }

    public String genKey(long id) {
        return PREFIX + ":" + key + ":" + id;
    }
}
