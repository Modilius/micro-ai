package org.modilius.microai.cdi.extension;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import org.modilius.microai.cdi.extension.spi.RegisterAIService;

@RegisterAIService
public interface MyDummyAIService  {
    @SystemMessage("toto")
    @UserMessage("titi")
    String detectFraudForCustomer(@V("name") String name, @V("surname") String surname);

    default String fraudFallback(String name, String surname) {
        throw new RuntimeException(
                "Sorry, I am not able to detect fraud for customer " + name + " " + surname
                        + " at the moment. Please try again later.");
    }
}
