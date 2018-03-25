package com.kiro.s3.compatible.filesystem;

import com.amazonaws.services.s3.model.ObjectListing;

public class FileSystemService {
    private final FileSystem mFileSystem;
    private final String mBucketName;

    public FileSystemService(final FileSystem fileSystem, final String bucketName) {
        mFileSystem = fileSystem;
        mBucketName = bucketName;
    }


    public ObjectListing list(String fileToList, int maxEntries) throws FileSystemOperationException {
        assert !(fileToList == null || fileToList.isEmpty()) : "Folder name cannot be null";

        return mFileSystem.list(mBucketName, fileToList, maxEntries);
    }


    public Boolean createDirectory(String folderName) throws FileSystemOperationException {
        assert !(folderName == null || folderName.isEmpty()) : "Folder name cannot be null";

        return mFileSystem.createDirectory(mBucketName, folderName);
    }

    public Boolean delete(String fileToDelete) throws FileSystemOperationException {
        assert !(fileToDelete == null || fileToDelete.isEmpty()) : "File to delete cannot be null";

        return mFileSystem.delete(mBucketName, fileToDelete);
    }

    public boolean isFile(String fileName) {
        return !fileName.endsWith("/");
    }

    public Boolean uploadFile(String remoteFilePath, String localFilePath) throws FileSystemOperationException {
        assert !(remoteFilePath == null || remoteFilePath.isEmpty()) : "Folder name cannot be null";
        assert !(localFilePath == null || localFilePath.isEmpty()) : "Folder name cannot be null";

        return mFileSystem.uploadFile(mBucketName, remoteFilePath, localFilePath);
    }

    public Boolean rename(String oldFileName, String newFileName) throws FileSystemOperationException {
        assert !(oldFileName == null || oldFileName.isEmpty()) :
                "File which you want to rename cannot be null";
        assert !(newFileName == null || newFileName.isEmpty()) :
                "Name, which you want file to be renamed to cannot be null";

        return mFileSystem.rename(mBucketName, oldFileName, newFileName);
    }

    public boolean exist(String fileName) throws FileSystemOperationException {
        assert !(fileName == null || fileName.isEmpty()) :
                "Filename cannot be null";
        return mFileSystem.exist(mBucketName, fileName);
    }

}
