package org.modilius.microai.cdi.extension.plugins.azure.openapi;

import dev.langchain4j.model.azure.AzureOpenAiChatModel;
import dev.langchain4j.model.chat.ChatLanguageModel;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.build.compatible.spi.BuildCompatibleExtension;
import jakarta.enterprise.inject.build.compatible.spi.Synthesis;
import jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanBuilder;
import jakarta.enterprise.inject.build.compatible.spi.SyntheticComponents;
import org.jboss.logging.Logger;

public class AzureOpenAPIExtension implements BuildCompatibleExtension {
    private static final Logger LOGGER = Logger.getLogger(AzureOpenAPIExtension.class);

    @Synthesis
    public void synthesis(SyntheticComponents syntheticComponents) {
        LOGGER.info("Create synthetic AzureOpenAiChatModel");
        SyntheticBeanBuilder<AzureOpenAiChatModel> builder = syntheticComponents.addBean(AzureOpenAiChatModel.class);
        builder.createWith(AzureOpenAiChatModelCreator.class)
                .type(AzureOpenAiChatModel.class)
                .type(ChatLanguageModel.class)
                .name("AzureOpenAiChatModel") //Quarkus mandatory instead of unremovable
                .scope(ApplicationScoped.class);
    }

}