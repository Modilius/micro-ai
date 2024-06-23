package org.modilius.microai.cdi.extension;

import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.enterprise.inject.spi.ProcessAnnotatedType;
import jakarta.enterprise.inject.spi.WithAnnotations;
import org.jboss.logging.Logger;
import org.modilius.microai.cdi.extension.spi.RegisterAIService;

import java.util.HashSet;
import java.util.Set;


public class LangChain4jExtension implements Extension {
    private static final Logger LOGGER = Logger.getLogger(LangChain4jExtension.class);
    private static final Set<String> detectedAIServicesDeclaredInterfaces = new HashSet<>();
    public static Set<String> getDetectedAIServicesDeclaredInterfaces() {
        return detectedAIServicesDeclaredInterfaces;
    }

    <T> void processAnnotatedType(@Observes @WithAnnotations({RegisterAIService.class}) ProcessAnnotatedType<T> pat) {
        if ( pat.getAnnotatedType().getJavaClass().isInterface()) {
            LOGGER.info("processAnnotatedType register "+pat.getAnnotatedType().getJavaClass().getName());
            detectedAIServicesDeclaredInterfaces.add(pat.getAnnotatedType().getJavaClass().getName());
        } else {
            LOGGER.warn("processAnnotatedType reject "+pat.getAnnotatedType().getJavaClass().getName()+" wich is not an interface");
            pat.veto();
        }
    }



}
