package com.kiro.s3.compatible.filesystem.operations;

import com.amazonaws.services.s3.AmazonS3;

import com.amazonaws.services.s3.model.CopyObjectRequest;

public class CopyFileOperation extends FileSystemOperationBase {

    private final String mNewBucketName;
    private final String mOldFileName;
    private final String mNewFileName;

    private boolean mResult = false;


    public CopyFileOperation(AmazonS3 s3Client, String oldBucketName, String oldFileName, String newBucketName, String newFileName) {
        super(s3Client,oldBucketName);
        assert !(newBucketName== null || newBucketName.isEmpty()) : "New bucket name cannot be null";
        assert !(newFileName == null || newFileName.isEmpty()) : "New file name cannot be null";
        assert !(oldFileName == null || oldFileName.isEmpty()) : "Old file name cannot be null";

        mOldFileName = oldFileName;
        mNewFileName = newFileName;
        mNewBucketName = newBucketName;
    }

    @Override
    public void execute() {
        CopyObjectRequest copyObjRequest = new CopyObjectRequest(mBucketName,
                mOldFileName, mNewBucketName, mNewFileName);

        //copy oldObject to newly named one
        mAmazonS3Client.copyObject(copyObjRequest);

        mResult = true;
    }

    @Override
    public Boolean getResult() {
        return mResult;
    }
}
