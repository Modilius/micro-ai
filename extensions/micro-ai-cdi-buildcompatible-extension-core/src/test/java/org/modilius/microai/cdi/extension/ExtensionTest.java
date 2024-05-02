package org.modilius.microai.cdi.extension;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.inject.Inject;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldJunit5Extension;
import org.jboss.weld.junit5.WeldSetup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.Annotation;

@ExtendWith(WeldJunit5Extension.class)
public class ExtensionTest {

    @Inject
    MyDummyAIService myDummyAIService;

    @Inject
    MyDummyApplicationScopedAIService myDummyApplicationScopedAIService;

    @Inject
    BeanManager beanManager;

    @WeldSetup
    public WeldInitiator weld = WeldInitiator.from(
            MyDummyAIService.class,
            MyDummyApplicationScopedAIService.class
    ).build();

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

    private void assertBeanScope(Class<?> beanType, Class<?> scopedClass) {
        Class<? extends Annotation> scope = beanManager.getBeans(beanType).iterator().next().getScope();
        Assertions.assertTrue(scope.isAssignableFrom(scopedClass));
    }

}
