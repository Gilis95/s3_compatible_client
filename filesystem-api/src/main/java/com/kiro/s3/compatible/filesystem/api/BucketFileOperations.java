package com.kiro.s3.compatible.filesystem.api;

import com.amazonaws.services.s3.model.ObjectListing;
import com.kiro.s3.compatible.filesystem.api.exceptions.FileSystemOperationException;

public interface BucketFileOperations {

    ObjectListing list(String fileToList, int maxEntries) throws FileSystemOperationException;

    Boolean createDirectory(String folderName) throws FileSystemOperationException;

    Boolean delete(String fileToDelete) throws FileSystemOperationException;

    boolean isFile(String fileName);

    Boolean uploadFile(String remoteFilePath, String localFilePath) throws FileSystemOperationException;

    Boolean rename(String oldFileName, String newFileName) throws FileSystemOperationException;

    boolean exist(String fileName) throws FileSystemOperationException;

}
