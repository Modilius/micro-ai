package org.modilius.microai.cdi.extension.spi;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(ElementType.TYPE)
public @interface RegisterAIService {
    Class<?>[] tools() default {};
    int chatMemoryMaxMessages() default 10;
}
