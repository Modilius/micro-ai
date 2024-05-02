package org.modilius.microai.cdi.extension;

import jakarta.enterprise.inject.build.compatible.spi.BuildCompatibleExtension;
import jakarta.enterprise.inject.build.compatible.spi.Enhancement;
import jakarta.enterprise.inject.build.compatible.spi.Messages;
import jakarta.enterprise.inject.build.compatible.spi.Synthesis;
import jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanBuilder;
import jakarta.enterprise.inject.build.compatible.spi.SyntheticComponents;
import jakarta.enterprise.inject.build.compatible.spi.Types;
import jakarta.enterprise.lang.model.declarations.ClassInfo;
import org.jboss.logging.Logger;
import org.modilius.microai.cdi.extension.spi.AIServiceCreator;
import org.modilius.microai.cdi.extension.spi.RegisterAIService;

import java.util.HashSet;
import java.util.Set;

public class MicroAICDIBuildCompatibleExtension implements BuildCompatibleExtension {
    private static final Logger LOGGER = Logger.getLogger(MicroAICDIBuildCompatibleExtension.class);
    private static Set<String> detectedAIServicesDeclaredInterfaces = new HashSet<>();

    public static Set<String> getDetectedAIServicesDeclaredInterfaces() {
        return detectedAIServicesDeclaredInterfaces;
    }

    @Enhancement(types = Object.class, withAnnotations = RegisterAIService.class, withSubtypes = true)
    public void detectRegisterAIService(ClassInfo classInfo) {
        LOGGER.info("Detect new AIService " + classInfo.name());
        detectedAIServicesDeclaredInterfaces.add(classInfo.name());
    }

    @Synthesis
    public void synthesis(SyntheticComponents syntheticComponents, Types types, Messages messages) throws ClassNotFoundException {
        //syntheticComponents.addBean(RegisterAIService.class)
        for (String interfaceName : detectedAIServicesDeclaredInterfaces) {
            LOGGER.info("Create synthetic " + interfaceName);
            Class<?> interfaceClass = Class.forName(interfaceName);
            RegisterAIService annotation = interfaceClass.getAnnotation(RegisterAIService.class);
            //
            SyntheticBeanBuilder builder = syntheticComponents.addBean(interfaceClass);
            builder = builder.createWith(AIServiceCreator.class);
            builder = builder.type(interfaceClass);
            builder = builder.scope(annotation.scope());
            builder = builder.withParam("interfaceClass", interfaceClass);

        }
    }

}