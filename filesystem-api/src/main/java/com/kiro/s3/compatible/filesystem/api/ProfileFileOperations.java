package com.kiro.s3.compatible.filesystem.api;

import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectListing;
import com.kiro.s3.compatible.filesystem.api.exceptions.FileSystemOperationException;

import java.util.List;

/**
 * @author christian
 */
public interface ProfileFileOperations {

    List<Bucket> listBuckets() throws FileSystemOperationException;


    /**
     * This method list files in given directory
     *
     * @param bucketName - where folder you want to list is contained
     * @param fileToList - directory you want to list
     * @param maxEntries - maximum amount of entries that will be listed in single batch
     * @return {@link ObjectListing} which contains listed files
     * @throws FileSystemOperationException in case of any error occurred during operation execution
     */
    ObjectListing list(String bucketName, String fileToList, int maxEntries) throws FileSystemOperationException;

    /**
     * This method is used for creating directory on S3 storage
     *
     * @param bucketName - bucket where you want directory to be created
     * @param folderName - name of directory you want to create
     * @return true if creation was successful, false otherwise
     * @throws FileSystemOperationException in case of any error occurred during operation execution
     */
    Boolean createDirectory(String bucketName, String folderName) throws FileSystemOperationException;

    /**
     * This method is used for deleting file from S3 storage
     *
     * @param bucketName   - name of bucket where file is contained
     * @param fileToDelete - absolute path to file, which you want to delete
     * @return true if operation has been successful, false otherwise
     * @throws FileSystemOperationException in case of any error occurred during operation execution
     */
    Boolean delete(String bucketName, String fileToDelete) throws FileSystemOperationException;

    /**
     * Reads file from local filesystem and upload it to S3 storage
     *
     * @param bucketName     - bucket where you want file to be uploaded
     * @param localFilePath  - path to file on local filesystem
     * @param remoteFilePath - path on the S3 storage where you want it to be upload
     * @return true if upload has been successful, false otherwise
     * @throws FileSystemOperationException in case of any error occurred during operation execution
     */
    Boolean uploadFile(String bucketName, String remoteFilePath, String localFilePath) throws FileSystemOperationException;

    /**
     * This method is used for renaming files
     *
     * @param bucketName  - bucket where file is placed
     * @param oldFileName - the original name of file
     * @param newFileName - the file you want to be renamed to
     * @return true if operation has been successful
     * @throws FileSystemOperationException in case of any error occurred during operation execution
     */
    boolean rename(String bucketName, String oldFileName, String newFileName) throws FileSystemOperationException;

    /**
     * Checks if file exist in given bucket for given file path
     *
     * @param bucketName - bucket where you want to check for file existence
     * @param fileName   - absolute path to file you want to check for existence
     * @return true if file exists
     * @throws FileSystemOperationException in case of any error occurred during operation execution
     */
    boolean exist(String bucketName, String fileName) throws FileSystemOperationException;
}
