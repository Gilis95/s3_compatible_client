//
// Created by christian on 22.12.19.
//

#include <aws/s3/S3Errors.h>
#include <aws/core/NoResult.h>
#include <aws/core/utils/Outcome.h>
#include "console-user-interface.h"
#include "s3-client.h"


namespace Aws {
    namespace Browser {
        namespace ConsoleInterface {

            void mkBucket(std::istringstream &input, const Aws::Browser::S3Client::S3OperationHelper &operationHelper);


            void ConsoleUserInterface::startInterface() {
                std::string userInput;
                std::getline(std::cin, userInput);
                while (!std::getline(std::cin, userInput) && userInput != "exit") {
                    std::istringstream userInputStream(userInput);
                    std::string command;
                    userInputStream >> command;
                    auto operation = operations.find(command);
                    if (operation != operations.end()) {
                        operation->second(userInputStream, getOperationsHelper());
                        continue;
                    }

                    std::cout << "No such command" << userInput << std::endl;
                }
            }

            ConsoleUserInterface::ConsoleUserInterface(const Aws::Browser::S3Client::S3OperationHelper operationHelper)
                    : UserInterface(operationHelper) {
                operations["mkbucket"] = Aws::Browser::ConsoleInterface::mkBucket;
            }

            void mkBucket(std::istringstream &input, const Aws::Browser::S3Client::S3OperationHelper &operationHelper) {
                Aws::String bucket;

                input >> bucket;

                operationHelper.mkBucket(bucket);
            }
        }
    }
}