package org.modilius.microai.cdi.extension;

import io.smallrye.config.inject.ConfigExtension;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.inject.Inject;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldJunit5Extension;
import org.jboss.weld.junit5.WeldSetup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.Annotation;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.concurrent.Callable;

@ExtendWith(WeldJunit5Extension.class)
public class ExtensionTest {

    @Inject
    MyDummyAIService myDummyAIService;

    @Inject
    MyDummyApplicationScopedAIService myDummyApplicationScopedAIService;

    @Inject
    RequestContextCaller requestContextCaller;

    @Inject
    BeanManager beanManager;

    @WeldSetup
    public WeldInitiator weld = WeldInitiator.from(
                    MyDummyAIService.class,
                    MyDummyApplicationScopedAIService.class,
                    RequestContextCaller.class,
                    DummyChatLanguageModel.class,
                    ConfigExtension.class
            )
            .build();

    @Test
    void detectAIServiceInterface() {
        Assertions.assertTrue(
                MicroAICDIBuildCompatibleExtension
                        .getDetectedAIServicesDeclaredInterfaces()
                        .contains(MyDummyAIService.class.getName())
        );
        Assertions.assertTrue(
                MicroAICDIBuildCompatibleExtension
                        .getDetectedAIServicesDeclaredInterfaces()
                        .contains(MyDummyApplicationScopedAIService.class.getName())
        );
    }

    @Test
    void ensureInjectAndScope() {
        Assertions.assertNotNull(myDummyAIService);
        Assertions.assertNotNull(myDummyApplicationScopedAIService);
        assertBeanScope(MyDummyAIService.class, RequestScoped.class);
        assertBeanScope(MyDummyApplicationScopedAIService.class, ApplicationScoped.class);
    }

    @Test
    void callEffectiveCreation() {
        Assertions.assertNotNull(requestContextCaller.run(() -> myDummyAIService.toString()));

    }

    @ActivateRequestContext
    public static class RequestContextCaller {
        public <T> T run(Callable<T> callable) {
            try {
                return callable.call();
            } catch (Exception e) {
                throw new UndeclaredThrowableException(e);
            }
        }
    }

    private void assertBeanScope(Class<?> beanType, Class<?> scopedClass) {
        Class<? extends Annotation> scope = beanManager.getBeans(beanType).iterator().next().getScope();
        Assertions.assertTrue(scope.isAssignableFrom(scopedClass));
    }

}
