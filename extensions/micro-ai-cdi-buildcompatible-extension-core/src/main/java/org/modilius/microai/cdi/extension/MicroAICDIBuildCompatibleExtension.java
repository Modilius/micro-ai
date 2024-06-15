package org.modilius.microai.cdi.extension;

import jakarta.enterprise.inject.build.compatible.spi.BuildCompatibleExtension;
import jakarta.enterprise.inject.build.compatible.spi.ClassConfig;
import jakarta.enterprise.inject.build.compatible.spi.Enhancement;
import jakarta.enterprise.lang.model.declarations.ClassInfo;
import org.jboss.logging.Logger;
import org.modilius.microai.cdi.extension.spi.RegisterAIService;

import java.util.HashSet;
import java.util.Set;

public class MicroAICDIBuildCompatibleExtension implements BuildCompatibleExtension {
    private static final Logger LOGGER = Logger.getLogger(MicroAICDIBuildCompatibleExtension.class);
    private static final Set<String> detectedAIServicesDeclaredInterfaces = new HashSet<>();
    public static Set<String> getDetectedAIServicesDeclaredInterfaces() {
        return detectedAIServicesDeclaredInterfaces;
    }


    @Enhancement(types = Object.class, withAnnotations = RegisterAIService.class,withSubtypes = true)
    public void detectRegisterAIService(ClassConfig classConfig) {
        ClassInfo classInfo = classConfig.info();
        LOGGER.info("Detect new AIService from Enhancement {} " + classInfo.name());
        detectedAIServicesDeclaredInterfaces.add(classInfo.name());
    }

}
