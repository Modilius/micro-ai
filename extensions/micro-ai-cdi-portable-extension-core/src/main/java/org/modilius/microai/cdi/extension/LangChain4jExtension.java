package org.modilius.microai.cdi.extension;

import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.AfterBeanDiscovery;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.enterprise.inject.spi.ProcessAnnotatedType;
import jakarta.enterprise.inject.spi.WithAnnotations;
import org.jboss.logging.Logger;
import org.modilius.microai.cdi.extension.spi.RegisterAIService;

import java.util.HashSet;
import java.util.Set;


public class LangChain4jExtension implements Extension {
    private static final Logger LOGGER = Logger.getLogger(LangChain4jExtension.class);
    private static final Set<Class<?>> detectedAIServicesDeclaredInterfaces = new HashSet<>();
    public static Set<Class<?>> getDetectedAIServicesDeclaredInterfaces() {
        return detectedAIServicesDeclaredInterfaces;
    }

    <T> void processAnnotatedType(@Observes @WithAnnotations({RegisterAIService.class}) ProcessAnnotatedType<T> pat) {
        if ( pat.getAnnotatedType().getJavaClass().isInterface()) {
            LOGGER.info("processAnnotatedType register "+pat.getAnnotatedType().getJavaClass().getName());
            detectedAIServicesDeclaredInterfaces.add(pat.getAnnotatedType().getJavaClass());
        } else {
            LOGGER.warn("processAnnotatedType reject "+pat.getAnnotatedType().getJavaClass().getName()+" which is not an interface");
            pat.veto();
        }
    }


    void afterBeanDiscovery(@Observes AfterBeanDiscovery afterBeanDiscovery,BeanManager beanManager) throws ClassNotFoundException {
        for ( Class<?> aiServiceClass : detectedAIServicesDeclaredInterfaces ) {
            LOGGER.info("afterBeanDiscovery create synthetic :  "+aiServiceClass.getName());
            final RegisterAIService annotation = aiServiceClass.getAnnotation(RegisterAIService.class);
            afterBeanDiscovery.addBean()
                    .types(aiServiceClass)
                    .scope(RequestScoped.class)
                    .createWith(creationalContext -> AIServiceCreator.create(creationalContext,annotation,aiServiceClass));
        }
    }
}
