//
// Created by christian on 22.12.19.
//

#pragma once

#include "user-interface.h"

namespace Aws {
    namespace Browser {
        namespace ConsoleInterface {
            class ConsoleUserInterface : public UserInterface {
            private:
                std::map<std::string, void (*)(std::istringstream &,
                                               const Aws::Browser::S3Client::S3OperationHelper &)> operations;
            public:
                ConsoleUserInterface(const Aws::Browser::S3Client::S3OperationHelper operationHelper);

                void startInterface();
            };
        }
    }
}