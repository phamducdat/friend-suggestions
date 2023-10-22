package com.datpd.utils;

public enum CacheKeyEnum {

    USER_DTO("user"),

    USER_FRIENDS("user:friends"),

    USER_FRIEND_SUGGESTIONS("user:friend:suggestions");

    private static final String PREFIX = "wiinvent";

    private final String key;

    CacheKeyEnum(String key) {
        this.key = key;
    }

    public String genKey(long id) {
        return PREFIX + ":" + key + ":" + id;
    }
}
