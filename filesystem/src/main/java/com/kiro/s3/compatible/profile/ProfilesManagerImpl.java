package com.kiro.s3.compatible.profile;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfilesConfigFile;
import com.amazonaws.auth.profile.internal.BasicProfile;
import com.amazonaws.auth.profile.internal.Profile;
import com.amazonaws.auth.profile.internal.ProfileKeyConstants;
import com.amazonaws.profile.path.AwsProfileFileLocationProvider;
import com.kiro.s3.compatible.FileSystemConstants;
import com.kiro.s3.compatible.profile.api.ProfilesManager;
import com.kiro.s3.compatible.profile.aws.ProfilesConfigFileWriter;

import java.util.Map;

public class ProfilesManagerImpl implements ProfilesManager {
    private ProfilesConfigFile mProfilesConfigFile = new ProfilesConfigFile();

    public void createS3CompatibleProfile(String endpoint, String profileName, String accessKey, String secretKey) {
        Profile profileForCreation = new Profile(profileName, new BasicAWSCredentials(accessKey, secretKey));

        profileForCreation.getProperties().put(FileSystemConstants.PROFILE_ENDPOINT, endpoint);


        ProfilesConfigFileWriter.modifyOrInsertProfiles(
                AwsProfileFileLocationProvider.DEFAULT_CREDENTIALS_LOCATION_PROVIDER.getLocation(),
                profileForCreation);

        // Refresh profile holder
        mProfilesConfigFile.refresh();
    }


    public void createS3Profile(String regionName, String profileName, String accessKey, String secretKey) {
        Profile profileForCreation = new Profile(profileName, new BasicAWSCredentials(accessKey, secretKey));

        profileForCreation.getProperties().put(ProfileKeyConstants.REGION, regionName);


        ProfilesConfigFileWriter.modifyOrInsertProfiles(
                AwsProfileFileLocationProvider.DEFAULT_CREDENTIALS_LOCATION_PROVIDER.getLocation(),
                profileForCreation);

        // Refresh profile holder
        mProfilesConfigFile.refresh();
    }

    /**
     * Checks if certain profile exists
     *
     * @return true if profile exists false otherwise
     */
    public boolean doesProfileExist(String profileName) {
        return mProfilesConfigFile.getAllBasicProfiles().containsKey(profileName);
    }

    /**
     * Loads the local AWS credential profiles from the standard location (~/.aws/credentials), which
     * can be easily overridden through the <code>AWS_CREDENTIAL_PROFILES_FILE</code> environment
     * variable.
     */
    public Map<String, BasicProfile> getAllAvailableProfiles() {
        return mProfilesConfigFile.getAllBasicProfiles();
    }


    public BasicProfile getProfile(String profileName) {
        return mProfilesConfigFile.getAllBasicProfiles().get(profileName);
    }

}
