//
// Created by christian on 22.12.19.
//

#pragma once


#include <s3-client.h>
#include <aws/s3/model/CreateBucketRequest.h>
#include <aws/core/NoResult.h>
#include "client-configuration-provider.h"
#include "s3-client.h"

namespace Aws {
    namespace Browser {
        class UserInterface {
        private:
            const Aws::Browser::S3Client::S3OperationHelper operations;
        public:
            UserInterface(const Aws::Browser::S3Client::S3OperationHelper operations) : operations(operations) {
            }

            virtual void startInterface() = 0;

        protected:
            const Aws::Browser::S3Client::S3OperationHelper& getOperationsHelper() const {
                return operations;
            }
        };
    }
}