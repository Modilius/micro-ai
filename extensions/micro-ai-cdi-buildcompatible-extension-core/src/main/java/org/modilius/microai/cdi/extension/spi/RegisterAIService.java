package org.modilius.microai.cdi.extension.spi;

import jakarta.enterprise.context.RequestScoped;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(ElementType.TYPE)
//@Stereotype // need to detect interfaces with this annotation as annotated-type
public @interface RegisterAIService {

    Class<? extends Annotation> scope() default RequestScoped.class;
    Class<?>[] tools() default {};

    int chatMemoryMaxMessages() default 10;


}
