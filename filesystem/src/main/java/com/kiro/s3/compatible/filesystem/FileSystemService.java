package com.kiro.s3.compatible.filesystem;

import com.amazonaws.services.s3.model.*;

public class FileSystemService {
    private final FileSystem mFileSystem;
    private final String mBucketName;

    public FileSystemService(final FileSystem fileSystem, final String bucketName) {
        mFileSystem = fileSystem;
        mBucketName = bucketName;
    }


    public ObjectListing list(String fileToList, int maxEntries) throws Exception {
        assert !(fileToList == null || fileToList.isEmpty()) : "Folder name cannot be null";

        return mFileSystem.list(mBucketName, fileToList, maxEntries);
    }


    public void createDirectory(String folderName) throws Exception {
        assert !(folderName == null || folderName.isEmpty()) : "Folder name cannot be null";

        mFileSystem.createDirectory(mBucketName, folderName);
    }

    public void delete(String fileToDelete) throws Exception {
        assert !(fileToDelete == null || fileToDelete.isEmpty()) : "File to delete cannot be null";

        mFileSystem.delete(mBucketName, fileToDelete);
    }

    public boolean isFile(String fileName) {
        return !fileName.endsWith("/");
    }

    public void uploadFile(String remoteFilePath, String localFilePath) throws Exception {
        assert !(remoteFilePath == null || remoteFilePath.isEmpty()) : "Folder name cannot be null";
        assert !(localFilePath == null || localFilePath.isEmpty()) : "Folder name cannot be null";

        mFileSystem.uploadFile(mBucketName, remoteFilePath, localFilePath);
    }

    public Boolean rename() {

    }

    public void getLastModificationDate() {

    }

}
