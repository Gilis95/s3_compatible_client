/*
   Copyright 2010-2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.
   This file is licensed under the Apache License, Version 2.0 (the "License").
   You may not use this file except in compliance with the License. A copy of
   the License is located at
    http://aws.amazon.com/apache2.0/
   This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
   CONDITIONS OF ANY KIND, either express or implied. See the License for the
   specific language governing permissions and limitations under the License.
*/

//snippet-start:[s3.cpp.create_bucket.inc]
#include <aws/core/Aws.h>
#include <aws/s3/S3Client.h>
#include <aws/s3/model/CreateBucketRequest.h>
#include <aws/core/http/HttpClientFactory.h>
#include <aws/core/auth/AWSCredentials.h>
#include <client-configuration-provider.h>
#include <s3-client.h>
#include <aws/core/utils/logging/ConsoleLogSystem.h>
#include <aws/core/utils/logging/DefaultLogSystem.h>
#include <console-user-interface.h>
//snippet-end:[s3.cpp.create_bucket.inc]

/**
 * Exercise create_bucket()
 */
int main() {
    // Set these values before compiling and running the program
    const Aws::String bucket_name_in_default_region = "pesho";
    const Aws::String bucket_name_in_specified_region = "gosho";
    const Aws::S3::Model::BucketLocationConstraint region =
            Aws::S3::Model::BucketLocationConstraint::us_west_2;

    Aws::Browser::AwsConnectivityConfigurationProvider configurationProvider;
    auto configurationOutcome = configurationProvider.parseClientConfigurationFromFile("pesho");

    if (!configurationOutcome.IsSuccess()) {
        std::cout << configurationOutcome.GetError() << std::endl;

    }

    const char *tag = "S3Browser";

    Aws::SDKOptions options;
    options.loggingOptions.logLevel = Aws::Utils::Logging::LogLevel::Info;
    options.loggingOptions.logger_create_fn = [&]() -> std::shared_ptr<Aws::Utils::Logging::LogSystemInterface> {
        return Aws::MakeShared<Aws::Utils::Logging::DefaultLogSystem>(tag,
                                                                      Aws::Utils::Logging::LogLevel::Info,
                                                                      "/home/christian/aws-browser");
    };

    Aws::InitAPI(options);

    Aws::Browser::ConsoleInterface::ConsoleUserInterface(
            Aws::Browser::S3Client::S3OperationHelper(configurationOutcome.GetResult())).startInterface();


    Aws::ShutdownAPI(options);
}
