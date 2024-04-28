package io.jefrajames.booking;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.azure.AzureOpenAiChatModel;
import dev.langchain4j.service.AiServices;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class FraudAiFactory {

    @Inject
    private AzureOpenAiChatModel model;

    @Inject
    @ConfigProperty(name = "fraud.memory.max.messages", defaultValue = "5")
    private Integer memoryMaxMessages;

    @Inject
    private BookingService bookingService;

    @Produces
    public FraudAiService getFraudAiService() {

        return AiServices.builder(FraudAiService.class)
                .chatLanguageModel(model)
                .tools(bookingService)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(memoryMaxMessages))
                .build();
    }

}
