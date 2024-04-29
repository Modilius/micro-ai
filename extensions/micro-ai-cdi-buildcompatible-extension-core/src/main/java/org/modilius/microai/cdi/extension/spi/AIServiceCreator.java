package org.modilius.microai.cdi.extension.spi;

import dev.langchain4j.service.AiServices;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.build.compatible.spi.Parameters;
import jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanCreator;

public class AIServiceCreator implements SyntheticBeanCreator<Object> {
    @Override
    public Object create(Instance<Object> lookup, Parameters params) {
        Class interfaceClass = params.get("interfaceClass", Class.class);
        RegisterAIService annotation = (RegisterAIService) interfaceClass.getAnnotation(RegisterAIService.class);
        try {
            return AiServices.builder(interfaceClass)
                    .chatLanguageModel(annotation.model().getConstructor().newInstance())
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
