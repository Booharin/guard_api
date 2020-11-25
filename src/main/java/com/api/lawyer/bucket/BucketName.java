package com.api.lawyer.bucket;

public enum BucketName {

    PROFILE_IMAGE("lawyer-api");

    private final String bucketName;

    BucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getBucketName() {
        return bucketName;
    }
}
