package com.kiro.s3.compatible.filesystem;

import com.amazonaws.SdkBaseException;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.kiro.s3.compatible.filesystem.api.exceptions.FileSystemOperationException;

/**
 * This class is used for executing {@link FileSystemOperation} implementations
 *
 * @author christian
 */
public class FileSystemOperationExecutor {

    /**
     * This method executes provided {@link FileSystemOperation} and wraps {@link SdkBaseException}
     * which is runtime exception into {@link FileSystemOperationException}
     */
    public void executeFileSystemOperation(FileSystemOperation fileSystemOperation) throws FileSystemOperationException {
        try {
            fileSystemOperation.execute();
        } catch (AmazonS3Exception e) {
            if (e.getStatusCode() == 404) {
                throw new FileSystemOperationException("Object for rename was not found");
            }
            throw new FileSystemOperationException(e.getMessage(), e);
        } catch (SdkBaseException e) {
            throw new FileSystemOperationException(e.getMessage(), e);
        }
    }
}
