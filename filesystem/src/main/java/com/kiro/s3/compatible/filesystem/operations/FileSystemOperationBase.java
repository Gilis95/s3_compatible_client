package com.kiro.s3.compatible.filesystem.operations;

import com.amazonaws.services.s3.AmazonS3;
import com.kiro.s3.compatible.filesystem.FileSystemOperation;

abstract class FileSystemOperationBase implements FileSystemOperation {
    final AmazonS3 mAmazonS3Client;
    final String mBucketName;

    FileSystemOperationBase(final AmazonS3 s3Client, final String bucketName) {
        assert !(s3Client == null) : "Amazon S3 cl;ient cannot be null";
        assert !(bucketName == null || bucketName.isEmpty()) : "Bucket name cannot be null";

        mAmazonS3Client = s3Client;
        mBucketName = bucketName;
    }

    FileSystemOperationBase(final AmazonS3 s3Client) {
        assert !(s3Client == null) : "Amazon S3 cl;ient cannot be null";

        mBucketName = null;
        mAmazonS3Client = s3Client;
    }
}
