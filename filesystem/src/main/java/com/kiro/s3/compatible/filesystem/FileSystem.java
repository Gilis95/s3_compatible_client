package com.kiro.s3.compatible.filesystem;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.auth.profile.ProfilesConfigFile;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectListing;
import com.kiro.s3.compatible.filesystem.operations.*;

public class FileSystem {
    private final AmazonS3 mAmazonS3Client;
    private final FileSystemOperationExecutor mFileSystemOperationExecutor;

    public FileSystem(String profileName, String endpoint) {
        mAmazonS3Client = AmazonS3ClientBuilder.standard().withCredentials(
                new ProfileCredentialsProvider(new ProfilesConfigFile(), profileName)).
                withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, ""))
                .build();

        mFileSystemOperationExecutor = new FileSystemOperationExecutor();

    }

    public FileSystem(String profileName, Regions region) {
        mAmazonS3Client = AmazonS3ClientBuilder.standard().withCredentials(
                new ProfileCredentialsProvider(new ProfilesConfigFile(), profileName)).withRegion(region)
                .build();

        mFileSystemOperationExecutor = new FileSystemOperationExecutor();
    }

    public ObjectListing list(String bucketName, String fileToList, int maxEntries) throws Exception {
        assert !(bucketName == null || bucketName.isEmpty()) : "Bucket name cannot be null";
        assert !(fileToList == null || fileToList.isEmpty()) : "Folder name cannot be null";

        ListOperation listOperation = new ListOperation(mAmazonS3Client, bucketName, fileToList, maxEntries);
        mFileSystemOperationExecutor.executeFileSystemOperation(listOperation);

        return listOperation.getResult();
    }


    public Boolean createDirectory(String bucketName, String folderName) throws Exception {
        assert !(bucketName == null || bucketName.isEmpty()) : "Bucket name cannot be null";
        assert !(folderName == null || folderName.isEmpty()) : "Folder name cannot be null";

        CreateDirectoryOperation createDirectoryOperation =
                new CreateDirectoryOperation(mAmazonS3Client, bucketName, folderName);
        mFileSystemOperationExecutor.executeFileSystemOperation(createDirectoryOperation);

        return createDirectoryOperation.getResult();
    }

    public Boolean delete(String bucketName, String fileToDelete) throws Exception {
        assert !(bucketName == null || bucketName.isEmpty()) : "Bucket name cannot be null";
        assert !(fileToDelete == null || fileToDelete.isEmpty()) : "File to delete cannot be null";

        DeleteFileOperation deleteFileOperation = new DeleteFileOperation(mAmazonS3Client, bucketName, fileToDelete);
        mFileSystemOperationExecutor.executeFileSystemOperation(deleteFileOperation);

        return deleteFileOperation.getResult();
    }

    public boolean isFile(String fileName) {
        return !fileName.endsWith("/");
    }

    public Boolean uploadFile(String bucketName, String remoteFilePath, String localFilePath) throws Exception {
        assert !(bucketName == null || bucketName.isEmpty()) : "Bucket name cannot be null";
        assert !(remoteFilePath == null || remoteFilePath.isEmpty()) : "Folder name cannot be null";
        assert !(localFilePath == null || localFilePath.isEmpty()) : "Folder name cannot be null";

        UploadFileOperation uploadFileOperation =
                new UploadFileOperation(mAmazonS3Client, bucketName, remoteFilePath, localFilePath);
        mFileSystemOperationExecutor.executeFileSystemOperation(uploadFileOperation);

        return uploadFileOperation.getResult();
    }

    public boolean rename(String bucketName, String oldFileName, String newFileName) throws Exception {
        assert !(bucketName == null || bucketName.isEmpty()) : "Bucket name cannot be null";
        assert !(oldFileName == null || oldFileName.isEmpty()) : "File which you want to rename cannot be null";
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

    public boolean exist(String bucketName, String fileName) throws Exception {
        FileExistOperation fileExistOperation = new FileExistOperation(mAmazonS3Client, bucketName, fileName);

        mFileSystemOperationExecutor.executeFileSystemOperation(fileExistOperation);

        return fileExistOperation.getResult();
    }


}
