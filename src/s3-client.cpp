//
// Created by christian on 24.11.19.
//

#include <s3-client.h>
#include <aws/s3/model/CreateBucketRequest.h>
#include <aws/core/utils/logging/LogLevel.h>
#include <aws/core/utils/logging/LogMacros.h>


namespace Aws {
    namespace Browser {
        static const char *LOG_TAG = "S3Client";

        S3Client::S3Client(const AwsConnectivityConfiguration &connectivityConfiguration) : client(
                connectivityConfiguration.credentials, connectivityConfiguration.clientConfiguration) {}

        template<class Result>
        Aws::Utils::Outcome<Result, Aws::Client::AWSError<Aws::S3::S3Errors>> &
        handleError(Aws::Utils::Outcome<Result, Aws::Client::AWSError<Aws::S3::S3Errors>> &outcome,
                    const std::string &errorMessage);

        Aws::Utils::Outcome<S3::Model::CreateBucketResult, Aws::Client::AWSError<Aws::S3::S3Errors>> &
        S3Client::mkBucket(const Aws::String &bucketName) const {

            // Set up the request
            Aws::S3::Model::CreateBucketRequest request;
            request.SetBucket(bucketName);

            auto outcome = client.CreateBucket(request);

            return handleError(outcome, "ERROR: CreateBucket: ");
        }

        template<class Result>
        Aws::Utils::Outcome<Result, Aws::Client::AWSError<Aws::S3::S3Errors>> &
        handleError(Aws::Utils::Outcome<Result, Aws::Client::AWSError<Aws::S3::S3Errors>> &outcome,
                    const std::string &errorMessage) {
            if (!outcome.IsSuccess()) {
                AWS_LOGSTREAM_ERROR(LOG_TAG, errorMessage << outcome.GetError() << std::endl);
            }

            return outcome;
        }
    }
}