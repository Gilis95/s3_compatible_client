//
// Created by christian on 24.11.19.
//
#include <aws/core/utils/memory/stl/AWSStreamFwd.h>
#include <aws-browser-errors.h>


namespace Aws {
    namespace Browser {

        Error::Error() : errorCode(none) {}

        Error::Error(const ErrorCode &errorCode, const Aws::String &message) : errorCode(errorCode), message(message) {}


        /**
         * Gets underlying error code.
         */
        inline const ErrorCode &Error::getErrorCode() const { return errorCode; }

        /**
         * Gets the error message.
         */
        inline const Aws::String &Error::getMessage() const { return message; }


        std::ostream &operator<<(std::ostream &os, const Error &e) {
            return os << "Error message: " << e.getMessage() << "\n"
                      << "Error code: " << e.errorCode << " response headers:";
        }

    } // namespace Browser
} // namespace Aws