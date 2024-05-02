package org.modilius.microai.cdi.extension.spi;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.output.Response;
import jakarta.enterprise.context.RequestScoped;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.List;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(ElementType.TYPE)
public @interface RegisterAIService {

    Class<? extends Annotation> scope() default RequestScoped.class;
    Class<?>[] tools() default {};

    Class<? extends ChatLanguageModel> model() default  DetectChatLanguageModel.class;


    class DetectChatLanguageModel implements ChatLanguageModel {
        @Override
        public Response<AiMessage> generate(List<ChatMessage> list) {
            throw new UnsupportedOperationException("Should not be called");
        }
    }

}
