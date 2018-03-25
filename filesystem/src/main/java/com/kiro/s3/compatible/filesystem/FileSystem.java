package com.kiro.s3.compatible.filesystem;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.auth.profile.ProfilesConfigFile;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.profile.path.AwsProfileFileLocationProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectListing;
import com.kiro.s3.compatible.filesystem.operations.*;

/**
 * This class used as holder of chosen S3 server profile and hostname/IP address.
 * This class is single instance for each profile. It executes all filesystem operations.
 *
 * @author christian
 */
public class FileSystem {
    private final AmazonS3 mAmazonS3Client;
    private final FileSystemOperationExecutor mFileSystemOperationExecutor;

    /**
     * This constructor is used for establishing connections to S3 compatible storage
     *
     * @param endpoint    - hostname/IP address of S3 storage
     * @param profileName - the name of profile, which credentials are contained into
     *                    {@link AwsProfileFileLocationProvider#DEFAULT_CREDENTIALS_LOCATION_PROVIDER}
     */
    public FileSystem(String profileName, String endpoint) {
        mAmazonS3Client = AmazonS3ClientBuilder.standard().withCredentials(
                new ProfileCredentialsProvider(new ProfilesConfigFile(), profileName)).
                withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, ""))
                .build();

        mFileSystemOperationExecutor = new FileSystemOperationExecutor();

    }

    /**
     * This constructor is used for establishing connection to official amazon storage
     *
     * @param region      - name of region, you want to establish connection to
     * @param profileName - the name of profile, which credentials are contained into
     *                    {@link AwsProfileFileLocationProvider#DEFAULT_CREDENTIALS_LOCATION_PROVIDER}
     */
    public FileSystem(String profileName, Regions region) {
        mAmazonS3Client = AmazonS3ClientBuilder.standard().withCredentials(
                new ProfileCredentialsProvider(new ProfilesConfigFile(), profileName)).withRegion(region)
                .build();

        mFileSystemOperationExecutor = new FileSystemOperationExecutor();
    }

    /**
     * This method list files in given directory
     *
     * @param bucketName - where folder you want to list is contained
     * @param fileToList - directory you want to list
     * @param maxEntries - maximum amount of entries that will be listed in single batch
     * @return {@link ObjectListing} which contains listed files
     * @throws FileSystemOperationException in case of any error occurred during operation execution
     */
    ObjectListing list(String bucketName, String fileToList, int maxEntries) throws FileSystemOperationException {
        assert !(bucketName == null || bucketName.isEmpty()) : "Bucket name cannot be null";
        assert !(fileToList == null || fileToList.isEmpty()) : "Folder name cannot be null";
        assert maxEntries > 0 : "Entries to list cannot be less than 1";

        ListOperation listOperation = new ListOperation(mAmazonS3Client, bucketName, fileToList, maxEntries);
        mFileSystemOperationExecutor.executeFileSystemOperation(listOperation);

        return listOperation.getResult();
    }

    /**
     * This method is used for creating directory on S3 storage
     *
     * @param bucketName - bucket where you want directory to be created
     * @param folderName - name of directory you want to create
     * @return true if creation was successful, false otherwise
     * @throws FileSystemOperationException in case of any error occurred during operation execution
     */
    Boolean createDirectory(String bucketName, String folderName) throws FileSystemOperationException {
        assert !(bucketName == null || bucketName.isEmpty()) : "Bucket name cannot be null";
        assert !(folderName == null || folderName.isEmpty()) : "Folder name cannot be null";

        CreateDirectoryOperation createDirectoryOperation =
                new CreateDirectoryOperation(mAmazonS3Client, bucketName, folderName);
        mFileSystemOperationExecutor.executeFileSystemOperation(createDirectoryOperation);

        return createDirectoryOperation.getResult();
    }

    /**
     * This method is used for deleting file from S3 storage
     *
     * @param bucketName   - name of bucket where file is contained
     * @param fileToDelete - absolute path to file, which you want to delete
     * @return true if operation has been successful, false otherwise
     * @throws FileSystemOperationException in case of any error occurred during operation execution
     */
    Boolean delete(String bucketName, String fileToDelete) throws FileSystemOperationException {
        assert !(bucketName == null || bucketName.isEmpty()) : "Bucket name cannot be null";
        assert !(fileToDelete == null || fileToDelete.isEmpty()) : "File to delete cannot be null";

        DeleteFileOperation deleteFileOperation = new DeleteFileOperation(mAmazonS3Client, bucketName, fileToDelete);
        mFileSystemOperationExecutor.executeFileSystemOperation(deleteFileOperation);

        return deleteFileOperation.getResult();
    }

    /**
     * Reads file from local filesystem and upload it to S3 storage
     *
     * @param bucketName     - bucket where you want file to be uploaded
     * @param localFilePath  - path to file on local filesystem
     * @param remoteFilePath - path on the S3 storage where you want it to be upload
     * @return true if upload has been successful, false otherwise
     * @throws FileSystemOperationException in case of any error occurred during operation execution
     */
    Boolean uploadFile(String bucketName, String remoteFilePath, String localFilePath) throws FileSystemOperationException {
        assert !(bucketName == null || bucketName.isEmpty()) : "Bucket name cannot be null";
        assert !(remoteFilePath == null || remoteFilePath.isEmpty()) : "Folder name cannot be null";
        assert !(localFilePath == null || localFilePath.isEmpty()) : "Folder name cannot be null";

        UploadFileOperation uploadFileOperation =
                new UploadFileOperation(mAmazonS3Client, bucketName, remoteFilePath, localFilePath);
        mFileSystemOperationExecutor.executeFileSystemOperation(uploadFileOperation);

        return uploadFileOperation.getResult();
    }

    /**
     * This method is used for renaming files
     *
     * @param bucketName  - bucket where file is placed
     * @param oldFileName - the original name of file
     * @param newFileName - the file you want to be renamed to
     * @return true if operation has been successful
     * @throws FileSystemOperationException in case of any error occurred during operation execution
     */
    boolean rename(String bucketName, String oldFileName, String newFileName) throws FileSystemOperationException {
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
     * Checks if file exist in given bucket for given file path
     *
     * @param bucketName - bucket where you want to check for file existence
     * @param fileName   - absolute path to file you want to check for existence
     * @return true if file exists
     * @throws FileSystemOperationException in case of any error occurred during operation execution
     */
    boolean exist(String bucketName, String fileName) throws FileSystemOperationException {
        assert !(bucketName == null || bucketName.isEmpty()) : "Bucket name cannot be null";
        assert !(fileName == null || fileName.isEmpty()) :
                "Filename cannot be null";
        FileExistOperation fileExistOperation = new FileExistOperation(mAmazonS3Client, bucketName, fileName);

        mFileSystemOperationExecutor.executeFileSystemOperation(fileExistOperation);

        return fileExistOperation.getResult();
    }


}
