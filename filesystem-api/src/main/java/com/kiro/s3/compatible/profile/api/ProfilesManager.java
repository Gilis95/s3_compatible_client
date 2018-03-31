package com.kiro.s3.compatible.profile.api;

import com.amazonaws.auth.profile.internal.BasicProfile;

import java.util.Map;

public interface ProfilesManager {


    void createS3CompatibleProfile(String endpoint, String profileName, String accessKey, String secretKey);

    void createS3Profile(String regionName, String profileName, String accessKey, String secretKey);

    /**
     * Checks if certain profile exists
     *
     * @return true if profile exists false otherwise
     */
    boolean doesProfileExist(String profileName);

    /**
     * Loads the local AWS credential profiles from the standard location (~/.aws/credentials), which
     * can be easily overridden through the <code>AWS_CREDENTIAL_PROFILES_FILE</code> environment
     * variable.
     */
    Map<String, BasicProfile> getAllAvailableProfiles();


    BasicProfile getProfile(String profileName);
}
