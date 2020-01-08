//
// Created by christian on 22.12.19.
//

#include <aws/s3/S3Errors.h>
#include <aws/core/NoResult.h>
#include <aws/core/utils/Outcome.h>
#include "console-user-interface.h"

namespace Aws {
    namespace Browser {
        namespace ConsoleInterface {

            void mkBucket(const std::string& commandName, std::istringstream &input,
                          const Aws::Browser::S3Client::S3OperationHelper &operationHelper);


            template<typename Result>
            void handleError(const std::string& commandName,
                             Aws::Utils::Outcome<Result, Aws::Client::AWSError<Aws::S3::S3Errors>> result);

            void ConsoleUserInterface::startInterface() {
                std::string userInput;

                while (std::getline(std::cin, userInput) && userInput != "exit") {
                    std::istringstream userInputStream(userInput);
                    std::string command;
                    userInputStream >> command;
                    auto operation = operations.find(command);
                    if (operation != operations.end()) {
                        operation->second(command,userInputStream, getOperationsHelper());
                        continue;
                    }

                    std::cout << "No such command \"" << userInput << "\"."<<std::endl;
                }
            }

            ConsoleUserInterface::ConsoleUserInterface(const Aws::Browser::S3Client::S3OperationHelper operationHelper)
                    : UserInterface(operationHelper) {
                operations["mkbucket"] = Aws::Browser::ConsoleInterface::mkBucket;
            }

            void mkBucket(const std::string& commandName, std::istringstream &input,
                          const Aws::Browser::S3Client::S3OperationHelper &operationHelper) {
                Aws::String bucket;

                input >> bucket;

                handleError(commandName, operationHelper.mkBucket(bucket));
            }

            template<typename Result>
            void handleError(const std::string& commandName,
                             Aws::Utils::Outcome<Result, Aws::Client::AWSError<Aws::S3::S3Errors>> result) {
                if (result.IsSuccess()) {
                    std::cout << "Command \"" << commandName << "\" has been executed successfully." << std::endl;
                } else {
                    std::cout << "Command \"" << commandName << "\" has failed with following error \"" <<
                    result.GetError().GetMessage() << "\"."<< std::endl;
                }

            }
        }
    }
}