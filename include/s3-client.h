//
// Created by christian on 24.11.19.
//

#pragma once

#include <aws/core/client/ClientConfiguration.h>
#include <aws/s3/S3Client.h>
#include <client-configuration-provider.h>

namespace Aws {
    namespace Browser {
        namespace S3Client {
            class S3OperationHelper {
            private:
                Aws::S3::S3Client client;
            public:
                S3OperationHelper(const AwsConnectivityConfiguration &connectivityConfiguration) : client(
                        connectivityConfiguration.credentials,
                        connectivityConfiguration.clientConfiguration) { };

                Aws::S3::Model::CreateBucketOutcome mkBucket(const Aws::String &bucketName) const;

                Aws::S3::Model::GetObjectOutcome ls(const Aws::String &directory) const;
            };
        }
    }
}
