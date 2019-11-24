//
// Created by christian on 19.11.19.
//

#pragma once

#include <aws/core/utils/memory/stl/AWSStreamFwd.h>
#include <aws/core/utils/memory/stl/AWSString.h>

namespace Aws {
    namespace Browser {
        enum ErrorCode {
            profileNotFound,
            none
        };


        class Error {
        public:
            Error();

            Error(const ErrorCode &errorCode, const Aws::String &message);


            /**
             * Gets underlying error code.
             */
            inline const ErrorCode &getErrorCode() const;

            /**
             * Gets the error message.
             */
            inline const Aws::String &getMessage() const;

        private:
            const ErrorCode errorCode;
            const Aws::String message;

            friend std::ostream &operator<<(std::ostream &os, const Error &e);
        };

        std::ostream &operator<<(std::ostream &os, const Error &e);
    } // namespace Browser
} // namespace Aws


