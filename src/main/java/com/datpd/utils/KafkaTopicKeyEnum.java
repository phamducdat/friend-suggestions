package com.datpd.utils;

public enum KafkaTopicKeyEnum {

    FRIENDS_SUGGESTIONS("friend_suggestions");

    public static final String PREFIX = "wiinvent";

    private final String key;

    KafkaTopicKeyEnum(String key) {
        this.key = key;
    }

    public String getKey() {
        return PREFIX + "_" + key;
    }

}
