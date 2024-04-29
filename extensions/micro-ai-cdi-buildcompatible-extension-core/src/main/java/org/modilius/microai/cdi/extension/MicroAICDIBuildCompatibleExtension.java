package org.modilius.microai.cdi.extension;

import jakarta.enterprise.inject.build.compatible.spi.BuildCompatibleExtension;
import jakarta.enterprise.inject.build.compatible.spi.Discovery;
import jakarta.enterprise.inject.build.compatible.spi.Enhancement;
import jakarta.enterprise.inject.build.compatible.spi.Messages;
import jakarta.enterprise.inject.build.compatible.spi.MetaAnnotations;
import jakarta.enterprise.inject.build.compatible.spi.ScannedClasses;
import jakarta.enterprise.inject.build.compatible.spi.Synthesis;
import jakarta.enterprise.inject.build.compatible.spi.SyntheticComponents;
import jakarta.enterprise.inject.build.compatible.spi.Types;
import jakarta.enterprise.lang.model.declarations.ClassInfo;
import org.jboss.logging.Logger;
import org.modilius.microai.cdi.extension.spi.RegisterAIService;

import java.util.ArrayList;
import java.util.List;

public class MicroAICDIBuildCompatibleExtension  implements BuildCompatibleExtension {
    private static final Logger LOGGER = Logger.getLogger(MicroAICDIBuildCompatibleExtension.class);
    private static List<String> detectedAIServicesDeclaredInterfaces = new ArrayList<>();

    public static List<String> getDetectedAIServicesDeclaredInterfaces() {
        return detectedAIServicesDeclaredInterfaces;
    }

    @Discovery
    public void discovery(ScannedClasses scannedClasses, MetaAnnotations metaAnnotations) {
        LOGGER.info("===> Call @Discovery");
        //scannedClasses.add(IgniteInstanceProducer.class.getName());
    }

    @Enhancement(types = Object.class, withAnnotations = RegisterAIService.class,withSubtypes = true)
    public void detectRegisterAIService(ClassInfo classInfo) {
        LOGGER.info("Detect new AIService "+classInfo.name());
        detectedAIServicesDeclaredInterfaces.add(classInfo.name());
    }


    @Synthesis
    public void synthesis(SyntheticComponents syntheticComponents, Types types, Messages messages) {
        //syntheticComponents.addBean(RegisterAIService.class)

    }

}