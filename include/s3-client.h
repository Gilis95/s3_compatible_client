//
// Created by christian on 24.11.19.
//

#pragma once

#include <aws/core/client/ClientConfiguration.h>
#include <aws/s3/S3Client.h>
#include <client-configuration-provider.h>

namespace Aws {
    namespace Browser {
        class S3Client {
        private:
            const Aws::S3::S3Client client;
        public:
            S3Client(const AwsConnectivityConfiguration &);

            Aws::Utils::Outcome<S3::Model::CreateBucketResult, Aws::Client::AWSError<Aws::S3::S3Errors>> &
            mkBucket(const Aws::String &) const;
        };
    }
}
