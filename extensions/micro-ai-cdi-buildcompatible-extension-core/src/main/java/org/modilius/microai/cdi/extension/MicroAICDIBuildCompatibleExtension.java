package org.modilius.microai.cdi.extension;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.build.compatible.spi.BuildCompatibleExtension;
import jakarta.enterprise.inject.build.compatible.spi.ClassConfig;
import jakarta.enterprise.inject.build.compatible.spi.Enhancement;
import jakarta.enterprise.inject.build.compatible.spi.Synthesis;
import jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanBuilder;
import jakarta.enterprise.inject.build.compatible.spi.SyntheticComponents;
import jakarta.enterprise.inject.literal.NamedLiteral;
import jakarta.enterprise.lang.model.declarations.ClassInfo;
import jakarta.inject.Named;
import org.jboss.logging.Logger;
import org.modilius.microai.cdi.extension.spi.RegisterAIService;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class MicroAICDIBuildCompatibleExtension implements BuildCompatibleExtension {
    private static final Logger LOGGER = Logger.getLogger(MicroAICDIBuildCompatibleExtension.class);
    private static final Set<Class<?>> detectedAIServicesDeclaredInterfaces = new HashSet<>();
    private static final Set<String> tools = new HashSet<>();
    public static final int FIRST = 100;
    public static final int SECOND = 500;

    public static Set<Class<?>> getDetectedAIServicesDeclaredInterfaces() {
        return detectedAIServicesDeclaredInterfaces;
    }


    @SuppressWarnings("unused")
    @Enhancement(types = Object.class, withSubtypes = true)
    @Priority(SECOND)
    public void fixToolsWithNamed(ClassConfig classConfig) throws ClassNotFoundException {
        if (tools.contains(classConfig.info().name())) {
            Class<?> toolClass = getLoadClass(classConfig.info().name());
            if (toolClass.getAnnotation(Named.class) == null) {
                classConfig.addAnnotation(NamedLiteral.of("xxx-" + classConfig.info().name()));
                LOGGER.info("Add a Name to " + classConfig.info().name());
            } else {
                LOGGER.info("No need to add Name to " + classConfig.info().name());
            }
        }
    }


    @SuppressWarnings("unused")
    @Enhancement(types = Object.class, withAnnotations = RegisterAIService.class, withSubtypes = true)
    @Priority(FIRST)
    public void detectRegisterAIService(ClassConfig classConfig) throws ClassNotFoundException {
        ClassInfo classInfo = classConfig.info();
        String className = classInfo.name();
        LOGGER.info("Analyze from Ehancement " + className);
        if (classInfo.isInterface()) {
            Class<?> interfaceClass = getLoadClass(className);
            detectedAIServicesDeclaredInterfaces.add(interfaceClass);
            RegisterAIService annotation = interfaceClass.getAnnotation(RegisterAIService.class);
            tools.addAll(Arrays.stream(annotation.tools()).map(Class::getName).collect(Collectors.toList()));
            LOGGER.info("Detected tools : " + tools);
        } else {
            LOGGER.warn("The class is Annotated with @RegisterAIService, but only interface are allowed" + classConfig.info());
        }
    }

    private static Class<?> getLoadClass(String className) throws ClassNotFoundException {
        return Thread.currentThread().getContextClassLoader().loadClass(className);
    }

    public static final String PARAM_INTERFACE_CLASS = "interfaceClass";

    @SuppressWarnings({"unchecked", "unused"})
    @Synthesis
    public void synthesis(SyntheticComponents syntheticComponents) throws ClassNotFoundException {
        LOGGER.info("Synthesis");
        for (Class<?> interfaceClass : detectedAIServicesDeclaredInterfaces) {
            LOGGER.info("Create synthetic " + interfaceClass);
            SyntheticBeanBuilder<Object> builder = (SyntheticBeanBuilder<Object>) syntheticComponents.addBean(interfaceClass);
            builder.createWith(AIServiceCreator.class)
                    .type(interfaceClass)
                    .scope(RequestScoped.class)
                    .withParam(PARAM_INTERFACE_CLASS, interfaceClass);
        }
    }
}
