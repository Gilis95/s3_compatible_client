//
// Created by christian on 18.11.19.
//

#pragma once

#include <aws/core/client/ClientConfiguration.h>
#include <aws/core/auth/AWSCredentials.h>
#include <map>
#include <aws-browser-errors.h>
#include <aws/core/utils/Outcome.h>
#include <aws/core/config/AWSProfileConfigLoader.h>

namespace Aws {
    namespace Browser {
        static const char *CLIENT_CONFIG_TAG = "ClientConfiguration";

        using ProfilesContainer = Aws::Map<Aws::String, Aws::Config::Profile>;

        static const Aws::String ENDPOINT_PROPERTY_NAME = "aws_endpoint";

        static const Aws::String SCHEME_PROPERTY_NAME = "aws_schema";

        static const Aws::String VERIFY_SSL_PROPERTY_NAME= "aws_verify_ssl";

        static const Aws::String  CA_PATH_PROPERTY_NAME= "aws_ca_path";

        static const Aws::String  CA_FILE_PROPERTY_NAME= "aws_ca_file";

        static const Aws::String REGION_PROPERTY_NAME= "aws_region";

        static const Aws::String USE_DUAL_STACK_PROPERTY_NAME= "aws_use_dual_stack";

        static const Aws::String MAX_CONNECTIONS_PROPERTY_NAME= "aws_max_connections";

        static const Aws::String HTTP_REQUEST_TIMEOUT_MS_PROPERTY_NAME= "aws_http_request_timeout_ms";

        static const Aws::String REQUEST_TIMEOUT_MS_PROPERTY_NAME= "aws_request_timeout_ms";

        static const Aws::String CONNECT_TIMEOUT_MS_PROPERTY_NAME= "aws_connect_timeout_ms";

        static const Aws::String ENABLE_TCP_KEEP_ALIVE_PROPERTY_NAME= "aws_enable_tcp_keep_alive";

        static const Aws::String TCP_KEEP_ALIVE_INTERVAL_MS_PROPERTY_NAME= "aws_tcp_keep_alive_interval_ms";

        static const Aws::String LOW_SPEED_LIMIT_PROPERTY_NAME= "aws_low_speed_limit";

        static const Aws::String FOLLOW_REDIRECTS_PROPERTY_NAME = "aws_follow_redirects";

        static const Aws::String DISABLE_EXPECT_HEADER_PROPERTY_NAME = "aws_disable_expect_header";

        static const Aws::String ENABLE_CLOCK_SKEW_ADJUSTMENT_PROPERTY_NAME = "aws_enable_clock_skew_adjustment";

        static const Aws::String ENABLE_HOST_PREFIX_INJECTION_PROPERTY_NAME = "aws_enable_host_prefix_injection";

        static const Aws::String ENABLE_ENDPOINT_DISCOVERY_PROPERTY_NAME = "aws_enable_endpoint_discovery";


        struct AwsConnectivityConfiguration {
            Aws::Client::ClientConfiguration clientConfiguration;
            Aws::Auth::AWSCredentials credentials;
        };

        class AwsConnectivityConfigurationProvider {
        private:
            std::map<Aws::String, AwsConnectivityConfiguration> configurationProfileMap;
            ProfilesContainer availableProfiles;
        public:
            Aws::Utils::Outcome<AwsConnectivityConfiguration, Aws::Browser::Error>
            parseClientConfigurationFromFile(const Aws::String &profileName);

            void loadConfiguration();

            AwsConnectivityConfigurationProvider() noexcept;
        };

        void insertProperty(Aws::Client::ClientConfiguration &clientConfiguration, const Aws::Config::Profile &profile,
                            const Aws::String &propertyName,
                            const std::function<void(const Aws::String&,
                                                     Aws::Client::ClientConfiguration&)> &propertyInsertFunction);
    }
}