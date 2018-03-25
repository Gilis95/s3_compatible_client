package com.kiro.s3.compatible.filesystem.operations;

import com.amazonaws.services.s3.AmazonS3;

public class FileExistOperation extends FileSystemOperationBase {
    private boolean mResult;
    private final String mFileName;

    public FileExistOperation(final AmazonS3 s3Client, final String bucketName, final String fileName) {
        super(s3Client, bucketName);
        assert !(fileName == null || fileName.isEmpty()) : "Filename cannot be null";

        mFileName = fileName;

    }


    @Override
    public void execute() {
        mResult = mAmazonS3Client.doesObjectExist(mBucketName, mFileName);
    }

    @Override
    public Boolean getResult() {
        return mResult;
    }
}
