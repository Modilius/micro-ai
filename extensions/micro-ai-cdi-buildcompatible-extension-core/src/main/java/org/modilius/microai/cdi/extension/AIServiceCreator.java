package org.modilius.microai.cdi.extension;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.service.AiServices;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.build.compatible.spi.Parameters;
import jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanCreator;
import org.modilius.microai.cdi.extension.spi.RegisterAIService;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AIServiceCreator implements SyntheticBeanCreator<Object> {
    @Override
    public Object create(Instance<Object> lookup, Parameters params) {
        Class<?> interfaceClass = params.get(MicroAICDIBuildCompatibleExtension.PARAM_INTERFACE_CLASS, Class.class);
        RegisterAIService annotation = interfaceClass.getAnnotation(RegisterAIService.class);
        try {
            List<Object> tools = Stream.of(annotation.tools())
                    .map(c -> lookup.select(c).get())
                    .collect(Collectors.toList());
            //
            AiServices<?> aiServices = AiServices.builder(interfaceClass)
                    .chatLanguageModel(lookup.select(ChatLanguageModel.class).get())
                    .tools(tools)
                    .chatMemory(MessageWindowChatMemory.withMaxMessages(annotation.chatMemoryMaxMessages()));
            //
            Instance<ContentRetriever> contentRetrievers = lookup.select(ContentRetriever.class);
            if (contentRetrievers.isResolvable())
                aiServices.contentRetriever(contentRetrievers.get());
            return aiServices.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
