package org.modilius.microai.cdi.extension;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import org.modilius.microai.cdi.extension.spi.RegisterAIService;

@SuppressWarnings("CdiManagedBeanInconsistencyInspection")
@RegisterAIService
public interface MyDummyAIService2 {
    @SystemMessage("toto")
    @UserMessage("titi")
    String test(@V("name") String name, @V("surname") String surname);

}
