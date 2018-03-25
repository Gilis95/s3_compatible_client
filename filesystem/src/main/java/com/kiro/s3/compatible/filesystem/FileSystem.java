package com.kiro.s3.compatible.filesystem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.auth.profile.ProfilesConfigFile;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AbortMultipartUploadRequest;
import com.amazonaws.services.s3.model.CompleteMultipartUploadRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadResult;
import com.amazonaws.services.s3.model.PartETag;
import com.amazonaws.services.s3.model.UploadPartRequest;


public class FileSystem {

    final AmazonS3 mAmazonS3Client;

    public FileSystem(String profileName, String endpoint) {
        mAmazonS3Client = AmazonS3ClientBuilder.standard().withCredentials(
                new ProfileCredentialsProvider(new ProfilesConfigFile(), profileName)).
                withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint,""))
                .build();

    }

    public FileSystem(String profileName, Regions region){
        mAmazonS3Client = AmazonS3ClientBuilder.standard().withCredentials(
                new ProfileCredentialsProvider(new ProfilesConfigFile(), profileName)).withRegion(region)
                .build();

    }

    public void list(File fileToList) {

    }


    public void createDirectory() {

    }

    public void delete(File fileToDelete) {

    }

    public void deleteFile() {

    }

    public void isFile() {

    }

    public void uploadFile(String buckerName, String fileName) {

        // Create a list of UploadPartResponse objects. You get one of these for
        // each part upload.
        List<PartETag> partETags = new ArrayList<PartETag>();

        // Step 1: Initialize.
        InitiateMultipartUploadRequest initRequest = new InitiateMultipartUploadRequest(
                buckerName, fileName);
        InitiateMultipartUploadResult initResponse =
                mAmazonS3Client.initiateMultipartUpload(initRequest);

        File file = new File(filePath);
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
                        .withBucketName(existingBucketName).withKey(keyName)
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
                    CompleteMultipartUploadRequest(existingBucketName,
                    keyName,
                    initResponse.getUploadId(),
                    partETags);

            mAmazonS3Client.completeMultipartUpload(compRequest);
        } catch (Exception e) {
            mAmazonS3Client.abortMultipartUpload(new AbortMultipartUploadRequest(
                    existingBucketName, keyName, initResponse.getUploadId()));
        }
    }

    public void rename() {

    }

    public void getLastModificationDate() {

    }
}
