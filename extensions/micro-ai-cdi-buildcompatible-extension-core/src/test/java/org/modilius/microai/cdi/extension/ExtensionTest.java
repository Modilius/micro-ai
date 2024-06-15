package org.modilius.microai.cdi.extension;

import jakarta.enterprise.context.RequestScoped;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldJunit5Extension;
import org.jboss.weld.junit5.WeldSetup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(WeldJunit5Extension.class)
public class ExtensionTest {
    @WeldSetup
    public WeldInitiator weld =
            WeldInitiator.from(
                            WeldInitiator
                                    .createWeld()
                                    .enableDiscovery()
                                    .addBeanClasses(DummyBean.class))
                    .build();
    @RequestScoped
    public static class DummyBean {}

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
                        .contains(MyDummyAIService2.class.getName())
        );
    }


}
