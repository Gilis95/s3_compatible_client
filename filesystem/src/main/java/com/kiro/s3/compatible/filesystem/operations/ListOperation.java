package com.kiro.s3.compatible.filesystem.operations;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;

public class ListOperation extends FileSystemOperationBase {

    private final String mFileToList;
    private final int mMaxEntries;

    private ObjectListing mResult;


    public ListOperation(AmazonS3 s3Client, String bucketName, String fileToList, int maxEntries) {
        super(s3Client, bucketName);

        assert !(fileToList == null || fileToList.isEmpty()) : "Folder name cannot be null";

        mFileToList = fileToList;
        mMaxEntries = maxEntries;
    }

    @Override
    public void execute() {
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
                .withBucketName(mBucketName)
                .withPrefix(mFileToList)
                .withMaxKeys(mMaxEntries);

        mResult = mAmazonS3Client.listObjects(listObjectsRequest);
    }

    @Override
    public ObjectListing getResult() {
        return mResult;
    }
}
