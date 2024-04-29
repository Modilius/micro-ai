package org.modilius.microai.cdi.extension;

import jakarta.inject.Inject;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldJunit5Extension;
import org.jboss.weld.junit5.WeldSetup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(WeldJunit5Extension.class)
public class DetectAIServiceInterfacesTest {
    @Inject
    DummyBean dummyBean;

    @WeldSetup
    public WeldInitiator weld = WeldInitiator.from(
            DummyBean.class,
            MyDummyAIService.class
    ).build();

    @Test
    void detectAIServiceInterface() {
        Assertions.assertTrue(
                MicroAICDIBuildCompatibleExtension
                        .getDetectedAIServicesDeclaredInterfaces()
                        .contains(MyDummyAIService.class.getName())
        );
    }

}
