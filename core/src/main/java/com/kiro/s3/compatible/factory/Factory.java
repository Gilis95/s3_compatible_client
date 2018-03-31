package com.kiro.s3.compatible.factory;

import com.kiro.s3.compatible.filesystem.api.BucketFileOperations;
import com.kiro.s3.compatible.filesystem.api.ProfileFileOperations;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Factory {

    private static volatile Factory sInstance;

    private final static Object mutex = new Object();

    private Map<String, ProfileFileOperations> mProfileFileSystemInstances
            = Collections.synchronizedMap(new HashMap<>());

    private Map<String, BucketFileOperations> mProfileBucketFileServiceInstances
            = Collections.synchronizedMap(new HashMap<>());

    private final Object profileMutex = new Object();

    private final Object bucketMutex = new Object();

    private Factory() {

    }

    public static Factory getInstance() {
        if (sInstance == null) {
            synchronized (mutex) {
                if (sInstance == null) {
                    sInstance = new Factory();
                }
            }
        }

        return sInstance;
    }


    public ProfileFileOperations getFileSystemForProfile(String profile) {
        if (null == mProfileFileSystemInstances.get(profile)) {
            synchronized (profileMutex) {
                if (null == mProfileFileSystemInstances.get(profile)) {
                    try {
                        Class profileFileOperationsClass = Class.forName("com.kiro.s3.compatible.filesystem.ProfileFileOperationsImpl");
                        mProfileFileSystemInstances.put(profile,
                                (ProfileFileOperations) profileFileOperationsClass.getConstructor(String.class)
                                        .newInstance(profile));
                    } catch (ClassNotFoundException | IllegalAccessException
                            | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return mProfileFileSystemInstances.get(profile);
    }

    public BucketFileOperations getFileSystemService(String profile, String bucket) {
        String fileSystemServiceName = profile + '#' + bucket;

        if (null == mProfileFileSystemInstances.get(fileSystemServiceName)) {
            synchronized (bucketMutex) {
                if (null == mProfileFileSystemInstances.get(fileSystemServiceName)) {
                    mProfileBucketFileServiceInstances.put(fileSystemServiceName,
                            new BucketFileOperationsImpl(getFileSystemForProfile(profile), bucket));
                }
            }
        }

        return mProfileBucketFileServiceInstances.get(fileSystemServiceName);
    }
}
