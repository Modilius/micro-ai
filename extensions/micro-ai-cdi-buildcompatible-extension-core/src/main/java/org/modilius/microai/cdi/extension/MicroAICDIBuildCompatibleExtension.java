package org.modilius.microai.cdi.extension;

import jakarta.enterprise.inject.build.compatible.spi.BeanInfo;
import jakarta.enterprise.inject.build.compatible.spi.BuildCompatibleExtension;
import jakarta.enterprise.inject.build.compatible.spi.Discovery;
import jakarta.enterprise.inject.build.compatible.spi.Enhancement;
import jakarta.enterprise.inject.build.compatible.spi.Messages;
import jakarta.enterprise.inject.build.compatible.spi.MetaAnnotations;
import jakarta.enterprise.inject.build.compatible.spi.Registration;
import jakarta.enterprise.inject.build.compatible.spi.ScannedClasses;
import jakarta.enterprise.inject.build.compatible.spi.Synthesis;
import jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanBuilder;
import jakarta.enterprise.inject.build.compatible.spi.SyntheticComponents;
import jakarta.enterprise.lang.model.AnnotationInfo;
import jakarta.enterprise.lang.model.declarations.ClassInfo;
import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.AnnotationTarget;
import org.jboss.jandex.DotName;
import org.jboss.jandex.Index;
import org.jboss.jandex.IndexReader;
import org.jboss.logging.Logger;
import org.modilius.microai.cdi.extension.spi.AIServiceCreator;
import org.modilius.microai.cdi.extension.spi.RegisterAIService;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class MicroAICDIBuildCompatibleExtension implements BuildCompatibleExtension {
    private static final Logger LOGGER = Logger.getLogger(MicroAICDIBuildCompatibleExtension.class);
    private static final Set<String> detectedAIServicesDeclaredInterfaces = new HashSet<>();
    public static final String PARAM_INTERFACE_CLASS = "interfaceClass";

    public static Set<String> getDetectedAIServicesDeclaredInterfaces() {
        return detectedAIServicesDeclaredInterfaces;
    }


    @SuppressWarnings("unused")
    @Discovery
    public void discoverRegisterAIServiceAnnotatedServices(ScannedClasses scannedClasses, Messages messages, MetaAnnotations metaAnnotations) throws IOException {
        LOGGER.info("Core ext");
        Enumeration<URL> resources = this.getClass().getClassLoader().getResources("META-INF/jandex.idx");
        while (resources.hasMoreElements()) {
            try (InputStream input = resources.nextElement().openStream()) {
                IndexReader indexReader = new IndexReader(input);
                Index index = indexReader.read();

                DotName registerAiServiceDotName = DotName.createSimple(RegisterAIService.class);
                List<AnnotationInstance> annotations = index.getAnnotations(registerAiServiceDotName);

                for (AnnotationInstance annotation : annotations) {
                    if (Objects.requireNonNull(annotation.target().kind()) == AnnotationTarget.Kind.CLASS) {
                        String detectedClass = annotation.target().toString();
                        messages.info("Detect new AIService " + detectedClass);
                        scannedClasses.add(detectedClass);
                    }
                }
            }

        }
    }

    @SuppressWarnings("unused")
    @Enhancement(types = Object.class, withAnnotations = RegisterAIService.class, withSubtypes = true)
    public void detectRegisterAIService(ClassInfo classInfo) {
        // ajout opentrace
        LOGGER.info("Detect new AIService " + classInfo.name());
        detectedAIServicesDeclaredInterfaces.add(classInfo.name());
    }

    @SuppressWarnings("unused")
    @Registration(types = Object.class)
    public void registration(BeanInfo beanInfo, Messages messages) {
        AnnotationInfo annotation = beanInfo.declaringClass().annotation(RegisterAIService.class);
        if (annotation != null)
            messages.info(" ===>Registered bean for " + beanInfo.name());
    }

    @SuppressWarnings({"unused", "unchecked"})
    @Synthesis
    public void synthesis(SyntheticComponents syntheticComponents) throws ClassNotFoundException {
        LOGGER.info("Synthesis");

        for (String interfaceName : detectedAIServicesDeclaredInterfaces) {
            LOGGER.info("Create synthetic " + interfaceName);
            Class<?> interfaceClass = Class.forName(interfaceName);
            RegisterAIService annotation = interfaceClass.getAnnotation(RegisterAIService.class);

            SyntheticBeanBuilder<Object> builder = (SyntheticBeanBuilder<Object>) syntheticComponents.addBean(interfaceClass);
            builder.createWith(AIServiceCreator.class)
                    .type(interfaceClass)
                    .scope(annotation.scope())
                    .withParam(PARAM_INTERFACE_CLASS, interfaceClass);

        }
    }

}