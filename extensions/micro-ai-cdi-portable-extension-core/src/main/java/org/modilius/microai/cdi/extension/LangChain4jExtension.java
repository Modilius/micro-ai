package org.modilius.microai.cdi.extension;

import jakarta.enterprise.inject.spi.Extension;

import java.util.HashSet;
import java.util.Set;

public class LangChain4jExtension implements Extension {
    private static final Set<String> detectedAIServicesDeclaredInterfaces = new HashSet<>();
    public static Set<String> getDetectedAIServicesDeclaredInterfaces() {
        return detectedAIServicesDeclaredInterfaces;
    }

}
