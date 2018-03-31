package com.kiro.s3.compatible.profile.api;

public class ProfilesManagerFactory {

    private static volatile ProfilesManagerFactory sInstance;

    private volatile ProfilesManager profilesManager;

    private final Object profileMutex = new Object();

    private static final Object mutex = new Object();

    private ProfilesManagerFactory() {
    }

    public static ProfilesManagerFactory getInstance() {
        if (null == sInstance) {
            synchronized (mutex) {
                if (null == sInstance) {
                    sInstance = new ProfilesManagerFactory();
                }
            }
        }

        return sInstance;
    }


    public ProfilesManager getProfilesManager() {
        if (null == profilesManager) {
            synchronized (profileMutex) {
                if (null == profilesManager) {
                    try {
                        profilesManager =
                                (ProfilesManager) Class.forName("com.kiro.s3.compatible.profile.ProfilesManagerImpl")
                                        .newInstance();
                    } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return profilesManager;
    }
}
