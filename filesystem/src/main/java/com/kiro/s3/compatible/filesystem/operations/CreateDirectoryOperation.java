package com.kiro.s3.compatible.filesystem.operations;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class CreateDirectoryOperation extends FileSystemOperationBase {
    private final String mFolderName;
    private boolean mResult = false;

    public CreateDirectoryOperation(final AmazonS3 s3Client, final String bucketName, String folderName) {
        super(s3Client, bucketName);
        assert !(folderName == null || folderName.isEmpty()) : "Name of the folder cannot be null";


        mFolderName = folderName;
    }

    @Override
    public void execute() {
        String fullFolderName = mFolderName;
        if (!mFolderName.endsWith("/")) {
            fullFolderName = mFolderName.concat("/");
        }

        // create meta-data for your folder and set content-length to 0
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(0);

        // create empty content
        InputStream emptyContent = new ByteArrayInputStream(new byte[0]);

        // create a PutObjectRequest passing the folder name suffixed by /
        PutObjectRequest putObjectRequest = new PutObjectRequest(mBucketName,
                fullFolderName, emptyContent, metadata);

        // send request to S3 to create folder
        mAmazonS3Client.putObject(putObjectRequest);
        mResult = true;
    }

    @Override
    public Boolean getResult() {
        return mResult;
    }
}
