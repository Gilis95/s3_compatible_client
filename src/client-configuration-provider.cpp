//
// Created by christian on 18.11.19.
//
#include <client-configuration-provider.h>
#include <aws/core/client/ClientConfiguration.h>
#include <aws/core/config/AWSProfileConfigLoader.h>
#include <aws/core/auth/AWSCredentialsProvider.h>
#include <aws/core/utils/logging/LogMacros.h>

namespace Aws {
    namespace Browser {
        AwsConnectivityConfigurationProvider::AwsConnectivityConfigurationProvider() noexcept {
            loadConfiguration();
        }

        void convertProfileToClientConfiguration(const Aws::Config::Profile &profile,
                                                 Aws::Client::ClientConfiguration& clientConfiguration);

        Aws::Utils::Outcome<AwsConnectivityConfiguration, Aws::Browser::Error>
        AwsConnectivityConfigurationProvider::parseClientConfigurationFromFile(const Aws::String &profileName) {
            //check if profiles found in file contains profile with such name
            auto it = availableProfiles.find(profileName);
            if (it == availableProfiles.end()) {
                Aws::String errorMessage;

                errorMessage.reserve(51 + profileName.size());

                errorMessage.append("Failed to load profile: [").append(profileName).append(
                        "] from configuration file.");

                return Aws::Utils::Outcome<AwsConnectivityConfiguration, Aws::Browser::Error>(
                        Error(Aws::Browser::ErrorCode::profileNotFound, errorMessage));
            }

            const Aws::Config::Profile &profile = it->second;

            //check if we already construct such configuration
            auto iter = configurationProfileMap.find(profileName);
            if (iter != configurationProfileMap.end()) {
                return iter->second;
            }

            Aws::Client::ClientConfiguration clientConfiguration;

            convertProfileToClientConfiguration(profile, clientConfiguration);

            return Aws::Utils::Outcome<AwsConnectivityConfiguration, Aws::Browser::Error>(
                    AwsConnectivityConfiguration{clientConfiguration, profile.GetCredentials()});
        }

        void convertProfileToClientConfiguration(const Aws::Config::Profile &profile,
                                                 Aws::Client::ClientConfiguration& clientConfiguration) {
            insertProperty(clientConfiguration, profile, ENDPOINT_PROPERTY_NAME,
                           [](const Aws::String &propertyValue,
                              Aws::Client::ClientConfiguration &configuration) -> void {
                               configuration.endpointOverride = propertyValue;
                           });
            insertProperty(clientConfiguration, profile, SCHEME_PROPERTY_NAME,
                           [](const Aws::String &propertyValue,
                              Aws::Client::ClientConfiguration &configuration) -> void {
                               if (propertyValue == "HTTP" || propertyValue == "http") {
                                   configuration.scheme = Aws::Http::Scheme::HTTP;
                               }
                           });
            insertProperty(clientConfiguration, profile, CA_FILE_PROPERTY_NAME,
                           [](const Aws::String &propertyValue,
                              Aws::Client::ClientConfiguration &configuration) -> void {
                               if (configuration.scheme == Aws::Http::Scheme::HTTPS) {
                                   configuration.caFile = propertyValue;
                               }
                           });
            insertProperty(clientConfiguration, profile, CA_PATH_PROPERTY_NAME,
                           [](const Aws::String &propertyValue,
                              Aws::Client::ClientConfiguration &configuration) -> void {
                               if (configuration.scheme == Aws::Http::Scheme::HTTPS) {
                                   configuration.caPath = propertyValue;
                               }
                           });

            insertProperty(clientConfiguration, profile, ENABLE_TCP_KEEP_ALIVE_PROPERTY_NAME,
                           [](const Aws::String &propertyValue,
                              Aws::Client::ClientConfiguration &configuration) -> void {
                               bool b;
                               std::istringstream(propertyValue.c_str()) >> b;
                               configuration.enableTcpKeepAlive = b;
                           });

            insertProperty(clientConfiguration, profile, ENABLE_CLOCK_SKEW_ADJUSTMENT_PROPERTY_NAME,
                           [](const Aws::String &propertyValue,
                              Aws::Client::ClientConfiguration &configuration) -> void {
                               bool b;
                               std::istringstream(propertyValue.c_str()) >> b;
                               configuration.enableClockSkewAdjustment = b;
                           });

            insertProperty(clientConfiguration, profile, ENABLE_ENDPOINT_DISCOVERY_PROPERTY_NAME,
                           [](const Aws::String &propertyValue,
                              Aws::Client::ClientConfiguration &configuration) -> void {
                               bool b;
                               std::istringstream(propertyValue.c_str()) >> b;
                               configuration.enableEndpointDiscovery = b;
                           });

            insertProperty(clientConfiguration, profile, ENABLE_HOST_PREFIX_INJECTION_PROPERTY_NAME,
                           [](const Aws::String &propertyValue,
                              Aws::Client::ClientConfiguration &configuration) -> void {
                               bool b;
                               std::istringstream(propertyValue.c_str()) >> b;
                               configuration.enableHostPrefixInjection = b;
                           });

            insertProperty(clientConfiguration, profile, CONNECT_TIMEOUT_MS_PROPERTY_NAME,
                           [](const Aws::String &propertyValue,
                              Aws::Client::ClientConfiguration &configuration) -> void {
                               configuration.connectTimeoutMs = std::stol(propertyValue.c_str());
                           });

            insertProperty(clientConfiguration, profile, DISABLE_EXPECT_HEADER_PROPERTY_NAME,
                           [](const Aws::String &propertyValue,
                              Aws::Client::ClientConfiguration &configuration) -> void {
                               bool b;
                               std::istringstream(propertyValue.c_str()) >> b;
                               configuration.disableExpectHeader = b;
                           });

            insertProperty(clientConfiguration, profile, FOLLOW_REDIRECTS_PROPERTY_NAME,
                           [](const Aws::String &propertyValue,
                              Aws::Client::ClientConfiguration &configuration) -> void {
                               bool b;
                               std::istringstream(propertyValue.c_str()) >> b;
                               configuration.followRedirects = b;
                           });

            insertProperty(clientConfiguration, profile, HTTP_REQUEST_TIMEOUT_MS_PROPERTY_NAME,
                           [](const Aws::String &propertyValue,
                              Aws::Client::ClientConfiguration &configuration) -> void {
                               configuration.httpRequestTimeoutMs = std::stol(propertyValue.c_str());
                           });

            insertProperty(clientConfiguration, profile, LOW_SPEED_LIMIT_PROPERTY_NAME,
                           [](const Aws::String &propertyValue,
                              Aws::Client::ClientConfiguration &configuration) -> void {
                               configuration.lowSpeedLimit = std::stol(propertyValue.c_str());
                           });

            insertProperty(clientConfiguration, profile, MAX_CONNECTIONS_PROPERTY_NAME,
                           [](const Aws::String &propertyValue,
                              Aws::Client::ClientConfiguration &configuration) -> void {
                               configuration.maxConnections = std::stoi(propertyValue.c_str());
                           });

            insertProperty(clientConfiguration, profile, REGION_PROPERTY_NAME,
                           [](const Aws::String &propertyValue,
                              Aws::Client::ClientConfiguration &configuration) -> void {
                               configuration.region = propertyValue;
                           });

            insertProperty(clientConfiguration, profile, REQUEST_TIMEOUT_MS_PROPERTY_NAME,
                           [](const Aws::String &propertyValue,
                              Aws::Client::ClientConfiguration &configuration) -> void {
                               configuration.requestTimeoutMs = std::stol(propertyValue.c_str());
                           });

            insertProperty(clientConfiguration, profile, TCP_KEEP_ALIVE_INTERVAL_MS_PROPERTY_NAME,
                           [](const Aws::String &propertyValue,
                              Aws::Client::ClientConfiguration &configuration) -> void {
                               configuration.tcpKeepAliveIntervalMs = std::stol(propertyValue.c_str());
                           });

            insertProperty(clientConfiguration, profile, USE_DUAL_STACK_PROPERTY_NAME,
                           [](const Aws::String &propertyValue,
                              Aws::Client::ClientConfiguration &configuration) -> void {
                               bool b;
                               std::istringstream(propertyValue.c_str()) >> b;
                               configuration.useDualStack = b;
                           });

            insertProperty(clientConfiguration, profile, VERIFY_SSL_PROPERTY_NAME,
                           [](const Aws::String &propertyValue,
                              Aws::Client::ClientConfiguration &configuration) -> void {
                               bool b;
                               std::istringstream(propertyValue.c_str()) >> b;
                               configuration.verifySSL = b;
                           });
        }

        void insertProperty(Aws::Client::ClientConfiguration &clientConfiguration, const Aws::Config::Profile &profile,
                            const Aws::String &propertyName,
                            const std::function<void(const Aws::String &,
                                                     Aws::Client::ClientConfiguration &)> &propertyInsertFunction) {
            auto propertyValue = profile.GetValue(propertyName);
            if (!propertyValue.empty()) {
                propertyInsertFunction(propertyValue, clientConfiguration);
            }
        }

        void AwsConnectivityConfigurationProvider::loadConfiguration() {
            const Aws::String configFilename = Aws::Auth::GetConfigProfileFilename();
            Aws::Config::AWSConfigFileProfileConfigLoader configLoader(configFilename, true);
            configLoader.Load();
            availableProfiles = configLoader.GetProfiles();
        }
    }
}