package com.kiro.s3.compatible.filesystem.operations;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UploadFileOperation extends FileSystemOperationBase {
    private final String mRemoteFilePath;
    private final String mLocalFilePath;
    private boolean mResult = false;

    public UploadFileOperation(final AmazonS3 s3Client, final String bucketName,
                               final String remoteFilePath, final String localFilePath) {
        super(s3Client, bucketName);

        assert !(remoteFilePath == null || remoteFilePath.isEmpty()) : "Folder name cannot be null";
        assert !(localFilePath == null || localFilePath.isEmpty()) : "Folder name cannot be null";


        mRemoteFilePath = remoteFilePath;
        mLocalFilePath = localFilePath;
    }

    @Override
    public void execute() {
        // Create a list of UploadPartResponse objects. You get one of these for
        // each part upload.
        List<PartETag> partETags = new ArrayList<>();

        // Step 1: Initialize.
        InitiateMultipartUploadRequest initRequest = new InitiateMultipartUploadRequest(
                mBucketName, mRemoteFilePath);
        InitiateMultipartUploadResult initResponse =
                mAmazonS3Client.initiateMultipartUpload(initRequest);

        File file = new File(mLocalFilePath);
        long contentLength = file.length();
        long partSize = 5 * 1024 * 1024; // Set part size to 5 MB.

        try {
            // Step 2: Upload parts.
            long filePosition = 0;
            for (int i = 1; filePosition < contentLength; i++) {
                // Last part can be less than 5 MB. Adjust part size.
                partSize = Math.min(partSize, (contentLength - filePosition));

                // Create request to upload a part.
                UploadPartRequest uploadRequest = new UploadPartRequest()
                        .withBucketName(mBucketName).withKey(mRemoteFilePath)
                        .withUploadId(initResponse.getUploadId()).withPartNumber(i)
                        .withFileOffset(filePosition)
                        .withFile(file)
                        .withPartSize(partSize);

                // Upload part and add response to our list.
                partETags.add(mAmazonS3Client.uploadPart(uploadRequest).getPartETag());

                filePosition += partSize;
            }

            // Step 3: Complete.
            CompleteMultipartUploadRequest compRequest = new
                    CompleteMultipartUploadRequest(mBucketName,
                    mRemoteFilePath,
                    initResponse.getUploadId(),
                    partETags);

            mAmazonS3Client.completeMultipartUpload(compRequest);
            mResult = true;
        } catch (Exception e) {
            mAmazonS3Client.abortMultipartUpload(new AbortMultipartUploadRequest(
                    mBucketName, mRemoteFilePath, initResponse.getUploadId()));
        }
    }

    @Override
    public Boolean getResult() {
        return mResult;
    }
}
