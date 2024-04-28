package io.jefrajames.booking;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.azure.AzureOpenAiChatModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.service.AiServices;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class ChatAiFactory {

    @Inject
    private BookingService bookingService;

    @Inject
    private ContentRetriever retriever;

    @Inject
    private AzureOpenAiChatModel model;

    @Inject
    @ConfigProperty(name = "chat.memory.max.messages", defaultValue = "10")
    private Integer memoryMaxMessages;

    @Produces
    public ChatAiService getChatAiService() {

        return AiServices.builder(ChatAiService.class)
                .chatLanguageModel(model)
                .tools(bookingService)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(memoryMaxMessages))
                .contentRetriever(retriever)
                .build();
    }

}
