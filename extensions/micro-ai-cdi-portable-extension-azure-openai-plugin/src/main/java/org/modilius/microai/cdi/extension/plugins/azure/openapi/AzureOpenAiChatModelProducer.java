package org.modilius.microai.cdi.extension.plugins.azure.openapi;

import dev.langchain4j.model.azure.AzureOpenAiChatModel;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.Duration;

@ApplicationScoped
public class AzureOpenAiChatModelProducer {

    @Inject
    @ConfigProperty(name = "azure.openai.endpoint")
    private String AZURE_OPENAI_ENDPOINT;

    @Inject
    @ConfigProperty(name = "azure.openai.api.key")
    private String AZURE_OPENAI_KEY;

    @Inject
    @ConfigProperty(name = "azure.openai.deployment.name")
    private String AZURE_OPENAI_DEPLOYMENT_NAME;

    @Inject
    @ConfigProperty(name = "azure.openai.service.version", defaultValue = "2024-02-15-preview")
    private String AZURE_OPENAI_SERVICE_VERSION;

    @Inject
    @ConfigProperty(name = "azure.openai.temperature", defaultValue = "0.1")
    private Double AZURE_OPENAI_TEMPERATURE;

    @Inject
    @ConfigProperty(name = "azure.openai.top.p", defaultValue = "0.1")
    private Double AZURE_OPENAI_TOP_P;

    @Inject
    @ConfigProperty(name = "azure.openai.timeout.seconds", defaultValue = "120")
    private Integer AZURE_OPENAI_TIMEOUT_SECONDS;

    @Inject
    @ConfigProperty(name = "azure.openai.max.retries", defaultValue = "2")
    private Integer AZURE_OPENAI_MAX_RETRIES;

    @Inject
    @ConfigProperty(name = "azure.openai.log.requests.and.responses", defaultValue = "false")
    private Boolean AZURE_OPENAI_LOG_REQUESTS_AND_RESPONSES;

    @Produces
    public AzureOpenAiChatModel initModel() {
        return AzureOpenAiChatModel.builder()
                .apiKey(AZURE_OPENAI_KEY)
                .endpoint(AZURE_OPENAI_ENDPOINT)
                .serviceVersion(AZURE_OPENAI_SERVICE_VERSION)
                .deploymentName(AZURE_OPENAI_DEPLOYMENT_NAME)
                .temperature(AZURE_OPENAI_TEMPERATURE)
                .topP(AZURE_OPENAI_TOP_P)
                .timeout(Duration.ofSeconds(AZURE_OPENAI_TIMEOUT_SECONDS))
                .maxRetries(AZURE_OPENAI_MAX_RETRIES)
                .logRequestsAndResponses(AZURE_OPENAI_LOG_REQUESTS_AND_RESPONSES)
                .build();
    }
}
