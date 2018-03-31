package com.kiro.s3.compatible.filesystem;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.auth.profile.ProfilesConfigFile;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.profile.path.AwsProfileFileLocationProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectListing;
import com.kiro.s3.compatible.FileSystemConstants;
import com.kiro.s3.compatible.filesystem.api.ProfileFileOperations;
import com.kiro.s3.compatible.filesystem.api.exceptions.FileSystemOperationException;
import com.kiro.s3.compatible.filesystem.operations.*;

import java.util.List;

/**
 * This class used as holder of chosen S3 server profile and hostname/IP address.
 * This class is single instance for each profile. It executes all filesystem operations.
 *
 * @author christian
 */
public class ProfileFileOperationsImpl implements ProfileFileOperations {
    private final AmazonS3 mAmazonS3Client;
    private final FileSystemOperationExecutor mFileSystemOperationExecutor;

    /**
     * This constructor is used for establishing connections to S3 compatible storage
     *
     * @param profileName - the name of profile, which credentials are contained into
     *                    {@link AwsProfileFileLocationProvider#DEFAULT_CREDENTIALS_LOCATION_PROVIDER}
     */
    public ProfileFileOperationsImpl(final String profileName) {
        ProfilesConfigFile profilesConfigFile = new ProfilesConfigFile();
        AmazonS3ClientBuilder amazonS3ClientBuilder = AmazonS3Client.builder()
                .withCredentials(new ProfileCredentialsProvider(profilesConfigFile, profileName));

        String endpoint = profilesConfigFile.getAllBasicProfiles().get(profileName)
                .getPropertyValue(FileSystemConstants.PROFILE_ENDPOINT);

        if (null != endpoint && !endpoint.isEmpty()) {
            amazonS3ClientBuilder.setEndpointConfiguration(new AwsClientBuilder
                    .EndpointConfiguration(endpoint, ""));
        }

        String region = profilesConfigFile.getAllBasicProfiles().get(profileName).getRegion();

        if (null != region && !region.isEmpty()) {
            amazonS3ClientBuilder.setRegion(region);
        }

        mAmazonS3Client = amazonS3ClientBuilder.build();

        mFileSystemOperationExecutor = new FileSystemOperationExecutor();

    }

    public List<Bucket> listBuckets() throws FileSystemOperationException {
        ListBucketOperation listBucketOperation = new ListBucketOperation(mAmazonS3Client);

        mFileSystemOperationExecutor.executeFileSystemOperation(listBucketOperation);

        return listBucketOperation.getResult();
    }

    /**
     * {@inheritDoc}
     */
    public ObjectListing list(String bucketName, String fileToList, int maxEntries) throws FileSystemOperationException {
        assert !(bucketName == null || bucketName.isEmpty()) : "Bucket name cannot be null";
        assert !(fileToList == null || fileToList.isEmpty()) : "Folder name cannot be null";
        assert maxEntries > 0 : "Entries to list cannot be less than 1";

        ListOperation listOperation = new ListOperation(mAmazonS3Client, bucketName, fileToList, maxEntries);
        mFileSystemOperationExecutor.executeFileSystemOperation(listOperation);

        return listOperation.getResult();
    }


    /**
     * {@inheritDoc}
     */
    public Boolean createDirectory(String bucketName, String folderName) throws FileSystemOperationException {
        assert !(bucketName == null || bucketName.isEmpty()) : "Bucket name cannot be null";
        assert !(folderName == null || folderName.isEmpty()) : "Folder name cannot be null";

        CreateDirectoryOperation createDirectoryOperation =
                new CreateDirectoryOperation(mAmazonS3Client, bucketName, folderName);
        mFileSystemOperationExecutor.executeFileSystemOperation(createDirectoryOperation);

        return createDirectoryOperation.getResult();
    }


    /**
     * {@inheritDoc}
     */
    public Boolean delete(String bucketName, String fileToDelete) throws FileSystemOperationException {
        assert !(bucketName == null || bucketName.isEmpty()) : "Bucket name cannot be null";
        assert !(fileToDelete == null || fileToDelete.isEmpty()) : "File to delete cannot be null";

        DeleteFileOperation deleteFileOperation = new DeleteFileOperation(mAmazonS3Client, bucketName, fileToDelete);
        mFileSystemOperationExecutor.executeFileSystemOperation(deleteFileOperation);

        return deleteFileOperation.getResult();
    }


    /**
     * {@inheritDoc}
     */
    public Boolean uploadFile(String bucketName, String remoteFilePath, String localFilePath) throws FileSystemOperationException {
        assert !(bucketName == null || bucketName.isEmpty()) : "Bucket name cannot be null";
        assert !(remoteFilePath == null || remoteFilePath.isEmpty()) : "Folder name cannot be null";
        assert !(localFilePath == null || localFilePath.isEmpty()) : "Folder name cannot be null";

        UploadFileOperation uploadFileOperation =
                new UploadFileOperation(mAmazonS3Client, bucketName, remoteFilePath, localFilePath);
        mFileSystemOperationExecutor.executeFileSystemOperation(uploadFileOperation);

        return uploadFileOperation.getResult();
    }


    /**
     * {@inheritDoc}
     */
    public boolean rename(String bucketName, String oldFileName, String newFileName) throws FileSystemOperationException {
        assert !(bucketName == null || bucketName.isEmpty()) : "Bucket name cannot be null";
        assert !(oldFileName == null || oldFileName.isEmpty()) :
                "File which you want to rename cannot be null";
        assert !(newFileName == null || newFileName.isEmpty()) :
                "Name, which you want file to be renamed to cannot be null";

        CopyFileOperation copyFileOperation =
                new CopyFileOperation(mAmazonS3Client, bucketName, oldFileName, bucketName, newFileName);
        mFileSystemOperationExecutor.executeFileSystemOperation(copyFileOperation);

        if (copyFileOperation.getResult()) {
            DeleteFileOperation deleteFileOperation =
                    new DeleteFileOperation(mAmazonS3Client, bucketName, oldFileName);

            mFileSystemOperationExecutor.executeFileSystemOperation(deleteFileOperation);

            return deleteFileOperation.getResult();
        }

        return false;
    }


    /**
     * {@inheritDoc}
     */
    public boolean exist(String bucketName, String fileName) throws FileSystemOperationException {
        assert !(bucketName == null || bucketName.isEmpty()) : "Bucket name cannot be null";
        assert !(fileName == null || fileName.isEmpty()) :
                "Filename cannot be null";
        FileExistOperation fileExistOperation = new FileExistOperation(mAmazonS3Client, bucketName, fileName);

        mFileSystemOperationExecutor.executeFileSystemOperation(fileExistOperation);

        return fileExistOperation.getResult();
    }


}
