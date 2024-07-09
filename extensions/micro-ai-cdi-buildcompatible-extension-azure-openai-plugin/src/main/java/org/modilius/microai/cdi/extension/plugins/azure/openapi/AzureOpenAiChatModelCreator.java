package org.modilius.microai.cdi.extension.plugins.azure.openapi;

import dev.langchain4j.model.azure.AzureOpenAiChatModel;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.build.compatible.spi.Parameters;
import jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanCreator;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.Optional;

public class AzureOpenAiChatModelCreator implements SyntheticBeanCreator<AzureOpenAiChatModel> {
    @Override
    public AzureOpenAiChatModel create(Instance<Object> lookup, Parameters params) {
        Config config = ConfigProvider.getConfig();
        return AzureOpenAiChatModel.builder()
                .apiKey(getConfigValue(config,"azure.openai.api.key"))
                .endpoint(getConfigValue(config,"azure.openai.endpoint"))
                .serviceVersion(getConfigValue(config, "azure.openai.service.version", "2024-02-15-preview"))
                .deploymentName(getConfigValue(config,"azure.openai.deployment.name"))
                .temperature(Double.valueOf(getConfigValue(config,"azure.openai.temperature","0.1")))
                .topP(Double.valueOf(getConfigValue(config,"azure.openai.top.p","0.1")))
                .timeout(Duration.ofSeconds(Integer.parseInt(getConfigValue(config,"azure.openai.timeout.seconds","120"))))
                .maxRetries(Integer.parseInt(getConfigValue(config,"azure.openai.max.retries","2")))
                .logRequestsAndResponses(Boolean.valueOf(getConfigValue(config,"azure.openai.log.requests.and.responses","false")))
                .build();
    }

    private static @NotNull String getConfigValue(Config config, String propertyName) {
        return getConfigValue(config,propertyName,null);
    }
    private static @NotNull String getConfigValue(Config config, String propertyName, String other) {
        return Optional.of(config.getConfigValue(propertyName).getValue()).orElse(other);
    }
}
