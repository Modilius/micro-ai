package org.modilius.microai.cdi.extension;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.output.Response;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldJunit5Extension;
import org.jboss.weld.junit5.WeldSetup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.List;
import java.util.concurrent.Callable;

@ExtendWith(WeldJunit5Extension.class)
public class ExtensionTest {
    @Inject
    MyDummyAIService myDummyAIService;

    @Inject
    MyDummyAIService2 myDummyAIService2;

    @Inject
    RequestContextCaller requestContextCaller;

    @WeldSetup
    public WeldInitiator weld =
            WeldInitiator.from(
                            WeldInitiator
                                    .createWeld()
                                    .enableDiscovery()
                                    .addBeanClasses(DummyBean.class,RequestContextCaller.class))
                    .build();
    @RequestScoped
    public static class DummyBean {}

    @Test
    void detectAIServiceInterface() {
        Assertions.assertTrue(
                LangChain4jExtension
                        .getDetectedAIServicesDeclaredInterfaces()
                        .contains(MyDummyAIService.class)
        );
        Assertions.assertTrue(
                LangChain4jExtension
                        .getDetectedAIServicesDeclaredInterfaces()
                        .contains(MyDummyAIService2.class)
        );
    }


    @Test
    void ensureInjectAndScope() {
        Assertions.assertNotNull(myDummyAIService);
        Assertions.assertNotNull(myDummyAIService2);
        Assertions.assertNotNull(requestContextCaller.runInRequestScope(()->myDummyAIService2.toString()));
    }

    @Dependent
    public static class DummyChatLanguageModel implements ChatLanguageModel {
        @Override
        public Response<AiMessage> generate(List<ChatMessage> list) {
            return null;
        }
    }

    @ActivateRequestContext
    public static class RequestContextCaller {
        public <T> T runInRequestScope(Callable<T> callable) {
            try {
                return callable.call();
            } catch (Exception e) {
                throw new UndeclaredThrowableException(e);
            }
        }
    }

}
