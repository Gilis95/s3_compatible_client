package com.kiro.s3.compatible.filesystem.operations;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;

import java.util.List;

public class ListBucketOperation extends FileSystemOperationBase {

    List<Bucket> mResult;

    public ListBucketOperation(AmazonS3 s3Client) {
        super(s3Client);
    }

    @Override
    public void execute() {
        mResult = mAmazonS3Client.listBuckets();
    }

    @Override
    public List<Bucket> getResult() {
        return mResult;
    }
}
