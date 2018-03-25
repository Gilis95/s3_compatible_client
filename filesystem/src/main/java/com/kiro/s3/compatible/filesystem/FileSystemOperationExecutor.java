package com.kiro.s3.compatible.filesystem;

import com.amazonaws.SdkBaseException;
import com.amazonaws.services.s3.model.AmazonS3Exception;


public class FileSystemOperationExecutor {

    public void executeFileSystemOperation(FileSystemOperation fileSystemOperation) throws Exception {
        try {
            fileSystemOperation.execute();
        } catch (AmazonS3Exception e) {
            if (e.getStatusCode() == 404) {
                throw new Exception("Object for rename was not found");
            }
            throw new Exception(e.getMessage(), e);
        } catch (SdkBaseException e) {
            throw new Exception(e.getMessage(), e);
        }
    }
}
