package org.modilius.microai.cdi.extension.spi;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.service.AiServices;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.build.compatible.spi.Parameters;
import jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanCreator;
import jakarta.enterprise.inject.spi.CDI;
import org.modilius.microai.cdi.extension.MicroAICDIBuildCompatibleExtension;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AIServiceCreator implements SyntheticBeanCreator<Object> {
    @Override
    public Object create(Instance<Object> lookup, Parameters params) {
        Class<?> interfaceClass = params.get(MicroAICDIBuildCompatibleExtension.PARAM_INTERFACE_CLASS, Class.class);
        RegisterAIService annotation = interfaceClass.getAnnotation(RegisterAIService.class);

        CDI<Object> cdi = CDI.current();
        ChatLanguageModel chatLanguageModel = cdi.select(ChatLanguageModel.class).get();

        try {
            AiServices<?> aiServices = AiServices.builder(interfaceClass)
                    .chatLanguageModel(chatLanguageModel)
                    .tools(Stream.of(annotation.tools())
                            .map(c->cdi.select(c).get())
                            .collect(Collectors.toList()))
                    .chatMemory(MessageWindowChatMemory.withMaxMessages(annotation.chatMemoryMaxMessages()));

            Instance<ContentRetriever> contentRetrievers = cdi.select(ContentRetriever.class);
            if (contentRetrievers.isResolvable())
                aiServices.contentRetriever(contentRetrievers.get());

            return aiServices.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
