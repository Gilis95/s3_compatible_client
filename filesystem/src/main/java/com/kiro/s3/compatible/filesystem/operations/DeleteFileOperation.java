package com.kiro.s3.compatible.filesystem.operations;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;

public class DeleteFileOperation extends FileSystemOperationBase {
    private String mFileToDelete;
    private boolean mResult= false;

    public DeleteFileOperation(AmazonS3 s3Client, String bucketName, String fileToDelete) {
        super(s3Client, bucketName);
        assert !(fileToDelete == null || fileToDelete.isEmpty()) : "File for deletion cannot be null";
        mFileToDelete = fileToDelete;
    }

    @Override
    public void execute() {
        DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(mBucketName, mFileToDelete);

        mAmazonS3Client.deleteObject(deleteObjectRequest);
        mResult = true;
    }

    @Override
    public Boolean getResult() {
        return mResult;
    }
}
