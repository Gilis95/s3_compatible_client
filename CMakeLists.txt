cmake_minimum_required(VERSION 3.13)

project(s3-browser)

set(CMAKE_CXX_STANDARD 11)

include_directories(/home/christian/workspace/c++/aws-sdk-cpp/aws-cpp-sdk-s3/include)
include_directories(/home/christian/workspace/c++/aws-sdk-cpp/aws-cpp-sdk-s3-encryption/include)
include_directories(/home/christian/workspace/c++/aws-sdk-cpp/aws-cpp-sdk-s3control/include)
include_directories(/home/christian/workspace/c++/aws-sdk-cpp/aws-cpp-sdk-core/include)
include_directories(/home/christian/workspace/c++/aws-sdk-cpp/.deps/install/include)
include_directories(include)

add_library(curl SHARED IMPORTED GLOBAL)
set_target_properties(curl PROPERTIES IMPORTED_LOCATION /usr/lib/libcurl.so)

add_library(openssl SHARED IMPORTED GLOBAL)
set_target_properties(openssl PROPERTIES IMPORTED_LOCATION /usr/lib/libcrypto.so)

# Your-external "mylib", add GLOBAL if the imported library is located in directories above the current.
add_library(aws-cpp-sdk-s3 SHARED IMPORTED GLOBAL)
# You can define two import-locations: one for debug and one for release.
set_target_properties(aws-cpp-sdk-s3 PROPERTIES IMPORTED_LOCATION /home/christian/workspace/c++/aws-sdk-cpp/aws-cpp-sdk-s3/libaws-cpp-sdk-s3.so)

add_library(aws-cpp-sdk-core SHARED IMPORTED GLOBAL)
set_target_properties(aws-cpp-sdk-core PROPERTIES IMPORTED_LOCATION /home/christian/workspace/c++/aws-sdk-cpp/aws-cpp-sdk-core/libaws-cpp-sdk-core.so)

add_library(aws-checksum SHARED IMPORTED GLOBAL)
set_target_properties(aws-checksum PROPERTIES IMPORTED_LOCATION /home/christian/workspace/c++/aws-sdk-cpp/.deps/install/lib/libaws-checksums.so)

add_executable(s3-browser src/s3-browser.cpp include/client-configuration-provider.h src/client-configuration-provider.cpp include/aws-browser-errors.h src/s3-client.cpp include/s3-client.h src/aws-browser-errors.cpp include/user-interface.h include/console-user-interface.h src/console-user-interface.cpp)

target_link_libraries(s3-browser curl)
target_link_libraries(s3-browser openssl)
target_link_libraries(s3-browser aws-cpp-sdk-core)
target_link_libraries(s3-browser aws-checksum)
target_link_libraries(s3-browser aws-cpp-sdk-s3)

install(TARGETS s3-browser DESTINATION bin)
